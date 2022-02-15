package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appendages.BotAppendages;
import org.firstinspires.ftc.teamcode.drive.MecanumAutonomous;
import org.firstinspires.ftc.teamcode.opmodes.auto.AbstractAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.opmodes.auto.FieldPositions;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

@Autonomous(group = "auto")
public class RED_WAREHOUSE extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.INSIDE);

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex();
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(13, -62, Math.toRadians(180)));

        int backwall = -62;
        int backwallAlign = -7;
        int driveDistanceInWarehouse = 45;

        for (int i = 0; i < 3; i++) {
            if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                backwallAlign = -2;
            if (i == 2)
                backwallAlign = 1;

            // --- Move to deploy -- changes based on deploy level
            switch (barcodePlace) {
                case 3:
                    appendages.gondalaHigh();
                    drive.line(new Pose2d(backwallAlign, backwall + 5 + (i * 2), Math.toRadians(180)));
                    if (i > 0) { // --- Give the gondola time to settle down before releasing
                        sleep(500);
                    }
                    break;
                case 2:
                    drive.line(new Pose2d(backwallAlign, backwall + 19, Math.toRadians(180)));
                    appendages.gondalaMiddle();
                    sleep(500);
                    break;
                case 1:
                    drive.line(new Pose2d(backwallAlign, backwall + 31, Math.toRadians(180)));
                    appendages.gondalaLow();
                    sleep(500);
                    break;
            }

            appendages.extakeGondola(); // --- Deploy!
            appendages.gondalaDown(); // --- Lower gondola

            drive.line(new Pose2d(14, backwall, Math.toRadians(180))); // --- Move back to wall

            barcodePlace = 3; // --- After first block, switch to top

            appendages.intakeBlocksStart();

            if (i < 2) { // --- For first or second block
                drive.line(new Pose2d(driveDistanceInWarehouse, backwall, Math.toRadians(180))); // --- Move to blocks

                // --- Collect blocks using sensor to detect block
                while (!appendages.isBlockInGondola() && !isStopRequested()) {
                    // --- Increment distance into warehouse
                    driveDistanceInWarehouse += 2;
                    if (driveDistanceInWarehouse > 60)
                        driveDistanceInWarehouse = 55;

                    drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                    try {
                        drive.line(new Pose2d(driveDistanceInWarehouse, backwall, Math.toRadians(180)));
                    } catch (Exception e) {
                    }
                }

                drive.setSpeed(MecanumAutonomous.Speed.FAST);

            } else { // --- For third block (go park!)
                appendages.intakeBlocksStop();
                drive.line(new Pose2d(60, backwall, Math.toRadians(180)));
            }

            backwall -= 2; // --- Move backwall further away to compensate for strafing
        }

        sleep(4000);
    }
}