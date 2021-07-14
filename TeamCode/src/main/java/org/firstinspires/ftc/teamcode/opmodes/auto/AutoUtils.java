package org.firstinspires.ftc.teamcode.opmodes.auto;

public class AutoUtils {
    public enum Alliance {
        RED,
        BLUE
    }

    public enum StartingPosition {
        INSIDE,
        OUTSIDE,
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
