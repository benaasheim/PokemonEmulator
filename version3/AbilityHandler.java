
public interface AbilityHandler {
    static Ability fromString(String string) {
        switch (string) {
            case "ADAPTABILITY": return Ability.Adaptability;
            case "GUTS": return Ability.Guts;
            case "null": return null;
            default:
                throw new IllegalArgumentException("No such type: " + string);
        }
    }
    static String toString(Ability type) {
        if (type == null) {
            return "null";
        }
        switch (type) {
            case Guts: return "GUTS";
            case Adaptability: return "ADAPTABILITY";
            default:
                throw new IllegalArgumentException("No such type: " + type);
        }
    }
}
