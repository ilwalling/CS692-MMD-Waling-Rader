import java.lang.reflect.Array;
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


//                System.out.println("New Comparison");
//                System.out.println(i + " " + j);
//                System.out.println(comparisonLine.differences);
            }
        }
        ArrayList<ModuleComparison> nearestComparisons = new ArrayList();
        for(int j = 0; j<modules.keySet().size(); j++){
            HashMap<String,String> emptyModule = new HashMap();
            ModuleComparison leastDifferenceComparison = new ModuleComparison(emptyModule,-1,emptyModule,-1);
            leastDifferenceComparison.differences=999;
            for (int i = 0; i<differenceList.size(); i++){
                ModuleComparison comparison = differenceList.get(i);
                if ((comparison.module1Index == j || comparison.module2Index == j) && (comparison.differences < leastDifferenceComparison.differences)){
                    leastDifferenceComparison = comparison;
                    leastDifferenceComparison.differences = comparison.differences;
//                modules.put(modules.size()+1, modules.get(comparison.module1));
//                modules.get(modules.size()+1).put("CloneGroup", "1");
//                modules.remove(comparison.module1Index);
//                modules.remove(comparison.module2Index);

                }
            }
            nearestComparisons.add(leastDifferenceComparison);
            System.out.println(leastDifferenceComparison.module1Index + " " + leastDifferenceComparison.module2Index + ": " + leastDifferenceComparison.differences);
        }


    }
}
