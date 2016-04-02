package onl.deepspace.zoorallye.questions.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import onl.deepspace.zoorallye.helper.Const;

/**
 * Created by Dennis on 31.03.2016.
 *
 * Static class for upgrading the Question Database
 */
public class UpgradeQuestionsDb {

    @SuppressWarnings("unused")
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

            insertSliderQuestions(db, slider);
            insertRadioQuestions(db, radio);
            insertCheckboxQuestions(db, checkbox);
            insertTrueFalseQuestions(db, trueFalse);
            insertSortQuestions(db, sort);
            insertTextQuestions(db, text);
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    private static void insertSliderQuestions(SQLiteDatabase db, JSONArray slider) throws JSONException {
        for (int i = 0; i < slider.length(); i++) {
            JSONObject element = slider.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            float min = Float.valueOf(element.getString(Const.QuestionsAPI_MIN));
            float max = Float.valueOf(element.getString(Const.QuestionsAPI_MAX));
            float step = Float.valueOf(element.getString(Const.QuestionsAPI_STEP));
            float answer = Float.valueOf(element.getString(Const.QuestionsAPI_ANSWER));

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.Slider.COL_NAME_ID, id);
            values.put(QuestionsContract.Slider.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.Slider.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.Slider.COL_NAME_MIN, min);
            values.put(QuestionsContract.Slider.COL_NAME_MAX, max);
            values.put(QuestionsContract.Slider.COL_NAME_STEP, step);
            values.put(QuestionsContract.Slider.COL_NAME_ANSWER, answer);
            db.insert(QuestionsContract.Slider.TABLE_NAME, null, values);
        }
    }

    private static void insertRadioQuestions(SQLiteDatabase db, JSONArray radio) throws JSONException {
        for (int i = 0; i < radio.length(); i++) {
            JSONObject element = radio.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            String answer = element.getString(Const.QuestionsAPI_ANSWER);
            String falseAnswers = element.getString(Const.QuestionsAPI_FALSE_ANSWERS);

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.Radio.COL_NAME_ID, id);
            values.put(QuestionsContract.Radio.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.Radio.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.Radio.COL_NAME_ANSWER, answer);
            values.put(QuestionsContract.Radio.COL_NAME_FALSE_ANSWERS, falseAnswers);
            db.insert(QuestionsContract.Radio.TABLE_NAME, null, values);
        }
    }

    private static void insertCheckboxQuestions(SQLiteDatabase db, JSONArray checkbox) throws JSONException {
        for (int i = 0; i < checkbox.length(); i++) {
            JSONObject element = checkbox.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            String answers = element.getString(Const.QuestionsAPI_ANSWERS);
            String falseAnswers = element.getString(Const.QuestionsAPI_FALSE_ANSWERS);

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.Checkbox.COL_NAME_ID, id);
            values.put(QuestionsContract.Checkbox.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.Checkbox.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.Checkbox.COL_NAME_ANSWERS, answers);
            values.put(QuestionsContract.Checkbox.COL_NAME_FALSE_ANSWERS, falseAnswers);
            db.insert(QuestionsContract.Checkbox.TABLE_NAME, null, values);
        }
    }

    private static void insertTrueFalseQuestions(SQLiteDatabase db, JSONArray trueFalse) throws JSONException {
        for (int i = 0; i < trueFalse.length(); i++) {
            JSONObject element = trueFalse.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            String answer = element.getString(Const.QuestionsAPI_ANSWER);

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.TrueFalse.COL_NAME_ID, id);
            values.put(QuestionsContract.TrueFalse.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.TrueFalse.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.TrueFalse.COL_NAME_ANSWER, answer);
            db.insert(QuestionsContract.TrueFalse.TABLE_NAME, null, values);
        }
    }

    private static void insertSortQuestions(SQLiteDatabase db, JSONArray sort) throws JSONException {
        for (int i = 0; i < sort.length(); i++) {
            JSONObject element = sort.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            String answers = element.getString(Const.QuestionsAPI_ANSWERS);

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.Sort.COL_NAME_ID, id);
            values.put(QuestionsContract.Sort.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.Sort.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.Sort.COL_NAME_ANSWERS, answers);
            db.insert(QuestionsContract.Sort.TABLE_NAME, null, values);
        }
    }

    private static void insertTextQuestions(SQLiteDatabase db, JSONArray text) throws JSONException {
        for (int i = 0; i < text.length(); i++) {
            JSONObject element = text.getJSONObject(i);
            int id = Integer.valueOf(element.getString(Const.QuestionsAPI_ID));
            String question = element.getString(Const.QuestionsAPI_QUESTION);
            String enclosure = element.getString(Const.QuestionsAPI_ENCLOSURE);
            String answer = element.getString(Const.QuestionsAPI_ANSWER);

            ContentValues values = new ContentValues();
            values.put(QuestionsContract.Text.COL_NAME_ID, id);
            values.put(QuestionsContract.Text.COL_NAME_QUESTION, question);
            values.put(QuestionsContract.Text.COL_NAME_ENCLOSURE, enclosure);
            values.put(QuestionsContract.Text.COL_NAME_ANSWER, answer);
            db.insert(QuestionsContract.Text.TABLE_NAME, null, values);
        }
    }

    public static void deleteDatabases(SQLiteDatabase db) {
        db.execSQL(QuestionsContract.Sort.DELETE_TABLE);
        db.execSQL(QuestionsContract.Slider.DELETE_TABLE);
        db.execSQL(QuestionsContract.Radio.DELETE_TABLE);
        db.execSQL(QuestionsContract.Checkbox.DELETE_TABLE);
        db.execSQL(QuestionsContract.Text.DELETE_TABLE);
        db.execSQL(QuestionsContract.TrueFalse.DELETE_TABLE);
    }

    public static void setupDatabases(SQLiteDatabase db) {
        db.execSQL(QuestionsContract.Sort.CREATE_TABLE);
        db.execSQL(QuestionsContract.Slider.CREATE_TABLE);
        db.execSQL(QuestionsContract.Radio.CREATE_TABLE);
        db.execSQL(QuestionsContract.Checkbox.CREATE_TABLE);
        db.execSQL(QuestionsContract.Text.CREATE_TABLE);
        db.execSQL(QuestionsContract.TrueFalse.CREATE_TABLE);
    }
}
