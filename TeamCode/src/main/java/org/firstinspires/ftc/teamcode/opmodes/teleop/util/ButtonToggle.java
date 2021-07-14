package org.firstinspires.ftc.teamcode.opmodes.teleop.util;

public class ButtonToggle {
    private boolean active;
    private boolean prePressed;

    public ButtonToggle() {
        active = false;
    }

    public ButtonToggle(boolean initActive) {
        active = initActive;
    }

    public boolean update(boolean currentlyPressed) {
        if (currentlyPressed && !prePressed) {
            prePressed = true;
            active = !active;
        } else if (!currentlyPressed) {
            prePressed = false;
        }

        return isActive();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
