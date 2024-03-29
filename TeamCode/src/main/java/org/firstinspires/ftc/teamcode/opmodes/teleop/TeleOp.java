package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.AppendagesTeleOp;

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
//  - Left Bumper           - 
//  - Left Trigger          - 
//  - Right Bumper          - 
//  - Right Trigger         - 
//  - X                     - 
//  - Y                     - Speed Fast
//  - B                     - Speed Slow
//  - A                     -
//----------------------------------------------------------------------
// Gamepad 2
//  - Left Stick            - 
//  - Right Stick           - 
//  - Dpad Up               - 
//  - Dpad Down             - 
//  - Dpad Right            -
//  - Dpad Left             -
//  - Left Bumper           -
//  - Left Trigger          - 
//  - Right Bumper          - 
//  - Right Trigger         -
//  - X                     - 
//  - B                     - 
//  - Y                     - 
//  - A                     - 
//----------------------------------------------------------------------

public class TeleOp {
    private LinearOpMode opMode;
    private AutoUtils.Alliance alliance;

    private AppendagesTeleOp appendages;

    private MecanumTeleOp drive;

    public TeleOp(LinearOpMode opMode, AutoUtils.Alliance alliance) {
        this.opMode = opMode;
        this.alliance = alliance;

        drive = new MecanumTeleOp(opMode);
        drive.enableTurning(true);

        if (alliance == AutoUtils.Alliance.RED)
            drive.setStickGains(-1, -1, 1);

        appendages = new AppendagesTeleOp(opMode);
        appendages.updateLights(alliance);
    }

    public void run() {
        drive.arcadeDrive();
        appendages.updateLights(alliance);
        appendages.updateTankDrive(drive.getForwardDriveSpeed());
        appendages.updateIntake();
        appendages.updateDuckWheels();
        appendages.updateGondola();
    }
}
