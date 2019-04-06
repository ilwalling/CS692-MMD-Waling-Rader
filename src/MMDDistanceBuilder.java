import java.util.ArrayList;
import java.util.HashMap;

public class MMDDistanceBuilder {
    public void buildDistanceMapping(HashMap<Integer, HashMap<String,String>> modules, ArrayList<String> normalHeaders){
        //ONLY check on metrics and XML tags NOT regular tags
        ArrayList<ModuleComparison> differenceList = new ArrayList();
        for( int i = 0; i<modules.keySet().size()-1; i++){
            for(int j =i+1; j<modules.keySet().size();j++){
                ModuleComparison comparisonLine = new ModuleComparison(modules.get(i),i,modules.get(j),j);
                int numOfDifferences = 0;
                ArrayList<String> differentHeadings = new ArrayList();
                for (String heading:modules.get(i).keySet()) {

                    if(!normalHeaders.contains(heading) && !modules.get(i).get(heading).equals(modules.get(j).get(heading))){
                        differentHeadings.add(heading);
                        numOfDifferences++;
                    }
                }
                comparisonLine.differences = numOfDifferences;
                comparisonLine.differentMetrics = differentHeadings;
                differenceList.add(comparisonLine);

                System.out.println("New Comparison");
                System.out.println(i);
                System.out.println(j);
                System.out.println(comparisonLine.differences);
            }
        }

    }
}
