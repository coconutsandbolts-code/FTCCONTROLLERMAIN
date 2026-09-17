package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "AutoRedWall", group = "Exercises")
public class AutoRedWall extends LinearOpMode {

    DcMotor bl, fl, fr, br;

    static final double NORMAL_SPEED = 0.5;
    static final double DOUBLE_SPEED = 1;
    @Override
    public void runOpMode() throws InterruptedException {

        bl = hardwareMap.get(DcMotor.class, "LB");
        fl = hardwareMap.get(DcMotor.class, "LF");
        fr = hardwareMap.get(DcMotor.class, "RF");
        br = hardwareMap.get(DcMotor.class, "RB");




        double Kp = 0;
        double Ki = 0;
        double Kd = 0;

        double Kf = 15.4;


        //((DcMotorEx)ot).setVelocityPIDFCoefficients(Kp, Ki, Kd, Kf);

        br.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.FORWARD);
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        if (opModeIsActive()) {

            //flick.setPosition(1.2);


            moveForward(NORMAL_SPEED,900);
            strafeRight(DOUBLE_SPEED, 900);
            strafeLeft(DOUBLE_SPEED, 900);

            //right gear intake on right side of the forward line
        }
        // example:
        //        moveForward(NORMAL_SPEED, 1000);
        //        stopDrive();
        //        sleep(250);
        //
        //        strafeRight(NORMAL_SPEED, 800);
        //        stopDrive();
        //        sleep(250);
        //
        //        turnRight(NORMAL_SPEED, 600);
        //        stopDrive();
        //        sleep(250);
        //
        //        intakeIn(1.0, 800);
        //        outtakeOut(1.0, 800);
    }

    public void moveForward(double speed, long time) throws InterruptedException {
        driveTimed(speed, speed, speed, speed, time);
    }

    public void strafeRight(double speed, long time) throws InterruptedException {
        driveTimed(speed, -speed, -speed, speed, time);
    }
    public void strafeLeft(double speed, long time) throws InterruptedException {
        driveTimed(-speed, speed, speed, -speed, time);
    }

    public void turnRight(double speed, long time) throws InterruptedException {
        driveTimed(speed, speed, -speed, -speed, time);
    }

    public void driveTimed(double blP, double flP, double frP, double brP, long time) throws InterruptedException {
        bl.setPower(blP);
        fl.setPower(flP);
        fr.setPower(frP);
        br.setPower(brP);
        sleep(time);
    }





    public void stopDrive() {
        bl.setPower(0);
        fl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }
}
