package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "ThreadTest", group = "test")

public class ThreadTest extends OpMode {

    ModuleThread motorThread;

    int count = 0;

    double power = 0;

    DcMotor motor_main;
    double lastposition_main = 0;
    int movements_main = 0;


    @Override
    public void init() {
        motorThread = new ModuleThread("motorThread");

        motor_main = hardwareMap.get(DcMotor.class, "1-1");
    }

    @Override
    public void loop() {
        count++;
        power = 1;

        motor_main.setPower(power);
        if(motor_main.getCurrentPosition() != lastposition_main){
            movements_main++;
        }

        telemetry.addData( "movements thread (fullpower)", motorThread.threadTasks.movements);
        telemetry.addData( "movements main   (fullpower)", movements_main);
        telemetry.addData( "count", count);
        telemetry.update();
    }

    @Override
    public void stop(){
        power = 0;
        motor_main.setPower(power);
    }


    public class ModuleThread implements Runnable{

        ThreadTasks threadTasks;

        private boolean exit;
        Thread t;
        ModuleThread(String name) {
            threadTasks = new ThreadTasks(name);
            t = new Thread(this, name);
            exit = false;
            t.start();
        }

        public void run(){
            threadTasks.initialize();
            while(!exit) {
                threadTasks.execute();

//                try{
//                    Thread.sleep(20);
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }

            }
        }

        public void stop(){
            exit = true;
        }
    }

    class ThreadTasks {
        double lastPosition = 0;
        String name;

        int movements = 0;
        DcMotor motor;

        ThreadTasks(String name_input){
            name = name_input;
        }
        void initialize(){
            time = System.currentTimeMillis();
            motor = hardwareMap.get(DcMotor.class, "1-0");
        }

        void execute(){
            motor.setPower(power);
            if(motor.getCurrentPosition() != lastPosition){
                movements++;
            }
            lastPosition = motor.getCurrentPosition();
        }
    }


}
