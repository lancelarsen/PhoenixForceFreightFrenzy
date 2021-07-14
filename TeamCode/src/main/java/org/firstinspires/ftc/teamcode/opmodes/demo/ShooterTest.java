package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.appendages.AppendagesTeleOp;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;
import org.firstinspires.ftc.teamcode.drive.MecanumTeleOp;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="ShooterTest", group="1")
public class ShooterTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        DcMotorEx shooterWheel = hardwareMap.get(DcMotorEx.class, "shooterWheel");
        shooterWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterWheel.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        shooterWheel.setVelocity(1700);

        while(opModeIsActive());
    }
}
