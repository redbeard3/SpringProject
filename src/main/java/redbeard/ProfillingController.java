package redbeard;

public class ProfillingController implements ProfillingControllerMBean {
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
