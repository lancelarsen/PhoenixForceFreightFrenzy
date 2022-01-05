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
    private Encoder leftFront;
    private Encoder leftRear;
    private Encoder rightFront;
    private Encoder rightRear;

    @Override
    public void runOpMode() {
        BotMecanumDrive drive = new BotMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront = new Encoder(hardwareMap.get(DcMotorEx.class, "lf"));
        leftRear = new Encoder(hardwareMap.get(DcMotorEx.class, "lr"));
        rightFront = new Encoder(hardwareMap.get(DcMotorEx.class, "rf"));
        rightRear = new Encoder(hardwareMap.get(DcMotorEx.class, "rr"));

        waitForStart();
        
        long initLeftFront = leftFront.getCurrentPosition();
        long initLeftRear = leftRear.getCurrentPosition();
        long initRightFront = rightFront.getCurrentPosition();
        long initRightRear = rightRear.getCurrentPosition();

        while (opModeIsActive()) {
            telemetry.addData("leftFront", leftFront.getCurrentPosition() - initLeftFront);
            telemetry.addData("leftRear", leftRear.getCurrentPosition() - initLeftRear);
            telemetry.addData("rightFront", rightFront.getCurrentPosition() - initRightFront);
            telemetry.addData("rightRear", rightRear.getCurrentPosition() - initRightRear);
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