package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Demo 2", group = "Linear Opmode")
public class Demo2 extends LinearOpMode {

    DcMotor leftFront;
    DcMotor leftBack;
    DcMotor rightFront;
    DcMotor rightBack;

    DcMotor shooter;

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("Left Front");
        leftBack = hardwareMap.dcMotor.get("Left Back");
        rightFront = hardwareMap.dcMotor.get("Right Front");
        rightBack = hardwareMap.dcMotor.get("Right Back");

        shooter = hardwareMap.dcMotor.get("Shooter");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        while(opModeIsActive()){
            rightFront.setPower(-gamepad1.right_stick_y);
            rightBack.setPower(-gamepad1.right_stick_y);
            leftFront.setPower(-gamepad1.left_stick_y);
            leftBack.setPower(-gamepad1.left_stick_y);

            if(gamepad1.a){
                shooter.setPower(1);
            } else if (gamepad1.b){
                shooter.setPower(-1);
            } else {
                shooter.setPower(0);
            }

        }


    }
}