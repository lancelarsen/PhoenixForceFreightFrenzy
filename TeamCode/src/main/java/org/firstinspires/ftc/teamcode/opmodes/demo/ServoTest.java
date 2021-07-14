/*
 * EasyOpenCV
 * Examples: https://github.com/OpenFTC/EasyOpenCV/tree/master/examples/src/main/java/org/openftc/easyopencv/examples
 */

package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.vision.RingVision;

@SuppressWarnings("SpellCheckingInspection")
@Disabled
@TeleOp(name = "Servo Test", group = "Test")
public class ServoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        Servo servo1 = hardwareMap.get(Servo.class, "servo1");
        Servo servo2 = hardwareMap.get(Servo.class, "servo2");
        Servo servo3 = hardwareMap.get(Servo.class, "servo3");
        Servo servo4 = hardwareMap.get(Servo.class, "servo4");
        Servo servo5 = hardwareMap.get(Servo.class, "servo5");

        waitForStart();

        while (opModeIsActive()) {
            if(gamepad1.dpad_up){ // Right opener
                servo1.setPosition(1);
            } else {
                servo1.setPosition(0);
            }
            if(gamepad1.dpad_left){ // Right extender
                servo2.setPosition(1);
            } else {
                servo2.setPosition(0);
            }
            if(gamepad1.dpad_down){ // Left opener
                servo3.setPosition(1);
            } else {
                servo3.setPosition(0);
            }
            if(gamepad1.dpad_right){ // Left extender
                servo4.setPosition(0);
            } else {
                servo4.setPosition(1);
            }
            if(gamepad1.a){ // Blinkin
                servo5.setPosition(1);
            } else {
                servo5.setPosition(0);
            }
        }
    }
}