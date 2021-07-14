package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;

@Disabled
@Autonomous(group = "auto")
public class B_OUT_TEST_HG extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.OUTSIDE);

        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
        sleep(2000);

        //--- set starting position
        drive.setCurrentPosition(FieldPositions.BL0);

        //--- Shoot top goal
        drive.curve(FieldPositions.BTO);
        appendages.shootRings();
        appendages.shooterOff();

        //--- Wait for 20 seconds
        sleep(5000);
    }

}
