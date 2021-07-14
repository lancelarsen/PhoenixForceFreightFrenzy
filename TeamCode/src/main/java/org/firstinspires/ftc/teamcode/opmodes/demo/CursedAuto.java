//package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.trajectory.Trajectory;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
//import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
//
//public class AutoPowerShots {
//}

package org.firstinspires.ftc.teamcode.opmodes.demo;

        import com.acmerobotics.dashboard.config.Config;
        import com.acmerobotics.roadrunner.geometry.Pose2d;
        import com.acmerobotics.roadrunner.geometry.Vector2d;
        import com.acmerobotics.roadrunner.trajectory.Trajectory;
        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

        import org.firstinspires.ftc.teamcode.appendages.AppendagesAutonomous;
        import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
        import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
        import org.opencv.core.Mat;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Disabled
@Autonomous(group = "drive")
public class CursedAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        BotMecanumDrive drive = new BotMecanumDrive(hardwareMap);
        AppendagesAutonomous appendages = new AppendagesAutonomous(this);

        waitForStart();

        if (isStopRequested()) return;

        //appendages.openGoalGrabber(false);
        //sleep(1000);
        //appendages.setGoalLifterPosition(BotAppendages.GoalLifterPosition.MIDDLE);

        //sleep(25*1000);

//        Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
//                //.lineToConstantHeading(new Vector2d(6 * 12 - 7, -5))
//                .splineToLinearHeading(new Pose2d(30, 30, Math.toRadians(90)), Math.toRadians(180))
//                .build();

        //drive.followTrajectory(trajectory);

        //sleep(1000);

        double DISTANCE_LEFT = 7.5; // in
        double DISTANCE_BACK = 2; // in


        //appendages.enableShooterWheel(true);
        //appendages.enableShooterWheel(true, 0.68);

//        Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
//                .strafeLeft(0)
//                .build();
//        drive.followTrajectory(trajectory);

        drive.setMotorPowers(-0.5, -0.5, -0.5, -0.5);
        sleep(500);
        drive.setMotorPowers(0, 0, 0, 0);

        sleep(2000);
        appendages.extendShooterArm(true);
        sleep(350);
        appendages.extendShooterArm(false);
        sleep(700);

        Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(7.5)
                .build();
        drive.followTrajectory(trajectory);

        drive.setMotorPowers(-0.5, -0.5, -0.5, -0.5);
        sleep(500);
        drive.setMotorPowers(0, 0, 0, 0);

        sleep(2000);
        appendages.extendShooterArm(true);
        sleep(350);
        appendages.extendShooterArm(false);
        sleep(700);

        trajectory = drive.trajectoryBuilder(new Pose2d())
                .strafeLeft(15)
                .build();
        drive.followTrajectory(trajectory);

        drive.setMotorPowers(-0.5, -0.5, -0.5, -0.5);
        sleep(500);
        drive.setMotorPowers(0, 0, 0, 0);

        sleep(2000);
        appendages.extendShooterArm(true);
        sleep(350);
        appendages.extendShooterArm(false);
        sleep(700);


        //appendages.setGoalLifterPosition(BotAppendages.GoalLifterPosition.DOWN);
        sleep(1000);
        //appendages.openGoalGrabber(true);
        sleep(1000);

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("finalX", poseEstimate.getX());
        telemetry.addData("finalY", poseEstimate.getY());
        telemetry.addData("finalHeading", poseEstimate.getHeading());
        telemetry.update();

        while (!isStopRequested() && opModeIsActive()) ;
    }
}
