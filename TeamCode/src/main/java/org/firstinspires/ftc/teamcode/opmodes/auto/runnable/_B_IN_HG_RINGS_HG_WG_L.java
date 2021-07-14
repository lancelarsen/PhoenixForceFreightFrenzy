package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Autonomous(group = "auto")
public class _B_IN_HG_RINGS_HG_WG_L extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        //--- Detect number of rings
        RingVision.TargetZone targetZone = ringVision.getTargetZone();

        appendages.ringIntakeStart();

        //--- Grab wobble goal
        drive.line(FieldPositions.BSI_W);
        appendages.wobbleGoalGrab();

        appendages.ringIntakeStop();

        //--- Shoot top goal
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.HIGH_GOAL);
        drive.curve(FieldPositions.BTI);
        appendages.shootRings();

        //--- Go get rings and shoot!
        switch (targetZone) {
            case ZONE_A:
                break;
            case ZONE_B:
                drive.line(FieldPositions.BR);

                appendages.intakeThreeRings();
                drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                drive.line(FieldPositions.BR_B);

                drive.setSpeed(MecanumAutonomous.Speed.FAST);
                drive.line(FieldPositions.BTI_B);

                appendages.shootRings(1);
                appendages.shooterOff();
                break;
            case ZONE_C:
                drive.line(FieldPositions.BR);

                appendages.ringIntakeReverseStart();    //--- knock over stack
                drive.line(FieldPositions.BR_A);

                //TODO: Count rings to stop taking in too many
                appendages.intakeThreeRings();
                drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                drive.line(FieldPositions.BR_B);

                drive.setSpeed(MecanumAutonomous.Speed.FAST);
                drive.line(FieldPositions.BTI_B);
                sleep(2000);                //--- wait for rings to drop into indexer
                appendages.shootRings();
                appendages.shooterOff();
                break;
        }

        //--- Drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.BX2);
                sleep(7000);
                drive.line(FieldPositions.BI_WA);
                break;
            case ZONE_B:
                drive.line(FieldPositions.BI_WB);
                break;
            case ZONE_C:
                drive.line(FieldPositions.BI_WC); break;
        }
        appendages.wobbleGoalDrop();

        //--- Park on line
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.BX2);
            case ZONE_B:
            case ZONE_C:
                drive.line(FieldPositions.BLS3); break;
        }
   }
}