

import java.awt.*;
import java.io.Serializable;

class Coordinate implements Serializable {
    double x;
    double y;
    Color color;

    public double getWind() {
        return wind;
    }

    public void setWind( double wind) {
        this.wind = wind;
    }

    double wind;
    Coordinate(double xCoordinate, double yCoordinate, Color color) {
        x = xCoordinate;
        y = yCoordinate;
        this.color = color;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}
