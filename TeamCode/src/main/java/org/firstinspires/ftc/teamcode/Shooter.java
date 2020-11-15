package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.ButtonPress;
import org.firstinspires.ftc.teamcode.lib.Calculate.PIDF;
import org.firstinspires.ftc.teamcode.lib.Tuner;

@TeleOp(name = "Shooter", group = "test")

public class Shooter extends OpMode {

    DcMotorEx motor;
    PIDF pid = new PIDF();

    Servo yeetup;
    boolean yeeting = false;

    ButtonPress ypress = new ButtonPress();

    Tuner tuner;

    double backpos = 0.77;
    double frontpos = 0.69;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotorEx.class, "1-2");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        yeetup = hardwareMap.servo.get("yeetup");
        tuner = new Tuner(
                new String[] {"speed", "delay", "toggleBoost", "p", "i", "d", "f"},
                new double[] { 0.35  ,   0.16 ,      1       , 0.1,  0 ,  0 ,  1 },
                gamepad1,
                telemetry
        );
        resetStartTime();
    }

    @Override
    public void loop() {
        tuner.tune();
        double targetSpeed = tuner.get("speed");
        double delay = tuner.get("delay");

        pid.setConstants(
                tuner.get("p"),
                tuner.get("i"),
                tuner.get("d"),
                tuner.get("f"),
                0,
                0
        );

        if(ypress.status(gamepad1.y) == ButtonPress.Status.COMPLETE){
            yeeting = true;
            resetStartTime();
        }

        if(getRuntime() < 3*delay && yeeting){
            if(tuner.get("toggleBoost") > 0){
                targetSpeed = 1;
            }
            if(getRuntime() % delay < 0.5*delay){
                yeetup.setPosition(frontpos);
            }else{
                yeetup.setPosition(backpos);
            }
        }
        double currentSpeed = motor.getVelocity() / motor.getMotorType().getAchieveableMaxTicksPerSecond();

        pid.loop(currentSpeed, targetSpeed);
        motor.setPower(pid.getPower());

        telemetry.addData("power", motor.getPower());
        telemetry.addData("error", pid.getError());
        telemetry.addData( "currentSpeed", currentSpeed);
        telemetry.update();
    }

}
