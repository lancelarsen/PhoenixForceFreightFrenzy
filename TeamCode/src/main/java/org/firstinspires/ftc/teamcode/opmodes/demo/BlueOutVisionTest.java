package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.vision.BarcodeVision;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "BlueOutVisionTest", group = "1")
public class BlueOutVisionTest extends AbstractAuto {

    @Override
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);

        while (!isStopRequested()) {
            telemetry.addData("Capstone index", vision.getCapstoneIndex());
            telemetry.addData("Color level", vision.getColorLevel());
            telemetry.update();

            /*
             * switch (vision.getCapstoneIndex()) {
             * case 1:
             * System.out.println("Left barcode");
             * break;
             * case 2:
             * System.out.println("Center barcode");
             * break;
             * case 3:
             * System.out.println("Right barcode");
             * break;
             * }
             */
        }
    }
}