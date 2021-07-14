package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.AppendagesTeleOp;
import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.drive.MecanumTeleOp;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;

import static org.firstinspires.ftc.teamcode.appendages.BotAppendages.TRIGGER_PRESSED_THRESH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

//----------------------------------------------------------------------
// Gamepad 1
//  - Left Stick            - Arcade Drive Movement
//  - Right Stick           - Arcade Turning (Push: Realign Goal Lifter)
//  - Dpad Up               - Drive Straight Forward
//  - Dpad Down             - Drive Straight Backward
//  - Dpad Right            - Drive Strafe Right
//  - Dpad Left             - Drive Strafe Left
//  - Left Bumper           - Goal Grabber Open
//  - Left Trigger          - Goal Lifter Up
//  - Right Bumper          - Goal Grabber Closed
//  - Right Trigger         - Goal Lifter Down
//  - X                     - Auto Shoot Powershots
//  - Y                     - Speed Fast
//  - B                     - Speed Slow
//  - A                     -
//----------------------------------------------------------------------
// Gamepad 2
//  - Left Stick            - Tilt Ring Shooter
//  - Right Stick           - (Push: Realign Ring Lifter)
//  - Dpad Up               - Ring Lifter Up
//  - Dpad Down             - Ring Lifter Down
//  - Dpad Right            -
//  - Dpad Left             -
//  - Left Bumper           -
//  - Left Trigger          - teleop-auto: shoot power shots
//  - Right Bumper          - teleop-auto: shoot high goal
//  - Right Trigger         -
//  - X                     - Shoot a Ring!
//  - B                     - Turn Ring Intake On/Off While Lifter Up
//  - Y                     - Turn Ring Shooter On/Off While Lifter Down
//  - A                     - Reverse Ring Intake Direction (While Pressed)
//----------------------------------------------------------------------

public class TeleOp {
    private LinearOpMode opMode;
    private AutoUtils.Alliance alliance;

    private MecanumTeleOp drive;
    private MecanumAutonomous mecanumAuto;
    private AppendagesTeleOp appendages;

    private TeleAutomations automations;

    public TeleOp(LinearOpMode opMode, AutoUtils.Alliance alliance) {
        this.opMode = opMode;
        this.alliance = alliance;

        drive = new MecanumTeleOp(opMode);
        mecanumAuto = new MecanumAutonomous(opMode);
        appendages = new AppendagesTeleOp(opMode);

        //automations = new TeleAutomations(opMode, alliance, drive, appendages);
    }

    public void run() {
        appendages.updateLights(alliance);
        appendages.commandIntake();
        appendages.commandShooter();
        appendages.commandGoalGrabber();

        drive.enableTurning(!appendages.isAdjGoalLifterPosition());
        drive.arcadeDrive();

        shootHighGoalFromSide();
        shootPowerShotsFromSide();

        //automations.commandHighGoalShooting();
        //automations.commandFrontPowershotShooting();
        //automations.commandSidePowershotShooting();
    }

    private void shootHighGoalFromSide() {
        if (opMode.gamepad2.right_trigger > TRIGGER_PRESSED_THRESH) {
            switch (alliance) {
                case RED:
                    mecanumAuto.setCurrentPosition(FieldPositions.RL0);
                    appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
                    mecanumAuto.line(FieldPositions.RTO);
//                    sleep(1000);
                    appendages.shootRings();
                    break;
                case BLUE:
                    mecanumAuto.setCurrentPosition(FieldPositions.BL0);
                    appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
                    mecanumAuto.line(FieldPositions.BTO);
//                    sleep(1000);
                    appendages.shootRings();
                    break;
            }
        }
    }

    private void shootPowerShotsFromSide() {
        if (opMode.gamepad2.left_trigger > TRIGGER_PRESSED_THRESH) {
            switch (alliance) {
                case RED:
                    mecanumAuto.setCurrentPosition(FieldPositions.RL0);
                    shootPowershotsFromSide(FieldPositions.RTO_TELE_PS, FieldPositions.RTO_TELE_PSA);
                    break;
                case BLUE:
                    mecanumAuto.setCurrentPosition(FieldPositions.BL0);
                    shootPowershotsFromSide(FieldPositions.BTO_TELE_PS, FieldPositions.BTO_TELE_PSA);
                    break;
            }
        }
    }

    private void shootPowershotsFromSide(Pose2d shootingPose, double turnAngles[]) {
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.POWERSHOTS);
        mecanumAuto.line(shootingPose);
        sleep(2000);
        for (int i = 0; i < 3; i++) {
            appendages.shootRings(1);
            if (i == 2) break;
            mecanumAuto.turnLeft(turnAngles[i]);
        }
    }
}
