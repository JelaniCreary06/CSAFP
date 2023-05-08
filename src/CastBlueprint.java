import java.io.Serializable;

public class CastBlueprint implements Serializable {
    private String className;

    public CastBlueprint(String className) {
        this.className = className;
    }

    public String className() {
        return this.className;
    }
}
