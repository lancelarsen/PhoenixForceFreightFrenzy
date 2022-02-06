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
        int barcodePlace = vision.getCapstoneIndex(); // TODO: Max Fix Me??

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-27, 62, 0));

        // --- Move to the duck spinner
        drive.line(new Pose2d(-50, 45, 0));
        drive.turn(180);
        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
        drive.line(new Pose2d(-62, 55, Math.toRadians(-180)));
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        // --- Spin the duck!
        appendages.enableDuckWheels(true);
        sleep(4000);
        appendages.enableDuckWheels(false);

        // --- Move to deploy -- changes based on deploy level
        switch (barcodePlace) {
            case 3:
                drive.line(new Pose2d(-51, 17, Math.toRadians(85)));
                appendages.gondalaHigh();
                break;
            case 2:
                drive.line(new Pose2d(-35, 19, Math.toRadians(85)));
                appendages.gondalaMiddle();
                break;
            case 1:
                drive.line(new Pose2d(-23, 19, Math.toRadians(85)));
                appendages.gondalaLow();
                break;
        }

        // --- Deploy!
        sleep(1500);
        appendages.extakeGondola();
        sleep(500);
        appendages.gondalaDown();

        // --- Park
        drive.line(new Pose2d(-68, 35, Math.toRadians(90)));

        // Future ideas?
        // -- get duck and deliver it?

        sleep(4000);
    }
}