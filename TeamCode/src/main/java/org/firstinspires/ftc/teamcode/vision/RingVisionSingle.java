package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

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

public class RingVisionSingle {
    HardwareMap hardwareMap;

    OpenCvCamera webcam;
    RingVisionPipeline pipeline;

    public enum RingCount {
        FOUR,
        ONE,
        ZERO
    }

    public RingVisionSingle(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RingVisionPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(() ->
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        );
    }

    public void setViewportPaused(boolean paused) {
        pipeline.setViewportPaused(paused);
    }

    public boolean isViewportPaused() {
        return pipeline.viewportPaused;
    }

    public int getColorLevel() {
        return pipeline.averageColorLevel;
    }

    public RingCount getRingCount() {
        return pipeline.ringCount;
    }

    private class RingVisionPipeline extends OpenCvPipeline {
        Scalar BLUE = new Scalar(0, 0, 255);
        Scalar GREEN = new Scalar(0, 255, 0);
        Scalar RED = new Scalar(255, 0, 0);

        int FOUR_RING_THRESHOLD = 138;
        int ONE_RING_THRESHOLD = 130;

        //Point regionPointA = new Point(142,106);
        //Point regionPointB = new Point(170,126);

        //--- Red - Left Starting (Far Left of Line)
        //Point regionPointA = new Point(235,126);
        //Point regionPointB = new Point(263,146);

        //--- Red - Right Starting (Far Right of Line) -- change thresholds
        Point regionPointA = new Point(10,116);
        Point regionPointB = new Point(36,136);

        Mat regionCb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        private boolean viewportPaused;
        private int averageColorLevel;
        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile RingCount ringCount = RingCount.FOUR;

        /*
        Takes the RGB, converts to YCrCb, and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat input) {
            inputToCb(input);
            regionCb = Cb.submat(new Rect(regionPointA, regionPointB));
        }

        void drawDetectionPreview(Mat input) {

            // Draw box
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    regionPointA, // First point which defines the rectangle
                    regionPointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            // Draw target
            int lineLength = 50;
            Imgproc.line(input,
                    new Point(regionPointA.x + Math.round((regionPointB.x - regionPointA.x) / 2), regionPointA.y - lineLength + 2),
                    new Point(regionPointA.x + Math.round((regionPointB.x - regionPointA.x) / 2),regionPointA.y - 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionPointA.x + Math.round((regionPointB.x - regionPointA.x) / 2), regionPointB.y + lineLength + 2),
                    new Point(regionPointA.x + Math.round((regionPointB.x - regionPointA.x) / 2),regionPointB.y + 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionPointA.x - lineLength + 2, regionPointA.y + Math.round((regionPointB.y - regionPointA.y) / 2)),
                    new Point(regionPointA.x - 2, regionPointA.y + Math.round((regionPointB.y - regionPointA.y) / 2)),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionPointB.x + lineLength + 2, regionPointA.y + Math.round((regionPointB.y - regionPointA.y) / 2)),
                    new Point(regionPointB.x + 2, regionPointA.y + Math.round((regionPointB.y - regionPointA.y) / 2)),
                    RED, 2);

            // Draw text
            Scalar displayColor = RED;
            if (pipeline.ringCount == RingCount.FOUR) {
                displayColor = GREEN;
            } else if (pipeline.ringCount == RingCount.ONE) {
                displayColor = BLUE;
            }
            Imgproc.putText(input, ringCount + " RING(S)", new Point(75, 190), 1, 2, displayColor, 2);
            Imgproc.putText(input, String.valueOf(averageColorLevel), new Point(135, 230), 1, 2, displayColor, 2);
        }

        @Override
        public Mat processFrame(Mat input) {

            // Calculate color level
            inputToCb(input);
            averageColorLevel = (int) Core.mean(regionCb).val[0];

            ringCount = RingCount.FOUR;
            if (averageColorLevel > FOUR_RING_THRESHOLD) {
                ringCount = RingCount.FOUR;
            } else if (averageColorLevel > ONE_RING_THRESHOLD) {
                ringCount = RingCount.ONE;
            } else {
                ringCount = RingCount.ZERO;
            }

            drawDetectionPreview(input);

            return input;
        }

        @Override
        public void onViewportTapped() {
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

            setViewportPaused(!viewportPaused);
        }

        public void setViewportPaused(boolean paused) {
            viewportPaused = paused;

            if(viewportPaused) {
                webcam.pauseViewport();
            } else {
                webcam.resumeViewport();
            }
        }
    }
}
