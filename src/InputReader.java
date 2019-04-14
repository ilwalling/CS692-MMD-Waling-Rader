
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class InputReader {
    int headersCount = 0;
    ArrayList<String> metricHeaders = new ArrayList();
    ArrayList<String> xmlTagHeaders = new ArrayList();
    ArrayList<String> normalHeaders = new ArrayList();

    private StringBuilder readFile(StringBuilder builder, File fileName){
        String st;
        BufferedReader br;
        try {
            br =  new BufferedReader(new FileReader(fileName));
            while ((st = br.readLine()) != null) {
                builder.append(st + "\n");
            }
        }
        catch (Exception ex) {
            System.out.println("We caught ya!  No file Found");
        }
        return  builder;
    }


    public HashMap<Integer, HashMap<String,String>> parseDma(){
        HashMap<Integer,String> headers;
        headers = parseDmaFmt();
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFile = new File("test/TestInput001/Input/mmd_test.dma");
        stringBuilt = readFile(stringBuilt,mmdDmaFile);
        String dmaText = stringBuilt.toString();
        String[] dmaLines = dmaText.split("\n");
        if(dmaLines.length == 0){
            throw new ArrayStoreException("GENERAL ERROR: Invalid number of parameters");
        }

//checks to make sure the number of inputs is the same as the number of headers
        for (String line : dmaLines) {
            if(headersCount != line.split("\\s+").length || headersCount == 0){
                throw new ArrayStoreException("GENERAL ERROR: Invalid number of parameters");
            }
        }
        return buildModlules(headers,dmaLines);
    }

    private HashMap<Integer,String> parseDmaFmt(){
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFmtFile = new File("test/TestInput001/Input/mmd_test1.dma_fmt");
        stringBuilt = readFile(stringBuilt,mmdDmaFmtFile);
        String dmaFmtText = stringBuilt.toString().toUpperCase();
        String[] dmaFmtLines = dmaFmtText.split("\n");
        headersCount = dmaFmtLines.length;


        HashMap<Integer,String> headers = new HashMap();
        int i = 0;
        for (String line: dmaFmtLines){
            if(line.startsWith("METRIC") || line.startsWith("XMLTAG")){
                try{
                    String[] lineContents = line.split("\\s+");
                    if(lineContents.length != 2 || lineContents[1].equals("METRIC") || lineContents[1].equals("XMLTAG")){
                        throw new ArrayStoreException("GENERAL ERROR: INVALID XMLTAG or METRIC input");
                    }
                    if(lineContents[0].equals("METRIC")){
                        headers.put(i,lineContents[1].trim());
                        metricHeaders.add(lineContents[1].trim());
                    }
                    else if(lineContents[0].equals("XMLTAG")){
                        headers.put(i,lineContents[1].trim());
                        xmlTagHeaders.add(lineContents[1].trim());
                    }
                    else {
                        throw new ArrayStoreException("GENERAL ERROR: INVALID XMLTAG or METRIC input");
                    }

                }
                catch (Exception ex){
                    System.out.println("GENERAL ERROR: Invalid input");
                }
            }
            else {
                headers.put(i,line.trim());
                normalHeaders.add(line.trim());
            }
            i++;
        }
        checkType2(normalHeaders);
        return headers;
    }

    private  HashMap<Integer, HashMap<String,String>> buildModlules(HashMap<Integer,String> headers, String[] moduleLines){
        HashMap<Integer, HashMap<String,String>> modules = new HashMap();
        for(int i = 0; i<moduleLines.length; i++){
            HashMap<String,String> module = new HashMap();
            String[] moduleValues;
            moduleValues = moduleLines[i].split("\\s+");
            for(int j = 0; j<moduleValues.length; j++){
                module.put(headers.get(j),moduleValues[j]);
            }
            modules.put(i,module);
        }
        return modules;
    }

    private void checkType2(ArrayList<String> normalHeaders){
        if (normalHeaders.contains("TYPE2") && !normalHeaders.contains("TYPE")){
            throw new ArrayStoreException("GENERAL ERROR: INVALID XMLTAG or METRIC input");
        }
    }
}

