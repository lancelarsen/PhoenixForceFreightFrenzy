package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Autonomous(group = "auto")
public class _R_IN_HG_RINGS_HG_WG_L extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.INSIDE);

        //--- Detect number of rings
        RingVision.TargetZone targetZone = ringVision.getTargetZone();

        appendages.ringIntakeStart();

        //--- Grab wobble goal
        drive.line(FieldPositions.RSI_W);
        appendages.wobbleGoalGrab();

        appendages.ringIntakeStop();

        //--- Shoot top goal
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
        drive.curve(FieldPositions.RTI);
        appendages.shootRings();

        //--- Go get rings and shoot!
        switch (targetZone) {
            case ZONE_A:
                break;
            case ZONE_B:
                drive.line(FieldPositions.RR);

                appendages.intakeThreeRings();
                drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                drive.line(FieldPositions.RR_B);

                drive.setSpeed(MecanumAutonomous.Speed.FAST);
                drive.line(FieldPositions.RTI_B);

                appendages.shootRings(1);
                appendages.shooterOff();
                break;
            case ZONE_C:
                drive.line(FieldPositions.RR);

                appendages.ringIntakeReverseStart();    //--- knock over stack
                drive.line(FieldPositions.RR_A);

                //TODO: Count rings to stop taking in too many
                appendages.intakeThreeRings();
                drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                drive.line(FieldPositions.RR_B);

                drive.setSpeed(MecanumAutonomous.Speed.FAST);
                drive.line(FieldPositions.RTI_B);
                sleep(2000);                //--- wait for rings to drop into indexer
                appendages.shootRings();
                appendages.shooterOff();
                break;
        }

        //--- Drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RX2);
                sleep(7000);
                drive.line(FieldPositions.RI_WA);
                break;
            case ZONE_B:
                drive.line(FieldPositions.RI_WB);
                break;
            case ZONE_C:
                drive.line(FieldPositions.RI_WC); break;
        }
        appendages.wobbleGoalDrop();

        //--- Park on line
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RX2);
            case ZONE_B:
            case ZONE_C:
                drive.line(FieldPositions.RLS3); break;
        }
    }
}