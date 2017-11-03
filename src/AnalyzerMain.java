import org.apache.lucene.util.Version;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.*;

public class AnalyzerMain {

    public static String textExtraction(String directory)throws Exception{

        InputStream stream = new FileInputStream(directory);

        Metadata metadata = new Metadata();

        //Le pasamos el -1 para que no nos salte un error de que el fichero sobrepasa el límite de caracteres.
        ContentHandler handler = new BodyContentHandler(-1);

        ParseContext parseContext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        parser.parse(stream, handler, metadata, parseContext);

        return handler.toString();

    }

    public static void main(String[] args) throws IOException, Exception {

        /*PARTE 1*/

        AnalyzerLucene analycerlucene = new AnalyzerLucene();

        String directorioDatos = System.getProperty("user.dir") + "/datosEntrada/";

        File origen = new File(directorioDatos);

        String [] ficheros = origen.list();

        if (ficheros == null){
            return;
        }

        for (String f: ficheros) {

            if(!f.contains(".DS_Store")) {

                String text = textExtraction(directorioDatos+f);

                try {

                    analycerlucene.displayTokens(text);

                    return;

                } finally {

                }
            }
        }

        /*PARTE 2*/

        /*String ficheroJava = System.getProperty("user.dir") + "/datosEntradaEspeciales/AnalyzerUtils.java";

        AnalyzerJava aj = new AnalyzerJava();

        String textJava = textExtraction(ficheroJava);

        aj.showTokenAnalyzed(textJava);*/


        /*PARTE 3*/
        /*String ficheroPalabras = System.getProperty("user.dir") + "/datosEntradaEspeciales/PalabrasEscondidas.txt";

        AnalyzerSpecialOne aso = new AnalyzerSpecialOne();

        String textWords = textExtraction(ficheroPalabras);

        aso.showTokenAnalyzed(textWords);*/

    }
}
