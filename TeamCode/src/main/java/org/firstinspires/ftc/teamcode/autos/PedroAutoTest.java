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
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
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
    DcMotor it = null; // Intake
    // our poses
    private final PoseFactory poseFactory = PoseFactory.degrees();
//    Pose start = poseFactory.of(26.389755113572896, 10.9, 90); // 180 degrees
//    Pose end = poseFactory.of(10, 10, 90); // 90 degrees
//    Pose control = poseFactory.of(10, 20, 45); // 45 degrees
    private final Pose startPose = poseFactory.of(55, 0, 90);
    private final Pose shootPose = poseFactory.of(55, 15, 90);
    private final Pose pointPose = poseFactory.of(109, 15, 0);

    private final Pose tPose = poseFactory.of(109, 110, 90);
    private final Pose parkPose = poseFactory.of(8, 110, 90);

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

    }
    @Override
    public void start() {
        it = hardwareMap.get(DcMotor.class, "IT");
        it.setDirection(DcMotor.Direction.REVERSE);
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
        return line(tPose  , parkPose).linear(tPose, parkPose);
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
                follow(follower, startToScore()),
                follow(follower, shootToPoint()),
                instant(() -> it.setPower(.75)),
                follow(follower, pointToT()),
                follow(follower, park())
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