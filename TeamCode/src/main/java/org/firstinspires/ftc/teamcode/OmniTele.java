package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 11/3/2016.
 */
@TeleOp(name="Omni", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class OmniTele extends LinearOpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        Robot bot = new Robot(hardwareMap);
        print("init");
        waitForStart();
        runtime.reset();

        boolean backwards = false;
        boolean aPressed = false;
        boolean a1Pressed = false;
        boolean bPressed = false;
        boolean xPressed = false;
        boolean yPressed = false;
        boolean rBumper = false;
        boolean lBumper = false;
        boolean slow = false;

        double rFPower = .8;
        double rBPower = .8;
        double lFPower = .8;
        double lBPower = .8;

        double count = -1;
        while (opModeIsActive()) {//runs until the end
            //drive

            if(gamepad1.a){
                bot.rightFront.setPower(-1 + .25);
                bot.leftFront.setPower(1 - .5);
                bot.rightBack.setPower(1 - .25);
                bot.leftBack.setPower(-1);
            }
            else if(gamepad1.x){
                bot.rightFront.setPower(-1 + .1);
                bot.leftFront.setPower(1 - .5);
                bot.rightBack.setPower(1 - .1);
                bot.leftBack.setPower(-1);
            }
            else if(gamepad1.b){
                bot.rightFront.setPower(rFPower);
                bot.leftFront.setPower(lFPower);
                bot.rightBack.setPower(-rBPower);
                bot.leftBack.setPower(-lBPower);
            }
            else if(gamepad1.y){
                bot.rightFront.setPower(-rFPower);
                bot.leftFront.setPower(-lFPower);
                bot.rightBack.setPower(rBPower);
                bot.leftBack.setPower(lBPower);
            }
            else{
                bot.rightFront.setPower(-gamepad1.right_stick_y);
                bot.rightBack.setPower(-gamepad1.right_stick_y);
                bot.leftFront.setPower(-gamepad1.left_stick_y);
                bot.leftBack.setPower(-gamepad1.left_stick_y);
            }

        }

    }
    private void clip(DcMotor motor, float stickValue, double power){
        if(stickValue >= .25){
            motor.setPower(power);//sets constants so that it isn't too touchy
        }
        else if(stickValue <= -.25){
            motor.setPower(-power);//sets constants so that it isn't too touchy
        }
        else {
            motor.setPower(0);//otherwise turn it off
        }
    }
    private void print(String s){
        telemetry.addLine(s);
        telemetry.update();
    }
}
