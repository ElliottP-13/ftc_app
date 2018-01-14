package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by pryor on 1/11/2018.
 */
@Autonomous(name = "Blue Autonomous", group = "Autonomous")
public class AutoBlue extends LinearOpMode {

    VuforiaLocalizer vuforia;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    private DcMotor rightFront = null;
    private DcMotor rightBack = null;

    private DcMotor arm1 = null;
    private DcMotor arm2 = null;

    private Servo leftServo = null;
    private Servo rightServo = null;

    private ColorSensor colorSensor = null;
    private DistanceSensor distanceSensor = null;

    private double msPerCm = 11;//just a guess we will refine later
    //msPerCm log
    //11 = 11.8V

    private double msPerDeg = 300; //just a guess we will refine later

    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
        parameters.vuforiaLicenseKey = "AT/9j9n/////AAAAGfut9qsmlkSAj/EuRSYOAYAN+mYb3Re80hY2qXsNPI8W7iZ3Ttg5BMsJgJ0HGyHVWoTGfG9ma3h58XKFj69bvy4IIjR4usiMTxfD335J3Zdy40RqeSz2NoFkRhtzZ3Es2rkCcGhcKQjAbphxvhi35GvAr/W3eOvbjwujiSQ5/yRIcTotiBWuwgQnEhbI0ZQBMTOssU9UAH5Dda2av9leohksx3GhNE/dvRJPXjS8398X7b9X9JADGSaSJp9qIt1Jnnu0kKSRSUoIFADk8Dv1j4VAIq0Sud9oZrUy3oqAVadJDqD0xhHQn8IjUp1+ju6zXKAl6uqXsi6xmeKjEkTpF5IvFsKt+z8Alx4d+zZ6osOs";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        relicTrackables.activate();

        int threshhold = 10;

        if (colorSensor.blue() > threshhold && colorSensor.red() < threshhold){
            //we see blue ball hit the other one

        } else if (colorSensor.blue() < threshhold && colorSensor.red() > threshhold){
            //we see the red ball hit it!
        }



        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            if(vuMark == RelicRecoveryVuMark.CENTER){
                //RUN CENTER CODE
            } else if(vuMark == RelicRecoveryVuMark.LEFT) {
                //RUN LEFT CODE
            }
            else if(vuMark == RelicRecoveryVuMark.RIGHT){
                //RUN RIGHT CODE

            }


                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
            telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();

            //telemetry.addData("Pose", format(pose));

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
            if (pose != null) {
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                // Extract the X, Y, and Z components of the offset of the target relative to the robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);

                // Extract the rotational components of the target relative to the robot
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;

                telemetry.addLine("X: " + tX + " Y: " + tY + " Z: " + tZ);
                telemetry.addLine("rX: " + rX + " rY: " + rY + " rZ: " + rZ);
            }

        } else {
            telemetry.addData("VuMark", "not visible");
        }

        telemetry.update();

    }

    private void driveStraight(double cm) {

        double power = (cm > 0) ? 1 : -1;
        double timeToRun = cm * msPerCm;

        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        leftBack.setPower(power);

        runtime.reset();

        while (opModeIsActive() && runtime.milliseconds() < timeToRun) {
            //DO NOTHING!
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);


    }

    private void turn(double degree) {

        double power = (degree > 0) ? .7 : -.7;
        double timeToRun = degree * msPerDeg;

        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(-power);
        leftBack.setPower(-power);

        runtime.reset();

        while (opModeIsActive() && runtime.milliseconds() < timeToRun) {
            //DO NOTHING!
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);


    }

    private void initialize() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        leftFront = hardwareMap.get(DcMotor.class, "left front");
        leftBack = hardwareMap.get(DcMotor.class, "left back");
        rightFront = hardwareMap.get(DcMotor.class, "right front");
        rightBack = hardwareMap.get(DcMotor.class, "right back");

        arm1 = hardwareMap.get(DcMotor.class, "arm");
        arm2 = hardwareMap.get(DcMotor.class, "arm 2");
        //dropper = hardwareMap.get(Servo.class, "dropper");
        leftServo = hardwareMap.get(Servo.class, "left hook");
        rightServo = hardwareMap.get(Servo.class, "right hook");

        colorSensor = hardwareMap.get(ColorSensor.class, "sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "sensor");

        //dropper.setPosition(0);

        leftServo.setPosition(0.52);
        rightServo.setPosition(0.59);

        arm2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

    }

}
