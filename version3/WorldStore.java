import java.util.Arrays;

public class WorldStore extends ModStore{
    public static final String FileName = ".idea/WorldStore";
    public static final int NUM_PROPERTIES = 4;

    public WorldStore(){
        super();
    }

    public boolean processLine(String line, ImageStore imageStore) {
        try {
            String[] properties = line.split("\\s");
            if (properties.length == NUM_PROPERTIES) {
                String key = properties[0];
                String[] value = Arrays.copyOfRange(properties, 1, 4);
                put(key, value);
            }
            return (properties.length == NUM_PROPERTIES);
        }
        catch (Exception e) {
            throw new LineProcessingError(e, line);
        }
    }

    public World getWorld(String string, ImageStore imageStore) {
        String[] args = getCode(string);
        World world = new World(VirtualWorld.WORLD_ROWS, VirtualWorld.WORLD_COLS, imageStore.createDefaultBackground());
        Loadable.tryload(world, args[2], imageStore);
        return world;
    }
}
