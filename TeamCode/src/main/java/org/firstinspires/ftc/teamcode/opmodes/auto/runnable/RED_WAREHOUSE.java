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
        int driveDistanceInWarehouse = 45;

        for (int i = 0; i < 3; i++) {

            // --- Move to deploy -- changes based on deploy level
            appendages.setGatesUp();
            switch (barcodePlace) {
                case 3:
                    appendages.gondalaHigh();
                    drive.line(new Pose2d(-7, backwall + 3, Math.toRadians(180)));
                    break;
                case 2:
                    drive.line(new Pose2d(-7, backwall + 22, Math.toRadians(180)));
                    appendages.gondalaMiddle();
                    break;
                case 1:
                    drive.line(new Pose2d(-7, backwall + 32, Math.toRadians(180)));
                    appendages.gondalaLow();
                    break;
            }

            // --- Deploy!
            // sleep(500);
            appendages.extakeGondola();
            // sleep(500);
            // --- Lower gondola
            appendages.gondalaDown();
            appendages.setGatesDown();

            // --- Move back to wall
            // drive.line(new Pose2d(-7, backwall, Math.toRadians(180)));
            drive.line(new Pose2d(10, backwall, Math.toRadians(180)));

            // --- After first block, switch to top
            barcodePlace = 3;

            appendages.intakeBlocksStart();

            // --- For first or second block
            if (i < 2) {
                // --- Move to blocks
                drive.line(new Pose2d(driveDistanceInWarehouse, backwall, Math.toRadians(180)));

                // --- Collect blocks using sensor to detect block
                while (!appendages.isBlockInGondola() && !isStopRequested()) {
                    // --- Increment distance into warehouse
                    driveDistanceInWarehouse += 1;
                    if (driveDistanceInWarehouse > 62)
                        driveDistanceInWarehouse = 58;

                    drive.setSpeed(MecanumAutonomous.Speed.VERY_SLOW);
                    drive.line(new Pose2d(driveDistanceInWarehouse, backwall, Math.toRadians(180)));

                    telemetry.addData("Drive warehouse", driveDistanceInWarehouse);
                    telemetry.addData("Is block in gondola", appendages.isBlockInGondola());
                    telemetry.addData("Distance", appendages.getBlockDistance());
                    telemetry.update();
                }

                drive.setSpeed(MecanumAutonomous.Speed.FAST);

            } else { // --- For third block (go park!)
                drive.line(new Pose2d(driveDistanceInWarehouse + 3, backwall, Math.toRadians(180)));
            }

            backwall -= 2; // --- Move backwall further away to compensate for strafing
        }

        appendages.intakeBlocksStop();

        sleep(10000);
    }
}