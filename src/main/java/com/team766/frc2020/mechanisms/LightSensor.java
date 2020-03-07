package com.team766.frc2020.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.DigitalInputReader;
import com.team766.hal.RobotProvider;

public class LightSensor extends Mechanism {
    public DigitalInputReader m_toplightSensor;
    public DigitalInputReader m_bottomlightSensor;
    public LightSensor(){
        m_toplightSensor = RobotProvider.instance.getDigitalInput("toplightsensor");
        m_bottomlightSensor = RobotProvider.instance.getDigitalInput("bottomlightsensor");
    }

    public boolean getTopLightSensorState() {
        return m_toplightSensor.get();
    }

    public boolean getBottomLightSensorState() {
        return m_bottomlightSensor.get();
    }

    public void checkLightSensor(){
        if(m_toplightSensor.get()==true){
            System.out.println("Top is true");
        } else{
            System.out.println("Top is false");
        }
        if(m_bottomlightSensor.get()==true){
            System.out.println("Bottom is true");
        } else{
            System.out.println("Bottom is false");
        }
    }
}