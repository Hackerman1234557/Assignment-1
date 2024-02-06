package uob.oop;

import java.text.DecimalFormat;

public class NewsClassifier {
    public String[] myHTMLs;
    public String[] myStopWords = new String[127];
    public String[] newsTitles;
    public String[] newsContents;
    public String[] newsCleanedContent;
    public double[][] newsTFIDF;

    private final String TITLE_GROUP1 = "Osiris-Rex's sample from asteroid Bennu will reveal secrets of our solar system";
    private final String TITLE_GROUP2 = "Bitcoin slides to five-month low amid wider sell-off";

    public Toolkit myTK;

    public NewsClassifier() {
        myTK = new Toolkit();
        myHTMLs = myTK.loadHTML();
        myStopWords = myTK.loadStopWords();

        loadData();
    }

    public static void main(String[] args) {
        NewsClassifier myNewsClassifier = new NewsClassifier();

        myNewsClassifier.newsCleanedContent = myNewsClassifier.preProcessing();

        myNewsClassifier.newsTFIDF = myNewsClassifier.calculateTFIDF(myNewsClassifier.newsCleanedContent);

        //Change the _index value to calculate similar based on a different news article.
        double[][] doubSimilarity = myNewsClassifier.newsSimilarity(0);

        System.out.println(myNewsClassifier.resultString(doubSimilarity, 10));

        String strGroupingResults = myNewsClassifier.groupingResults(myNewsClassifier.TITLE_GROUP1, myNewsClassifier.TITLE_GROUP2);
        System.out.println(strGroupingResults);
    }

    public void loadData() {
        //TODO 4.1 - 2 marks
        this.newsTitles = new String[myHTMLs.length];
        this.newsContents = new String[myHTMLs.length];
        for (int i = 0; i < myHTMLs.length; i++){
            newsTitles[i] = HtmlParser.getNewsTitle(myHTMLs[i]);
        }
        for (int i = 0; i < myHTMLs.length; i++){
            newsContents[i] = HtmlParser.getNewsContent(myHTMLs[i]);
        }
    }

    public String[] preProcessing() {
        String[] myCleanedContent = new String[this.newsContents.length];
        //TODO 4.2 - 5 marks
        for (int i = 0; i < this.newsContents.length; i++){
            myCleanedContent[i] = NLP.textCleaning(this.newsContents[i]);
        }
        for (int i = 0; i < this.newsContents.length; i++){
            myCleanedContent[i] = NLP.textLemmatization(myCleanedContent[i]);
        }
        for (int i = 0; i < this.newsContents.length; i++){
            myCleanedContent[i] = NLP.removeStopWords(myCleanedContent[i], this.myStopWords);
        }
        return myCleanedContent;
    }

    public double[][] calculateTFIDF(String[] _cleanedContents) {
        String[] vocabularyList = buildVocabulary(_cleanedContents);
        //TODO 4.3 - 10 marks
        double[][] myTFIDF = new double[_cleanedContents.length][vocabularyList.length];
        for (int i = 0; i < _cleanedContents.length; i++){
            for (int j = 0; j < vocabularyList.length; j++){
                Double tfidf = calculateTF(vocabularyList[j], _cleanedContents[i]) * calculateIDF(vocabularyList[j], _cleanedContents);
                myTFIDF[i][j] = tfidf;
            }
        }
        return myTFIDF;
    }

    public Double calculateTF(String t, String d){
        Double tcount = 0.0;
        String[] splitd = d.split(" ");
        for (int i = 0; i < splitd.length; i++){
            if (t.equals(splitd[i])){
                tcount ++;
            }
        }
        return (tcount / (double) splitd.length);
    }


    public Double calculateIDF(String v, String[] corpus){
        Double inverseDocumentFrequency = 0.0;
        Double invcount = 0.0;
        for (int i = 0; i < corpus.length; i++){
            String[] wordsInDocument = corpus[i].split(" ");
            for (String word : wordsInDocument) {
                if (word.equals(v)) {
                    invcount++;
                    break;
                }
            }
        }
        if (invcount > 0) {
            inverseDocumentFrequency = Math.log((double) (corpus.length) / invcount) + 1;
        }
        return inverseDocumentFrequency;
    }

    public String[] buildVocabulary(String[] _cleanedContents) {
        Integer maxLength = 0;
        for (String s : _cleanedContents){
            if (s.length() > maxLength){
                maxLength = s.length();
            }
        }
        String[] tempArray = new String[maxLength];
        //TODO 4.4 - 10 marks
        Integer uniqueCount = 0;
        for (String content : _cleanedContents) {
            String[] words = content.split(" ");
            for (String word : words) {
                boolean unique = true;
                for (int i = 0; i < uniqueCount; i++) {
                    if (tempArray[i].equals(word)) {
                        unique = false;
                        break;
                    }
                }
                if (unique) {
                    tempArray[uniqueCount] = word;
                    uniqueCount++;
                }
            }
        }
        String[] arrayVocabulary = new String[uniqueCount];
        for (int i = 0; i < uniqueCount; i++){
            arrayVocabulary[i] = tempArray[i];
        }
        return arrayVocabulary;
    }

    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = new double[newsCleanedContent.length][2];
        //TODO 4.5 - 15 marks
        Vector[] docValues = new Vector[newsCleanedContent.length];
        for (Vector v : docValues){
            v = new Vector(new double[newsCleanedContent.length]);
        }
        double[][] x = calculateTFIDF(newsCleanedContent);
        for (int i = 0; i < newsCleanedContent.length; i++){
            docValues[i] = new Vector(x[i]);
            }
        for (int j = 0; j < newsCleanedContent.length; j++){
            mySimilarity[j][0] = j;
        }
        for (int k = 0; k < newsCleanedContent.length; k++){
            mySimilarity[k][0] = k;
            mySimilarity[k][1] = docValues[_newsIndex].cosineSimilarity(docValues[k]);
        }
        for (int l = 0; l < newsCleanedContent.length; l++){
            for (int m = 0; m < newsCleanedContent.length - 1; m++){
                if (mySimilarity[m][1] < mySimilarity[m + 1][1]){
                    double temp = mySimilarity[m][1];
                    mySimilarity[m][1] = mySimilarity[m + 1][1];
                    mySimilarity[m + 1][1] = temp;
                    temp = mySimilarity[m][0];
                    mySimilarity[m][0] = mySimilarity[m + 1][0];
                    mySimilarity [m+1][0] = temp;
                }
            }
        }
        return mySimilarity;
    }


    public String groupingResults(String _firstTitle, String _secondTitle) {
        int firstIndex = 0;
        int secondIndex = 0;
        //TODO 4.6 - 15 marks
        for (int i = 0; i < newsTitles.length; i++){
            if (newsTitles[i].equals(_firstTitle)){
                firstIndex = i;
                break;
            }
        }
        for (int i = 0; i < newsTitles.length; i++){
            if (newsTitles[i].equals(_secondTitle)){
                secondIndex = i;
                break;
            }
        }
        double[] tempArr1 = new double[newsTitles.length];
        double[] tempArr2 = new double[newsTitles.length];
        int count1 = 0;
        int count2 = 0;
        Vector v1 = new Vector(newsTFIDF[firstIndex]);
        Vector v2 = new Vector(newsTFIDF[secondIndex]);
        for (int i = 0; i < newsTitles.length; i++){
            Vector compare = new Vector(newsTFIDF[i]);
            if (v1.cosineSimilarity(compare) > v2.cosineSimilarity(compare)){
                for (int j = 1; j < newsTitles.length; j++) {
                    if (tempArr1[j] == 0.0) {
                        tempArr1[j] = i;
                        break;
                    }
                }
                count1 ++;
            } else if (v1.cosineSimilarity(compare) < v2.cosineSimilarity(compare)){
                for (int j = 0; j < newsTitles.length; j++) {
                    if (tempArr2[j] == 0){
                        tempArr2[j] = i;
                        break;
                    }
                }
                count2 ++;
            } else if (v1.cosineSimilarity(compare) == v2.cosineSimilarity(compare)){
                for (int j = 0; j < newsTitles.length; j++) {
                    if (tempArr1[j] == 0.0) {
                        tempArr1[j] = i;
                        break;
                    }
                }
                count1 ++;
                for (int j = 0; j < newsTitles.length; j++) {
                    if (tempArr2[j] == 0.0){
                        tempArr2[j] = i;
                        break;
                    }
                }
                count2 ++;
            }
        }
        int[] arrayGroup1 = new int[count1];
        int[] arrayGroup2 = new int[count2];
        for (int i = 0; i < count1; i++){
            arrayGroup1[i] = (int) tempArr1[i];
        }
        for (int i = 0; i < count2; i++){
            arrayGroup2[i] = (int) tempArr2[i];
        }

        for (int i = 0; i < count1; i++){
            for (int j = 0; i < count1 - 1; i++){
                Vector compare = new Vector(newsTFIDF[j]);
                Vector nextCompare = new Vector(newsTFIDF[j+1]);
                if (v1.cosineSimilarity(compare) < v1.cosineSimilarity(nextCompare)){
                    int temp = arrayGroup1[j];
                    arrayGroup1[j] = arrayGroup1[j+1];
                    arrayGroup1[j+1] = temp;
                }
            }
        }
        for (int i = 0; i < count2; i++){
            for (int j = 0; i < count2 - 1; i++){
                Vector compare = new Vector(newsTFIDF[j]);
                Vector nextCompare = new Vector(newsTFIDF[j+1]);
                if (v1.cosineSimilarity(compare) < v1.cosineSimilarity(nextCompare)){
                    int temp = arrayGroup2[j];
                    arrayGroup2[j] = arrayGroup2[j+1];
                    arrayGroup2[j+1] = temp;
                }
            }
        }
        return resultString(arrayGroup1, arrayGroup2);
    }

    public String resultString(double[][] _similarityArray, int _groupNumber) {
        StringBuilder mySB = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        for (int j = 0; j < _groupNumber; j++) {
            for (int k = 0; k < _similarityArray[j].length; k++) {
                if (k == 0) {
                    mySB.append((int) _similarityArray[j][k]).append(" ");
                } else {
                    String formattedCS = decimalFormat.format(_similarityArray[j][k]);
                    mySB.append(formattedCS).append(" ");
                }
            }
            mySB.append(newsTitles[(int) _similarityArray[j][0]]).append("\r\n");
        }
        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public String resultString(int[] _firstGroup, int[] _secondGroup) {
        StringBuilder mySB = new StringBuilder();
        mySB.append("There are ").append(_firstGroup.length).append(" news in Group 1, and ").append(_secondGroup.length).append(" in Group 2.\r\n").append("=====Group 1=====\r\n");

        for (int i : _firstGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }
        mySB.append("=====Group 2=====\r\n");
        for (int i : _secondGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }

        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

}
