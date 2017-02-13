package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

/**
 * Created by epryor on 1/30/2017.
 */
@Autonomous(name = "Auto Read", group = "Linear Opmode")
public class AutoRead extends LinearOpMode {

    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor leftBack;

    @Override
    public void runOpMode() {
        ReadWrite r = new ReadWrite();
        ArrayList<Double> rightPower = parsePower(r.readNstoreArrayList("RightSide.txt", " , "));
        ArrayList<Double> leftPower = parsePower(r.readNstoreArrayList("LeftSide.txt", " , "));
        initalize();
        waitForStart();
        boolean run = true;
        int i = 0;
        double setRightPower = 0;
        double setLeftPower = 0;

        telemetry.addLine("Lenght " + rightPower.size());
        telemetry.update();
        sleep(2000);

        while (opModeIsActive() && run){

            if(i < rightPower.size() && i < leftPower.size()) {
                setRightPower = rightPower.get(i);
                setLeftPower = leftPower.get(i);
            }
            else {
                setRightPower = 0;
                setLeftPower = 0;
                run = false;
            }
            rightFront.setPower(setRightPower);
            rightBack.setPower(setRightPower);
            leftFront.setPower(setLeftPower);
            leftBack.setPower(setLeftPower);

            i++;
            sleep(10);//sleep so it is the same timing as the writer
        }
        telemetry.addLine("ALL DONE");
        telemetry.update();
        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);

    }
    private ArrayList<Double> parsePower(ArrayList<String> list){
        ArrayList<Double> ret = new ArrayList<Double>();
        if(list.get(0) != null) {
            for (int i = 0; i < list.size(); i++) {
                double d = Double.parseDouble(list.get(i));
                ret.add(d);
            }
            return ret;
        }
        return ret;
    }
    private void initalize(){
        rightFront = hardwareMap.dcMotor.get("right front");
        rightBack = hardwareMap.dcMotor.get("right back");
        leftFront = hardwareMap.dcMotor.get("left front");
        leftBack = hardwareMap.dcMotor.get("left back");
    }
}
