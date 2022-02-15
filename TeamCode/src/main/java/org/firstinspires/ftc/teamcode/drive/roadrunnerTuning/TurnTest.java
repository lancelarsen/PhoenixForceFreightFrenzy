package org.firstinspires.ftc.teamcode.drive.roadrunnerTuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Disabled
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
    public static double ANGLE = 180; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        BotMecanumDrive drive = new BotMecanumDrive(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        drive.turnAsync(Math.toRadians(ANGLE));
        drive.waitForIdle();
    }
}
