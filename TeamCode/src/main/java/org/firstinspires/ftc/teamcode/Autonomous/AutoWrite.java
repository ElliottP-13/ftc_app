package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by epryor on 1/30/2017.
 */

@TeleOp(name = "Tele Write", group = "Linear Opmode")  // @Autonomous(...) is the other common choice
public class AutoWrite extends LinearOpMode {
    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor leftBack;

    private DcMotor arm1 = null;

    private Servo leftServo = null;
    private Servo rightServo = null;

    @Override
    public void runOpMode() {
        ReadWrite r = new ReadWrite();
        ArrayList<Double> leftPower = new ArrayList<Double>();
        ArrayList<Double> rightPower = new ArrayList<Double>();
        ArrayList<Double> armPower = new ArrayList<Double>();

        ArrayList<Double> servoLeft = new ArrayList<>();
        ArrayList<Double> servoRight = new ArrayList<>();


        ElapsedTime runtime = new ElapsedTime();
        initalize();
        waitForStart();
        runtime.reset();
        while ((runtime.seconds() <= 30) && !gamepad1.a){
            double rightJoyPower = gamepad1.right_stick_y;
            double leftJoyPower = gamepad1.left_stick_y;


            rightFront.setPower(rightJoyPower);
            rightBack.setPower(rightJoyPower);
            leftFront.setPower(leftJoyPower);
            leftBack.setPower(leftJoyPower);

            leftPower.add(leftJoyPower);
            rightPower.add(rightJoyPower);

            double armPow = -gamepad2.left_stick_y;
            arm1.setPower(armPow);

            if(gamepad2.right_bumper){
                leftServo.setPosition(leftServo.getPosition() - .03);
                rightServo.setPosition(rightServo.getPosition() + .03);
            } else if (gamepad2.left_bumper){
                leftServo.setPosition(leftServo.getPosition() + .015);
                rightServo.setPosition(rightServo.getPosition() - .015);
            }

            servoLeft.add(leftServo.getPosition());
            servoRight.add(rightServo.getPosition());

            armPower.add(armPow);

            telemetry.addLine("Time Left " + (30 - runtime.seconds()));
            telemetry.update();
            sleep(10);//sleep for a bit so we dont end up with a billion things in the array
        }
        telemetry.addLine("Done Creating Autonomous");
        telemetry.update();

        File rightSide = r.createInternalFile(hardwareMap.appContext, "RightSide.txt");
        r.overwriteFileWithString(createString(rightPower), rightSide);

        File leftSide = r.createInternalFile(hardwareMap.appContext, "LeftSide.txt");
        r.overwriteFileWithString(createString(leftPower), leftSide);

        File leftServo = r.createInternalFile(hardwareMap.appContext, "leftServo.txt");
        r.overwriteFileWithString(createString(servoLeft), leftServo);

        File rightServo = r.createInternalFile(hardwareMap.appContext, "rightServo.txt");
        r.overwriteFileWithString(createString(servoRight), rightServo);

        File arm = r.createInternalFile(hardwareMap.appContext, "arm.txt");
        r.overwriteFileWithString(createString(armPower), arm);

        telemetry.addLine("Done Writing");
        telemetry.update();
        sleep(1000);
        stop();
    }
    private String createString(ArrayList<Double> list){
        String str = "";
        for(int i = 0; i < list.size(); i++){
            str = str + list.get(i) + " ";
            telemetry.addLine("I is " + i);
            telemetry.update();
        }
        return str;
    }
    private void initalize(){
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftFront  = hardwareMap.get(DcMotor.class, "left front");
        leftBack = hardwareMap.get(DcMotor.class, "left back");
        rightFront  = hardwareMap.get(DcMotor.class, "right front");
        rightBack = hardwareMap.get(DcMotor.class, "right back");

        arm1 = hardwareMap.get(DcMotor.class, "arm");

        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");

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