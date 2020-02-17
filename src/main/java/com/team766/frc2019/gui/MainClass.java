package com.team766.frc2019.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import com.team766.frc2019.gui.*;
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
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import com.team766.frc2019.gui.*;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;
import java.io.*;

public class MainClass extends JPanel {

  public MainClass() {

      addMouseListener(new MouseAdapter() { 
          public void mousePressed(MouseEvent me) { 
            // System.out.println(me); 
            PointerInfo c = MouseInfo.getPointerInfo();
            Point b = c.getLocation();
            int x = (int) b.getX();
            int y = (int) b.getY();
            System.out.println("("+x+", "+y+")");
            try{
                PrintWriter out = new PrintWriter(new FileWriter("waypointslist.txt"));
                out.println(x + " " + y);
                out.close();
            } catch (Exception e){
                e.printStackTrace();
            }

          } 
        }); 

        

  }
}

