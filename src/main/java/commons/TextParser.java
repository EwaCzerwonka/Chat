package commons;

import org.apache.commons.lang3.StringUtils;

public class TextParser {

    private TextParser(){}

    public static int parseLastNumber(String text, String separator){
        Integer number = 0;
        String num = getLastPart(text, separator);
        try {
            number = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
        }
        return number;
    }

    public static String getLastPart(String text, String separator){
        return StringUtils.substringAfter(text, separator).trim();
    }
}
