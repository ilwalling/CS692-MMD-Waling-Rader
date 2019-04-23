
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class InputReader {
    InputFmtChecker fmtChecker = new InputFmtChecker();
    int headersCount = 0;
    ArrayList<String> metricHeaders = new ArrayList();
    ArrayList<String> xmlTagHeaders = new ArrayList();
    ArrayList<String> normalHeaders = new ArrayList();
    String fileName = "";

    private StringBuilder readFile(StringBuilder builder, File fileName) throws Exception{
        String st;
        BufferedReader br;
        try {
            br =  new BufferedReader(new FileReader(fileName));
            while ((st = br.readLine()) != null) {
                builder.append(st + "\n");
            }
        }
        catch (Exception ex) {
            throw new Exception("GENERAL ERROR: We caught ya!  No file Found");
        }
        return  builder;
    }


    public HashMap<Integer, HashMap<String,String>> parseDma(String inputPath) throws Exception{
        HashMap<Integer,String> headers;
        headers = parseDmaFmt(inputPath);
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFileDir = new File( inputPath);
        File[] dmaFiles = mmdDmaFileDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".dma");
            }
        });
        if (dmaFiles.length != 1){
            throw new Exception("GENERAL ERROR: Invalid amount of .dma files");
        }
        stringBuilt = readFile(stringBuilt,dmaFiles[0]);
        fileName = dmaFiles[0].toString();
        String dmaText = stringBuilt.toString();
        String[] dmaLines = dmaText.split("\n");
        if(dmaLines.length == 0){
            throw new Exception("GENERAL ERROR: Invalid number of parameters");
        }

//checks to make sure the number of inputs is the same as the number of headers
        for (String line : dmaLines) {
            if(headersCount != line.split("\\s+").length || headersCount == 0){
                throw new Exception("GENERAL ERROR: Invalid number of parameters");
            }
        }
        return buildModlules(headers,dmaLines);
    }

    private HashMap<Integer,String> parseDmaFmt(String inputPath) throws Exception{
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFmtFile = new File(inputPath);
        File[] dmafmtFiles = mmdDmaFmtFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".dma_fmt");
            }
        });
        if (dmafmtFiles.length != 1){
            throw new Exception("GENERAL ERROR: Invalid amount of .dmafmt files");
        }
        stringBuilt = readFile(stringBuilt,dmafmtFiles[0]);
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
                        throw new Exception("GENERAL ERROR: INVALID XMLTAG or METRIC input");
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
                        throw new Exception("GENERAL ERROR: INVALID XMLTAG or METRIC input");
                    }

                }
                catch (Exception ex){
                    throw new Exception("GENERAL ERROR: Invalid input");
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

    private  HashMap<Integer, HashMap<String,String>> buildModlules(HashMap<Integer,String> headers, String[] moduleLines) throws Exception{
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
        fmtChecker.checkAllInput(modules, metricHeaders, xmlTagHeaders);
        fmtChecker.checkMultiplicity(normalHeaders);
        return modules;
    }

    private void checkType2(ArrayList<String> normalHeaders) throws Exception{
        if (normalHeaders.contains("TYPE2") && !normalHeaders.contains("TYPE")){
            throw new Exception("GENERAL ERROR: INVALID XMLTAG or METRIC input");
        }
    }

    public String findIdentifier() throws  Exception{
        if (normalHeaders.contains("XMI_ID")){
            return "XMI_ID";
        }
        else if(normalHeaders.contains("TEST_NUMBER")){
            return "TEST_NUMBER";
        }
        else if(normalHeaders.contains("NAME")){
            return "NAME";
        }
        else if(normalHeaders.contains("ID")){
            return "ID";
        }
        else if(normalHeaders.contains("CLONE_ID")){
            return "CLONE_ID";
        }
        else if(normalHeaders.contains("DMA_ID")){
            return "DMA_ID";
        }
        throw new Exception("GENERAL ERROR: NO IDENTIFIER");
    }
}

