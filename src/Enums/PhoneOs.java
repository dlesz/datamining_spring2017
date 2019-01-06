package Enums;

/**
 * Created by dlesz on 22/04/2017.
 */
public enum PhoneOs {
    UBUNTU_TOUCH(22),
    WINDOWS(28),
    ARCH_LINUX(34),
    ANDROID(40),
    IOS(46);

    private double numVal;

    PhoneOs(double numVal) { this.numVal = numVal;}

    public double getNumVal(){
        return numVal;
    }
}
