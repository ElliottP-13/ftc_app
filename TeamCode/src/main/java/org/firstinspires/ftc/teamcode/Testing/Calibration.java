package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Autonomous.Robot;

/**
 * Created by pryor on 1/17/2018.
 */
@Autonomous (name = "Calibration", group = "Test")
public class Calibration extends Robot {
    @Override
    public void runOpMode() throws InterruptedException {
        initialize(hardwareMap);

        waitForStart();

        //driveStraight(100);

        //driveTime(125);

        //Collumn 1 (L)
        //driveStraight(24.5);
        vuforiaDrive(24.5);
        nap(250);
        turnToDegree(90);
        driveStraight(14);

        nap(10000);

    }
}
