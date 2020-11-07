package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.lib.Tuner;

@TeleOp(name = "ShooterThread", group = "test")

public class ShooterThread extends OpMode {

    MotorThread motorThread;
    double speed = 0;

    Tuner tuner = new Tuner(
        new String[] {"speed"},
        new double[] { -0.9  },
        gamepad1,
        telemetry
    );

    @Override
    public void init() {
        motorThread = new MotorThread("1-2");
    }

    @Override
    public void loop() {
        tuner.tune();
        speed = tuner.get("speed");

        telemetry.addData( "velo", motorThread.motor.getVelocity() / motorThread.motor.getMotorType().getAchieveableMaxTicksPerSecond());
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
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            new Thread(this).start();
        }

        public void run(){
            while(!stopped){
                motor.setPower(speed);
            }
        }

        void stop(){
            stopped = true;
            motor.setPower(0);
        }
    }

}
