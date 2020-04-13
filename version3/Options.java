public class Options implements Loadable{
    public static final String FileName = ".idea/Preferences";
    public int UP, DOWN, LEFT, RIGHT, START, SAVE, HEAL, ABUTTON, BBUTTON;
    public Options() {
        this.UP = 38;
        this.DOWN = 40;
        this.LEFT = 37;
        this.RIGHT = 39;
        this.START = 80;
        this.SAVE = 83;
        this.HEAL = 72;
        this.ABUTTON = 65;
        this.BBUTTON = 66;
    }
    public String codeToCommand(int input) {
        if (input == UP) {
            return ("UP");
        }
        if (input == DOWN) {
            return ("DOWN");
        }
        if (input == LEFT) {
            return ("LEFT");
        }
        if (input == RIGHT) {
            return ("RIGHT");
        }
        if (input == START) {
            return ("START");
        }
        if (input == SAVE) {
            return ("SAVE");
        }
        if (input == HEAL) {
            return ("HEAL");
        }
        if (input == ABUTTON) {
            return ("ABUTTON");
        }
        if (input == BBUTTON) {
            return ("BBUTTON");
        }
        return "";
    }
    public boolean processLine(String line, ImageStore imageStore)
    {
        try {
            String[] properties = line.split("\\s");
            if (properties.length > 0) {
                switch (properties[0]) {
                    case "UP": UP = asInt(properties[1]);break;
                    case "DOWN": DOWN = asInt(properties[1]);break;
                    case "LEFT": LEFT = asInt(properties[1]);break;
                    case "RIGHT": RIGHT = asInt(properties[1]);break;
                    case "START": START = asInt(properties[1]);break;
                    case "HEAL": HEAL = asInt(properties[1]);break;
                    case "ABUTTON": ABUTTON = asInt(properties[1]);break;
                    case "SAVE": BBUTTON = asInt(properties[1]);break;
                }
            }
            return false;
        }
        catch (Exception e) {
            throw new LineProcessingError(e, line);
        }
    }
    public static int asInt(String s) {
        return Integer.parseInt(s);
    }
}
