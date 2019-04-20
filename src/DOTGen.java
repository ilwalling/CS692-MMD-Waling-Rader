import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DOTGen {
    public static final String dotFilePath = "test/TestInput001/Output/dotOut.dot";
    public static Path pathDotFilePath = Paths.get("test/TestInput001/Output/dotOut.dot");
    public static void generateDOT(HashMap<Integer, HashMap<String,String>> modules, HashMap<Integer, ArrayList<ModuleComparison>> fixedNearestComparisons, Integer removedCount, String identifier,
                                   ArrayList<String> normalHeaders, ArrayList<String>metricHeaders, ArrayList<String>xmlTagHeaders){
        ArrayList<String> lines = new ArrayList();
        try {
            PrintWriter writer = new PrintWriter(dotFilePath);
            lines.add("strict graph G{");
            for(int i = 0; i<modules.keySet().size()+removedCount;i++){
                String tempLineString = "";
                if(!modules.keySet().contains(i)) {
                    continue;
                }
                if(modules.get(i).keySet().contains("MetricID")){
                    tempLineString += modules.get(i).get("MetricID");
                    tempLineString += "[shape= circle]";
                }
                else {
                    tempLineString += modules.get(i).get(identifier);
                    tempLineString += "[shape= box]";
                }
                tempLineString += ";";
                lines.add(tempLineString);
            }
            for (int j=0;j<fixedNearestComparisons.keySet().size();j++){
                for(int k = 0; k<fixedNearestComparisons.get(j).size();k++){
                    String tempLineString = "";
                    ModuleComparison currentComparison = fixedNearestComparisons.get(j).get(k);
                    if(!currentComparison.module1.keySet().contains("MetricID")){
                        tempLineString += currentComparison.module1.get(identifier);
                    }
                    else{
                        tempLineString += currentComparison.module1.get("MetricID");
                    }
                    tempLineString += " -- ";
                    if(!currentComparison.module2.keySet().contains("MetricID")){
                        tempLineString += currentComparison.module2.get(identifier);
                    }
                    else{
                        tempLineString += currentComparison.module2.get("MetricID");
                    }

                    String listString = String.join(", ", currentComparison.differentMetrics);
                    tempLineString += "[label = " + listString + "]";

                    if(!lines.contains(tempLineString)){
                        lines.add(tempLineString);
                    }
                }
            }
            lines.add("}");

            Files.write(pathDotFilePath, lines, Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
