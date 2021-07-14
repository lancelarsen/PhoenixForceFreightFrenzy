package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;
import org.firstinspires.ftc.teamcode.vision.RingVision;

@Disabled
@Autonomous(group = "auto")
public class R_OUT_PS_WGx2_L extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.OUTSIDE);

        //--- Detect number of rings
        RingVision.TargetZone targetZone = ringVision.getTargetZone();
        targetZone = targetZone.ZONE_C; //--- TEMPORARY

        //--- Grab wobble goal
        drive.line(FieldPositions.RSO_W);
        appendages.wobbleGoalGrab();

        //--- Shoot power shots
        appendages.setShooterSpeed(BotAppendages.ShooterSpeed.POWERSHOTS);
        drive.line(FieldPositions.P6);
        shootPowershots(FieldPositions.P6A);
        appendages.shooterOff();

        //--- Drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RWA_O2); break;
            case ZONE_B:
                drive.line(FieldPositions.RWB_O2); break;
            case ZONE_C:
                drive.line(FieldPositions.RWC_O2); break;
        }
        appendages.wobbleGoalDrop();

        //--- Get 2nd wobble goal and drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RWA_O2); break;
            case ZONE_B:
                drive.line(FieldPositions.RWB_O2); break;
            case ZONE_C:
                //--- Go back to grab the other wobble goal
                drive.line(FieldPositions.S3G_1);
                drive.line(FieldPositions.S3G_2);
                drive.line(FieldPositions.S3G_3);
                appendages.wobbleGoalGrab();
                break;
        }

        //--- Drop wobble goal
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RWA_O2); break;
            case ZONE_B:
                drive.line(FieldPositions.RWB_O2); break;
            case ZONE_C:
                drive.line(FieldPositions.W6O_2); break;
        }
        appendages.wobbleGoalDrop();

        //--- Park on line
        switch (targetZone) {
            case ZONE_A:
                drive.line(FieldPositions.RWA_O2); break;
            case ZONE_B:
                drive.line(FieldPositions.RWB_O2); break;
            case ZONE_C:
                drive.line(FieldPositions.S3G_4); break;
        }

//        //--- Return to start
//        sleep(5000);
//        drive.setSpeed(MecanumAutonomous.Speed.MEDIUM);
//        drive.line(FieldPositions.S4);
//        appendages.wobbleGoalDrop();
    }
}
