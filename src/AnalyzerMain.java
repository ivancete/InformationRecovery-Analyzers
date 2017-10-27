import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class AnalyzerMain {

    public static final Analyzer [] analizadores = {
            new WhitespaceAnalyzer(),
            new SimpleAnalyzer(),
            new StopAnalyzer(),
            new StandardAnalyzer(),
            new SpanishAnalyzer(),
            new UAX29URLEmailAnalyzer()
    };

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

    public static String textExtraction(InputStream stream)throws Exception{

        Metadata metadata = new Metadata();

        //Le pasamos el -1 para que no nos salte un error de que el fichero sobrepasa el límite de caracteres.
        ContentHandler handler = new BodyContentHandler(-1);

        ParseContext parseContext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        parser.parse(stream, handler, metadata, parseContext);

        return handler.toString();

    }

    public static void main(String[] args) throws IOException, Exception {

        /*displayTokens("Esto es un mensaje, with some text to write's 123.45 12 AB&C and jhg@decsai.ugr.es http://decsai.ugr.es");
        testLuceneStandardTokenizer();
        displayTokens("Ella dijo: dijeron perro perra perritos perrita  'No me puedo creer que el Madrid presentación presentaremos  ganará la copa de S.M. el Rey en 2015-16'.");
        */

        String directorioDatos = System.getProperty("user.dir") + "/datosEntrada/";

        System.out.println(directorioDatos);

        File origen = new File(directorioDatos);

        String [] ficheros = origen.list();

        if (ficheros == null){
            return;
        }

        for (String f: ficheros) {

            if(!f.contains(".DS_Store")) {

                InputStream stream = new FileInputStream(directorioDatos+f);

                String text = textExtraction(stream);

                try {

                    displayTokens(text);

                } finally {

                }
            }
        }
    }
}
