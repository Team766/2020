package com.team766.frc2020.paths;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Arrays;

import com.team766.frc2020.paths.MapArray;

    //TODO: make a low res 1/6th res map to make calculation faster (1/6 pixel will make each node one 1/2 robot width which is good)
public class AStarGeneration {
    // private double[][] imageDoubleArray = new double[707][1384];
    MapArray array = new MapArray();

/*     public static void main(String[] args) throws IOException{
        // open image
        File imgPath = new File("src\\main\\java\\com\\team766\\frc2020\\paths\\CLEAN FieldMap.png");
        BufferedImage bufferedImage = ImageIO.read(imgPath);
       
        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferInt data   = (DataBufferInt)raster.getDataBuffer();
        //DataBufferByte data   = DataBufferByte( (DataBufferByte)raster.getDataBuffer(), 707);
       
        // System.out.println(data.getData());
        // data.getData().toString();
        // System.out.println((char)data.getData()[1]);
        System.out.println(Arrays.toString(data.getData()));


    }     */   


    public static void main(String[] args) throws IOException{
        // open image
        File imgPath = new File("src\\main\\java\\com\\team766\\frc2020\\paths\\CLEAN FieldMap.png");
        BufferedImage image = new BufferedImage(707,1384,BufferedImage.TYPE_INT_RGB);
       

        for(int i = 0; i < 707; i++)
            for(int j = 0; j < 1384; j++)
                array.setArray(i, j, image.getRGB(i, j));


/*
        //make sure no out of bounds collisions and stuff, should be fine
        for(int i = 0; i < 707; i+6)
            for(int j = 0; j < 1384; j+6)
                array.setLowResArray(i, j, image.getRGB(i, j)); 
                */
    } 

    
}
