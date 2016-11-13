package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 10/31/2016.
 */

public class AutoBase extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1120;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.75;     // For figuring circumference
    static final double countsPerInch = 95.12;
    static final double countsPerDegree = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double driveSpeed = 0.7;
    static final double turnSpeed = 0.5;
    static final double miliPerInch = 52.57;
    static final double miliPerDegree = 12.55;
    static final double voltChange = 0.7;
    Robot robot = null;

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "Blue", new String[]{"Blue", "Red"});
    AutonomousTextOption startPosition = new AutonomousTextOption("Start Position", "One", new String[]{"One", "Two"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousTextOption start = new AutonomousTextOption("Hit Cap Ball", "Hit Cap", new String[]{"Hit Cap", "Go Corner", "Neither"});
    AutonomousBooleanOption pressBootin = new AutonomousBooleanOption("Bootin Press", true);
    AutonomousBooleanOption pressOtherBootin = new AutonomousBooleanOption("Bootin 2 Press", true);
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

    public void turn(double speed, double degrees, double timeoutS) {//defaults right, turn left w/ -degrees
        int leftTarget;
        int rightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            leftTarget = robot.leftMotor.getCurrentPosition() + (int) (degrees * countsPerDegree * directonAdjust);
            rightTarget = robot.rightMotor.getCurrentPosition() - (int) (degrees * countsPerDegree * directonAdjust);
            runEncoder(leftTarget, rightTarget, timeoutS, speed);
        }
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
            robot.leftMotor.setPower(Lpower);
            robot.rightMotor.setPower(Rpower);
            while ((runtime.milliseconds() < ((miliPerInch * Math.abs(inches)) + voltAjust * robot.checkVoltage())) && opModeIsActive()) {
                //do nothing because we are waiting for it to finish
                telemetry.addLine("Running to: " + (miliPerInch * inches) + voltAjust * robot.checkVoltage());
                telemetry.addLine("Currently at: " + runtime.milliseconds());
                telemetry.update();
            }
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
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
            robot.leftMotor.setPower(.7);
            robot.rightMotor.setPower(-.7);
        }
        if (degrees < 0) {//turn left
            robot.leftMotor.setPower(-.7);
            robot.rightMotor.setPower(.7);
        }
        while ((runtime.milliseconds() < (miliPerDegree * Math.abs(degrees)) + voltAdjust * robot.checkVoltage()) && opModeIsActive()) {
            //does nothing because we are waiting for it to finish
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        sleep(125);
    }

    public void driveStraight(double speed, double inches, double timeoutS) {//**make a calc timeout**
        int leftTarget;
        int rightTarget;

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            leftTarget = robot.leftMotor.getCurrentPosition() + (int) (inches * countsPerInch);
            rightTarget = robot.rightMotor.getCurrentPosition() + (int) (inches * countsPerInch);
            runEncoder(leftTarget, rightTarget, timeoutS, speed);
        }
    }

    public void detectLine() {
        while ((robot.lineSensor.getRawLightDetected() < 120) && opModeIsActive()) {//loops through until we see the line
            robot.leftMotor.setPower(.5);
            robot.rightMotor.setPower(.5);
        }

        print("Found Line!");
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);
        turn(90, 5);//turns right
        //hopefully we are right in front of beacon
        scanSensors();//we see the button.
    }

    public void scanSensors() {//scans at 3 different angles so we get a better idea of what it is
        int[][] values = new int[3][3];
        robot.scanner.setPosition(.25);
        values = putValues(values, 0);//puts them all in the array so i can look at them later and compare
        robot.scanner.setPosition(.5);
        values = putValues(values, 1);//hopefully i don't lose my previously stored info
        robot.scanner.setPosition(.75);
        values = putValues(values, 2);

        //calculate
        int sumRed = 0;
        for (int i = 0; i < values.length; i++) {
            sumRed += values[i][0];//adds all of the values together
        }
        int sumBlue = 0;
        for (int i = 0; i < values.length; i++) {
            sumBlue += values[i][2];
        }
        if (sumBlue > sumRed) {
            //its blue
            print("Its Blue");
        } else if (sumRed > sumBlue) {
            //its red
            print("Its red");
        } else {
            //something went wrong, lets try again
            //maybe move forward or backwards?
            scanSensors();
        }

    }

    private int[][] putValues(int[][] array, int scanNum) {
        array[scanNum][0] = robot.sensor.red();
        array[scanNum][1] = robot.sensor.green();
        array[scanNum][2] = robot.sensor.blue();

        return array;
    }

    private void runEncoder(int LtargetPos, int RtargetPos, double timeoutS, double speed) {//change to calc timeout
        robot.leftMotor.setTargetPosition(LtargetPos);
        robot.rightMotor.setTargetPosition(RtargetPos);

        // Turn On RUN_TO_POSITION
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        robot.leftMotor.setPower(Math.abs(speed));
        robot.rightMotor.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        boolean passedTarget = false;
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.leftMotor.isBusy() && robot.rightMotor.isBusy()) &&
                !passedTarget) {
            int delta = Math.abs(robot.leftMotor.getCurrentPosition() - robot.rightMotor.getCurrentPosition());
            if (delta > 10) {
                if (Math.abs(robot.leftMotor.getCurrentPosition()) > Math.abs(robot.rightMotor.getCurrentPosition())) {
                    robot.leftMotor.setPower(robot.leftMotor.getPower() - .01);
                } else if (Math.abs(robot.leftMotor.getCurrentPosition()) < Math.abs(robot.rightMotor.getCurrentPosition())) {
                    robot.rightMotor.setPower(robot.rightMotor.getPower() - .01);
                } else {
                    robot.leftMotor.setPower(Math.abs(speed));
                    robot.rightMotor.setPower(Math.abs(speed));
                }
            }

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", LtargetPos, RtargetPos);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    robot.leftMotor.getCurrentPosition(),
                    robot.rightMotor.getCurrentPosition());
            telemetry.update();
            passedTarget = (Math.abs(robot.leftMotor.getCurrentPosition()) > Math.abs(LtargetPos)) ||
                    (Math.abs(robot.rightMotor.getCurrentPosition()) > Math.abs(RtargetPos));
        }

        // Stop all motion;
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
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
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
