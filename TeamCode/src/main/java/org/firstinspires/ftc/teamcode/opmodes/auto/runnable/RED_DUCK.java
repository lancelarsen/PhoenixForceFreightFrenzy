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
public class RED_DUCK extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex(); // TODO: Max Fix Me??

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-24, -62, Math.toRadians(180)));

        appendages.enableDuckWheels(true);

        // --- Move to the duck spinner
        drive.line(new Pose2d(-50, -45, Math.toRadians(180)));
        drive.turn(180);
        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
        drive.line(new Pose2d(-62, -55, Math.toRadians(0)));
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        // --- Spin the duck!
        sleep(4000);
        appendages.enableDuckWheels(false);

        // --- Move to deploy -- changes based on deploy level
        appendages.setGatesUp();
        switch (barcodePlace) {
            case 3:
                drive.line(new Pose2d(-51, -18, Math.toRadians(90)));
                appendages.gondalaHigh();
                break;
            case 2:
                drive.line(new Pose2d(-32, -21, Math.toRadians(90)));
                appendages.gondalaMiddle();
                break;
            case 1:
                drive.line(new Pose2d(-20, -22, Math.toRadians(90)));
                appendages.gondalaLow();
                break;
        }

        // --- Deploy!
        sleep(1500);
        appendages.extakeGondola();
        sleep(500);
        appendages.gondalaDown();
        appendages.setGatesDown();

        // --- Park
        drive.line(new Pose2d(-68, -35, Math.toRadians(90)));

        sleep(4000);
    }
}