package com.team766.frc2020.paths;

import com.team766.frc2020.paths.InputMapArray;

public class AStarNode {
    protected static final int BASICMOVEMENTCOST = 10;
    protected static final int DIAGONALMOVEMENTCOST = 14;

    private int xPosition;
    private int yPosition;
    private boolean walkable;
    private AStarNode previous;
    private boolean diagonally;

    private int gCosts;
    private int hCosts;

    public AStarNode() {};

    public AStarNode(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.walkable = InputMapArray.Reader.isWalkable(xPosition, yPosition);
    };

    public boolean isDiagonally() {
        return diagonally;
    }

    public void setIsDiagonally(boolean isDiagonally) {
        this.diagonally = isDiagonally;
    }

    public void setCoordinates(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public AStarNode getPrevious() {
        return previous;
    }

    public void setPrevious(AStarNode previous) {
        this.previous = previous;
    }

    public int getfCosts() {
        return gCosts + hCosts;
    }

    public int getgCosts() {
        return gCosts;
    }

    private void setgCosts(int gCosts) {
        this.gCosts = gCosts;
    }

    public void setgCosts(AStarNode previousAbstractNode, int basicCost) {
        setgCosts(previousAbstractNode.getgCosts() + basicCost);
    }

    public void setgCosts(AStarNode previousAbstractNode) {
        if (diagonally) {
            setgCosts(previousAbstractNode, DIAGONALMOVEMENTCOST);
        } else {
            setgCosts(previousAbstractNode, BASICMOVEMENTCOST);
        }
    }

    public int calculategCosts(AStarNode previousAbstractNode) {
        if (diagonally) {
            return (previousAbstractNode.getgCosts()
                    + DIAGONALMOVEMENTCOST);
        } else {
            return (previousAbstractNode.getgCosts()
                    + BASICMOVEMENTCOST);
        }
    }

    public int calculategCosts(AStarNode previousAbstractNode, int movementCost) {
        return (previousAbstractNode.getgCosts() + movementCost);
    }

    public int gethCosts() {
        return hCosts;
    }

    protected void sethCosts(int hCosts) {
        this.hCosts = hCosts;
    }

    public String toString() {
        return "(" + getxPosition() + ", " + getyPosition() + "): h: "
                + gethCosts() + " g: " + getgCosts() + " f: " + getfCosts();
    }

    public String toStringDesmos() {
        return "(" + 6*getxPosition() + ", " + -1*6*getyPosition() + ")"; // multiply by 6 to get back into inch coord system that will integrate directly into the path grapher GUI. -1*y because y=0 is at the top of the frame for these calculations. Inverted to graph correctly in desmos.
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AStarNode other = (AStarNode) obj;
        if (this.xPosition != other.xPosition) {
            return false;
        }
        if (this.yPosition != other.yPosition) {
            return false;
        }
        return true;
    }

    public void sethCosts(AStarNode endNode) {
        this.sethCosts((Math.abs(this.getxPosition() - endNode.getxPosition())
                + Math.abs(this.getyPosition() - endNode.getyPosition()))
                * BASICMOVEMENTCOST);
    }
}