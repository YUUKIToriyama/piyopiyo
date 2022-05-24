import java.util.regex.Pattern;

public class PrefectureRegex {
    public String prefName;
    public Pattern pattern;

    PrefectureRegex(String prefName, Pattern pattern) {
        this.prefName = prefName;
        this.pattern = pattern;
    }
}
