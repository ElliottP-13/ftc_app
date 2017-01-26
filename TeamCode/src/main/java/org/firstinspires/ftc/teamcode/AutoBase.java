package org.firstinspires.ftc.teamcode;


import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Map;

/**
 * Created by Elliott on 10/31/2016.
 */

public class AutoBase extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();

    static final double countsPerInch = 29.5;
    static final double countsPerDegree = 2.575;//3.25 = old
    static final double voltSlide = 4;
    static final double driveSpeed = 0.5;
    static final double turnSpeed = .35;
    static final double miliPerInch = 30;
    static final double miliPerDegree = 8.017;
    //goes about 720 + 45 degrees, when told to go 90 degrees
    static final double voltChange = 0.7;
    Robot robot = null;

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "Blue", new String[]{"Blue", "Red"});
    AutonomousTextOption startPosition = new AutonomousTextOption("Start Position", "One", new String[]{"One", "Two"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousTextOption start = new AutonomousTextOption("Start Strategy", "Hit Cap", new String[]{"Hit Cap", "Go Corner", "Neither"});
    AutonomousBooleanOption pressBootin = new AutonomousBooleanOption("Bootin Press", false);
    AutonomousBooleanOption pressOtherBootin = new AutonomousBooleanOption("Bootin 2 Press", false);
    AutonomousTextOption afterButton = new AutonomousTextOption("After Button", "Park Corner", new String[]{"Park Corner", "Block"});

    AutonomousOption[] autoOptions = {allianceColor, startPosition, waitStart, start, pressBootin, pressOtherBootin, afterButton};
    int currentOption = 0;

    double directonAdjust = 1.0;

    @Override
    public void runOpMode() {
        //do nothing because this is the base class
    }

    public void initialize(HardwareMap map) {
        robot = new Robot(map);
        //selectOptions();
        if (allianceColor.getValue().equals("Red")) {
            directonAdjust = -1.0;
        }
    }

    public void turn(double degrees, double timeout) {//defaults to the right, go left w/ -degrees
        turn(turnSpeed, degrees, timeout);
    }

    public void driveStraight(double inches, double timeout) {
        driveStraight(driveSpeed, inches, timeout);
    }

    public void driveTime(double inches) {
        if (opModeIsActive()) {
            runtime.reset();
            double Rpower = 1;
            double Lpower = Rpower - .01;
            double voltAjust = 0;
//            if (robot.checkVoltage() + voltChange < 12.05) {
//                print("Low Power");
//                voltAjust = 0.575;
//            }
//            if (robot.checkVoltage() + voltChange > 12.4) {
//                print("Low Power");
//                voltAjust = -1.2;
//            }
            if (inches < 0) {
                Lpower *= -1;
                Rpower *= -1;
            }
            for(int i = 0; i <= 1 * 10; i++){
                double power = i * .1;
                robot.rightFront.setPower(power * Rpower);
                robot.rightBack.setPower(power * Rpower);
                robot.leftFront.setPower(power * Lpower);
                robot.leftBack.setPower(power * Lpower);
            }
            while ((runtime.milliseconds() < ((miliPerInch * Math.abs(inches)) + voltAjust * robot.checkVoltage())) && opModeIsActive()) {
                //do nothing because we are waiting for it to finish
                telemetry.addLine("Running to: " + (miliPerInch * inches) + voltAjust * robot.checkVoltage());
                telemetry.addLine("Currently at: " + runtime.milliseconds());
                telemetry.update();
            }
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);
            sleep(125);
        }
    }

    public void turnTime(double degrees) {
        double voltAdjust = 0;
        if (robot.checkVoltage() + voltChange <= 12.05) {
            print("low power");
            voltAdjust = 0.293;
        }
        if (robot.checkVoltage() >= 12.8) {
            print("HIGH POWAA");
            voltAdjust = -16.5;
        }
        runtime.reset();
        if (degrees > 0) {//turn right
            robot.leftFront.setPower(.7);
            robot.leftBack.setPower(.7);
            robot.rightFront.setPower(-.7);
            robot.rightBack.setPower(-.7);
        }
        else if (degrees < 0) {//turn left
            robot.leftFront.setPower(-.7);
            robot.leftBack.setPower(-.7);
            robot.rightFront.setPower(.7);
            robot.rightBack.setPower(.7);
        }
        while ((runtime.milliseconds() < (miliPerDegree * Math.abs(degrees)) + voltAdjust * robot.checkVoltage()) && opModeIsActive()) {
            //does nothing because we are waiting for it to finish
        }
        robot.leftFront.setPower(0);
        robot.leftBack.setPower(0);
        robot.rightFront.setPower(0);
        robot.rightBack.setPower(0);
        sleep(500);
    }
    public void turn(double speed, double degrees, double timeoutS) {//defaults right, turn left w/ -degrees
//        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int Lback = robot.leftBack.getCurrentPosition();
        int Rback = robot.rightBack.getCurrentPosition();
        int Lfront = robot.leftFront.getCurrentPosition();
        int Rfront = robot.rightFront.getCurrentPosition();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            Lback = robot.leftBack.getCurrentPosition() + (int) (degrees * countsPerDegree * directonAdjust);
            Lfront = robot.leftFront.getCurrentPosition() + (int) (degrees * countsPerDegree * directonAdjust);
            Rback = robot.rightBack.getCurrentPosition() - (int) (degrees * countsPerDegree * directonAdjust);
            Rfront = robot.rightFront.getCurrentPosition() - (int) (degrees * countsPerDegree * directonAdjust);
            //runEncoder(leftTarget, rightTarget, timeoutS, speed, false);
        }

        int rAdjust = 1;
        boolean rdir = true;
        int lAdjust = 1;
        boolean ldir = true;
        if(robot.rightBack.getCurrentPosition() > Rback){//needs to go backwards
            rAdjust *= -1;
            rdir = false;
        }
        if(robot.leftBack.getCurrentPosition() > Lback){//needs to go backwards
            lAdjust *= -1;
            ldir = false;
        }
        robot.rightFront.setPower(speed * rAdjust);
        robot.rightBack.setPower(speed * rAdjust);
        robot.leftFront.setPower(speed * lAdjust);
        robot.leftBack.setPower(speed * lAdjust);
//        for(int i = 0; i <= speed * 10; i++){
//            double power = i * .1;
//            robot.rightBack.setPower(power * rAdjust);
//            robot.leftBack.setPower(power * lAdjust);
//        }
        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (checkDistance(Lback, robot.leftBack, ldir) ||
                        checkDistance(Rback, robot.rightBack, rdir))) {

            if(!checkDistance(Lback, robot.leftBack, ldir)){
                telemetry.addLine("Stop Left");
                robot.leftBack.setPower(0);
                robot.leftFront.setPower(0);
            }
            else if(!checkDistance(Rback, robot.rightBack, rdir)){
                telemetry.addLine("Stop Right");
                robot.rightBack.setPower(0);
                robot.rightFront.setPower(0);
            }
            // Display it for the driver.
            telemetry.addData("Back", "Running to %7d :%7d", Lback, Rback);

            telemetry.addData("back 2", "Running at %7d :%7d",
                    robot.leftBack.getCurrentPosition(),
                    robot.rightBack.getCurrentPosition());
            telemetry.update();
            //maybe try doing sleep or Thread.yield
        }
        telemetry.addLine("I am Done");
        telemetry.update();
        // Stop all motion;
        robot.leftBack.setPower(0);
        robot.leftFront.setPower(0);
        robot.rightFront.setPower(0);
        robot.rightBack.setPower(0);

    }

    public void driveStraight(double speed, double inches, double timeoutS) {//**make a calc timeout**
        int lFront;
        int lBack;
        int rFront;
        int rBack;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller

            lBack = Math.abs(robot.leftBack.getCurrentPosition()) + (int) (inches * countsPerInch);

            rBack = Math.abs(robot.rightBack.getCurrentPosition()) + (int) (inches * countsPerInch);
            runEncoder(lBack, rBack, timeoutS, speed);
        }
    }

    private void runEncoder(int Lback, int Rback, double timeoutS, //change to calc timeout
                            double speed) {
        if (!opModeIsActive()) {
            stop();
        }
        if (isStopRequested()) {
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);

        } else {
            runtime.reset();
            boolean dir = robot.leftBack.getCurrentPosition() < Lback;
            int direction = 1;
            if(!dir){//go backwards
                direction = -1;
            }
            double delta = 10;
            // keep looping while we are still active, and there is time left, and both motors are running.

            for(int i = 0; i <= speed * 10; i++){
                double power = i * .1;
                if(checkDistance(Lback, robot.leftBack, dir) && checkDistance(Rback, robot.rightBack, dir)) {
                    robot.rightFront.setPower(power * direction);
                    robot.rightBack.setPower(power * direction);
                    robot.leftFront.setPower(power * direction);
                    robot.leftBack.setPower(power * direction);
                }
            }
            int left = 0;
            int right = 0;
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (checkDistance(Lback, robot.leftBack, dir) && checkDistance(Rback, robot.rightBack, dir))) {

                if(robot.rightBack.getCurrentPosition() > robot.leftBack.getCurrentPosition() + delta){
                    telemetry.addLine("Slow Right");
                    right ++;
                    robot.rightFront.setPower(Math.abs(speed) - .2);
                    robot.rightBack.setPower(Math.abs(speed) - .2);
                    robot.leftFront.setPower(Math.abs(speed));
                    robot.leftBack.setPower(Math.abs(speed));
                }
                if(robot.leftBack.getCurrentPosition() > robot.rightBack.getCurrentPosition() + delta){
                    telemetry.addLine("Slow Left");
                    left ++;
                    robot.leftFront.setPower(Math.abs(speed) - .2);
                    robot.leftBack.setPower(Math.abs(speed) - .2);
                    robot.rightFront.setPower(Math.abs(speed));
                    robot.rightBack.setPower(Math.abs(speed));
                }

                // Display it for the driver.
                telemetry.addData("Back", "Running to %7d :%7d", Lback, Rback);

                telemetry.addData("back 2", "Running at %7d :%7d",
                        robot.leftBack.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition());
                telemetry.update();
            }
            telemetry.addLine("STOP");
            telemetry.addData("Slowed", "Right %7d Left %7d", right, left);
            telemetry.update();
            // Stop all motion;
            robot.leftFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightFront.setPower(0);
            robot.rightBack.setPower(0);
        }
    }
    private boolean checkDistance(int tickTarget, DcMotor motor, boolean forwards){
        telemetry.addLine("" + (Math.abs(tickTarget) < Math.abs(motor.getCurrentPosition())));
        if(forwards)
            return Math.abs(tickTarget) > Math.abs(motor.getCurrentPosition());
        else
            return tickTarget < motor.getCurrentPosition();
    }
    public boolean checkColor(){
        boolean red = robot.color.red() > robot.color.blue();//return true if it is red, false if it is blue
        if(red && allianceColor.equals("Red")){//it is red and we are red
            return true;
        }
        else if(red && allianceColor.equals("Blue")){//it is red and we are blue
            return false;
        }
        else if(!red && allianceColor.equals("Blue")){//it is blue, and we are blue
            return true;
        }
        else {//it is blue and we are red
            return false;
        }
    }
    public void selectOptions() {
        boolean aPressed = false;
        boolean yPressed = false;
        boolean bPressed = false;
        boolean xPressed = false;
        while (currentOption < autoOptions.length && !opModeIsActive()) {
            showOptions();
            if (gamepad1.a && !aPressed) {
                currentOption = currentOption + 1;
                aPressed = true;
            } else {
                aPressed = gamepad1.a;
            }
            if (gamepad1.y && !yPressed) {
                currentOption = currentOption - 1;
                yPressed = true;
            } else {
                yPressed = gamepad1.y;
            }
            if (gamepad1.b && !bPressed) {
                autoOptions[currentOption].nextValue();
                bPressed = true;
            } else {
                bPressed = gamepad1.b;
            }
            if (gamepad1.x && !xPressed) {
                autoOptions[currentOption].previousValue();
                xPressed = true;
            } else {
                xPressed = gamepad1.x;
            }
        }
    }

    private void showOptions() {
        String str = "";
        switch (autoOptions[currentOption].optionType) {
            case STRING:
                str = ((AutonomousTextOption) autoOptions[currentOption]).getValue();
                break;
            case INT:
                str = Integer.toString(((AutonomousIntOption) autoOptions[currentOption]).getValue());
                break;
            case BOOLEAN:
                str = String.valueOf(((AutonomousBooleanOption) autoOptions[currentOption]).getValue());
                break;
        }
        telemetry.addLine("Current Number: " + currentOption);
        telemetry.addLine(autoOptions[currentOption].name);
        telemetry.addLine("Current Option: " + str);
        telemetry.update();
    }

    private void showOptions(String additonalInfo) {
        telemetry.addLine(additonalInfo);
        showOptions();
    }

    public void print(String info) {
        telemetry.addLine(info);
        telemetry.update();
    }

    public void nap(int time) {
        runtime.reset();
        if(!opModeIsActive()){
            stop();
        }else {
            while ((runtime.milliseconds() < time) && opModeIsActive()) {
                //do nothing, this is a wait.
            }
        }
    }
}
