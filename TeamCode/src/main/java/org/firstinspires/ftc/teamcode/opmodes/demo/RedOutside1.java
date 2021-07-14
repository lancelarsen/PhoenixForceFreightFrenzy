package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Disabled
@TeleOp(name = "DEMO Red->Outside 1", group = "Auto")
public class RedOutside1 extends LinearOpMode {

    private static LinearOpMode _opMode;
    private static AppendagesAutonomous _appendages;
    private static RingVision _ringVision;
    BotMecanumDrive _drive;

    private enum Speed
    {
        Fast,
        Medium,
        Slow
    }

    private Pose2d _poseLast = null;

    private void SetStartPosition(Pose2d position)
    {
        Pose2d initialPosition = position;
        _drive.setPoseEstimate(initialPosition);
        _poseLast = initialPosition;
    }

    private void DriveTurn(double angle, Speed speed)
    {
        switch (speed)
        {
            //TODO: Max FIX ME PLEASE!!!
            case Fast: break;
            case Medium: break;
            case Slow: break;
        }

        //_drive.turn(Math.toRadians(angle));
        _poseLast = _poseLast.plus(new Pose2d(0, 0, Math.toRadians(angle)));
    }

    private void DriveCurve(Pose2d position, Speed speed)
    {
        switch (speed)
        {
            //TODO: Max FIX ME PLEASE!!!
            case Fast: break;
            case Medium: break;
            case Slow: break;
        }

        Trajectory trajectory = _drive.trajectoryBuilder(_poseLast)
                .splineTo(new Vector2d(position.getX(), position.getY()), Math.toRadians(position.getHeading())).build();
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    private void DriveCurveFixedHeading(Pose2d position, Speed speed)
    {
        switch (speed)
        {
            //TODO: Max FIX ME PLEASE!!!
            case Fast: break;
            case Medium: break;
            case Slow: break;
        }

        Trajectory trajectory = _drive.trajectoryBuilder(_poseLast)
                        .splineToConstantHeading(new Vector2d(position.getX(), position.getY()), Math.toRadians(position.getHeading())).build();
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    private void DriveStraight(Pose2d position, Speed speed)
    {
        switch (speed)
        {
            //TODO: Max FIX ME PLEASE!!!
            case Fast: break;
            case Medium: break;
            case Slow: break;
        }

        Trajectory trajectory = _drive.trajectoryBuilder(_poseLast)
                .lineToLinearHeading(position).build();
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    @Override
    public void runOpMode()
    {
        _opMode = this;
        _appendages = new AppendagesAutonomous(_opMode);
        _ringVision  = new RingVision(_opMode.hardwareMap);
        _drive = new BotMecanumDrive(hardwareMap);

        SetStartPosition(FieldPositions.RSO);

        waitForStart();
        if (isStopRequested()) return;

        DriveStraight(FieldPositions.RSO_W, Speed.Fast); //--- drive away from wall
        sleep(2000);
        DriveCurve(FieldPositions.L0, Speed.Fast); //--- drive to middle of the white line

//        DriveCurve(FieldPositions.P2A, Speed.Fast); //--- drive to power shot line
//        sleep(1000);
//        DriveStraight(FieldPositions.P2B, Speed.Fast); //--- strafe
//        sleep(1000);
//        DriveStraight(FieldPositions.P2C, Speed.Fast); //--- strafe
//
//        DriveTurn(-90, Speed.Medium); //--- turn right 90 so we're lined up with wobble goal 1
//        DriveStraight(FieldPositions.W1, Speed.Fast); //--- drive to wobble goal
    }
}
