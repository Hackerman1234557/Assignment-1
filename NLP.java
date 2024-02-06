package uob.oop;

import java.util.Objects;

public class NLP {
    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    public static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.1 - 3 marks;
        for (char c : _content.toLowerCase().toCharArray()){
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                sbContent.append(c);
            }

        }
        return sbContent.toString().trim();
    }

    /***
     * Text lemmatization. Delete 'ing', 'ed', 'es' and 's' from the end of the word.
     * @param _content Text that need to be lemmatized.
     * @return Lemmatized text.
     */
    public static String textLemmatization(String _content) {
        StringBuilder sbContent = new StringBuilder();
        //TODO Task 2.2 - 3 marks
        for (String c : _content.split(" ")){
            if (c.endsWith("ing")){
                StringBuilder x = new StringBuilder(c);
                x.delete(x.length()-3, x.length());
                sbContent.append(x).append(" ");
            } else if (c.endsWith("ed")){
                StringBuilder x = new StringBuilder(c);
                x.delete(x.length()-2, x.length());
                sbContent.append(x).append(" ");
            } else if (c.endsWith("es")){
                StringBuilder x = new StringBuilder(c);
                x.delete(x.length()-2, x.length());
                sbContent.append(x).append(" ");
            } else if (c.endsWith("s")){
                StringBuilder x = new StringBuilder(c);
                x.deleteCharAt(x.length()-1);
                sbContent.append(x).append(" ");
            } else {
                sbContent.append(c).append(" ");
            }
        }
        return sbContent.toString().trim();
    }

    /***
     * Remove stop-words from the text.
     * @param _content The original text.
     * @param _stopWords An array that contains stop-words.
     * @return Modified text.
     */
    public static String removeStopWords(String _content, String[] _stopWords) {
        StringBuilder sbConent = new StringBuilder();
        //TODO Task 2.3 - 3 marks
        for (String c : _content.split(" ")){
            boolean haltWordFound = false;
            for (String d : _stopWords){
                if (Objects.equals(c, d)) {
                    haltWordFound = true;
                    break;
                }
            }
            if (!haltWordFound) {
                sbConent.append(c).append(" ");
            }
        }
        return sbConent.toString().trim();
    }

}
