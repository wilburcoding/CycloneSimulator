import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

class Storm implements Serializable {
    double winds;
    double x;
    int damages = 0;
    double y;
    int landfalls = 0;
    double ace = 0;
    double sixMax = 0;
    double offSet;
    double wChange = 0;
    double damageChange = 0;
    double yoffset;
    double xoffset;
    String headline = "...New Tropical Wave Forms...";
    double width;
    double movementSpeed;
    String name;
    double oldShear = 0.0;
    double height;
    JMenuItem menuItem;
    double chanceform = 0;
    boolean dead;
    double oldMovementSpeed = 3;
    double oldDamages;
    boolean rapidIntensification = false;
    double windPeak;
    double xChange = 0;
    double yChange = 0;
    StormType stormType;
    double stormSize;
    boolean land = false;
    boolean formed = false;
    ArrayList<Coordinate> history = new ArrayList<>();
    ArrayList<Integer> windHistory = new ArrayList<>();
    int startday;
    int ewrc = -1;

    public void setMenuItem(JMenuItem menuItem) {
        this.menuItem = menuItem;

    }

    public JMenuItem getMenuItem() {
        return menuItem;
    }

    public Storm(int startingWindspeed, int numLows, int day) {
        winds = startingWindspeed;
        wChange = 0;
        stormSize = myRandom(12, 25);
        windHistory.add(startingWindspeed);
        double random = myRandom(0, 10);
        if (day > 212 && day < 320) {
            if (random < 6) {
                x = myRandom(300, 950);
                y = myRandom(250, 500);
            } else {
                if (random < 8.3) {
                    x = myRandom(300, 600);
                    y = myRandom(310, 500);
                } else {
                    if (random < 9.1) {
                        x = myRandom(250, 500);
                        y = myRandom(380, 480);
                    } else {
                        x = myRandom(200, 500);
                        y = myRandom(420, 460);
                    }
                }
            }
        } else {
            if (random < 6) {
                x = myRandom(300, 950);
                y = myRandom(260, 500);
            } else {
                if (random < 8.3) {
                    x = myRandom(300, 600);
                    y = myRandom(340, 500);
                } else {
                    x = myRandom(200, 500);
                    if (random < 9.1) {
                        y = myRandom(400, 480);
                    } else {
                        y = myRandom(420, 460);
                    }
                }
            }
        }
        dead = false;
        if (y < 300) {
            xoffset = myRandom(0, 20) - 10;
            yoffset = myRandom(0, 17) - 7;
        } else {
            if (y < 350) {
                xoffset = myRandom(0, 16) - 8;
                yoffset = myRandom(0, 15) - 6;
            } else {
                if (y < 400) {
                    xoffset = myRandom(0, 14) - 7;
                } else {
                    xoffset = myRandom(0, 10) - 5;
                }
                yoffset = myRandom(0, 13) - 5;
            }
        }
        offSet = myRandom(0, 0.09);
        this.height = 500;
        this.width = 1000;
        history.clear();
        windPeak = 0;
        startday = day;
        stormType = new StormType(0);
        numLows++;
        if (numLows < 10) {
            name = "0" + numLows + "L";
        } else {
            name = numLows + "L";
        }


    }

    public ArrayList<Integer> getWindHistory() {
        return windHistory;
    }

    public int getStartDay() {
        return startday;
    }

    public double myRandom(double min, double max) {
        Random r = new Random();
        if (min <= 0) {
            min = 0.0001;
        }
        if (max <= 0) {
            max = 0.0001;
        }
        if (max < min) {
            double newmin = max;
            max = min;
            min = newmin;
        }

        return (r.nextInt((int) ((Math.abs(max) - Math.abs(min)) * 10 + 1)) + Math.abs(min) * 10) / 10.0;
    }

    public int randomGenerator(int minimum, int maximum) {
        Random r = new Random();
        return r.nextInt((maximum - minimum) + 1) + minimum;

    }

    public boolean isDead() {
        return dead;
    }

    public String getName() {
        return name;
    }

    public String getHeadline() {
        return headline;
    }

    public StormType getStormType() {
        return stormType;
    }

    public double getStormSize() {
        return stormSize;
    }

    public int getDamages() {
        return damages;
    }

    public int getLandfalls() {
        return landfalls;
    }

    public double getDamageChange() {
        return damageChange;
    }

    public void moveForward(double shear, boolean onland) {
        if ((x > 10) && (y > 10) && (winds > 10)) {
            double oldX = x;
            double oldY = y;
            double oldW = winds;
            if (y < 451) {

                if (Math.abs(xoffset) < (250 - winds) / 30.0 + ((500 - y) - 50) / 45) {
                    if (randomGenerator(0, 1) == 1) {
                        xoffset = xoffset - myRandom(0, 1.3);
                    } else {
                        xoffset = xoffset + myRandom(0, 1.3);
                    }
                } else {
                    if (xoffset < 0) {
                        xoffset = xoffset + 1;
                    } else {
                        xoffset = xoffset - 1;
                    }
                }
            } else {
                if (Math.abs(xoffset) < (250 - winds) / 30.0) {
                    if (randomGenerator(0, 1) == 1) {
                        xoffset = xoffset - myRandom(0, 0.9);
                    } else {
                        xoffset = xoffset + myRandom(0, 0.9);
                    }
                } else {
                    if (xoffset < 0) {
                        xoffset = xoffset + 1;
                    } else {
                        xoffset = xoffset - 1;
                    }
                }
            }
            if (y < 451) {
                if (Math.abs(yoffset) < (250 - winds) / 35.0 + ((500 - y) - 50) / 45) {
                    if (x < 50) {
                        yoffset = yoffset + myRandom(2, 4);
                    } else {
                        if (x < 100) {
                            yoffset = yoffset + myRandom(0, 1.3);
                        } else {
                            if (randomGenerator(0, 1) == 1) {
                                yoffset = yoffset - myRandom(0, 1.2);
                            } else {
                                yoffset = yoffset + myRandom(0, 1.2);
                            }
                        }
                    }
                } else {
                    if (yoffset < 0) {
                        yoffset = yoffset + 1;
                    } else {
                        yoffset = yoffset - 1;
                    }
                }
            } else {
                if (Math.abs(yoffset) < (250 - winds) / 40.0) {
                    if (x < 50) {
                        yoffset = yoffset + myRandom(2, 4);
                    } else {
                        if (x < 100) {
                            yoffset = yoffset + myRandom(0, 2);
                        } else {
                            if (randomGenerator(0, 1) == 1) {
                                yoffset = yoffset - myRandom(0, 1);
                            } else {
                                yoffset = yoffset + myRandom(0, 1);
                            }
                        }
                    }
                } else {
                    if (yoffset < 0) {
                        yoffset = yoffset + 1;
                    } else {
                        yoffset = yoffset - 1;
                    }
                }
            }
            if (y < 370 && !formed && !land) {
                winds = winds + ((500 - y) / 30.0);
            }
            if (y > 490) {
                yoffset = yoffset + 3;
            }
            x = x - (xoffset/1.3);
            y = y - (yoffset/1.3);

            double startingWinds = winds;
            if (!rapidIntensification) {
                if (myRandom(0, 2) < 0.6 && y > 300) {
                    rapidIntensification = true;
                }
            } else {
                winds = winds + myRandom(3, 10);
                if (myRandom(0, 1) < 0.3 || y < 300 || myRandom(winds, 400) > 350) {
                    rapidIntensification = false;
                }
            }

            if (onland) {
                oldDamages = damages;
                rapidIntensification = false;
                if (winds > 50) {
                    winds = winds - myRandom(winds / 9, winds / 5);
                } else {
                    winds = winds - myRandom(0, 5);
                }
                if (winds < 40) {
                    damages = damages + (int) myRandom(winds / 1.5, winds);
                } else {
                    if (winds < 60) {
                        damages = damages + (int) myRandom(winds / 0.6, winds / 0.8);
                    } else {
                        if (winds < 80) {
                            damages = damages + (int) myRandom(winds / 0.2, winds / 0.4);
                        } else {
                            if (winds < 100) {
                                damages = damages + (int) myRandom(winds / 0.01, winds / 0.1);
                            } else {
                                if (winds < 120) {
                                    damages = damages + (int) myRandom(winds / 0.04, winds / 0.02);
                                } else {
                                    damages = damages + (int) myRandom(winds / 0.009, winds / 0.001);
                                }
                            }
                        }
                    }
                }
                damageChange = damages - oldDamages;

            }

            int onLand = 0;
            if (land != onland) {
                if (!land) {
                    onLand = 1;
                } else {
                    onLand = -1;
                }
            }

            land = onland;
            oldShear = shear;

            if (winds < 70) {
                winds = winds - myRandom(shear / 7.0, shear / 3.0);
            } else {
                if (winds < 90) {
                    winds = winds - myRandom(shear / 6.6, shear / 2.6);
                } else {
                    if (winds < 110) {
                        winds = winds - myRandom(shear / 6.0, shear / 2.4);
                    } else {
                        if (winds < 140) {
                            winds = winds - myRandom(shear / 5.4, shear / 2.2);
                        } else {
                            winds = winds - myRandom(shear / 5.4, shear / 2.1);
                        }
                    }
                }
            }

            shear = shear / 2;
            double cX = x;
            double cY = y;
            if (y > (height * (0.9 + offSet))) {
                y = y - myRandom(0, 5);
                x = x - myRandom(1, 8);
                stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(5, 14)));
            } else {
                if (y > (height * (0.8 + offSet))) {
                    y = y - myRandom(1, 8);
                    x = x - myRandom(1, 10);
                    stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(5, 12)));
                } else {
                    if (y > (height * (0.7 + offSet))) {
                        y = y - myRandom(1, 8);
                        x = x - myRandom(2, 10);
                        stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(4, 10)));
                    } else {
                        if (y > (height * (0.6 + offSet))) {
                            y = y - myRandom(1, 9);
                            x = x - myRandom(2, 7);
                            stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(2, 8)));
                        } else {
                            if (y > (height * (0.5 + offSet))) {
                                y = y - myRandom(1, 8);
                                x = x - myRandom(1, 4);
                                stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(1, 5)));
                            } else {
                                if (y > (height * (0.4 + offSet))) {
                                    y = y - myRandom(1, 7);
                                    x = x + myRandom(1, 2);
                                    stormType.setPercentEx((stormType.getPercentEx() - randomGenerator(0, 2)));
                                } else {
                                    if (y > (height * (0.3 + offSet))) {
                                        y = y - myRandom(1, 5);
                                        x = x + myRandom(0, 3);
                                        stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(2, 5)));
                                    } else {
                                        if (y > (height * (0.2 + offSet))) {
                                            y = y - myRandom(1, 5);
                                            x = x + myRandom(1, 5);
                                            stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(4, 9)));
                                        } else {
                                            if (y > (height * (0.1 + offSet))) {
                                                y = y - myRandom(0, 5);
                                                x = x + myRandom(2, 16);
                                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(5, 12)));
                                            } else {
                                                y = y - myRandom(0, 5);
                                                x = x + myRandom(2, 16);
                                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(10, 25)));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            double xRChange = x-cX;
            while (Math.abs(xRChange) > 7) {
                if (xRChange < 0) {
                    x+=1;
                } else {
                    x-=1;
                }
                xRChange = x-cX;

            }
            double yRChange = y-cY;
            while (Math.abs(yRChange) > 7) {
                if (yRChange < 0) {
                    y+=1;
                } else {
                    y-=1;
                }
                yRChange = y-cY;

            }
            if (stormType.getStormType() == 1) {
                winds = winds + myRandom(1, 2);
            }

            if (winds > 140) {
                if (y > 450) {
                    if (myRandom(1, 2) < 1.8) {
                        winds = winds + myRandom(1, 2.5);
                    } else {
                        winds = winds - myRandom(1, 3);
                    }
                } else {
                    if (y > 400) {
                        if (myRandom(1, 2) < 1.73) {
                            winds = winds + myRandom(1, 2.5);
                        } else {
                            winds = winds - myRandom(1, 3);
                        }
                    } else {
                        if (y > 350) {
                            if (myRandom(1, 2) < 1.66) {
                                winds = winds + myRandom(1, 2.1);
                            } else {
                                winds = winds - myRandom(1, 3.2);
                            }
                        } else {
                            if (y > 300) {
                                if (myRandom(1, 2) < 1.6) {
                                    winds = winds + myRandom(1, 1);
                                } else {
                                    winds = winds - myRandom(1, 3.5);
                                }
                            } else {
                                if (y > 250) {
                                    if (myRandom(1, 2) < 1.57) {
                                        winds = winds + myRandom(1, 1);
                                    } else {
                                        winds = winds - myRandom(1, 4);
                                    }
                                } else {
                                    if (y > 200) {
                                        if (myRandom(1, 2) < 1.51) {
                                            winds = winds + myRandom(1, 1);
                                        } else {
                                            if (winds > 130) {
                                                winds = winds - myRandom(1, 8 - shear);
                                            } else {
                                                winds = winds - myRandom(1, 5.7);
                                            }
                                        }
                                    } else {
                                        if (y > 150) {
                                            if (myRandom(1, 2) < 1.48) {
                                                winds = winds + myRandom(1, 1);
                                            } else {
                                                if (winds > 130) {
                                                    winds = winds - myRandom(2, 8 - shear);
                                                } else {
                                                    winds = winds - myRandom(3, 8);
                                                }
                                            }
                                        } else {
                                            if (y > 100) {
                                                if (myRandom(1, 2) < 1.42) {
                                                    winds = winds + myRandom(1, 1);
                                                } else {
                                                    if (winds > 130) {
                                                        winds = winds - myRandom(3, 10 - shear);
                                                    } else {
                                                        winds = winds - myRandom(3, 8);
                                                    }
                                                }
                                            } else {
                                                if (y > 50) {
                                                    if (myRandom(1, 2) < 1.38) {
                                                        winds = winds + myRandom(1, 2.2);
                                                    } else {
                                                        if (winds > 130) {
                                                            winds = winds - myRandom(3, 12 - shear);
                                                        } else {
                                                            winds = winds - myRandom(3, 9);
                                                        }

                                                    }
                                                } else {
                                                    if (myRandom(1, 2) < 1.32) {
                                                        winds = winds + myRandom(1, 2);
                                                    } else {
                                                        if (winds > 130) {
                                                            winds = winds - myRandom(8, 12);
                                                        } else {
                                                            winds = winds - myRandom(3, 9);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (y > 450) {
                    if (myRandom(1, 2) < 1.8) {
                        winds = winds + myRandom(0, 8 - shear);
                    } else {
                        winds = winds - myRandom(0, 3);
                    }
                } else {
                    if (y > 400) {
                        if (myRandom(1, 2) < 1.73) {
                            winds = winds + myRandom(0, 7 - shear);
                        } else {
                            winds = winds - myRandom(0, 3.2);
                        }
                    } else {
                        if (y > 350) {
                            if (myRandom(1, 2) < 1.66) {
                                winds = winds + myRandom(0, 6 - shear);
                            } else {
                                winds = winds - myRandom(0, 3.7);
                            }
                        } else {
                            if (y > 300) {
                                if (myRandom(1, 2) < 1.6) {
                                    winds = winds + myRandom(0, 5 - shear);
                                } else {
                                    winds = winds - myRandom(0, 4);
                                }
                            } else {
                                if (y > 250) {
                                    if (myRandom(1, 2) < 1.57) {
                                        winds = winds + myRandom(0, 5 - shear);
                                    } else {
                                        winds = winds - myRandom(0, 4.3);
                                    }
                                } else {
                                    if (y > 200) {
                                        if (myRandom(1, 2) < 1.51) {
                                            winds = winds + myRandom(0, 3.5);
                                        } else {
                                            if (winds > 90) {
                                                winds = winds - myRandom(0, 4 - shear);
                                            } else {
                                                winds = winds - myRandom(0, 1.42);
                                            }
                                        }
                                    } else {
                                        if (y > 150) {
                                            if (myRandom(1, 2) < 1.48) {
                                                winds = winds + myRandom(0, 2.9);
                                            } else {
                                                if (winds > 90) {
                                                    winds = winds - myRandom(0, 4 - shear);
                                                } else {
                                                    winds = winds - myRandom(0, 4.9);
                                                }
                                            }
                                        } else {
                                            if (y > 100) {
                                                if (myRandom(1, 2) < 1.42) {
                                                    winds = winds + myRandom(0, 2.6);
                                                } else {
                                                    if (winds > 90) {
                                                        winds = winds - myRandom(0, 5 - shear);
                                                    } else {
                                                        winds = winds - myRandom(0, 5.5);
                                                    }
                                                }
                                            } else {
                                                if (y > 50) {
                                                    if (myRandom(1, 2) < 1.38) {
                                                        winds = winds + myRandom(0, 2.2);
                                                    } else {
                                                        if (winds > 90) {
                                                            winds = winds - myRandom(0, 6 - shear);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);
                                                        }

                                                    }
                                                } else {
                                                    if (myRandom(1, 2) < 1.32) {
                                                        winds = winds + myRandom(0, 2 - shear);
                                                    } else {
                                                        if (winds > 90) {
                                                            winds = winds - myRandom(0, 6 - shear);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            history.add(new Coordinate(x, y, getCategory(winds)));
            if (history.size() > 1) {
                xChange = x-history.get(history.size()-2).getX();
                yChange = y-history.get(history.size()-2).getY();

            }
            if (history.size() > 2) {
                xChange = (xChange+(x-history.get(history.size()-2).getX()))/2;
                yChange = (yChange+(y-history.get(history.size()-2).getY()))/2;
            }
            if (winds > windPeak) {
                windPeak = winds;
            }
            if (winds > 37 && !formed) {
                formed = true;
            }
            movementSpeed = (Math.sqrt(Math.pow(oldX - x, 2) + Math.pow(oldY - y, 2)) + oldMovementSpeed) / 2;
            int oldewrc = ewrc;
            if (winds > 100 && ewrc == -1) {
                if (randomGenerator((int) winds - 100, 3000) > 2900) {
                    ewrc = randomGenerator(4, 8);
                }
            } else {
                if (ewrc >= 0) {
                    ewrc -= 1;
                    winds += myRandom(-10, 1);
                    if (winds - windHistory.get(windHistory.size()-1) > 0) {
                        winds-=myRandom(4,7);
                    }
                }
            }
            if (winds < 70) {
                ewrc = -1;
            }
            if (!formed) {
                chanceform = (5 * winds) / ((shear/12.0) + 1.4);
            }
            if (wChange != 0) {
                while (Math.abs(winds-oldW) > 10) {
                    if (winds-oldW > 0) {
                        winds-=1;
                    } else {
                        winds+=1;
                    }
                }
            }
            wChange = winds - oldW;
            if (history.size() % 2 == 0) {
                if (sixMax < winds) {
                    sixMax = winds;
                }
                if (sixMax > 39) {
                    ace+=Math.pow(sixMax * 0.868976,2)/10000.0;

                }
                ace = (double) Math.round(ace * 1000) /1000;
            } else {
                sixMax = winds;
            }

            if (oldewrc != ewrc) {
                if (ewrc >= 0 && oldewrc == -1) {
                    headline = "...Starting EWRC...";
                    stormSize = stormSize - myRandom(0, 0.2);
                } else if (ewrc >= 0) {
                    headline = "...Undergoing EWRC...";
                } else {
                    headline = "...Completing EWRC...";
                    stormSize = stormSize + myRandom(0, 0.2);

                }
            } else {
                if (ewrc >= 0) {
                    headline = "...Becomes Extratropical...";
                    stormSize = stormSize + myRandom(0, 0.2);
                } else {
                    if (onLand == 1) {
                        headline = "...Makes Landfall...";
                        stormSize = stormSize - myRandom(1, 3);
                        if (windPeak > 29) {
                            landfalls++;
                        }
                    } else {
                        if (onLand == -1) {
                            headline = "...Returns to Sea...";
                            stormSize = stormSize + myRandom(0, 1);
                        } else {
                            if (startingWinds > winds) {
                                double difference = startingWinds - winds;
                                if (difference < 3) {
                                    headline = "...Slowly Weakening...";
                                    stormSize = stormSize - myRandom(-0.2, 0.2);
                                } else {
                                    if (difference < 6) {
                                        headline = "...Weakening...";
                                        stormSize = stormSize - myRandom(-0.1, 0.3);
                                    } else {
                                        headline = "...Quickly Weakening...";
                                        stormSize = stormSize - myRandom(0, 0.4);
                                    }
                                }
                            } else {
                                double difference = winds - startingWinds;
                                if (difference < 3) {
                                    headline = "...Slowly Strengthening...";
                                    stormSize = stormSize + myRandom(-0.2, 0.2);
                                } else {
                                    if (difference < 6) {
                                        headline = "...Continuing to Strengthen...";
                                        stormSize = stormSize + myRandom(-0.1, 0.3);
                                    } else {
                                        headline = "...Rapidly Strengthening...";
                                        stormSize = stormSize + myRandom(0, 0.4);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            windHistory.add((int) Math.round(winds));
        } else {
            windHistory.add((int) Math.round(winds));
            dead = true;
            if (winds < 15) {
                headline = "...Last Advisory...";
            } else {
                if (x < 20) {
                    headline = "...Leaves Basin...";
                } else {
                    headline = "......";
                }
            }

        }
        if (winds < 0) {
            winds = 0;
        }
    }

    public double getOldShear() {
        return oldShear;
    }
    public double getChanceForm() {
        return chanceform;
    }
    public double getMovementSpeed() {
        return movementSpeed;
    }



    public ArrayList<Double> getForecastWinds(ArrayList<Land> land) {
        double x = this.x;
        double y = this.y;
        double shear = 0;
        double winds = this.winds;
        double xoffset = this.xoffset;
        double yoffset = this.yoffset;
        StormType stormType = new StormType("ts");
        ArrayList<Double> history = new ArrayList<>();
        history.add(winds);
        double craziness = myRandom(0, 3) - 1;
        for (int i = 0; i < 20; i++) {

            if (winds < 15) {
                break;
            }
            boolean onland = false;
            for (Land singleLand : land) {
                if (singleLand.inLand(x, y)) {
                    onland = true;

                }


            }
            if ((x > 10) && (y > 10) && (winds > 15)) {
                winds = winds - myRandom((Math.sqrt(oldShear) / 4), (Math.sqrt(oldShear)));
                if (Math.abs(xoffset) < (250 - winds) / 20.0) {
                    if (randomGenerator(0, 1) == 1) {
                        xoffset = xoffset - myRandom(0, 1);
                    } else {
                        xoffset = xoffset + myRandom(0, 1);
                    }
                } else {
                    if (xoffset < 0) {
                        xoffset = xoffset + 1;
                    } else {
                        xoffset = xoffset - 1;
                    }
                }
                if (Math.abs(yoffset) < (250 - winds) / 30.0) {
                    if (x < 100) {
                        yoffset = yoffset + myRandom(0, 0.5);
                    } else {
                        if (randomGenerator(0, 1) == 1) {
                            yoffset = yoffset - myRandom(0, 1);
                        } else {
                            yoffset = yoffset + myRandom(0, 1);
                        }
                    }
                } else {
                    if (yoffset < 0) {
                        yoffset = yoffset + 1;
                    } else {
                        yoffset = yoffset - 1;
                    }
                }
                x = x - (xoffset/1.4);
                y = y - yoffset/1.4;
                double cX = x;
                double cY = y;
                if (onland) {

                    if (winds > 50) {
                        winds = winds - myRandom(winds / 9, winds / 5);
                    } else {
                        winds = winds - myRandom(0 + craziness, 2 + craziness);
                    }
                }

                winds = winds - myRandom((Math.sqrt(shear) / 5), (Math.sqrt(shear)));
                if (y > (height * (0.9 + offSet))) {
                    y = y - myRandom(0, 5);
                    x = x - myRandom(1, 10);
                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                } else {
                    if (y > (height * (0.8 + offSet))) {
                        y = y - myRandom(1, 8);
                        x = x - myRandom(1, 12);
                        stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                    } else {
                        if (y > (height * (0.7 + offSet))) {
                            y = y - myRandom(1, 8);
                            x = x - myRandom(2, 12);
                            stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                        } else {
                            if (y > (height * (0.6 + offSet))) {
                                y = y - myRandom(1, 9);
                                x = x - myRandom(2, 9);
                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 2)));
                            } else {
                                if (y > (height * (0.5 + offSet))) {
                                    y = y - myRandom(1, 10);
                                    x = x - myRandom(1, 4);
                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 3)));
                                } else {
                                    if (y > (height * (0.4 + offSet))) {
                                        y = y - myRandom(1, 9);
                                        x = x + myRandom(1, 2);
                                        stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 4)));
                                    } else {
                                        if (y > (height * (0.3 + offSet))) {
                                            y = y - myRandom(1, 7);
                                            x = x + myRandom(0, 3);
                                            stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(1, 4)));
                                        } else {
                                            if (y > (height * (0.2 + offSet))) {
                                                y = y - myRandom(1, 5);
                                                x = x + myRandom(1, 7);
                                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(2, 6)));
                                            } else {
                                                if (y > (height * (0.1 + offSet))) {
                                                    y = y - myRandom(0, 5);
                                                    x = x + myRandom(2, 18);
                                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(3, 8)));
                                                } else {
                                                    y = y - myRandom(0, 5);
                                                    x = x + myRandom(2, 18);
                                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(4, 12)));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                double xRChange = x-cX;
                while (Math.abs(xRChange) > 4) {
                    if (xRChange < 0) {
                        x+=1;
                    } else {
                        x-=1;
                    }
                    xRChange = x-cX;

                }
                double yRChange = y-cY;
                while (Math.abs(yRChange) > 4) {
                    if (yRChange < 0) {
                        y+=1;
                    } else {
                        y-=1;
                    }
                    yRChange = y-cY;

                }
                if (winds > 100) {
                    if (y > 450) {
                        if (myRandom(1, 2) < 1.8) {
                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                        } else {
                            winds = winds - myRandom(1 + craziness, 4 + craziness);
                        }
                    } else {
                        if (y > 400) {
                            if (myRandom(1, 2) < 1.73) {
                                winds = winds + myRandom(1 + craziness, 3 + craziness);
                            } else {
                                winds = winds - myRandom(1 + craziness, 4.2 + craziness);
                            }
                        } else {
                            if (y > 350) {
                                if (myRandom(1, 2) < 1.66) {
                                    winds = winds + myRandom(1 + craziness, 2.5 + craziness);
                                } else {
                                    winds = winds - myRandom(1 + craziness, 4.7 + craziness);
                                }
                            } else {
                                if (y > 300) {
                                    if (myRandom(1, 2) < 1.6) {
                                        winds = winds + myRandom(1 + craziness, 2 + craziness);
                                    } else {
                                        winds = winds - myRandom(1 + craziness, 5 + craziness);
                                    }
                                } else {
                                    if (y > 250) {
                                        if (myRandom(1, 2) < 1.57) {
                                            winds = winds + myRandom(1 + craziness, 2 + craziness);
                                        } else {
                                            winds = winds - myRandom(1 + craziness, 5.3 + craziness);
                                        }
                                    } else {
                                        if (y > 200) {
                                            if (myRandom(1, 2) < 1.51) {
                                                winds = winds + myRandom(1 + craziness, 2 + craziness);
                                            } else {
                                                if (winds > 130) {
                                                    winds = winds - myRandom(1 + craziness, 7 + craziness);
                                                } else {
                                                    winds = winds - myRandom(1 + craziness, 5.7 + craziness);
                                                }
                                            }
                                        } else {
                                            if (y > 150) {
                                                if (myRandom(1, 2) < 1.48) {
                                                    winds = winds + myRandom(1 + craziness, 2 + craziness);
                                                } else {
                                                    if (winds > 130) {
                                                        winds = winds - myRandom(2 + craziness, 8 + craziness);
                                                    } else {
                                                        winds = winds - myRandom(1 + craziness, 8 + craziness);
                                                    }
                                                }
                                            } else {
                                                if (y > 100) {
                                                    if (myRandom(1, 2) < 1.42) {
                                                        winds = winds + myRandom(1 + craziness, 2 + craziness);
                                                    } else {
                                                        if (winds > 130) {
                                                            winds = winds - myRandom(3 + craziness, 8 + craziness);
                                                        } else {
                                                            winds = winds - myRandom(1 + craziness, 8 + craziness);
                                                        }
                                                    }
                                                } else {
                                                    if (y > 50) {
                                                        if (myRandom(1, 2) < 1.38) {
                                                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                                                        } else {
                                                            if (winds > 130) {
                                                                winds = winds - myRandom(3 + craziness, 12 + craziness);
                                                            } else {
                                                                winds = winds - myRandom(1 + craziness, 9 + craziness);
                                                            }

                                                        }
                                                    } else {
                                                        if (myRandom(1, 2) < 1.32) {
                                                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                                                        } else {
                                                            if (winds > 130) {
                                                                winds = winds - myRandom(3 + craziness, 12 + craziness);
                                                            } else {
                                                                winds = winds - myRandom(1 + craziness, 9 + craziness);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (y > 450) {
                        if (myRandom(1, 2) < 1.8) {
                            winds = winds + myRandom(0, 9);
                        } else {
                            winds = winds - myRandom(0, 3);
                        }
                    } else {
                        if (y > 400) {
                            if (myRandom(1, 2) < 1.73) {
                                winds = winds + myRandom(0, 8);
                            } else {
                                winds = winds - myRandom(0, 3.2);
                            }
                        } else {
                            if (y > 350) {
                                if (myRandom(1, 2) < 1.66) {
                                    winds = winds + myRandom(0, 7);
                                } else {
                                    winds = winds - myRandom(0, 3.7);
                                }
                            } else {
                                if (y > 300) {
                                    if (myRandom(1, 2) < 1.6) {
                                        winds = winds + myRandom(0, 7);
                                    } else {
                                        winds = winds - myRandom(0, 4);
                                    }
                                } else {
                                    if (y > 250) {
                                        if (myRandom(1, 2) < 1.57) {
                                            winds = winds + myRandom(0, 6);
                                        } else {
                                            winds = winds - myRandom(0, 4.3);
                                        }
                                    } else {
                                        if (y > 200) {
                                            if (myRandom(1, 2) < 1.51) {
                                                winds = winds + myRandom(0, 5);
                                            } else {
                                                winds = winds - myRandom(0, 1.42);
                                            }
                                        } else {
                                            if (y > 150) {
                                                if (myRandom(1, 2) < 1.48) {
                                                    winds = winds + myRandom(0, 3);
                                                } else {
                                                    winds = winds - myRandom(0, 4.9);
                                                }
                                            } else {
                                                if (y > 100) {
                                                    if (myRandom(1, 2) < 1.42) {
                                                        winds = winds + myRandom(0, 4);
                                                    } else {
                                                        winds = winds - myRandom(0, 5.5);
                                                    }
                                                } else {
                                                    if (y > 50) {
                                                        if (myRandom(1, 2) < 1.38) {
                                                            winds = winds + myRandom(0, 3);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);

                                                        }
                                                    } else {
                                                        if (myRandom(1, 2) < 1.32) {
                                                            winds = winds + myRandom(0, 3);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                history.add(winds);
            }
        }

        return history;
    }

    public ArrayList<Coordinate> getTrackForecast(ArrayList<Land> land) {
        double x = this.x;
        double y = this.y;
        double shear = 0;
        double winds = this.winds;
        double xoffset = this.xoffset;
        double yoffset = this.yoffset;
        StormType stormType = new StormType("ts");
        ArrayList<Coordinate> history = new ArrayList<>();
        Coordinate newCoord = new Coordinate(x, y, getCategory(winds));
        newCoord.setWind(winds);
        history.add(newCoord);
        double craziness = myRandom(0, 3) - 1;
        for (int i = 0; i < 20; i++) {

            if (winds < 15) {
                break;
            }
            boolean onland = false;
            for (Land singleLand : land) {
                if (singleLand.inLand(x, y)) {
                    onland = true;

                }


            }
            if ((x > 10) && (y > 10) && (winds > 15)) {
                winds = winds - myRandom((Math.sqrt(oldShear) / 4), (Math.sqrt(oldShear)));
                if (stormType.getStormType()!=1) {
                    if (Math.abs(xoffset) < (250 - winds) / 20.0) {
                        if (randomGenerator(0, 1) == 1) {
                            xoffset = xoffset - myRandom(0, 1);
                        } else {
                            xoffset = xoffset + myRandom(0, 1);
                        }
                    } else {
                        if (xoffset < 0) {
                            xoffset = xoffset + 1;
                        } else {
                            xoffset = xoffset - 1;
                        }
                    }
                    if (Math.abs(yoffset) < (250 - winds) / 30.0) {
                        if (x < 100) {
                            yoffset = yoffset + myRandom(0, 0.5);
                        } else {
                            if (randomGenerator(0, 1) == 1) {
                                yoffset = yoffset - myRandom(0, 1);
                            } else {
                                yoffset = yoffset + myRandom(0, 1);
                            }
                        }
                    } else {
                        if (yoffset < 0) {
                            yoffset = yoffset + 1;
                        } else {
                            yoffset = yoffset - 1;
                        }
                    }
                }
                x = x - xoffset/1.3;
                y = y - yoffset/1.3;
                double cX = x;
                double cY = y;
                if (onland) {

                    if (winds > 50) {
                        winds = winds - myRandom(winds / 9, winds / 5);
                    } else {
                        winds = winds - myRandom(0 + craziness, 2 + craziness);
                    }
                }
                winds = winds - myRandom((Math.sqrt(shear) / 5), (Math.sqrt(shear)));
                if (y > (height * (0.9 + offSet))) {
                    y = y - myRandom(0, 5);
                    x = x - myRandom(1, 10);
                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                } else {
                    if (y > (height * (0.8 + offSet))) {
                        y = y - myRandom(1, 8);
                        x = x - myRandom(1, 12);
                        stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                    } else {
                        if (y > (height * (0.7 + offSet))) {
                            y = y - myRandom(1, 8);
                            x = x - myRandom(2, 12);
                            stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 1)));
                        } else {
                            if (y > (height * (0.6 + offSet))) {
                                y = y - myRandom(1, 9);
                                x = x - myRandom(2, 9);
                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 2)));
                            } else {
                                if (y > (height * (0.5 + offSet))) {
                                    y = y - myRandom(1, 10);
                                    x = x - myRandom(1, 4);
                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 3)));
                                } else {
                                    if (y > (height * (0.4 + offSet))) {
                                        y = y - myRandom(1, 9);
                                        x = x + myRandom(1, 2);
                                        stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(0, 4)));
                                    } else {
                                        if (y > (height * (0.3 + offSet))) {
                                            y = y - myRandom(1, 7);
                                            x = x + myRandom(0, 3);
                                            stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(1, 4)));
                                        } else {
                                            if (y > (height * (0.2 + offSet))) {
                                                y = y - myRandom(1, 5);
                                                x = x + myRandom(1, 7);
                                                stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(2, 6)));
                                            } else {
                                                if (y > (height * (0.1 + offSet))) {
                                                    y = y - myRandom(0, 5);
                                                    x = x + myRandom(2, 18);
                                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(3, 8)));
                                                } else {
                                                    y = y - myRandom(0, 5);
                                                    x = x + myRandom(2, 18);
                                                    stormType.setPercentEx((stormType.getPercentEx() + randomGenerator(4, 12)));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                double xRChange = x-cX;
                while (Math.abs(xRChange) > 4) {
                    if (xRChange < 0) {
                        x+=1;
                    } else {
                        x-=1;
                    }
                    xRChange = x-cX;

                }
                double yRChange = y-cY;
                while (Math.abs(yRChange) > 4) {
                    if (yRChange < 0) {
                        y+=1;
                    } else {
                        y-=1;
                    }
                    yRChange = y-cY;

                }

                if (winds > 100) {
                    if (y > 450) {
                        if (myRandom(1, 2) < 1.8) {
                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                        } else {
                            winds = winds - myRandom(1 + craziness, 4 + craziness);
                        }
                    } else {
                        if (y > 400) {
                            if (myRandom(1, 2) < 1.73) {
                                winds = winds + myRandom(1 + craziness, 3 + craziness);
                            } else {
                                winds = winds - myRandom(1 + craziness, 4.2 + craziness);
                            }
                        } else {
                            if (y > 350) {
                                if (myRandom(1, 2) < 1.66) {
                                    winds = winds + myRandom(1 + craziness, 2.5 + craziness);
                                } else {
                                    winds = winds - myRandom(1 + craziness, 4.7 + craziness);
                                }
                            } else {
                                if (y > 300) {
                                    if (myRandom(1, 2) < 1.6) {
                                        winds = winds + myRandom(1 + craziness, 2 + craziness);
                                    } else {
                                        winds = winds - myRandom(1 + craziness, 5 + craziness);
                                    }
                                } else {
                                    if (y > 250) {
                                        if (myRandom(1, 2) < 1.57) {
                                            winds = winds + myRandom(1 + craziness, 2 + craziness);
                                        } else {
                                            winds = winds - myRandom(1 + craziness, 5.3 + craziness);
                                        }
                                    } else {
                                        if (y > 200) {
                                            if (myRandom(1, 2) < 1.51) {
                                                winds = winds + myRandom(1 + craziness, 2 + craziness);
                                            } else {
                                                if (winds > 130) {
                                                    winds = winds - myRandom(1 + craziness, 7 + craziness);
                                                } else {
                                                    winds = winds - myRandom(1 + craziness, 5.7 + craziness);
                                                }
                                            }
                                        } else {
                                            if (y > 150) {
                                                if (myRandom(1, 2) < 1.48) {
                                                    winds = winds + myRandom(1 + craziness, 2 + craziness);
                                                } else {
                                                    if (winds > 130) {
                                                        winds = winds - myRandom(2 + craziness, 8 + craziness);
                                                    } else {
                                                        winds = winds - myRandom(1 + craziness, 8 + craziness);
                                                    }
                                                }
                                            } else {
                                                if (y > 100) {
                                                    if (myRandom(1, 2) < 1.42) {
                                                        winds = winds + myRandom(1 + craziness, 2 + craziness);
                                                    } else {
                                                        if (winds > 130) {
                                                            winds = winds - myRandom(3 + craziness, 8 + craziness);
                                                        } else {
                                                            winds = winds - myRandom(1 + craziness, 8 + craziness);
                                                        }
                                                    }
                                                } else {
                                                    if (y > 50) {
                                                        if (myRandom(1, 2) < 1.38) {
                                                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                                                        } else {
                                                            if (winds > 130) {
                                                                winds = winds - myRandom(3 + craziness, 12 + craziness);
                                                            } else {
                                                                winds = winds - myRandom(1 + craziness, 9 + craziness);
                                                            }

                                                        }
                                                    } else {
                                                        if (myRandom(1, 2) < 1.32) {
                                                            winds = winds + myRandom(1 + craziness, 3 + craziness);
                                                        } else {
                                                            if (winds > 130) {
                                                                winds = winds - myRandom(3 + craziness, 12 + craziness);
                                                            } else {
                                                                winds = winds - myRandom(1 + craziness, 9 + craziness);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (y > 450) {
                        if (myRandom(1, 2) < 1.8) {
                            winds = winds + myRandom(0, 9);
                        } else {
                            winds = winds - myRandom(0, 3);
                        }
                    } else {
                        if (y > 400) {
                            if (myRandom(1, 2) < 1.73) {
                                winds = winds + myRandom(0, 8);
                            } else {
                                winds = winds - myRandom(0, 3.2);
                            }
                        } else {
                            if (y > 350) {
                                if (myRandom(1, 2) < 1.66) {
                                    winds = winds + myRandom(0, 7);
                                } else {
                                    winds = winds - myRandom(0, 3.7);
                                }
                            } else {
                                if (y > 300) {
                                    if (myRandom(1, 2) < 1.6) {
                                        winds = winds + myRandom(0, 7);
                                    } else {
                                        winds = winds - myRandom(0, 4);
                                    }
                                } else {
                                    if (y > 250) {
                                        if (myRandom(1, 2) < 1.57) {
                                            winds = winds + myRandom(0, 6);
                                        } else {
                                            winds = winds - myRandom(0, 4.3);
                                        }
                                    } else {
                                        if (y > 200) {
                                            if (myRandom(1, 2) < 1.51) {
                                                winds = winds + myRandom(0, 5);
                                            } else {
                                                winds = winds - myRandom(0, 1.42);
                                            }
                                        } else {
                                            if (y > 150) {
                                                if (myRandom(1, 2) < 1.48) {
                                                    winds = winds + myRandom(0, 3);
                                                } else {
                                                    winds = winds - myRandom(0, 4.9);
                                                }
                                            } else {
                                                if (y > 100) {
                                                    if (myRandom(1, 2) < 1.42) {
                                                        winds = winds + myRandom(0, 4);
                                                    } else {
                                                        winds = winds - myRandom(0, 5.5);
                                                    }
                                                } else {
                                                    if (y > 50) {
                                                        if (myRandom(1, 2) < 1.38) {
                                                            winds = winds + myRandom(0, 3);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);

                                                        }
                                                    } else {
                                                        if (myRandom(1, 2) < 1.32) {
                                                            winds = winds + myRandom(0, 3);
                                                        } else {
                                                            winds = winds - myRandom(0, 5.5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                newCoord = new Coordinate(x,y,getCategory(winds));
                newCoord.setWind(winds);
                history.add(newCoord);
            }
        }

        return history;
    }

    public ArrayList<Coordinate> getHistory() {
        return history;
    }

    public double getX() {
        return x;
    }

    public double getWinds() {
        return winds;
    }

    public double getY() {
        return y;
    }

    public Color getCategory(double winds) {
        if (winds < 30) {
            return Color.GRAY;
        } else {
            if (winds < 38) {
                return new Color(91, 99, 240);
            } else {
                if (winds < 74) {
                    return new Color(23, 235, 25);
                } else {
                    if (winds < 96) {
                        return new Color(236, 237, 172);
                    } else {
                        if (winds < 111) {
                            return new Color(240, 216, 106);
                        } else {
                            if (winds < 130) {
                                return new Color(255, 167, 40);

                            } else {
                                if (winds < 157) {
                                    return new Color(254, 109, 31);
                                } else {
                                    return new Color(255, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getLetter() {
        if (winds < 38) {
            return "TD";
        } else {
            if (winds < 74) {
                return "TS";
            } else {
                if (winds < 96) {
                    return "1";
                } else {
                    if (winds < 111) {
                        return "2";
                    } else {
                        if (winds < 130) {
                            return "3";

                        } else {
                            if (winds < 157) {
                                return "4";
                            } else {
                                return "5";
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isFormed() {
        return formed;
    }

    public double getAce() {
        return ace;
    }

    public double getWindPeak() {
        return windPeak;
    }
}
