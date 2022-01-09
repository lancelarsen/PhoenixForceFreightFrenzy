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

        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(-25, -62, Math.toRadians(180)));

        // --- Move to the duck spinner
        drive.line(new Pose2d(-50, -45, Math.toRadians(180)));
        drive.turn(180);
        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
        drive.line(new Pose2d(-62, -55, Math.toRadians(0)));
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        // //--- Spin the duck!
        appendages.enableDuckWheels(true);
        sleep(4000);
        appendages.enableDuckWheels(false);

        // --- Move to deploy -- changes based on deploy level
        drive.line(new Pose2d(-50, -17, Math.toRadians(95)));

        // --- Deploy to top!
        appendages.gondalaHigh();
        sleep(2000);
        appendages.extakeGondola();
        appendages.gondalaDown();

        // --- Park
        drive.line(new Pose2d(-68, -35, Math.toRadians(90)));

        // Future ideas?
        // -- get duck and deliver it?

        sleep(4000);
    }
}