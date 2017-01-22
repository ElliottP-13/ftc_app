package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 11/3/2016.
 */
@TeleOp(name = "Tele", group = "Linear Opmode")  // @Autonomous(...) is the other common choice
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

        boolean a1Pressed = false;
        boolean slow = false;
        while (opModeIsActive()) {//runs until the end

            //bot.harvester.setPower(.75);
            if (slow) {//runs the drive
                bot.rightBack.setPower((-gamepad1.right_stick_y) / 3);
                bot.leftBack.setPower((-gamepad1.left_stick_y) / 3);
            } else {
                bot.rightBack.setPower(-gamepad1.right_stick_y);
                bot.leftBack.setPower(-gamepad1.left_stick_y);
            }
            if (gamepad1.a && !a1Pressed) {//slows it down for bubb
                a1Pressed = true;
                slow = !slow;
            } else {
                a1Pressed = gamepad1.a;
            }

            clip(bot.harvester, gamepad2.left_stick_y, .75);
            clip(bot.shooter, gamepad2.right_stick_y, 1);
            if (gamepad2.right_bumper) {
                bot.shooter.setPower(.5);
            } else {
                bot.shooter.setPower(0);
            }
        }

    }

    private void clip(DcMotor motor, float stickValue, double power) {
        if (stickValue >= .25) {
            motor.setPower(power);//sets constants so that it isn't too touchy
        } else if (stickValue <= -.25) {
            motor.setPower(-power);//sets constants so that it isn't too touchy
        } else {
            motor.setPower(0);//otherwise turn it off
        }
    }

    private void print(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }
}
