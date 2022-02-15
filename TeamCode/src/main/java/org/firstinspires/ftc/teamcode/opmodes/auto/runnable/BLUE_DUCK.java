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

import org.firstinspires.ftc.teamcode.vision.BarcodeVision;

@Autonomous(group = "auto")
public class BLUE_DUCK extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex();
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-27, 62, 0));

        appendages.enableDuckWheels(true);

        // --- Move to the duck spinner
        drive.line(new Pose2d(-50, 45, 0));
        drive.turn(180);
        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
        drive.line(new Pose2d(-62, 55, Math.toRadians(-180)));
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        // --- Spin the duck!
        sleep(3000);
        appendages.enableDuckWheels(false);

        // --- Move to deploy -- changes based on deploy level
        appendages.setGatesUp();
        switch (barcodePlace) {
            case 3:
                drive.line(new Pose2d(-50, 19, Math.toRadians(82)));
                appendages.gondalaHigh();
                break;
            case 2:
                drive.line(new Pose2d(-50, 19, Math.toRadians(82)));
                appendages.gondalaMiddle();
                sleep(1500);
                drive.line(new Pose2d(-32, 21, Math.toRadians(90)));
                break;
            case 1:
                drive.line(new Pose2d(-50, 19, Math.toRadians(82)));
                appendages.gondalaLow();
                drive.line(new Pose2d(-20, 21, Math.toRadians(82)));
                break;
        }

        // --- Deploy!
        sleep(1500);
        switch (barcodePlace) {
            case 1:
                appendages.gondolaOpen();
                sleep(1000);
                drive.line(new Pose2d(-30, 21, Math.toRadians(82)));
                break;
            default:
                appendages.extakeGondola();
                break;
        }

        sleep(500);
        appendages.gondalaDown();
        appendages.setGatesDown();

        // --- Park
        switch (barcodePlace) {
            case 3:
                drive.line(new Pose2d(-68, 35, Math.toRadians(90)));
                break;
            case 2:
                drive.line(new Pose2d(-68, 35, Math.toRadians(90)));
                break;
            case 1:
                drive.line(new Pose2d(-68, 37, Math.toRadians(90)));
                break;
        }

        appendages.gondolaClosed();

        sleep(4000);
    }
}