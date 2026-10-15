package org.firstinspires.ftc.teamcode.pedro;
import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("LF");
        c.frontRightName.set("RF");
        c.backLeftName.set("LB");
        c.backRightName.set("RB");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(2.2183994984063578);
        c.yPodOffset.set(6.216288889486959);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.16604053557502557);
                Controller secondaryTranslationalForward = Controller.proportional(0.06134754121014978);
                Controller primaryTranslationalLateral = Controller.proportional(0.22246702968143908);
                Controller secondaryTranslationalLateral = Controller.proportional(0.08219562303877855);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01983502628771377));
                c.brake.set(Controller.proportionalFeedforward(0.016859772344556705));

                c.headingFeedback.set(Controller.proportional(3.21133214897101));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.052349834828530596, 0.0057477733496355));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0846324816764366, 0.06530339201922461));
                c.quadraticBrakeCoefficients.set(Matrix.diag(8.227951295309502E-4, 0.0017144998529466612));

                c.maxAchievableForwardVelocity.set(50.76735914755478);
                c.maxAchievableStrafeVelocity.set(43.38597478320215);
                c.naturalForwardDeceleration.set(71.67152533892374);
                c.naturalStrafeDeceleration.set(81.62228782113107);
            }
    );


}