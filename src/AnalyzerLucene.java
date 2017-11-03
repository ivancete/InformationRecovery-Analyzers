import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
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

import java.io.*;
import java.util.*;

public class AnalyzerLucene {

    private FileWriter fw;

    private BufferedWriter bw;

    private PrintWriter pw;

    private int totalOccurrences;

    private Map<String, Integer> occurrences;
    private TreeMultimap<Integer, String> occurrencesSorted;

    private static final String [] Analyzer_Cadenas = {
            "WhiteSpaceAnalyzer",
            "SimpleAnalyzer",
            "StopAnalyzer",
            "StandardAnalyzer",
            "SpanishAnalyzer",
            "EmailAnalyzer"
    };

    public static final Analyzer[] analizadores = {
            new WhitespaceAnalyzer(),
            new SimpleAnalyzer(),
            new StopAnalyzer(),
            new StandardAnalyzer(),
            new SpanishAnalyzer(),
            new UAX29URLEmailAnalyzer()
    };

    public AnalyzerLucene (){

        occurrences = new HashMap<String, Integer>();
        occurrencesSorted = TreeMultimap.create(Ordering.natural().reverse(),Ordering.natural());
        totalOccurrences= 0;

    }

    public void tokenizeString(Analyzer analyzer, String string) {


        String cad;

        try {
            TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
            OffsetAttribute offsetAtt = stream.addAttribute(OffsetAttribute.class);
            CharTermAttribute cAtt= stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {

                //cad = stream.getAttribute(CharTermAttribute.class).toString();
                //result.add( cAtt.toString()+" : ("+ offsetAtt.startOffset()+"," + offsetAtt.endOffset()+")");

                if(occurrences.containsKey(cAtt.toString())) {
                    occurrences.put(cAtt.toString(), occurrences.get(cAtt.toString()) + 1);
                }
                else{

                    occurrences.put(cAtt.toString(),1);
                    totalOccurrences++;

                }
            }

            stream.close();

            occurrences.forEach((k,v) -> occurrencesSorted.put(v,k));
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
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

    public void displayTokens( String nombre, String text) throws IOException {

        int i = 0;

        for (Analyzer an : analizadores){
            //System.out.println("Analizador "+an.getClass());

            fw = new FileWriter("datosSalida/Analizador"+Analyzer_Cadenas[i]+nombre+".dat");

            bw = new BufferedWriter(fw);

            pw = new PrintWriter(bw);

            tokenizeString(an,text);

            int j = 1;

            for (Integer key : occurrencesSorted.keySet()){

                Set<String> aux = occurrencesSorted.get(key);

                for (String cadena : aux){
                    pw.println(j + "\t" + key);
                    j++;
                }
            }
            i++;
            pw.close();
            bw.close();
            fw.close();
        }
    }
}
