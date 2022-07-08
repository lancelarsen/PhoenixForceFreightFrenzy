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

    // --- Settings
    int backwall = -62;
    int distanceBlockDeploy = -7;
    int distanceIntoWarehouse = 44;
    int distanceIntoWarehouseMax = 55;
    int distanceLastBlock = distanceIntoWarehouse;

    String _message = "";

    public void runOpMode() {
        initAuto(AutoUtils.Alliance.RED, AutoUtils.StartingPosition.INSIDE);
        appendages.disableIntakeGates();

        // --- Read the barcode position
        int barcodePlace = vision.getCapstoneIndex();
        int barcodeOriginal = barcodePlace;
        if (barcodePlace == 0)
            barcodePlace = 3; // --- Default if not detected

        // --- Set our starting position
        drive.setSpeed(MecanumAutonomous.Speed.FAST);
        drive.setCurrentPosition(new Pose2d(13, -62, Math.toRadians(180)));

        AddMessage("--- Start ---");
        showCurrentInfo();

        for (int i = 0; i < 3; i++) {

            AddMessage("--- Set backwall distance ---");
            switch (barcodeOriginal) {
                case 3:
                    AddMessage("--- HIGH ---");
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        distanceBlockDeploy = -1;
                    if (i == 2)
                        distanceBlockDeploy = 0;
                    break;
                case 2:
                    AddMessage("--- MIDDLE ---");
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        distanceBlockDeploy = -6; // --- more "-"" is to right
                    if (i == 2)
                        distanceBlockDeploy = -3;
                    break;
                case 1:
                    AddMessage("--- LOW ---");
                    if (i == 1) // --- Move backwall align towards warehouse as it slides when collecting
                        distanceBlockDeploy = -6;
                    if (i == 2)
                        distanceBlockDeploy = -3;
                    break;
            }
            showCurrentInfo();

            // --- Move to deploy -- changes based on deploy level
            switch (barcodePlace) {
                case 3:
                    appendages.gondalaHigh();
                    moveToPosition(distanceBlockDeploy, backwall + 5 + (i * 2), 180, "move to deploy HIGH");
                    if (i > 0) { // --- Give the gondola time to settle down before releasing
                        moveToPosition(distanceBlockDeploy, backwall + 6 + (i * 2), 180, "move to deploy HIGH");
                    }
                    break;
                case 2:
                    moveToPosition(distanceBlockDeploy, backwall + 18, 180, "move to deploy MIDDLE");
                    appendages.gondalaMiddle();
                    moveToPosition(distanceBlockDeploy, backwall + 20, 180, "move to deploy MIDDLE");
                    sleep(200);
                    break;
                case 1:
                    moveToPosition(distanceBlockDeploy + 2, backwall + 30, 180, "move to deploy LOW");
                    appendages.gondalaLow();
                    moveToPosition(distanceBlockDeploy + 2, backwall + 31, 180, "move to deploy LOW");
                    sleep(200);
                    break;
            }

            AddMessage("--- deploy ---");
            sleep(200);
            appendages.extakeGondola(); // --- Deploy!
            appendages.gondalaDown(); // --- Lower gondola

            moveToPosition(14, backwall, 180, "move to backwall");
            if (i < 2) // --- For runs except the last
            {
                appendages.gondalaLow();
                sleep(100);
                appendages.gondalaDown(); // --- Lower gondola
                sleep(100);
            }

            barcodePlace = 3; // --- After first block, switch to top

            appendages.intakeBlocksStart();
            appendages.enableIntakeGates();

            if (i < 2) { // --- For first or second block
                moveToPosition(distanceLastBlock, backwall, 180, "move to warehouse");

                // --- Collect blocks using sensor to detect block
                while (!appendages.isBlockInGondola() && !isStopRequested()) {
                    // --- Increment distance into warehouse
                    distanceIntoWarehouse += 2;
                    if (distanceIntoWarehouse > distanceIntoWarehouseMax) {
                        AddMessage("--- TOO far in warehouse! Backup ---");
                        drive.setSpeed(MecanumAutonomous.Speed.FAST);
                        appendages.intakeBlocksReverse();
                        distanceIntoWarehouse = distanceIntoWarehouse - 10;
                        moveToPosition(distanceIntoWarehouse, backwall, 180, "move back");
                        sleep(100);
                        distanceIntoWarehouse += 2;
                        // distanceIntoWarehouse = distanceLastBlock + 5;
                        distanceIntoWarehouseMax += 3; // --- Increase due to slippage
                        appendages.intakeBlocksStart();
                    }

                    showCurrentInfo();
                    drive.setSpeed(MecanumAutonomous.Speed.SLOW);
                    moveToPosition(distanceIntoWarehouse, backwall, 180, "move to blocks");
                }

                // --- Found block!
                AddMessage("--- found block! ---");
                appendages.intakeBlocksReverse();
                distanceLastBlock = distanceIntoWarehouse;
                showCurrentInfo();
                drive.setSpeed(MecanumAutonomous.Speed.FAST);

                // --- Don't drive over to drop off block if not enough time to then go park
                if (getGameTime() > 25) {
                    AddMessage("--- less than time, don't leave warehouse ---");
                    appendages.intakeBlocksStop();
                    moveToPosition(distanceIntoWarehouseMax + 3, backwall, 180, "move to park");
                    break;
                }

            } else { // --- For third block (go park!)
                appendages.intakeBlocksStop();
                moveToPosition(distanceIntoWarehouseMax + 3, backwall, 180, "move to park");
                appendages.gondalaLow();
                sleep(100);
                appendages.gondalaDown(); // --- Lower gondola
            }

            appendages.disableIntakeGates();

            backwall -= 3; // --- Move backwall further away to compensate for strafing
            showCurrentInfo();
        }

        sleep(10000);
    }

    private void moveToPosition(double x, double y, double angle, String message) {
        AddMessage("[" + x + " / " + y + " / " + angle + "] => " + message);
        try {
            drive.line(new Pose2d(x, y, Math.toRadians(angle)));
        } catch (Exception e) {
        }
    }

    private void showCurrentInfo() {
        AddMessage("[BLK: " + distanceBlockDeploy
                + " WH: " + distanceIntoWarehouse
                + " (" + distanceLastBlock
                + ") BW: " + backwall + "]");
    }

    private void AddMessage(String value) {
        double time = Math.round(getGameTime() * 100.0) / 100.0;
        _message = "(" + time + ") " + value + "\n" + _message;
        appendages.showMessage(_message);
    }
}