import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.*;
import java.util.*;
import java.util.List;

public class AnalyzerSpecialOne extends Analyzer{


    // Tokens longer than this length are discarded. Defaults to 50 chars. */
    private List<String> stopwords;
    OffsetAttribute offsetAtt;


    public AnalyzerSpecialOne()throws Exception {

        stopwords = new ArrayList<String>();

        String fileStopWords = System.getProperty("user.dir") + "/stopWords/StopWordsPalabrasEscondidas.txt";

        File file = new File (fileStopWords);

        FileReader filereader = new FileReader(file);

        BufferedReader br = new BufferedReader(filereader);

        String linea;
        while((linea=br.readLine())!=null) {
            stopwords.add(linea);
        }

    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String string){

        //To change body of generated methods, choose Tools | Templates.

        final Tokenizer source = new MyTokenizerSpecialOne();

        TokenStream pipeline = source;
        pipeline = new StandardFilter(pipeline);
        pipeline = new StopFilter(pipeline, new CharArraySet(stopwords,true));

        offsetAtt = pipeline.addAttribute(OffsetAttribute.class);


        return new Analyzer.TokenStreamComponents(source, pipeline);
    }

    public void showTokenAnalyzed(String text)throws IOException {

        TokenStream stream = this.tokenStream("field", new StringReader(text));

        // get the CharTermAttribute from the TokenStream
        CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

        try {
            stream.reset();

            // print all tokens until stream is exhausted
            while (stream.incrementToken()) {
                System.out.println(termAtt.toString()+" : ("+ offsetAtt.startOffset()+"," + offsetAtt.endOffset()+")");
            }

            stream.end();
        } finally {
            stream.close();
        }
    }
}
