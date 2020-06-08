package com.team766.frc2020.paths;

import java.io.DataInputStream; 
import java.io.FileInputStream; 
import java.io.IOException; 
  
// This class takes in the binary representation of the passable field map in binary.map and makes it an array in very fancy efficient way
public class InputMapArray 
{ 
    static class Reader 
    { 
        // to convert the low res map points back to normal inches just multiply x (col) and y (row) each by 6
        final private static int width = 707/6;
        final private static int height = 1384/6;
        public static boolean[][] imageBoolArray = new boolean[height][width];
        final private int BUFFER_SIZE = 1 << 16; 
        private DataInputStream din; 
        private byte[] buffer; 
        private int bufferPointer, bytesRead; 
        
  
        public Reader() throws IOException 
        { 
            din = new DataInputStream(new FileInputStream("src\\main\\java\\com\\team766\\frc2020\\paths\\binary.map")); 
            buffer = new byte[BUFFER_SIZE]; 
            bufferPointer = bytesRead = 0; 
        } 
  
        public int nextInt() throws IOException 
        { 
            int ret = 0; 
            byte c = read(); 
            while (c <= ' ') 
                c = read(); 
            boolean neg = (c == '-'); 
            if (neg) 
                c = read(); 
            do
            { 
                ret = ret * 10 + c - '0'; 
            }  while ((c = read()) >= '0' && c <= '9'); 
  
            if (neg) 
                return -ret; 
            return ret; 
        } 
  
        private void fillBuffer() throws IOException 
        { 
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE); 
            if (bytesRead == -1) 
                buffer[0] = -1; 
        } 
  
        private byte read() throws IOException 
        { 
            if (bufferPointer == bytesRead) 
                fillBuffer(); 
            return buffer[bufferPointer++]; 
        } 
  
        public void close() throws IOException 
        { 
            if (din == null) 
                return; 
            din.close(); 
        }
        
        public static void printArray() {
            for (int i = 0; i <= width; i++) {
                System.out.print(" _"); // border of map
            }
            System.out.print("\n");

            for (int j = 0; j < height; j++) {
                System.out.print("|"); // border of map
                for (int i = 0; i < width; i++) {
                    if (imageBoolArray[j][i]) {
                        System.out.print("  ");
                    } else {
                        System.out.print(" #"); // draw unwalkable
                    }
                }
                System.out.print("|\n"); // border of map
            }

            for (int i = 0; i <= width; i++) {
                System.out.print(" _"); // border of map
            }
        }

        public static boolean isWalkable(int xPosition, int yPosition) {
            return imageBoolArray[yPosition][xPosition];
        }
  
        public static void generateMapArray() throws IOException { //978488 ints
            Reader s = new Reader(); 
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (s.nextInt() == 1) {
                        imageBoolArray[i][j] = true;
                    } else {
                        imageBoolArray[i][j] = false;
                    }
                    for (int k = 0; k < 5; k++) { // skip 5 columns
                        s.nextInt();
                    }
                }
                for (int k = 0; k < 5; k++) { // reach end of row
                    s.nextInt();
                } 
                for (int l = 0; l < 5 * 707; l++) { // skip 5 rows
                    s.nextInt();
                } 
            }
            s.close();
        } 
    }
} 