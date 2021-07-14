package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.appendages.AppendagesTeleOp;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;
import org.firstinspires.ftc.teamcode.drive.MecanumTeleOp;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="LiftTest", group="1")
public class LiftTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        AppendagesTeleOp appendages = new AppendagesTeleOp(this);
        MecanumTeleOp mecanum = new MecanumTeleOp(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        appendages.goalLifter.setTargetPosition(EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, 40));
        appendages.goalLifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        appendages.goalLifter.setPower(1.0);

        sleep(5000);
    }
}
