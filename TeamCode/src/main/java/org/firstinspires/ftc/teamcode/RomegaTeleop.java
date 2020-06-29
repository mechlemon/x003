package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.lib.Tuner;


@TeleOp(name = "RomegaTeleop", group = "drive")

public class RomegaTeleop extends OpMode {

    private Hardware hardware;
    private Tuner tuner;

    private String[] titles = new String[] {"linCoeff", "angCoeff", "halfDistBetWheels"};
    private double[] values = new double[] {     0.7  ,      1  ,          0.1       };

    public void init(){
        hardware = new Hardware(hardwareMap, telemetry, false, false);
        tuner = new Tuner(titles, values, gamepad1, telemetry);
    }

    public void loop(){
        tuner.tune();

        double linVelo = gamepad1.left_stick_y * tuner.get("linCoeff");
        double angVelo = gamepad1.right_stick_x * tuner.get("angCoeff");

        double turnCenter = linVelo/angVelo; // r=v/ω

        double radiusL = turnCenter - tuner.get("halfDistBetWheels");
        double radiusR = turnCenter + tuner.get("halfDistBetWheels");

        double leftVelo = angVelo * radiusL;
        double rightVelo = angVelo * radiusR;

        hardware.drivetrain.setVelocities(leftVelo, rightVelo);

        telemetry.addData("linVelo", linVelo);
        telemetry.addData("angVelo", angVelo);
        telemetry.addData("turnCenter", turnCenter);
        telemetry.addData("leftVelo target", leftVelo);
        telemetry.addData("rightVelo target", rightVelo);
        telemetry.update();
    }
}
