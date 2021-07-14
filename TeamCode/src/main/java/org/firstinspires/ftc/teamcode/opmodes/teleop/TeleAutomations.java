package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.appendages.AppendagesTeleOp;
import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumTeleOp;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.opmodes.teleop.util.GamepadUtils;

import static org.firstinspires.ftc.teamcode.appendages.BotAppendages.TRIGGER_PRESSED_THRESH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class TeleAutomations {
    private volatile LinearOpMode opMode;
    private volatile AutoUtils.Alliance alliance;

    private volatile MecanumTeleOp drive;
    private volatile AppendagesTeleOp appendages;

    private volatile GamepadUtils driverGamepad;
    private volatile Thread shootThread;

    private volatile boolean hasLocalizedRobot = false;
    private volatile int autoPowershotsShot = 0;

    private final static Pose2d BLUE_SIDE_INIT_POWERSHOT_POSE = new Pose2d(18, 54, Math.toRadians(0));
    private final static Pose2d BLUE_FRONT_INIT_POWERSHOT_POSE = new Pose2d(58, 11, Math.toRadians(0));
    private final static Pose2d RED_SIDE_INIT_POWERSHOT_POSE = new Pose2d(18, -59, Math.toRadians(0));
    private final static Pose2d RED_FRONT_INIT_POWERSHOT_POSE = new Pose2d(58, -11, Math.toRadians(0));

    public TeleAutomations(LinearOpMode opMode, AutoUtils.Alliance alliance, MecanumTeleOp drive, AppendagesTeleOp appendages) {
        this.opMode = opMode;
        this.alliance = alliance;

        this.drive = drive;
        this.appendages = appendages;

        driverGamepad = new GamepadUtils(opMode.gamepad1);
    }

    public void commandHighGoalShooting() {
        if (opMode.gamepad2.right_trigger > TRIGGER_PRESSED_THRESH) {
            switch (alliance) {
                case RED:
                    drive.setPoseEstimate(FieldPositions.RL0);
                    shootHighGoal(FieldPositions.RTO);
                    break;
                case BLUE:
                    drive.setPoseEstimate(FieldPositions.BL0);
                    shootHighGoal(FieldPositions.BTO);
                    break;
            }
        }
    }

    private void shootHighGoal(Pose2d shootingPose) {
        /*interruptShootThread();

        Runnable shootTask = () -> {*/
            //try {
                appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);

                Trajectory shootingTrajectory = drive.line(shootingPose);
                drive.followTrajectory(shootingTrajectory);

                for (int i = 0; i < GameConstants.MAX_RINGS_IN_ROBOT; i++) {
                    appendages.shootRings(1); // call one by one so we can update lights between each shot
                    //appendages.updateLights(alliance);
                }
            //} catch (Exception e) {};
        /*};

        shootThread = new Thread(shootTask);
        shootThread.start();

        waitForShootThread();*/
    }

    public void commandSidePowershotShooting() {
        if (opMode.gamepad2.left_trigger > TRIGGER_PRESSED_THRESH) {
            switch (alliance) {
                case RED:
                    drive.setPoseEstimate(FieldPositions.RL0);
                    shootPowershotsFromSide(FieldPositions.RTO_TELE_PS, FieldPositions.RTO_TELE_PSA);
                    break;
                case BLUE:
                    drive.setPoseEstimate(FieldPositions.BL0);
                    shootPowershotsFromSide(FieldPositions.BTO_TELE_PS, FieldPositions.BTO_TELE_PSA);
                    break;
            }
        }
    }

    private void shootPowershotsFromSide(Pose2d shootingPose, double turnAngles[]) {
        /*interruptShootThread();

        Runnable shootTask = () -> {*/
            //try {
                appendages.setShooterSpeed(BotAppendages.ShooterSpeed.POWERSHOTS);
                /*Thread.*/sleep(2000);

                Trajectory shootingTrajectory = drive.line(shootingPose);
                drive.followTrajectory(shootingTrajectory);

                for (int i = 0; i < GameConstants.MAX_RINGS_IN_ROBOT; i++) {
                    appendages.shootRings(1);
                    //appendages.updateLights(alliance);

                    if (i == GameConstants.MAX_RINGS_IN_ROBOT - 1) break;

                    drive.turnAsync(Math.toRadians(turnAngles[i]));
                    drive.waitForIdle();
                }
            //} catch (InterruptedException e) {};
        /*};

        shootThread = new Thread(shootTask);
        shootThread.start();

        waitForShootThread();*/

        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
    }

    public void commandFrontPowershotShooting() {
        if (opMode.gamepad2.left_bumper) {
            if (!hasLocalizedRobot) return;

            switch (alliance) {
                case RED:
                    shootPowershotsFromFront(FieldPositions.P4, FieldPositions.P4A);
                    break;
                case BLUE:
                    shootPowershotsFromFront(FieldPositions.P2, FieldPositions.P2A);
                    break;
            }
        } else if (opMode.gamepad2.dpad_up) {
            hasLocalizedRobot = true;
            autoPowershotsShot = 0;

            switch (alliance) {
                case RED:
                    drive.setPoseEstimate(RED_FRONT_INIT_POWERSHOT_POSE);
                    shootPowershotsFromFront(FieldPositions.P4, FieldPositions.P4A);
                    break;
                case BLUE:
                    drive.setPoseEstimate(BLUE_FRONT_INIT_POWERSHOT_POSE);
                    shootPowershotsFromFront(FieldPositions.P2, FieldPositions.P2A);
                    break;
            }
        } else if (opMode.gamepad2.dpad_left && alliance == AutoUtils.Alliance.BLUE) {
            hasLocalizedRobot = true;
            autoPowershotsShot = 0;

            drive.setPoseEstimate(BLUE_SIDE_INIT_POWERSHOT_POSE);
            shootPowershotsFromFront(FieldPositions.P2, FieldPositions.P2A);
        } else if (opMode.gamepad2.dpad_right && alliance == AutoUtils.Alliance.RED) {
            hasLocalizedRobot = true;
            autoPowershotsShot = 0;

            drive.setPoseEstimate(RED_SIDE_INIT_POWERSHOT_POSE);
            shootPowershotsFromFront(FieldPositions.P4, FieldPositions.P4A);
        }
    }

    private void shootPowershotsFromFront(Pose2d shootingPose, double turnAngles[]) {
        interruptShootThread();

        Runnable shootTask = () -> {
            try {
                appendages.setShooterSpeed(BotAppendages.ShooterSpeed.POWERSHOTS);

                Trajectory shootingTrajectory = drive.line(shootingPose);
                drive.follow(shootingTrajectory);

                Thread.sleep(500);

                int previousPowershotsShot = autoPowershotsShot;

                for (int i = 0; i < GameConstants.MAX_RINGS_IN_ROBOT; i++) {
                    // Don't shoot powershots already shot
                    if (i + 1 <= previousPowershotsShot) continue;

                    appendages.shootRings(1);
                    appendages.updateLights(alliance);
                    autoPowershotsShot++;

                    if (i == GameConstants.MAX_RINGS_IN_ROBOT - 1) break;

                    drive.turnAsync(Math.toRadians(turnAngles[i]));
                    drive.waitForIdle();
                }
            } catch (InterruptedException e) {};
        };

        shootThread = new Thread(shootTask);
        shootThread.start();

        waitForShootThread();
    }

    private void interruptShootThread() {
        //if (shootThread != null) shootThread.interrupt();
    }

    private void waitForShootThread() {
        /*while (shootThread.isAlive()) {
            if (driverGamepad.areJoysticksActive() || driverGamepad.isDpadActive() || !opMode.opModeIsActive()) {
                shootThread.interrupt();
                return;
            }
        }*/
    }
}
