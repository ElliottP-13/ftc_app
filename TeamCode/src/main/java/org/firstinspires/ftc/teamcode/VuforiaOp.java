package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Elliott on 2/5/2017.
 */
@TeleOp(name = "Vuforia", group = "Linear Opmode")
public class VuforiaOp extends LinearOpMode {

    float mmPerInch = 25.4f;
    float mmBotWidth = 18 * mmPerInch;            // ... or whatever is right for your robot
    float mmFTCFieldWidth = (12 * 12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels
    OpenGLMatrix lastLocation = null;

    OpenGLMatrix redTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the RED WALL. Our translation here
                is a negative translation in X.*/
            .translation(-mmFTCFieldWidth / 2, 0, 0)
            .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X, then 90 in Z */
                    AxesReference.EXTRINSIC, AxesOrder.XZX,
                    AngleUnit.DEGREES, 90, 90, 0));

    OpenGLMatrix blueTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the Blue Audience wall.
                Our translation here is a positive translation in Y.*/
            .translation(0, mmFTCFieldWidth / 2, 0)
            .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                    AxesReference.EXTRINSIC, AxesOrder.XZX,
                    AngleUnit.DEGREES, 90, 0, 0));

    OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
            .translation(mmBotWidth / 2, 0, 0)
            .multiplied(Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC, AxesOrder.YZY,
                    AngleUnit.DEGREES, -90, 0, 0));

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

        beacons.get(3).setLocation(blueTargetLocationOnField);
        for (VuforiaTrackable beac : beacons) {
            ((VuforiaTrackableDefaultListener) beac.getListener()).setPhoneInformation(phoneLocationOnRobot, params.cameraDirection);
        }

        waitForStart();

        beacons.activate();//starts tracking
        while (opModeIsActive()) {
            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();
                VuforiaTrackableDefaultListener beacon = (VuforiaTrackableDefaultListener) beac.getListener();
                if (pose != null) {//pose would be null if the object isn't found
                    VectorF translation = pose.getTranslation();//makes it so it doesn't return 0 for odd reason

                    telemetry.addData(beac.getName() + "-Translation", translation);
                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));//if vertical do .get(1), .get(2) (y, z)
                    //if horizontal do .get(0) .get(2) (x, z)
                    telemetry.addData(beac.getName() + "1: ", translation.get(0));
                    telemetry.addData(beac.getName() + "2: ", translation.get(1));
                    telemetry.addData(beac.getName() + "3: ", translation.get(2));
                    telemetry.addData(beac.getName() + " Degrees", degreesToTurn);

                }
                VectorF angle = anglesFromTarget(beacon);
                VectorF trans = navOffWall(beacon.getPose().getTranslation(), //beacon target
                        Math.toDegrees(angle.get(0)) - 90, //X angle minus 90 because youtuber said to
                        new VectorF(500, 0, 0)); //target postition off the wall => want to be 50 cm from wall, no rotation or offset
                //if want offset, change last param (z) to whatever and it will be offset

                if(trans.get(0) > 0) {//turn right
                }else {//turn left
                }

                do{
                    if(beacon.getPose() != null){//we can see it
                        //update amount to turn
                        trans = navOffWall(beacon.getPose().getTranslation(), Math.toDegrees(angle.get(0)) - 90, new VectorF(500, 0, 0));
                    }
                    idle();//so it doesn't loop too fast
                }while (opModeIsActive() && Math.abs(trans.get(0)) > 30);

                //stop the robot

                //use encoders to drive forward until in front of beac
                //motor.setCurrentPosition((int) motor.getCurPos() + (Math.hypot(trans.get(0), trans.get(2) + 150) / wheelCircum * tickPerInch)));//add 150 because center of turn might be off

                //run while op active, we can't see it, or we aren't straight (ish) to wall
                while(opModeIsActive() && (beacon.getPose() == null || beacon.getPose().getTranslation().get(0) > 10)){
                    if(beacon.getPose() != null){
                        //turn
                        if(beacon.getPose().getTranslation().get(0) > 0){
                            //turn left
                        }else {
                            //turn right
                        }
                    }
                }

                //stop robot

            }
            /**
             * Provide feedback as to where the robot was last located (if we know).
             */

            telemetry.update();
        }
    }

    public VectorF navOffWall(VectorF trans, double robotAngle, VectorF offWall) {
        return new VectorF((float) (trans.get(0) - offWall.get(0) * Math.sin(Math.toRadians(robotAngle)) - offWall.get(2) * Math.cos(Math.toRadians(robotAngle))), trans.get(1), (float) (trans.get(2) + offWall.get(0) * Math.cos(Math.toRadians(robotAngle)) - offWall.get(2) * Math.sin(Math.toRadians(robotAngle))));
    }

    public VectorF anglesFromTarget(VuforiaTrackableDefaultListener image) {
        float[] data = image.getRawPose().getData();
        float[][] rotation = {{data[0], data[1]}, {data[4], data[5], data[6]}, {data[8], data[9], data[10]}};
        double thetaX = Math.atan2(rotation[2][1], rotation[2][2]);
        double thetaY = Math.atan2(-rotation[2][0], Math.sqrt(rotation[2][1] * rotation[2][1] + rotation[2][2] * rotation[2][2]));
        double thetaZ = Math.atan2(rotation[1][0], rotation[0][0]);
        return new VectorF((float) thetaX, (float) thetaY, (float) thetaZ);
    }
}
