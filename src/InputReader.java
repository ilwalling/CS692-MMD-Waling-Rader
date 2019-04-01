
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class InputReader {
    int headersCount = 0;

    public StringBuilder readFile(StringBuilder builder, File fileName){
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
        HashMap<Integer,String> headers = new HashMap();
        headers = parseDmaFmt();
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFile = new File("test/TestInput001/Input/mmd_test.dma");
        stringBuilt = readFile(stringBuilt,mmdDmaFile);
        String dmaText = stringBuilt.toString();
        String[] dmaLines = dmaText.split("\n");
//checks to make sure the number of inputs is the same as the number of headers
        for (String line : dmaLines) {
            if(line.split("\\s+").length != headersCount){
                throw new ArrayStoreException("Invalid number of parameters");
            }
        }
        return buildModlules(headers,dmaLines);
    }

    public HashMap<Integer,String> parseDmaFmt(){
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFmtFile = new File("test/TestInput001/Input/mmd_test1.dma_fmt");
        stringBuilt = readFile(stringBuilt,mmdDmaFmtFile);
        String dmaFmtText = stringBuilt.toString();
        String[] dmaFmtLines = dmaFmtText.split("\n");
        headersCount = dmaFmtLines.length;
        ArrayList<String> normalHeaders = new ArrayList();
        ArrayList<String> metricHeaders = new ArrayList();
        ArrayList<String> xmlTagHeaders = new ArrayList();
        HashMap<Integer,String> headers = new HashMap();
        int i = 0;
        for (String line: dmaFmtLines){
            if(line.startsWith("METRIC") || line.startsWith("XMLTAG")){
                try{
                    String[] lineContents = line.split("\\s+");
                    if(lineContents[0].equals("METRIC")){
                        headers.put(i,lineContents[1].trim());
                        metricHeaders.add(lineContents[1].trim());
                    }
                    else if(lineContents[0].equals("XMLTAG")){
                        headers.put(i,lineContents[1].trim());
                        xmlTagHeaders.add(lineContents[1].trim());
                    }
                    else {
                        throw new ArrayStoreException("INVALID XMLTAG or METRIC input");
                    }

                }
                catch (Exception ex){
                    System.out.println("Invalid input");
                }
            }
            else {
                headers.put(i,line.trim());
                normalHeaders.add(line.trim());
            }
            i++;
        }
        return headers;
    }


    public  HashMap<Integer, HashMap<String,String>> buildModlules(HashMap<Integer,String> headers, String[] moduleLines){
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
}

