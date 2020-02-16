package com.team766.frc2019.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;

class DemoPaint extends JComponent {

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    Image picture = Toolkit.getDefaultToolkit().getImage("C:\\Users\\samir\\Documents\\Github\\2020\\src\\main\\java\\com\\team766\\frc2019\\gui\\map.png");
    g2.drawImage(picture, 10, 10, this);
    g2.finalize();
  }
}


