
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOpRed", group="Exercises") // Marks this as a TeleOp program
public class TeleOpRed extends LinearOpMode {
    // Declare the motors for the wheels
    DcMotor bl = null; // Back Left motor
    DcMotor fl = null; // Front Left motor
    DcMotor fr = null; // Front Right motor
    DcMotor br = null; // Back Right motor

    DcMotor it = null; // Intake

    DcMotor ot = null;
    DcMotor itf = null;

    //Servo flick;

    double integralSum = 0;
    double Kp = 0;
    double Ki = 0;
    double Kd = 0;

    double Kf = 0;

    ElapsedTime timer = new ElapsedTime();

    private double lastError = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize the wheel motors from the robot configuration
        bl = hardwareMap.get(DcMotor.class, "LB"); // Back Left motor (named "LB" in robot config)
        fl = hardwareMap.get(DcMotor.class, "LF"); // Front Left motor (named "LF" in robot config)
        fr = hardwareMap.get(DcMotor.class, "RF"); // Front Right motor (named "RF" in robot config)
        br = hardwareMap.get(DcMotor.class, "RB"); // Back Right motor (named "RB" in robot config)
        it = hardwareMap.get(DcMotor.class, "IT");
//        itb = hardwareMap.get(DcMotor.class, "ITB");
        ot = hardwareMap.get(DcMotor.class, "OT1");
        //flick = hardwareMap.get(Servo.class, "Flick");

        ot.setDirection(DcMotor.Direction.REVERSE);
        // Set the direction of each motor
        // Reverse the left motors to ensure correct movement direction
        br.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.FORWARD);
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        //((DcMotorEx) ot).setVelocityPIDFCoefficients(250, 0, 0, 12);



        // Display status on the driver station
        telemetry.addData("Mode", "waiting"); // Shows "waiting" until start button is pressed
        telemetry.update();

        // Wait for the start button to be pressed
        waitForStart();
        //ot1.setVelocityPIDFCoefficients(Kp, Ki, Kd, Kf);

        while (opModeIsActive()) {
            // Read the joystick values from gamepad 1
            double leftY = -gamepad1.left_stick_y; // Forward/Backward movement (inverted because joystick Y is negative up)
            double leftX = -gamepad1.left_stick_x;  // Strafing left/right movement
            double rightX = gamepad1.right_stick_x; // Rotation (turning left/right)

            // Calculate power for each motor using mecanum drive formulas
            double blPower = leftY + leftX + rightX; // Back Left motor power
            double flPower = leftY - leftX + rightX; // Front Left motor power
            double brPower = leftY - leftX - rightX; // Back Right motor power
            double frPower = leftY + leftX - rightX; // Front Right motor power
            double scale;

            if (gamepad1.right_bumper) {
                scale = 1;
            } else if (gamepad1.right_trigger > 0) {
                scale = 0.25;
            } else {
                scale = 0.75;
            }

            blPower *= scale;
            flPower *= scale;
            frPower *= scale;
            brPower *= scale;

            // Set the calculated power to each motor
            bl.setPower(blPower);
            fl.setPower(flPower);
            br.setPower(brPower);
            fr.setPower(frPower);


            if (gamepad2.left_trigger <= 0 && !gamepad2.dpad_right && !gamepad2.a && !gamepad2.x && !gamepad2.dpad_down && !gamepad2.dpad_left && gamepad2.right_trigger <= 0 && !gamepad2.dpad_up && !gamepad2.b){
                it.setPower(0);
//                itb.setPower(0);
                ot.setPower(0);
            }
            else{
                if (gamepad2.a) {
                    it.setPower(-.75);   // Intake runs while holding A

                }
                if (gamepad2.b){
                    it.setPower(.75);

                }
//                if(gamepad2.dpad_up){
//                    itf.setPower(-1);
//                }
//                if(gamepad2.dpad_down){
//                    itb.setPower(-1);
//                }
                if (gamepad2.dpad_right) {
                    //ot.setPower(.60);   // Outtake runs while holding B short
                    //double power = PIDControl(25,ot.getVelocity());
                    //((DcMotorEx) ot).setVelocity(200);
                    ot.setPower(.7);




                }

                if (gamepad2.xWasPressed()){
                    ot.setPower(.9);
                }

                if(gamepad2.dpad_left){
                    //ot1.setVelocity(1400);
                }
                if(gamepad2.b){
                    ot.setPower(-0.7);
                }


                if (gamepad2.left_trigger > 0) {
                    //remove


                    //remove
//                    itf.setPower(-1);
//                    itb.setPower(-1);
                    ot.setPower(.8);
                }
                if(gamepad2.right_trigger > 0){
//                    itf.setPower(-1);
//                    itb.setPower(-1);
                    ot.setPower(.8);
                }
            }
            // Display motor powers for debugging purposes
            telemetry.addData("BL Power", blPower); // Back Left power
            telemetry.addData("FL Power", flPower); // Front Left power
            telemetry.addData("BR Power", brPower); // Back Right power
            telemetry.addData("FR Power", frPower); // Front Right power


            //telemetry.addData("Velocity", ot1.setPower()); // Front Right power
            telemetry.update(); // Updates the telemetry on the driver station
        }


    }

    
//    public double PIDControl(double reference, double state){
//        double error = reference - state;
//        integralSum += error * timer.seconds();
//        double derivative = (error - lastError) / timer.seconds();
//        lastError = error;
//        timer.reset();
//        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki) + (reference * Kf);
//
//        return output;
//
//    }
}

/*
Robot Configuration Instructions:
1. Open the Robot Configuration on your FTC Driver Station.
2. Add four motors to your configuration:
   - Name them as follows:
     - Back Left motor: "LB"
     - Front Left motor: "LF"
     - Front Right motor: "RF"
     - Back Right motor: "RB"




     -
3. Set the motor directions:
   - Set "LB" and "LF" as REVERSE.
   - Set "RF" and "RB" as FORWARD.
4. Ensure all motors are properly connected to the REV Hub.

This code is designed to work out of the box with the specified motor setup.
*/

//NAMES:
// OT = OUTTAKE
// 0 on Expansion is nothing
// 1 is IT = Intake
// 2 is Back Left
// 3 is Forward Left