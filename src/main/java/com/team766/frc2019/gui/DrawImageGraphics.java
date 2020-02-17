package com.team766.frc2019.gui;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import com.team766.frc2019.gui.DemoPaint;
import javax.swing.*;
import com.team766.frc2019.gui.MainClass;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO; 
import com.team766.frc2019.gui.*;


public class DrawImageGraphics {
    public static void main(String[] a) {
      JFrame window = new JFrame();
      window.getContentPane().add(new MainClass());


      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setBounds(30, 30, 700, 700);

      window.getContentPane().add(new DemoPaint());
      window.getContentPane().add(new MainClass()); // only one of these 2 work at a time
      window.setVisible(true);


      // need to add a button that will clear all point in arraylist
      // need to write the arraylist all to a file in a nice place all at once
      // add ability to read in stuff, to add to waypoints
    }
  }