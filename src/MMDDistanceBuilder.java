import java.util.ArrayList;
import java.util.HashMap;

public class MMDDistanceBuilder {
    public HashMap<Integer,ArrayList<ModuleComparison>> calculateDistance(HashMap<Integer, HashMap<String,String>> modules, ArrayList<String> normalHeaders, Integer sizeIncrease){
        //ONLY check on metrics and XML tags NOT regular tags
//Calculates differences between EVERY module
        ArrayList<ModuleComparison> differenceList = new ArrayList();
        for( int i = 0; i<modules.keySet().size()+ sizeIncrease -1; i++){
            for(int j =i+1; j<modules.keySet().size() + sizeIncrease;j++){
                if(!modules.containsKey(i) || !modules.containsKey(j)){
                    continue;
                }
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
//               System.out.println(comparisonLine.differences);
            }
        }

//Finds nearest module(s)
        HashMap<Integer,ArrayList<ModuleComparison> > nearestComparisons = new HashMap();
        for(int j = 0; j<modules.keySet().size() + sizeIncrease; j++){
            HashMap<String,String> emptyModule = new HashMap();
            ModuleComparison leastDifferenceComparison = new ModuleComparison(emptyModule,-1,emptyModule,-1);
            leastDifferenceComparison.differences=999;
            ArrayList<ModuleComparison> nearestNeighbors = new ArrayList();
            for (int i = 0; i<differenceList.size(); i++){
                ModuleComparison comparison = differenceList.get(i);
                if ((comparison.module1Index == j || comparison.module2Index == j) && (comparison.differences < leastDifferenceComparison.differences)){
                    nearestNeighbors.clear();
                    nearestNeighbors.add(comparison);
                    leastDifferenceComparison = comparison;
                    leastDifferenceComparison.differences = comparison.differences;
//                modules.put(modules.size()+1, modules.get(comparison.module1));
//                modules.get(modules.size()+1).put("CloneGroup", "1");
//                modules.remove(comparison.module1Index);
//                modules.remove(comparison.module2Index);
                }
                else if((comparison.module1Index == j || comparison.module2Index == j) && (comparison.differences == leastDifferenceComparison.differences)){
                    nearestNeighbors.add(comparison);
                }

            }
            nearestComparisons.put(j,nearestNeighbors);
            System.out.println(leastDifferenceComparison.module1Index + " " + leastDifferenceComparison.module2Index + ": " + leastDifferenceComparison.differences);
        }
        return nearestComparisons;
    }
}
