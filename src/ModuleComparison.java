import java.util.ArrayList;
import java.util.HashMap;

public class ModuleComparison {
    Integer module1Index;
    Integer module2Index;
    HashMap<String,String> module1;
    HashMap<String,String> module2;
    int differences;
    ArrayList<String> differentMetrics;
    public ModuleComparison( HashMap<String,String> module1Input, int module1IndexInput, HashMap<String,String> module2Input, int module2IndexInput){
        module1 = module1Input;
        module1Index = module1IndexInput;
        module2 = module2Input;
        module2Index = module2IndexInput;
    }
}
