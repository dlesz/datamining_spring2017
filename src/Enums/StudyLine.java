package Enums;

/**
 * Created by dlesz on 06/02/2017.
 */
public enum StudyLine {
    UNKNOWN(0.0),
    SDT_SE(22.0),
    SDT_AC(26.0),
    SDT_DT(30.0),
    GAMES(34.0),
    DIM(38.0),
    SWU(42.0),
    GUEST(46.0);

    private Double numVal;
    private Double normalizedVal;

    public void setNormalizedVal(Double normalizedVal) {
        this.normalizedVal = normalizedVal;
    }

    public Double getNormalizedVal() {
        return normalizedVal;
    }


    StudyLine(Double numVal) { this.numVal = numVal;}

    public Double getNumVal() {
        return numVal;
    }
}
