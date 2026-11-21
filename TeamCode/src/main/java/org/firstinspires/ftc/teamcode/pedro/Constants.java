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
        c.xPodOffset.set(4.608990826944667);
        c.yPodOffset.set(-4.548713503860114);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.36005710733345286);
                Controller secondaryTranslationalForward = Controller.proportional(0.13303148025661218);
                Controller primaryTranslationalLateral = Controller.proportional(0.7989703003539057);
                Controller secondaryTranslationalLateral = Controller.proportional(0.2951981771011548);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.020529815772951477));
                c.brake.set(Controller.proportionalFeedforward(0.017450343407008757));

                c.headingFeedback.set(Controller.proportional(5.707787468677254));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.048774358652981686, 0.010416012780309148));

                c.linearBrakeCoefficients.set(Matrix.diag(0.04550686169525764, 0.07171012821456617));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.003014634607790862, 0.002041174805997158));

                c.maxAchievableForwardVelocity.set(50.66229048712929);
                c.maxAchievableStrafeVelocity.set(40.45457668712854);
                c.naturalForwardDeceleration.set(57.59289789658548);
                c.naturalStrafeDeceleration.set(125.4274086686866);
            }
    );


}