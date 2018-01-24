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

//        vuforiaDrive(55.5);
//        nap(250);
//        turnToDegree(90);
//        driveStraight(29);

        vuforiaTrack(0,0,0);

        nap(10000);

    }
}
