package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;

@Disabled
@Autonomous(group = "auto")
public class R_OUT_TEST_HG extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
        sleep(2000);

        //--- set starting position
        drive.setCurrentPosition(FieldPositions.RL0);

        //--- Shoot top goal
        drive.curve(FieldPositions.RTO);
        appendages.shootRings();
        appendages.shooterOff();

        //--- Wait for 20 seconds
        sleep(5000);
    }

}
