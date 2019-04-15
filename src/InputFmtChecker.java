import java.util.*;

public class InputFmtChecker {

    public void checkAllInput(HashMap<Integer, HashMap<String, String>> modules, ArrayList<String> metricHeaders, ArrayList<String> xmlHeaders){
        for (int i = 0; i < modules.keySet().size(); i++){
            for (String header:modules.get(i).keySet()) {
                if(header.equals("TYPE2") || header.equals("XMI_ID") || header.equals("ENUM") || header.equals("TEST_NUMBER") || header.equals("NAME")
                 || header.equals("ID") || header.equals("FILENAME") || header.equals("ENUM2") || header.equals("CLONE_ID") || header.equals("CATEGORY")
                || header.equals("SIGNATURE") || header.equals("TYPE") || header.equals("DMA_ID") || header.equals("CHAR") ||
                        header.equals("BENCHMARK_FILENAME") || header.equals("BENCHMARK_VERSION")){
                    try{
                        String.valueOf(modules.get(i).get(header));
                    }
                    catch(Exception ex){
                        System.out.println("GENERAL ERROR: INVALID INPUT TYPE");
                    }
                }
                else if(header.equals("INT") || header.equals("CLONE_MIXED_TYPE") || header.equals("ENDING_LINENO") || header.equals("CWE")
                || header.equals("CLONE__TYPE") || header.equals("CHANGE") || header.equals("EFFORT") || header.equals("CLONE") ||
                        header.equals("BEGINNING_LINENO") || header.equals("VULNERABILITY") || metricHeaders.contains(header) || xmlHeaders.contains(header)){
                    try{
                        Integer intValue = Integer.valueOf(modules.get(i).get(header));
                        if(header.equals("CLONE_MIXED_TYPE") || header.equals("CLONE__TYPE") || header.equals("CLONE") ||header.equals("VULNERABILITY")){
                            if (intValue != 1 && intValue != 0){
                                throw new IllegalArgumentException("GENERAL ERROR: INVALID INPUT TYPE");
                            }
                        }
                        if(!String.valueOf(intValue).equals(modules.get(i).get(header))){
                            throw new IllegalArgumentException("GENERAL ERROR: INVALID INPUT TYPE");
                        }
                    }
                    catch(Exception ex){
                        System.out.println("GENERAL ERROR: INVALID INPUT TYPE");
                    }
                }
                else{
                    throw new IllegalArgumentException("GENERAL ERROR: INVALID INPUT TYPE");
                }


            }
        }
    }

    public void checkMultiplicity(ArrayList<String> normalHeaders){
        Set<String> distinct = new HashSet<>(normalHeaders);
        for(String header:distinct){
            if(header.equals("TYPE2") || header.equals("XMI_ID") || header.equals("ENUM") || header.equals("CLONE_MIXED_TYPE")
            || header.equals("ENDING_LINENO") || header.equals("CWE") || header.equals("TEST_NUMBER") || header.equals("CLONE_TYPE") ||
            header.equals("NAME") || header.equals("ID") || header.equals("FILENAME") || header.equals("CHANGE") || header.equals("ENUM2") ||
                    header.equals("CLONE_ID") || header.equals("CATEGORY") || header.equals("SIGNATURE") || header.equals("TYPE") ||
                    header.equals("DMA_ID") || header.equals("EFFORT") || header.equals("CLONE") ||header.equals("BEGINNING_LINENO") ||
                    header.equals("VULERABILITY") || header.equals("BENCHMARK_FILENAME") || header.equals("BENCHMARK_VERSION")){
                if(Collections.frequency(normalHeaders,header) != 1){
                    throw new IllegalArgumentException("GENERAL ERROR: INVALID MULTIPLICITY TYPE");
                }
            }
        }

    }
}
