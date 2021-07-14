package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Autonomous(group = "auto")
public class _R_OUT_HG_WG_L extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        //--- Detect number of rings
        RingVision.TargetZone targetZone = ringVision.getTargetZone();

        //--- Start intake to deploy collector
        appendages.ringIntakeStart();

        //--- Grab wobble goal
        drive.line(FieldPositions.RSO_W);
        appendages.wobbleGoalGrab();

        //--- Stop intake
        appendages.ringIntakeStop();

        //--- delays
        switch (targetZone) {
            case ZONE_A:
                break;
            case ZONE_B:
            case ZONE_C:
              //  sleep(10000);
                break;
        }

        //--- Shoot top goal
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
        drive.curve(FieldPositions.RTO2);
        sleep(1000);
        appendages.shootRings();
        appendages.shooterOff();

        //--- Drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RO_WA); break;
            case ZONE_B:
                drive.line(FieldPositions.RO_WB); break;
            case ZONE_C:
                drive.line(FieldPositions.RO_WC); break;
        }
        appendages.wobbleGoalDrop();

        //--- Park on line
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RX6);
                sleep(8000);
                drive.line(FieldPositions.RLS1_A);
                appendages.setReachArmPosition(BotAppendages.ReachArmPosition.EXTENDED);
                break;
            case ZONE_B:
            case ZONE_C:

                drive.line(FieldPositions.RLS1);
                break;
        }
        sleep(2000);
    }
}
