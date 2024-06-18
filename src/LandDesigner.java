import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.Key;
import java.util.ArrayList;

public class LandDesigner extends JPanel {
    public ArrayList<Land> lands = new ArrayList<>();
    public JMenuBar menuBar = new JMenuBar();
    public Land selectedLand = null;
    public JMenuBar countries;
    Timer timer = new Timer(30, e -> {
        repaint();
    });

    public LandDesigner(JMenuBar landsMenu) {
        timer.start();
        requestFocus();
        JMenu actions = new JMenu("Actions");
        JMenuItem load = new JMenuItem("Load Save");

        JMenuItem save = new JMenuItem("Save");
        JMenuItem select = new JMenuItem("Select Land");
        JMenuItem reset = new JMenuItem("Reset Map");

        actions.add(save);
        actions.add(select);
        actions.add(load);

        actions.add(reset);

        landsMenu.add(actions);
        landsMenu.updateUI();
        load.addActionListener(e -> {
            FileFilter filter = new FileNameExtensionFilter("Serializable file", "ser");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            int option = fileChooser.showSaveDialog(getParent());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    FileInputStream fil = new FileInputStream(file.getPath());
                    ObjectInputStream in = new ObjectInputStream(fil);
                    LandSave newSave = (LandSave) in.readObject();
                    lands = newSave.getLandSave();
                    in.close();
                    fil.close();
                    JOptionPane.showMessageDialog(null, "Loaded " + file.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "No Land Save found in this file!");

                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        getParent(),
                        "Are you sure you want to reset?",
                        "Reset Map",
                        JOptionPane.YES_NO_OPTION);
                if (n==JOptionPane.YES_OPTION) {
                    lands=new ArrayList<>();
                    selectedLand=null;
                }
            }
        });
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> options = new ArrayList<>();
                for (Land land : lands) {
                    options.add(land.getLandName());
                }


                String s = (String)JOptionPane.showInputDialog(
                        getParent(),
                        "Select a country...",
                        "Select Country",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options.toArray(),
                        "ham");
                for (Land land: lands) {
                    if (land.getLandName().equals(s)) {
                        selectedLand = land;
                        break;
                    }
                }

            }
        });
        save.addActionListener(e1 -> {
            FileFilter filter = new FileNameExtensionFilter("Serializable file", "ser");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);
            LandSave landsave = new LandSave(lands);
            int option = fileChooser.showSaveDialog(getParent());
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    FileOutputStream fil = new FileOutputStream(file.getPath());
                    ObjectOutputStream out = new ObjectOutputStream(fil);

                    out.writeObject(landsave);
                    out.close();
                    fil.close();
                    JOptionPane.showMessageDialog(null, "Success!");

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "File not found!");
                }

            }
        });


        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < 1000 && selectedLand == null && lands.size() < 50) {


                    Land newLand = new Land(e.getX(), e.getY(), 50, 50, "New Country");
                    lands.add(newLand);


                    selectedLand = newLand;
                }
            }

            @Override
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


    }

    public void handlerKeyEvent(KeyEvent e) {
        if (selectedLand==null) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            selectedLand.xLeftBound -= 2;

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectedLand.xLeftBound += 2;

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedLand.yTopBound += 2;

        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            selectedLand.yTopBound -= 2;

        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            if (selectedLand.xSize > 2) {
                selectedLand.xSize -= 2;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
            if (selectedLand.xSize < 500) {
                selectedLand.xSize += 2;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) {
            if (selectedLand.ySize < 500) {
                selectedLand.ySize += 2;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) {
            if (selectedLand.ySize > 2) {
                selectedLand.ySize -= 2;

            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
                String s = (String) JOptionPane.showInputDialog(this
                        ,
                        "Enter a new county name:",
                        "Rename Country",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "");
                if (s.length() > 15) {
                    s=s.substring(0,15);
                }
                selectedLand.landName = s;

        }else if (e.getKeyCode() == KeyEvent.VK_D) {
            selectedLand = null;
        }else if (e.getKeyCode()== KeyEvent.VK_C) {
            lands.remove(selectedLand);
            selectedLand=null;
        }
        if (selectedLand != null) {
            selectedLand.update();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        for (Land land : lands) {
            ArrayList<Coordinate> landCoordinates = land.getCoordinates();
            g2d.fillRect((int) landCoordinates.get(0).getX() - 2, (int) landCoordinates.get(0).getY() - 2, land.getxSize() + 4, land.getySize() + 4);
            g2d.setColor(Color.WHITE);
            g2d.fillRect((int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY(), land.getxSize(), land.getySize());

            g2d.setColor(Color.BLACK);
            g2d.drawString(land.getLandName(), (int) landCoordinates.get(0).getX(), (int) landCoordinates.get(0).getY() - 4);

        }
        g2d.setColor(new Color(189, 188, 183));
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        g2d.fillRect(1000, 0, 200, 500);
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawString("Land Designer", 1010, 30);
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        g2d.drawString("Countries: " + lands.size(), 1010, 50);
        g2d.drawString("Selected: ", 1010, 70);

        if (selectedLand==null) {
            g2d.drawString("No Land Selected", 1010, 90);

        } else {
            g2d.drawString(selectedLand.getLandName(), 1010, 90);

        }
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        g2d.drawString("Controls", 1010, 115);
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        g2d.drawString("Click to create", 1010, 135);

        g2d.drawString("- to decrease width", 1010, 155);
        g2d.drawString("+ to increase width", 1010, 175);
        g2d.drawString("[ to decrease height", 1010, 195);
        g2d.drawString("] to increase height", 1010, 215);
        g2d.drawString("Arrow keys to move", 1010, 235);

        g2d.drawString("r to rename", 1010, 255);
        g2d.drawString("c to cancel/delete", 1010, 275);
        g2d.drawString("d to finish/deselect", 1010, 295);


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 500);
    }


}
