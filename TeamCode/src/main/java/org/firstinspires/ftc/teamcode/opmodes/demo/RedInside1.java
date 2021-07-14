package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_VEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ANG_VEL;

@Disabled
@TeleOp(name = "DEMO Red->Inside 1", group = "zzz")
public class RedInside1 extends LinearOpMode {

    private static LinearOpMode _opMode;
    private static AppendagesAutonomous _appendages;
    private static RingVision _ringVision;
    BotMecanumDrive _drive;
    private Pose2d _poseLast = null;
    MinVelocityConstraint _velConstraint;
    ProfileAccelerationConstraint _accelConstraint;

    private enum Speed
    {
        Fast,
        Medium,
        Slow,
        VerySlow
    }

    private enum Facing
    {
        Goal,
        Left,
        Right,
        Back
    }

    private enum DriveDirection
    {
        Forward,
        Backwards
    }

    public double GetFacingAngle(Facing facing)
    {
        double angle = 0;
        switch (facing)
        {
            case Goal: angle = 0; break;
            case Left: angle = -90; break;
            case Right: angle = 90; break;
            case Back: angle = -180; break;
        }
        return angle;
    }

    private void SetSpeed(Speed speed)
    {
        double maxVel = 0;
        switch (speed) {
            case Fast:
                maxVel = MAX_VEL; break;
            case Medium:
                maxVel = MAX_VEL / 2; break;
            case Slow:
                maxVel = MAX_VEL / 4; break;
            case VerySlow:
                maxVel = 10; break;
        }

        _velConstraint = new MinVelocityConstraint(
                Arrays.asList(
                        new AngularVelocityConstraint(MAX_ANG_VEL),
                        new MecanumVelocityConstraint(maxVel, DriveConstants.TRACK_WIDTH)
                )
        );
        _accelConstraint = new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL);
    }

    private void SetStartPosition(Pose2d position)
    {
        Pose2d initialPosition = position;
        _drive.setPoseEstimate(initialPosition);
        _poseLast = initialPosition;
    }

    private void DriveTurn(double angle, Speed speed)
    {
        double _maxAngularVelocity = 0;
        double _maxAngularAcceleration = 0;
        switch (speed) {
            case Fast:
                _maxAngularVelocity = MAX_ANG_VEL;
                _maxAngularAcceleration = MAX_ANG_ACCEL;
                break;
            case Medium:
                _maxAngularVelocity = MAX_ANG_VEL / 2;
                _maxAngularAcceleration = MAX_ANG_ACCEL / 2;
                break;
            case Slow:
                _maxAngularVelocity = MAX_ANG_VEL / 4;
                _maxAngularAcceleration = MAX_ANG_ACCEL / 4;
                break;
            case VerySlow:
                _maxAngularVelocity = MAX_ANG_VEL / 8;
                _maxAngularAcceleration = MAX_ANG_ACCEL / 8;
                break;
        }

        //_drive.turn(Math.toRadians(angle), _maxAngularVelocity, _maxAngularAcceleration);
        _poseLast = _poseLast.plus(new Pose2d(0, 0, Math.toRadians(angle)));
    }

    private void DriveCurve(Pose2d position, Speed speed)
    {
        DriveCurve(position, speed, 180, DriveDirection.Forward);
    }

    private void DriveCurve(Pose2d position, Speed speed, double splineAngle, DriveDirection direction)
    {
        boolean UseSplineToHeading = false;
        boolean driveBackwards = false;
        Trajectory trajectory = null;
        if (direction == DriveDirection.Backwards)
        {
            driveBackwards = true;
            UseSplineToHeading = true;
        }
        if (_poseLast.getHeading() != position.getHeading())
        {
            UseSplineToHeading = true;
        }
        SetSpeed(speed);
        if (UseSplineToHeading)
        {
            trajectory = _drive.trajectoryBuilder(_poseLast, driveBackwards)
                    .splineToLinearHeading(position, Math.toRadians(splineAngle), _velConstraint, _accelConstraint).build();
        }
        else
        {
            trajectory = _drive.trajectoryBuilder(_poseLast, driveBackwards)
                    .splineTo(new Vector2d(position.getX(), position.getY()), Math.toRadians(position.getHeading()), _velConstraint, _accelConstraint).build();
        }
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    private void DriveCurveFixedHeading(Pose2d position, Speed speed)
    {
        DriveCurveFixedHeading(position, speed);
    }

    private void DriveCurveFixedHeading(Pose2d position, Speed speed, double splineAngle)
    {
        SetSpeed(speed);
        Trajectory trajectory = _drive.trajectoryBuilder(_poseLast)
                        .splineToConstantHeading(new Vector2d(position.getX(), position.getY()), Math.toRadians(splineAngle), _velConstraint, _accelConstraint).build();
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    private void DriveStraight(Pose2d position, Speed speed)
    {
        SetSpeed(speed);
        Trajectory trajectory = _drive.trajectoryBuilder(_poseLast)
                .lineToLinearHeading(position, _velConstraint, _accelConstraint).build();
        _drive.followTrajectory(trajectory);
        _poseLast = trajectory.end();
    }

    private void DriveAlignBackOnWall()
    {
        _drive.setMotorPowers(-0.5, -0.5, -0.5, -0.5);
        sleep(1000);
    }

    @Override
    public void runOpMode()
    {
        _opMode = this;
        _appendages = new AppendagesAutonomous(_opMode);
        _ringVision  = new RingVision(_opMode.hardwareMap);
        _drive = new BotMecanumDrive(hardwareMap);

        SetStartPosition(FieldPositions.RSI);

        waitForStart();
        if (isStopRequested()) return;

        //----------------------------------------------------------------------
        //--- pick up wobble
        //----------------------------------------------------------------------
        DriveStraight(FieldPositions.RSI_W, Speed.Fast); //--- drive away from wall
        sleep(2000);

        //----------------------------------------------------------------------
        //--- power shots
        //----------------------------------------------------------------------
//        DriveStraight(FieldPositions.P4[0], Speed.Fast); //--- drive to power shot line
//        sleep(1000);
//        DriveStraight(FieldPositions.P4[1], Speed.Fast); //--- strafe
//        sleep(1000);
//        DriveStraight(FieldPositions.P4[2], Speed.Fast); //--- strafe

        //----------------------------------------------------------------------
        //--- collect rings
        //----------------------------------------------------------------------
        DriveCurve(FieldPositions.C2A, Speed.Fast); //--- prepare to collect rings
        //--- start intake
        DriveCurve(FieldPositions.C2B, Speed.VerySlow); //--- slow pull forward
        sleep(2000);

        //----------------------------------------------------------------------
        //--- deliver wobble to target
        //----------------------------------------------------------------------
        //--- deliver to far target

        //--- deliver to middle target
        DriveTurn(90, Speed.Fast);
        DriveStraight(FieldPositions.W5S, Speed.Fast); //--- deliver wobble goal
        sleep(2000);

        //--- deliver to close target

        //----------------------------------------------------------------------
        //--- park on line
        //----------------------------------------------------------------------
        DriveCurve(FieldPositions.X3, Speed.Fast); //--- prepare to collect rings
        DriveStraight(FieldPositions.L0, Speed.Fast); //--- prepare to collect rings

//        DriveTurn(-270, Speed.Slow);
//        sleep(2000);
//        DriveTurn(270, Speed.Medium);
//        sleep(2000);
//        DriveTurn(-270, Speed.Fast);

//        sleep(2000);
//        DriveCurveFixedHeading(FieldPositions.S3W, Speed.Fast, Facing.Goal); //--- back to start

//        DriveTurn(-90, Speed.Medium); //--- turn right 90 so we're lined up with wobble goal 1
//        DriveStraight(FieldPositions.W1, Speed.Fast); //--- drive to wobble goal

        //DriveCurve(FieldPositions.S3W, Speed.Slow, 0, DriveDirection.Backwards);
        //DriveCurveFixedHeading(FieldPositions.S3W, Speed.Slow, Facing.Left);

        //----------------------------------------------------------------------
        //--- drive back to starting position
        //----------------------------------------------------------------------
        sleep(2000);
        DriveStraight(FieldPositions.RSI_W, Speed.Medium);
        //DriveCurve(FieldPositions.S3W, Speed.Slow, 0, DriveDirection.Backwards);
        DriveAlignBackOnWall();
    }
}
