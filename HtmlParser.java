package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        //TODO Task 1.1 - 5 marks
        int beginning = _htmlCode.indexOf("<title>");
        int end = _htmlCode.indexOf("|");
        if (beginning == -1 || end == -1){
            return "Title not found!";
        } else {
            return _htmlCode.substring(beginning + 7, end - 1);
        }
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        //TODO Task 1.2 - 5 marks
        int beginning = _htmlCode.indexOf("articleBody");
        int end = _htmlCode.indexOf("mainEntityOfPage");
        if (beginning == -1 || end == -1) {
            return "Content not found!";
        } else {
            return _htmlCode.substring(beginning + 15, end - 4).toLowerCase();
        }
    }


}
