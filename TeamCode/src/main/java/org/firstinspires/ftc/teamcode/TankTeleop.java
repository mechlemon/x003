package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TankTeleop", group = "drive")

public class TankTeleop extends OpMode {

    private Hardware hardware;

    public void init(){
        hardware = new Hardware(hardwareMap, telemetry, false, false);
    }

    public void loop(){
        hardware.drivetrain.setPowers(gamepad1.left_stick_y, gamepad1.right_stick_y);

        telemetry.addData("left stick y", gamepad1.left_stick_y);
        telemetry.addData("right stick y", gamepad1.right_stick_y);
        telemetry.addData("left inches", hardware.drivetrain.getLeftDistance());
        telemetry.addData("right inches", hardware.drivetrain.getRightDistance());
        telemetry.update();
    }
}
