package org.firstinspires.ftc.teamcode;


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

    static final double countsPerInch = 37.5;
    static final double countsPerDegree = 3.25;
    static final double voltSlide = 4;
    static final double driveSpeed = 0.3;
    static final double turnSpeed = 0.5;
    static final double miliPerInch = 55.8556;
    static final double miliPerDegree = 8.366;
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
            if (robot.checkVoltage() + voltChange < 12.05) {
                print("Low Power");
                voltAjust = 0.575;
            }
            if (robot.checkVoltage() + voltChange > 12.4) {
                print("Low Power");
                voltAjust = -1.2;
            }
            if (inches < 0) {
                Lpower *= -1;
                Rpower *= -1;
            }
            robot.leftFront.setPower(Lpower);
            robot.leftBack.setPower(Lpower);
            robot.rightFront.setPower(Rpower);
            robot.rightBack.setPower(Rpower);
            while ((runtime.milliseconds() < ((miliPerInch * Math.abs(inches)) + voltAjust * robot.checkVoltage())) && opModeIsActive()) {
                //do nothing because we are waiting for it to finish
                telemetry.addLine("Running to: " + (miliPerInch * inches) + voltAjust * robot.checkVoltage());
                telemetry.addLine("Currently at: " + runtime.milliseconds());
                telemetry.update();
            }
            robot.leftFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightFront.setPower(0);
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
        if (robot.checkVoltage() + voltChange >= 12.4) {
            print("HIGH POWAA");
            voltAdjust = -42.5;
        }
        runtime.reset();
        if (degrees > 0) {//turn right
            robot.leftFront.setPower(.7);
            robot.leftBack.setPower(.7);
            robot.rightFront.setPower(-.7);
            robot.rightBack.setPower(-.7);
        }
        if (degrees < 0) {//turn left
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
        sleep(125);
    }
    public void turn(double speed, double degrees, double timeoutS) {//defaults right, turn left w/ -degrees
        int Lfront = robot.leftFront.getCurrentPosition();
        int Lback = robot.leftBack.getCurrentPosition();
        int Rfront = robot.rightFront.getCurrentPosition();
        int Rback = robot.rightBack.getCurrentPosition();

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            Lfront = robot.leftFront.getCurrentPosition() + (int) (degrees * countsPerDegree * directonAdjust);
            Lback = robot.leftBack.getCurrentPosition() + (int) (degrees * countsPerDegree * directonAdjust);
            Rfront = robot.rightFront.getCurrentPosition() - (int) (degrees * countsPerDegree * directonAdjust);
            Rback = robot.rightBack.getCurrentPosition() - (int) (degrees * countsPerDegree * directonAdjust);
            //runEncoder(leftTarget, rightTarget, timeoutS, speed, false);
        }

        int rAdjust = 1;
        boolean rdir = true;
        int lAdjust = 1;
        boolean ldir = true;
        if(robot.rightFront.getCurrentPosition() > Rfront){//needs to go backwards
            rAdjust *= -1;
            rdir = false;
        }
        if(robot.leftFront.getCurrentPosition() > Lfront){//needs to go backwards
            lAdjust *= -1;
            ldir = false;
        }
        for(int i = 0; i <= speed * 10; i++){
            double power = i * .1;
            robot.rightFront.setPower(power * rAdjust);
            robot.rightBack.setPower(power * rAdjust);
            robot.leftFront.setPower(power * lAdjust);
            robot.leftBack.setPower(power * lAdjust);
        }
        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (checkDistance(Lback, robot.leftBack, ldir) &&
                        checkDistance(Rback, robot.rightBack, rdir))) {

            // Display it for the driver.
            telemetry.addData("Front",  "Running to %7d :%7d", Lfront,  Rfront);
            telemetry.addData("Back",  "Running to %7d :%7d", Lback,  Rback);
            telemetry.addData("front 2",  "Running at %7d :%7d",
                    robot.leftFront.getCurrentPosition(),
                    robot.rightFront.getCurrentPosition());
            telemetry.addData("back 2",  "Running at %7d :%7d",
                    robot.leftBack.getCurrentPosition(),
                    robot.rightBack.getCurrentPosition());
            telemetry.update();
        }

        // Stop all motion;
        robot.leftFront.setPower(0);
        robot.leftBack.setPower(0);
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
            lFront = Math.abs(robot.leftFront.getCurrentPosition()) + (int) (inches * countsPerInch);// goes 1 less for roll over
            lBack = Math.abs(robot.leftBack.getCurrentPosition()) + (int) (inches * countsPerInch);
            rFront = Math.abs(robot.rightFront.getCurrentPosition()) + (int) (inches * countsPerInch);// goes 1 less for roll over
            rBack = Math.abs(robot.rightBack.getCurrentPosition()) + (int) (inches * countsPerInch);
            runEncoder(lFront, lBack, rFront, rBack, timeoutS, speed);
        }
    }

    private void runEncoder(int Lfront, int Lback, int Rfront, int Rback, double timeoutS, //change to calc timeout
                            double speed) {
        if (!opModeIsActive()) {
            stop();
        }
        if (isStopRequested()) {
            robot.leftBack.setPower(0);
            robot.leftFront.setPower(0);
            robot.rightBack.setPower(0);
            robot.rightFront.setPower(0);
        } else {
            runtime.reset();
            boolean dir = robot.leftFront.getCurrentPosition() < Lfront;
            double delta = 10;
            // keep looping while we are still active, and there is time left, and both motors are running.

            for(int i = 0; i <= speed * 10; i++){
                double power = i * .1;
                if(checkDistance(Lfront, robot.leftFront, dir) && checkDistance(Lback, robot.leftBack, dir) &&
                        checkDistance(Rfront, robot.rightFront, dir) && checkDistance(Rback, robot.rightBack, dir)) {
                    robot.rightFront.setPower(power);
                    robot.rightBack.setPower(power);
                    robot.leftFront.setPower(power);
                    robot.leftBack.setPower(power);
                }
            }

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (checkDistance(Lfront, robot.leftFront, dir) && checkDistance(Lback, robot.leftBack, dir) &&
                    checkDistance(Rfront, robot.rightFront, dir) && checkDistance(Rback, robot.rightBack, dir))) {
                if(robot.rightFront.getCurrentPosition() > robot.leftFront.getCurrentPosition() + delta){
                    //slows the right front motor down if it is faster then the left one
                    robot.rightFront.setPower(Math.abs(speed) - .1);
                }
                if(robot.leftFront.getCurrentPosition() > robot.rightFront.getCurrentPosition() + delta){
                    //slows the left front motor down if it is faster then th right one
                    robot.leftFront.setPower(Math.abs(speed) - .1);
                }
                if(robot.rightBack.getCurrentPosition() > robot.leftBack.getCurrentPosition() + delta){
                    robot.rightBack.setPower(Math.abs(speed) - .1);
                }
                if(robot.leftBack.getCurrentPosition() > robot.rightBack.getCurrentPosition() + delta){
                    robot.leftBack.setPower(Math.abs(speed) - .1);
                }

                // Display it for the driver.
                telemetry.addData("Front", "Running to %7d :%7d", Lfront, Rfront);
                telemetry.addData("Back", "Running to %7d :%7d", Lback, Rback);
                telemetry.addData("front 2", "Running at %7d :%7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition());
                telemetry.addData("back 2", "Running at %7d :%7d",
                        robot.leftBack.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition());
                telemetry.update();
            }
            telemetry.addLine("STOP");
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
        if(robot.checkVoltage() > 12.3){//calculate slide
            tickTarget -= (4 * (robot.checkVoltage() - 12.3));
        }
        if(forwards)
            return Math.abs(tickTarget) > Math.abs(motor.getCurrentPosition());
        else
            return tickTarget < motor.getCurrentPosition();
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
