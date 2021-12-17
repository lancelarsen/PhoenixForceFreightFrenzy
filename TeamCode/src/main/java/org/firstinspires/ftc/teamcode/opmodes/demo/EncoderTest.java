package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.roadrunnerUtils.Encoder;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="EncoderTest", group="1")
public class EncoderTest extends LinearOpMode {
    private Encoder centerEncoder;
    private Encoder leftEncoder;
    private Encoder rightEncoder;

    @Override
    public void runOpMode() {
        BotMecanumDrive drive = new BotMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        centerEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rf"));
        centerEncoder.setDirection(Encoder.Direction.REVERSE);
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "frontIntakeRoller"));
        leftEncoder.setDirection(Encoder.Direction.REVERSE);
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "tankDrive"));
        rightEncoder.setDirection(Encoder.Direction.REVERSE);

        waitForStart();
        
        long initCenter = centerEncoder.getCurrentPosition();
        long initLeft = leftEncoder.getCurrentPosition();
        long initRight = rightEncoder.getCurrentPosition();

        while (opModeIsActive()) {
            telemetry.addData("centerEncoder", centerEncoder.getCurrentPosition() - initCenter);
            telemetry.addData("leftEncoder", leftEncoder.getCurrentPosition() - initLeft);
            telemetry.addData("rightEncoder", rightEncoder.getCurrentPosition() - initRight);
            telemetry.update();

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();
        }
    }
}