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
public class RED_DUCK_CHICAGO extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OTHER);
        appendages.disableIntakeGates();

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-27, -62, Math.toRadians(180)));

        appendages.enableDuckWheels(true);

        // --- Move to the duck spinner
        drive.line(new Pose2d(-50, -45, Math.toRadians(180)));
        drive.turn(180);
        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
        drive.line(new Pose2d(-62, -55, 0));
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        // --- Spin the duck!
        sleep(3000);
        appendages.enableDuckWheels(false);

        drive.line(new Pose2d(-55, -45, 0));
        drive.line(new Pose2d(-30, -15, Math.toRadians(160)));

        sleep(1000);
        appendages.gondalaMiddle();
        sleep(500);
        appendages.extakeGondola();
        appendages.gondalaDown();

        sleep(500);
        drive.line(new Pose2d(-68, -26, Math.toRadians(-270)));
    }
}