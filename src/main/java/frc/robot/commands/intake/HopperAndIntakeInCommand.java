// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HopperSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class HopperAndIntakeInCommand extends CommandBase {
  private HopperSubsystem hopperSubsystem; 
  private IntakeSubsystem intakeSubsystem; 

  public HopperAndIntakeInCommand(HopperSubsystem hopperSubsystem, IntakeSubsystem intakeSubsystem) {
    this.hopperSubsystem = hopperSubsystem;
    this.intakeSubsystem = intakeSubsystem; 
    addRequirements(hopperSubsystem, intakeSubsystem);
  }

  @Override
  public void initialize() {
    this.hopperSubsystem.funnelIn();
    this.intakeSubsystem.rollIn();
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    this.hopperSubsystem.funnelStop();
    this.intakeSubsystem.stopRoll();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
