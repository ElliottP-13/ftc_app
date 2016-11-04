/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p/>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p/>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Auto Test", group = "Linear Opmode")
//@Disabled
public class AutoTest extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "blue", new String[]{"Blue", "Red"});
    AutonomousTextOption startPos = new AutonomousTextOption("Start Position", "Middle", new String[]{"Mountain", "Middle", "Corner"});
    AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousBooleanOption pressBootin = new AutonomousBooleanOption("Bootin Press", true);
    AutonomousIntOption waitButton = new AutonomousIntOption("Wait at Button", 0, 0, 20);
    AutonomousBooleanOption blockButton = new AutonomousBooleanOption("Block other Button", false);
    AutonomousTextOption mountain = new AutonomousTextOption("Mountain", allianceColor.getValue(), new String[]{"Blue", "Red", "Far Blue", "Far Red"});
    AutonomousBooleanOption mountainClimbers = new AutonomousBooleanOption("Mountain Climbers", true);

    AutonomousOption[] autoOptions = {allianceColor, startPos, waitStart, pressBootin, waitButton, blockButton, mountain, mountainClimbers};
    int currentOption = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.addLine("Initing");
        telemetry.update();

        selectOptions();
        if (allianceColor.getValue().equals("Red")) {
            //directionadjustment = -1.0;
        }
        waitForStart();
        runtime.reset();

        print("Hello, running now");
        sleep(1000);
        print("I waited 1000 milis");
        sleep(1000);
        print("now I am done!");
    }

    public void selectOptions() {
        boolean aPressed = false;
        boolean yPressed = false;
        boolean bPressed = false;
        boolean xPressed = false;
        while (currentOption < autoOptions.length && !opModeIsActive()) {
            showOptions();
            if (gamepad1.a && !aPressed) {
                currentOption = currentOption + 1;
                aPressed = true;
            } else {
                aPressed = gamepad1.a;
            }
            if (gamepad1.y && !yPressed) {
                currentOption = currentOption - 1;
                yPressed = true;
            } else {
                yPressed = gamepad1.y;
            }
            if (gamepad1.b && !bPressed) {
                autoOptions[currentOption].nextValue();
                bPressed = true;
            } else {
                bPressed = gamepad1.b;
            }
            if (gamepad1.x && !xPressed) {
                autoOptions[currentOption].previousValue();
                xPressed = true;
            } else {
                xPressed = gamepad1.x;
            }
        }
    }

    private void showOptions() {
        String str = "";
        switch (autoOptions[currentOption].optionType) {
            case STRING:
                str = ((AutonomousTextOption) autoOptions[currentOption]).getValue();
                break;
            case INT:
                str = Integer.toString(((AutonomousIntOption) autoOptions[currentOption]).getValue());
                break;
            case BOOLEAN:
                str = String.valueOf(((AutonomousBooleanOption) autoOptions[currentOption]).getValue());
                break;
        }
        telemetry.addLine("Current Number: " + currentOption);
        telemetry.addLine("Current Option: " + str);
        telemetry.update();
    }

    private void showOptions(String additonalInfo) {
        telemetry.addLine(additonalInfo);
        showOptions();
    }

    private void print(String s) {
        telemetry.addLine(s);
        telemetry.update();
    }
}
