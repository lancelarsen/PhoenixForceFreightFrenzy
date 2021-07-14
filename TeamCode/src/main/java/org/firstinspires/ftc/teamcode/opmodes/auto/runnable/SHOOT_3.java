package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;

@Autonomous(group = "auto")
public class SHOOT_3 extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        appendages.setShooterSpeed(1650);
        sleep(2000);
        appendages.shootRings(2);
        appendages.setShooterSpeed(1550);
        sleep(2000);
        appendages.shootRings(1);
        appendages.shooterOff();
    }
}
