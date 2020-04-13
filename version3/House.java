import java.util.List;
import java.util.stream.IntStream;

public class House extends PolyEntity{
    public static final String KEYA = "houseA";
    public static final String KEYB = "houseB";

    //For Parsing
    public static final int DOORKEY = 4;
    public static final int DOORPOINT = 5;
    private static final int NUM_PROPERTIES = 6;

    //Instance Variables
    private String doorImages;
    private String doorCode;
    private int doorspot;
    private int roofAdjust;
    private Point outPoint;

    //Class Methods
    public House(int width, int height, int doorspot, String doorkey, int roofAdjust, String doorCode, Point outPoint) {
        super(width, height);
        this.doorspot = doorspot;
        this.doorImages = doorkey;
        this.roofAdjust = roofAdjust;
        this.doorCode = doorCode;
        this.outPoint = outPoint;
    }


    public static boolean parse(String [] properties, ImageStore imageStore, World world) {
        if (properties.length == NUM_PROPERTIES)
        {


            String id = properties[Functions.ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.COL]),
                    Integer.parseInt(properties[Functions.ROW]));


            House house = houseSpec(properties[Functions.PROPERTY_KEY], properties[DOORKEY], Point.fromString(properties[DOORPOINT]));

            house.assembleComponents(imageStore, pt, id, properties[Functions.PROPERTY_KEY]);
            house.houseSpecs(house.getComponents(), imageStore);
            world.tryAddPoly(house);
        }
        return properties.length == NUM_PROPERTIES;
    }

    public static House houseSpec(String s, String doorCode, Point point) {
        switch (s) {
            case KEYA: return new House(5, 2, 3, "atlantis", 0, doorCode, point);
            case KEYB: return new House(5, 3, 2, "atlantis", 32, doorCode, point);
            default: throw new IllegalArgumentException(s);
        }
    }

    public void houseSpecs(List<Entity> newComponents, ImageStore imageStore) {
        IntStream.range(0, newComponents.size()).forEach(i -> houseTinkering(newComponents, i, imageStore));
    }
    public void houseTinkering(List<Entity> newComponents, int i, ImageStore imageStore) {
        if (i < super.getWidth()) {
            newComponents.get(i).setImageAdjust(roofAdjust);
        }
        else if (i == ((super.getHeight()-1)*(super.getWidth()))+doorspot) {
            Atlantis atlantis = Atlantis.createAtlantis("door", newComponents.get(i).getPosition(), imageStore.getImageList(doorImages), doorCode, outPoint);
            newComponents.set(i, atlantis);
        }
    }

}
