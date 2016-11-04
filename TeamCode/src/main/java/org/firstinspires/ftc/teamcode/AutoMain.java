package org.firstinspires.ftc.teamcode;

/**
 * Created by Elliott on 10/31/2016.
 */
public class AutoMain extends AutoBase{
    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        driveStraight(60, 6);
        turn(-90, 10);
        driveStraight(60, 6);
        //hit button
        driveStraight(-12, 6);
        turn(-90, 30);
        driveStraight(48, 6);
        turn(90, 30);
        driveStraight(12, 6);//maybe not do this, might accidentally hit button
        //scan hit button
    }

}
