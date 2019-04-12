import java.util.ArrayList;
import java.util.HashMap;

public class MMDmain {
    public static void main(String[] args) {
        InputReader reader = new InputReader();
        HashMap<Integer, HashMap<String,String>> modules = reader.parseDma();
        MMDDistanceBuilder distanceBuilder = new MMDDistanceBuilder();
        HashMap<Integer,ArrayList<ModuleComparison>> nearestComparisons = distanceBuilder.calculateDistance(modules,reader.normalHeaders,0);
        //has to be done here to fix the global 'modules' hashmap
        int removedCount = 0;
        for(int i = 0; i < nearestComparisons.size(); i++){
            for(int j = 0; j<nearestComparisons.get(i).size(); j++){
                ModuleComparison comparison = nearestComparisons.get(i).get(j);
                if (comparison.differences == 0){
                    modules.remove(comparison.module2Index);
                    HashMap<String,String> cloneModule = modules.get(comparison.module1Index);
                    cloneModule.put("MetricClone", "True");
                    modules.replace(comparison.module1Index,comparison.module1,cloneModule);
                    removedCount++;
                }
            }
        }
        ArrayList<String> updatedHeaders = reader.normalHeaders;
        updatedHeaders.add("MetricClone");
        System.out.println("NEW");
        HashMap<Integer,ArrayList<ModuleComparison>> fixedNearestComparisons = distanceBuilder.calculateDistance(modules,updatedHeaders, removedCount);
        System.out.println("FIXED");
        for(int i = 0; i < fixedNearestComparisons.size(); i++){
            for(int j = 0; j<fixedNearestComparisons.get(i).size(); j++){
                ModuleComparison comparison = fixedNearestComparisons.get(i).get(j);
                System.out.println(comparison.module1Index + " " + comparison.module2Index + " :" +comparison.differences);
            }
        }
        for(int i = 0; i < modules.size()+removedCount; i++){
            if(!modules.keySet().contains(i)){
                continue;
            }
            System.out.println(modules.get(i).get("NAME"));
        }





    }
}
