package org.firstinspires.ftc.teamcode.opmodes.teleop.util;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadUtils {
    final static double DRIVER_CONTROLS_ACTIVE_THRESHOLD = 0.3;

    private Gamepad gamepad;

    public GamepadUtils(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public boolean areJoysticksActive() {
        return
                axisActive(gamepad.left_stick_x) ||
                axisActive(gamepad.left_stick_y) ||
                axisActive(gamepad.right_stick_x) ||
                axisActive(gamepad.right_stick_y);
    }

    public boolean isDpadActive() {
        return gamepad.dpad_up || gamepad.dpad_left || gamepad.dpad_down || gamepad.dpad_right;
    }

    public static boolean axisActive(double axisValue) {
        return Math.abs(axisValue) > DRIVER_CONTROLS_ACTIVE_THRESHOLD;
    }
}
