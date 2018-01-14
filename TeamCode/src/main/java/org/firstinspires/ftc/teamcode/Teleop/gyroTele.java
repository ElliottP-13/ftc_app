package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * Created by pryor on 1/11/2018.
 */

@TeleOp(name = "Gyro Tele", group = "Tele")
public class gyroTele extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    private DcMotor arm1 = null;

    private DcMotor leftGrab = null;
    private DcMotor rightGrab = null;
    private DcMotor spindle = null;
    private Servo leftServo = null;
    private Servo rightServo = null;

    private ColorSensor colorSensor = null;
    private DistanceSensor distanceSensor = null;

    private BNO055IMU imu = null;
    private double initialHeading;

    private Servo dropper = null;



    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while (opModeIsActive()){

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);

            double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;
            final double lf = r * Math.cos(robotAngle) + rightX;
            final double rf = r * Math.sin(robotAngle) - rightX;
            final double lb = r * Math.sin(robotAngle) + rightX;
            final double rb = r * Math.cos(robotAngle) - rightX;

            double div = 1;

            if(gamepad1.left_bumper){
                div = 2;
            } else {
                div = 1;
            }

            leftFront.setPower(lf/div);
            rightFront.setPower(rf/div);
            leftBack.setPower(lb/div);
            rightBack.setPower(rb/div);

            //arm1.setPower(1);
            //arm2.setPower(1);

            double armPow = -gamepad2.left_stick_y;
            arm1.setPower(armPow);

            telemetry.update();


            if(gamepad2.right_bumper){
                leftServo.setPosition(leftServo.getPosition() - .03);
                rightServo.setPosition(rightServo.getPosition() + .03);
            } else if (gamepad2.left_bumper){
                leftServo.setPosition(leftServo.getPosition() + .015);
                rightServo.setPosition(rightServo.getPosition() - .015);
            } else if (gamepad2.x){
                leftServo.setPosition(.52);
                rightServo.setPosition(.59);
            }


            if(gamepad2.a){
               spindle.setPower(1);
            }
            else if(gamepad2.b) {
                spindle.setPower(-1);
            } else {
                spindle.setPower(0);
            }

            leftGrab.setPower(gamepad2.right_stick_y);
            rightGrab.setPower(gamepad2.right_stick_y);

            //right stick down sucks in
            //a == deploy
            //b == pull in

            telemetry.addData("Heading", "Angle = %.2f", getRelativeHeading());

            telemetry.update();
        }
    }

    private void resetHeading(){
        initialHeading = getAbsoluteHeading();
    }

    private double getAbsoluteHeading(){
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    private double getRelativeHeading(){
        return AngleUnit.DEGREES.normalize(getAbsoluteHeading() - initialHeading);
    }

    private void initialize(){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftFront  = hardwareMap.get(DcMotor.class, "left front");
        leftBack = hardwareMap.get(DcMotor.class, "left back");
        rightFront  = hardwareMap.get(DcMotor.class, "right front");
        rightBack = hardwareMap.get(DcMotor.class, "right back");

        arm1 = hardwareMap.get(DcMotor.class, "arm");
        leftGrab = hardwareMap.get(DcMotor.class, "grabber left");
        rightGrab = hardwareMap.get(DcMotor.class,"grabber right");
        spindle = hardwareMap.get(DcMotor.class, "spindle");

        dropper = hardwareMap.get(Servo.class, "dropper");
        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");

        colorSensor = hardwareMap.get(ColorSensor.class, "sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor");

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);





        dropper.setPosition(0);

        leftServo.setPosition(1);
        rightServo.setPosition(-1);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

    }

}
