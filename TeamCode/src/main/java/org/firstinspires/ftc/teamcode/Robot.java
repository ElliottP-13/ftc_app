package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by Elliott on 10/31/2016.
 */
public class Robot{
    HardwareMap map;
    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor leftBack;
    DcMotor harvester;
    DcMotor shooter;

    ColorSensor color;
    double volts;
    DcMotorController  controller;
    public Robot(HardwareMap HMap){
        // Save reference to Hardware map
        map = HMap;

        rightFront = map.dcMotor.get("Right Front");
        rightBack = map.dcMotor.get("Right Back");
        leftFront = map.dcMotor.get("Left Front");
        leftBack = map.dcMotor.get("Left Back");

        harvester = map.dcMotor.get("Harvester");
        shooter = map.dcMotor.get("Shooter");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        controller = map.dcMotorController.get("left");
        color = map.colorSensor.get("color");
    }
    public double checkVoltage(){
        volts = map.voltageSensor.get("left").getVoltage();
        return volts;
    }

}
