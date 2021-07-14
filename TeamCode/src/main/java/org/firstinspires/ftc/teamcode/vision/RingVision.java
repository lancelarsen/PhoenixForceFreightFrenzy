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

public class RingVision {
    HardwareMap hardwareMap;

    OpenCvCamera webcam;
    RingVisionPipeline pipeline;

    public enum RingCount { FOUR, ONE, ZERO }
    public enum TargetZone { ZONE_A, ZONE_B, ZONE_C }

    private static final Scalar BLUE = new Scalar(0, 0, 255);
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final Scalar RED = new Scalar(255, 0, 0);

    private int _thresholdRingsFour = 0;
    private int _thresholdRingsOne = 0;
    private Point _regionTopLeft = new Point(0,0);
    private Point _regionBottomRight = new Point(0,0);
    AutoUtils.Alliance _alliance;
    AutoUtils.StartingPosition _startingPosition;

    public RingVision(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void init(AutoUtils.Alliance alliance, AutoUtils.StartingPosition startingPosition) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RingVisionPipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(() ->
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
        );

        _alliance = alliance;
        _startingPosition = startingPosition;

        int boxHeight = 0;
        int boxWidth = 0;
        int boxX = 0;
        int boxY = 0;

        //--- Red - Right Starting (Far Right of Line)
        if (alliance == AutoUtils.Alliance.RED && startingPosition == AutoUtils.StartingPosition.OUTSIDE) {
            _thresholdRingsFour = 138;
            _thresholdRingsOne = 128;
            boxX = 10;
            boxY = 116;
            boxWidth = 26;
            boxHeight = 17;
        }
        //--- Red - Left Starting (Far Left of Line)
        else if (alliance == AutoUtils.Alliance.RED && startingPosition == AutoUtils.StartingPosition.INSIDE) {
            _thresholdRingsFour = 138;
            _thresholdRingsOne = 132;
            boxX = 235;
            boxY = 126;
            boxWidth = 28;
            boxHeight = 20;
        }
        //--- Blue - Right Starting (Far Right of Line)
        else if (alliance == AutoUtils.Alliance.BLUE && startingPosition == AutoUtils.StartingPosition.INSIDE) {
            _thresholdRingsFour = 138;
            _thresholdRingsOne = 128;
            boxX = 10;
            boxY = 116;
            boxWidth = 26;
            boxHeight = 20;
        }
        //--- Blue - Left Starting (Center of Line)
        else if (alliance == AutoUtils.Alliance.BLUE && startingPosition == AutoUtils.StartingPosition.OUTSIDE) {
            _thresholdRingsFour = 138;
            _thresholdRingsOne = 130;
            boxX = 235;
            boxY = 126;
            boxWidth = 28;
            boxHeight = 17;
        }

        _regionTopLeft = new Point(boxX, boxY);
        _regionBottomRight = new Point(boxX + boxWidth, boxY + boxHeight);

        setViewportPaused(false);
    }

    private class RingVisionPipeline extends OpenCvPipeline {
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        private boolean _viewportPaused;
        private int _averageColorLevel;
        private volatile RingCount _ringCount = RingCount.FOUR; //--- Volatile since accessed by OpMode thread w/o synchronization

        private List<SearchRegion> _regionsToSearch = new ArrayList<>();
        private List<RingsFound> _ringsFound = new ArrayList<>();

        //----------------------------------------------------------------------
        //--- Takes the RGB, converts to YCrCb, and extracts the Cb channel to the 'Cb' variable
        //----------------------------------------------------------------------
        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat input) {
            inputToCb(input);
            AddMatrixOfSearchLocations(10, 4);
        }

        private void AddMatrixOfSearchLocations(int widthX, int widthY)
        {
            for (int x = -1 * widthX; x < widthX + 1; x++)
            {
                for (int y = -1 * widthY; y < widthY + 1; y++)
                {
                    _regionsToSearch.add(AddRegionToSearch(x, y));
                }
            }
        }

        private SearchRegion AddRegionToSearch(int offsetX, int offsetY)
        {
            SearchRegion searchRegion = new SearchRegion();
            searchRegion.topLeft = new Point(_regionTopLeft.x + offsetX, _regionTopLeft.y + offsetY);
            searchRegion.bottomRight = new Point(_regionBottomRight.x + offsetX, _regionBottomRight.y + offsetY);
            return searchRegion;
        }

        void drawDetectionPreview(Mat input, RingCount count, int colorLevel, Point regionTopLeft, Point regionBottomRight) {

            // Draw box
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    regionTopLeft, // First point which defines the rectangle
                    regionBottomRight, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            // Draw target
            int lineLength = 50;
            Imgproc.line(input,
                    new Point(regionTopLeft.x + Math.round((regionBottomRight.x - regionTopLeft.x) / 2), regionTopLeft.y - lineLength + 2),
                    new Point(regionTopLeft.x + Math.round((regionBottomRight.x - regionTopLeft.x) / 2), regionTopLeft.y - 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionTopLeft.x + Math.round((regionBottomRight.x - regionTopLeft.x) / 2), regionBottomRight.y + lineLength + 2),
                    new Point(regionTopLeft.x + Math.round((regionBottomRight.x - regionTopLeft.x) / 2), regionBottomRight.y + 2),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionTopLeft.x - lineLength + 2, regionTopLeft.y + Math.round((regionBottomRight.y - regionTopLeft.y) / 2)),
                    new Point(regionTopLeft.x - 2, regionTopLeft.y + Math.round((regionBottomRight.y - regionTopLeft.y) / 2)),
                    RED, 2);
            Imgproc.line(input,
                    new Point(regionBottomRight.x + lineLength + 2, regionTopLeft.y + Math.round((regionBottomRight.y - regionTopLeft.y) / 2)),
                    new Point(regionBottomRight.x + 2, regionTopLeft.y + Math.round((regionBottomRight.y - regionTopLeft.y) / 2)),
                    RED, 2);

            // Draw text
            Scalar displayColor = RED;
            if (count == RingCount.FOUR) {
                displayColor = GREEN;
            } else if (count == RingCount.ONE) {
                displayColor = BLUE;
            }
            Imgproc.putText(input, count + " RINGS", new Point(75, 190), 1, 2, displayColor, 2);
            Imgproc.putText(input, String.valueOf(colorLevel), new Point(135, 230), 1, 2, displayColor, 2);

            if (_alliance == AutoUtils.Alliance.RED)
            {
                Imgproc.putText(input, "RED ALLIANCE", new Point(5, 30), 1, 1.5, RED, 2);
            }
            else
            {
                Imgproc.putText(input, "BLUE ALLIANCE", new Point(5, 30), 1, 1.5, BLUE, 2);
            }

            if (_startingPosition == AutoUtils.StartingPosition.OUTSIDE)
            {
                Imgproc.putText(input, "OUTSIDE", new Point(5, 60), 1, 1.5, RED, 2);
            }
            else
            {
                Imgproc.putText(input, "INSIDE", new Point(5, 60), 1, 1.5, BLUE, 2);
            }

            Imgproc.putText(input, "4 RINGS: " + _thresholdRingsFour, new Point(10, 90), 1, 1.5, GREEN, 2);
            Imgproc.putText(input, "1 RING: " + _thresholdRingsOne, new Point(10, 120), 1, 1.5, BLUE, 2);
        }

        @Override
        public Mat processFrame(Mat input) {
            //--- Calculate color level
            inputToCb(input);

            //--- Loop through each of the search regions and scan
            _ringsFound.clear();
            for (SearchRegion searchRegion : _regionsToSearch)
            {
                _ringsFound.add(GetRingCountFromCoordinates(searchRegion.topLeft, searchRegion.bottomRight));
            }

            //--- Loop through scanned region results
            boolean IsFourRings = false;
            boolean IsOneRing = false;
            RingsFound fourRings = new RingsFound();
            RingsFound oneRing = new RingsFound();
            for (RingsFound found : _ringsFound)
            {
                if (found.count == RingCount.FOUR)
                {
                    IsFourRings = true;
                    fourRings = found;
                }
            }
            if (!IsFourRings)
                for (RingsFound found : _ringsFound) {
                    if (found.count == RingCount.ONE) {
                        IsOneRing = true;
                        oneRing = found;
                    }
                }

            if (IsFourRings)
            {
                _averageColorLevel = fourRings.colorLevel;
                _ringCount = fourRings.count;
                drawDetectionPreview(input, fourRings.count, fourRings.colorLevel, fourRings.topLeft, fourRings.bottomRight);
            }
            else if (IsOneRing)
            {
                _averageColorLevel = oneRing.colorLevel;
                _ringCount = oneRing.count;
                drawDetectionPreview(input, oneRing.count, oneRing.colorLevel, oneRing.topLeft, oneRing.bottomRight);
            }
            else
            {
                RingsFound zeroRings = GetRingCountFromCoordinates(_regionTopLeft, _regionBottomRight);
                _averageColorLevel = zeroRings.colorLevel;
                _ringCount = zeroRings.count;
                drawDetectionPreview(input, zeroRings.count, zeroRings.colorLevel, zeroRings.topLeft, zeroRings.bottomRight);
            }

            return input;
        }

        private RingsFound GetRingCountFromCoordinates(Point topLeft, Point bottomRight)
        {
            Mat region = Cb.submat(new Rect(topLeft, bottomRight));
            RingsFound found = GetRingCountForRegion(region);
            found.topLeft = topLeft;
            found.bottomRight = bottomRight;
            return found;
        }

        private RingsFound GetRingCountForRegion(Mat region)
        {
            RingsFound found = new RingsFound();
            found.colorLevel = (int) Core.mean(region).val[0];
            found.count = GetRingCountFromColorLevel(found.colorLevel);

            if (found.count == RingCount.FOUR) { found.countInteger = 4;  }
            else if (found.count == RingCount.ONE) { found.countInteger = 1; }
            else { found.countInteger = 0; }

            return found;
        }

        private RingCount GetRingCountFromColorLevel(int level)
        {
            RingCount count;
            if (level > _thresholdRingsFour) {
                count = RingCount.FOUR;
            } else if (level > _thresholdRingsOne) {
                count = RingCount.ONE;
            } else {
                count = RingCount.ZERO;
            }
            return count;
        }

        private class RingsFound {
            private RingCount count = RingCount.FOUR;
            private int countInteger = 0;
            private int colorLevel = 0;
            private Point topLeft = new Point(0,0);
            private Point bottomRight = new Point(0,0);
        }

        private class SearchRegion {
            private Point topLeft = new Point(0,0);
            private Point bottomRight = new Point(0,0);
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

            setViewportPaused(!_viewportPaused);
        }

        public void setViewportPaused(boolean paused) {
            _viewportPaused = paused;

            if(_viewportPaused) {
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
        return pipeline._viewportPaused;
    }

    public int getColorLevel() {
        return pipeline._averageColorLevel;
    }

    public RingCount getRingCount() {
        return pipeline._ringCount;
    }

    public TargetZone getTargetZone() {
        switch (getRingCount()) {
            case ONE:
                return TargetZone.ZONE_B;
            case FOUR:
                return TargetZone.ZONE_C;
            default:
                return TargetZone.ZONE_A;
        }
    }
}
