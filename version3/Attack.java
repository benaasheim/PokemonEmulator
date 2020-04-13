
/*
Attack: A pokemon's attack on another Pokemon, typically inflicting damage on an opponent
 */

public class Attack {

    //Universal Values
    private static final String SCRATCH = "Scratch", TACKLE = "Tackle", WATERGUN = "WaterGun", EMBER = "Ember";
    private static final Attack Scratch = new Attack(SCRATCH, Type.NORMAL, Base.Phys, 35, 28, 90);
    private static final Attack Tackle = new Attack(TACKLE, Type.NORMAL, Base.Phys, 25, 40, 95);
    private static final Attack WaterGun = new Attack(WATERGUN, Type.NORMAL, Base.Phys, 10, 50, 80);
    private static final Attack Ember = new Attack(EMBER, Type.NORMAL, Base.Phys, 10, 50, 80);

    //Instance Variables
    private Type type;
    private Base base;
    private int pp;
    private int pow;
    private double acc;
    private String name;
    private int MAXpp;

    //Class methods
    public Attack(String name, Type type, Base base, int pp, int pow, double acc) {
        this.name = name;
        this.type = type;
        this.base = base;
        this.pp = pp;
        this.MAXpp = pp;
        this.pow = pow;
        this.acc = acc;
    }
    public boolean equals(Object other) {
        return other instanceof Attack && name.equals(((Attack) other).name);
    }

    public static Attack attackFromName(String string) {
        switch (string) {
            case SCRATCH: return Scratch;
            case TACKLE: return Tackle;
            case WATERGUN: return WaterGun;
            case EMBER: return Ember;
            default: return null;
        }
    }

    public static Quake quakeFromName(Point point, ImageStore imageStore, String string) {
        int rc;
        switch (string) {
            case WATERGUN: rc = 6; return new Quake(string, point, imageStore.getImageList(string), (rc + 1)*100, 50, rc);
            case EMBER: rc = 13; return new Quake(string, point, imageStore.getImageList(string), (rc + 1)*100, 50, rc);
            case TACKLE: rc = 3; return new Quake(string, point, imageStore.getImageList(string), (rc + 1)*100, 50, rc);
            default: rc = 10; return new Quake(string, point, imageStore.getImageList(string), (rc + 3)*100, 25, rc);
        }
    }

    //Accessors
    public Type getType() {
        return type;
    }
    public Base getBase() {
        return base;
    }
    public int getPp() {
        return pp;
    }
    public int getPow() {
        return pow;
    }
    public double getAcc() {
        return acc;
    }
    public String getName() {
        return name;
    }

    //Mutators
    public void restorePP() {
        pp = MAXpp;
    }
    public void setPp( int i) {
        pp = i;
    }
    public void minus1PP() {
        pp -= 1;
    }
}
