package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

@Autonomous(group = "auto")
public class FORWARD_TEST extends LinearOpMode {
    public void runOpMode() {
        /*MecanumAutonomous drive = new MecanumAutonomous(this);
        drive.setSpeed(MecanumAutonomous.Speed.FAST);

        waitForStart();

        //initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);
        drive.setCurrentPosition(new Pose2d(0, 0, 0));

        sleep(2 * 1000);
        drive.line(new Pose2d(25, 0, 0));
        sleep(2 * 1000);*/

        MecanumAutonomous drive = new MecanumAutonomous(this);

        Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
        .forward(30)
        .build();

        waitForStart();

        if (isStopRequested()) return;

        sleep(2 * 1000);
        drive.followTrajectory(trajectory);
   }
}