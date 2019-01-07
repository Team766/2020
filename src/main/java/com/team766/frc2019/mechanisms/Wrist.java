package com.team766.frc2019.mechanisms;

import com.team766.config.ConfigFileReader;
import com.team766.controllers.PIDController;
import com.team766.framework.Mechanism;
import com.team766.hal.EncoderReader;
import com.team766.hal.RobotProvider;
import com.team766.hal.SpeedController;
import com.team766.library.ValueProvider;

public class Wrist extends Mechanism {
	
	public static final ValueProvider<Double> STOWED_POSITION =
			ConfigFileReader.getInstance().getDouble("wrist.stowedPosition");
	public static final ValueProvider<Double> VERTICAL_POSITION =
			ConfigFileReader.getInstance().getDouble("wrist.verticalPosition");
	public static final ValueProvider<Double> DOWN_POSITION =
			ConfigFileReader.getInstance().getDouble("wrist.downPosition");

	private PIDController m_controller;
	private EncoderReader m_encoder;
	private SpeedController m_motor;
	
	private double m_commandedPosition;
	
	public Wrist() {
		m_controller = PIDController.loadFromConfig("wrist");
		m_encoder = RobotProvider.instance.getEncoder("wrist.encoder");
		m_motor = RobotProvider.instance.getMotor("wrist.motor");
	}
	
	public void run() {
		m_controller.setSetpoint(m_commandedPosition);
		m_controller.calculate(getPosition(), true);
		double command = m_controller.getOutput();
		m_motor.set(command);
	}
	
	public double getPosition() {
		return m_encoder.getDistance();
	}
	
	public void setTargetPosition(double position) {
		m_commandedPosition = position;
	}
	
	public void setTargetStowed() {
		setTargetPosition(STOWED_POSITION.get());
	}
	
	public void setTargetVertical() {
		setTargetPosition(VERTICAL_POSITION.get());
	}
	
	public void setTargetDown() {
		setTargetPosition(DOWN_POSITION.get());
	}
	
	public boolean atTarget() {
		return m_controller.isDone();
	}
	
}
