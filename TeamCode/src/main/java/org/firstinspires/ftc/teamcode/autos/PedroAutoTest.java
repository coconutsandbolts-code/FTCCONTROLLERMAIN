package org.firstinspires.ftc.teamcode.autos;
import com.pedropathing.api.Paths;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.paths.Path;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "pedroAutoTest")
public class PedroAutoTest extends OpMode {
    private Follower follower;

    // our poses
    private final PoseFactory poseFactory = PoseFactory.degrees();
//    Pose start = poseFactory.of(26.389755113572896, 10.9, 90); // 180 degrees
//    Pose end = poseFactory.of(10, 10, 90); // 90 degrees
//    Pose control = poseFactory.of(10, 20, 45); // 45 degrees
    private final Pose startPose = poseFactory.of(0, 0, 90);
    private final Pose scorePose = poseFactory.of(80, 0, 90);
    private final Pose ballPose = poseFactory.of(80, 70, 90);

    private final Pose parkPose = poseFactory.of(0, 0, 90);

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

    }
    @Override
    public void start() {
        schedule(autoRoutine());

        // using the same park path from the last page
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        // We will add these two, but you can use the many other follower methods aswell in your own code!
        telemetry.addData("Path completion", follower.completion());
        telemetry.addData("Distance remaining in path", follower.remainingDistance());
        telemetry.update();
    }
    private Path park() {
        return line(ballPose, parkPose).linear(ballPose, parkPose);
    }
    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }
    private Path scoreToBall() {
        return line(scorePose, ballPose).linear(scorePose, ballPose);
    }


    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),
                follow(follower, scoreToBall()),
                // Add mechanism commands here.
                follow(follower, park())



        );
    }
    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }





}