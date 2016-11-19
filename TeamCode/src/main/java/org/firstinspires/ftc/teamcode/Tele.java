package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 11/3/2016.
 */
@TeleOp(name="Tele", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class Tele extends LinearOpMode {
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
        while (opModeIsActive()) {//runs until the end
            //drive
            if(slow){
                bot.rightMotor.setPower((-gamepad1.right_stick_y) / 1.5);
                bot.leftMotor.setPower((-gamepad1.right_stick_y) / 1.5);
            } else {
                bot.rightMotor.setPower(-gamepad1.right_stick_y);//drive
                bot.leftMotor.setPower(-gamepad1.left_stick_y);
            }
            //harvester
            clip(bot.harvester, gamepad2.left_stick_y, 0.5);
            //shooter
            //clip(bot.shooter, gamepad2.right_stick_y, 1);
            if (gamepad1.a && !a1Pressed) {//slows it down for bubb
                a1Pressed = true;
                slow = !slow    ;
            } else {
                a1Pressed = gamepad1.a;
            }
            if (gamepad2.left_bumper && !lBumper) {
                lBumper = true;
                bot.scanner.setPosition(.20);
            } else {
                lBumper = gamepad2.left_bumper;
            }

            if (gamepad2.right_bumper && !rBumper) {
                rBumper = true;
                bot.scanner.setPosition(.45);//aims a bit high
            } else {
                rBumper = gamepad2.left_bumper;
            }

            if (gamepad2.a && !aPressed) {
                aPressed = true;
                bot.scanner.setPosition(bot.scanner.getPosition() + .05);
            } else {
                aPressed = gamepad2.a;
            }
            
            if (gamepad2.b && !bPressed) {
                bPressed = true;
                bot.scanner.setPosition(bot.scanner.getPosition() - .05);
            } else {
                bPressed = gamepad2.b;
            }

            if (gamepad2.x && !xPressed) {
                xPressed = true;
                bot.scanner.setPosition(bot.scanner.getPosition() + .10);
            } else {
                xPressed = gamepad2.x;
            }

            if (gamepad2.y && !yPressed) {
                yPressed = true;
                bot.scanner.setPosition(bot.scanner.getPosition() - .10);
            } else {
                yPressed = gamepad2.y;
            }
            telemetry.addLine("" + bot.scanner.getPosition());
            telemetry.update();
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
