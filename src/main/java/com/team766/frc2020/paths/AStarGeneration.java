package com.team766.frc2020.paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException; 

import com.team766.frc2020.paths.InputMapArray;
import com.team766.frc2020.paths.AStarNode;


    
public class AStarGeneration {
    final private static int width = 707/6;
    final private static int height = 1384/6;
    
    private ArrayList<AStarNode> openList = new ArrayList<AStarNode>();
    private ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();
    private boolean done = false;

    private static AStarNode[][] nodeMap = new AStarNode[height][width];
     
    public static void main(String[] args) throws IOException{
        InputMapArray.Reader.generateMapArray();
        InputMapArray.Reader.printArray();

        initEmptyNodes();
        ArrayList<AStarNode> path = findPath(84, 514, 621, 929);

        // can check the path in desmos
        for (int i = 0; i < path.size(); i++) {
            System.out.println("(" + path.get(i).getxPosition() + ", " + path.get(i).getyPosition() + ")");
        }
    }

    private static void initEmptyNodes() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nodeMap[i][j] = new AStarNode(j, i);
            }
        }
    }

    private AStarNode getNode(int xPosition, int yPosition) {
        return nodeMap[yPosition][xPosition];
    }

    public final ArrayList<AStarNode> findPath(int startX, int startY, int endX, int endY) {
        openList.add(nodeMap[startX][startY]); // add starting node to open list

        AStarNode current;
        while (!done) {
            current = lowestFInOpen(); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if ((current.getxPosition() == endX)
                    && (current.getyPosition() == endY)) { // found goal
                return calcPath(nodeMap[startX][startY], current);
            }

            // for all adjacent nodes:
            ArrayList<AStarNode> adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                AStarNode currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) { // node is not in openList
                    currentAdj.setPrevious(current); // set current node as previous for this node
                    currentAdj.sethCosts(nodeMap[endX][endY]); // set h costs of this node (estimated costs to goal)
                    currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    openList.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) { // costs from current node are cheaper than previous costs
                        currentAdj.setPrevious(current); // set current node as previous for this node
                        currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    }
                }
            }

            if (openList.isEmpty()) { // no path exists
                return new ArrayList<AStarNode>(); // return empty list
            }
        }
        return null; // unreachable
    }

    // trace the nodes from end to beginning to make the path
    private ArrayList<AStarNode> calcPath(AStarNode start, AStarNode goal) {
        ArrayList<AStarNode> path = new ArrayList<AStarNode>();

        AStarNode curr = goal;
        boolean isDone = false;
        while (!isDone) {
            path.add(0, curr);
            curr = (AStarNode) curr.getPrevious();

            if (curr.equals(start)) {
                isDone = true;
            }
        }
        return path;
    }

    private AStarNode lowestFInOpen() {
        AStarNode cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getfCosts() < cheapest.getfCosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private ArrayList<AStarNode> getAdjacent(AStarNode node) {
        int x = node.getxPosition();
        int y = node.getyPosition();
        ArrayList<AStarNode> adj = new ArrayList<AStarNode>();

        AStarNode temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(false);
                adj.add(temp);
            }
        }

        if (x < width - 1) {
            temp = this.getNode((x + 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(false);
                adj.add(temp);
            }
        }

        if (y < height - 1) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(false);
                adj.add(temp);
            }
        }

        // diagonal nodes also:

        if (x < width - 1 && y < height - 1) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y < height - 1) {
            temp = this.getNode((x - 1), (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(true);
                adj.add(temp);
            }
        }

        if (x < width - 1 && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonally(true);
                adj.add(temp);
            }
        }
        
        return adj;
    }


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
}
