package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.pedro.PedroCommands.hold;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.qualcomm.robotcore.hardware.DcMotorEx;


@Autonomous(name = "ShootRun")
public class ShootRun extends OpMode {
    DcMotor bl = null; // Back Left motor
    DcMotor fl = null; // Front Left motor
    DcMotor fr = null; // Front Right motor
    DcMotor br = null; // Back Right motor
    DcMotor it = null; // Intake
    Servo flick = null;
    Servo flower = null;
    DcMotor ot = null;

    double Kp = 20;
    double Ki = 0;
    double Kd = 0;

    double Kf = 0;
    private Follower follower;

    // our poses
    private final PoseFactory poseFactory = PoseFactory.degrees();
    //    Pose start = poseFactory.of(26.389755113572896, 10.9, 90); // 180 degrees
//    Pose end = poseFactory.of(10, 10, 90); // 90 degrees
//    Pose control = poseFactory.of(10, 20, 45); // 45 degrees
    private final Pose startPose = poseFactory.of(60, 5, 90);
    private final Pose shootPose = poseFactory.of(60, 20, 90);
    private final Pose pointPose = poseFactory.of(25, 34, 90);

    private final Pose tPose = poseFactory.of(10, 105, 90);
    private final Pose parkPose = poseFactory.of(8, 110, 90);
    private Pose target;

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();


    }
    @Override
    public void start() {
        bl = hardwareMap.get(DcMotor.class, "LB"); // Back Left motor (named "LB" in robot config)
        fl = hardwareMap.get(DcMotor.class, "LF"); // Front Left motor (named "LF" in robot config)
        fr = hardwareMap.get(DcMotor.class, "RF"); // Front Right motor (named "RF" in robot config)
        br = hardwareMap.get(DcMotor.class, "RB"); // Back Right motor (named "RB" in robot config)
        it = hardwareMap.get(DcMotor.class, "IT");
//        itb = hardwareMap.get(DcMotor.class, "ITB");
        ot = hardwareMap.get(DcMotor.class, "OT1");
        //flick = hardwareMap.get(Servo.class, "Flick");
        flower = hardwareMap.get(Servo .class, "FLOWER");
        flick = hardwareMap.get(Servo.class, "FLICK");
        it = hardwareMap.get(DcMotor.class, "IT");
        it.setDirection(DcMotor.Direction.REVERSE);
        ot.setDirection(DcMotor.Direction.REVERSE);
        // Set the direction of each motor
        // Reverse the left motors to ensure correct movement direction
        br.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.FORWARD);
        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);
        it.setDirection(DcMotor.Direction.REVERSE);
        ((DcMotorEx) ot).setVelocityPIDFCoefficients(Kp, Ki, Kd, Kf);
        schedule(autoRoutine());
        flick.setPosition(.45);


        // using the same park path from the last page
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading",Math.toDegrees(follower.pose().heading()));

        // We will add these two, but you can use the many other follower methods aswell in your own code!

        telemetry.update();
    }
    private Path park() {
        return line(tPose, parkPose).linear(tPose, parkPose);
    }
    private Path startToScore() {
        return line(startPose, shootPose).linear(startPose, shootPose);
    }
    private Path shootToPoint() {
        return line(shootPose, pointPose).linear(shootPose, pointPose);
    }
    private Path pointToT() {
        return line(pointPose, tPose).linear(pointPose, tPose);
    }

    private Command autoRoutine() {

        return sequential(
                instant(() -> ((DcMotorEx) ot).setVelocity(1980)),
                follow(follower, startToScore().with(Constants.foresightConfig.maxPathSpeed.at(0.3))),
//                parallel(
//                        follow(follower, startToScore().with(Constants.foresightConfig.maxPathSpeed.at(0.5))),
//                        infinite(() -> it.setPower(-.85))
//                                .until(() -> !follower.isBusy())
//
//                ),

                instant(() -> it.setPower(-.85)),
                waitMs(2000),
                instant(() -> flick.setPosition(.9)),
                waitMs(1000),
                instant(() -> it.setPower(0)),

                //instant(() -> flick.setPosition(.45)),

                //instant(() -> follower.setHeading(270))
                follow(follower, shootToPoint().with(Constants.foresightConfig.maxPathSpeed.at(0.5))),
                follow(follower, pointToT().with(Constants.foresightConfig.maxPathSpeed.at(0.5)))
//


                //instant(() -> it.setPower(.75)),
                //follow(follower, pointToT())
//                follow(follower, park())
                //instant(() -> it.setPower(0))
                // Add mechanism commands here.




        );
    }
    private Command intakeAtHalfWay() {
        return sequential(
                //waitUntil(() -> follower.completion() >= 0.5),
                instant(() -> it.setPower(.75))
        );
    }
    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }





}