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

    @Override
    public void runOpMode() {
        //do nothing because this is the base class
    }
    public void turn(double degrees, double timeout){
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

}
