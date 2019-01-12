package com.team766.frc2019.mechanisms;

import com.team766.framework.Mechanism;
import com.team766.hal.RobotProvider;
import com.team766.hal.EncoderReader;

public class Encoder extends Mechanism {
    private EncoderReader m_leftEncoder;
    private EncoderReader m_rightEncoder;

    public Encoder() {
        m_leftEncoder = RobotProvider.instance.getEncoder("drive.leftEncoder"); //change these to whatever we call it in config
        m_rightEncoder = RobotProvider.instance.getEncoder("drive.rightEncoder"); //also here
    }

    
}