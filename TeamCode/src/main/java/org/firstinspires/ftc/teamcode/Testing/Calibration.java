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

        phone.setPosition(0.5);


        vuforiaDrive(-50.7);
        telemetry.addLine("Done Driving");
        nap(250);
        turnToDegree(60);
        driveStraight(33.5);
        spinToDegree(90);
        driveStraight(10);
        nap(250);

        for(int i = 0; i <3; i++){
            driveStraight(-10);
            nap(250);

            driveStraight(15);
        }


        //vuforiaTrack(-10,10,0);

        nap(10000);

    }
}
