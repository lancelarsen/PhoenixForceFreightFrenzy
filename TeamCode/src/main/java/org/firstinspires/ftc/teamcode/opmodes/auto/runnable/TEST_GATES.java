package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

@Autonomous(group = "auto")
public class TEST_GATES extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.INSIDE);

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex();
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        // drive.setSpeed(MecanumAutonomous.Speed.FAST);
        // drive.setCurrentPosition(new Pose2d(13, -62, Math.toRadians(180)));

        appendages.intakeBlocksStart();

        sleep(1000);

        while (!isStopRequested()) {
            if (appendages.isBlockInGondola()) {
                sleep(1000);
                appendages.gondalaLow();
                appendages.extakeGondola();
                sleep(500);
                appendages.gondalaDown();
                sleep(2000);
            }
        }

        /*
         * while (true) {
         * 
         * appendages.intakeBlocksStart();
         * 
         * boolean isBlock = appendages.isBlockInGondola();
         * if (isBlock) {
         * // appendages.setGatesUp();
         * appendages.intakeBlocksReverse();
         * sleep(1000);
         * appendages.gondalaLow();
         * appendages.extakeGondola();
         * appendages.gondalaDown();
         * sleep(1000);
         * // appendages.setGatesDown();
         * sleep(1000);
         * }
         * 
         * telemetry.addData("isBlock", isBlock);
         * telemetry.addData("Distance", appendages.getBlockDistance());
         * telemetry.update();
         * }
         */

        // appendages.intakeBlocksStop();
    }
}