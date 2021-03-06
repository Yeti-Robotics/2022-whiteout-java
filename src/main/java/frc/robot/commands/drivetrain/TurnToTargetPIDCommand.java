// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.utils.Limelight;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TurnToTargetPIDCommand extends PIDCommand {
  /** Creates a new TurnToTargetPIDCommand. */
  public TurnToTargetPIDCommand(DrivetrainSubsystem drivetrainSubsystem) {
    super(
        // The controller that the command will use
        new PIDController(0.055, 0, 0.005),
        // This should return the measurement
        Limelight::getTx,
        // This should return the setpoint (can also be a constant)
        0.0,
        // This uses the output
        output -> {
          // if(output > 0){
          //   drivetrainSubsystem.tankDrive(output, -output);
          // } else {
          //   drivetrainSubsystem.tankDrive(-output, output);
          // }
            drivetrainSubsystem.tankDrive(-output, output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    addRequirements(drivetrainSubsystem);
    getController().setTolerance(0.5);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
