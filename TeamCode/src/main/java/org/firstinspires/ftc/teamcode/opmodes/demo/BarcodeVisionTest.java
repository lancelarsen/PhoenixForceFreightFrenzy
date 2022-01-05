package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.vision.BarcodeVision;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="BarcodeVisionTest", group="1")
public class BarcodeVisionTest extends LinearOpMode {

    private BarcodeVision vision;

    @Override
    public void runOpMode() {

        vision = new BarcodeVision(hardwareMap);
        vision.init(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        waitForStart();
        while (isStopRequested()) {
            telemetry.addData("Zone", vision.getColorLevelIndex());
            telemetry.addData("Color level", vision.getColorLevel());
        }
    }
}