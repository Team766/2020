package com.team766.framework;

import java.lang.reflect.Field;

public class AutonomousCommandUtils {
	public static Command getCommand(Enum<?> autonomousModesEnum) {
	    try {
	        Field f = autonomousModesEnum.getClass().getField(autonomousModesEnum.name());
	        AutonomousCommand annotation = f.getAnnotation(AutonomousCommand.class);
	        return annotation.commandClass().getConstructor().newInstance();
	    } catch (Exception cause) {
	        throw new RuntimeException(cause);
	    }
	}
}
