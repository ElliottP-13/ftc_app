package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Elliott on 10/31/2016.
 */
@Autonomous(name = "Auto Main", group = "Linear Opmode")
public class AutoMain extends AutoBase {
    @Override
    public void runOpMode() {
        initialize(hardwareMap);
        //selectOptions();
        waitForStart();
        /*if (allianceColor.getValue().equals("Red")) {
            directonAdjust = -1.0;
        }*/
        //nap(waitStart.getValue() * 1000);
        //startFork();
        robot.color.enableLed(false);
        driveStraight(.8, 70, 10);//big first movement that gets us in front of the beacon
        nap(100);//so we don't slide and mess up the turn
        print("Done Waiting");
        turn(127, 10);//turns too much so that it accounts for the curve in the slow drive straight
        driveStraight(.3, 51, 4);//hits button, time so we don't constantly churn against the wall, and goes slow so don't damage field

        if(!checkColor()){//need to hit it again
            driveStraight(-6, 10);
            nap(4300);//waits long enough to hit it again
            driveStraight(.3, 8, 10);
        }
        driveStraight(-10, 10);
        turn(-97, 10);
        driveStraight(1, 53, 10);//gets in front of second beacon
        turn(113, 10);
        driveStraight(.3, 15, 4);
        if(!checkColor()){//need to hit it again
            driveStraight(-6, 10);
            nap(4300);//waits long enough to hit it again
            driveStraight(.3, 8, 10);
        }
    }

    private void startFork() {
        //start position two
        if (opModeIsActive()) {
            if (startPosition.getValue().equals("Two")) {
                driveStraight(6, 10);
                turn(20, 10);
                //shoot
                robot.shooter.setPower(.3);
                nap(5000);
                robot.shooter.setPower(0);
                driveStraight(34, 10);
                turn(60, 10);
                driveStraight(.8,  70, 10);
                turn(-90, 10);
                driveStraight(20, 10);
                turn(90, 10);
                driveStraight(.3, 50, 10);//hits first button
            }
            /**************************start position one *************************************/
            else {
                if (start.getValue().equals("Hit Cap")) {
                    driveStraight(5, 10);//DeCapps
                    turn(47, 10);
                    driveStraight(57, 10);
                    capFork();
                } else if (start.getValue().equals("Go Corner")) {
                    print("go Corner");
                    driveStraight(20, 10);
                    turn(90, 10);//should be 90, but its dumb
                    driveStraight(55, 10);
                    turn(50, 10);//also dumb, should be 45
                    driveStraight(37, 10);
                   // robot.harvester.setPower(-.8);
                    nap(2000);//waits for us to score in corner goal
                    //robot.harvester.setPower(0);
                    cornerFork();
                } else {//do neither fork
                    driveStraight(10, 10);
                    turn(90, 10);
                    driveStraight(47, 10);
                    turn(-45, 10);
                    driveStraight(20, 10);
                    turn(-45, 10);
                    neitherFork();
                }
            }
        }
    }

    private void capFork() {
        if (opModeIsActive()) {
            if ((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner")) || (pressOtherBootin.getValue() && !pressBootin.getValue())) {
                //goes to second button
                turn(50, 10);// turns more than 45 so we can go straight onto line
                driveStraight(29.8, 10);
                turn(-50, 10);
                beacon2();
            } else if (pressBootin.getValue()) {
                //press button 1
                driveStraight(-9, 10);
                turn(45, 10);
                driveStraight(55, 10);
                turn(-90, 10);//faces buttons
                neitherFork();
            } else {//park in the corner
                print("Cap Fork Corner");
                turn(101, 10);
                driveStraight(65, 10);
             //   robot.harvester.setPower(-.8);
                nap(2000);
               // robot.harvester.setPower(0);
                //capFork();
            }
        }
    }

    private void neitherFork() {
        if (opModeIsActive()) {
            if (pressBootin.getValue()) {
                driveStraight(15, 10);//gets close to button
                beacon1();
            } else if (pressOtherBootin.getValue()) {
                driveStraight(63, 10);//goes past first button and close to next one
                beacon2();
            }
        }
    }

    private void cornerFork() {
        //we can't back up so just stop
        /*if (pressBootin.getValue() || pressOtherBootin.getValue()){//go hit the buttons
            driveStraight(-20, 10);//backs up off the ramp
            turn(-45, 10);//faces button
            driveStraight(17, 10);
            turn(-45, 10);//straightens for button
            neitherFork();
        }*/

    }

    private void beacon1() {
        if (opModeIsActive()) {
            if (pressOtherBootin.getValue()) {
                driveStraight(-6, 10);
                turn(-90, 10);
                driveStraight(42, 10);
                beacon2();
            } else if (afterButton.getValue().equals("Park Corner")) {
                driveStraight(-6, 10);
                turn(90, 10);
                driveStraight(17, 10);
                turn(45, 10);
                driveStraight(17, 10);
                turn(-90, 10);
                driveStraight(17, 10);
                //robot.harvester.setPower(-.8);
                nap(2000);//waits for us to score in corner goal
            }
        }
    }

    private void beacon2() {
        if (opModeIsActive()) {
            driveStraight(-6, 10);
            if ((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner"))) {
                //hit b1, then go corner
                turn(90, 10);//goes to first beacon
                beacon1();
            } else if (afterButton.getValue().equals("Park Corner")) {
                turn(90, 10);
                driveStraight(48, 10);//goes to first beacon
                driveStraight(17, 10);
                turn(45, 10);
                driveStraight(17, 10);
                turn(-90, 10);
                driveStraight(17, 10);
                //robot.harvester.setPower(-.8);
                nap(2000);//waits for us to score in corner goal
            } else if (afterButton.getValue().equals("Block")) {
                turn(-158, 10);
                driveStraight(132, 10);
            }
        }
    }
}
