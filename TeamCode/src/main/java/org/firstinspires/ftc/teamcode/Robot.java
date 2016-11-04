package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Elliott on 10/31/2016.
 */
public class Robot{
    HardwareMap map;
    DcMotor leftMotor;
    DcMotor rightMotor ;
    DcMotor harvester;
    DcMotor shooter;
    Servo scanner;
    ColorSensor sensor;
    LightSensor lineSensor;
    public Robot(HardwareMap HMap){
        // Save reference to Hardware map
        map = HMap;

        // Define and Initialize Motors
        leftMotor = map.dcMotor.get("Left Drive");
        rightMotor = map.dcMotor.get("Right Drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        //harvester = hardwareMap.dcMotor.get("Harvester");
        //shooter = hardwareMap.dcMotor.get("Shooter");
        scanner = map.servo.get("Scanner");
        sensor = map.colorSensor.get("Sensor");
        lineSensor = map.lightSensor.get("Line Sensor");

    }

}
