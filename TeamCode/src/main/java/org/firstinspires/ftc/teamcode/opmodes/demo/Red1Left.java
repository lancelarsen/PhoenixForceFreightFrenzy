package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Disabled
@Autonomous(name = "NA LanceAuto", group = "zzz")
public class Red1Left extends LinearOpMode {

    private static LinearOpMode _opMode;
    private static AppendagesAutonomous _appendages;
    private static RingVision _ringVision;
    BotMecanumDrive _drive;
    private enum DriveType
    {
        Curve,
        CurveFixedHeading,
        Straight
    }
    private enum Speed
    {
        Fast,
        Medium,
        Slow
    }
    private enum StartingPosition
    {
        S0, S1, S2, S3, S4, S5
    }
    private enum Position
    {
        S1W, S2W, S3W, S4W,
        R1, R2, R3, R4,
        X0, X1, X2,
        L0, L1, L2,
        W1, W2, W3, W4, W5, W6,
        C1, C2,
        T1, T2, T3, T4, T5, T6,
        P1A, P2A, P3A, P4A,
        P1B, P2B, P3B, P4B,
        P1C, P2C, P3C, P4C
    }

    private Pose2d _poseLast = null;

    private void SetStartPosition(StartingPosition position, double angle)
    {
        double x = 0;
        double y = 0;
        switch(position) {
            case S0: x = 0;     y = 0;   break; //--- center of arena
            case S1: x = -63.5; y = 47;  break; //--- against wall, left blue
            case S2: x = -63.5; y = 11;  break; //--- against wall, right blue
            case S3: x = -63.5; y = -16; break; //--- against wall, left red
            case S4: x = -63.5; y = -61; break; //--- against wall, right red
            case S5: x = -63.5; y = 0;   break; //--- against wall, center
        }

        Pose2d initialPosition = new Pose2d(x, y, Math.toRadians(angle));
        _drive.setPoseEstimate(initialPosition);
        _poseLast = initialPosition;
    }

    private void Turn(double angle, Speed speed)
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

    private void DriveTo(Position position, double angle, DriveType type, Speed speed)
    {
        switch (speed)
        {
            //TODO: Max FIX ME PLEASE!!!
            case Fast: break;
            case Medium: break;
            case Slow: break;
        }

        double x = 0;
        double y = 0;
        switch(position) {
            case S1W: x = -61; y = 47;  break; //--- 2 inches away from wall, left blue
            case S2W: x = -61; y = 11;  break; //--- 2 inches away from wall, right blue
            case S3W: x = -61; y = -16; break; //--- 2 inches away from wall, left red
            case S4W: x = -61; y = -61; break; //--- 2 inches away from wall, right red
            case R1: x = 0; y = 0; break;
            case R2: x = 0; y = 0; break;
            case R3: x = 0; y = 0; break;
            case R4: x = 0; y = 0; break;
            case X0: x = 0; y = 0; break; //--- center of the arena
            case X1: x = 0; y = 0; break;
            case X2: x = 0; y = 0; break;
            case L0: x = 10; y = 0; break; //--- center on white line
            case L1: x = 0; y = 0; break;
            case L2: x = 0; y = 0; break;
            case W1: x = 3; y = 60; break;
            case W2: x = 0; y = 0; break;
            case W3: x = 0; y = 0; break;
            case W4: x = 0; y = 0; break;
            case W5: x = 0; y = 0; break;
            case W6: x = 0; y = 0; break;
            case C1: x = 0; y = 0; break;
            case C2: x = 0; y = 0; break;
            case T1: x = 0; y = 0; break;
            case T2: x = 0; y = 0; break;
            case T3: x = 0; y = 0; break;
            case T4: x = 0; y = 0; break;
            case T5: x = 0; y = 0; break;
            case T6: x = 0; y = 0; break;
            case P1A: x = 0; y = 0; break;
            case P1B: x = 0; y = 0; break;
            case P1C: x = 0; y = 0; break;
            case P2A: x = -4; y = 11; break;
            case P2B: x = -4; y = 4; break;
            case P2C: x = -4; y = -3; break;
            case P3A: x = 0; y = 0; break;
            case P3B: x = 0; y = 0; break;
            case P3C: x = 0; y = 0; break;
            case P4A: x = 0; y = 0; break;
            case P4B: x = 0; y = 0; break;
            case P4C: x = 0; y = 0; break;
        }

        Trajectory trajectory = null;
        switch(type) {
            case Curve:
                trajectory = _drive.trajectoryBuilder(_poseLast)
                    .splineTo(new Vector2d(x, y), Math.toRadians(angle)).build();
            break;
            case CurveFixedHeading:
                trajectory = _drive.trajectoryBuilder(_poseLast)
                    .splineToConstantHeading(new Vector2d(x, y), Math.toRadians(angle)).build();
                break;
            case Straight:
                trajectory = _drive.trajectoryBuilder(_poseLast)
                    .lineToLinearHeading(new Pose2d(x, y, Math.toRadians(angle))).build();
                break;
        }

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

        SetStartPosition(StartingPosition.S2, 0);

        waitForStart();
        if (isStopRequested()) return;

        DriveTo(Position.S2W, 0, DriveType.Straight, Speed.Slow); //--- drive away from wall
        sleep(2000);
        //DriveTo(Position.L0, 0, DriveType.Curve, Speed.Fast); //--- drive to middle of the white line

        DriveTo(Position.P2A, 0, DriveType.Curve, Speed.Fast); //--- drive to power shot line
        sleep(1000);
        DriveTo(Position.P2B, 0, DriveType.Straight, Speed.Fast); //--- strafe
        sleep(1000);
        DriveTo(Position.P2C, 0, DriveType.Straight, Speed.Fast); //--- strafe

        Turn(-90, Speed.Medium); //--- turn right 90 so we're lined up with wobble goal 1
        DriveTo(Position.W1, 270, DriveType.Straight, Speed.Fast); //--- drive to wobble goal
    }
}
