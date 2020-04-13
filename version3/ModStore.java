import java.util.HashMap;
import java.util.Map;

public abstract class ModStore  implements Loadable{
    private Map<String, String[]> codes;
    public ModStore(){
        this.codes = new HashMap<>();
    }
    public String [] getCode(String s){
        return codes.get(s);
    }
    public void put(String key, String[] value) {
        codes.put(key, value);
    }
}
