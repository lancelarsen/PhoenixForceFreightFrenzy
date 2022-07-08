package org.firstinspires.ftc.teamcode.appendages;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class AppendagesAutonomous extends BotAppendages {
    private LinearOpMode opMode;

    private volatile double intakeSpeed = 0;

    private Thread gateThread;

    public AppendagesAutonomous(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;
    }

    public void intakeBlocks(boolean reverse) {
        double speed = INTAKE_ROLLER_SPEED;
        if (reverse) {
            speed *= -1;
        }

        setIntakeSpeed(speed);
    }

    public void intakeBlocksStart() {
        intakeBlocks(false);
    }

    public void intakeBlocksReverse() {
        intakeBlocks(true);
    }

    public void intakeBlocksStop() {
        setIntakeSpeed(0);
    }

    private void setIntakeSpeed(double speed) {
        intakeSpeed = speed;

        setFrontIntakeSpeed(-speed);
        setRearIntakeSpeed(speed);
    }

    public double getBlockDistance() {
        return getBlockMeanDistance();
    }

    public void enableIntakeGates() {
        Runnable gateTask = () -> {
            while (!Thread.interrupted() && !opMode.isStopRequested()) {
                boolean gateUp = !canAcceptBlock() || intakeSpeed == 0;

                setFrontGateUp(gateUp);
                setRearGateUp(gateUp);

                if (gateUp && intakeSpeed > 0) {
                    intakeBlocks(true);
                } else if (!gateUp && intakeSpeed != 0) {
                    intakeBlocks(false);
                }

                opMode.telemetry.addData("Can accept block", canAcceptBlock());
                opMode.telemetry.addData("Gates up", gateUp);
                opMode.telemetry.addData("Intake speed", intakeSpeed);
                opMode.telemetry.update();
            }
        };

        gateThread = new Thread(gateTask);
        gateThread.start();
    }

    public void disableIntakeGates() {
        if (gateThread != null)
            gateThread.interrupt();

        setGatesDown();
    }

    public boolean isIntakeGatesEnabled() {
        if (gateThread == null)
            return false;

        return gateThread.isAlive();
    }

    public void gondalaHigh() {
        setGondalaPosition(GONDOLA_LIFTER_HIGH_POSITION);

    }

    public void gondalaMiddle() {
        setGondalaPosition(GONDOLA_LIFTER_MIDDLE_POSITION);

    }

    public void gondalaLow() {
        setGondalaPosition(GONDOLA_LIFTER_LOW_POSITION);
    }

    public void gondalaDown() {
        setGondalaPosition(GONDOLA_LIFTER_DOWN_POSITION);

        // --- Manual down to make sure the gondola is fully down
        gondolaLifter.setPower(-1);
        sleep(250);
    }

    public void extakeGondola() {
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_OPEN_POSITION);
        sleep(1000);
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_CLOSED_POSITION);
    }

    public void gondolaOpen() {
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_OPEN_POSITION);
    }

    public void gondolaClosed() {
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_CLOSED_POSITION);
    }

    public void setGatesUp() {
        setFrontGateUp(true);
        setRearGateUp(true);
    }

    public void setGatesDown() {
        setFrontGateUp(false);
        setRearGateUp(false);
    }

    public void showMessage(String message) {
        opMode.telemetry.addData(message, 0);
        opMode.telemetry.update();
    }
}
