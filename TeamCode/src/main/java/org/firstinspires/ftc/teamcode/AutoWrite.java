package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by epryor on 1/30/2017.
 */

@TeleOp(name = "Tele Write", group = "Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class AutoWrite extends LinearOpMode {
    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor leftBack;
    @Override
    public void runOpMode() {
        ReadWrite r = new ReadWrite();
        ArrayList<Double> leftPower = new ArrayList<Double>();
        ArrayList<Double> rightPower = new ArrayList<Double>();
        
        ElapsedTime runtime = new ElapsedTime();
        initalize();
        waitForStart();
        runtime.reset();
        while (runtime.seconds() <= 30){
            double rightJoyPower = -gamepad1.right_stick_y;
            double leftJoyPower = -gamepad1.left_stick_y;
            rightFront.setPower(rightJoyPower);
            rightBack.setPower(rightJoyPower);
            leftFront.setPower(leftJoyPower);
            leftBack.setPower(leftJoyPower);

            leftPower.add(leftJoyPower);
            rightPower.add(rightJoyPower);

            telemetry.addLine("Time Left " + (30 - runtime.seconds()));
            telemetry.update();
            sleep(10);//sleep for a bit so we dont end up with a billion things in the array
        }
        telemetry.addLine("Done Creating Autonomous");
        telemetry.update();
        File rightSide = r.createFileIfNotExists("RightSide.txt");
        r.overwriteFileWithString(createString(rightPower), rightSide);

        telemetry.addLine("DONE WITH RIGHT SIDE");
        telemetry.update();
        sleep(1000);

        File leftSide = r.createFileIfNotExists("LeftSide.txt");
        r.overwriteFileWithString(createString(leftPower), leftSide);
        telemetry.addLine("Done Writing");
        telemetry.update();
        sleep(1000);
        stop();
    }
    private String createString(ArrayList<Double> list){
        String str = "";
        for(int i = 0; i < list.size(); i++){
            str = str + list.get(i) + " , ";
            telemetry.addLine("I is " + i);
            telemetry.update();
        }
        return str;
    }
    private void initalize(){
        rightFront = hardwareMap.dcMotor.get("right front");
        rightBack = hardwareMap.dcMotor.get("right back");
        leftFront = hardwareMap.dcMotor.get("left front");
        leftBack = hardwareMap.dcMotor.get("left back");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

    }
}
