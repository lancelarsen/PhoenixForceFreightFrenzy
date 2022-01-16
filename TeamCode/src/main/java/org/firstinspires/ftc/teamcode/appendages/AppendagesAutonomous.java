package org.firstinspires.ftc.teamcode.appendages;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
import org.firstinspires.ftc.teamcode.appendages.utils.EncoderUtil;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class AppendagesAutonomous extends BotAppendages {
    private LinearOpMode opMode;

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
        setFrontIntakeSpeed(-speed);
        setRearIntakeSpeed(speed);
    }

    public void gondalaHigh() {
        double position = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_UP_POSITION);
        setGondalaPosition(position);
    }

    public void gondalaMiddle() {
        double position = 1000;
        setGondalaPosition(position);
    }

    public void gondalaLow() {
        double position = 500;
        setGondalaPosition(position);
    }

    public void gondalaDown() {
        double position = EncoderUtil.inchesToTicks(EncoderUtil.Motor.GOBILDA_5202, GONDOLA_LIFTER_DOWN_POSITION);
        setGondalaPosition(position);
    }

    public void extakeGondola() {
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_OPEN_POSITION);
        sleep(1000);
        gondolaDeployer.setPosition(GONDOLA_DEPLOYER_CLOSED_POSITION);
    }
}
