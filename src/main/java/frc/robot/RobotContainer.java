/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.Constants.OIConstants;
import frc.robot.autoRoutines.DriveForwardThenBumpFireCommandGroup;
import frc.robot.autoRoutines.FireThreeThenForwardCommandGroup;
import frc.robot.commands.AllInCommand;
import frc.robot.commands.AllOutCommand;
import frc.robot.commands.drivetrain.TurnToTargetPIDCommand;

import frc.robot.commands.hood.HoodInCommand;
import frc.robot.commands.hood.HoodOutCommand;




import frc.robot.commands.intake.IntakeInCommand;
import frc.robot.commands.intake.IntakeOutCommand;
import frc.robot.commands.intake.ToggleIntakeCommand;
import frc.robot.commands.neck.NeckClearCommand;

import frc.robot.commands.shifting.ToggleShiftingCommand;
import frc.robot.commands.shooter.ToggleShooterCommand;
import frc.robot.subsystems.*;
import frc.robot.utils.Limelight;
import frc.robot.utils.XController;
import frc.robot.utils.XboxDPad;
import frc.robot.utils.XboxTrigger;
//import frc.robot.utils.DoubleButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // public final Joystick leftJoy;
  // public final Joystick rightJoy;
  // public final Joystick secondaryJoy;
  // private WheelOfFortuneSubsystem wheelOfFortuneSubsystem;
  public final Joystick driverStationJoy;
  private final CommandScheduler commandScheduler;
  // public final Joystick climberJoy;
  Joystick servoJoy;
  public DrivetrainSubsystem drivetrainSubsystem;
  public IntakeSubsystem intakeSubsystem;
  public NeckSubsystem neckSubsystem;
  public ShooterSubsystem shooterSubsystem;
  public HopperSubsystem hopperSubsystem;
  public HoodSubsystem hoodSubsystem;
  public ShiftingGearsSubsystem shiftingGearsSubsystem;
  public Limelight limelight;
  private XboxSubsystem xboxSubsystem;

  public boolean isDriverStation;
  // private DoubleButton toggleClimb;

  public RobotContainer() {
    commandScheduler = CommandScheduler.getInstance();
    driverStationJoy = new Joystick(OIConstants.DRIVER_STATION_JOY);
    // climberJoy = new Joystick(OIConstants.CLIMBER_JOY);

    isDriverStation = !(DriverStation.getJoystickIsXbox(0) || DriverStation.getJoystickIsXbox(1));

    drivetrainSubsystem = new DrivetrainSubsystem();
    intakeSubsystem = new IntakeSubsystem();
    neckSubsystem = new NeckSubsystem();
    shooterSubsystem = new ShooterSubsystem();
    hopperSubsystem = new HopperSubsystem();
    hoodSubsystem = new HoodSubsystem();
    shiftingGearsSubsystem = new ShiftingGearsSubsystem();
    xboxSubsystem = new XboxSubsystem();
    limelight = new Limelight();


    //enable this to drive!!
    switch (drivetrainSubsystem.getDriveMode()) {
      case TANK:
        drivetrainSubsystem.setDefaultCommand(new RunCommand(() -> drivetrainSubsystem.tankDrive(getLeftY(), getRightY()), drivetrainSubsystem));
        break;
      case CHEEZY:
        drivetrainSubsystem.setDefaultCommand(new RunCommand(() -> drivetrainSubsystem.cheezyDrive(getLeftY(), getRightX()), drivetrainSubsystem));
        break;
      case ARCADE:
        drivetrainSubsystem.setDefaultCommand(new RunCommand(() -> drivetrainSubsystem.arcadeDrive(getLeftY(), getRightX()), drivetrainSubsystem));
        break;
      default:
        // tank 
        drivetrainSubsystem.setDefaultCommand(new RunCommand(() -> drivetrainSubsystem.tankDrive(getLeftY(), getRightY()), drivetrainSubsystem));
        break;
    }    
    configureButtonBindings();
  }
  
  private void configureButtonBindings() {
    if (isDriverStation) {
      // bottom row
      setJoystickButtonWhileHeld(driverStationJoy, 1, new AllInCommand(intakeSubsystem, hopperSubsystem, neckSubsystem));
      setJoystickButtonWhenPressed(driverStationJoy, 2, new ToggleShooterCommand(shooterSubsystem));
      setJoystickButtonWhileHeld(driverStationJoy, 3, new NeckClearCommand(neckSubsystem)); //Change from while held to when pressed, just have to figure out the correct time value
      setJoystickButtonWhileHeld(driverStationJoy, 4, new HoodInCommand(hoodSubsystem));

      setJoystickButtonWhileHeld(driverStationJoy, 5, new InstantCommand());

      // top row
      setJoystickButtonWhileHeld(driverStationJoy, 6, new AllOutCommand(intakeSubsystem, hopperSubsystem, neckSubsystem));
      setJoystickButtonWhileHeld(driverStationJoy, 7, new IntakeInCommand(intakeSubsystem));
      setJoystickButtonWhenPressed(driverStationJoy, 8, new InstantCommand());
      setJoystickButtonWhileHeld(driverStationJoy, 9, new HoodOutCommand(hoodSubsystem));

      setJoystickButtonWhileHeld(driverStationJoy, 10, new InstantCommand());

      //joystick buttons
      setJoystickButtonWhenPressed(driverStationJoy, 11, new ToggleShiftingCommand(shiftingGearsSubsystem));
      setJoystickButtonWhenPressed(driverStationJoy, 12, new ToggleIntakeCommand(intakeSubsystem));

      
      // climber joystick buttons
      // setJoystickButtonWhileHeld(climberJoy, 10, new ClimberDownCommand(climberSubsystem));
      // setJoystickButtonWhileHeld(climberJoy, 11, new ClimberUpCommand(climberSubsystem));
      // setJoystickButtonWhenPressed(climberJoy, 1, new ToggleBrakeCommand(climberSubsystem));
    } else {
      /*
        Allowed buttons:
        kA, kB, kBack, kBumperLeft, kBumperRight, kStart, kStickLeft, kStickRight, kX, kY (and triggers)
      */

      setXboxButtonWhenPressed(xboxSubsystem.getController(), XboxController.Button.kLeftStick, new ToggleShiftingCommand(shiftingGearsSubsystem));
      setXboxButtonWhenPressed(xboxSubsystem.getController(), XboxController.Button.kRightStick, new ToggleIntakeCommand(intakeSubsystem));
      
      
      xboxSubsystem.getController().setTriggerWhileHeld(XboxTrigger.Hand.RIGHT, new AllInCommand(intakeSubsystem, hopperSubsystem, neckSubsystem));
      xboxSubsystem.getController().setTriggerWhileHeld(XboxTrigger.Hand.LEFT, new AllOutCommand(intakeSubsystem, hopperSubsystem, neckSubsystem));
      setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kLeftBumper, new IntakeOutCommand(intakeSubsystem));
      setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kRightBumper, new IntakeInCommand(intakeSubsystem)); 

      setXboxButtonWhenPressed(xboxSubsystem.getController(), XboxController.Button.kA, new TurnToTargetPIDCommand(drivetrainSubsystem));
      setXboxButtonWhenPressed(xboxSubsystem.getController(), XboxController.Button.kB, new ToggleShooterCommand(shooterSubsystem));

      setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kY, new InstantCommand());
      setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kX, new InstantCommand());
      //setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kY, new HoodInCommand(hoodSubsystem));// up
     // setXboxButtonWhileHeld(xboxSubsystem.getController(), XboxController.Button.kX, new HoodOutCommand(hoodSubsystem));// down
    }
  }

  public double getServoThrottle() {
    return servoJoy.getZ();
  }

  public double getLeftY() {
    return (isDriverStation) ? -driverStationJoy.getRawAxis(0) : -xboxSubsystem.getController().getLeftY();
  }

  public double getLeftX() {
    return (isDriverStation) ? driverStationJoy.getRawAxis(1) : -xboxSubsystem.getController().getLeftX();
  }

  public double getRightY() {
    return (isDriverStation) ? -driverStationJoy.getRawAxis(2) : -xboxSubsystem.getController().getRightY();
  }

  public double getRightX() {
    return (isDriverStation) ? driverStationJoy.getRawAxis(3) : -xboxSubsystem.getController().getRightX();
  }

  public double getLeftThrottle() {
    return (driverStationJoy.getThrottle() + 1) / 2;
  }

  private void setJoystickButtonWhenPressed(Joystick joystick, int button, CommandBase command) {
    new JoystickButton(joystick, button).whenPressed(command);
  }

  private void setJoystickButtonWhileHeld(Joystick joystick, int button, CommandBase command) {
    new JoystickButton(joystick, button).whileHeld(command);
  }

  private void setXboxButtonWhenPressed(XboxController xboxController, XboxController.Button button, CommandBase command) {
    new JoystickButton(xboxController, button.value).whenPressed(command);
  }

  private void setXboxButtonWhileHeld(XboxController xboxController, XboxController.Button button, CommandBase command) {
    new JoystickButton(xboxController, button.value).whileHeld(command);
  }

  public void updateIsDriverStation(){
    boolean prev = isDriverStation;
    isDriverStation = !(DriverStation.getJoystickIsXbox(0) || DriverStation.getJoystickIsXbox(1));
    if (prev == isDriverStation) {
      return;
    } else {
      commandScheduler.clearButtons();
      configureButtonBindings();
    }
  }

  public Command getAutonomousCommand(){
    switch ((Robot.AutoModes) Robot.autoChooser.getSelected()) {
			case FIRE_FORWARD:
				return new FireThreeThenForwardCommandGroup(0.5, shooterSubsystem, intakeSubsystem, hopperSubsystem, neckSubsystem, drivetrainSubsystem, hoodSubsystem);
			case FORWARD_FIRE:
        return new DriveForwardThenBumpFireCommandGroup(0.5, drivetrainSubsystem, shooterSubsystem, intakeSubsystem, hopperSubsystem, neckSubsystem, hoodSubsystem);
			default:
        return new DriveForwardThenBumpFireCommandGroup(0.5, drivetrainSubsystem, shooterSubsystem, intakeSubsystem, hopperSubsystem, neckSubsystem, hoodSubsystem);
		}
  }
}
