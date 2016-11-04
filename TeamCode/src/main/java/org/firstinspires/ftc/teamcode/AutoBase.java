package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 10/31/2016.
 */

public class AutoBase extends LinearOpMode{
    public ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.75 ;     // For figuring circumference
    static final double     countsPerInch           = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     countsPerDegree         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     driveSpeed              = 0.7;
    static final double     turnSpeed               = 0.5;

    Robot robot = new Robot(hardwareMap);

    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "Blue", new String[]{"Blue", "Red"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousBooleanOption pressBootin = new AutonomousBooleanOption("Bootin Press", true);
    AutonomousBooleanOption pressOtherBootin = new AutonomousBooleanOption("Bootin 2 Press", true);
    AutonomousTextOption afterButton = new AutonomousTextOption("After Button", "Park Corner", new String[]{"Park Corner", "Block Buttons"});

    AutonomousOption[] autoOptions = {allianceColor, waitStart, pressBootin, pressOtherBootin, afterButton};
    int currentOption = 0;

    double directonAdjust = 1.0;

    @Override
    public void runOpMode() {
        //do nothing because this is the base class
    }
    public void initialize(){
        selectOptions();
        if (allianceColor.getValue().equals("Red")) {
            directonAdjust = -1.0;
        }
    }
    public void turn(double degrees, double timeout){//defaults to the right, go left w/ -degrees
        turn(turnSpeed, degrees, timeout);
    }
    public void driveStraight(double inches, double timeout){
        driveStraight(driveSpeed, inches, timeout);
    }
    public void turn(double speed, double degrees, double timeoutS){//defaults right, turn left w/ -degrees
        int leftTarget;
        int rightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            leftTarget = robot.leftMotor.getCurrentPosition() + (int) (degrees * countsPerDegree);
            rightTarget = robot.rightMotor.getCurrentPosition() - (int) (degrees * countsPerDegree);
            runEncoder(leftTarget, rightTarget, timeoutS, speed);
        }
    }

    public void driveStraight(double speed, double inches, double timeoutS) {//**make a calc timeout**
        int leftTarget;
        int rightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            leftTarget = robot.leftMotor.getCurrentPosition() + (int) (inches * countsPerInch);
            rightTarget = robot.rightMotor.getCurrentPosition() + (int) (inches * countsPerInch);
            runEncoder(leftTarget, rightTarget, timeoutS, speed);
        }
    }
    public void detectLine(){
        while(robot.lineSensor.getRawLightDetected() < 120){//loops through until we see the line
            robot.leftMotor.setPower(.3);
            robot.rightMotor.setPower(.3);
        }
        turn(90, 5);//turns right
        //hopefully we are right in front of beacon
        scanSensors();//we see the button.
    }
    public  void scanSensors(){//scans at 3 different angles so we get a better idea of what it is
        int[][] values = new int[3][3];
        robot.scanner.setPosition(.25);
        values = putValues(values, 0);//puts them all in the array so i can look at them later and compare
        robot.scanner.setPosition(.5);
        values = putValues(values, 1);//hopefully i don't lose my previously stored info
        robot.scanner.setPosition(.75);
        values = putValues(values, 2);

        //calculate
        int sumRed = 0;
        for (int i = 0; i < values.length; i++){
            sumRed += values[i][0];//adds all of the values together
        }
        int sumBlue = 0;
        for (int i = 0; i < values.length; i++){
            sumBlue += values[i][2];
        }
        if(sumBlue > sumRed){
            //its blue
        }
        else if(sumRed > sumBlue){
            //its red
        }
        else{
            //something went wrong, lets try again
            //maybe move forward or backwards?
            scanSensors();
        }

    }
    private int[][] putValues(int[][] array, int scanNum){
        array[scanNum][0] = robot.sensor.red();
        array[scanNum][1] = robot.sensor.green();
        array[scanNum][2] = robot.sensor.blue();

        return array;
    }
    private void runEncoder(int LtargetPos, int RtargetPos, double timeoutS, double speed){//change to calc timeout
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
        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS) &&
                (robot.leftMotor.isBusy() && robot.rightMotor.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1",  "Running to %7d :%7d", LtargetPos,  RtargetPos);
            telemetry.addData("Path2",  "Running at %7d :%7d",
                    robot.leftMotor.getCurrentPosition(),
                    robot.rightMotor.getCurrentPosition());
            telemetry.update();
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
        telemetry.addLine("Current Option: " + str);
        telemetry.update();
    }
    private void showOptions(String additonalInfo) {
        telemetry.addLine(additonalInfo);
        showOptions();
    }
}
