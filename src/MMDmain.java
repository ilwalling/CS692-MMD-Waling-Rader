import java.util.HashMap;

public class MMDmain {
    public static void main(String[] args) {
        InputReader reader = new InputReader();
        HashMap<Integer, HashMap<String,String>> modules = reader.parseDma();
        MMDDistanceBuilder distanceBuilder = new MMDDistanceBuilder();
        distanceBuilder.buildDistanceMapping(modules,reader.normalHeaders);





    }
}
