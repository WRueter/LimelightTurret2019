/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;


public class Turret extends Subsystem {

  private static Turret _instance = new Turret(); //Singleton pattern
  public static Turret getInstance(){
    return _instance;
  }
  private TalonSRX _motor;

  //PID Values
  private static double f = 0.1124;
  private static double p = .4;
  private static double i = 0;
  private static double d = 0;

  private Turret(){

    //Motor & PID Configuration
    _motor = new TalonSRX(3);

    _motor.configFactoryDefault();
    _motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    _motor.selectProfileSlot(0, 0);
    _motor.config_kF(0, f, 30);
    _motor.config_kP(0, p, 30);
    _motor.config_kI(0, i, 30);
    _motor.config_kD(0, d, 30);
    _motor.configMotionCruiseVelocity(1200);
    _motor.configMotionAcceleration(1500);
    _motor.configAllowableClosedloopError(0, 10, 30);

  }

  public void followTarget(int t){
    _motor.set(ControlMode.MotionMagic, t);
  }
  public int getMotorSpeed(){
    return _motor.getSelectedSensorVelocity();
  }
  public int getMotorPos(){
    return _motor.getSelectedSensorPosition();
  }
  public void zeroMotor(){
    _motor.setSelectedSensorPosition(0);
  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}