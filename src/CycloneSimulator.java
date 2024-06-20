import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CycloneSimulator implements Serializable {
    final JMenuBar menuBar = new JMenuBar();
    private final String version = "v2.2.2";
    private final JComboBox<String> activityThings = new JComboBox<>();
    private final JComboBox<String> trackType = new JComboBox<>();
    private final JComboBox<String> resolution = new JComboBox<>();
    private final JLabel topLabel = new JLabel("Settings");
    private final JButton landDesignerButton = new JButton("Start Land Designer");
    private final JButton button = new JButton("Start Simulator");
    private final JSlider animationSpeed2 = new JSlider();
    private final JSlider numLand = new JSlider();
    private final JSlider numBuoy = new JSlider();
    private final JToggleButton randomActivity = new JToggleButton("OFF");
    private final JTextField yearField = new JTextField();
    private JLabel loadedInfo = new JLabel("");
    public JPanel SimulationPane = new JPanel();
    double activity = myRandom(0, 2);
    Storm showingStormInfo = null;
    Land showingLandInfo = null;
    Buoy showingBuoyInfo = null;
    int[] hsp1 = new int[3];
    int[] hsp2 = new int[3];
    LandSave loadedLand = null;
    int tracktype = 0;
    int SimResolution = 0;
    int[] hsp3 = new int[3];
    double minus = 0;
    boolean pressed = false;
    boolean paused = false;
    boolean t = false;
    boolean trackforecast = false;
    JPanel headerpanel = new JPanel();
    JFrame frame = new JFrame("Settings - CycloneSimulator "+ version);
    int frameDelay = 50;
    int year = 2021;
    boolean tcchance = false;
    JMenu actionMenu = new JMenu("Actions");
    JMenu layerMenu = new JMenu("Layers");
    JMenuItem landMenu = new JMenuItem("Select Land");
    JMenuItem buoyMenu = new JMenuItem("Select Buoy");

    JMenu selectMenu = new JMenu("Select");
    JMenuItem stormMenu = new JMenuItem("Select Storm");

    boolean sWind = false;
    boolean gusts = false;
    boolean surge = false;
    boolean threat = false;
    boolean seasonSummary = false;
    boolean huchance = false;
    int mouseX;
    boolean sst = false;
    int mouseY;
    String symbol = "M";
    double damages = 0;
    boolean singleStep = false;
    boolean defaultLand = false;
    private JMenuItem deselect;
    private JMenuItem trackforecastshow;

    public CycloneSimulator() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }


            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.add(headerpanel, BorderLayout.CENTER);
            yearField.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                        e.consume();  // if it's not a number, ignore the event
                    }
                }
            });
            headerpanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            trackType.addItem("Lined");
            trackType.addItem("Dotted");
            button.addActionListener(new StartSeason());
            landDesignerButton.addActionListener(new StartLandDesign());

            topLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(topLabel);
            headerpanel.add(new JLabel(""), new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(loadedInfo);
            JButton load = new JButton("Load Land");
            //headerpanel.add(new JLabel(""), new Font("Comic Sans MS", Font.PLAIN, 24));

            headerpanel.add(load);
            load.addActionListener(e -> {
                FileFilter filter = new FileNameExtensionFilter("Serializable file", "ser");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(filter);
                fileChooser.setFileFilter(filter);
                paused = true;
                int option = fileChooser.showSaveDialog(frame);
                paused = false;
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    try {
                        FileInputStream fil = new FileInputStream(file.getPath());
                        ObjectInputStream in = new ObjectInputStream(fil);
                        LandSave loaded = (LandSave) in.readObject();
                        ArrayList<Land> newSave = new ArrayList<>();
                        for (Land land: loaded.getLandSave()) {
                            newSave.add(new Land(land.getX(), land.getY(), land.getxSize(), land.getySize(), land.getLandName()));
                        }
                        loadedLand = new LandSave(newSave);
                        in.close();
                        fil.close();
                        loadedInfo.setText("Loaded " + file.getName());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "No Land Save found in this file or this save is outdated!!");

                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });
            resolution.addItem("100x50");
            resolution.addItem("200x100");
            resolution.addItem("250x500");
            resolution.addItem("333x666");
            resolution.addItem("500x1000");

            resolution.addItem("20x10");
            JLabel randomJLabel5 = new JLabel("Season Activity");
            randomJLabel5.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel5);
            headerpanel.add(activityThings);

            selectMenu.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            actionMenu.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            layerMenu.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));

            frame.setResizable(true);

            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            animationSpeed2.setMaximum(100);
            animationSpeed2.setMinimum(10);
            JLabel randomJLabel4 = new JLabel("Frame Delay");
            randomJLabel4.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel4);
            headerpanel.add(animationSpeed2);
            numLand.setMaximum(30);
            numLand.setMinimum(0);
            numLand.setValue(10);
            numLand.setPaintTicks(true);
            ItemListener itemListener = itemEvent -> {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    randomActivity.setText("ON"); // show your message here
                } else {
                    randomActivity.setText("OFF"); // remove your message
                }
            };
            randomActivity.addItemListener(itemListener);
            numLand.setMajorTickSpacing(5);
            numLand.setMinorTickSpacing(3);
            numBuoy.setMaximum(30);
            numBuoy.setMinimum(5);
            numBuoy.setValue(10);
            numBuoy.setPaintTicks(true);
            numBuoy.setPaintLabels(true);
            numLand.setPaintLabels(true);

            numBuoy.setMajorTickSpacing(5);
            numBuoy.setMinorTickSpacing(2);
            animationSpeed2.setPaintTicks(true);
            animationSpeed2.setPaintLabels(true);
            animationSpeed2.setMajorTickSpacing(25);
            animationSpeed2.setMinorTickSpacing(10);
            animationSpeed2.setToolTipText("Frame Delay");
            activityThings.addItem("Very Inactive");
            activityThings.setToolTipText("Activity");
            activityThings.addItem("Inactive");
            activityThings.addItem("Normal");
            activityThings.addItem("Above Normal");
            activityThings.addItem("Very Active");
            activityThings.addItem("Hyperactive");
            activityThings.addItem("Super Active");
            activityThings.setSelectedIndex(2);
            headerpanel.setLayout(new GridLayout(6, 4));
            animationSpeed2.setPreferredSize(new Dimension(400, 60));
            frame.pack();
            headerpanel.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            JLabel randomJLabel2 = new JLabel("# of Land");
            randomJLabel2.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel2);
            headerpanel.add(numLand);
            JLabel randomJLabel6 = new JLabel("# of Buoys");
            randomJLabel6.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel6);
            headerpanel.add(numBuoy);

            //create menu items

            JLabel randomJLabel8 = new JLabel("Starting Year");
            randomJLabel8.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel8);
            headerpanel.add(yearField);
            yearField.setText("2000");
            JLabel randomJLabel9 = new JLabel("Random Activity");
            randomJLabel9.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel9);
            headerpanel.add(randomActivity);

            JLabel randomJLabel10 = new JLabel("Resolution");
            randomJLabel10.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel10);
            headerpanel.add(resolution);
            JLabel randomJLabel44 = new JLabel("Track Style");
            randomJLabel44.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            headerpanel.add(randomJLabel44);
            headerpanel.add(trackType);
            //JLabel randomJLabel3 = new JLabel("Start Button");
            //randomJLabel3.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
            //headerpanel.add(randomJLabel3);
            headerpanel.add(button);
            headerpanel.add(landDesignerButton);


        });
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public double myRandom(double min, double max) {
        if (max < min) {
            double newmin = max;
            max = min;
            min = newmin;
        }
        Random r = new Random();
        int randomNum = (int) (r.nextInt((int) ((max * 1000) - (min * 1000) + 1)) + (min * 1000));
        return randomNum / 1000.0;
    }
    private double calcSigWave(double windspeed) {
        final double gravity = 9.80665;
        double fetchlength=0.7;
        double height = Math.pow(windspeed,2)*Math.pow(0.283*Math.tanh(0.0125*((gravity*fetchlength)/Math.pow(windspeed,2))),0.42)/gravity;
        height = height * 3.2808399;
        return height * 0.8 ;
    }
    private void ShowError(String error) {

        JPanel newPanel = new JPanel();
        SimulationPane.setVisible(false);
        frame.add(newPanel, BorderLayout.CENTER);
        newPanel.setLayout(new GridLayout(3, 1));
        newPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newPanel.add(new JLabel("Error"));
        newPanel.add(new JLabel("Message: " + error));

    }

    public class SimulationPane extends JPanel implements KeyListener {


        int majors = 0;
        int hurricanes = 0;
        int[] monthly = new int[12];
        int numstorms = 0;
        int tds = 0;
        int tswatches = 0;
        int tswarnings = 0;
        int huwatches = 0;
        int huwarnings = 0;
        double ace =0;
        int c5 = 0;
        int lows;
        int toIncrement = 0;
        double shearToMonth;
        Storm strongestStorm = null;
        ArrayList<Land> landList = new ArrayList<>();
        ArrayList<Storm> storms = new ArrayList<>();
        ArrayList<ArrayList<Coordinate>> coordinateToAdd = new ArrayList<>();
        ArrayList<Buoy> buoys = new ArrayList<>();
        ArrayList<Storm> history = new ArrayList<>();
        ArrayList<Landfall> landfalls = new ArrayList<>();
        int z = 0;
        int day = 1;

        Timer timer = new Timer(30, e -> {

            LocalDate localDate = LocalDate.ofEpochDay(day);
            int month = localDate.getMonthValue();
            month--;
            toIncrement = month;

            if (day == 365 && z == 24) {
                day = 1;
                majors = 0;
                activity = myRandom(0, 2);
                if (randomActivity.isSelected()) {
                    activityThings.setSelectedIndex(randInt(-1, 6));
                }
                if (activityThings.getSelectedIndex() == 0) {
                    activity = myRandom(1.5, 2);
                    minus = activity / 100.0 * -1;
                    hsp1[0] = randInt(5, 11);
                    hsp2[0] = randInt(5, 11);
                    hsp3[0] = randInt(5, 11);
                } else {
                    if (activityThings.getSelectedIndex() == 1) {
                        activity = myRandom(0.5, 1.5);
                        minus = activity / 100.0 * -1;
                        hsp1[0] = randInt(7, 13);
                        hsp2[0] = randInt(7, 13);
                        hsp3[0] = randInt(7, 13);
                    } else {
                        if (activityThings.getSelectedIndex() == 2) {
                            activity = myRandom(0.5, 1.5);
                            minus = (activity - 1) / 100.0 * -1;
                            hsp1[0] = randInt(8, 15);
                            hsp2[0] = randInt(8, 15);
                            hsp3[0] = randInt(8, 15);
                        } else {
                            if (activityThings.getSelectedIndex() == 3) {
                                activity = myRandom(0.5, 1.0);
                                minus = (activity) / 100.0;
                                hsp1[0] = randInt(10, 17);
                                hsp2[0] = randInt(11, 17);
                                hsp3[0] = randInt(10, 17);
                            } else {
                                if (activityThings.getSelectedIndex() == 4) {
                                    activity = myRandom(1.0, 1.5);
                                    hsp1[0] = randInt(12, 18);
                                    hsp2[0] = randInt(12, 18);
                                    hsp3[0] = randInt(12, 18);
                                } else {
                                    if (activityThings.getSelectedIndex() == 5) {
                                        activity = myRandom(1.5, 2.5);
                                        hsp1[0] = randInt(14, 20);
                                        hsp2[0] = randInt(14, 20);
                                        hsp3[0] = randInt(14, 20);
                                    } else {
                                        activity = myRandom(2.5, 7.0);
                                        hsp1[0] = randInt(17, 25);
                                        hsp2[0] = randInt(17, 25);
                                        hsp3[0] = randInt(17, 25);
                                    }
                                }
                                minus = (activity - 1) / 100.0;
                            }
                        }
                    }
                }
                System.out.println(minus);
                hsp1[1] = (int) (hsp1[0] / myRandom(1.3, 3));
                hsp1[2] = (int) (hsp1[1] / myRandom(1.2, 2.3));
                hsp2[1] = (int) (hsp2[0] / myRandom(1.6, 3.2));
                hsp2[2] = (int) (hsp2[1] / myRandom(1.3, 2.5));
                hsp3[1] = (int) (hsp3[0] / myRandom(1.5, 3.2));
                hsp3[2] = (int) (hsp3[1] / myRandom(1.3, 2.8));
                hurricanes = 0;
                numstorms = 0;
                symbol = "M";
                tds = 0;
                c5 = 0;
                monthly = new int[12];
                coordinateToAdd.clear();
                damages = 0;
                history.clear();
                year++;
                z = 0;
                landfalls = new ArrayList<>();
                strongestStorm = null;
                lows = 0;
                for (Land sland : landList) {
                    sland.clearDamages();
                }
            }
            deselect.setEnabled(showingStormInfo != null || showingBuoyInfo != null || showingLandInfo != null);
            trackforecastshow.setEnabled(showingStormInfo != null);
            if (!paused) {
                if (!storms.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(frameDelay);
                    } catch (Exception e2) {
                        System.out.println("SLEEP ERROR");
                    }
                }
                update();

                repaint();

                if (z == 24) {
                    z = 0;
                    day++;
                } else {
                    z = z + 3;
                }


                frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00");
                if (sWind) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (SUSTAINED WINDS)");
                }
                if (threat) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (LAND THREAT)");
                }
                if (gusts) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (WIND GUSTS)");
                }
                if (sst) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (SST)");
                }
                if (tcchance) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (TS WIND PROBABILITY)");
                }
                if (huchance) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (HU WIND PROBABILITY)");
                }
                if (surge) {
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (SURGE HEIGHT)");
                }
            } else {
                if (singleStep) {
                    if (!storms.isEmpty()) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(frameDelay);
                        } catch (Exception e2) {
                            System.out.println("SLEEP ERROR");
                        }
                    }
                    update();

                    repaint();

                    if (z == 24) {
                        z = 0;
                        day++;
                    } else {
                        z = z + 3;
                    }

                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (PAUSED)");
                    singleStep = false;
                } else {
                    boolean clickOut = false;
                    boolean showing = false;
                    Storm original = showingStormInfo;
                    if (original != null) {
                        showing = true;
                    }
                    if (pressed && (!sst && !tcchance && !huchance && !sWind && !gusts && !surge && !threat)) {

                        for (Storm storm : storms) {
                            double distance = Math.sqrt(Math.pow(mouseY - 50 - storm.getY(), 2) + Math.pow(mouseX - 30 - storm.getX(), 2));
                            System.out.println(distance);
                            if (distance < 20) {
                                showingStormInfo = storm;

                                showingLandInfo = null;
                                showingBuoyInfo = null;
                                showing = true;
                                clickOut = true;

                                break;
                            }
                        }
                        if (!clickOut) {
                            for (Land lands : landList) {

                                double distance = Math.sqrt(Math.pow(mouseY - 50 - lands.getY(), 2) + Math.pow(mouseX - 30 - lands.getX(), 2));


                                if (distance < 15) {
                                    showingStormInfo = null;
                                    showingLandInfo = lands;
                                    showingBuoyInfo = null;
                                    showing = true;
                                    clickOut = true;

                                    break;
                                }
                            }
                        }
                        if (!clickOut) {
                            for (Buoy buoy : buoys) {
                                double distance;

                                if (Math.abs(buoy.getX() - (mouseX - 41)) > Math.abs(buoy.getY() - (mouseY - 30))) {
                                    distance = Math.abs(buoy.getX() - (mouseX));
                                } else {
                                    distance = Math.abs(buoy.getX() - (mouseY));
                                }

                                if (distance < 15) {
                                    showingStormInfo = null;
                                    showingLandInfo = null;
                                    showingBuoyInfo = buoy;
                                    showing = true;
                                    clickOut = true;

                                    break;
                                }
                            }
                        }
                        if (!clickOut) {
                            showing = false;
                            showingStormInfo = null;
                            trackforecast = false;
                            showingLandInfo = null;
                            showingBuoyInfo = null;
                        }
                        pressed = false;
                    }
                    frame.setTitle(Year.of(year).atDay(day) + ", " + z + ":00 (PAUSED)");

                    if (!showing) {
                        if (original != null) {
                            repaint();
                        }
                    } else {
                        if (original == null) {
                            repaint();
                        }
                    }
                }
            }
        });

        public SimulationPane() {
            if (!defaultLand) {
                if (loadedLand != null) {
                    landList = loadedLand.getLandSave();
                    for (Land newLand : landList) {
                        JMenuItem newLandItem = new JMenuItem(newLand.getLandName());
                        newLandItem.addActionListener(e -> {
                            showingLandInfo = newLand;
                            showingBuoyInfo = null;
                            showingStormInfo = null;
                        });
                    }
                } else {
                    for (int i = 0; i < numLand.getValue(); i++) {
                        Land newLand = new Land(i);
                        landList.add(newLand);
                        JMenuItem newLandItem = new JMenuItem(newLand.getLandName());
                        newLandItem.addActionListener(e -> {
                            showingLandInfo = newLand;
                            showingBuoyInfo = null;
                            showingStormInfo = null;
                        });
                    }
                }
                landMenu.updateUI();
            } else {
                //like this for now
                for (int i = 0; i < numLand.getValue(); i++) {
                    landList.add(new Land(i));
                }
            }
            stormMenu.addActionListener(e -> {
                ArrayList<String> options = new ArrayList<>();
                for (Storm storm : storms) {
                    options.add(storm.getName());
                }
                for (Storm storm : history) {
                    options.add(storm.getName());
                }


                String s = (String)JOptionPane.showInputDialog(
                        getParent(),
                        "Select a storm...",
                        "Select Storm",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options.toArray(),
                        "");
                for (Storm storm: storms) {
                    if (storm.getName().equals(s)) {
                        showingStormInfo = storm;
                        showingBuoyInfo = null;
                        showingLandInfo = null;
                        break;
                    }
                }
                for (Storm storm: history) {
                    if (storm.getName().equals(s)) {
                        showingStormInfo = storm;
                        showingBuoyInfo = null;
                        showingLandInfo = null;
                        break;
                    }
                }
            });
            landMenu.addActionListener(e -> {
                ArrayList<String> options = new ArrayList<>();
                for (Land land : landList) {
                    options.add(land.getLandName());
                }


                String s = (String)JOptionPane.showInputDialog(
                        getParent(),
                        "Select a country...",
                        "Select Country",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options.toArray(),
                        "");
                for (Land land: landList) {
                    if (land.getLandName().equals(s)) {
                        showingStormInfo = null;
                        showingBuoyInfo = null;
                        showingLandInfo = land;
                        break;
                    }
                }
            });
            buoyMenu.addActionListener(e -> {
                ArrayList<String> options = new ArrayList<>();
                for (Buoy buoy : buoys) {
                    options.add("Buoy #" + buoy.getNumber());
                }
                String s = (String)JOptionPane.showInputDialog(
                        getParent(),
                        "Select a buoy...",
                        "Select Buoy",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options.toArray(),
                        "");
                for (Buoy buoy: buoys) {
                    if (("Buoy #" +buoy.getNumber()).equals(s)) {
                        showingStormInfo = null;
                        showingBuoyInfo = buoy;
                        showingLandInfo = null;
                        break;
                    }
                }
            });
            loadedLand = new LandSave(landList);
            for (int i = 0; i < numBuoy.getValue(); i++) {
                boolean inLand = false;
                Buoy newBuoy = new Buoy();
                for (Land land: landList) {
                    if (land.inLand(newBuoy.getX(), newBuoy.getY())) {
                        inLand = true;
                        break;
                    }
                }
                if (!inLand) {
                    buoys.add(newBuoy);
                }
            }

            if (activityThings.getSelectedIndex() == 0) {
                activity = myRandom(1.5, 2);
                minus = activity / 100.0 * -1;
                hsp1[0] = randInt(5, 11);
                hsp2[0] = randInt(5, 11);
                hsp3[0] = randInt(5, 11);
            } else {
                if (activityThings.getSelectedIndex() == 1) {
                    activity = myRandom(0.5, 1.5);
                    minus = activity / 100.0 * -1;
                    hsp1[0] = randInt(7, 13);
                    hsp2[0] = randInt(7, 13);
                    hsp3[0] = randInt(7, 13);
                } else {
                    if (activityThings.getSelectedIndex() == 2) {
                        activity = myRandom(0.5, 1.5);
                        minus = (activity - 1) / 100.0 * -1;
                        hsp1[0] = randInt(8, 15);
                        hsp2[0] = randInt(8, 15);
                        hsp3[0] = randInt(8, 15);
                    } else {
                        if (activityThings.getSelectedIndex() == 3) {
                            activity = myRandom(0.5, 1.0);
                            minus = (activity) / 100.0;
                            hsp1[0] = randInt(10, 17);
                            hsp2[0] = randInt(11, 17);
                            hsp3[0] = randInt(10, 17);
                        } else {
                            if (activityThings.getSelectedIndex() == 4) {
                                activity = myRandom(1.0, 1.5);
                                hsp1[0] = randInt(12, 18);
                                hsp2[0] = randInt(12, 18);
                                hsp3[0] = randInt(12, 18);
                            } else {
                                if (activityThings.getSelectedIndex() == 5) {
                                    activity = myRandom(1.5, 2.5);
                                    hsp1[0] = randInt(14, 20);
                                    hsp2[0] = randInt(14, 20);
                                    hsp3[0] = randInt(14, 20);
                                } else {
                                    activity = myRandom(2.5, 7.0);
                                    hsp1[0] = randInt(17, 30);
                                    hsp2[0] = randInt(17, 30);
                                    hsp3[0] = randInt(17, 30);
                                }
                            }
                            minus = (activity - 1) / 100.0;
                        }
                    }
                }
            }
            hsp1[1] = (int) (hsp1[0] / myRandom(1.3, 3));
            hsp1[2] = (int) (hsp1[1] / myRandom(1.2, 2.3));
            hsp2[1] = (int) (hsp2[0] / myRandom(1.6, 3.2));
            hsp2[2] = (int) (hsp2[1] / myRandom(1.3, 2.5));
            hsp3[1] = (int) (hsp3[0] / myRandom(1.5, 3.2));
            hsp3[2] = (int) (hsp3[1] / myRandom(1.3, 2.8));
            timer.start();

        }

        protected void update() {
//            DecimalFormat df = new DecimalFormat("#.###");
//            damages = Double.parseDouble(df.format(damages));
            if (damages > 999) {
                if (symbol.equals("M")) {
                    symbol = "B";
                    damages = damages / 1000;
                } else {
                    if (symbol.equals("B")) {
                        symbol = "T";
                        damages = damages / 1000;
                    }
                }
            }
            damages = Math.round(damages);
            if (day < 31) {
                shearToMonth = 5 + (1.0 / (day));
                if (myRandom(0, 55) < 0.03 + minus) {
                    Storm newStorm = new Storm(26, lows, day);
                    storms.add(newStorm);
                    JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                    newStorm.setMenuItem(newMenuItem);

                    newMenuItem.addActionListener(e1 -> {
                        showingStormInfo = newStorm;
                        showingBuoyInfo = null;
                        showingLandInfo = null;
                    });
                    newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                    monthly[toIncrement] += 1;
                    lows++;

                }
            } else {
                if (day < 59) {
                    shearToMonth = 6 + (1.0 / (day - 30));
                    if (myRandom(0, 65) < 0.02 + minus) {
                        Storm newStorm = new Storm(26, lows, day);
                        storms.add(newStorm);
                        JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                        newStorm.setMenuItem(newMenuItem);
                            newMenuItem.addActionListener(e1 -> {
                            showingStormInfo = newStorm;
                            showingBuoyInfo = null;
                            showingLandInfo = null;
                        });
                        newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                        monthly[toIncrement] += 1;
                        lows++;

                    }
                } else {
                    if (day < 90) {
                        shearToMonth = 7 + (1.0 / (day - 58));
                        if (myRandom(0.009, 65) < 0.02 + minus) {
                            monthly[toIncrement] += 1;
                            Storm newStorm = new Storm(26, lows, day);
                            storms.add(newStorm);
                            JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                            newStorm.setMenuItem(newMenuItem);
                                    newMenuItem.addActionListener(e1 -> {
                                showingStormInfo = newStorm;
                                showingBuoyInfo = null;
                                showingLandInfo = null;
                            });
                            newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                            lows++;
                        }
                    } else {
                        if (day < 120) {
                            shearToMonth = 8;
                            if (myRandom(0, 45) < 0.02 + minus) {
                                Storm newStorm = new Storm(26, lows, day);
                                storms.add(newStorm);
                                JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                newStorm.setMenuItem(newMenuItem);
                                            newMenuItem.addActionListener(e1 -> {
                                    showingStormInfo = newStorm;
                                    showingBuoyInfo = null;
                                    showingLandInfo = null;
                                });
                                newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                monthly[toIncrement] += 1;
                                lows++;
                            }
                        } else {
                            if (day < 151) {
                                shearToMonth = 8 - (2.0 / (151 - day));
                                if (myRandom(0, 23) < 0.08 + minus) {
                                    Storm newStorm = new Storm(26, lows, day);
                                    storms.add(newStorm);
                                    JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                    newStorm.setMenuItem(newMenuItem);
                                                    newMenuItem.addActionListener(e1 -> {
                                        showingStormInfo = newStorm;
                                        showingBuoyInfo = null;
                                        showingLandInfo = null;
                                    });
                                    newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                    monthly[toIncrement] += 1;
                                    lows++;
                                }
                            } else {
                                if (day < 181) {
                                    shearToMonth = 6 - (1.0 / (181 - day));
                                    if (myRandom(0, 13) < 0.08 + minus) {
                                        Storm newStorm = new Storm(26, lows, day);
                                        storms.add(newStorm);
                                        JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                        newStorm.setMenuItem(newMenuItem);
                                                            newMenuItem.addActionListener(e1 -> {
                                            showingStormInfo = newStorm;
                                            showingBuoyInfo = null;
                                            showingLandInfo = null;
                                        });
                                        newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                        monthly[toIncrement] += 1;
                                        lows++;
                                    }
                                } else {
                                    if (day < 212) {
                                        shearToMonth = 5 - (1.0 / (212 - day));
                                        if (myRandom(0, 9) < 0.08 + minus) {
                                            Storm newStorm = new Storm(26, lows, day);
                                            storms.add(newStorm);
                                            JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                            newStorm.setMenuItem(newMenuItem);
                                                                    newMenuItem.addActionListener(e1 -> {
                                                showingStormInfo = newStorm;
                                                showingBuoyInfo = null;
                                                showingLandInfo = null;
                                            });
                                            newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                            monthly[toIncrement] += 1;
                                            lows++;
                                        }
                                    } else {
                                        if (day < 243) {
                                            shearToMonth = 4 - (1.0 / (243 - day));
                                            if (myRandom(0, 8) < 0.08 + minus) {
                                                Storm newStorm = new Storm(26, lows, day);
                                                storms.add(newStorm);
                                                JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                                newStorm.setMenuItem(newMenuItem);
                                                                            newMenuItem.addActionListener(e1 -> {
                                                    showingStormInfo = newStorm;
                                                    showingBuoyInfo = null;
                                                    showingLandInfo = null;
                                                });
                                                newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                                monthly[toIncrement] += 1;
                                                lows++;
                                            }
                                        } else {
                                            if (day < 273) {
                                                shearToMonth = 3;
                                                if (myRandom(0, 6) < 0.08 + minus) {
                                                    Storm newStorm = new Storm(26, lows, day);
                                                    storms.add(newStorm);
                                                    JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                                    newStorm.setMenuItem(newMenuItem);
                                                                                    newMenuItem.addActionListener(e1 -> {
                                                        showingStormInfo = newStorm;
                                                        showingBuoyInfo = null;
                                                        showingLandInfo = null;
                                                    });
                                                    newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                                    monthly[toIncrement] += 1;
                                                    lows++;
                                                }
                                            } else {
                                                if (day < 304) {
                                                    shearToMonth = 3;
                                                    if (myRandom(0, 8) < 0.08 + minus) {
                                                        Storm newStorm = new Storm(26, lows, day);
                                                        storms.add(newStorm);
                                                        JMenuItem newMenuItem = new JMenuItem(newStorm.getName());
                                                        newStorm.setMenuItem(newMenuItem);
                                                                                            newMenuItem.addActionListener(e1 -> {
                                                            showingStormInfo = newStorm;
                                                            showingBuoyInfo = null;
                                                            showingLandInfo = null;
                                                        });
                                                        newMenuItem.setBackground(newStorm.getCategory(newStorm.winds));
                                                        monthly[toIncrement] += 1;
                                                        lows++;
                                                    }
                                                } else {
                                                    if (day < 334) {
                                                        shearToMonth = 4 - (1.0 / (day - 303));
                                                        if (myRandom(0, 14) < 0.08 + minus) {
                                                            storms.add(new Storm(24, lows, day));
                                                            monthly[toIncrement] += 1;
                                                            lows++;
                                                        }
                                                    } else {
                                                        shearToMonth = 5 - (1.0 / (day - 332));
                                                        if (myRandom(0, 38) < 0.08 + minus) {
                                                            storms.add(new Storm(24, lows, day));
                                                            monthly[toIncrement] += 1;
                                                            lows++;
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
            menuBar.updateUI();
            ArrayList<Storm> executionQueue = new ArrayList<>();
            if (storms.isEmpty()) {
                for (Land singleLand : landList) {
                    singleLand.noDamage();
                }
            }
            for (Storm storm : storms) {
                if (storm.isDead()) {
                    executionQueue.add(storm);
                    coordinateToAdd.add(storm.getHistory());
                } else {
                    double shear;
                    if (day < 259 && day > (259 - 178)) {
                        shear = 12 - ((day - (259 - 178)) / (259.0) * 12);
                    } else {
                        if (day >= 259) {
                            shear = ((day - 259) / (106.0)) * 9;

                        } else {
                            shear = (((day) / (81.0)) * 3) + 9;
                        }
                    }
                    double news;

                    if (storm.history.isEmpty()) {
                        news = shear;

                    } else {
                        if (storm.oldShear < shear) {
                            news = storm.oldShear += myRandom(-0.3, 0.7);
                        } else {
                            news = storm.oldShear += myRandom(-0.8, 0.2);

                        }
                    }
                    if (news < 1) {
                        news = 1;
                    }
                    news += (news / myRandom(3, 6)) * ((126 - (storm.getY() / 4.0)) / 125.0);
                    boolean onland = false;
                    Land landon = new Land(3);
                    for (Land singleLand : landList) {
                        if (singleLand.inLand(storm.getX(), storm.getY())) {
                            onland = true;
                            landon = singleLand;
                            break;

                        } else {
                            singleLand.noDamage();
                        }


                    }
                    if (!storm.land && onland) {
                        landfalls.add(new Landfall(landon, storm, storm.getWinds()));
                    }
                    storm.moveForward(news, onland);
                    if (onland) {
                        landon.addDamages(storm.getDamageChange(), storm);
                    }

                }
            }
            for (Buoy buoy : buoys) {
                buoy.updateData(storms);
            }
            for (Storm storm : executionQueue) {
                if (symbol.equals("M")) {
                    damages = damages + storm.getDamages();
                } else {
                    if (symbol.equals("B")) {
                        damages = damages + (storm.getDamages() / 1000.0);
                    } else {
                        damages = damages + (storm.getDamages() / 1000000.0);
                    }
                }

                if (storm.getWindPeak() > 29) {
                    tds++;

                }
                if (storm.getWindPeak() > 37) {
                    numstorms++;
                }
                if (storm.getWindPeak() > 73) {
                    hurricanes++;
                }
                if (storm.getWindPeak() > 110) {
                    majors++;
                }
                if (storm.getWindPeak() > 156) {
                    c5++;
                }
                if (strongestStorm == null) {
                    strongestStorm = storm;
                } else {
                    if (storm.getWindPeak() > strongestStorm.getWindPeak()) {
                        strongestStorm = storm;
                    }
                }
                ace+=storm.getAce();
                history.add(storm);
                storms.remove(storm);

            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1200, 500);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        protected void paintComponent(Graphics g) {
            try {
                super.paintComponent(g);
                Graphics2D g3d = (Graphics2D) g.create();
                g3d.setColor(new Color(129, 140, 255));
                g3d.fillRect(0, 0, 1000, 500);

                ArrayList<Color> colors = new ArrayList<>();
                colors.add(Color.GRAY); //0
                ArrayList<Storm> executionQueue = new ArrayList<>();
                colors.add(new Color(91, 99, 240)); //1
                colors.add(new Color(23, 235, 25)); //2
                colors.add(new Color(236, 237, 172));
                colors.add(new Color(240, 216, 106));
                colors.add(new Color(255, 167, 40));
                colors.add(new Color(254, 102, 31));
                colors.add(new Color(255, 0, 0));
                double windThreat = 0;
                double surgeThreat = 0;
                double rainThreat = 0;
                for (Land lands : landList) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    ArrayList<Coordinate> landCoordinates = lands.getCoordinates();
                    int noLandfall = 0;
                    int tsLandfall = 0;
                    int huLandfall = 0;
                    boolean anyStorms = false;
                    for (int i = 0; i < 10; i++) {
                        for (Storm storm : storms) {

                            ArrayList<Coordinate> coordinates = storm.getTrackForecast(landList);
                            boolean nolandfall = true;
                            boolean hu = false;
                            for (Coordinate coordinate : coordinates) {
                                if (coordinates.indexOf(coordinate) > 8) {
                                    break;
                                }
                                if (lands.inLand(coordinate.getX(), coordinate.getY())) {
                                    if (colors.indexOf(coordinate.getColor()) >= 2) {
                                        nolandfall = false;
                                        anyStorms = true;

                                    }
                                    if (colors.indexOf(coordinate.getColor()) >= 3) {
                                        hu = true;
                                        break;
                                    }
                                    if (coordinate.getWind() > windThreat) {
                                        windThreat = coordinate.getWind();
                                    }
                                    double winds = Math.sqrt(((15 * storm.getWinds()) * ((storm.getStormSize() + 20) / 2))) / (Math.cbrt(storm.getMovementSpeed()+2)) ;

                                    if (calcSigWave(winds) > surgeThreat) {
                                        surgeThreat = calcSigWave(winds);
                                    }
                                    double rain = ((Math.sqrt((2 * coordinate.getWind()) * (storm.getStormSize()/1.2)))/(2 * (storm.getMovementSpeed()/4) + 4));
                                    if (rain > rainThreat) {
                                        rainThreat=rain;
                                    }

                                }

                            }
                            if (nolandfall) {
                                noLandfall += 1;
                            } else {
                                if (hu) {
                                    huLandfall += 40;
                                } else {
                                    tsLandfall += 2;
                                }
                            }


                        }
                    }
                    g2d.setColor(new Color(0, 0, 0));
                    int threat= 0;
                    if (!anyStorms) {
                        g2d.setColor(new Color(0, 0, 0));
                    } else {
                        if (noLandfall >= (tsLandfall + huLandfall)) {

                            if (huLandfall > tsLandfall) {
                                g2d.setColor(new Color(242, 177, 172));
                                huwatches++;
                                threat = 3;

                            } else {
                                g2d.setColor(new Color(255, 225, 0));
                                tswatches++;
                                threat = 1;
                            }

                        } else {

                            if (tsLandfall > huLandfall) {
                                if (tsLandfall - huLandfall < 3) {
                                    g2d.setColor(new Color(242, 177, 172));
                                    huwatches++;
                                    threat = 3;
                                } else {

                                    g2d.setColor(new Color(0, 71, 255));
                                    tswarnings++;
                                    threat = 2;

                                }
                            } else {
                                if (huLandfall - tsLandfall < 3) {
                                    g2d.setColor(new Color(242, 177, 172));
                                    huwatches++;
                                    threat = 3;
                                } else {
                                    g2d.setColor(new Color(255, 0, 0));
                                    huwarnings++;
                                    threat = 4;
                                }
                            }


                        }

                    }

                    lands.setThreat(threat);
                    if (windThreat > 70) {
                        System.out.println(rainThreat);
                    }
                    lands.setWindThreat(windThreat);
                    lands.setRainThreat(rainThreat);

                    lands.setSurgeThreat(surgeThreat);


                    g2d.fillRect((int) landCoordinates.get(0).getX() - 2, (int) landCoordinates.get(0).getY() - 2, lands.getxSize() + 4, lands.getySize() + 4);

                    g2d.setColor(Color.GREEN);
                    g2d.fillRect((int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY(), lands.getxSize(), lands.getySize());

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(lands.getLandName(), (int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY() - 4);

                }
                for (Land lands : landList) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    ArrayList<Coordinate> landCoordinates = lands.getCoordinates();
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(lands.getLandName(), (int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY() - 4);
                    if (lands.getDamageChange() != 0) {
                        g2d.setColor(Color.RED);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
                        g2d.drawString("- $" + lands.getDamageChange() + "M", (int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY() - 15);
                    }
                }

                if (!sWind && !gusts && !sst && !tcchance && !huchance && !surge && !threat) {
                    tswatches = 0;
                    tswarnings = 0;
                    huwarnings = 0;
                    huwatches = 0;

                    for (Buoy buoy : buoys) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        if (buoy.getSustainedWinds() < 30) {
                            g2d.setColor(Color.BLACK);
                        } else {
                            Storm storm = new Storm(30, 3, 0);
                            g2d.setColor(storm.getCategory(buoy.getSustainedWinds()));

                        }
                        g2d.fillOval(buoy.getX(), buoy.getY(), 10, 10);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 7));
                        g2d.drawString("#" + buoy.getNumber(), buoy.getX(), buoy.getY());


                    }

                    for (ArrayList<Coordinate> storm : coordinateToAdd) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        for (Coordinate coordinate : storm) {
                            g2d.setColor(coordinate.getColor());

                            if (storm.indexOf(coordinate) > 0) {
                                if (tracktype == 1) {
                                    g2d.setColor(coordinate.getColor());
                                    g2d.drawOval((int) (storm.get(storm.indexOf(coordinate) - 1).getX() - 2), (int) (storm.get(storm.indexOf(coordinate) - 1).getY() - 2), 4, 4);

                                }
                                if (tracktype == 1) {

                                    g2d.setColor(Color.BLACK);
                                }
                                g2d.setStroke(new BasicStroke(2));
                                g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) storm.get(storm.indexOf(coordinate) - 1).getX(), (int) storm.get(storm.indexOf(coordinate) - 1).getY());
                            }
                            g2d.setColor(coordinate.getColor());
                            if (tracktype == 1) {
                                g2d.drawOval((int) (coordinate.getX() - 2), (int) (coordinate.getY() - 2), 4, 4);
                            }
                        }

                    }
                    for (Storm storm : storms) {
                        if (!(storm.isDead())) {

                            Graphics2D g2d = (Graphics2D) g.create();
                            for (int i = 0; i < 5; i++) {
                                ArrayList<Coordinate> forecast = storm.getTrackForecast(landList);
                                for (Coordinate coordinate : forecast) {

                                    if (forecast.indexOf(coordinate) > 0) {
                                        g2d.setStroke(new BasicStroke(2));
                                        g2d.setColor(forecast.get(forecast.indexOf(coordinate) - 1).getColor());
                                        g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) forecast.get(forecast.indexOf(coordinate) - 1).getX(), (int) forecast.get(forecast.indexOf(coordinate) - 1).getY());
                                    }
                                    g2d.setColor(coordinate.getColor());
                                    g2d.fillOval((int) coordinate.getX() - 2, (int) coordinate.getY() - 2, 4, 4);

                                }
                            }

                            if (storm.getWinds() > 30) {
                                for (Coordinate coordinate : storm.getHistory()) {
                                    g2d.setColor(coordinate.getColor());

                                    if (storm.getHistory().indexOf(coordinate) > 0) {
                                        ArrayList<Coordinate> coordinates = storm.getHistory();
                                        if (tracktype == 1) {
                                            g2d.setColor(coordinate.getColor());
                                            g2d.drawOval((int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getX() - 2), (int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getY() - 2), 4, 4);

                                        }
                                        if (tracktype == 1) {

                                            g2d.setColor(Color.BLACK);
                                        }
                                        g2d.setStroke(new BasicStroke(2));
                                        g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getX(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getY());
                                    }
                                    g2d.setColor(coordinate.getColor());
                                    if (tracktype == 1) {
                                        g2d.drawOval((int) (coordinate.getX() - 2), (int) (coordinate.getY() - 2), 4, 4);
                                    }


                                }
                                int x = (int) storm.getX();
                                int y = (int) storm.getY();
                                g2d.setColor(new Color(0, 0, 0));
                                g2d.drawString(storm.getName(), x, y);

                                if (storm.getStormType().getStormType() == 0) {
                                    g2d.setColor(storm.getCategory(storm.getWinds()));
                                    g2d.fillOval(x, y, 30, 30);
                                    g2d.setColor(new Color(0, 0, 0));
                                    if (storm.getLetter().length() > 1) {
                                        g2d.drawString(storm.getLetter(), x + 7, y + 19);
                                    } else {
                                        g2d.drawString(storm.getLetter(), x + 12, y + 19);
                                    }

                                } else {
                                    g2d.setColor(Color.DARK_GRAY);
                                    g2d.fillOval(x, y, 30, 30);
                                    g2d.setColor(new Color(0, 0, 0));
                                    g2d.drawString("EX", x + 5, y + 15);


                                }


                            } else {
                                for (Coordinate coordinate : storm.getHistory()) {
                                    g2d.setColor(coordinate.getColor());

                                    if (storm.getHistory().indexOf(coordinate) > 0) {
                                        ArrayList<Coordinate> coordinates = storm.getHistory();
                                        if (tracktype == 1) {
                                            g2d.setColor(coordinate.getColor());
                                            g2d.drawOval((int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getX() - 2), (int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getY() - 2), 4, 4);

                                        }
                                        if (tracktype == 1) {

                                            g2d.setColor(Color.BLACK);
                                        }
                                        g2d.setStroke(new BasicStroke(2));
                                        g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getX(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getY());
                                    }
                                    g2d.setColor(coordinate.getColor());
                                    if (tracktype == 1) {
                                        g2d.drawOval((int) (coordinate.getX() - 2), (int) (coordinate.getY() - 2), 4, 4);
                                    }


                                }
                                int x = (int) storm.getX();
                                int y = (int) storm.getY();
                                g2d.setColor(new Color(0, 0, 0));
                                g2d.drawString(storm.getName(), x, y);
                                if (storm.getStormType().getStormType() == 0) {
                                    if (!storm.isFormed()) {
                                        if (storm.getChanceForm() < 30) {
                                            g2d.setColor(new Color(255, 246, 88));

                                        } else if (storm.getChanceForm() < 70) {
                                            g2d.setColor(new Color(255, 177, 88));

                                        } else {
                                            g2d.setColor(new Color(255, 94, 88));

                                        }

                                        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
                                        g2d.drawString("X", x , y+20);


                                    } else {
                                        g2d.setColor(Color.LIGHT_GRAY);
                                        g2d.fillOval(x, y, 30, 30);
                                        g2d.setColor(new Color(0, 0, 0));
                                        g2d.drawString("L", x + 12, y + 19);

                                    }

                                } else {
                                    g2d.setColor(Color.DARK_GRAY);
                                    g2d.fillOval(x, y, 30, 30);
                                    g2d.setColor(new Color(0, 0, 0));
                                    g2d.drawString("EX", x + 7, y + 15);


                                }


                            }

                            g2d.dispose();

                        } else {

                            executionQueue.add(storm);
                            coordinateToAdd.add(storm.getHistory());
                        }
                    }


                }
                if (gusts) {
                    int x2;
                    int y2 = 0;
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil( 1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            double highestWinds = 0;

                            int x = x2 + (SimResolution / 2);
                            int y = y2 + (SimResolution / 2);
                            for (Storm storm : storms) {
                                //((x2-x1)²+(y2-y1)²)
                                double winds;
                                double distance = Math.sqrt((y - storm.getY()) * (y - storm.getY()) + (x - storm.getX()) * (x - storm.getX()));
                                winds = Math.sqrt(((15 * storm.getWinds()) * (storm.getStormSize()/2.3)) / (distance / 3))*1.2 + myRandom(-1,1);
                                if (winds > storm.getWinds() * 1.2) {
                                    winds = storm.getWinds() * 1.2;
                                }
                                if (winds > highestWinds) {
                                    highestWinds = winds;
                                }
                            }
                            Storm colorthing = new Storm(30, 0, 0);
                            if (highestWinds < 25) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else {
                                g2d.setColor(colorthing.getCategory(highestWinds));
                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }

                }
                if (sst) {
                    int x2;
                    int y2 = 0;
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil( 1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            int prevTemp = y2 + SimResolution / 2;
                            double heat = (double)prevTemp / 10.0D - shearToMonth;
                            if (heat < -10.0D) {
                                g2d.setColor(new Color(0, 60, 255));
                            } else if (heat < -7.0D) {
                                g2d.setColor(new Color(0, 102, 255));
                            } else if (heat < -4.0D) {
                                g2d.setColor(new Color(0, 157, 255));
                            } else if (heat < -1.0D) {
                                g2d.setColor(new Color(4, 201, 255));
                            } else if (heat < 2.0D) {
                                g2d.setColor(new Color(0, 238, 246));
                            } else if (heat < 5.0D) {
                                g2d.setColor(new Color(0, 255, 207));
                            } else if (heat < 8.0D) {
                                g2d.setColor(new Color(2, 255, 129));
                            } else if (heat < 11.0D) {
                                g2d.setColor(new Color(0, 255, 21));
                            } else if (heat < 14.0D) {
                                g2d.setColor(new Color(136, 236, 13));
                            } else if (heat < 17.0D) {
                                g2d.setColor(new Color(212, 243, 11));
                            } else if (heat < 20.0D) {
                                g2d.setColor(new Color(246, 215, 15));
                            } else if (heat < 23.0D) {
                                g2d.setColor(new Color(253, 193, 0));
                            } else if (heat < 26.0D) {
                                g2d.setColor(new Color(253, 143, 0));
                            } else if (heat < 29.0D) {
                                g2d.setColor(new Color(253, 105, 0));
                            } else if (heat < 32.0D) {
                                g2d.setColor(new Color(253, 76, 0));
                            } else if (heat < 35.0D) {
                                g2d.setColor(new Color(253, 30, 0));
                            } else if (heat < 38.0D) {
                                g2d.setColor(new Color(253, 0, 97));
                            } else if (heat < 41.0D) {
                                g2d.setColor(new Color(253, 0, 173));
                            } else if (heat < 44.0D) {
                                g2d.setColor(new Color(253, 0, 232));
                            } else if (heat < 47.0D) {
                                g2d.setColor(new Color(239, 61, 235));
                            } else {
                                g2d.setColor(new Color(238, 113, 236));
                            }

                            g2d.fillRect(x2, y2, CycloneSimulator.this.SimResolution, CycloneSimulator.this.SimResolution);
                            x2 += CycloneSimulator.this.SimResolution;
                        }

                        y2 += CycloneSimulator.this.SimResolution;
                    }
                }
                if (tcchance) {
                    int x2;
                    int y2 = 0;
                    ArrayList<ArrayList<Coordinate>> trackForecasts = new ArrayList<>();

                    for (Storm storm : storms) {
                        trackForecasts.add(storm.getTrackForecast(landList));


                    }
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil(1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            double tsWindChance = 0;
                            double maxWinds = 0;
                            for (ArrayList<Coordinate> trackForecast : trackForecasts) {

                                Storm storm = storms.get(trackForecasts.indexOf(trackForecast));
                                for (Coordinate coordinate : trackForecast) {
                                    //int[] presetWinds = {25,35,55,85,100,120,137,160};
                                    double distance = Math.sqrt(((y2 + (SimResolution / 2.0) - coordinate.getY()) * ((y2 + (SimResolution / 2.0)) - coordinate.getY()) + ((x2 + (SimResolution / 2.0)) - coordinate.getX()) * ((x2 + (SimResolution / 2.0)) - coordinate.getX())));
                                    if (distance < 3) {
                                        distance = 3;
                                    }
                                    double winds = Math.sqrt(((15 * storm.getWinds()) * (storm.getStormSize()/2.3)) / ((distance) / 3) );
                                    double wChance;

                                    if (winds > 60) {
                                        wChance = 100;
                                    } else {
                                        wChance = ((winds-30)/10)*100;
                                    }
                                    double oldWChance = wChance;
                                    wChance*=(double) (trackForecast.size()- (trackForecast.indexOf(coordinate)))/(trackForecast.size());
                                    if (wChance > oldWChance) {
                                        wChance = oldWChance;
                                    }
                                    if (wChance > tsWindChance) {
                                        tsWindChance=wChance;
                                    }

                                    //tsWindChance -= trackForecast.indexOf(coordinate) * 3;
                                    if (winds > maxWinds) {
                                        maxWinds=winds;

                                    }

                                }
                            }
                            if (tsWindChance <= 20) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else {
                                if (tsWindChance < 40) {
                                    g2d.setColor(new Color(255, 242, 0));
                                } else {
                                    if (tsWindChance < 65 ) {
                                        g2d.setColor(new Color(255, 174, 0));

                                    } else {
                                        if (tsWindChance < 80) {
                                            g2d.setColor(new Color(255, 98, 0));
                                        } else {
                                            g2d.setColor(new Color(255, 0, 0));
                                        }
                                    }
                                }
                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }

                }
                if (huchance) {
                    int x2;
                    int y2 = 0;
                    ArrayList<ArrayList<Coordinate>> trackForecasts = new ArrayList<>();

                    for (Storm storm : storms) {
                        trackForecasts.add(storm.getTrackForecast(landList));


                    }
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil(1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            double tsWindChance = 0;
                            double maxWinds = 0;
                            for (ArrayList<Coordinate> trackForecast : trackForecasts) {

                                Storm storm = storms.get(trackForecasts.indexOf(trackForecast));
                                for (Coordinate coordinate : trackForecast) {
                                    //int[] presetWinds = {25,35,55,85,100,120,137,160};
                                    double distance = Math.sqrt(((y2 + (SimResolution / 2.0) - coordinate.getY()) * ((y2 + (SimResolution / 2.0)) - coordinate.getY()) + ((x2 + (SimResolution / 2.0)) - coordinate.getX()) * ((x2 + (SimResolution / 2.0)) - coordinate.getX())));
                                    if (distance < 3) {
                                        distance = 3;
                                    }
                                    double winds = Math.sqrt(((15 * storm.getWinds()) * (storm.getStormSize()/2.3)) / ((distance) / 3.0));
                                    double wChance;

                                    if (winds > 90) {
                                        wChance = 100;
                                    } else {
                                        wChance = ((winds-70)/20)*100;
                                    }
                                    double oldWChance = wChance;
                                    wChance*=(double) (trackForecast.size()- (trackForecast.indexOf(coordinate)))/(trackForecast.size());
                                    if (wChance > oldWChance) {
                                        wChance = oldWChance;
                                    }

                                    if (wChance > tsWindChance) {
                                        tsWindChance=wChance;
                                    }

                                    //tsWindChance -= trackForecast.indexOf(coordinate) * 3;
                                    if (winds > maxWinds) {
                                        maxWinds=winds;

                                    }

                                }
                            }
                            if (tsWindChance <= 10) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else {
                                if (tsWindChance < 50) {
                                    g2d.setColor(new Color(255, 242, 0));
                                } else {
                                    if (tsWindChance < 70 ) {
                                        g2d.setColor(new Color(255, 174, 0));

                                    } else {

                                        if (tsWindChance < 90) {
                                            g2d.setColor(new Color(255, 98, 0));
                                        } else {
                                            g2d.setColor(new Color(255, 0, 0));
                                        }
                                    }
                                }
                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }

                }
                if (surge) {
                    int x2;
                    int y2 = 0;
                    int highest = 0;
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil( 1000.0 / (double) SimResolution); j++) {
                            int x = x2 + (SimResolution / 2);
                            int y = y2 + (SimResolution / 2);
                            boolean inLand = false;
                            for (Land land: landList) {
                                if (land.inLandBorder(x,y, SimResolution)) {
                                    inLand=true;
                                    break;
                                }
                            }
                            Graphics2D g2d = (Graphics2D) g.create();

                            if (inLand) {
                                g2d.setColor(new Color(168, 165, 165));
                                g2d.fillRect(x2, y2, SimResolution, SimResolution);
                                x2 = x2 + SimResolution;
                                continue;
                            }
                            double sustainedWinds;
                            double highestWinds = 0;



                            for (Storm storm : storms) {
                                //((x2-x1)²+(y2-y1)²)

                                double winds;
                                double offsetX = storm.getX()+(storm.xChange/4);
                                double offsetY = storm.getY()+(storm.yChange/4);


                                double distance = Math.sqrt((y - offsetY) * (y - offsetY) + (x - offsetX) * (x - offsetX));
                                winds = Math.sqrt(((15 * storm.getWinds()) * ((storm.getStormSize()+20)/2)) / (distance / 3)) / (Math.cbrt(storm.getMovementSpeed()+5)) ;
                                if (winds > storm.getWinds()) {
                                    winds = storm.getWinds();
                                }
                                if (winds > highestWinds) {
                                    highestWinds = winds;
                                }

                            }

                            sustainedWinds = highestWinds;
                            double waveHeight = calcSigWave(sustainedWinds);
                            if (waveHeight>highest) {
                                highest=(int) Math.round(waveHeight);
                            }
                            if (waveHeight < 2) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else if (waveHeight < 4){
                                g2d.setColor(new Color(117, 255, 117));
                            }else if (waveHeight < 7){
                                g2d.setColor(new Color(184, 255, 108));
                            }else if (waveHeight < 10){
                                g2d.setColor(new Color(234, 255, 105));
                            }else if (waveHeight < 13){
                                g2d.setColor(new Color(255, 236, 116));
                            }else if (waveHeight < 16){
                                g2d.setColor(new Color(255, 209, 111));
                            }else if (waveHeight < 19){
                                g2d.setColor(new Color(255, 183, 113));
                            }else if (waveHeight < 23){
                                g2d.setColor(new Color(255, 158, 106));
                            }else if (waveHeight < 26){
                                g2d.setColor(new Color(255, 130, 104));
                            }else if (waveHeight < 30){
                                g2d.setColor(new Color(255, 108, 108));
                            } else {
                                g2d.setColor(new Color(255, 109, 215));

                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }
                    Graphics2D g2d = (Graphics2D) g.create();

                    g2d.setColor(new Color(217, 217, 217));

                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 9));
                    g2d.fillRect(820, 0, 180, 40);


                    g2d.setColor(new Color(168, 165, 165));
                    g2d.fillRect(840, 0, 15, 15);
                    g2d.setColor(new Color(117, 255, 117));
                    g2d.fillRect(855, 0, 15, 15);
                    g2d.setColor(new Color(184, 255, 108));
                    g2d.fillRect(870, 0, 15, 15);
                    g2d.setColor(new Color(234, 255, 105));
                    g2d.fillRect(885, 0, 15, 15);
                    g2d.setColor(new Color(255, 236, 116));
                    g2d.fillRect(900, 0, 15, 15);
                    g2d.setColor(new Color(255, 209, 111));
                    g2d.fillRect(915, 0, 15, 15);
                    g2d.setColor(new Color(255, 183, 113));
                    g2d.fillRect(930, 0, 15, 15);
                    g2d.setColor(new Color(255, 158, 106));
                    g2d.fillRect(945, 0, 15, 15);
                    g2d.setColor(new Color(255, 108, 108));
                    g2d.fillRect(960, 0, 15, 15);
                    g2d.setColor(new Color(255, 109, 215));
                    g2d.fillRect(975, 0, 15, 15);
                    g2d.setColor(new Color(0, 0, 0));

                    g2d.drawString("0", 838, 25);
                    g2d.drawString("2", 852, 25);
                    g2d.drawString("4", 867, 25);
                    g2d.drawString("7", 880, 25);
                    g2d.drawString("10", 895, 25);
                    g2d.drawString("13", 909, 25);
                    g2d.drawString("16", 924, 25);
                    g2d.drawString("19", 939, 25);
                    g2d.drawString("23", 954, 25);
                    g2d.drawString("26", 969, 25);
                    g2d.drawString("30", 984, 25);
                    g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 7));

                    g2d.drawString("FT", 824, 25);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));

                    g2d.drawString("Highest Height: " + highest + " FT", 824, 35);



                }
                if (threat) {
                    int x2;
                    int y2 = 0;
                    ArrayList<ArrayList<Coordinate>> trackForecasts = new ArrayList<>();

                    for (Storm storm : storms) {
                        trackForecasts.add(storm.getTrackForecast(landList));


                    }
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil(1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            int x = x2 + (SimResolution / 2);
                            int y = y2 + (SimResolution / 2);
                            double tsWindChance = 0;
                            double huWindChance = 0;
                            double confidenceTS = 0;
                            double confidenceHU = 0;
                            boolean inLand = false;
                            for (Land land: landList) {
                                if (land.inLandBorder(x,y, SimResolution)) {
                                    inLand=true;
                                } else {
                                    for (int l = -1; l < 2; l++) {
                                        for (int m = -1; m < 2; m++) {
                                            int tx = x2 + (SimResolution / 2) + (SimResolution * l);
                                            int ty = y2 + (SimResolution / 2) + (SimResolution * m);
                                            if (land.inLandBorder(tx,ty, SimResolution)) {
                                                inLand = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (!inLand) {
                                g2d.setColor(new Color(168, 165, 165));
                                g2d.fillRect(x2, y2, SimResolution, SimResolution);
                                x2 = x2 + SimResolution;
                                continue;
                            }
                            for (ArrayList<Coordinate> trackForecast : trackForecasts) {

                                Storm storm = storms.get(trackForecasts.indexOf(trackForecast));

                                for (Coordinate coordinate : trackForecast) {
                                    //int[] presetWinds = {25,35,55,85,100,120,137,160};
                                    double distance = Math.sqrt(((y2 + (SimResolution / 2.0) - coordinate.getY()) * ((y2 + (SimResolution / 2.0)) - coordinate.getY()) + ((x2 + (SimResolution / 2.0)) - coordinate.getX()) * ((x2 + (SimResolution / 2.0)) - coordinate.getX())));
                                    if (distance < 3) {
                                        distance = 3;
                                    }
                                    //Math.sqrt(((15 * storm.getWinds()) * (storm.getStormSize()/2.3)) / (distance / 3))
                                    double winds = Math.sqrt(((15 * coordinate.getWind()) * (storm.getStormSize()/2.3)) / ((distance) / 3) );
                                    double wChance;
                                    double hChance;
                                    if (winds > coordinate.getWind()) {
                                        winds = coordinate.getWind();
                                    }

                                    if (winds > 60) {
                                        wChance = 100;
                                    } else {
                                        wChance = ((winds-30)/10)*100;
                                    }
                                    double oldWChance = wChance;
                                    wChance*=(double) (trackForecast.size()- (trackForecast.indexOf(coordinate)))/(trackForecast.size());
                                    if (wChance > oldWChance) {
                                        wChance = oldWChance;
                                    }
                                    if (wChance > tsWindChance) {
                                        tsWindChance=wChance;
                                        confidenceTS = trackForecast.indexOf(coordinate);
                                    }
                                    if (winds > 90) {
                                        hChance = 100;
                                    } else {
                                        hChance = ((winds-70)/20)*100;
                                    }
                                    double oldHChance = hChance;
                                    hChance*=(double) (trackForecast.size()- (trackForecast.indexOf(coordinate)))/(trackForecast.size());
                                    if (hChance > oldHChance) {
                                        hChance = oldHChance;
                                    }

                                    if (hChance > huWindChance) {
                                        huWindChance=hChance;
                                        confidenceHU = trackForecast.indexOf(coordinate);
                                    }


                                }
                            }
                            if (!inLand) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else {
                                if (tsWindChance > 60) {
                                    if (huWindChance > 60) {
                                        g2d.setColor(new Color(255, 0, 0));
                                    } else {
                                        if (confidenceHU > 4 && huWindChance > 30) {
                                            g2d.setColor(new Color(242, 177, 172));
                                        } else if (tsWindChance > 80){
                                            g2d.setColor(new Color(0, 71, 255));
                                        } else {
                                            if (confidenceTS > 3) {
                                                g2d.setColor(new Color(255, 225, 0));

                                            } else {
                                                g2d.setColor(new Color(168, 165, 165));

                                            }
                                        }
                                    }
                                } else {
                                    if (confidenceTS > 3 && tsWindChance > 30) {
                                        g2d.setColor(new Color(255, 225, 0));

                                    } else {
                                        g2d.setColor(new Color(168, 165, 165));

                                    }
                                }
                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }

                }
                if (sWind) {
                    int x2;
                    int y2 = 0;
                    for (int i = 0; i < Math.ceil( 500.0 / (double) SimResolution); i++) {
                        x2 = 0;
                        for (int j = 0; j < Math.ceil( 1000.0 / (double) SimResolution); j++) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            double sustainedWinds;
                            double highestWinds = 0;

                            int x = x2 + (SimResolution / 2);
                            int y = y2 + (SimResolution / 2);

                            for (Storm storm : storms) {
                                //((x2-x1)²+(y2-y1)²)

                                double winds;

                                double distance = Math.sqrt((y - storm.getY()) * (y - storm.getY()) + (x - storm.getX()) * (x - storm.getX()));
                                winds = Math.sqrt(((15 * storm.getWinds()) * (storm.getStormSize()/2.3)) / (distance / 3)) + myRandom(-1,1);
                                if (winds > storm.getWinds()) {
                                    winds = storm.getWinds();
                                }
                                if (winds > highestWinds) {
                                    highestWinds = winds;
                                }

                            }

                            sustainedWinds = highestWinds;
                            Storm colorthing = new Storm(30, 0, 0);
                            if (sustainedWinds < 25) {
                                g2d.setColor(new Color(168, 165, 165));
                            } else {
                                g2d.setColor(colorthing.getCategory(sustainedWinds));
                            }
                            g2d.fillRect(x2, y2, SimResolution, SimResolution);
                            x2 = x2 + SimResolution;

                        }
                        y2 = y2 + SimResolution;
                    }

                }
                if (sWind || gusts || sst || tcchance || surge || huchance || threat) {
                    for (Land lands : landList) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setStroke(new BasicStroke(1));
                        g2d.drawRect((int) lands.getCoordinates().get(0).getX(), (int) lands.getCoordinates().get(0).getY(), lands.getxSize(), lands.getySize());
                    }
                    for (ArrayList<Coordinate> storm : coordinateToAdd) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        for (Coordinate coordinate : storm) {
                            g2d.setColor(coordinate.getColor());

                            if (storm.indexOf(coordinate) > 0) {
                                if (tracktype == 1) {
                                    g2d.setColor(coordinate.getColor());
                                    g2d.fillOval((int) (storm.get(storm.indexOf(coordinate) - 1).getX() - 2), (int) (storm.get(storm.indexOf(coordinate) - 1).getY() - 2), 4, 4);

                                }
                                if (tracktype == 1) {

                                    g2d.setColor(Color.BLACK);
                                }
                                g2d.setStroke(new BasicStroke(1));
                                g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) storm.get(storm.indexOf(coordinate) - 1).getX(), (int) storm.get(storm.indexOf(coordinate) - 1).getY());
                            }
                            g2d.setColor(coordinate.getColor());
                            if (tracktype == 1) {
                                g2d.fillOval((int) (coordinate.getX() - 2), (int) (coordinate.getY() - 2), 4, 4);
                            }
                        }

                    }
                    for (Storm storm : storms) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        for (Coordinate coordinate : storm.getHistory()) {
                            g2d.setColor(coordinate.getColor());

                            if (storm.getHistory().indexOf(coordinate) > 0) {
                                ArrayList<Coordinate> coordinates = storm.getHistory();
                                if (tracktype == 1) {
                                    g2d.setColor(coordinate.getColor());
                                    g2d.fillOval((int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getX() - 2), (int) (coordinates.get(coordinates.indexOf(coordinate) - 1).getY() - 2), 4, 4);

                                }
                                if (tracktype == 1) {

                                    g2d.setColor(Color.BLACK);
                                }
                                g2d.setStroke(new BasicStroke(1));
                                g2d.drawLine((int) coordinate.getX(), (int) coordinate.getY(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getX(), (int) coordinates.get(coordinates.indexOf(coordinate) - 1).getY());
                            }
                            g2d.setColor(coordinate.getColor());
                            if (tracktype == 1) {
                                g2d.fillOval((int) (coordinate.getX() - 2), (int) (coordinate.getY() - 2), 4, 4);
                            }

                        }
                    }
                }
                if (showingStormInfo == null && showingBuoyInfo == null && showingLandInfo == null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(189, 188, 183));
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                    g2d.fillRect(1000, 0, 200, 500);
                    g2d.setColor(new Color(0, 0, 0));
                    g2d.drawString("Season Stats", 1010, 30);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                    g2d.drawString("Low Pressures: " + lows, 1010, 50);

                    g2d.drawString("Tropical Depressions: " + tds, 1010, 70);
                    g2d.drawString("Tropical Storms: " + numstorms, 1010, 90);
                    g2d.drawString("Hurricanes: " + hurricanes, 1010, 110);
                    g2d.drawString("Major Hurricanes: " + majors, 1010, 130);
                    g2d.drawString("C5 Hurricanes: " + c5, 1010, 150);
                    g2d.drawString("Landfalls: " + landfalls.size(), 1010, 170);
                    g2d.drawString("Frame Delay: " + frameDelay, 1010, 190);
                    g2d.drawString("Damages: $" + damages + symbol, 1010, 210);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                    g2d.drawString("Current Storms", 1010, 240);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                    int yPlace = 260;
                    for (Storm storm : storms) {
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                        g2d.setColor(storm.getCategory(storm.getWinds()));
                        g2d.drawString(storm.getName() + ": " + Math.round(storm.getWinds()) + " mph", 1010, yPlace);
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                        g2d.drawString(storm.getHeadline(), 1010, yPlace + 16);
                        yPlace = yPlace + 36;
                    }
                    if (storms.size() < 5) {
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
                        g2d.drawString("Buoys", 1010, 405);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        Buoy one;
                        Buoy two;
                        Buoy three;
                        Buoy four;
                        ArrayList<Buoy> buoyslist = new ArrayList<>();
                        for (Buoy buoy : buoys) {

                            int placetoput = buoyslist.size();
                            if (!buoyslist.isEmpty()) {
                                for (int i = buoyslist.size() - 1; i > -1; i--) {
                                    if (buoyslist.get(i).getSustainedWinds() < buoy.getSustainedWinds()) {
                                        placetoput--;
                                    }
                                }
                                buoyslist.add(placetoput, buoy);
                            } else {
                                buoyslist.add(buoy);
                            }
                        }
                        one = buoyslist.get(0);
                        two = buoyslist.get(1);
                        three = buoyslist.get(2);
                        four = buoyslist.get(3);
                        Storm coloruse = new Storm(30, 1, 0);
                        g2d.setColor(coloruse.getCategory(one.getSustainedWinds()));
                        g2d.drawString("Buoy#" + one.getNumber() + ": " + Math.round(one.getSustainedWinds()) + " mph", 1010, 425);
                        g2d.setColor(coloruse.getCategory(two.getSustainedWinds()));
                        g2d.drawString("Buoy#" + two.getNumber() + ": " + Math.round(two.getSustainedWinds()) + " mph", 1010, 445);
                        g2d.setColor(coloruse.getCategory(three.getSustainedWinds()));
                        g2d.drawString("Buoy#" + three.getNumber() + ": " + Math.round(three.getSustainedWinds()) + " mph", 1010, 465);
                        g2d.setColor(coloruse.getCategory(four.getSustainedWinds()));
                        g2d.drawString("Buoy#" + four.getNumber() + ": " + Math.round(four.getSustainedWinds()) + " mph", 1010, 485);

                    }


                } else {
                    if (showingStormInfo != null) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setColor(new Color(189, 188, 183));
                        g2d.fillRect(1000, 0, 200, 500);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                        g2d.setColor(new Color(0, 0, 0));
                        g2d.drawString(showingStormInfo.getName(), 1010, 25);
                        g2d.setColor(showingStormInfo.getCategory(showingStormInfo.getWinds()));
                        g2d.fillOval(1075, 30, 50, 50);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        g2d.setColor(new Color(0, 0, 0));
                        g2d.drawString(showingStormInfo.getName(), 1085, 60);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
                        g2d.drawString(showingStormInfo.getHeadline(), 1010, 95);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        String unitD = "M";
                        double damages = showingStormInfo.getDamages();
                        if (damages > 1000) {
                            damages/=1000;
                            unitD = "B";
                        }
                        String dissipated = "";
                        if (showingStormInfo.isDead()) {
                            dissipated=Year.of(year).atDay(showingStormInfo.getStartDay() + (showingStormInfo.getHistory().size() / 8)).toString();
                        }
                        g2d.drawString("Winds: " + Math.round(showingStormInfo.getWinds()) + " mph", 1010, 115);
                        g2d.drawString("Peak: " + Math.round(showingStormInfo.getWindPeak()) + " mph", 1010, 135);
                        g2d.drawString("Damages: $" + round(damages,2) + unitD, 1010, 155);
                        g2d.drawString("Lasted: ", 1010, 175);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));

                        g2d.drawString(Year.of(year).atDay(showingStormInfo.getStartDay()) + " - " + dissipated, 1010, 195);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));

                        if (showingStormInfo.getStormType().getStormType() == 0) {
                            g2d.drawString("Type: Tropical", 1010, 215);
                        } else {
                            g2d.drawString("Type: Extratropical", 1010, 215);
                        }
                        g2d.drawString("ACE: " + showingStormInfo.getAce() , 1010, 235);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
//                        g2d.drawString("Forecast Models", 1010, 240);
//                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
//                        g2d.drawString("Highest predicted category", 1010, 255);
//                        g2d.drawString(" out of 15 model runs", 1010, 272);
//                        ArrayList<Integer> peaks = new ArrayList<>();
//                        for (int i = 0; i < 8; i++) {
//                            peaks.add(0);
//                        }
//                        for (int i = 0; i < 15; i++) {
//                            int largest = 0;
//                            for (Coordinate coordinate : showingStormInfo.getTrackForecast(landList)) {
//                                if (colors.indexOf(coordinate.getColor()) > largest) {
//                                    largest = colors.indexOf(coordinate.getColor());
//                                }
//                            }
//                            peaks.set(largest, peaks.get(largest) + 1);
//                        }
//                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
//                        g2d.setColor(colors.get(0));
//                        g2d.drawString("Low: " + peaks.get(0), 1010, 290);
//                        g2d.setColor(colors.get(1));
//                        g2d.drawString("TD: " + peaks.get(1), 1010, 310);
//                        g2d.setColor(colors.get(2));
//                        g2d.drawString("TS: " + peaks.get(2), 1010, 330);
//                        g2d.setColor(colors.get(3));
//                        g2d.drawString("Cat 1: " + peaks.get(3), 1010, 350);
//                        g2d.setColor(colors.get(4));
//                        g2d.drawString("Cat 2: " + peaks.get(4), 1010, 370);
//                        g2d.setColor(colors.get(5));
//                        g2d.drawString("Cat 3: " + peaks.get(5), 1010, 390);
//                        g2d.setColor(colors.get(6));
//                        g2d.drawString("Cat 4: " + peaks.get(6), 1010, 410);
//                        g2d.setColor(colors.get(7));
//                        g2d.drawString("Cat 5: " + peaks.get(7), 1010, 430);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                        g2d.setColor(Color.BLACK);
                        g2d.drawString("Envr. Conditions", 1010, 455);
                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        g2d.drawString("Shear: ", 1010, 475);
                        if (showingStormInfo.getOldShear() < 2) {
                            g2d.setColor(new Color(0, 224, 255));
                            g2d.drawString("None", 1100, 475);
                        } else {
                            if (showingStormInfo.getOldShear() < 6) {
                                g2d.setColor(new Color(75, 255, 0));
                                g2d.drawString("Very Low", 1100, 475);
                            } else {
                                if (showingStormInfo.getOldShear() < 10) {
                                    g2d.setColor(new Color(255, 243, 0));
                                    g2d.drawString("Low", 1100, 475);
                                } else {
                                    if (showingStormInfo.getOldShear() < 14) {
                                        g2d.setColor(new Color(255, 183, 0));
                                        g2d.drawString("Moderate", 1100, 475);
                                    } else {
                                        if (showingStormInfo.getOldShear() < 18) {
                                            g2d.setColor(new Color(255, 102, 0));
                                            g2d.drawString("High", 1100, 475);
                                        } else {
                                            g2d.setColor(new Color(255, 0, 0));
                                            g2d.drawString("Very High", 1100, 475);
                                        }
                                    }
                                }
                            }
                        }
                        g2d.setColor(new Color(0, 0, 0));
                        double heat = showingStormInfo.getY() / 10 - shearToMonth;
                        g2d.drawString("SSTs: ", 1010, 495);
                        if (heat < 13) {
                            g2d.setColor(new Color(0, 224, 255));
                            g2d.drawString("Very Cold", 1100, 495);
                        } else {
                            if (heat < 20) {
                                g2d.setColor(new Color(75, 255, 0));
                                g2d.drawString("Cold", 1100, 495);
                            } else {
                                if (heat < 27) {
                                    g2d.setColor(new Color(255, 243, 0));
                                    g2d.drawString("Warm", 1100, 495);
                                } else {
                                    if (heat < 35) {
                                        g2d.setColor(new Color(255, 183, 0));
                                        g2d.drawString("Very Warm", 1100, 495);
                                    } else {
                                        if (heat < 43) {
                                            g2d.setColor(new Color(255, 102, 0));
                                            g2d.drawString("Hot", 1100, 495);
                                        } else {
                                            g2d.setColor(new Color(255, 0, 0));
                                            g2d.drawString("Very Hot", 1100, 495);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (showingBuoyInfo != null) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setColor(new Color(189, 188, 183));
                            g2d.fillRect(1000, 0, 200, 500);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                            g2d.setColor(new Color(0, 0, 0));
                            g2d.drawString("Buoy #" + showingBuoyInfo.getNumber(), 1010, 25);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            g2d.drawString("Gusts: " + showingBuoyInfo.getGusts() + "mph", 1010, 45);
                            g2d.drawString("Winds: " + showingBuoyInfo.getSustainedWinds() + "mph", 1010, 65);
                            g2d.drawString("Wave Height: " + showingBuoyInfo.getWaveheight() + "ft", 1010, 85);

                        } else {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setColor(new Color(189, 188, 183));
                            g2d.fillRect(1000, 0, 200, 500);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                            g2d.setColor(new Color(0, 0, 0));
                            g2d.drawString(showingLandInfo.getLandName(), 1010, 25);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            String unitD = "M";
                            double damages = showingLandInfo.getDamages();
                            if (damages > 1000) {
                                damages/=1000;
                                unitD = "B";
                            }
                            ArrayList<Color> threatColors = new ArrayList<>();
                            int wThreat;
                            int sThreat;
                            int rThreat;
                            if (showingLandInfo.getWindThreat() < 20) {
                                wThreat = 0;
                            } else if (showingLandInfo.getWindThreat() < 40) {
                                wThreat = 1;
                            } else if (showingLandInfo.getWindThreat() < 70) {
                                wThreat = 2;
                            } else if (showingLandInfo.getWindThreat() < 95) {
                                wThreat = 3;
                            }else if (showingLandInfo.getWindThreat() < 120) {
                                wThreat = 4;
                            } else if (showingLandInfo.getWindThreat() < 140) {
                                wThreat = 5;
                            } else {
                                wThreat = 6;
                            }
                            if (showingLandInfo.getSurgeThreat() < 1) {
                                sThreat = 0;
                            } else if (showingLandInfo.getSurgeThreat() < 3) {
                                sThreat = 1;
                            } else if (showingLandInfo.getSurgeThreat() < 6) {
                                sThreat = 2;
                            } else if (showingLandInfo.getSurgeThreat() < 9) {
                                sThreat = 3;
                            }else if (showingLandInfo.getSurgeThreat() < 12) {
                                sThreat = 4;
                            } else if (showingLandInfo.getSurgeThreat() < 15) {
                                sThreat = 5;
                            } else {
                                sThreat = 6;
                            }
                            if (showingLandInfo.getRainThreat() < 1) {
                                rThreat = 0;
                            } else if (showingLandInfo.getRainThreat() < 3) {
                                rThreat = 1;
                            } else if (showingLandInfo.getRainThreat() < 8) {
                                rThreat = 2;
                            } else if (showingLandInfo.getRainThreat() < 13) {
                                rThreat = 3;
                            }else if (showingLandInfo.getRainThreat() < 18) {
                                rThreat = 4;
                            } else if (showingLandInfo.getRainThreat() < 23) {
                                rThreat = 5;
                            } else {
                                rThreat = 6;
                            }

                            threatColors.add(new Color(100, 100, 100));
                            threatColors.add(new Color(200, 255, 117));
                            threatColors.add(new Color(249, 255, 77));
                            threatColors.add(new Color(255, 205, 77));
                            threatColors.add(new Color(255, 166, 77));
                            threatColors.add(new Color(255, 119, 77));
                            threatColors.add(new Color(255, 77, 77));


                            String[] levels = {"None", "TS Watch", "TS Warning", "HU Watch", "HU Warning"};
                            String[] threatLabels = {"None","Limited", "Minor", "Enhanced", "Major", "High", "Extreme"};

                            g2d.drawString("Threat: " + levels[showingLandInfo.getThreat()], 1010, 45);
                            g2d.drawString("Width: " + showingLandInfo.getxSize(), 1010, 65);
                            g2d.drawString("Length: " + showingLandInfo.getySize(), 1010, 85);
                            g2d.drawString("Damages: $" + round(damages,2) + unitD, 1010, 110);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
                            g2d.drawString("Threats", 1010, 135);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Wind", 1010, 160);
                            g2d.setColor(threatColors.get(wThreat));
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            g2d.drawString("Threat Level: " + threatLabels[wThreat], 1010, 180);
                            g2d.drawString("Max Wind: " + Math.round(showingLandInfo.getWindThreat()) + " mph", 1010, 200);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Surge", 1010, 225);
                            g2d.setColor(threatColors.get(sThreat));
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            g2d.drawString("Threat Level: " + threatLabels[sThreat], 1010, 245);
                            g2d.drawString("Max Surge: " + Math.round(showingLandInfo.getSurgeThreat()) + " ft", 1010, 265);
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Rain", 1010, 290);
                            g2d.setColor(threatColors.get(rThreat));
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            g2d.drawString("Threat Level: " + threatLabels[rThreat], 1010, 310);
                            g2d.drawString("Max Rain: " + Math.round(showingLandInfo.getRainThreat()) + " in", 1010, 330);

                            g2d.setColor(new Color(0,0,0));
                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

                            g2d.drawString("Storm Damages", 1010, 355);
                            int y = 355;

                            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                            for (Storm storm : showingLandInfo.getStormsAffected().keySet()) {
                                y = y + 20;
                                damages = showingLandInfo.getStormsAffected().get(storm);
                                String unit = "M";
                                if (damages > 1000) {
                                    damages/= 1000;
                                    unit = "B";
                                }
                                g2d.drawString(storm.getName() + ": $" + Math.round(damages) + unit, 1010, y);
                            }


                        }
                    }

                }
                if (t && showingStormInfo == null && showingLandInfo == null && showingBuoyInfo == null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(189, 188, 183));
                    g2d.fillRect(52, 50, 1095, 400);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRect(52, 50, 1095, 400);
                    int y = 90;
                    g2d.setStroke(new BasicStroke(1));
                    double y2 = 52;
                    for (int i = 0; i < 12; i++) {
                        g2d.drawLine((int) Math.round(y2), 50, (int) Math.round(y2), 450);
                        y2 = y2 + 91.5;
                    }
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                    for (Storm storm : history) {
                        g2d.setColor(storm.getCategory(storm.getWindPeak()));
                        g2d.fillRect(storm.getStartDay() * 3 + 52, y - 38, (int) Math.round(storm.getHistory().size() / 8.0) * 3 + 2, 28);
                        g2d.drawString(storm.getName(), (int) (storm.getStartDay() * 3 + 52 + (Math.round(storm.getHistory().size() / 8.0) * 3 + 2)) + 5, y - 15);
                        y = y + 30;
                        if (y > 440) {
                            y = 90;
                        }

                    }
                    for (Storm storm : storms) {
                        g2d.setColor(storm.getCategory(storm.getWindPeak()));
                        g2d.fillRect(storm.getStartDay() * 3 + 52, y - 38, (int) Math.round(storm.getHistory().size() / 8.0) * 3 + 2, 28);
                        g2d.drawString(storm.getName(), (int) (storm.getStartDay() * 3 + 52 + (Math.round(storm.getHistory().size() / 8.0) * 3 + 2)) + 5, y - 15);
                        y = y + 30;
                        if (y == 450) {
                            y = 90;
                        }

                    }

                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawRect(52, 50, 1095, 400);
                } else {
                    if (t && showingStormInfo != null) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setColor(new Color(189, 188, 183));
                        g2d.fillRect(25, 25, 1150, 450);

                        g2d.setColor(colors.get(0));
                        g2d.fillRect(80, 475 - (int) ((29) * 1.8), 1150 - 55, (int) (29 * 1.8));
                        g2d.setColor(colors.get(1));
                        g2d.fillRect(80, 473 - (int) ((29 + 8) * 1.8), 1150 - 55, (int) (9 * 1.8));
                        g2d.setColor(colors.get(2));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37) * 1.8), 1150 - 55, (int) (37 * 1.8));
                        g2d.setColor(colors.get(3));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22) * 1.8), 1150 - 55, (int) (22 * 1.8));
                        g2d.setColor(colors.get(4));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15) * 1.8), 1150 - 55, (int) (15 * 1.8));
                        g2d.setColor(colors.get(5));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19) * 1.8), 1150 - 55, (int) (20 * 1.8));
                        g2d.setColor(colors.get(6));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27) * 1.8), 1150 - 55, (int) (28 * 1.8));
                        g2d.setColor(colors.get(7));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27 + 93) * 1.8), 1150 - 55, (int) (93.5 * 1.8));
                        g2d.setColor(Color.BLACK);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawRect(25, 25, 1150, 450);

                        for (int i = 20; i < 220; i += 20) {
                            g2d.setColor(Color.BLACK);
                            g2d.drawString(i + " mph", 30, 475 - ((int) (i * 1.8)));
                        }
                        if (showingStormInfo.getWindHistory().size() > 1) {
                            double distanceBetweenPoints = (1150.0 - 55.0) / showingStormInfo.getWindHistory().size();
                            int x = 80;
                            int previousX = 0;
                            int previousWind = 0;
                            for (int wind : showingStormInfo.getWindHistory()) {
                                //System.out.println(wind);
                                if (showingStormInfo.getWindHistory().indexOf(wind) > 1) {

                                    g2d.setColor(Color.BLACK);
                                    g2d.drawLine(previousX, 475 - (int) (previousWind * 1.8), x, 475 - (int) (wind * 1.8));
                                    //g2d.drawLine(x,475- (int) (wind * 1.8), (int) (x + distanceBetweenPoints),475-(int) (showingStormInfo.getWindHistory().get(showingStormInfo.getWindHistory().indexOf(wind) + 1)*1.8));
                                }
                                previousX = x;
                                previousWind = wind;
                                x = (int) (x + distanceBetweenPoints);
                            }

                        }
                    } else {
                        if (t && showingStormInfo != null) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setColor(new Color(189, 188, 183));
                            g2d.fillRect(25, 25, 1150, 450);

                            g2d.setColor(colors.get(0));
                            g2d.fillRect(80, 475 - (int) ((29) * 1.8), 1150 - 55, (int) (29 * 1.8));
                            g2d.setColor(colors.get(1));
                            g2d.fillRect(80, 473 - (int) ((29 + 8) * 1.8), 1150 - 55, (int) (9 * 1.8));
                            g2d.setColor(colors.get(2));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37) * 1.8), 1150 - 55, (int) (37 * 1.8));
                            g2d.setColor(colors.get(3));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22) * 1.8), 1150 - 55, (int) (22 * 1.8));
                            g2d.setColor(colors.get(4));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15) * 1.8), 1150 - 55, (int) (15 * 1.8));
                            g2d.setColor(colors.get(5));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19) * 1.8), 1150 - 55, (int) (20 * 1.8));
                            g2d.setColor(colors.get(6));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27) * 1.8), 1150 - 55, (int) (28 * 1.8));
                            g2d.setColor(colors.get(7));
                            g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27 + 93) * 1.8), 1150 - 55, (int) (93.5 * 1.8));
                            g2d.setColor(Color.BLACK);
                            g2d.setStroke(new BasicStroke(3));
                            g2d.drawRect(25, 25, 1150, 450);
                            for (int j = 20; j < 220; j += 20) {
                                g2d.setColor(Color.BLACK);
                                g2d.drawString(j + " mph", 30, 475 - ((int) (j * 1.8)));
                            }

                            if (showingStormInfo.getWindHistory().size() > 1) {
                                double distanceBetweenPoints = ((1150.0 - 55.0) / showingStormInfo.getWindHistory().size());
                                double x = 80;
                                double previousX = 0;
                                double previousWind = 0;
                                for (int i = 1; i < showingStormInfo.getWindHistory().size(); i++) {

                                    double wind = showingStormInfo.getWindHistory().get(i);
                                    g2d.setColor(Color.BLACK);
                                    g2d.drawLine((int) previousX, 475 - (int) (previousWind * 1.8), (int) x, 475 - (int) (wind * 1.8));
                                    //g2d.drawLine(x,475- (int) (wind * 1.8), (int) (x + distanceBetweenPoints),475-(int) (showingStormInfo.getWindHistory().get(showingStormInfo.getWindHistory().indexOf(wind) + 1)*1.8));

                                    previousX = x;
                                    previousWind = wind;
                                    x = (int) (x + distanceBetweenPoints);
                                }


                            }

                        } else {
                            if (t && showingBuoyInfo != null) {
                                Graphics2D g2d = (Graphics2D) g.create();
                                g2d.setColor(new Color(189, 188, 183));
                                g2d.fillRect(25, 25, 1150, 450);

                                g2d.setColor(colors.get(0));
                                g2d.fillRect(80, 475 - (int) ((29) * 1.8), 1150 - 55, (int) (29 * 1.8));
                                g2d.setColor(colors.get(1));
                                g2d.fillRect(80, 473 - (int) ((29 + 8) * 1.8), 1150 - 55, (int) (9 * 1.8));
                                g2d.setColor(colors.get(2));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37) * 1.8), 1150 - 55, (int) (37 * 1.8));
                                g2d.setColor(colors.get(3));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22) * 1.8), 1150 - 55, (int) (22 * 1.8));
                                g2d.setColor(colors.get(4));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15) * 1.8), 1150 - 55, (int) (15 * 1.8));
                                g2d.setColor(colors.get(5));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19) * 1.8), 1150 - 55, (int) (20 * 1.8));
                                g2d.setColor(colors.get(6));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27) * 1.8), 1150 - 55, (int) (28 * 1.8));
                                g2d.setColor(colors.get(7));
                                g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27 + 93) * 1.8), 1150 - 55, (int) (93.5 * 1.8));
                                g2d.setColor(Color.BLACK);
                                g2d.setStroke(new BasicStroke(3));
                                g2d.drawRect(25, 25, 1150, 450);

                                for (int j = 20; j < 220; j += 20) {
                                    g2d.setColor(Color.BLACK);
                                    g2d.drawString(j + " mph", 30, 475 - ((int) (j * 1.8)));
                                }
                                if (showingBuoyInfo.getWindHistory().size() > 1) {
                                    double distanceBetweenPoints = ((1150.0 - 55) / showingBuoyInfo.getWindHistory().size());
                                    double x = 80;
                                    double previousX = 80;
                                    double previousWind = showingBuoyInfo.getWindHistory().get(0);
                                    for (int i = 1; i < showingBuoyInfo.getWindHistory().size(); i++) {

                                        double wind = showingBuoyInfo.getWindHistory().get(i);
                                        g2d.setColor(Color.BLUE);
                                        g2d.drawLine((int) previousX, 475 - (int) (previousWind * 1.8), (int) x, 475 - (int) (wind * 1.8));
                                        //g2d.drawLine(x,475- (int) (wind * 1.8), (int) (x + distanceBetweenPoints),475-(int) (showingStormInfo.getWindHistory().get(showingStormInfo.getWindHistory().indexOf(wind) + 1)*1.8));

                                        previousX = x;
                                        previousWind = wind;
                                        x = (x + distanceBetweenPoints);
                                    }

                                }
                                if (showingBuoyInfo.getGustHistory().size() > 1) {
                                    double distanceBetweenPoints = ((1150.0 - 55.0) / showingBuoyInfo.getGustHistory().size());
                                    double x = 80;
                                    double previousX = 80;
                                    double previousWind = showingBuoyInfo.getGustHistory().get(0);
                                    for (int i = 1; i < showingBuoyInfo.getGustHistory().size(); i++) {

                                        double wind = showingBuoyInfo.getGustHistory().get(i);
                                        g2d.setColor(Color.BLACK);
                                        g2d.drawLine((int) previousX, 475 - (int) (previousWind * 1.8), (int) x, 475 - (int) (wind * 1.8));
                                        //g2d.drawLine(x,475- (int) (wind * 1.8), (int) (x + distanceBetweenPoints),475-(int) (showingStormInfo.getWindHistory().get(showingStormInfo.getWindHistory().indexOf(wind) + 1)*1.8));

                                        previousX = x;
                                        previousWind = wind;
                                        x = (x + distanceBetweenPoints);
                                    }


                                }
                            }
                        }
                    }
                }
                if (trackforecast) {
                    if (showingStormInfo == null) {
                        trackforecast = false;
                    } else {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setColor(new Color(189, 188, 183));
                        g2d.fillRect(25, 25, 1150, 450);

                        g2d.setColor(colors.get(0));
                        g2d.fillRect(80, 475 - (int) ((29) * 1.8), 1150 - 55, (int) (29 * 1.8));
                        g2d.setColor(colors.get(1));
                        g2d.fillRect(80, 473 - (int) ((29 + 8) * 1.8), 1150 - 55, (int) (9 * 1.8));
                        g2d.setColor(colors.get(2));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37) * 1.8), 1150 - 55, (int) (37 * 1.8));
                        g2d.setColor(colors.get(3));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22) * 1.8), 1150 - 55, (int) (22 * 1.8));
                        g2d.setColor(colors.get(4));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15) * 1.8), 1150 - 55, (int) (15 * 1.8));
                        g2d.setColor(colors.get(5));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19) * 1.8), 1150 - 55, (int) (20 * 1.8));
                        g2d.setColor(colors.get(6));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27) * 1.8), 1150 - 55, (int) (28 * 1.8));
                        g2d.setColor(colors.get(7));
                        g2d.fillRect(80, 475 - (int) ((29 + 8 + 37 + 22 + 15 + 19 + 27 + 93) * 1.8), 1150 - 55, (int) (93.5 * 1.8));
                        g2d.setColor(Color.BLACK);
                        g2d.setStroke(new BasicStroke(3));
                        g2d.drawRect(25, 25, 1150, 450);

                        for (int j = 20; j < 220; j += 20) {
                            g2d.setColor(Color.BLACK);
                            g2d.drawString(j + " mph", 25, 475 - ((int) (j * 1.8)));
                        }
                        ArrayList<Color> modelcolors = new ArrayList<>();
                        modelcolors.add(new Color(74, 133, 78));
                        modelcolors.add(new Color(141, 132, 79));
                        modelcolors.add(new Color(136, 66, 40));
                        modelcolors.add(new Color(98, 65, 120));
                        modelcolors.add(new Color(93, 38, 56));

                        for (int i = 0; i < 5; i++) {
                            ArrayList<Double> forecast = showingStormInfo.getForecastWinds(landList);

                            if (forecast.size() > 1) {

                                double distanceBetweenPoints = ((1150.0 - 55) / forecast.size());
                                double x = 80;
                                double previousX = 80;
                                double previousWind = forecast.get(0);
                                for (int k = 1; k < forecast.size(); k++) {

                                    double wind = forecast.get(k);
                                    g2d.setColor(modelcolors.get(i));
                                    g2d.setStroke(new BasicStroke(2));
                                    g2d.drawLine((int) previousX, 475 - (int) (previousWind * 1.8), (int) x, 475 - (int) (wind * 1.8));
                                    //g2d.drawLine(x,475- (int) (wind * 1.8), (int) (x + distanceBetweenPoints),475-(int) (showingStormInfo.getWindHistory().get(showingStormInfo.getWindHistory().indexOf(wind) + 1)*1.8));

                                    previousX = x;
                                    previousWind = wind;
                                    x = (x + distanceBetweenPoints);
                                }

                            }
                        }
                    }
                }
                if (seasonSummary) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(new Color(189, 188, 183));
                    g2d.fillRect(25, 25, 1150, 450);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRect(25, 25, 1150, 450);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                    g2d.drawString("Season Summary", 30, 52);
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 23));
                    g2d.drawString("Season Details", 30, 77);
                    g2d.drawString("Strongest Storm", 280, 260);
                    g2d.drawString("Monthly Activity", 530, 77);
                    g2d.drawString("Season Forecasts", 740, 77);
                    g2d.drawString("Strongest Landfalls", 30, 260);
                    //top = 25 left = 25 right = 1175 bottom = 475
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                    g2d.drawString("Tropical Depressions: " + tds, 30, 97);
                    if (lows > 0) {
                        g2d.drawString("January: " + monthly[0] + " (" + Math.round(((double) monthly[0] / (double) lows) * 100) + "%)", 530, 97);
                        g2d.drawString("February: " + monthly[1] + " (" + Math.round(((double) monthly[1] / (double) lows) * 100) + "%)", 530, 117);
                        g2d.drawString("March: " + monthly[2] + " (" + Math.round(((double) monthly[2] / (double) lows) * 100) + "%)", 530, 137);
                        g2d.drawString("April: " + monthly[3] + " (" + Math.round(((double) monthly[3] / (double) lows) * 100) + "%)", 530, 157);
                        g2d.drawString("May: " + monthly[4] + " (" + Math.round(((double) monthly[4] / (double) lows) * 100) + "%)", 530, 177);
                        g2d.drawString("June: " + monthly[5] + " (" + Math.round(((double) monthly[5] / (double) lows) * 100) + "%)", 530, 197);
                        g2d.drawString("July: " + monthly[6] + " (" + Math.round(((double) monthly[6] / (double) lows) * 100) + "%)", 530, 217);
                        g2d.drawString("August: " + monthly[7] + " (" + Math.round(((double) monthly[7] / (double) lows) * 100) + "%)", 530, 237);
                        g2d.drawString("September: " + monthly[8] + " (" + Math.round(((double) monthly[8] / (double) lows) * 100) + "%)", 530, 257);
                        g2d.drawString("October: " + monthly[9] + " (" + Math.round(((double) monthly[9] / (double) lows) * 100) + "%)", 530, 277);
                        g2d.drawString("November: " + monthly[10] + " (" + Math.round(((double) monthly[10] / (double) lows) * 100) + "%)", 530, 297);
                        g2d.drawString("December: " + monthly[11] + " (" + Math.round(((double) monthly[11] / (double) lows) * 100) + "%)", 530, 317);
                    } else {
                        g2d.drawString("No storms yet", 530, 97);
                    }
                    g2d.drawString("HSP1", 740, 97);
                    g2d.drawString(hsp1[0] + " named storms (" + Math.round(Math.abs(((double) (numstorms - hsp1[0]) / (double) hsp1[0]) * 100)) + "% error)", 740, 117);
                    g2d.drawString(hsp1[1] + " hurricanes (" + Math.round(Math.abs(((double) (hurricanes - hsp1[1]) / (double) hsp1[1]) * 100)) + "% error)", 740, 137);
                    g2d.drawString(hsp1[2] + " majors (" + Math.round(Math.abs(((double) (majors - hsp1[2]) / (double) hsp1[2]) * 100)) + "% error)", 740, 157);
                    g2d.drawString("HSP2", 740, 177);
                    g2d.drawString(hsp2[0] + " named storms (" + Math.round(Math.abs(((double) (numstorms - hsp2[0]) / (double) hsp2[0]) * 100)) + "% error)", 740, 197);
                    g2d.drawString(hsp2[1] + " hurricanes (" + Math.round(Math.abs(((double) (hurricanes - hsp2[1]) / (double) hsp2[1]) * 100)) + "% error)", 740, 217);
                    g2d.drawString(hsp2[2] + " majors (" + Math.round(Math.abs(((double) (majors - hsp2[2]) / (double) hsp2[2]) * 100)) + "% error)", 740, 237);
                    g2d.drawString("HSP3", 740, 257);
                    g2d.drawString(hsp3[0] + " named storms (" + Math.round(Math.abs(((double) (numstorms - hsp3[0]) / (double) hsp3[0]) * 100)) + "% error)", 740, 277);
                    g2d.drawString(hsp3[1] + " hurricanes (" + Math.round(Math.abs(((double) (hurricanes - hsp3[1]) / (double) hsp3[1]) * 100)) + "% error)", 740, 297);
                    g2d.drawString(hsp3[2] + " majors (" + Math.round(Math.abs(((double) (majors - hsp3[2]) / (double) hsp3[2]) * 100)) + "% error)", 740, 317);

                    g2d.drawString("Tropical Storms: " + numstorms, 30, 117);
                    g2d.drawString("Hurricanes: " + hurricanes, 30, 137);
                    g2d.drawString("Major Hurricanes: " + majors, 30, 157);
                    g2d.drawString("C5 Hurricanes: " + c5, 30, 177);
                    g2d.drawString("Landfalls: " + landfalls.size(), 30, 197);
                    g2d.drawString("Frame Delay: " + frameDelay, 30, 217);
                    g2d.drawString("Damages: $" + damages + symbol, 30, 237);
                    g2d.drawString("ACE: " + ace, 280, 97);

                    if (strongestStorm != null) {

                        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                        g2d.drawString(strongestStorm.getName(), 280, 280);
                        g2d.drawString("Peak: " + Math.round(strongestStorm.getWindPeak()) + " mph", 280, 300);
                        g2d.drawString("Formed: " + Year.of(year).atDay(strongestStorm.getStartDay()), 280, 320);
                        g2d.drawString("Dissipated: " + Year.of(year).atDay(strongestStorm.getStartDay() + (strongestStorm.getHistory().size() / 8)), 280, 340);
                        g2d.drawString("ACE: " + strongestStorm.getAce(), 280, 360);

                    }
                    int lx = 30;
                    int ly = 280;
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
                    int amt = 1;
                    landfalls.sort((landfall, landfall2) -> (int) Math.round(landfall2.getIntensity() - landfall.getIntensity()));
                    for (Landfall landfall : landfalls) {
                        g2d.setColor(landfall.getStorm().getCategory(landfall.getIntensity()));
                        g2d.drawString(landfall.getStorm().getName() + " (" + Math.round(landfall.getIntensity()) + " mph) - " + landfall.getLand().getLandName(), lx, ly);
                        if (ly >= 450) {
                            ly = 280;
                            lx += 260;
                        } else {
                            ly += 20;
                        }

                        if (amt == 20) {
                            break;
                        }
                        amt++;
                    }


                }
                for (Storm storm : executionQueue) {
                    if (symbol.equals("M")) {
                        damages = damages + storm.getDamages();
                    } else {
                        if (symbol.equals("B")) {
                            damages = damages + (storm.getDamages() / 1000.0);
                        } else {
                            damages = damages + (storm.getDamages() / 1000000.0);
                        }
                    }

                    if (storm.getWindPeak() > 29) {
                        tds++;

                    }
                    if (storm.getWindPeak() > 37) {
                        numstorms++;
                    }
                    if (storm.getWindPeak() > 73) {
                        hurricanes++;
                    }
                    if (storm.getWindPeak() > 110) {
                        majors++;
                    }
                    if (storm.getWindPeak() > 156) {
                        c5++;
                    }
                    if (strongestStorm == null) {
                        strongestStorm = storm;
                    } else {
                        if (storm.getWindPeak() > strongestStorm.getWindPeak()) {
                            strongestStorm = storm;
                        }
                    }
                    ace+=storm.getAce();
                    history.add(storm);
                    storms.remove(storm);
                }
            } catch (Exception e) {
                ShowError(e.getMessage());
                e.printStackTrace();
                timer.stop();
            }


        }

    }
    private class StartLandDesign implements ActionListener {
        public StartLandDesign() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            headerpanel.setVisible(false);

            JMenuBar landMenuBar = new JMenuBar();
            LandDesigner landDesigner = new LandDesigner(landMenuBar);

            frame.add(landDesigner);
            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    landDesigner.handlerKeyEvent(e);

                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });
            frame.setTitle("LandDesigner v0.0.7");

            frame.setJMenuBar(landMenuBar);
            frame.pack();
            frame.setResizable(false);
            frame.setFocusable(true);
            frame.requestFocus();
            landDesigner.setFocusable(true);
        }
    }
    private class StartSeason implements ActionListener {
        public StartSeason() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                headerpanel.setVisible(false);
                SimulationPane = new SimulationPane();
                frame.add(SimulationPane);
                JMenuItem newMenuItem = new JMenuItem("Close all layers");
                newMenuItem.setActionCommand("Close all layers");
                newMenuItem.addActionListener(e1 -> {
                    gusts = false;
                    sWind = false;
                    threat = false;
                    sst = false;
                    tcchance = false;
                    huchance= false;
                    surge = false;
                });
                menuBar.add(actionMenu);


                actionMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("Force Repaint");
                newMenuItem.setActionCommand("Force Repaint");
                newMenuItem.addActionListener(e1 -> frame.repaint());
                actionMenu.add(newMenuItem);
                deselect = new JMenuItem("Deselect Storm/Buoy/Country");
                deselect.setActionCommand("Deselect Storm/Buoy/Country");
                deselect.addActionListener(e1 -> {
                    showingStormInfo = null;
                    showingBuoyInfo = null;
                    showingLandInfo = null;
                });
                actionMenu.add(deselect);
                newMenuItem = new JMenuItem("Save Land");
                newMenuItem.setActionCommand("Save Land");

                newMenuItem.addActionListener(e1 -> {
                    FileFilter filter = new FileNameExtensionFilter("Serializable file", "ser");

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.addChoosableFileFilter(filter);
                    fileChooser.setFileFilter(filter);
                    paused = true;
                    int option = fileChooser.showSaveDialog(frame);
                    paused = false;
                    if (option == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();

                        try {
                            FileOutputStream fil = new FileOutputStream(file.getPath());
                            ObjectOutputStream out = new ObjectOutputStream(fil);

                            out.writeObject(loadedLand);
                            out.close();
                            fil.close();
                            JOptionPane.showMessageDialog(null, "Success!");

                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "File not found!");
                        }

                    }
                });
                actionMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("Season Timeline / Wind History");
                newMenuItem.setActionCommand("Season Timeline / Wind History");
                menuBar.add(layerMenu);

                menuBar.add(selectMenu);
                selectMenu.add(stormMenu);
                selectMenu.add(landMenu);

                selectMenu.add(buoyMenu);
                layerMenu.add(newMenuItem);
                frame.setJMenuBar(menuBar);
                newMenuItem.addActionListener(e1 -> {
                    t = !t;
                    seasonSummary = false;
                    frame.repaint();
                });
                newMenuItem = new JMenuItem("Sustained Winds");
                newMenuItem.setActionCommand("Sustained Winds");
                newMenuItem.addActionListener(e1 -> {
                    sWind = !sWind;
                    gusts = false;
                    sst = false;
                    threat = false;
                    surge = false;
                    tcchance = false;
                    huchance = false;
                });
                layerMenu.add(newMenuItem);

                newMenuItem = new JMenuItem("Wind Gusts");
                newMenuItem.setActionCommand("Wind Gusts");
                newMenuItem.addActionListener(e1 -> {
                    gusts = !gusts;
                    sWind = false;
                    sst = false;
                    surge = false;
                    tcchance = false;
                    huchance = false;
                    threat = false;
                });
                layerMenu.add(newMenuItem);

                newMenuItem = new JMenuItem("SSTs");
                newMenuItem.setActionCommand("SSTs");
                newMenuItem.addActionListener(e1 -> {
                    gusts = false;
                    sWind = false;
                    sst = !sst;
                    tcchance = false;
                    threat = false;
                    surge = false;
                    huchance = false;
                });
                layerMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("TS Wind Chance");
                newMenuItem.setActionCommand("TS Wind Chance");
                newMenuItem.addActionListener(e1 -> {
                    gusts = false;
                    sWind = false;
                    sst = false;
                    tcchance = !tcchance;
                    threat = false;
                    surge = false;
                    huchance = false;
                });
                layerMenu.add(newMenuItem);

                newMenuItem = new JMenuItem("Surge Height");
                newMenuItem.setActionCommand("Surge Height");
                newMenuItem.addActionListener(e1 -> {
                    gusts = false;
                    sWind = false;
                    threat = false;
                    sst = false;
                    huchance = false;
                    tcchance = false;
                    surge = !surge;
                });
                layerMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("Season Summary");
                newMenuItem.setActionCommand("Season Summary");
                newMenuItem.addActionListener(e1 -> {
                    seasonSummary = !seasonSummary;
                    t = false;
                });
                layerMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("HU Wind Chance");
                newMenuItem.setActionCommand("HU Wind Chance");
                newMenuItem.addActionListener(e1 -> {
                    huchance = !huchance;
                    gusts = false;
                    sWind = false;
                    sst = false;
                    tcchance = false;
                    surge = false;
                    threat = false;
                });

                layerMenu.add(newMenuItem);
                newMenuItem = new JMenuItem("Land Threat");
                newMenuItem.setActionCommand("Land Threat");
                newMenuItem.addActionListener(e1 -> {
                    sWind = false;
                    gusts = false;
                    sst = false;
                    surge = false;
                    tcchance = false;
                    huchance = false;
                    threat = !threat;
                });
                layerMenu.add(newMenuItem);
                trackforecastshow = new JMenuItem("Track Forecast");
                trackforecastshow.setActionCommand("Track Forecast");
                trackforecastshow.addActionListener(e1 -> trackforecast = !trackforecast);
                layerMenu.add(trackforecastshow);

                tracktype = trackType.getSelectedIndex();
                frameDelay = animationSpeed2.getValue();
                frame.pack();
                frame.setResizable(false);
                frame.setFocusable(true);
                frame.requestFocus();
                SimulationPane.setFocusable(true);
                year = Integer.parseInt(yearField.getText());
                if (resolution.getSelectedIndex() == 0) {
                    SimResolution = 10;

                } else {
                    if (resolution.getSelectedIndex() == 1) {
                        SimResolution = 5;

                    } else {
                        if (resolution.getSelectedIndex() == 2) {
                            SimResolution = 4;

                        } else {
                            if (resolution.getSelectedIndex() == 3) {
                                SimResolution = 3;

                            } else {
                                if (resolution.getSelectedIndex() == 4) {
                                    SimResolution = 1;

                                } else {
                                    SimResolution = 50;
                                }
                            }
                        }
                    }
                }

                frame.addMouseListener(new MouseListener() {// provides empty implementation of all
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mouseX = e.getX();
                        pressed = true;
                        mouseY = e.getY();
                    }

                    @Override //I override only one method for presentation
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                frame.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    public void keyPressed(KeyEvent ke) {

                        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                            paused = !paused;
                        } else {
                            if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                                if (frameDelay > 3) {
                                    frameDelay = frameDelay - 3;
                                }
                            } else {
                                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                                    if (frameDelay < 200) {
                                        frameDelay = frameDelay + 3;
                                    }
                                } else {
                                    if (paused && ke.getKeyCode() == KeyEvent.VK_LEFT) {
                                        singleStep = true;
                                    } else {
                                        if (ke.getKeyCode() == KeyEvent.VK_1) {
                                            t = !t;
                                            seasonSummary = false;
                                        } else {
                                            if (ke.getKeyCode() == KeyEvent.VK_2) {
                                                sWind = !sWind;
                                                gusts = false;
                                                sst = false;
                                                threat = false;
                                                surge = false;
                                                tcchance = false;
                                                huchance = false;

                                            } else if (ke.getKeyCode() == KeyEvent.VK_3) {
                                                gusts = !gusts;
                                                sWind = false;
                                                sst = false;
                                                threat = false;

                                                surge = false;
                                                huchance = false;

                                                tcchance = false;
                                            } else {
                                                if (ke.getKeyCode() == KeyEvent.VK_0) {
                                                    gusts = false;
                                                    sWind = false;
                                                    sst = false;
                                                    tcchance = false;
                                                    surge = false;
                                                    threat = false;

                                                    huchance = false;

                                                }
                                                if (ke.getKeyCode() == KeyEvent.VK_4) {
                                                    gusts = false;
                                                    sWind = false;
                                                    sst = !sst;
                                                    tcchance = false;
                                                    surge = false;
                                                    huchance = false;
                                                    threat = false;


                                                } else {
                                                    if (ke.getKeyCode() == KeyEvent.VK_5) {
                                                        gusts = false;
                                                        sWind = false;
                                                        threat = false;

                                                        sst = false;
                                                        tcchance = !tcchance;
                                                        surge = false;
                                                        huchance = false;

                                                    } else {
                                                        if (ke.getKeyCode() == KeyEvent.VK_6) {
                                                            gusts = false;
                                                            sWind = false;
                                                            sst = false;
                                                            threat = false;

                                                            tcchance = false;
                                                            surge = !surge;
                                                            huchance = false;
                                                        } else {
                                                            if (ke.getKeyCode() == KeyEvent.VK_8) {
                                                                gusts = false;
                                                                sWind = false;
                                                                threat = false;
                                                                sst = false;
                                                                tcchance = false;
                                                                surge = false;
                                                                huchance = !huchance;
                                                                t = false;
                                                            }
                                                            if (ke.getKeyCode() == KeyEvent.VK_9) {
                                                                gusts = false;
                                                                sWind = false;
                                                                sst = false;
                                                                tcchance = false;
                                                                surge = false;
                                                                huchance = false;
                                                                threat = !threat;
                                                                t = false;
                                                            }
                                                            if (ke.getKeyCode() == KeyEvent.VK_7) {
                                                                seasonSummary = !seasonSummary;
                                                                t = false;
                                                            } else {
                                                                if (ke.getKeyCode() == KeyEvent.VK_R) {
                                                                    if (randomActivity.isSelected()) {
                                                                        activityThings.setSelectedIndex(randInt(0, 6));
                                                                    }
                                                                    if (activityThings.getSelectedIndex() == 0) {
                                                                        activity = myRandom(1.5, 2);
                                                                        minus = activity / 100.0 * -1;
                                                                        hsp1[0] = randInt(5, 11);
                                                                        hsp2[0] = randInt(5, 11);
                                                                        hsp3[0] = randInt(5, 11);
                                                                    } else {
                                                                        if (activityThings.getSelectedIndex() == 1) {
                                                                            activity = myRandom(0.5, 1.5);
                                                                            minus = activity / 100.0 * -1;
                                                                            hsp1[0] = randInt(7, 13);
                                                                            hsp2[0] = randInt(7, 13);
                                                                            hsp3[0] = randInt(7, 13);
                                                                        } else {
                                                                            if (activityThings.getSelectedIndex() == 2) {
                                                                                activity = myRandom(0.5, 1.5);
                                                                                minus = (activity - 1) / 100.0 * -1;
                                                                                hsp1[0] = randInt(8, 15);
                                                                                hsp2[0] = randInt(8, 15);
                                                                                hsp3[0] = randInt(8, 15);
                                                                            } else {
                                                                                if (activityThings.getSelectedIndex() == 3) {
                                                                                    activity = myRandom(0.5, 1.0);
                                                                                    minus = (activity) / 100.0;
                                                                                    hsp1[0] = randInt(10, 17);
                                                                                    hsp2[0] = randInt(11, 17);
                                                                                    hsp3[0] = randInt(10, 17);
                                                                                } else {
                                                                                    if (activityThings.getSelectedIndex() == 4) {
                                                                                        activity = myRandom(1.0, 1.5);
                                                                                        hsp1[0] = randInt(12, 18);
                                                                                        hsp2[0] = randInt(12, 18);
                                                                                        hsp3[0] = randInt(12, 18);
                                                                                    } else {
                                                                                        if (activityThings.getSelectedIndex() == 5) {
                                                                                            activity = myRandom(1.5, 2.5);
                                                                                            hsp1[0] = randInt(14, 20);
                                                                                            hsp2[0] = randInt(14, 20);
                                                                                            hsp3[0] = randInt(14, 20);
                                                                                        } else {
                                                                                            activity = myRandom(2.5, 7.0);
                                                                                            hsp1[0] = randInt(17, 25);
                                                                                            hsp2[0] = randInt(17, 25);
                                                                                            hsp3[0] = randInt(17, 25);
                                                                                        }
                                                                                    }
                                                                                    minus = (activity - 1) / 100.0;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    hsp1[1] = (int) (hsp1[0] / myRandom(1.3, 3));
                                                                    hsp1[2] = (int) (hsp1[1] / myRandom(1.2, 2.3));
                                                                    hsp2[1] = (int) (hsp2[0] / myRandom(1.6, 3.2));
                                                                    hsp2[2] = (int) (hsp2[1] / myRandom(1.3, 2.5));
                                                                    hsp3[1] = (int) (hsp3[0] / myRandom(1.5, 3.2));
                                                                    hsp3[2] = (int) (hsp3[1] / myRandom(1.3, 2.8));

                                                                } else {
                                                                    if (ke.getKeyCode() == KeyEvent.VK_8) {
                                                                        if (showingStormInfo != null) {
                                                                            trackforecast = !trackforecast;
                                                                        }
                                                                    } else {
                                                                        if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
                                                                            SimulationPane.repaint();
                                                                        } else {
                                                                            if (ke.getKeyCode() == KeyEvent.VK_D) {
                                                                                showingStormInfo = null;
                                                                                showingBuoyInfo = null;
                                                                                showingLandInfo = null;
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
                            }
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });

            } catch (Exception y) {
                y.printStackTrace();
                ShowError(y.getMessage());
            }
        }
    }
}


