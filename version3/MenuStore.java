import java.util.Arrays;

public class MenuStore extends ModStore{
    public static final String FileName = "idea/MenuStore";
    public static final int NUM_PROPERTIES = 11;
    public MenuStore (){
        super();
    }

    public boolean processLine(String line, ImageStore imageStore){
       try {
           String[] properties = line.split("\\s");
        if (properties.length == NUM_PROPERTIES) {
            String key = properties[0];
            String[] value = Arrays.copyOfRange(properties, 1, 11);
            put(key, value);
        }
        return (properties.length == NUM_PROPERTIES);
       }
       catch (Exception e) {
           throw new LineProcessingError(e, line);
       }
    }

    public Menu getMenu(String string, ImageStore imageStore) {
        String[] args = getCode(string);
        int anchorx = Integer.parseInt(args[0]);
        int anchory = Integer.parseInt(args[1]);
        Point point = new Point(anchorx, anchory);
        int width = Integer.parseInt(args[2]);
        int height = Integer.parseInt(args[3]);
        int menu_BROWs = Integer.parseInt(args[5]);
        int menu_BCOLs = Integer.parseInt(args[6]);
        int CURSOR_START_X = Integer.parseInt(args[7]);
        int CURSOR_START_Y = Integer.parseInt(args[8]);
        String exit = args[9];
        System.out.println(point + " " + width + " " + height + " " + menu_BROWs + " " + menu_BCOLs + " " + CURSOR_START_X + " " + CURSOR_START_Y);
        Menu menu = new Menu(point, width, height, menu_BROWs, menu_BCOLs, new Cursor(new Point(CURSOR_START_X, CURSOR_START_Y)), exit);
        System.out.println(menu);
        Loadable.tryload(menu, args[4], imageStore);
        menu.getButton(menu.getCursor().getPos()).Highlight();
        return menu;
    }
}
