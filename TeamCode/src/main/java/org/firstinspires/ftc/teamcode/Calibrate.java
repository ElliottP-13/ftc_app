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
        while(opModeIsActive()) {
            robot.rightFront.setPower(1);
            robot.leftFront.setPower(1);
            robot.rightBack.setPower(1);
            robot.leftBack.setPower(1);
        }
    }
}
