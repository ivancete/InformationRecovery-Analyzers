import org.apache.lucene.analysis.util.CharTokenizer;

public class MyTokenizerJava extends CharTokenizer {

    public MyTokenizerJava() {
        super();
    }

    @Override
    protected boolean isTokenChar(int c) {
        return !Character.isSpaceChar(c) && (Character.isDigit(c) || Character.isAlphabetic(c));
    }
}