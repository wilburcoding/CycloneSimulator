import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Land implements Serializable {
    static boolean FindPoint(double x1, double y1, double x2, double y2, double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }
    public int yTopBound;
    public int xLeftBound;
    public int xRightBound;
    public int xSize;
    public int wait=0;
    public double preparedness;
    public int ySize;
    public double damageChange=0;
    public double damages=0;
    public String landName;
    public int yBottomBound;

    private static final long serialVersionUID = 5512234731442983181L;
    public void setThreat(int threat) {
        if (threat < 0) {
            threat = 0;
        }
        this.threat = threat;
    }



    public int getThreat() {
        return threat;
    }

    private int threat;
    private double windThreat;
    private double surgeThreat;
    private double rainThreat;


    HashMap<Storm, Double> stormsAffected = new HashMap<>();
    private int randomGenerator(int minimum, int maximum) {
        Random r = new Random();
        return r.nextInt((maximum - minimum) + 1) + minimum;

    }

    public HashMap<Storm, Double> getStormsAffected() {
        return stormsAffected;
    }

    public double getWindThreat() {
        return windThreat;
    }

    public void setWindThreat(double windThreat) {
        this.windThreat = windThreat;
    }

    public double getSurgeThreat() {
        return surgeThreat;
    }

    public void setSurgeThreat(double surgeThreat) {
        this.surgeThreat = surgeThreat;
    }

    public double getRainThreat() {
        return rainThreat;
    }

    public void setRainThreat(double rainThreat) {
        this.rainThreat = rainThreat;
    }

    public String getLandName() {
        return landName;
    }
    public double rNum(double min, double max) {
        return (new Random().nextInt((int) ((Math.abs(max) - Math.abs(min)) * 10 + 1)) + Math.abs(min) * 10) / 10.0;
    }
    public void clearDamages() {
        stormsAffected.clear();
    }
    public Land(int landnumber) {
        landName="Country #" + (landnumber+1);
        int left = randomGenerator(0,900);
        int top = randomGenerator(0,500);

        ySize = randomGenerator(10,150);
        xSize = randomGenerator(10,150);
        yTopBound = top;
        xLeftBound = left;
        yBottomBound = top+ySize;
        preparedness = rNum(0.7, 1.3);

        xRightBound = left+xSize;
    }
    public double getPreparedness() {
        return preparedness;
    }
    public void update() {
        yBottomBound = yTopBound+ySize;

        xRightBound = xLeftBound+xSize;
    }
    public Land(int x,int y, int xSize,int ySize, String name) {
        landName=name;
        this.ySize =ySize;
        this.xSize = xSize;
        yTopBound = y;
        xLeftBound = x;
        yBottomBound = yTopBound+ySize;
        xRightBound = xLeftBound+xSize;
    }
    public int getX() {
        return xLeftBound;
    }
    public int getY() {
        return yTopBound;
    }
    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }
    public boolean inLandBorder(double x, double y, int size) {
        double border = Math.round(size/2.0) ;
        if (border < 4) {
            border =4;
        }
        return FindPoint(xLeftBound+border, yTopBound+border, xRightBound-(border), yBottomBound-border,x,y);
    }

    public boolean inLand(double x, double y) {

        return FindPoint(xLeftBound, yTopBound, xRightBound, yBottomBound,x,y);

    }
    public ArrayList<Coordinate> getCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(xLeftBound, yTopBound, Color.GREEN));
        coordinates.add(new Coordinate(xRightBound, yBottomBound, Color.GREEN));
        return coordinates;
    }
    public void addDamages(double damagesToAdd, Storm storm) {
        damages+=damagesToAdd;
        damageChange=damagesToAdd;
        if (stormsAffected.containsKey(storm)) {
            stormsAffected.replace(storm, stormsAffected.get(storm), stormsAffected.get(storm)+damagesToAdd);
        } else {

            stormsAffected.put(storm, damagesToAdd);
        }
        wait=0;
    }
    public double getDamageChange() {
        return damageChange;
    }
    public double getDamages() {

        return damages;
    }
    public void noDamage() {
        if (wait > 10) {
            damageChange = 0;
            wait=0;
        } else {
            wait++;
        }
    }

}
