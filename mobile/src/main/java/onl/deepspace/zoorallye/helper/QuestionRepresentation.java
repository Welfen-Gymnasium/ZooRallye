package onl.deepspace.zoorallye.helper;

/**
 * Created by Dennis on 25.04.2016.
 */
public class QuestionRepresentation {

    private String type;
    private int id;

    public QuestionRepresentation(String type, int id) {
        // TODO: 25.04.2016 maybe extend to save all question information in it
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return type + "-" + id;
    }
}
