package onl.deepspace.zoorallye.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dennis on 25.04.2016.
 *
 * Class for holding information about a question
 */
public class Question implements Parcelable {

    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_CORRECT = 1;
    public static final int STATE_WRONG = 2;

    private String type;
    private JSONObject value;
    private int state;

    public Question(String type, JSONObject value) {
        this.type = type;
        this.value = value;
        this.state = STATE_UNKNOWN;
    }

    public Question(Parcel parcel) {
        this.type = parcel.readString();
        try {
            this.value = new JSONObject(parcel.readString());
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
        this.state = parcel.readInt();
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        try {
            return value.getString(Const.QUESTIONS_ID);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return "-1";
        }
    }

    public String getQuestion() {
        try {
            return value.getString(Const.QUESTIONS_QUESTION);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return "";
        }
    }

    public String getAnimal() {
        try {
            return value.getString(Const.QUESTIONS_ENCLOSURE);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return null;
        }
    }

    public int getScore() {
        switch (type) {
            case Const.QUESTION_TYPE_CHECKBOX:
                return Const.SCORE_CHECKBOX;
            case Const.QUESTION_TYPE_RADIO:
                return Const.SCORE_RADIO;
            case Const.QUESTION_TYPE_SEEKBAR:
                return Const.SCORE_SEEKBAR;
            case Const.QUESTION_TYPE_TEXT:
                return Const.SCORE_TEXT;
            case Const.QUESTION_TYPE_TRUE_FALSE:
                return Const.SCORE_TRUE_FALSE;
            case Const.QUESTION_TYPE_SORT:
                return Const.SCORE_SORT;
            default:
                return 0;
        }
    }

    public int getState() {
        return this.state;
    }

    public String getImage() {
        try {
            return value.getString(Const.QUESTION_IMAGE);
        } catch (JSONException e) {
            Log.w(Const.LOGTAG, "No Image defined");
            return null;
        }
    }

    public JSONObject getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + "-" + value.toString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {

        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(value.toString());
        dest.writeInt(state);
    }
}
