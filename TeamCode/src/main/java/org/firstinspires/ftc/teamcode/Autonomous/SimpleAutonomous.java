package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by pryor on 11/13/2017.
 */
@Autonomous(name = "Simple Autonomous")
public class SimpleAutonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    private DcMotor arm1 = null;
    private DcMotor arm2 = null;

    private Servo leftServo = null;
    private Servo rightServo = null;

    private ColorSensor sensor = null;

    private Servo dropper = null;

    private double msPerCm = 11;//just a guess we will refine later
    //msPerCm log
    //11 = 11.8V

    private double msPerDeg = 300; //just a guess we will refine later

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();
        waitForStart();

        arm1.setPower(.7);
        arm2.setPower(.7);
        nap(200);

        arm1.setPower(0);
        arm2.setPower(0);

        driveStraight(67);
        //turn(50);//13 volts 50 deg= 90 deg

        stop();

    }



    private void driveStraight(double cm){
        double power = 0.7;
        double dir = (cm > 0) ? 1 : -1;//if cm is > 0 go forward, if < 0 go backward
        runtime.reset();
        double timeToRun = Math.abs(cm) * msPerCm;

        leftFront.setPower(power * dir);
        leftBack.setPower(power * dir);
        rightFront.setPower(power * dir);
        rightBack.setPower(power * dir);
        runtime.reset();

        telemetry.addLine("Duration: " + timeToRun);
        telemetry.addLine("LeftFront " + power*dir);
        telemetry.update();

        while((runtime.milliseconds() < timeToRun) && opModeIsActive()){
            //wait for the motors to finish
        }

        telemetry.addLine("Finished");
        telemetry.update();

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    private void turn(double deg){
        double power = 0.7;
        double dir = (deg > 0) ? 1 : -1;//if cm is > 0 go forward, if < 0 go backward
        runtime.reset();
        double timeToRun = deg * msPerCm;
        while(runtime.milliseconds() < timeToRun){
            leftFront.setPower(power * -dir);
            leftBack.setPower(power * -dir);
            rightFront.setPower(power * dir);
            rightBack.setPower(power * dir);
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
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
        arm2 = hardwareMap.get(DcMotor.class, "arm 2");
        //dropper = hardwareMap.get(Servo.class, "dropper");
        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");

        //sensor = hardwareMap.get(ColorSensor.class, "sensor");

        //dropper.setPosition(0);

        leftServo.setPosition(0.52);
        rightServo.setPosition(0.59);

        arm2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

    }

    private void nap(double mili){
        runtime.reset();

        while((runtime.milliseconds() < mili) && opModeIsActive()){
            //do nothing!
        }
    }
}
