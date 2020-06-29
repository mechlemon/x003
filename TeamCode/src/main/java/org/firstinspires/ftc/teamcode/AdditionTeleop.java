package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.lib.Tuner;


@TeleOp(name = "AdditionTeleop", group = "drive")

public class AdditionTeleop extends OpMode {

    private Hardware hardware;
    private Tuner tuner;

    private String[] titles = new String[] {"turnCoeff"};
    private double[] values = new double[] {     0.7   };

    public void init(){
        hardware = new Hardware(hardwareMap, telemetry, false, false);
        tuner = new Tuner(titles, values, gamepad1, telemetry);
    }

    public void loop(){
        tuner.tune();

        double forward = gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x * tuner.get("turnCoeff");

        hardware.drivetrain.setPowers(forward + turn, forward - turn);

        telemetry.addData("forward", forward);
        telemetry.addData("turn", turn);
        telemetry.addData("left inches", hardware.drivetrain.getLeftDistance());
        telemetry.addData("right inches", hardware.drivetrain.getRightDistance());
        telemetry.update();
    }
}
