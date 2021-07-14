package org.firstinspires.ftc.teamcode.appendages;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GameConstants;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils.sleep;

public class AppendagesAutonomous extends BotAppendages {
    private final static long GOAL_GRABBER_CLOSE_DELAY = 700;
    private final static long GOAL_GRABBER_OPEN_DELAY = 500;

    private LinearOpMode opMode;
    private Thread lightThread;
    private Thread elevatorThread;
    private Thread intakeThread;

    public AppendagesAutonomous(LinearOpMode opMode) {
        super(opMode.hardwareMap);
        this.opMode = opMode;

        openGoalGrabber(true);
        setReachArmPosition(ReachArmPosition.RETRACTED);
    }

    public void updateLights(AutoUtils.Alliance alliance) {
        if (lightThread != null) lightThread.interrupt();

        Runnable lightTask = () -> {
            while (opMode.opModeIsActive() && !Thread.interrupted()) {
                switch (numRingsInRobot) {
                    case 0:
                        setBlinkinPattern(alliance == AutoUtils.Alliance.BLUE ? BlinkinPatterns.BLUE_BASE_PATTERN : BlinkinPatterns.RED_BASE_PATTERN);
                        break;
                    case 1:
                        setBlinkinPattern(BlinkinPatterns.ONE_RING_PATTERN);
                        break;
                    case 2:
                        setBlinkinPattern(BlinkinPatterns.TWO_RING_PATTERN);
                        break;
                    default:
                        setBlinkinPattern(BlinkinPatterns.THREE_RING_PATTERN);
                        break;
                }
            }
        };

        lightThread = new Thread(lightTask);
        lightThread.start();
    }

    public void asyncUpdateRingsInElevator() {
        if (elevatorThread != null) elevatorThread.interrupt();

        Runnable elevatorTask = () -> {
            while (opMode.opModeIsActive() && !Thread.interrupted()) {
                updateRingsInElevator();
            }
        };

        elevatorThread = new Thread(elevatorTask);
        elevatorThread.start();
    }

    public void intakeThreeRings() {
        interruptIntakeThread();

        setIntakeDirection(Direction.FORWARD);

        Runnable intakeTask = () -> {
            try {
                while (numRingsInRobot < GameConstants.MAX_RINGS_IN_ROBOT && opMode.opModeIsActive() && !Thread.interrupted()) {
                    enableElevator(true);
                    enableIntake(true);
                }

                enableIntake(false);

                Thread.sleep(2000);
                setIntakeDirection(Direction.REVERSE);

                Thread.sleep(2000);
                enableElevator(false);

            } catch (Exception e) {}
        };

        intakeThread = new Thread(intakeTask);
        intakeThread.start();
    }

    public void ringIntakeStart() {
        interruptIntakeThread();

        setIntakeDirection(Direction.FORWARD);
        enableElevator(true);
        enableIntake(true);
    }

    public void ringIntakeReverseStart() {
        interruptIntakeThread();

        setIntakeDirection(Direction.REVERSE);
        enableElevator(true);
        enableIntake(true);
    }

    public void ringIntakeStop() {
        interruptIntakeThread();

        setIntakeDirection(Direction.FORWARD);
        enableElevator(false);
        enableIntake(false);
    }

    private void interruptIntakeThread() {
        if (intakeThread != null) intakeThread.interrupt();
    }

    public void wobbleGoalGrab() {
        wobbleGoalGrab(GoalLifterPosition.MIDDLE);
    }

    public void wobbleGoalGrab(GoalLifterPosition lifterPosition) {
        if (lifterPosition == GoalLifterPosition.DOWN) return;

        openGoalGrabber(false);
        sleep(GOAL_GRABBER_CLOSE_DELAY);
        setGoalLifterPosition(lifterPosition);
    }

    public void wobbleGoalDrop() {
        setGoalLifterPosition(GoalLifterPosition.DOWN);
        while (goalLifter.isBusy()); // Wait for lifter to go down, better than using delay //TODO: Not working
        sleep(500); //TODO: temporary
        openGoalGrabber(true);
        sleep(GOAL_GRABBER_OPEN_DELAY);
    }

    public void extendReachArm() {
        setReachArmPosition(ReachArmPosition.EXTENDED);
    }
}
