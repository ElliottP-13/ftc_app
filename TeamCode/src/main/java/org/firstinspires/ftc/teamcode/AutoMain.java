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
        selectOptions();
        waitForStart();
        if (allianceColor.getValue().equals("Red")) {
            directonAdjust = -1.0;
        }
        nap(waitStart.getValue() * 1000);
        startFork();
    }

    private void startFork() {
        //start position two
        if (opModeIsActive()) {
            if (startPosition.getValue().equals("Two")) {
                if (start.getValue().equals("Hit Cap")) {
                    driveStraight(60, 10);//DeCapps
                    capFork();
                } else if (start.getValue().equals("Go Corner")) {
                    print("going corner");
                    driveStraight(24, 10);
                    turn(100, 10);
                    driveStraight(25, 10);//goes to corner ****Needs Adjustments so Straight On***
                    turn(90, 10);
                    driveStraight(24, 10);
                    robot.harvester.setPower(-.8);
                    nap(2000);//waits for us to score in corner goal
                    cornerFork();
                } else {
                    driveStraight(30, 10);
                    turn(90, 10);
                    driveStraight(30, 10);
                    turn(-45, 10);
                    driveStraight(18, 10);
                    turn(-70, 10);//should be 45 but something is weird other turns are fine, this one too short
                    neitherFork();
                }

            }
            //**************************start position one *************************************
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
                    robot.harvester.setPower(-.8);
                    nap(2000);//waits for us to score in corner goal
                    robot.harvester.setPower(0);
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
                detectLine();
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
                robot.harvester.setPower(-.8);
                nap(2000);
                robot.harvester.setPower(0);
                //capFork();
            }
        }
    }

    private void neitherFork() {
        if (opModeIsActive()) {
            if (pressBootin.getValue()) {
                driveStraight(15, 10);//gets close to button
                detectLine();
                beacon1();
            } else if (pressOtherBootin.getValue()) {
                driveStraight(63, 10);//goes past first button and close to next one
                detectLine();
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
                detectLine();
                beacon2();
            } else if (afterButton.getValue().equals("Park Corner")) {
                driveStraight(-6, 10);
                turn(90, 10);
                driveStraight(17, 10);
                turn(45, 10);
                driveStraight(17, 10);
                turn(-90, 10);
                driveStraight(17, 10);
                robot.harvester.setPower(-.8);
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
                detectLine();
                beacon1();
            } else if (afterButton.getValue().equals("Park Corner")) {
                turn(90, 10);
                driveStraight(48, 10);//goes to first beacon
                driveStraight(17, 10);
                turn(45, 10);
                driveStraight(17, 10);
                turn(-90, 10);
                driveStraight(17, 10);
                robot.harvester.setPower(-.8);
                nap(2000);//waits for us to score in corner goal
            } else if (afterButton.getValue().equals("Block")) {
                turn(-158, 10);
                driveStraight(132, 10);
            }
        }
    }
}
