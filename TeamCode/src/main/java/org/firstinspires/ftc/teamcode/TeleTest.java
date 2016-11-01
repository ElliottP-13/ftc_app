package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 10/28/2016.
 */
@TeleOp(name="Tele Test", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class TeleTest extends LinearOpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor = null;
    DcMotor rightMotor = null;

    @Override
    public void runOpMode() {
        print("init");
        leftMotor = hardwareMap.dcMotor.get("Left Drive");
        rightMotor = hardwareMap.dcMotor.get("Right Drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {//runs until the end
            rightMotor.setPower(-gamepad1.right_stick_y);
            leftMotor.setPower(-gamepad1.left_stick_y);
            print("" + -gamepad1.right_stick_y);
            print("" + -gamepad1.left_stick_y);
        }

    }
    private void print(String s){
        telemetry.addLine(s);
        telemetry.update();
    }
}
