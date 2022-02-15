package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.vision.BarcodeVision;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "RedOutVisionTest", group = "1")
public class RedOutVisionTest extends AbstractAuto {

    @Override
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        while (!isStopRequested()) {
            telemetry.addData("Capstone index", vision.getCapstoneIndex());
            telemetry.addData("Color level", vision.getColorLevel());
            telemetry.update();
        }
    }
}