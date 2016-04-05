package onl.deepspace.zoorallye.questions.sqlite;

import android.provider.BaseColumns;

/**
 * Created by Dennis on 31.03.2016.
 *
 * Contract for SQLite Database
 */
public final class QuestionsContract {

    public static final String SEPARATOR = ",";
    public static final String TYPE_TEXT = " TEXT";
    public static final String TYPE_INT = " INT";

    public QuestionsContract() {}

    public static abstract class Answers implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXITS " + Answers.TABLE_NAME + " (" +
                Answers.COL_NAME_ID + " INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                Answers.COL_NAME_VISIT_ID + TYPE_TEXT + SEPARATOR +
                Answers.COL_NAME_TYPE + TYPE_TEXT + SEPARATOR +
                Answers.COL_NAME_QUESTION_ID + TYPE_TEXT + SEPARATOR +
                Answers.COL_NAME_ANSWER + TYPE_TEXT + SEPARATOR +
                Answers.COL_NAME_CREDITS + TYPE_INT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXITS " + Answers.TABLE_NAME;

        public static final String TABLE_NAME = "question_answers";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_VISIT_ID = "visit id";
        public static final String COL_NAME_TYPE = "question_type";
        public static final String COL_NAME_QUESTION_ID = "question_id";
        public static final String COL_NAME_ANSWER = "question_answer";
        public static final String COL_NAME_CREDITS = "question_credits";
    }

    public static abstract class Sort implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + Sort.TABLE_NAME + " (" +
                Sort.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                Sort.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                Sort.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                Sort.COL_NAME_ANSWERS + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Sort.TABLE_NAME;

        public static final String TABLE_NAME = "questions_sort";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_ANSWERS = "answers";
    }

    public static abstract class Slider implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + Slider.TABLE_NAME + " (" +
                Slider.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                Slider.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                Slider.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                Slider.COL_NAME_MIN + TYPE_TEXT + SEPARATOR +
                Slider.COL_NAME_MAX + TYPE_TEXT + SEPARATOR +
                Slider.COL_NAME_STEP + TYPE_TEXT + SEPARATOR +
                Slider.COL_NAME_ANSWER + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Slider.TABLE_NAME;

        public static final String TABLE_NAME = "questions_slider";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_MIN = "min";
        public static final String COL_NAME_MAX = "max";
        public static final String COL_NAME_STEP = "step";
        public static final String COL_NAME_ANSWER = "answer";
    }

    public static abstract class Radio implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + Radio.TABLE_NAME + " (" +
                Radio.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                Radio.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                Radio.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                Radio.COL_NAME_ANSWER + TYPE_TEXT + SEPARATOR +
                Radio.COL_NAME_FALSE_ANSWERS + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Radio.TABLE_NAME;

        public static final String TABLE_NAME = "questions_radio";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_ANSWER = "answer";
        public static final String COL_NAME_FALSE_ANSWERS = "falseAnswers";
    }

    public static abstract class Checkbox implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + Checkbox.TABLE_NAME + " (" +
                Checkbox.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                Checkbox.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                Checkbox.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                Checkbox.COL_NAME_ANSWERS + TYPE_TEXT + SEPARATOR +
                Checkbox.COL_NAME_FALSE_ANSWERS + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Checkbox.TABLE_NAME;

        public static final String TABLE_NAME = "questions_checkbox";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_ANSWERS = "answers";
        public static final String COL_NAME_FALSE_ANSWERS = "falseAnswers";
    }

    public static abstract class Text implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + Text.TABLE_NAME + " (" +
                Text.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                Text.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                Text.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                Text.COL_NAME_ANSWER + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + Text.TABLE_NAME;

        public static final String TABLE_NAME = "questions_sort";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_ANSWER = "answer";
    }

    public static abstract class TrueFalse implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + TrueFalse.TABLE_NAME + " (" +
                TrueFalse.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                TrueFalse.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                TrueFalse.COL_NAME_ENCLOSURE + TYPE_TEXT + SEPARATOR +
                TrueFalse.COL_NAME_ANSWER + TYPE_TEXT + ")";
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TrueFalse.TABLE_NAME;

        public static final String TABLE_NAME = "questions_true_false";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ENCLOSURE = "enclosure";
        public static final String COL_NAME_ANSWER = "answer";
    }
}
