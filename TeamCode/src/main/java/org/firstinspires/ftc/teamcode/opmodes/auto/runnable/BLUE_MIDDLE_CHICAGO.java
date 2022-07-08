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
public class BLUE_MIDDLE_CHICAGO extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);
        appendages.disableIntakeGates();

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex();
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-27, 62, 0));

        // --- Move to deploy -- changes based on deploy level
        // appendages.setGatesUp();
        switch (barcodePlace) {
            case 3:
                drive.line(new Pose2d(-27, 50, Math.toRadians(34)));
                appendages.gondalaHigh();
                break;
            case 2:
                drive.line(new Pose2d(-24, 35, Math.toRadians(34)));
                appendages.gondalaMiddle();
                break;
            case 1:
                drive.line(new Pose2d(-16, 26, Math.toRadians(34)));
                appendages.gondalaLow();
                break;
        }

        // --- Deploy!
        sleep(1500);
        if (barcodePlace == 1) {
            appendages.gondolaOpen();
        } else {
            appendages.extakeGondola();
        }

        sleep(500);
        appendages.gondalaDown();
        appendages.gondolaClosed();

        drive.line(new Pose2d(-27, 62, 0));

        sleep(4000);
    }
}