import java.util.Comparator;
import java.util.function.Predicate;


public class TypeComparator implements Comparator<Type> {
    @Override
    public int compare(Type type, Type t1) {
        return 0;
    }

    public static final Predicate<Type> fireINEffectiveAgainst = (type) -> type == Type.FIRE || type == Type.WATER || type == Type.ROCK || type == Type.DRAGON;
    public static final Predicate<Type> fireISEffectiveAgainst = (type) -> type == Type.GRASS || type == Type.ICE || type == Type.BUG;

    public static final Predicate<Type> waterINEffectiveAgainst = (type) -> type == Type.WATER || type == Type.GRASS || type == Type.DRAGON;
    public static final Predicate<Type> waterISEffectiveAgainst = (type) -> type == Type.FIRE || type == Type.GROUND || type == Type.ROCK;

    public static final Predicate<Type> grassINEffectiveAgainst = (type) -> type == Type.FIRE || type == Type.GRASS || type == Type.FLYING || type == Type.BUG || type == Type.POISON || type == Type.DRAGON;
    public static final Predicate<Type> grassISEffectiveAgainst = (type) -> type == Type.WATER || type == Type.GROUND || type == Type.ROCK;

    public static final Predicate<Type> normalINEffectiveAgainst = (type) -> false;
    public static final Predicate<Type> normalISEffectiveAgainst = (type) -> false;

    //    attacktype    defendertype
    public static Double getY(Type type, Type type2) {
        Predicate<Type> predicate = effectiveAgainst(type);
        Predicate<Type> predicate2 = ineffectiveAgainst(type);
        return (predicate.test(type2) ? 0.5 : (predicate2.test(type2) ? 2.0 : 1));
    }
    public static Predicate<Type>  effectiveAgainst(Type type) {
        switch(type) {
            case FIRE: return fireISEffectiveAgainst;
            case WATER: return waterISEffectiveAgainst;
            case GRASS: return grassISEffectiveAgainst;
            case NORMAL: return normalISEffectiveAgainst;
            default: return null;
        }
    }
    public static Predicate<Type>  ineffectiveAgainst(Type type) {
        switch(type) {
            case FIRE: return fireINEffectiveAgainst;
            case WATER: return waterINEffectiveAgainst;
            case GRASS: return grassINEffectiveAgainst;
            case NORMAL: return normalINEffectiveAgainst;
            default: return null;
        }
    }
    public static Type fromString(String string) {
        switch (string) {
            case "FIRE": return Type.FIRE;
            case "WATER": return Type.WATER;
            case "NORMAL": return Type.NORMAL;
            case "null": return null;
            default:
                throw new IllegalArgumentException("No such type: " + string);
        }
    }
    public static String toString(Type type) {
        if (type == null) {
            return "null";
        }
        switch (type) {
            case FIRE: return "FIRE";
            case WATER: return "WATER";
            case NORMAL: return "NORMAL";
            default:
                throw new IllegalArgumentException("No such type: " + type);
        }
    }
}
