/*
 * EasyOpenCV
 * Examples: https://github.com/OpenFTC/EasyOpenCV/tree/master/examples/src/main/java/org/openftc/easyopencv/examples
 */

package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

@SuppressWarnings("SpellCheckingInspection")
@Disabled
@TeleOp(name = "Vision Ring Count", group = "Test")
public class VisionRingCount extends LinearOpMode
{
    OpenCvCamera webcam;
    SamplePipeline pipeline;

    /*----------------------------------------------------------------------*/
    /* Enumerations */
    /*----------------------------------------------------------------------*/
    public enum RingPosition
    {
        FOUR,
        ONE,
        NONE
    }

    @Override
    public void runOpMode()
    {
        /*----------------------------------------------------------------------*/
        /* Configure Webcam */
        /*----------------------------------------------------------------------*/
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new SamplePipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(() ->
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        );

        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.colorLevel());
            telemetry.addData("Position", pipeline.position);
            telemetry.update();

            /*
             * For the purposes of this sample, throttle ourselves to 10Hz loop to avoid burning
             * excess CPU cycles for no reason. (By default, telemetry is only sent to the DS at 4Hz
             * anyway). Of course in a real OpMode you will likely not want to do this.
             */
            sleep(100);
        }
    }

    class SamplePipeline extends OpenCvPipeline
    {
        boolean viewportPaused;

        /*----------------------------------------------------------------------*/
        /* Color Constants  */
        /*----------------------------------------------------------------------*/
        Scalar BLUE = new Scalar(0,0,255);
        Scalar GREEN = new Scalar(0,255,0);
        Scalar RED = new Scalar(255,0,0);

        /*----------------------------------------------------------------------*/
        /* Threshold Levels  */
        /*----------------------------------------------------------------------*/
        int FOUR_RING_THRESHOLD = 138;
        int ONE_RING_THRESHOLD = 130;

        /*----------------------------------------------------------------------*/
        /* Dimensions of square being scanned  */
        /*----------------------------------------------------------------------*/
        Point region1_pointA = new Point(142,106);
        Point region1_pointB = new Point(170,126);

        /*----------------------------------------------------------------------*/
        /* Variables  */
        /*----------------------------------------------------------------------*/
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        int averageColorLevel;

        //--- Volatile since accessed by OpMode thread w/o synchronization
        private volatile RingPosition position = RingPosition.FOUR;

        /*----------------------------------------------------------------------*/
        /* This function takes the RGB frame, converts to YCrCb,  */
        /* and extracts the Cb channel to the 'Cb' variable       */
        /*----------------------------------------------------------------------*/
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat input)
        {
            inputToCb(input);
            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input)
        {
            /*----------------------------------------------------------------------*/
            /* Calculate color levels  */
            /*----------------------------------------------------------------------*/
            inputToCb(input);
            averageColorLevel = (int) Core.mean(region1_Cb).val[0];

            /*----------------------------------------------------------------------*/
            /* Compare threshold to determine ring count  */
            /*----------------------------------------------------------------------*/
            position = RingPosition.FOUR;
            if (averageColorLevel > FOUR_RING_THRESHOLD) { position = RingPosition.FOUR; }
            else if (averageColorLevel > ONE_RING_THRESHOLD) { position = RingPosition.ONE; }
            else { position = RingPosition.NONE; }

            /*----------------------------------------------------------------------*/
            /* Draw Box  */
            /*----------------------------------------------------------------------*/
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            /*----------------------------------------------------------------------*/
            /* Draw Target  */
            /*----------------------------------------------------------------------*/
            int lineLength = 50;
            Imgproc.line(input,
                    new Point(region1_pointA.x + Math.round((region1_pointB.x - region1_pointA.x) / 2), region1_pointA.y - lineLength + 2),
                    new Point(region1_pointA.x + Math.round((region1_pointB.x - region1_pointA.x) / 2),region1_pointA.y - 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(region1_pointA.x + Math.round((region1_pointB.x - region1_pointA.x) / 2), region1_pointB.y + lineLength + 2),
                    new Point(region1_pointA.x + Math.round((region1_pointB.x - region1_pointA.x) / 2),region1_pointB.y + 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(region1_pointA.x - lineLength + 2, region1_pointA.y + Math.round((region1_pointB.y - region1_pointA.y) / 2)),
                    new Point(region1_pointA.x - 2, region1_pointA.y + Math.round((region1_pointB.y - region1_pointA.y) / 2)),
                    RED, 2);
            Imgproc.line(input,
                    new Point(region1_pointB.x + lineLength + 2, region1_pointA.y + Math.round((region1_pointB.y - region1_pointA.y) / 2)),
                    new Point(region1_pointB.x + 2, region1_pointA.y + Math.round((region1_pointB.y - region1_pointA.y) / 2)),
                    RED, 2);

            /*----------------------------------------------------------------------*/
            /* Draw Text  */
            /*----------------------------------------------------------------------*/
            Scalar displayColor = RED;
            if (pipeline.position == RingPosition.FOUR)
            {
                displayColor = GREEN;
            } else if (pipeline.position == RingPosition.ONE)
            {
                displayColor = BLUE;
            }
            Imgproc.putText(input, String.valueOf(pipeline.position) + " RING(S)", new Point(75, 190), 1, 2, displayColor, 2);
            Imgproc.putText(input, String.valueOf(pipeline.colorLevel()), new Point(135, 230), 1, 2, displayColor, 2);

             return input;
        }

        public int colorLevel()
        {
            return averageColorLevel;
        }

        @Override
        public void onViewportTapped()
        {
            /*
             * The viewport (if one was specified in the constructor) can also be dynamically "paused"
             * and "resumed". The primary use case of this is to reduce CPU, memory, and power load
             * when you need your vision pipeline running, but do not require a live preview on the
             * robot controller screen. For instance, this could be useful if you wish to see the live
             * camera preview as you are initializing your robot, but you no longer require the live
             * preview after you have finished your initialization process; pausing the viewport does
             * not stop running your pipeline.
             *
             * Here we demonstrate dynamically pausing/resuming the viewport when the user taps it
             */

            viewportPaused = !viewportPaused;

            if(viewportPaused)
            {
                webcam.pauseViewport();
            }
            else
            {
                webcam.resumeViewport();
            }
        }
    }
}