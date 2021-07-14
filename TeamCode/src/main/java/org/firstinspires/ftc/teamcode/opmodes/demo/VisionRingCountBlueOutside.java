/*
 * EasyOpenCV
 * Examples: https://github.com/OpenFTC/EasyOpenCV/tree/master/examples/src/main/java/org/openftc/easyopencv/examples
 */

package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Disabled
@TeleOp(name = "Ring Count: Blue Outside", group = "Vision")
public class VisionRingCountBlueOutside extends LinearOpMode {
    @Override
    public void runOpMode() {
        RingVision ringVision = new RingVision(hardwareMap);

        ringVision.init(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);

        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Analysis", ringVision.getColorLevel());
            telemetry.addData("Position", ringVision.getRingCount());
            telemetry.update();

            /*
             * For the purposes of this sample, throttle ourselves to 10Hz loop to avoid burning
             * excess CPU cycles for no reason. (By default, telemetry is only sent to the DS at 4Hz
             * anyway). Of course in a real OpMode you will likely not want to do this.
             */
            sleep(100);
        }
    }
}