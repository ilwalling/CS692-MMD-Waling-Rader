
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

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


    public void parseDma(){
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFile = new File("C:\\Users\\walla\\IdeaProjects\\Walling_Rader_MMD\\test\\TestInput001\\Input\\mmd_test1.dma");
        stringBuilt = readFile(stringBuilt,mmdDmaFile);
        String dmaText = stringBuilt.toString();
        String[] dmaLines = dmaText.split("\n");
//checks to make sure the number of inputs is the same as the number of headers
        for (String line : dmaLines) {
            if(line.split("\\s+").length != headersCount){
                throw new ArrayStoreException("Invalid number of parameters");
            }
        }
        System.out.println(dmaText);

    }

    public void parseDmaFmt(){
        StringBuilder stringBuilt = new StringBuilder();
//needs changed to dynamic
        File mmdDmaFmtFile = new File("C:\\Users\\walla\\IdeaProjects\\Walling_Rader_MMD\\test\\TestInput001\\Input\\mmd_test1.dma_fmt");
        stringBuilt = readFile(stringBuilt,mmdDmaFmtFile);
        String dmaFmtText = stringBuilt.toString();
        String[] dmaFmtLines = dmaFmtText.split("\n");
        headersCount = dmaFmtLines.length;
        String[] lines = new String[headersCount];
        int i = 0;
        for (String line: dmaFmtLines){
            if(line.startsWith("METRIC")){
                try{
                    String[] lineContents = line.split("\\s+");
                    lines[i]= lineContents[1];
                }
                catch (Exception ex){
                    System.out.println("Invalid input");
                }
            }
            else {
                lines[i]=line.trim();
            }
            System.out.println(lines[i]);
            i++;
        }



    }
}

