package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * Created by Elliott on 12/07/2016.
 */
public class DemoRobot{
    HardwareMap map;
    DcMotor leftMotor;
    DcMotor rightMotor ;
    DcMotor sideways;
    DcMotor harvester;
    DcMotor shooter;

    Servo scanner;
    ColorSensor sensor;
    LightSensor lineSensor;
    double volts;
    DcMotorController  drive;
    public DemoRobot(HardwareMap HMap){
        // Save reference to Hardware map
        map = HMap;

        // Define and Initialize Motors
        leftMotor = map.dcMotor.get("Left Drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor = map.dcMotor.get("Right Drive");
        sideways = map.dcMotor.get("Sideways");
        harvester = map.dcMotor.get("Harvester");
        drive = leftMotor.getController();
        volts = map.voltageSensor.get("drive").getVoltage();

        scanner.setPosition(.95);//initalize
        sensor.enableLed(false);

    }
    public double checkVoltage(){
        volts = map.voltageSensor.get("drive").getVoltage();
        return volts;
    }

}
