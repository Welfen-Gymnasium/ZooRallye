package onl.deepspace.zoorallye.helper;

/**
 * Created by Sese on 13.04.2016.
 */
public class Exceptions {

    public static class ClassNotInstanceOfAchievementActivityException extends Exception{
        public ClassNotInstanceOfAchievementActivityException(){super("Your Activity has to be a instance of AppCompatAchievementActivity!");}
    }

    public static class GooglePlayUnconnectedException extends Exception{
        public GooglePlayUnconnectedException(String message){super(message);}
    }

}
