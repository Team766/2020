package com.team766.frc2020.paths;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;

    
public class AStarGeneration {
    /* // This block of code can iake in any image and will output a text file "byte.map". As the name says, it is in bytes. You can just convert it to whatever other format you want.
        // I made it into binary to minimize the size and make it easily intakeable as a binary map for passable areas in a boolean array
    public static void main(String[] args) throws IOException{
        File imgPath = new File("src\\main\\java\\com\\team766\\frc2020\\paths\\CLEAN FieldMap.png");
        BufferedImage bufferedImage = ImageIO.read(imgPath);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data   = (DataBufferByte)raster.getDataBuffer();
  
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("byte.map"));
            out.write(Arrays.toString(data.getData()));
            out.close();
        } catch (IOException e) {
        }
    }  */

     public static void main(String[] args) throws IOException{
       


    /*
        //make sure no out of bounds collisions and stuff, should be fine
        //makes a low res 1/6th res map to make calculation faster (1/6 pixel will make each node one 1/2 robot width which is good)
        for(int i = 0; i < 707; i+6)
            for(int j = 0; j < 1384; j+6)
                array.setLowResArray(i, j, image.getRGB(i, j)); 
    */
    }
}
