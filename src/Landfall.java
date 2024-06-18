public class Landfall {
    public Land land;
    public Storm storm;
    public double intensity;
    public Landfall(Land land, Storm storm, double intensity) {
        this.land = land;
        this.storm = storm;
        this.intensity = intensity;
    }

    public double getIntensity() {
        return intensity;
    }

    public Land getLand() {
        return land;
    }

    public Storm getStorm() {
        return storm;
    }
}
