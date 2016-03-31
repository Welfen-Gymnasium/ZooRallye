package onl.deepspace.zoorallye.questions.sqlite;

import android.provider.BaseColumns;

/**
 * Created by Dennis on 31.03.2016.
 *
 * Contract for SQLite Database
 */
public final class QuestionsContract {

    public static final String SEPARATOR = ",";
    public static final String TYPE_TEXT = "TEXT";

    public QuestionsContract() {}

    public static abstract class SortQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + SortQuestions.TABLE_NAME + " (" +
                SortQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                SortQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                SortQuestions.COL_NAME_ANSWERS + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + SortQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_sort";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ANSWERS = "answers";
    }

    public static abstract class SliderQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + SliderQuestions.TABLE_NAME + " (" +
                SliderQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                SliderQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                SliderQuestions.COL_NAME_MIN + TYPE_TEXT + SEPARATOR +
                SliderQuestions.COL_NAME_MAX + TYPE_TEXT + SEPARATOR +
                SliderQuestions.COL_NAME_STEP + TYPE_TEXT + SEPARATOR +
                SliderQuestions.COL_NAME_ANSWER + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + SliderQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_slider";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_MIN = "min";
        public static final String COL_NAME_MAX = "max";
        public static final String COL_NAME_STEP = "step";
        public static final String COL_NAME_ANSWER = "answer";
    }

    public static abstract class RadioQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + RadioQuestions.TABLE_NAME + " (" +
                RadioQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                RadioQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                RadioQuestions.COL_NAME_ANSWER + TYPE_TEXT + SEPARATOR +
                RadioQuestions.COL_NAME_FALSE_ANSWERS + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + RadioQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_radio";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ANSWER = "answer";
        public static final String COL_NAME_FALSE_ANSWERS = "falseAnswers";
    }

    public static abstract class CheckboxQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + CheckboxQuestions.TABLE_NAME + " (" +
                CheckboxQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                CheckboxQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                CheckboxQuestions.COL_NAME_ANSWERS + TYPE_TEXT + SEPARATOR +
                CheckboxQuestions.COL_NAME_FALSE_ANSWERS + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + CheckboxQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_checkbox";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ANSWERS = "answers";
        public static final String COL_NAME_FALSE_ANSWERS = "falseAnswers";
    }

    public static abstract class TextQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + TextQuestions.TABLE_NAME + " (" +
                TextQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                TextQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                TextQuestions.COL_NAME_ANSWER + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TextQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_sort";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ANSWER = "answer";
    }

    public static abstract class TrueFalseQuestions implements BaseColumns {
        public static final String CREATE_TABLE = "CREATE TABLE " + TrueFalseQuestions.TABLE_NAME + " (" +
                TrueFalseQuestions.COL_NAME_ID + " INTEGER PRIMARY KEY," +
                TrueFalseQuestions.COL_NAME_QUESTION + TYPE_TEXT + SEPARATOR +
                TrueFalseQuestions.COL_NAME_ANSWER + TYPE_TEXT;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TrueFalseQuestions.TABLE_NAME;
        public static final String TABLE_NAME = "questions_true_false";
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_QUESTION = "question";
        public static final String COL_NAME_ANSWER = "answer";
    }
}
