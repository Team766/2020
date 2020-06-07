package com.team766.frc2020.paths;

import java.io.DataInputStream; 
import java.io.FileInputStream; 
import java.io.IOException; 
  
public class Input 
{ 
    public static boolean[][] imageBoolArray = new boolean[1384][707];
    static class Reader 
    { 
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
  
        public static void main(String[] args) throws IOException 
        { //978488 ints
            Reader s = new Reader(); 
            for (int i = 0; i < 1384; i++) {
                for (int j = 0; j < 707; j++) {
                    if (s.nextInt() == 1) {
                        imageBoolArray[i][j] = true;
                    } else {
                        imageBoolArray[i][j] = false;
                    }
                }
                // if (i % 13 == 0) {
                //     System.out.println("reading map " + i * 707 * 100 / 978488 + " percent done");
                // }
            }
            s.close();
        } 
    }
} 