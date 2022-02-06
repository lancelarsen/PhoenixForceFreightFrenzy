package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.vision.BarcodeVision;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "BlueInVisionTest", group = "1")
public class BlueInVisionTest extends AbstractAuto {

    @Override
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        while (!isStopRequested()) {
            telemetry.addData("Capstone index", vision.getCapstoneIndex());
            telemetry.addData("Color level", vision.getColorLevel());
            telemetry.update();
        }
    }
}