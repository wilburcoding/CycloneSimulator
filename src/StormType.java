import java.io.Serializable;

class StormType implements Serializable {
    private int stormType;
    private int percentEx;
    public StormType(String type) {
        if (type.equals("ex")) {
            stormType = 1;
            percentEx=100;
        } else {
            percentEx=0;
            if (type.equals("l")) {
                stormType=-1;
            } else {
                stormType = 0;

            }

        }
    }
    public StormType(int percentEX) {
        percentEx=percentEX;
    }

    public void setPercentEx(int percentEx) {
        if (percentEx < 0) {
            percentEx=0;
        }
        this.percentEx = percentEx;
        if (percentEx>100 && stormType<1) {
            stormType=3;
        }
    }

    public int getPercentEx() {
        return percentEx;
    }

    public int getStormType() {
        return stormType;
    }
}
