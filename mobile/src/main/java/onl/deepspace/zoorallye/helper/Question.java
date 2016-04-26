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

    private String type;
    private JSONObject value;

    public Question(String type, JSONObject value) {
        this.type = type;
        this.value = value;
    }

    public Question(Parcel parcel) {
        this.type = parcel.readString();
        try {
            this.value = new JSONObject(parcel.readString());
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    public String getType() {
        return type;
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
    }
}
