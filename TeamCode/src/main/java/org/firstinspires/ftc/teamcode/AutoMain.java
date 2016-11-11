package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Elliott on 10/31/2016.
 */
@Autonomous(name = "Auto Main", group = "Linear Opmode")
public class AutoMain extends AutoBase{
    @Override
    public void runOpMode() {
        initialize(hardwareMap);
        waitForStart();
        startFork();
    }
    private void startFork(){
        if (hitCap.getValue().equals("Hit Cap")){
            driveStraight(60, 6);//DeCapps
            capFork();
        }
        else if (hitCap.getValue().equals("Go Corner")){
            driveStraight(24, 3);
            turn(100, 5);
            driveStraight(48, 10);//goes to corner ****Needs Adjustments so Straight On***
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
            cornerFork();
        }
        else {
            driveStraight(36, 5);
            turn(90, 5);
            driveStraight(48, 10);
            turn(-90, 5);//faces buttons
            neitherFork();
        }
    }
    private void capFork(){
        if((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner")) || (pressOtherBootin.getValue() && !pressBootin.getValue())){
            //goes to second button
            turn(50, 4);// turns more than 45 so we can go straight onto line
            driveStraight(29.8, 8);
            turn(-50, 4);
            detectLine();
            beacon2();
        }
        else{
            //press button 1
            driveStraight(-24, 8);
            turn(90, 5);
            driveStraight(48, 10);
            turn(-90, 5);//faces buttons
            neitherFork();
        }
    }
    private void neitherFork(){
        if (pressBootin.getValue()){
            driveStraight(1, 15, 4);//gets close to button
            detectLine();
            beacon1();
        }
        else if(pressOtherBootin.getValue()){
            driveStraight(1, 63, 4);//goes past first button and close to next one
            detectLine();
            beacon2();
        }
    }
    private void cornerFork(){
        if (pressBootin.getValue() || pressOtherBootin.getValue()){
            driveStraight(-17, 4);//backs up off the ramp
            turn(-90, 4);//faces button
            driveStraight(17, 4);
            turn(-45, 4);//straightens for button
            neitherFork();
        }

    }
    private void beacon1(){
        if (pressOtherBootin.getValue()){
            driveStraight(-6, 3);
            turn(-90, 4);
            driveStraight(42, 6);
            detectLine();
            beacon2();
        }
        else if(afterButton.getValue().equals("Park Corner")){
            driveStraight(-6, 3);
            turn(90, 4);
            driveStraight(17, 4);
            turn(45, 4);
            driveStraight(17, 4);
            turn(-90, 4);
            driveStraight(17, 4);
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
        }
    }
    private void beacon2(){
        driveStraight(-6, 3);
        if((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner"))){
            //hit b1, then go corner
            turn(90, 4);
            driveStraight(42, 8);//goes to first beacon
            detectLine();
            beacon1();
        }
        else if(afterButton.getValue().equals("Park Corner")){
            turn(90, 4);
            driveStraight(48, 8);//goes to first beacon
            driveStraight(17, 4);
            turn(45, 4);
            driveStraight(17, 4);
            turn(-90, 4);
            driveStraight(17, 4);
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
        }
        else if(afterButton.getValue().equals("Block")){
            turn(-158, 6);
            driveStraight(132, 10);
        }

    }
}
