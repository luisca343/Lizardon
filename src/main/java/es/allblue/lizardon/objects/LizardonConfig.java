package es.allblue.lizardon.objects;

public class LizardonConfig {
    private String id;
    private String home;

    public LizardonConfig(String id, String SMARTROTOM_HOME) {
        this.id = id;
        this.home = SMARTROTOM_HOME;
    }

    public String getId() {
        return id;
    }

    public String getHome() {
        return home;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
