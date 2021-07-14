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
public class R_OUT_TEST_PS extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        //--- Shoot top goal
        drive.setCurrentPosition(FieldPositions.RL0);

        shootPowershotsFromSide(FieldPositions.RTO_TELE_PS, FieldPositions.RTO_TELE_PSA);
        appendages.shooterOff();

        //--- Wait for 20 seconds
        sleep(5000);
    }

    private void shootPowershotsFromSide(Pose2d shootingPose, double turnAngles[]) {
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.POWERSHOTS);
        drive.line(shootingPose);
        sleep(2000);
        for (int i = 0; i < 3; i++) {
            appendages.shootRings(1);
            if (i == 2) break;
            drive.turnLeft(turnAngles[i]);
        }
    }
}
