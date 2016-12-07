package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Elliott on 12/1/2016.
 */
@Autonomous(name = "Auto Dumb", group = "Linear Opmode")
public class DumbAuto extends LinearOpMode{
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftMotor;
    DcMotor rightMotor;

    static final double miliPerInch = 52.57;
    static final double miliPerDegree = 8.366;
    @Override
    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("Left Drive");
        rightMotor = hardwareMap.dcMotor.get("Right Drive");

        while(opModeIsActive()) {
            driveTime(24);
            turnTime(90);
            driveTime(24);
            turnTime(90);
            driveTime(24);
            turnTime(90);
            driveTime(24);
            turnTime(90);
        }
    }
    public void driveTime(double inches) {
        if (opModeIsActive()) {
            runtime.reset();
            double Rpower = 1;
            double Lpower = Rpower - .01;
            double voltAjust = 0;
            if (inches < 0) {
                Lpower *= -1;
                Rpower *= -1;
            }
            leftMotor.setPower(Lpower);
            rightMotor.setPower(Rpower);
            while ((runtime.milliseconds() < ((miliPerInch * Math.abs(inches)))) && opModeIsActive()) {
                //do nothing because we are waiting for it to finish
                telemetry.addLine("Running to: " + (miliPerInch * inches));
                telemetry.addLine("Currently at: " + runtime.milliseconds());
                telemetry.update();
            }
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            sleep(125);
        }
    }

    public void turnTime(double degrees) {
        double voltAdjust = 0;
        runtime.reset();
        if (degrees > 0) {//turn right
            leftMotor.setPower(.7);
            rightMotor.setPower(-.7);
        }
        if (degrees < 0) {//turn left
            leftMotor.setPower(-.7);
            rightMotor.setPower(.7);
        }
        while ((runtime.milliseconds() < (miliPerDegree * Math.abs(degrees))) && opModeIsActive()) {
            //does nothing because we are waiting for it to finish
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        sleep(125);
    }
}
