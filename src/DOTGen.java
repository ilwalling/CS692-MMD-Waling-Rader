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
   // public static final String dotFilePath = "test/TestInput008/Output/dotOut.dot";

    public void generateDOT(HashMap<Integer, HashMap<String,String>> modules, HashMap<Integer, ArrayList<ModuleComparison>> fixedNearestComparisons, Integer removedCount, String identifier,
                                   ArrayList<String> normalHeaders, ArrayList<String>metricHeaders, ArrayList<String>xmlTagHeaders, String outputPath) throws Exception{
        ArrayList<String> lines = new ArrayList();
        try {
            Path pathDotFilePath = Paths.get(outputPath + "dotOut.dot");
            //PrintWriter writer = new PrintWriter(dotFilePath);
            lines.add("strict graph G{");
            for(int i = 0; i<modules.keySet().size()+removedCount;i++){
                String tempLineString = "";
                if(!modules.keySet().contains(i)) {
                    continue;
                }
                if(modules.get(i).keySet().contains("MetricID")){
                    tempLineString += modules.get(i).get("MetricID");
                    tempLineString += "[shape= circle";
                    if (modules.get(i).containsKey("CloneVuln")){
                        String color = "";
                        if(Integer.valueOf(modules.get(i).get("CloneVuln")) == 1){
                            tempLineString += ", style = filled, fillcolor = yellow";
                        }
                        else if(Integer.valueOf(modules.get(i).get("CloneVuln")) > 1){
                            tempLineString += ", style = filled, fillcolor = red";
                        }
                    }
                    tempLineString += "]";

                }
                else {
                    tempLineString += modules.get(i).get(identifier);
                    tempLineString += "[shape= box";
                    if(modules.get(i).keySet().contains("VULNERABILITY")){
                        if(Integer.valueOf(modules.get(i).get("VULNERABILITY")) != 0){
                            tempLineString +=", style = filled, fillcolor = red";
                        }
                    }
                    tempLineString += "]";
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
                    String diffHeaders = "";
                    for(int counter = 0; counter<currentComparison.differentMetrics.size();counter++){
                        if(normalHeaders.contains(currentComparison.differentMetrics.get(counter))||
                        metricHeaders.contains(currentComparison.differentMetrics.get(counter)) ||
                        xmlTagHeaders.contains(currentComparison.differentMetrics.get(counter))){
                            if(diffHeaders.equals("")){
                                diffHeaders += currentComparison.differentMetrics.get(counter);
                            }else{
                                diffHeaders += " ," + currentComparison.differentMetrics.get(counter);
                            }


                        }
                    }
                    tempLineString += "[label = \"" + diffHeaders + "\"];";

                    if(!lines.contains(tempLineString)){
                        lines.add(tempLineString);
                    }
                }
            }
            lines.add("}");
            Files.write(pathDotFilePath, lines, Charset.forName("UTF-8"));
            System.out.println("Done printing .dot file");
        } catch (FileNotFoundException e) {
            throw new Exception("GENERAL ERROR: Output path not found");
        } catch (IOException e) {
            throw new Exception("GENERAL ERROR: I/O Error");
        }


    }
}
