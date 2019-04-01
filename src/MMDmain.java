import java.util.HashMap;

public class MMDmain {
    public static void main(String[] args) {
        InputReader reader = new InputReader();
        HashMap<Integer, HashMap<String,String>> modules = reader.parseDma();
        System.out.println(modules.get(0).get("CC"));


    }
}
