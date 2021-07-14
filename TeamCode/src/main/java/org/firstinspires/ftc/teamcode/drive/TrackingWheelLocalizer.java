package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.drive.roadrunnerUtils.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class TrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    //--- Rev through bore encoder -- https://www.revrobotics.com/rev-11-1271/
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.7874; // in (2cm radius)
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    //--- 15 inches is measured, but using TrackingWheelLateralDistanceTuner opmode - we calc as 15.7
    public static double LATERAL_DISTANCE = 15.7; // in; distance between the left and right wheels

    public static double FORWARD_OFFSET = -7.5; // in; offset of the lateral wheel

    //--- Calculated with the LocalizationTest opmode and moving the robot 100' forward manually and dividing 100/distance in opmode
    public static double X_MULTIPLIER = 0.941; // Multiplier in the X direction
    public static double Y_MULTIPLIER = 0.942; // Multiplier in the Y direction

    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public TrackingWheelLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "lr"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rr"));
        //--- Using encoder port from this motor as we aren't using an encoder on the roller
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intakeRoller"));

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
        leftEncoder.setDirection(Encoder.Direction.REVERSE);
        rightEncoder.setDirection(Encoder.Direction.REVERSE);
        frontEncoder.setDirection(Encoder.Direction.FORWARD);
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity() * X_MULTIPLIER),
                encoderTicksToInches(rightEncoder.getCorrectedVelocity() * X_MULTIPLIER),
                encoderTicksToInches(frontEncoder.getCorrectedVelocity() * Y_MULTIPLIER)
        );
    }
}
