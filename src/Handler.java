
public class Handler {
    public CycloneSimulator sim;
    public Save save;
    public Handler() {
        sim = new CycloneSimulator();
        save = new Save(sim);

    }
    public void loadSave(CycloneSimulator newSim) {
        sim.SimulationPane.setVisible(false);
        sim=newSim;
    }
}
