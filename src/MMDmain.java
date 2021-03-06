import java.util.ArrayList;
import java.util.HashMap;

public class MMDmain {
    public MMDmain(String inputPath,String natesStuff, String outputPath) throws Exception {
        InputReader reader = new InputReader();
        HashMap<Integer, HashMap<String,String>> modules = reader.parseDma(inputPath);
        MMDDistanceBuilder distanceBuilder = new MMDDistanceBuilder();
        HashMap<Integer,ArrayList<ModuleComparison>> nearestComparisons = distanceBuilder.calculateDistance(modules,reader.normalHeaders,0);
        String identifier = reader.findIdentifier();

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
                    if(reader.normalHeaders.contains("VULNERABILITY")){
                        cloneModule.put("CloneVuln",String.valueOf(Integer.valueOf(comparison.module1.get("VULNERABILITY")) + Integer.valueOf(comparison.module2.get("VULNERABILITY"))));
                    }
                    modules.remove(comparison.module2Index);
                    modules.replace(comparison.module1Index,comparison.module1,cloneModule);
                    removedCount++;
                }
            }
        }
        ArrayList<String> updatedHeaders = reader.normalHeaders;
        updatedHeaders.add("MetricClone");
        HashMap<Integer,ArrayList<ModuleComparison>> fixedNearestComparisons = distanceBuilder.calculateDistance(modules,updatedHeaders, removedCount);
        for(int i = 0; i < fixedNearestComparisons.size(); i++){
            for(int j = 0; j<fixedNearestComparisons.get(i).size(); j++){
                ModuleComparison comparison = fixedNearestComparisons.get(i).get(j);
            }
        }
        for(int i = 0; i < modules.size()+removedCount; i++){
            if(!modules.keySet().contains(i)){
                continue;
            }
        }
        XMLGen genXml = new XMLGen();
        genXml.generateXML(modules,fixedNearestComparisons,removedCount,identifier,reader.normalHeaders,reader.metricHeaders,reader.xmlTagHeaders, outputPath, reader.fileName);
        DOTGen dotGen = new DOTGen();
        dotGen.generateDOT(modules,fixedNearestComparisons,removedCount,identifier,reader.normalHeaders,reader.metricHeaders,reader.xmlTagHeaders, outputPath);

    }
}
