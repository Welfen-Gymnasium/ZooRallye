package onl.deepspace.zoorallye.helper;

/**
 * Created by Dennis on 29.03.2016.
 *
 * A class for all Constants
 */
public final class Const {
    final public static String LOGTAG = "ZooRally";

    public static final String PREFS = "onl.deepspace.zoorallye.preferences";
    public static final String PREF_TOTAL_SCORE = "prefTotalScore";
    public static final String PREF_TOTAL_ANSWERED_QUESTIONS = "prefTotalAnsweredQuestions";

    public static final String NAV_FRAGMENT = "selectedFragment";

    public static final int RALLY_QUESTION_COUNT = 40;

    public static final String ZOOS_API = "http://api.deepspace.onl/zoorallye/zoos/";
    public static final String ZOO_ID = "id";
    public static final String ZOO_LAT = "latitude";
    public static final String ZOO_LNG = "longitude";
    public static final String ZOO_ANIMALS = "animals";

    public static final String ZOO_BEACON_OPEN_AIR = "open-air enclosure";
    public static final String ZOO_BEACON_ANIMAL_HOUSE = "animal house";

    final public static String QuestionsAPI = "http://api.deepspace.onl/zoorallye/questions";
    final public static String QuestionsAPI_token = "token";
    public static final String QuestionsAPI_SLIDER = "slider";
    public static final String QuestionsAPI_RADIO = "radio";
    public static final String QuestionsAPI_CHECKBOX = "checkbox";
    public static final String QuestionsAPI_TRUE_FALSE = "trueFalse";
    public static final String QuestionsAPI_SORT = "sort";
    public static final String QuestionsAPI_TEXT = "text";

    public static final String QUESTIONS_ID = "id";
    public static final String QUESTIONS_QUESTION = "question";
    public static final String QUESTIONS_ENCLOSURE = "enclosure";
    public static final String QUESTIONS_ANSWER = "answer";
    public static final String QUESTIONS_ANSWERS = "answers";
    public static final String QUESTIONS_FALSE_ANSWERS = "falseAnswers";
    public static final String QUESTIONS_MIN = "min";
    public static final String QUESTIONS_MAX = "max";
    public static final String QUESTIONS_STEP = "step";

    @SuppressWarnings("SpellCheckingInspection")
    final public static String API_TOKEN = "x9j7qtRUOrGGg5fxHakK1sp6T4h9JbJjL44iulAKA8HPbbmBWd2QUEQpFD35i";

    public static final String CHAR_ENCODING = "UTF-8";
    public static final int MAX_RESPONSE_LENGTH = 500;

    public static final double minLatitude = 48.343800;
    public static final double minLongitude = 10.912091;
    public static final double maxLatitude = 48.349773;
    public static final double maxLongitude = 10.918707;

    public static final int GoogleAuthIntent= 1000;

    public static final String QUESTION = "question";
    public static final String QUESTION_ID = "questionId";
    public static final String QUESTION_TYPE = "questionType";
    public static final String QUESTION_BUNDLE = "questionBundle";
    public static final String QUESTION_IMAGE = "questionImage";
    public static final String QUESTION_ANSWER = "questionAnswer";
    public static final String QUESTION_ANSWERED = "questionAnswered";

    public static final String QUESTION_VISIT_ID = "questionVisitId";

    public static final String QUESTION_BUNDLE_ID = "questionBundleId";
    public static final String QUESTION_SCORE = "questionScore";
    public static final String QUESTION_TYPE_SORT = "sort";
    public static final String QUESTION_TYPE_SEEKBAR = "slider";
    public static final String QUESTION_TYPE_RADIO = "radio";
    public static final String QUESTION_TYPE_CHECKBOX = "checkbox";
    public static final String QUESTION_TYPE_TEXT = "text";

    public static final String QUESTION_TYPE_TRUE_FALSE = "trueFalse";
    public static final int SCORE_TRUE_FALSE = 100;
    public static final int SCORE_RADIO = 150;
    public static final int SCORE_TEXT = 200; // average credits
    public static final int SCORE_CHECKBOX = 250;
    public static final int SCORE_SORT = 300;
    public static final int SCORE_SEEKBAR = 300;
    public static final int SCORE_AVERAGE = (SCORE_TRUE_FALSE + SCORE_RADIO + SCORE_TEXT +
            SCORE_CHECKBOX + SCORE_SORT) / 5; // Score text

    public static final String RALLY_FINISHED_SCORE = "rallyFinishedScore";
}
