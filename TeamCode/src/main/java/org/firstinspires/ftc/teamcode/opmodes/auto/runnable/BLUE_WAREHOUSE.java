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
public class BLUE_WAREHOUSE extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);

        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(19, 62, 0));

        appendages.intakeBlocksStart();

        int backwall = 62;
        for (int i = 0; i < 3; i++) {
            // --- Move to deploy
            drive.line(new Pose2d(-5, backwall, 0));

            // --- Deploy to top!
            appendages.gondalaHigh();
            sleep(2000);
            appendages.extakeGondola();
            appendages.gondalaDown();

            // --- Move to blocks
            drive.line(new Pose2d(55, backwall, 0));

            // --- Collect
            sleep(500); // TODO: code me!

            backwall += 2;
        }

        appendages.intakeBlocksStop();

        sleep(4000);
    }
}