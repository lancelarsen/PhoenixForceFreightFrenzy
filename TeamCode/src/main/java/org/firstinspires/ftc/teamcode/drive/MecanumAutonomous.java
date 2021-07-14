package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.Arrays;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MAX_VEL;

public class MecanumAutonomous extends BotMecanumDrive {
    // In inches per second
    private static double VERY_SLOW_MAX_VEL = 3;
    private static double SLOW_MAX_VEL = DriveConstants.MAX_VEL / 4;
    private static double MEDIUM_MAX_VEL = DriveConstants.MAX_VEL / 2;
    private static double FAST_MAX_VEL = DriveConstants.MAX_VEL;

    private enum MovementMethod {
        TURN,
        LINE,
        SPLINE,
        SPLINE_CONSTANT_HEADING
    }

    public enum Speed {
        VERY_SLOW,
        SLOW,
        MEDIUM,
        FAST
    }

    private LinearOpMode opMode;

    private Pose2d position;
    private Speed speed = Speed.FAST;
    private boolean reversed = false;

    public MecanumAutonomous(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }
    public Speed getSpeed() {
        return speed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
    public boolean isReversed() {
        return reversed;
    }

    public void setCurrentPosition(Pose2d position) {
        this.position = position;
        setPoseEstimate(position);
    }

    public void turnLeft(double angle) {
        turn(angle);
    }

    public void turnRight(double angle) {
        turn(angle * -1);
    }

    public void turn(double angle) {
        turn(new Pose2d(0, 0, Math.toRadians(angle)));
    }

    public void turn(Pose2d pose2d) {
        drive(pose2d, MovementMethod.TURN);
    }

    public void line(Pose2d pose2d) {
        drive(pose2d, MovementMethod.LINE);
    }

    public void curve(Pose2d pose2d) {
        spline(pose2d, 0);
    }

    public void spline(Pose2d pose2d, double splineAngle) {
        drive(pose2d, MovementMethod.SPLINE, splineAngle);
    }

    public void splineConstantHeading(Pose2d pose2d, double splineAngle) {
        drive(pose2d, MovementMethod.SPLINE_CONSTANT_HEADING, splineAngle);
    }

    public void drive(Pose2d pose2d, MovementMethod movementMethod) {
        drive(pose2d, movementMethod, 0);
    }

    private void drive(Pose2d pose2d, MovementMethod movementMethod, double splineAngle) {
        double maxVel = 0;
        switch (speed) {
            case VERY_SLOW:
                maxVel = VERY_SLOW_MAX_VEL; break;
            case SLOW:
                maxVel = SLOW_MAX_VEL; break;
            case MEDIUM:
                maxVel = MEDIUM_MAX_VEL; break;
            case FAST:
                maxVel = FAST_MAX_VEL; break;
        }

        if (movementMethod == MovementMethod.TURN) {
            turnAsync(pose2d.getHeading()/*, maxVel, MAX_ACCEL*/);
            waitForIdle();
            position = position.plus(pose2d);

            return;
        }

        MinVelocityConstraint velConstraint = new MinVelocityConstraint(
                Arrays.asList(
                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                        new MecanumVelocityConstraint(maxVel, DriveConstants.TRACK_WIDTH)
                )
        );
        ProfileAccelerationConstraint accelConstraint = new ProfileAccelerationConstraint(MAX_ACCEL);

        TrajectoryBuilder builder = trajectoryBuilder(position, reversed);
        switch (movementMethod) {
            case LINE:
                if (position.getHeading() == pose2d.getHeading()) {
                    builder.lineTo(pose2d.vec(), velConstraint, accelConstraint);
                } else {
                    builder.lineToLinearHeading(pose2d, velConstraint, accelConstraint);
                }
                break;
            case SPLINE:
                if (position.getHeading() == pose2d.getHeading()) {
                    builder.splineTo(pose2d.vec(), Math.toRadians(splineAngle), velConstraint, accelConstraint);
                } else {
                    builder.splineToLinearHeading(pose2d, Math.toRadians(splineAngle), velConstraint, accelConstraint);
                }
                break;
            case SPLINE_CONSTANT_HEADING:
                builder.splineToConstantHeading(pose2d.vec(), Math.toRadians(splineAngle), velConstraint, accelConstraint);
                break;
        }

        Trajectory trajectory = builder.build();
        followTrajectoryAsync(trajectory);
        while (isBusy() && opMode.opModeIsActive() && !Thread.interrupted()) {
            update();
        }

        position = trajectory.end();
    }

    public void backupOnWall() {
        this.setMotorPowers(-0.5, -0.5, -0.5, -0.5);
        //sleep(1000); //TODO: ??
    }
}
