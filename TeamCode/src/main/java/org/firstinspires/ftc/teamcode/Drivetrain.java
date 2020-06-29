package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Drivetrain {

    private final double WHEEL_CIRCUMFERENCE = 2.5 * Math.PI; //inches
    private final double GEAR_RATIO = 26/22.0;
    private final double TICKS_PER_REV = 383.6; //yellowjacket 435rpm, counts per rev of motor shaft

    private final double TICKS_PER_INCH;

    public DcMotor leftMotor, rightMotor;
    private double leftZero, rightZero = 0;

    public Drivetrain(DcMotor leftMotor, DcMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;

        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        TICKS_PER_INCH = TICKS_PER_REV * GEAR_RATIO / WHEEL_CIRCUMFERENCE;
    }

    //call this every loop to make sure the power sent to the motors is updated
    //also does a last clip to make sure power never exceeds 1
    public void setPowers(double leftPower, double rightPower) {
        leftMotor.setPower(Range.clip(leftPower, -1, 1));
        rightMotor.setPower(Range.clip(rightPower, -1, 1));
    }

    public double getLeftDistance(){
        return (leftMotor.getCurrentPosition() - leftZero) * TICKS_PER_INCH;
    }

    public double getRightDistance(){
        return (rightMotor.getCurrentPosition() - rightZero) * TICKS_PER_INCH;
    }

    public void resetEncoders(){
        leftZero = leftMotor.getCurrentPosition();
        rightZero = rightMotor.getCurrentPosition();
    }
}
