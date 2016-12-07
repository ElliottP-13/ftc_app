package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 11/3/2016.
 */
@TeleOp(name="Demo", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class DemoTele extends LinearOpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        Robot bot = new Robot(hardwareMap);
        print("init");
        waitForStart();
        runtime.reset();

        boolean aPressed = false;
        boolean bPressed = false;
        boolean xPressed = false;
        boolean yPressed = false;
        boolean rBumper = false;
        boolean lBumper = false;
        while (opModeIsActive()) {//runs until the end

            bot.rightMotor.setPower(-gamepad1.right_stick_y);//drive
            bot.leftMotor.setPower(-gamepad1.left_stick_y);

            if(gamepad1.right_bumper){
                bot.sideways.setPower(.5);
            }
            else if(gamepad1.left_bumper){
                bot.sideways.setPower(-.5);
            }
            else{
                bot.sideways.setPower(0);
            }

            //harvester
            clip(bot.harvester, gamepad2.left_stick_y, 0.5);
            //shooter
            //clip(bot.shooter, gamepad2.right_stick_y, 1);
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
