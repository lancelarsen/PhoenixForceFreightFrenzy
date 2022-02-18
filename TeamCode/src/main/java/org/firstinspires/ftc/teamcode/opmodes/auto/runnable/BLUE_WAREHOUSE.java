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
public class BLUE_WAREHOUSE extends AbstractAuto {
    public void runOpMode() {
        initAuto(AutoUtils.Alliance.BLUE, AutoUtils.StartingPosition.INSIDE);

        // --- Read the barcode position
        telemetry.addData("Capstone index", vision.getCapstoneIndex());
        telemetry.addData("Color level", vision.getColorLevel());
        int barcodePlace = vision.getCapstoneIndex();
        int barcodeOriginal = barcodePlace;
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(13, 62, 0));

        int backwall = 62;
        int backwallAlign = -5;
        int driveDistanceInWarehouse = 48;

        for (int i = 0; i < 3; i++) {

            switch (barcodeOriginal) {
                case 3:
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        backwallAlign = -1;
                    if (i == 2)
                        backwallAlign = 1;
                    break;
                case 2:
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        backwallAlign = -3;
                    if (i == 2)
                        backwallAlign = -1;
                    break;
                case 1:
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        backwallAlign = -3;
                    if (i == 2)
                        backwallAlign = -1;
                    break;
            }

            // --- Move to deploy -- changes based on deploy level
            switch (barcodePlace) {
                case 3:
                    appendages.gondalaHigh();
                    drive.line(new Pose2d(backwallAlign, backwall - 2, 0));
                    if (i > 0) { // --- Give the gondola time to settle down before releasing
                        sleep(500);
                    }
                    break;
                case 2:
                    drive.line(new Pose2d(backwallAlign, backwall - 19, 0));
                    appendages.gondalaMiddle();
                    sleep(500);
                    break;
                case 1:
                    drive.line(new Pose2d(backwallAlign - 2, backwall - 31, 0));
                    appendages.gondalaLow();
                    sleep(500);
                    break;
            }

            appendages.extakeGondola(); // --- Deploy!
            appendages.gondalaDown(); // --- Lower gondola

            drive.line(new Pose2d(14, backwall, 0)); // --- Move back to wall

            barcodePlace = 3; // --- After first block, switch to top

            appendages.intakeBlocksStart();

            if (i < 2) { // --- For first or second block
                drive.line(new Pose2d(driveDistanceInWarehouse, backwall, 0)); // --- Move to blocks

                // --- Collect blocks using sensor to detect block
                while (!appendages.isBlockInGondola() && !isStopRequested()) {
                    // --- Increment distance into warehouse
                    driveDistanceInWarehouse += 2;
                    if (driveDistanceInWarehouse > 60) {
                        driveDistanceInWarehouse = 45;
                        drive.setSpeed(MecanumAutonomous.Speed.FAST);
                    } else {
                        drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                    }

                    try {
                        drive.line(new Pose2d(driveDistanceInWarehouse, backwall, 0));
                    } catch (Exception e) {
                    }
                }

                drive.setSpeed(MecanumAutonomous.Speed.FAST);

                // Don't drive over to drop off block if not enough time to then go park
                if (getGameTime() > 24)
                    break;

            } else { // --- For third block (go park!)
                appendages.intakeBlocksStop();
                drive.line(new Pose2d(60, backwall, 0));
            }

            backwall += 2; // --- Move backwall further away to compensate for strafing
        }

        sleep(4000);
    }
}