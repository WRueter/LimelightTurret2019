/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static OI m_oi;
  private TalonSRX _turret;

  //Limelight Values
  NetworkTable nt = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = nt.getEntry("tx");
  NetworkTableEntry ty = nt.getEntry("ty");
  NetworkTableEntry ta = nt.getEntry("ta");
  private double x, y, a, rx, ry, ra, d;

  //Motor related values
  private int target, error;
  private static double f = 0.1124;
  private static double p = .41;
  private static double i = 0;
  private static double pidD = 0;
  

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  @Override
  public void robotInit() {
    m_oi = new OI();
    m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    SmartDashboard.putData("Auto mode", m_chooser);

    //Motor & PID Configuration
    _turret = new TalonSRX(3);

    _turret.configFactoryDefault();
    _turret.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    _turret.selectProfileSlot(0, 0);
    _turret.config_kF(0, f, 30);
    _turret.config_kP(0, p, 30);
    _turret.config_kI(0, i, 30);
    _turret.config_kD(0, pidD, 30);
    _turret.configMotionCruiseVelocity(1200);
    _turret.configMotionAcceleration(1500);
    _turret.configAllowableClosedloopError(0, 10, 30);

  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    _turret.setSelectedSensorPosition(0); //Zero encoder
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();

    //Limelight Values
    x = tx.getDouble(0); //Horizontal angle offset
    y = ty.getDouble(0); //Vertical angle offset
    a = ta.getDouble(0); //Area of bounding box
    d = 190.42 * Math.pow(a, -0.471); //Distance calculation, based off of regression in Excel

    //Calculate target and set turret to follow it
    target = _turret.getSelectedSensorPosition() - (int) ((x / 360) * 4096 * 4); //Converts angle measure in degrees to native units, then subtracts from current position
    _turret.set(ControlMode.MotionMagic, target);

    //Values for SmartDashboard
    rx = Math.round(x);
    ry = Math.round(y);
    ra = Math.round(a);
    error = target - _turret.getSelectedSensorPosition();

    SmartDashboard.putNumber("Motor Speed", _turret.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Motor Position", _turret.getSelectedSensorPosition());
    SmartDashboard.putNumber("Limelight Horizontal Angle", rx);
    SmartDashboard.putNumber("Limelight Vertical Angle", ry);
    SmartDashboard.putNumber("Limelight Bounding Box Area", ra);
    SmartDashboard.putNumber("Distance", d);
    SmartDashboard.putNumber("Error", error);
    SmartDashboard.putNumber("Target", target);
  }

  @Override
  public void testPeriodic() {
  }
}
