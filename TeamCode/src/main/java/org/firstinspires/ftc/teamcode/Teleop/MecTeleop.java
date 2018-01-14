package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by pryor on 11/12/2017.
 */

@TeleOp(name="Mecanum Drive", group="Linear Opmode")
public class MecTeleop extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    private DcMotor arm1 = null;

    private Servo leftServo = null;
    private Servo rightServo = null;

    private ColorSensor sensor = null;

    private Servo dropper = null;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        //arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //arm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

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


            telemetry.addLine("" + sensor.blue());

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


        }

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
        dropper = hardwareMap.get(Servo.class, "dropper");
        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");

        sensor = hardwareMap.get(ColorSensor.class, "sensor");

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
