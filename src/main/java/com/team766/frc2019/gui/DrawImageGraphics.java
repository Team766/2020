package com.team766.frc2019.gui;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import com.team766.frc2019.gui.DemoPaint;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO; 

public class DrawImageGraphics {
    public static void main(String[] a) {
      JFrame window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setBounds(30, 30, 700, 700);
      window.getContentPane().add(new DemoPaint());
      window.setVisible(true);

      for (int i=0; i<10; i++) {
        PointerInfo c = MouseInfo.getPointerInfo();
        Point b = c.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();
        System.out.println("("+x+", "+y+")");
      }
    }
  }