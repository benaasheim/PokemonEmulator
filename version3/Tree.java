

public class Tree extends PolyEntity{
    public static final String KEY = "tree";
    private static final int NUM_PROPERTIES = 4;

    public Tree() {
        super(2, 2);
    }

    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {



            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));


            Tree tree = new Tree();

            tree.assembleComponents(imageStore, pt, id, KEY);

            world.tryAddPoly(tree);
        }
        return properties.length == NUM_PROPERTIES;
    }
}
