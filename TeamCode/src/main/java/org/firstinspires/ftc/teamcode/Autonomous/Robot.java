package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by pryor on 1/17/2018.
 */

public abstract class Robot extends LinearOpMode {
    HardwareMap map;

    OpMode mode;

    public VuforiaLocalizer vuforia;

    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFront = null;
    public DcMotor leftBack = null;

    public DcMotor rightFront = null;
    public DcMotor rightBack = null;

    public DcMotor arm1 = null;

    public DcMotor leftGrab = null;
    public DcMotor rightGrab = null;

    public DcMotor spindle = null;

    public Servo leftServo = null;
    public Servo rightServo = null;
    public Servo phone = null;

    public Servo twist = null;
    public Servo upDown = null;

    public ColorSensor colorSensor = null;
    public DistanceSensor distanceSensor = null;

    public BNO055IMU imu = null;
    public double initialHeading;


    public void initialize(HardwareMap map) {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        this.map = map;

        leftFront = map.get(DcMotor.class, "left front");
        leftBack = map.get(DcMotor.class, "left back");
        rightFront = map.get(DcMotor.class, "right front");
        rightBack = map.get(DcMotor.class, "right back");

        leftGrab = hardwareMap.get(DcMotor.class, "grabber left");
        rightGrab = hardwareMap.get(DcMotor.class, "grabber right");
        spindle = hardwareMap.get(DcMotor.class, "spindle");
        arm1 = map.get(DcMotor.class, "arm");

        //dropper = hardwareMap.get(Servo.class, "dropper");
        leftServo = map.get(Servo.class, "left hook");
        rightServo = map.get(Servo.class, "right hook");
        phone = map.get(Servo.class, "phone servo");

        twist = map.get(Servo.class, "arm twist");
        upDown = map.get(Servo.class, "arm up");

        colorSensor = map.get(ColorSensor.class, "sensor");
        distanceSensor = map.get(DistanceSensor.class, "sensor");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = map.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        //dropper.setPosition(0);

        leftServo.setPosition(0.32);
        rightServo.setPosition(0.79);
        phone.setPosition(0.5);

        twist.setPosition(0.69);
        upDown.setPosition(1);

        rightGrab.setDirection(DcMotor.Direction.REVERSE);


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", map.appContext.getPackageName());
        VuforiaLocalizer.Parameters vuforiaParams = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        vuforiaParams.vuforiaLicenseKey = "AT/9j9n/////AAAAGfut9qsmlkSAj/EuRSYOAYAN+mYb3Re80hY2qXsNPI8W7iZ3Ttg5BMsJgJ0HGyHVWoTGfG9ma3h58XKFj69bvy4IIjR4usiMTxfD335J3Zdy40RqeSz2NoFkRhtzZ3Es2rkCcGhcKQjAbphxvhi35GvAr/W3eOvbjwujiSQ5/yRIcTotiBWuwgQnEhbI0ZQBMTOssU9UAH5Dda2av9leohksx3GhNE/dvRJPXjS8398X7b9X9JADGSaSJp9qIt1Jnnu0kKSRSUoIFADk8Dv1j4VAIq0Sud9oZrUy3oqAVadJDqD0xhHQn8IjUp1+ju6zXKAl6uqXsi6xmeKjEkTpF5IvFsKt+z8Alx4d+zZ6osOs";
        vuforiaParams.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.createVuforiaLocalizer(vuforiaParams);

        resetHeading();


    }


    public void vuforiaTrack(double tX, double tZ, double rot) {
        boolean finished = false;
        double x = 0;
        double z = 0;

        double r = Math.hypot(tX - x, tZ - z);
        double robotAngle = Math.atan2(tZ - z, tX - x) - Math.PI / 4;

        double phonePos = phone.getPosition();

        double turn = getRelativeHeading(rot) / 180;//binds it between -1 and 1

        double lf = r * Math.cos(robotAngle) + turn;
        double rf = r * Math.sin(robotAngle) - turn;
        double lb = r * Math.sin(robotAngle) + turn;
        double rb = r * Math.cos(robotAngle) - turn;

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();

        relicTrackables.activate();

        while (pose == null && opModeIsActive()) {
            //do nothing!
            pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();

//            if(runtime.milliseconds() % 500 <= 3) {
//                phonePos -= 0.01;
//                phone.setPosition(phonePos);
//            }
        }

        x = pose.getTranslation().get(0) / 25.4;
        z = pose.getTranslation().get(2) / 25.4;

        Orientation rotation = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

//        rightFront.setPower(rf);
//        rightBack.setPower(rb);
//        leftFront.setPower(lf);
//        leftBack.setPower(lb);

        runtime.reset();

        double lastTime = runtime.milliseconds();
        double time;

        while (!finished && opModeIsActive()) {
            pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();

            r = Math.hypot(tX - x, tZ - z);
            robotAngle = Math.atan2(tX - x, tZ - z) - Math.PI / 4;

            turn = getRelativeHeading(rot) / 180;//binds it between -1 and 1

            lf = r * Math.cos(robotAngle) + turn;
            rf = r * Math.sin(robotAngle) - turn;
            lb = r * Math.sin(robotAngle) + turn;
            rb = r * Math.cos(robotAngle) - turn;


            if (pose != null) {

                rotation = Orientation.getOrientation(pose, AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);
                // Extract the X component of the offset of the target relative to the robot
                x = pose.getTranslation().get(0) / 25.4;
                z = pose.getTranslation().get(2) / 25.4;

                x+= .1;

                telemetry.addData("X: ", "%s", x);
            } else { //switch to trigk
//                x += (0.0461 * time) * Math.sin(robotAngle); //projects the change in distance if the target isn't visible.
//                z += (0.0461 * time) * Math.cos(robotAngle);
//
//                telemetry.addData("X", "%s visible", x);

                phonePos -= .00001;
            }

            //set servo position

            double servoPos = Math.atan(((Math.abs(x) - 0.5))/ (Math.abs(z) + 2.5)) / (3.14) + 0.5; //should return between 0 and 1
            servoPos -= rotation.secondAngle / Math.PI;
            //should divide by pi but not sensitive enough (doesn't move enough)
            int dir = (servoPos - phonePos) < 0 ? -1 : 1;
            //phonePos += .2;

            phone.setPosition(1 - servoPos);
            telemetry.addData("Servo Position", "%s", phonePos);
            telemetry.addData("Target Postition", "%s", servoPos);
            telemetry.addData("X , Z", "%s :X %s :Z", x, z);

            if (x >= tX - 1.53 && x <= tX && z >= tZ - 1.53 && z <= tZ) {//needs fixing
                //finished = true;
            }
            lastTime = runtime.milliseconds();

            telemetry.update();

//            rightFront.setPower(rf);
//            rightBack.setPower(rb);
//            leftFront.setPower(lf);
//            leftBack.setPower(lb);

            nap(50);
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);

    }

    public void vuforiaDrive(double in) {
        boolean finished = false;
        double x = 0;

        double dir = (in > 0) ? 1 : -1;
        double power = 0.7;

        double tX;

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();


        relicTrackables.activate();

        while (pose == null && opModeIsActive()){
            //do nothing!
            pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
        }

        x = pose.getTranslation().get(0)/25.4;
        tX = x + in;

        rightFront.setPower(power * dir);
        rightBack.setPower(power * dir);
        leftFront.setPower(power * dir);
        leftBack.setPower(power * dir);

        runtime.reset();

        double initialHeading = getRelativeHeading();

        double lastTime = runtime.milliseconds();
        double time;

        while (!finished && opModeIsActive()) {
            pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();

                telemetry.addData("Pose", format(pose));

                if (pose != null) {
                    // Extract the X component of the offset of the target relative to the robot
                    x = pose.getTranslation().get(0) / 25.4;
                    telemetry.addData("X", "%s visible", x);
                }
            } else {
                time = runtime.milliseconds() - lastTime;
                x += (0.0461 * time) * dir; //projects the change in distance if the target isn't visible.


            }

            if (x >= tX - 1.53 && x <= tX + 1.53) {
                finished = true;
            }

            lastTime = runtime.milliseconds();

            double servoPos = Math.atan(-x/10) / (3.14) + 0.5; //should return between 0 and 1
            //should divide by pi but not sensitive enough (doesn't move enough)
            phone.setPosition(servoPos);
            telemetry.addData("Target Postition", "%s", tX);
            telemetry.addData("Current X", "%s", x);

            telemetry.update();

            dir = (in - x > 0) ? 1 : -1;

            if ((getRelativeHeading() - initialHeading) * dir > 0) {
                rightFront.setPower(rightFront.getPower() - (.05 * dir));
                rightBack.setPower(rightBack.getPower() - (.05 * dir));
                leftFront.setPower(power * dir );
                leftBack.setPower(power * dir );
            } else if ((getRelativeHeading() - initialHeading) * dir < 0) {
                rightFront.setPower(power * dir);
                rightBack.setPower(power * dir);
                leftFront.setPower(leftFront.getPower() - (.05 * dir));
                leftBack.setPower(leftBack.getPower() - (.05 * dir));
            }




            nap(50);
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);

    }

    public void driveStraight(double in) {

        driveStraight(in, 0.7);
    }

    public void driveStraight(double in, double pow) {

        double dir = (in > 0) ? 1 : -1;
        double power = pow * dir;
        double timeToRun = (Math.abs(in) - 1.5293) / 0.0461;

        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        leftBack.setPower(power);

        runtime.reset();

        double initialHeading = getRelativeHeading();

        while (opModeIsActive() && runtime.milliseconds() < timeToRun) {
            updateTelemetry();

            if (getRelativeHeading() - initialHeading > 0) {
                rightFront.setPower(rightFront.getPower() - (.05 * dir));
                rightBack.setPower(rightBack.getPower() - (.05 * dir));
                leftFront.setPower(power);
                leftBack.setPower(power);
            } else if (getRelativeHeading() - initialHeading < 0) {
                rightFront.setPower(power);
                rightBack.setPower(power);
                leftFront.setPower(leftFront.getPower() - (.05 * dir));
                leftBack.setPower(leftBack.getPower() - (.05 * dir));
            }


        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);
    }

    public void driveTime(int milis) {

        double power = 0.7;


        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        leftBack.setPower(power);

        runtime.reset();

        while (opModeIsActive() && runtime.milliseconds() < milis) {
            //DO NOTHING!
            updateTelemetry();
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);


    }

    public void turnToDegree(double degree) {

        double power = (degree - getRelativeHeading() > 0) ? .5 : -.5;

        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(-power);
        leftBack.setPower(-power);


        //resetHeading();
        //double currentHeading = getAbsoluteHeading();
        runtime.reset();

        while (opModeIsActive() && ((getRelativeHeading() > degree + 5) || (getRelativeHeading() < degree - 5))) {
            //DO NOTHING!
            updateTelemetry();

            power = (degree - getRelativeHeading() > 0) ? .5 : -.5;

            rightFront.setPower(power);
            rightBack.setPower(power);
            leftFront.setPower(-power);
            leftBack.setPower(-power);
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);

        nap(100);

        if ((getRelativeHeading() > degree + 5) || (getRelativeHeading() < degree - 5)) {
            turnToDegree(degree);
        }


    }

    public void spinToDegree(double degree) {

        double dir = (degree - getRelativeHeading() > 0) ? 1 : -1;

        rightFront.setPower(.292893 * dir);//1.70711
        rightBack.setPower(1.70711 * dir);//.29
        leftFront.setPower(-.292893 * dir);//1.7
        leftBack.setPower(-1.70711 * dir);//.292893


        //resetHeading();
        //double currentHeading = getAbsoluteHeading();
        runtime.reset();

        while (opModeIsActive() && ((getRelativeHeading() > degree + 5) || (getRelativeHeading() < degree - 5))) {
            //DO NOTHING!
            updateTelemetry();
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);

        nap(100);

        if ((getRelativeHeading() > degree + 5) || (getRelativeHeading() < degree - 5)) {
            turnToDegree(degree);
        }


    }

    public void turn(double degree) {

        double power = (degree > 0) ? .5 : -.5;

        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(-power);
        leftBack.setPower(-power);


        //resetHeading();
        double currentHeading = getAbsoluteHeading();
        runtime.reset();

        while (opModeIsActive() && Math.abs(getRelativeHeading(currentHeading)) < Math.abs(degree)) {
            //DO NOTHING!
            updateTelemetry();
        }

        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);


    }

    public void resetHeading() {
        initialHeading = getAbsoluteHeading();
    }

    public double getAbsoluteHeading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public double getRelativeHeading() {
        return AngleUnit.DEGREES.normalize(getAbsoluteHeading() - initialHeading);
    }

    public double getRelativeHeading(double heading) {
        return AngleUnit.DEGREES.normalize(getAbsoluteHeading() - heading);
    }

    public void nap(double time) {
        double startime = runtime.milliseconds();
        while (opModeIsActive() && (runtime.milliseconds() - startime) < time) {
            updateTelemetry();
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    private void updateTelemetry() {
//        telemetry.addData("Heading", "Angle = %.2f", getRelativeHeading());
//        telemetry.update();
    }

    public void goToPosition(Servo servo, double pos){
        servo.setPosition(pos);
        while(servo.getPosition() != pos){
            telemetry.addData("Servo Pos: ", "%s", servo.getPosition());
            updateTelemetry();
        }
    }
}
