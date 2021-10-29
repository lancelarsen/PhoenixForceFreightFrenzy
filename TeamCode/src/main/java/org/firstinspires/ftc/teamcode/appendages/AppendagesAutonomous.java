package org.firstinspires.ftc.teamcode.appendages;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class AppendagesAutonomous extends BotAppendages {
    private LinearOpMode opMode;

    public AppendagesAutonomous(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;
    }
}
