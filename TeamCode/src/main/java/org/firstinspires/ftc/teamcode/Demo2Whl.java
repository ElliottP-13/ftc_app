package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Elliott Pryor on 9/10/2017.
 */

@TeleOp(name = "Demo 2Whl", group = "Linear Opmode")
public class Demo2Whl extends LinearOpMode {

    DcMotor left;
    DcMotor right;

    DcMotor shooter;

    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.dcMotor.get("Left");
        right = hardwareMap.dcMotor.get("Right");

        //shooter = hardwareMap.dcMotor.get("Shooter");

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        while(opModeIsActive()){
            right.setPower(-gamepad1.right_stick_y);
            left.setPower(-gamepad1.left_stick_y);

//            if(gamepad1.a){
//                shooter.setPower(1);
//            } else if (gamepad1.b){
//                shooter.setPower(-1);
//            } else {
//                shooter.setPower(0);
//            }

        }


    }
}
