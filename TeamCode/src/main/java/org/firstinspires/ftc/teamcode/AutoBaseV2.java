package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 10/31/2016.
 */

public class AutoBaseV2 extends LinearOpMode {
    public ElapsedTime runtime = new ElapsedTime();

    static final double countsPerInch = 89.174;
    static final double countsPerSideways = 89.174;
    static final double countsPerDegree = 15.88;
    static final double driveSpeed = 0.7;
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
        robot.leftFront.setTargetPosition(Lfront);
        robot.leftBack.setTargetPosition(Lback);
        robot.rightFront.setTargetPosition(Rfront);
        robot.rightBack.setTargetPosition(Rback);

        // Turn On RUN_TO_POSITION
        robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        robot.leftFront.setPower(Math.abs(speed));
        robot.leftBack.setPower(Math.abs(speed));
        robot.rightFront.setPower(Math.abs(speed));
        robot.rightBack.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.leftFront.isBusy() && robot.leftBack.isBusy()) &&
                (robot.rightFront.isBusy() && robot.rightBack.isBusy())) {

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

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        // Turn off RUN_TO_POSITION
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveStraight(double speed, double inches, double timeoutS) {//**make a calc timeout**
        int lFront;
        int lBack;
        int rFront;
        int rBack;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            lFront = Math.abs(robot.leftFront.getCurrentPosition()) + (int) ((inches - 1) * countsPerInch);// goes 1 less for roll over
            lBack = Math.abs(robot.leftBack.getCurrentPosition()) + (int) ((inches - 1) * countsPerInch);
            rFront = Math.abs(robot.rightFront.getCurrentPosition()) + (int) ((inches - 1) * countsPerInch);// goes 1 less for roll over
            rBack = Math.abs(robot.rightBack.getCurrentPosition()) + (int) ((inches - 1) * countsPerInch);
            runEncoder(lFront, lBack, rFront, rBack, timeoutS, speed, true);
        }
    }
    public void driveSideways(double inches, double timeoutS){
        /*double speed = .25;
        int sidewaysTarget = robot.sideways.getCurrentPosition();
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller
            sidewaysTarget = robot.sideways.getCurrentPosition() + (int) (inches * countsPerSideways);
        }

        robot.sideways.setTargetPosition(sidewaysTarget);

        // Turn On RUN_TO_POSITION
        robot.sideways.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        robot.sideways.setPower(Math.abs(speed));
        // keep looping while we are still active, and there is time left, and both motors are running.

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.sideways.isBusy())) {
            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d", sidewaysTarget);
            telemetry.addData("Path2", "Running at %7d",
                    robot.sideways.getCurrentPosition());
            telemetry.update();

            //sleep(500); //so we have time to read the telemetry
        }*/
    }

    private void runEncoder(int Lfront, int Lback, int Rfront, int Rback, double timeoutS, //change to calc timeout
                            double speed, boolean driveStraight) {
        if (!opModeIsActive()) {
            stop();
        }
        if (isStopRequested()) {
            robot.leftBack.setPower(0);
            robot.leftFront.setPower(0);
            robot.rightBack.setPower(0);
            robot.rightFront.setPower(0);
        } else {
            robot.leftFront.setTargetPosition(Lfront);
            robot.leftBack.setTargetPosition(Lback);
            robot.rightFront.setTargetPosition(Rfront);
            robot.rightBack.setTargetPosition(Rback);

            // Turn On RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.leftBack.isBusy()) &&
                    (robot.rightFront.isBusy() && robot.rightBack.isBusy())) {

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

            // Stop all motion;
            robot.leftFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightFront.setPower(0);
            robot.rightBack.setPower(0);

            robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            idle();
            // Turn off RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        }
        while((runtime.milliseconds() < time) && opModeIsActive()){
            //do nothing, this is a wait.
        }
    }
}
