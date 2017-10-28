import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.*;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.Version;
import org.bouncycastle.util.encoders.BufferedEncoder;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalyzerJava extends Analyzer {

    // Tokens longer than this length are discarded. Defaults to 50 chars. */
    private List<String> stopwords;
    private Version matchVersion;
    OffsetAttribute offsetAtt;


    public AnalyzerJava()throws Exception {

        stopwords = new ArrayList<String>();

        String fileStopWords = System.getProperty("user.dir") + "/stopWords/StopWordsJava.txt";

        this.matchVersion = Version.LUCENE_7_1_0;

        File file = new File (fileStopWords);

        FileReader filereader = new FileReader(file);

        BufferedReader br = new BufferedReader(filereader);

        String linea;
        while((linea=br.readLine())!=null) {
            stopwords.add(linea);
        }

    }

    @Override
    protected TokenStreamComponents createComponents(String string){

        //To change body of generated methods, choose Tools | Templates.

        final Tokenizer source = new MyTokenizer();

        TokenStream pipeline = source;
        pipeline = new StandardFilter(pipeline);

        //pipeline = new EnglishPossessiveFilter(pipeline);

        //pipeline = new ASCIIFoldingFilter(pipeline);
        pipeline = new LowerCaseFilter(pipeline);
        pipeline = new StopFilter(pipeline, new CharArraySet(stopwords,true));
        //pipeline = new PorterStemFilter(pipeline);

        offsetAtt = pipeline.addAttribute(OffsetAttribute.class);


        return new TokenStreamComponents(source, pipeline);
    }

    public void showTokenAnalyzed(String text)throws IOException{

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
