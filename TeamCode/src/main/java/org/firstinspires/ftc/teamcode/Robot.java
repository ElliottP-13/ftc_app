package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Elliott on 10/31/2016.
 */
public class Robot{
    HardwareMap map;
    DcMotor leftMotor;
    DcMotor rightMotor;
    public Robot(HardwareMap HMap){
        // Save reference to Hardware map
        map = HMap;

        // Define and Initialize Motors
        leftMotor   = map.dcMotor.get("left_drive");
        rightMotor  = map.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
    }

}
