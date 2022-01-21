package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.opmodes.auto.AutoUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarcodeVision {

    private class DetectionRegion {
        private Point topLeft;
        private Point bottomRight;

        public DetectionRegion(
                int topLeftX,
                int topLeftY,
                int width,
                int height) {
            topLeft = new Point(topLeftX, topLeftY);
            bottomRight = new Point(topLeftX + width, topLeftY + height);
        }

        public void drawDetectionPreview(Mat input, int regionIndex) {
            // Draw box
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    topLeft, // First point which defines the rectangle
                    bottomRight, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            Imgproc.putText(input, String.valueOf(regionIndex), new Point(topLeft.x, topLeft.y - 5), 1, 1.5, BLUE, 2);
        }

        public int getColorLevel(Mat Cb) {
            Mat region = Cb.submat(new Rect(topLeft, bottomRight));
            return (int) Core.mean(region).val[0];
        }
    }

    private HardwareMap hardwareMap;

    private OpenCvCamera webcam;
    private RingVisionPipeline pipeline;

    private static final Scalar BLUE = new Scalar(0, 0, 255);
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final Scalar RED = new Scalar(255, 0, 0);

    private AutoUtils.Alliance alliance;
    private AutoUtils.StartingPosition startingPosition;

    private DetectionRegion detectionRegions[] = new DetectionRegion[3];

    public BarcodeVision(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void init(AutoUtils.Alliance alliance, AutoUtils.StartingPosition startingPosition) {
        this.alliance = alliance;
        this.startingPosition = startingPosition;

        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier(
                        "cameraMonitorViewId",
                        "id",
                        hardwareMap.appContext.getPackageName());

        String webcamName;
        if (alliance == AutoUtils.Alliance.RED ^ startingPosition == AutoUtils.StartingPosition.INSIDE) {
            webcamName = "frontWebcam";
        } else {
            webcamName = "rearWebcam";
        }

        webcam = OpenCvCameraFactory.getInstance()
                .createWebcam(
                        hardwareMap.get(WebcamName.class,
                                webcamName),
                        cameraMonitorViewId);

        pipeline = new RingVisionPipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        // --- Red - Ouside (toward wall)
        if (alliance == AutoUtils.Alliance.RED && startingPosition == AutoUtils.StartingPosition.OUTSIDE) {
            detectionRegions[0] = new DetectionRegion(40, 75, 60, 80);
            detectionRegions[1] = new DetectionRegion(160, 75, 60, 80);
            detectionRegions[2] = new DetectionRegion(260, 75, 60, 80);

            // --- Red - Inside (toward barrier)
        } else if (alliance == AutoUtils.Alliance.RED && startingPosition == AutoUtils.StartingPosition.INSIDE) {
            detectionRegions[0] = new DetectionRegion(0, 60, 60, 80);
            detectionRegions[1] = new DetectionRegion(110, 60, 60, 80);
            detectionRegions[2] = new DetectionRegion(240, 60, 60, 80);

            // --- Blue - Outside (toward wall)
        } else if (alliance == AutoUtils.Alliance.BLUE && startingPosition == AutoUtils.StartingPosition.OUTSIDE) {
            detectionRegions[0] = new DetectionRegion(0, 40, 60, 80);
            detectionRegions[1] = new DetectionRegion(110, 40, 60, 80);
            detectionRegions[2] = new DetectionRegion(240, 40, 60, 80);

            // --- Blue - Inside (toward barrier)
        } else if (alliance == AutoUtils.Alliance.BLUE && startingPosition == AutoUtils.StartingPosition.INSIDE) {
            detectionRegions[0] = new DetectionRegion(0, 75, 60, 80);
            detectionRegions[1] = new DetectionRegion(110, 75, 60, 80);
            detectionRegions[2] = new DetectionRegion(240, 75, 60, 80);
        }

        setViewportPaused(false);
    }

    private class RingVisionPipeline extends OpenCvPipeline {
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        boolean viewportPaused;

        int colorLevel;
        int capstoneIndex;

        // ----------------------------------------------------------------------
        // --- Takes the RGB, converts to YCrCb, and extracts the Cb channel to the 'Cb'
        // variable
        // ----------------------------------------------------------------------
        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat input) {
            inputToCb(input);
        }

        void drawDetectionInfo(Mat input) {

            String locationString = alliance.toString() + " ALLIANCE "
                    + (startingPosition == startingPosition.OUTSIDE ? "OUT" : "IN");

            Imgproc.putText(input, locationString, new Point(5, 180), 1, 1.5, RED, 2);
            Imgproc.putText(input, "INDEX: " + capstoneIndex, new Point(5, 205), 1, 1.5, RED, 2);
            Imgproc.putText(input, "COLOR LVL: " + String.valueOf(colorLevel), new Point(5, 230), 1, 1.5, RED, 2);
        }

        @Override
        public Mat processFrame(Mat input) {
            // Calculate color level
            inputToCb(input);

            colorLevel = 0;
            for (int i = 0; i < detectionRegions.length; i++) {

                detectionRegions[i].drawDetectionPreview(input, i + 1);
                int newColorLevel = detectionRegions[i].getColorLevel(Cb);

                if (newColorLevel > colorLevel) {

                    capstoneIndex = i + 1;
                    colorLevel = newColorLevel;
                }
            }

            drawDetectionInfo(input);

            return input;
        }

        @Override
        public void onViewportTapped() {
            setViewportPaused(!viewportPaused);
        }

        public void setViewportPaused(boolean paused) {
            viewportPaused = paused;

            if (viewportPaused) {
                webcam.pauseViewport();
            } else {
                webcam.resumeViewport();
            }
        }
    }

    public void setViewportPaused(boolean paused) {
        pipeline.setViewportPaused(paused);
    }

    public boolean isViewportPaused() {
        return pipeline.viewportPaused;
    }

    public int getColorLevel() {
        return pipeline.colorLevel;
    }

    public int getCapstoneIndex() {
        return pipeline.capstoneIndex;
    }
}
