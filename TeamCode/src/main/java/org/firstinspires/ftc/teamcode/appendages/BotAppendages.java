package org.firstinspires.ftc.teamcode.appendages;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MapUtil;
import org.firstinspires.ftc.teamcode.appendages.utils.MovingAverage;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

@Config
public class BotAppendages {
    public final static double TRIGGER_PRESSED_THRESH = 0.5;
    public final static double JOYSTICK_DEAD_ZONE = 0.05;

    public final static double TANK_SPEED = 0.9;

    public final RevBlinkinLedDriver blinkin;
    public RevBlinkinLedDriver.BlinkinPattern blinkinPattern;

    public final DcMotorEx tankDrive;

    public BotAppendages(HardwareMap hardwareMap) {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        tankDrive = hardwareMap.get(DcMotorEx.class, "tankDrive");
    }
    

    public void setBlinkinPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
        blinkinPattern = pattern;
    }

    public RevBlinkinLedDriver.BlinkinPattern getBlinkinPattern() {
        return blinkinPattern;
    }

    public void enableTankDrive(boolean enabled) {
        tankDrive.setPower(enabled ? TANK_SPEED : 0);
    }
}
