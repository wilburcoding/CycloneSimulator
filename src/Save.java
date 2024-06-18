import java.io.*;

public class Save {
    private static CycloneSimulator simulator;
    public Save(CycloneSimulator cycloneSimulator) {
        simulator = cycloneSimulator;
    }
    public void saveTo(String filename) throws IOException {
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(simulator);
            out.close();
            file.close();
        } catch (IOException e) {
            throw new IOException(filename);
        }
    }
    public void getSim(String filename) throws IOException {
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            simulator = (CycloneSimulator) in.readObject();
            in.close();
            file.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException(filename);
        }
    }

}
