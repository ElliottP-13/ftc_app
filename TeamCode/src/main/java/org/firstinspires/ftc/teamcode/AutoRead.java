package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by epryor on 1/30/2017.
 */
@Autonomous(name = "Auto Read", group = "Linear Opmode")
public class AutoRead extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    private DcMotor arm1 = null;

    private Servo leftServo = null;
    private Servo rightServo = null;

    @Override
    public void runOpMode() {
        ReadWrite r = new ReadWrite();
//        File rightFile = new File(hardwareMap.appContext.getFilesDir() + "/RightSide.txt");
//        String[] right = r.readEntireFile(rightFile).split(" ");
//        telemetry.addLine(r.readEntireFile(rightFile));
//        telemetry.update();
//        sleep(2000);
//        ArrayList<Double> rightPower = parsePower(right);
//
//        File leftFile = new File(hardwareMap.appContext.getFilesDir() + "/LeftSide.txt");
//        String[] left = r.readEntireFile(leftFile).split(" ");
//        ArrayList<Double> leftPower = parsePower(left);
        ArrayList<Double> rightPower = parsePower(r.readNstoreArrayList(hardwareMap.appContext.getFilesDir() + "/RightSide.txt", " "));
        ArrayList<Double> leftPower = parsePower(r.readNstoreArrayList(hardwareMap.appContext.getFilesDir() + "/LeftSide.txt", " "));
        ArrayList<Double> leftServoPos = parsePower(r.readNstoreArrayList(hardwareMap.appContext.getFilesDir() + "/leftServo.txt", " "));
        ArrayList<Double> rightServoPos = parsePower(r.readNstoreArrayList(hardwareMap.appContext.getFilesDir() + "/rightServo.txt", " "));
        ArrayList<Double> armPower = parsePower(r.readNstoreArrayList(hardwareMap.appContext.getFilesDir() + "/arm.txt", " "));



        initalize();
        waitForStart();
        boolean run = true;
        int i = 0;
        double setRightPower = 0;
        double setLeftPower = 0;
        double setLeftServo = 0;
        double setRightServo = 0;
        double setArmPower = 0;


        while (opModeIsActive() && run){

            if(i < rightPower.size() && i < leftPower.size()) {
                setRightPower = rightPower.get(i);
                setLeftPower = leftPower.get(i);
                setArmPower = armPower.get(i);
                setRightServo = rightServoPos.get(i);
                setLeftServo = leftServoPos.get(i);
            }
            else {
                setRightPower = 0;
                setLeftPower = 0;
                run = false;
            }
            rightFront.setPower(setRightPower);
            rightBack.setPower(setRightPower);
            leftFront.setPower(setLeftPower);
            leftBack.setPower(setLeftPower);

            leftServo.setPosition(setLeftServo);
            rightServo.setPosition(setRightServo);

            arm1.setPower(setArmPower);

            i++;
            sleep(10);//sleep so it is the same timing as the writer
        }
        telemetry.addLine("ALL DONE");
        telemetry.update();

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);
        arm1.setPower(0);

        stop();

    }
    private ArrayList<Double> parsePower(ArrayList<String> list){
        telemetry.addLine("Original length " + list.size());
        telemetry.update();
        sleep(2000);
        ArrayList<Double> ret = new ArrayList<Double>();
        if(list.get(0) != null) {
            for (int i = 0; i < list.size(); i++) {
                double d = Double.parseDouble(list.get(i));
                ret.add(d);
            }
            return ret;
        }
        return ret;
    }
    private void initalize(){
        leftFront  = hardwareMap.get(DcMotor.class, "left front");
        leftBack = hardwareMap.get(DcMotor.class, "left back");
        rightFront  = hardwareMap.get(DcMotor.class, "right front");
        rightBack = hardwareMap.get(DcMotor.class, "right back");

        arm1 = hardwareMap.get(DcMotor.class, "arm");
        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");


        leftServo.setPosition(0.52);
        rightServo.setPosition(0.59);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
    }
}