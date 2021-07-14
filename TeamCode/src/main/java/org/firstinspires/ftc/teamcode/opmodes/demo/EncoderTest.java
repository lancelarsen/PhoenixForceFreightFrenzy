package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.BotMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.roadrunnerUtils.Encoder;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="EncoderTest", group="1")
public class EncoderTest extends LinearOpMode {
    private Encoder intakeElevator;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private  Encoder frontEncoder;

    @Override
    public void runOpMode() {
        BotMecanumDrive drive = new BotMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeElevator = new Encoder(hardwareMap.get(DcMotorEx.class, "intakeElevator"));
        intakeElevator.setDirection(Encoder.Direction.REVERSE);
        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "goalGrabberLifter"));
        leftEncoder.setDirection(Encoder.Direction.REVERSE);
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intakeRoller"));
        rightEncoder.setDirection(Encoder.Direction.REVERSE);
        // Using encoder port from this motor as we arn't using an encoder on the roller
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "shooterWheel"));
        frontEncoder.setDirection(Encoder.Direction.FORWARD);

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("intakeElevator", intakeElevator.getCurrentPosition());
            telemetry.addData("goalGrabberLifter", leftEncoder.getCurrentPosition());
            telemetry.addData("intakeRoller", rightEncoder.getCurrentPosition());
            telemetry.addData("shooterWheel", frontEncoder.getCurrentPosition());
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