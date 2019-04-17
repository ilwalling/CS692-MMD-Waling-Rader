import java.util.ArrayList;
import java.util.HashMap;

public class MMDmain {
    public static void main(String[] args) {
        InputReader reader = new InputReader();
        HashMap<Integer, HashMap<String,String>> modules = reader.parseDma();
        MMDDistanceBuilder distanceBuilder = new MMDDistanceBuilder();
        HashMap<Integer,ArrayList<ModuleComparison>> nearestComparisons = distanceBuilder.calculateDistance(modules,reader.normalHeaders,0);
        String identifier = reader.findIdentifier();
        System.out.println(identifier);

        //has to be done here to fix the global 'modules' hashmap
        int removedCount = 0;
        for(int i = 0; i < nearestComparisons.size(); i++){
            for(int j = 0; j<nearestComparisons.get(i).size(); j++){
                ModuleComparison comparison = nearestComparisons.get(i).get(j);
                if (comparison.differences == 0){

                    HashMap<String,String> cloneModule = modules.get(comparison.module1Index);
                    cloneModule.put("Module1",comparison.module1.get(identifier));
                    cloneModule.put("Module2",comparison.module2.get(identifier));
                    cloneModule.put("MetricID", "MetricClone" + removedCount);
                    modules.remove(comparison.module2Index);
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
        XMLGen genXml = new XMLGen();
        genXml.generateXML(modules,fixedNearestComparisons,removedCount,identifier);





    }
}
