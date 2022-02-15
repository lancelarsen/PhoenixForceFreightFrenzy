package org.firstinspires.ftc.teamcode.appendages;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class AppendagesAutonomous extends BotAppendages {
    private LinearOpMode opMode;
    private volatile double intakeSpeed = 0;

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

        Thread gateThread = new Thread(gateTask);
        gateThread.start();
    }

    public void gondalaHigh() {
        double position = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_UP_POSITION);
        setGondalaPosition(position);
    }

    public void gondalaMiddle() {
        double position = 1250;
        setGondalaPosition(position);
    }

    public void gondalaLow() {
        double position = 300;
        setGondalaPosition(position);
    }

    public void gondalaDown() {
        double position = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_DOWN_POSITION);
        setGondalaPosition(position);

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
}
