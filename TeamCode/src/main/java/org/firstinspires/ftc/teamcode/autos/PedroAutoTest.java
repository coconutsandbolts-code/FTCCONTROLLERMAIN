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
@Autonomous
public class PedroAutoTest extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    // our poses
    PoseFactory poseFactory = PoseFactory.degrees();

    Pose start = poseFactory.of(0, 0, 180); // 180 degrees
    Pose end = poseFactory.of(10, 10, 90); // 90 degrees
    Pose control = poseFactory.of(10, 20, 45); // 45 degrees
    private final Pose startPose = p.of(24, 24, 0);
    private final Pose park = p.of(48, 48, 90);
    private final Pose scorePose = p.of(48, 48, 90);
    private final Pose parkPose = p.of(72, 48, 90);
    private final Pose controlPose = p.of(36, 60, 45);
    Path line = Paths.line(start, end);

    Path curve = Paths.curve(start, control, end);

    Path path = Paths.path(line, curve);
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
        return line(startPose, parkPose).linear(startPose, parkPose);
    }
    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),
                // Add mechanism commands here.
                follow(follower, park())
        );
    }
    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
    public Path linePath() {
        return Paths.line(start, end);
    }

    public Path curvePath() {
        return Paths.curve(start, control, end);
    }

    public Path compoundPath() {
        return Paths.path(linePath(), curvePath());
    }



}