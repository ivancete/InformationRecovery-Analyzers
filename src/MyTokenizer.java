import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.Reader;

public class MyTokenizer extends CharTokenizer {

    public MyTokenizer() {
        super();
    }

    @Override
    protected boolean isTokenChar(int c) {
        return !Character.isSpaceChar(c) && (Character.isDigit(c) || Character.isAlphabetic(c));
    }


}