import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AnalyzerLucene {

    public static final Analyzer[] analizadores = {
            new WhitespaceAnalyzer(),
            new SimpleAnalyzer(),
            new StopAnalyzer(),
            new StandardAnalyzer(),
            new SpanishAnalyzer(),
            new UAX29URLEmailAnalyzer()
    };

    public AnalyzerLucene (){

    }

    public static List<String> tokenizeString(Analyzer analyzer, String string) {
        List<String> result = new ArrayList<String>();

        //  StandardTokenizer aux;


        String cad;

        try {
            TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
            OffsetAttribute offsetAtt = stream.addAttribute(OffsetAttribute.class);
            CharTermAttribute cAtt= stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {

                //cad = stream.getAttribute(CharTermAttribute.class).toString();
                result.add( cAtt.toString()+" : ("+ offsetAtt.startOffset()+"," + offsetAtt.endOffset()+")");
            }
            //stream.end();
            stream.close();
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void testLuceneStandardTokenizer() throws Exception {

        StandardTokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader("Ella dijo: 'No me puedo creer  presentación presentaremos que el Madrid ganará la copa de S.M. el jhg.kjh.kjh 123.234.334 Rey en 2015-16'."));
        //StandardTokenizer tokenizer=new StandardTokenizer(new StringReader("Ella dijo: 'No me puedo creer  presentación presentaremos que el Madrid ganará la copa de S.M. el jhg.kjh.kjh 123.234.334 Rey en 2015-16'."));
        List<String> result=new ArrayList<String>();
        tokenizer.reset();

        while (tokenizer.incrementToken()) {

            result.add(((CharTermAttribute)tokenizer.getAttribute(CharTermAttribute.class)).toString());
        }
        System.out.println(result.toString());
    }

    public static void displayTokens(  String text) throws IOException {

        List<String> tokens;

        for (Analyzer an : analizadores){
            System.out.println("Analizador "+an.getClass());
            tokens = tokenizeString(an,text);
            for (String tk : tokens) {
                System.out.println("[" + tk + "] ");
            }
        }
    }
}
