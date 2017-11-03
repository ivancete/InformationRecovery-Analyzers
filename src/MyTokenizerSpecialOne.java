import org.apache.lucene.analysis.util.CharTokenizer;

public class MyTokenizerSpecialOne extends CharTokenizer {

    public MyTokenizerSpecialOne() {
        super();
    }

    @Override
    protected boolean isTokenChar(int c) {

        return !Character.isSpaceChar(c) && Character.isUpperCase(c) && Character.isAlphabetic(c);

    }
}
