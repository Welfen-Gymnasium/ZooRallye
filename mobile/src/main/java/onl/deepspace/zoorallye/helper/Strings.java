package onl.deepspace.zoorallye.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import onl.deepspace.zoorallye.R;

/**
 * Created by Dennis on 27.04.2016.
 */
public class Strings {

    public static final String BASE_ANIMAL = "animal_";

    public static String getBeaconType(Context context, String type) {
        Resources res = context.getResources();
        switch (type) {
            case Const.ZOO_BEACON_OPEN_AIR:
                return res.getString(R.string.zoo_beacon_open_air);
            case Const.ZOO_BEACON_ANIMAL_HOUSE:
                return res.getString(R.string.zoo_beacon_animal_house);
            default:
                return "";
        }
    }

    public static String getType(Context context, String type) {
        Resources res = context.getResources();
        switch (type) {
            case Const.QUESTION_TYPE_CHECKBOX:
                return res.getString(R.string.type_checkbox);
            case Const.QUESTION_TYPE_RADIO:
                return res.getString(R.string.type_radio);
            case Const.QUESTION_TYPE_SEEKBAR:
                return res.getString(R.string.type_seekbar);
            case Const.QUESTION_TYPE_TEXT:
                return res.getString(R.string.type_text);
            case Const.QUESTION_TYPE_TRUE_FALSE:
                return res.getString(R.string.type_true_false);
            case Const.QUESTION_TYPE_SORT:
                return res.getString(R.string.type_sort);
            default:
                return "";
        }
    }

    public static String getAnimal(Context context, String animal) {
        final Resources res = context.getResources();

        animal = BASE_ANIMAL + animal.replaceAll(" ", "_");

        Log.d(Const.LOGTAG, animal);

        int id = res.getIdentifier(animal, "string", context.getPackageName());
        Log.d(Const.LOGTAG, id + "");
        if (id == 0)
            return animal;
        return res.getString(id);
    }
}
