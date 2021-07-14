package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class FieldPositions {

    //----------------------------------------------------------------------
    //--- Starting positions
    //----------------------------------------------------------------------
    public final static Pose2d RSO = new Pose2d(-63.5, -60, SetFacing(Facing.GOAL));     //---X red outside
    public final static Pose2d RSI = new Pose2d(-63.5, -16, SetFacing(Facing.GOAL));     //---X red inside

    public final static Pose2d BSO = new Pose2d(-63.5, 52, SetFacing(Facing.GOAL));      //---X blue outside
    public final static Pose2d BSI = new Pose2d(-63.5, 11, SetFacing(Facing.GOAL));      //---X blue inside

    public final static Pose2d NS0 = new Pose2d(0, 0, SetFacing(Facing.GOAL));           //---X center of arena
    public final static Pose2d NS1 = new Pose2d(-63.5, 0, SetFacing(Facing.GOAL));       //---X against wall, center

    //----------------------------------------------------------------------
    //--- Wobble goal positions
    //----------------------------------------------------------------------
    public final static Pose2d RSO_W = new Pose2d(-60, -60, SetFacing(Facing.GOAL));      //---X 2 inches away from wall, red outside
    public final static Pose2d RSI_W = new Pose2d(-60, -16, SetFacing(Facing.GOAL));      //---X 2 inches away from wall, red inside

    public final static Pose2d BSO_W = new Pose2d(-60, 53, SetFacing(Facing.GOAL));       //---X 2 inches away from wall, blue outside
    public final static Pose2d BSI_W = new Pose2d(-60, 11, SetFacing(Facing.GOAL));       //---X 2 inches away from wall, blue inside

    //----------------------------------------------------------------------
    //--- Field line positions
    //----------------------------------------------------------------------
    public final static Pose2d RL0 = new Pose2d(-2, -63, SetFacing(Facing.GOAL));       //--- against wall, behind white line - used to align for auto-in-teleop shooting
    public final static Pose2d BL0 = new Pose2d(-2, 63, SetFacing(Facing.GOAL));

    public final static Pose2d RLS1 = new Pose2d(10, -64, SetFacing(Facing.GOAL));      //--- park on square close to wall (square 1)
    public final static Pose2d BLS1 = new Pose2d(10, 62, SetFacing(Facing.GOAL));

    public final static Pose2d RLS1_A = new Pose2d(30, -54, SetFacing(Facing.RIGHT));   //--- park between square 1 and 2 and extend arm
    public final static Pose2d BLS1_A = new Pose2d(29, 57, SetFacing(Facing.RIGHT));    //--- park against the wall and extend arm

    public final static Pose2d RLS2 = new Pose2d(10, -37, SetFacing(Facing.GOAL));      //--- park on middle of three squares (square 2)
    public final static Pose2d BLS2 = new Pose2d(10, 37, SetFacing(Facing.GOAL));

    public final static Pose2d RLS3 = new Pose2d(10, -11, SetFacing(Facing.GOAL));      //--- park on close to center square (square 3)
    public final static Pose2d BLS3 = new Pose2d(10, 11, SetFacing(Facing.GOAL));

    //----------------------------------------------------------------------
    //--- Targeting positions (high goal)
    //----------------------------------------------------------------------
    public final static Pose2d RTO = new Pose2d(-5, -60, Math.toRadians(31));       //--- high goal, outside (Auto-in-Teleop)
    public final static Pose2d RTO2 = new Pose2d(-4, -61, Math.toRadians(20));      //--- high goal, outside (Missions)

    public final static Pose2d BTO = new Pose2d(-5, 60, Math.toRadians(-15));       //--- high goal, outside (Auto-in-Teleop)
    public final static Pose2d BTO2 = new Pose2d(-4, 55, Math.toRadians(-10));      //--- high goal, outside (Missions)

    public final static Pose2d RTI = new Pose2d(-5, -16, Math.toRadians(-11));      //--- high goal, inside
    public final static Pose2d RTI_B = new Pose2d(-5, -16, Math.toRadians(-10));    //--- high goal, inside (correction angle for inside mission)

    public final static Pose2d BTI = new Pose2d(-5, 11, Math.toRadians(19));        //--- high goal, inside
    public final static Pose2d BTI_B = new Pose2d(-5, 18, Math.toRadians(10));      //--- high goal, inside (correction angle for inside mission)

    //----------------------------------------------------------------------
    //--- Targeting positions (power shots)
    //----------------------------------------------------------------------
    public final static Pose2d RTO_TELE_PS = new Pose2d(-7, -58, Math.toRadians(42));   //--- red outside power shot
    public final static double RTO_TELE_PSA[] = { 7, 5 };

    public final static Pose2d BTO_TELE_PS = new Pose2d(-7, 58, Math.toRadians(-22));   //--- blue outside power shot
    public final static double BTO_TELE_PSA[] = { -7, -7 };

    //----------------------------------------------------------------------
    //--- Ring pickup positions
    //----------------------------------------------------------------------
    public final static Pose2d RR = new Pose2d(-25, -20, SetFacing(Facing.RIGHT));      //--- red pickup point
    public final static Pose2d RR_A = new Pose2d(-23, -30, SetFacing(Facing.RIGHT));    //--- move forward to pickup
    public final static Pose2d RR_B = new Pose2d(-23, -40, SetFacing(Facing.RIGHT));    //--- move forward to pickup
    public final static Pose2d BR = new Pose2d(-25, 20, SetFacing(Facing.LEFT));        //--- blue pickup point
    public final static Pose2d BR_A = new Pose2d(-23, 30, SetFacing(Facing.LEFT));      //--- move forward to pickup
    public final static Pose2d BR_B = new Pose2d(-23,40 , SetFacing(Facing.LEFT));      //---

    //----------------------------------------------------------------------
    //--- Wobble goal pickup positions (outside)
    //----------------------------------------------------------------------
    public final static Pose2d RO_WA = new Pose2d(20, -64, SetFacing(Facing.LEFT));     //---X red A - towards front
    public final static Pose2d RO_WB = new Pose2d(35, -55, SetFacing(Facing.GOAL));     //---X red B
    public final static Pose2d RO_WC = new Pose2d(44, -63, SetFacing(Facing.RIGHT));    //---X red C

    public final static Pose2d BO_WA = new Pose2d(21, 60, SetFacing(Facing.LEFT));              //---
    public final static Pose2d BO_WB = new Pose2d(35, 51, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    public final static Pose2d BO_WC = new Pose2d(44, 57, SetFacing(Facing.RIGHT));             //---

    //----------------------------------------------------------------------
    //--- Wobble goal pickup positions (inside)
    //----------------------------------------------------------------------
    public final static Pose2d RI_WA = new Pose2d(27, -61, SetFacing(Facing.LEFT));             //---XX red A
    public final static Pose2d RI_WB = new Pose2d(35, -23, SetFacing(Facing.BACK_ROTATE_LEFT)); //---XX red B
    public final static Pose2d RI_WC = new Pose2d(58, -51, SetFacing(Facing.BACK_ROTATE_LEFT)); //---XX red C

    public final static Pose2d BI_WA = new Pose2d(27, 61, SetFacing(Facing.LEFT));  //---XX blue A
    public final static Pose2d BI_WB = new Pose2d(35, 23, SetFacing(Facing.GOAL));  //---XX blue B
    public final static Pose2d BI_WC = new Pose2d(58, 40, SetFacing(Facing.GOAL));  //---XX blue C

    //----------------------------------------------------------------------
    //--- Transition positions
    //----------------------------------------------------------------------
    public final static Pose2d BX2 = new Pose2d(35, 15, SetFacing(Facing.GOAL));                   //---
    public final static Pose2d RX2 = new Pose2d(35, -15, SetFacing(Facing.BACK_ROTATE_RIGHT));   //---X center of close to target zone (reversed)

    public final static Pose2d BX6 = new Pose2d(41, 55, SetFacing(Facing.RIGHT));   //--- middle of the blue target zones
    public final static Pose2d RX6 = new Pose2d(35, -64, SetFacing(Facing.RIGHT));               //---X middle of out red zones





    //--- Ring pickup positions
    public final static Pose2d R1 = new Pose2d(0, 0, SetFacing(Facing.GOAL)); //---
    public final static Pose2d R2 = new Pose2d(0, 0, SetFacing(Facing.GOAL)); //---
    public final static Pose2d R3 = new Pose2d(0, 0, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    public final static Pose2d R4A = new Pose2d(-8, -36, Math.toRadians(-175)); //---
    public final static Pose2d R4B = new Pose2d(-20, -36, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    public final static Pose2d R4C = new Pose2d(-27, -36, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    public final static Pose2d R2A = new Pose2d(-8, 36, Math.toRadians(-175)); //---
    public final static Pose2d R2B = new Pose2d(-20, 36, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    public final static Pose2d R2C = new Pose2d(-27, 36, SetFacing(Facing.BACK_ROTATE_RIGHT)); //---
    //--- Transition positions
    public final static Pose2d X0 = new Pose2d(0, 0, SetFacing(Facing.GOAL));                   //--- center of the arena
    public final static Pose2d X1 = new Pose2d(0, 0, SetFacing(Facing.GOAL));                   //---

    public final static Pose2d X3 = new Pose2d(45, 0, SetFacing(Facing.GOAL));                  //---X center of close to target zone
    public final static Pose2d X3R = new Pose2d(45, 0, SetFacing(Facing.BACK_ROTATE_RIGHT));    //---X center of close to target zone (reversed)

    public final static Pose2d X5R = new Pose2d(67, 0, SetFacing(Facing.BACK_ROTATE_RIGHT));     //---X far back center



    //--- White line positions
    public final static Pose2d L0 = new Pose2d(10, 0, SetFacing(Facing.GOAL));                  //--- center on white line
    public final static Pose2d L0R = new Pose2d(10, 0, SetFacing(Facing.BACK_ROTATE_RIGHT));    //--- center on white line (reversed)
    public final static Pose2d L1 = new Pose2d(10, 11, SetFacing(Facing.GOAL));                 //--- blue side, middle square - park on line
    public final static Pose2d L1R = new Pose2d(10, 11, SetFacing(Facing.BACK_ROTATE_RIGHT));   //--- blue side, middle square - park on line (reversed)
    public final static Pose2d L2 = new Pose2d(10, -11, SetFacing(Facing.GOAL));                //--- red side, middle square - park on line
    public final static Pose2d L2R = new Pose2d(10, -11, SetFacing(Facing.BACK_ROTATE_RIGHT));  //--- red side, middle square - park on line (reversed)
    public final static Pose2d L3 = new Pose2d(3, -61, SetFacing(Facing.RIGHT));                //--- red side, park on line
    public final static Pose2d L4 = new Pose2d(1, 61, SetFacing(Facing.RIGHT));




    //---XX blue side, park on line


    //--- Wobble goal target positions


    public final static Pose2d W4 = new Pose2d(0, 0, SetFacing(Facing.GOAL));   //---
    public final static Pose2d W5 = new Pose2d(0, 0, SetFacing(Facing.GOAL));   //---
    public final static Pose2d W6 = new Pose2d(0, 0, SetFacing(Facing.GOAL));   //---


    //--- Special path back from R OUT - Zone C to grab wobble goal
    public final static Pose2d S3G_1 = new Pose2d(63, -30, Math.toRadians(180));  //---
    public final static Pose2d S3G_2 = new Pose2d(-30, -30, SetFacing(Facing.BACK_ROTATE_LEFT));  //---
    public final static Pose2d S3G_3 = new Pose2d(-36, -30, Math.toRadians(72));     //--- red inside
    public final static Pose2d S3G_4 = new Pose2d(0, -63, SetFacing(Facing.LEFT));  //---X park on line
    //    public final static Pose2d W6I_2 = new Pose2d(44, -63, SetFacing(Facing.RIGHT));    //---X red C (2nd wobble)
    public final static Pose2d W6O_2 = new Pose2d(44, -63, SetFacing(Facing.RIGHT));    //---X red C (2nd wobble)

    //--- Wobble goals From top
    public final static Pose2d W1S = new Pose2d(3, 60, SetFacing(Facing.RIGHT));                //---
    public final static Pose2d W2S = new Pose2d(0, 0, SetFacing(Facing.RIGHT));                 //---
    public final static Pose2d W3S = new Pose2d(0, 0, SetFacing(Facing.RIGHT));                 //---
    public final static Pose2d W4S = new Pose2d(42, -35, SetFacing(Facing.BACK_ROTATE_RIGHT));  //---
    public final static Pose2d W5S = new Pose2d(50, -40, SetFacing(Facing.RIGHT));              //---
    public final static Pose2d W6S = new Pose2d(22, -40, SetFacing(Facing.BACK_ROTATE_RIGHT));  //---

    //--- Collect rings after shooting power shots positions
    public final static Pose2d C1A = new Pose2d(38, 11, SetFacing(Facing.GOAL));    //--- blue side collect rings from missed power shots
    public final static Pose2d C1B = new Pose2d(58, 11, SetFacing(Facing.GOAL));
    public final static Pose2d C2A = new Pose2d(38, -11, SetFacing(Facing.GOAL));   //--- red side collect rings from missed power shots
    public final static Pose2d C2B = new Pose2d(58, -11, SetFacing(Facing.GOAL));

    //--- Targeting positions
    public final static Pose2d T1 = new Pose2d(-4, 46, SetFacing(Facing.GOAL));       //---

    public final static Pose2d T3 = new Pose2d(0, 0, SetFacing(Facing.GOAL));       //---
    public final static Pose2d T4 = new Pose2d(-4, -26, SetFacing(Facing.GOAL));    //---XX red center front line

    public final static Pose2d T6 = new Pose2d(0, 0, SetFacing(Facing.GOAL));       //---
    public final static Pose2d T7 = new Pose2d(-4, -16, Math.toRadians(-6));        //---X red inside front line

    //--- Older missions... look at removing
    public final static Pose2d RWA_O2 = new Pose2d(30, -64, SetFacing(Facing.LEFT));      //---X red A - towards goal
    public final static Pose2d RWB_O2 = new Pose2d(52, -45, SetFacing(Facing.LEFT));       //---X red B
    public final static Pose2d RWC_O2 = new Pose2d(48, -59, SetFacing(Facing.RIGHT));      //---X red C



    //--- Power shot positions
    public final static Pose2d P1 = new Pose2d(0, 0, SetFacing(Facing.GOAL));       //---
    public final static double P1A[] = { -8, -8 };
    public final static Pose2d P2 = new Pose2d(-4, 6, Math.toRadians(2));           //---
    public final static double P2A[] = { 10, 8 };
    public final static Pose2d P3 = new Pose2d(0, 0, SetFacing(Facing.GOAL));       //---
    public final static double P3A[] = { 8, 8 };
    public final static Pose2d P4 = new Pose2d(-4, -10, Math.toRadians(-1));        //---X red inside power show
    public final static double P4A[] = { 9, 8 };
    public final static Pose2d P5 = new Pose2d(-4, 61, Math.toRadians(-31));       //--- blue outside power show
    public final static double P5A[] = { 5, 8 };
    public final static Pose2d P6 = new Pose2d(-4, -61, Math.toRadians(31));        //---X red outside power shot
    public final static double P6A[] = { 5, 8 };

    private enum Facing
    {
        GOAL,
        LEFT,
        RIGHT,
        BACK_ROTATE_LEFT,
        BACK_ROTATE_RIGHT
    }

    public static double SetFacing(Facing facing)
    {
        double angle = 0;
        switch (facing)
        {
            case GOAL: angle = 0; break;
            case LEFT: angle = 90; break;
            case RIGHT: angle = -90; break;
            case BACK_ROTATE_LEFT: angle = -180; break;
            case BACK_ROTATE_RIGHT: angle = 180; break;
        }
        return Math.toRadians(angle);
    }
}
