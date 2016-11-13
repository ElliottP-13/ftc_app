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
        allianceColor.setValue("Blue"); waitStart.setValue(0); start.setValue("Go Corner"); pressBootin.setValue(true);
        pressOtherBootin.setValue(true); afterButton.setValue("Park Corner");
        startFork();
    }
    private void startFork(){
        if (start.getValue().equals("Hit Cap")){
            driveTime(60);//DeCapps
            capFork();
        }
        else if (start.getValue().equals("Go Corner")){
            driveTime(24);
            turnTime(100);
            driveTime(25);//goes to corner ****Needs Adjustments so Straight On***
            turnTime(90);
            driveTime(24);
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
            cornerFork();
        }
        else {
            driveTime(30);
            turnTime(90);
            driveTime(30);
            turnTime(-45);
            driveTime(18);
            turnTime(-70);//should be 45 but something is weird other turns are fine, this one too short
            neitherFork();
        }
    }
    private void capFork(){
        if((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner")) || (pressOtherBootin.getValue() && !pressBootin.getValue())){
            //goes to second button
            turnTime(50);// turnTimes more than 45 so we can go straight onto line
            driveTime(29.8);
            turnTime(-50);
            detectLine();
            beacon2();
        }
        else{
            //press button 1
            driveTime(-24);
            turnTime(90);
            driveTime(48);
            turnTime(-90);//faces buttons
            neitherFork();
        }
    }
    private void neitherFork(){
        if (pressBootin.getValue()){
            driveTime(15);//gets close to button
            detectLine();
            beacon1();
        }
        else if(pressOtherBootin.getValue()){
            driveTime(63);//goes past first button and close to next one
            detectLine();
            beacon2();
        }
    }
    private void cornerFork(){
        if (pressBootin.getValue() || pressOtherBootin.getValue()){//go hit the buttons
            driveTime(-17);//backs up off the ramp
            turnTime(-90);//faces button
            driveTime(17);
            turnTime(-45);//straightens for button
            neitherFork();
        }

    }
    private void beacon1(){
        if (pressOtherBootin.getValue()){
            driveTime(-6);
            turnTime(-90);
            driveTime(42);
            detectLine();
            beacon2();
        }
        else if(afterButton.getValue().equals("Park Corner")){
            driveTime(-6);
            turnTime(90);
            driveTime(17);
            turnTime(45);
            driveTime(17);
            turnTime(-90);
            driveTime(17);
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
        }
    }
    private void beacon2(){
        driveTime(-6);
        if((pressOtherBootin.getValue() && pressBootin.getValue() && afterButton.equals("Park Corner"))){
            //hit b1, then go corner
            turnTime(90);//goes to first beacon
            detectLine();
            beacon1();
        }
        else if(afterButton.getValue().equals("Park Corner")){
            turnTime(90);
            driveTime(48);//goes to first beacon
            driveTime(17);
            turnTime(45);
            driveTime(17);
            turnTime(-90);
            driveTime(17);
            robot.harvester.setPower(-.8);
            nap(2000);//waits for us to score in corner goal
        }
        else if(afterButton.getValue().equals("Block")){
            turnTime(-158);
            driveTime(132);
        }

    }
}
