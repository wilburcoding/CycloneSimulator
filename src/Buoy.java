

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
class Buoy implements Serializable {
    double sustainedWinds = 0;
    double gusts=0;
    double waveheight;
    int trend;
    int x;
    int number ;
    ArrayList<Double> windHistory = new ArrayList<>();
    ArrayList<Double> gustHistory = new ArrayList<>();
    int y;
    int i = 0;
    public int randomGenerator(int minimum, int maximum) {
        Random r = new Random();
        return r.nextInt((maximum - minimum) + 1) + minimum;

    }
    private double calcSigWave(double windspeed) {
        final double gravity = 9.80665;
        double fetchlength=4;
        double height = Math.pow(windspeed,2)*Math.pow(0.283*Math.tanh(0.0125*((gravity*fetchlength)/Math.pow(windspeed,2))),0.42)/gravity;
        height = height * 3.2808399;
        return (height);
    }
    public ArrayList<Double> getWindHistory() {
        return windHistory;
    }

    public ArrayList<Double> getGustHistory() {
        return gustHistory;
    }

    public int getNumber() {
        return number;
    }

    public double myRandom(double min, double max) {
        Random r = new Random();

        return (r.nextInt((int)((Math.abs(max)-Math.abs(min))*10+1))+Math.abs(min)*10) / 10.0;
    }
    public Buoy() {
        x = randomGenerator(0,1000);
        y = randomGenerator(0,500);
        sustainedWinds = randomGenerator(5,20);
        number = randomGenerator(1000,9999);
    }

    public void updateData(ArrayList<Storm> data) {
        if (gustHistory.size() > 150) {
            gustHistory.remove(0);
            windHistory.remove(0);
        }
        if (i < 1) {
            i++;
        } else {
            i = 0;
            double highestWinds = 0;
            double highestGusts = 0;
            if (!data.isEmpty()) {

                for (Storm storm : data) {
                    //((x2-x1)²+(y2-y1)²)

                    double winds=sustainedWinds;
                    double gust;
                    double distance = Math.sqrt((y - storm.getY()) * (y - storm.getY()) + (x - storm.getX()) * (x - storm.getX()));


                    if (distance < 20) {
                        winds = storm.getWinds() / myRandom(1, 2);

                    } else {
                        if (distance < 35) {
                            winds = storm.getWinds() / myRandom(1.5, 2.5);

                        } else {
                            if (distance < 47) {
                                winds = storm.getWinds() / myRandom(2, 3);

                            } else {
                                if (distance < 59) {
                                    winds = storm.getWinds() / myRandom(2.5, 3.5);

                                } else {
                                    if (distance < 70) {
                                        winds = storm.getWinds() / myRandom(3, 4);

                                    } else {
                                        if (trend == 2) {
                                            winds+=myRandom(-0.5, 2);
                                        } else if (trend == 1) {
                                            winds+=myRandom(-1,1.3);
                                        } else if (trend == 0) {
                                            winds+=myRandom(-0.5,0.5);
                                        } else if (trend == -1) {
                                            winds-=myRandom(-1,1.3);
                                        } else if (trend == -2) {
                                            winds-=myRandom(-0.5,2);
                                        }
                                        if (randomGenerator(0,10) < 4) {
                                            trend=randomGenerator(-2,2);

                                        }
                                        if (winds > 25) {
                                            trend = -2;
                                        }
                                        if (winds < 4) {
                                            trend = 2;
                                        }
                                        winds = (double) Math.round(winds * 10) /10;
                                    }
                                }
                            }
                        }


                    }
                    gust = Math.round(winds * myRandom(1.3, 1.7));
                    if (winds > highestWinds) {
                        highestWinds = winds;
                        highestGusts = gust;
                    }

                }
                sustainedWinds = Math.round(highestWinds);
                windHistory.add((double) Math.round(sustainedWinds));
                gusts = Math.round(highestGusts);
                waveheight=Math.round(calcSigWave(sustainedWinds)*10.0)/10.0;

                gustHistory.add((double) Math.round(gusts));
            } else {
                if (trend == 2) {
                    sustainedWinds+=myRandom(-0.5, 2);
                } else if (trend == 1) {
                    sustainedWinds+=myRandom(-1,1.3);
                } else if (trend == 0) {
                    sustainedWinds+=myRandom(-0.5,0.5);
                } else if (trend == -1) {
                    sustainedWinds-=myRandom(-1,1.3);
                } else if (trend == -2) {
                    sustainedWinds-=myRandom(-0.5,2);
                }
                if (myRandom(0,1) < 0.4) {
                    trend+=randomGenerator(-1,1);
                    if (trend > 2) {
                        trend = 2;
                    }
                    if (trend < -2) {
                        trend = -2;
                    }


                }
                if (sustainedWinds > 25) {
                    trend = -2;
                }
                if (sustainedWinds < 4) {
                    trend = 2;
                }
                sustainedWinds = (double) Math.round(sustainedWinds * 10) /10;
                windHistory.add((double) Math.round(sustainedWinds));
                gusts = Math.round(sustainedWinds * myRandom(1.3, 1.7));
                gustHistory.add((double) Math.round(gusts));
                waveheight=Math.round(calcSigWave(sustainedWinds)*10.0)/10.0;

            }

        }
    }

    public double getWaveheight() {
        return waveheight;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public double getSustainedWinds() {
        return sustainedWinds;
    }

    public double getGusts() {
        return gusts;
    }
}
