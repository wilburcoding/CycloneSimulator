import java.io.Serializable;
import java.util.ArrayList;

public class LandSave implements Serializable {
    public ArrayList<Land> landSave = new ArrayList<>();

    public LandSave(ArrayList<Land> landSave) {
        this.landSave = landSave;
    }

    public ArrayList<Land> getLandSave() {
        return landSave;
    }
}
