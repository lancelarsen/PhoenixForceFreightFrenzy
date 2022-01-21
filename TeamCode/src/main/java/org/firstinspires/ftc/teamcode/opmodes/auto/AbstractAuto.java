package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
import org.firstinspires.ftc.teamcode.appendages.BlinkinPatterns;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.vision.BarcodeVision;

abstract public class AbstractAuto extends LinearOpMode {
    public MecanumAutonomous drive;
    public volatile AppendagesAutonomous appendages;
    public volatile BarcodeVision vision;

    private static final long POWERSHOT_SHOOT_DELAY = 500;
    private static final long POWERSHOT_MOVE_DELAY = 500;

    private static final long LIGHT_LONG_FLASH_ON_TIME = 1500;
    private static final long LIGHT_SHORT_FLASH_ON_TIME = 500;
    private static final long LIGHT_FLASH_OFF_TIME = 250;

    private enum FlashLength {
        LONG,
        SHORT
    }

    public void initAuto(AutoUtils.Alliance alliance, AutoUtils.StartingPosition startingPosition) {
        drive = new MecanumAutonomous(this);
        appendages = new AppendagesAutonomous(this);
        vision = new BarcodeVision(hardwareMap);

        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        vision.init(alliance, startingPosition);

        RevBlinkinLedDriver.BlinkinPattern basePattern;

        if (alliance == AutoUtils.Alliance.BLUE) {
            basePattern = BlinkinPatterns.BLUE_BASE_PATTERN;
        } else {
            basePattern = BlinkinPatterns.RED_BASE_PATTERN;
        }

        updatePregameLights(basePattern);

        if (isStopRequested())
            return;

        vision.setViewportPaused(true);
        appendages.setBlinkinPattern(basePattern);

        // appendages.updateLights(alliance);
        appendages.setBlinkinPattern(basePattern);
    }

    // Runs till opmode start
    private void updatePregameLights(RevBlinkinLedDriver.BlinkinPattern basePattern) {
        Runnable lightTask = () -> {
            while (!Thread.interrupted()) {
                flashLights(basePattern, FlashLength.LONG);

                int capstoneIndex = vision.getCapstoneIndex();
                for (int i = 0; i < capstoneIndex; i++) {
                    flashLights(BlinkinPatterns.CAPSTONE_PATTERN, FlashLength.SHORT);
                }
            }
        };

        Thread lightThread = new Thread(lightTask);
        lightThread.start();

        while (!isStarted())
            ;
        lightThread.interrupt();
    }

    private void flashLights(RevBlinkinLedDriver.BlinkinPattern pattern, FlashLength length) {
        appendages.setBlinkinPattern(pattern);
        sleep(length == FlashLength.LONG ? LIGHT_LONG_FLASH_ON_TIME : LIGHT_SHORT_FLASH_ON_TIME);
        appendages.setBlinkinPattern(BlinkinPatterns.OFF);
        sleep(LIGHT_FLASH_OFF_TIME);
    }
}
