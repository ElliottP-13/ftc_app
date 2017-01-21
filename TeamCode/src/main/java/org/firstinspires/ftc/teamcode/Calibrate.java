package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Elliott on 10/31/2016.
 */
@Autonomous(name = "Calibration", group = "Linear Opmode")
public class Calibrate extends AutoBase {
    @Override
    public void runOpMode() {
        initialize(hardwareMap);
        waitForStart();
        turn(100000, 30);
}
}
