package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.ButtonPress;
import org.firstinspires.ftc.teamcode.lib.Calculate.PIDF;
import org.firstinspires.ftc.teamcode.lib.Tuner;

@TeleOp(name = "ShooterThread", group = "test")

public class ShooterThread extends OpMode {

    MotorThread motorThread;
    double speed = 0;

    PIDF pid = new PIDF();

    Servo yeetup;
    boolean yeeting = false;

    ButtonPress ypress = new ButtonPress();

    Tuner tuner;

    double backpos = 0.77;
    double frontpos = 0.69;

    @Override
    public void init() {
        motorThread = new MotorThread("1-2");
        yeetup = hardwareMap.servo.get("yeetup");
        tuner = new Tuner(
                new String[] {"speed", "delay", "toggleBoost", "p", "i", "d", "f"},
                new double[] { 0.35  ,   0.17 ,      1       , 0.1,  0 ,  0 ,  1 },
                gamepad1,
                telemetry
        );
        resetStartTime();
    }

    @Override
    public void loop() {
        tuner.tune();
        speed = tuner.get("speed");
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
                speed = 1;
            }
            if(getRuntime() % delay < 0.5*delay){
                yeetup.setPosition(frontpos);
            }else{
                yeetup.setPosition(backpos);
            }
        }


        telemetry.addData("power", motorThread.motor.getPower());
        telemetry.addData("error", pid.getError());
        telemetry.addData( "rpm", (motorThread.motor.getVelocity() / (28.0*6300)) * 60);
        telemetry.update();
    }

    @Override
    public void stop(){
        motorThread.stop();
    }

    class MotorThread implements Runnable {
        DcMotorEx motor;
        boolean stopped = false;

        MotorThread(String motorName) {
            motor = hardwareMap.get(DcMotorEx.class, motorName);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            new Thread(this).start();
        }

        public void run(){
            while(!stopped){
                pid.loop(motor.getVelocity() / (28.0*6300.0) * 60 , speed);
                motor.setPower(pid.getPower());
            }
        }

        void stop(){
            stopped = true;
            motor.setPower(0);
        }
    }

}
