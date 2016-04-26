package onl.deepspace.zoorallye.helper;

import org.json.JSONObject;

/**
 * Created by Dennis on 25.04.2016.
 */
public class QuestionRepresentation {

    private String type;
    private JSONObject value;

    public QuestionRepresentation(String type, JSONObject value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public JSONObject getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + "-" + value;
    }
}
