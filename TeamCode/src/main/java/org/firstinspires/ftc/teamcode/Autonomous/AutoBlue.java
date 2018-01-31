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
import org.firstinspires.ftc.robotcore.external.Telemetry;
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
public class AutoBlue extends Robot {
    @Override
    public void runOpMode() throws InterruptedException {

        initialize(hardwareMap);

        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        


        waitForStart();

        relicTrackables.activate();

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        twist.setPosition(0.55);
        goToPosition(upDown, 0.70);

        goToPosition(twist, 0.64);
        nap(500);
        goToPosition(upDown, 0.28);

        nap(2000);


        int threshhold = 10;


        telemetry.addData("Blue ", "%s", colorSensor.blue());
        telemetry.addData("Red", "%s", colorSensor.red());
        telemetry.update();

        if (colorSensor.blue()-colorSensor.red() > threshhold){
            //we see blue ball hit the other one
            goToPosition(twist, 0.30);

        } else if (colorSensor.red() - colorSensor.blue() > threshhold){
            //we see the red ball hit it!
            goToPosition(twist, 0.90);
        }

        goToPosition(upDown, 0.40);

        upDown.setPosition(1);
        twist.setPosition(0.69);

        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */


        boolean loop = true;

        while (loop && opModeIsActive()){
            vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if(vuMark != RelicRecoveryVuMark.UNKNOWN) {
                loop = false;
            }
        }


        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            telemetry.addLine("IN IF STATEMENT");
            telemetry.update();

            if(vuMark == RelicRecoveryVuMark.CENTER){
                //RUN CENTER CODE
                telemetry.addLine("Center");
                telemetry.update();
                driveStraight(27.8);

            } else if(vuMark == RelicRecoveryVuMark.LEFT) {
                //RUN LEFT CODE
                telemetry.addLine("Left");
                telemetry.update();
                driveStraight(15.76);
            }
            else if(vuMark == RelicRecoveryVuMark.RIGHT){
                //RUN RIGHT CODE
                telemetry.addLine("Right");
                telemetry.update();
                driveStraight(44.8);
            }


            nap(250);
            turnToDegree(60);
            driveStraight(33.5);
            spinToDegree(90);
            driveStraight(10);
            nap(250);

            leftServo.setPosition(0);
            rightServo.setPosition(1);

            for(int i = 0; i < 2; i++){
                driveStraight(-8);

                driveStraight(20, 0.5);
            }
            
            driveStraight(-20);

            turnToDegree(-90);
            driveStraight(14);

            spindle.setPower(-1);
            nap(900);
            spindle.setPower(0);

            leftGrab.setPower(-1);
            rightGrab.setPower(-1);
            driveStraight(12);
            
            int dir = 1;
            
            runtime.reset();
            while (runtime.milliseconds() < 3000){
                rightFront.setPower(.5 * dir);
                leftFront.setPower(.5 * dir);
                rightBack.setPower(.5 * dir);
                leftBack.setPower(.5 * dir);

                if(runtime.milliseconds() % 600 <= 2) {
                    dir *= -1;
                }
            }

            rightFront.setPower(.2);
            leftFront.setPower(.2);
            rightBack.setPower(.2);
            leftBack.setPower(.2);
            
            leftServo.setPosition(0.32);
            rightServo.setPosition(0.79);
            
            nap(700);

            rightFront.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            leftBack.setPower(0);
            
            leftGrab.setPower(0);
            rightGrab.setPower(0);



                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
            telemetry.addData("VuMark", "%s visible", vuMark);

        } else {
            telemetry.addData("VuMark", "not visible");
        }

        telemetry.update();

    }

}
