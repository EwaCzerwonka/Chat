package commons;

public class TextParser {

    private TextParser(){}

    public static int parseLastNumber(String text){
        Integer number = 0;
        String num = text.substring(text.lastIndexOf(" ") + 1);
        try {
            number = Integer.parseInt(num);
        } catch (NumberFormatException nfe) {
        }
        return number;
    }
}
