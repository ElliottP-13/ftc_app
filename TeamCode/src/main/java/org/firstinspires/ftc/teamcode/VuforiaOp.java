package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Elliott on 2/5/2017.
 */

public class VuforiaOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AT/9j9n/////AAAAGfut9qsmlkSAj/EuRSYOAYAN+mYb3Re80hY2qXsNPI8W7iZ3Ttg5BMsJgJ0HGyHVWoTGfG9ma3h58XKFj69bvy4IIjR4usiMTxfD335J3Zdy40RqeSz2NoFkRhtzZ3Es2rkCcGhcKQjAbphxvhi35GvAr/W3eOvbjwujiSQ5/yRIcTotiBWuwgQnEhbI0ZQBMTOssU9UAH5Dda2av9leohksx3GhNE/dvRJPXjS8398X7b9X9JADGSaSJp9qIt1Jnnu0kKSRSUoIFADk8Dv1j4VAIq0Sud9oZrUy3oqAVadJDqD0xhHQn8IjUp1+ju6zXKAl6uqXsi6xmeKjEkTpF5IvFsKt+z8Alx4d+zZ6osOs";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;//displays axes when sees target

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);//makes it track 4 images at one time

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");//gets the beacons out of the FTC_2016-17 xml file
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        waitForStart();

        beacons.activate();//starts tracking
        while(opModeIsActive()){
            for(VuforiaTrackable beac : beacons){
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if(pose != null){//pose would be null if the object isn't found
                    VectorF translation = pose.getTranslation();//makes it so it doesn't return 0 for odd reason
                    telemetry.addData(beac.getName() + "-Translation", translation);
                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));//if vertical do .get(1), .get(2) (y, z)
                    //if horizontal do .get(0) .get(2) (x, z)
                    telemetry.addData(beac.getName() + " Degrees", degreesToTurn);

                }
            }
            telemetry.update();
        }
    }
}
