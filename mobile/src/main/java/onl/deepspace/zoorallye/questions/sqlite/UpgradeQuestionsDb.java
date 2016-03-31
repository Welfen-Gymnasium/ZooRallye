package onl.deepspace.zoorallye.questions.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import onl.deepspace.zoorallye.helper.Const;

/**
 * Created by Dennis on 31.03.2016.
 */
public class UpgradeQuestionsDb {

    public static void putData(Context context, JSONObject data) {
        QuestionsDbHelper dbHelper = new QuestionsDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteDatabases(db);
        setupDatabases(db);
        try {
            JSONArray slider = data.getJSONArray(Const.QuestionsAPI_SLIDER);
            JSONArray radio = data.getJSONArray(Const.QuestionsAPI_RADIO);
            JSONArray checkbox = data.getJSONArray(Const.QuestionsAPI_CHECKBOX);
            JSONArray trueFalse = data.getJSONArray(Const.QuestionsAPI_TRUE_FALSE);
            JSONArray sort = data.getJSONArray(Const.QuestionsAPI_SORT);
            JSONArray text = data.getJSONArray(Const.QuestionsAPI_TEXT);

            // TODO: 31.03.2016 Insert values into SQLite database
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

    }

    public static void deleteDatabases(SQLiteDatabase db) {
        db.execSQL(QuestionsContract.SortQuestions.DELETE_TABLE);
        db.execSQL(QuestionsContract.SliderQuestions.DELETE_TABLE);
        db.execSQL(QuestionsContract.RadioQuestions.DELETE_TABLE);
        db.execSQL(QuestionsContract.CheckboxQuestions.DELETE_TABLE);
        db.execSQL(QuestionsContract.TextQuestions.DELETE_TABLE);
        db.execSQL(QuestionsContract.TrueFalseQuestions.DELETE_TABLE);
    }

    public static void setupDatabases(SQLiteDatabase db) {
        db.execSQL(QuestionsContract.SortQuestions.CREATE_TABLE);
        db.execSQL(QuestionsContract.SliderQuestions.CREATE_TABLE);
        db.execSQL(QuestionsContract.RadioQuestions.CREATE_TABLE);
        db.execSQL(QuestionsContract.CheckboxQuestions.CREATE_TABLE);
        db.execSQL(QuestionsContract.TextQuestions.CREATE_TABLE);
        db.execSQL(QuestionsContract.TrueFalseQuestions.CREATE_TABLE);
    }
}
