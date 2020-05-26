import java.util.LinkedList;
import java.util.List;

public class Pokemon {
    private String name;
    private int LVL;
    private int HP;
    private int MAXHP;
    private int Att;
    private int MAXATT;
    private int Def;
    private int MAXDEF;
    private int Spd;
    private int MAXSPD;
    private int SpcA;
    private int MAXSPCA;
    private int SpcD;
    private int MAXSPCD;
    private Type type;
    private Ability ability;
    private boolean Burned;
    private List<Attack> attacks;

    public Pokemon(String name, int LVL, int HP, int Att, int Def, int Spd, int SpcA, int SpcD, Type type, Ability ability) {
        this.name = name;
        this.LVL = LVL;
        this.HP = HP;
        this.MAXHP = HP;
        this.Att = Att;
        this.MAXATT = Att;
        this.Def = Def;
        this.MAXDEF = Def;
        this.Spd = Spd;
        this.MAXSPD = Spd;
        this.SpcA = SpcA;
        this.MAXSPCA = SpcA;
        this.SpcD = SpcD;
        this.MAXSPCD = SpcD;
        this.type = type;
        this.ability = ability;
        this.Burned = false;
        this.attacks = new LinkedList<>();
    }
    public Pokemon(String name, int LVL, int HP, int MAXHP, int Att, int MAXATT, int MAXDEF, int MAXSPD, int MAXSPCA, int MAXSPCD, int Def, int Spd, int SpcA, int SpcD, Type type, Ability ability) {
        this.name = name;
        this.LVL = LVL;
        this.HP = HP;
        this.MAXHP = MAXHP;
        this.Att = Att;
        this.MAXATT = MAXATT;
        this.Def = Def;
        this.MAXDEF = MAXDEF;
        this.Spd = Spd;
        this.MAXSPD = MAXSPD;
        this.SpcA = SpcA;
        this.MAXSPCA = MAXSPCA;
        this.SpcD = SpcD;
        this.MAXSPCD = MAXSPCD;
        this.type = type;
        this.ability = ability;
        this.Burned = false;
        this.attacks = new LinkedList<>();
    }

    //Accessors
    public String getName() {
        return name;
    }
    public int getLVL() {
        return LVL;
    }
    public int getHP() {
        return HP;
    }
    public int getMAXHP() {
        return MAXHP;
    }
    public int getAtt() {
        return Att;
    }
    public int getDef() {
        return Def;
    }
    public int getSpd() {
        return Spd;
    }
    public int getSpcA() {
        return SpcA;
    }
    public int getSpcD() {
        return SpcD;
    }
    public Type getType() {
        return type;
    }
    public boolean isBurned() {
        return Burned;
    }
    public List<Attack> getAttacks() {
        return attacks;
    }
    public Ability getAbility() {
        return ability;
    }
    public double getBadge() {
        return 1;
    }
    public double getCrit() {
        return 1;
//        return (double)Spd / 2;
    }
    public boolean stillPP(String string) {
        for (Attack attack : this.attacks) {
            System.out.println(attack.getName() + " pp: " + attack.getPp());
            if (attack.getName().equals(string) && attack.getPp() > 0) {
                return true;
            }
        }
        return false;
    }

    //Mutators
    public void restoreAllStatPP() {
        restoreHealth();
        Att = MAXATT;
        Def = MAXDEF;
        Spd = MAXSPD;
        SpcA = MAXSPCA;
        SpcD = MAXSPCD;
        restoreAllPP();
    }
    public void restoreHealth() {
        HP = MAXHP;
    }
    public void restoreAllPP() {
        for (Attack attack : attacks) {
            attack.restorePP();
        }
    }
    public boolean fasterThan (Pokemon pokemon) {
        return getSpd() >= pokemon.getSpd();
    }
    public void takeDamage(int i) {
        setHP(getHP() - i);
    }
    public void setHP(int i) {
        HP = i;
    }
    public void pickAttack(String string, Pokemon pokemon) {
        if (!string.equals(" ")) {
            for (Attack attack : attacks) {
                if (attack.getName().equals(string) && attack.getPp() > 0) {
                    System.out.println("RIGHT HERE!");
                    attack(attack, pokemon);
                    attack.minus1PP();
                }
            }
        }
    }
    public void addAttack(Attack attack) {
        if (!attacks.contains(attack)) {
            attacks.add(attack);
        }
    }
    public void addAttack(Attack attack, int pp) {
        if (!attacks.contains(attack)) {
            attacks.add(attack);
            attack.setPp(pp);
        }
        else {
            int ind = attacks.indexOf(attack);
            attacks.get(ind).setPp(pp);
        }
    }
    public void attack(Attack attack, Pokemon defender){
        double damage = 0;
        if (attack != null) {
            damage = calcDamage(this, defender, attack);
            System.out.println(name + " deals " + damage + " damage to " + defender.getName());
        }
        defender.takeDamage((int)damage);
    }

    //Pokemon(String name, int LVL, int HP, int Att, int Def, int Spd, int SpcA, int SpcD, Type type, Ability ability)
    public static Pokemon newSquirtle() {
        Pokemon pokemon = new Pokemon("Squirtle", 5, 44, 48, 65, 65, 50, 64, Type.WATER, null);
        pokemon.addAttack(Attack.attackFromName("WaterGun"));
        return pokemon;
    }
    public static Pokemon newZigzagoon() {
        Pokemon pokemon = new Pokemon("Zigzagoon", 3, 38, 30, 41, 60, 30, 41, Type.NORMAL, null);
        pokemon.addAttack(Attack.attackFromName("Tackle"));
        return pokemon;
    }
    public static Pokemon newCinderpiller() {
        Pokemon pokemon = new Pokemon("Cinderpillar", 5, 44, 48, 65, 65, 50, 64, Type.FIRE, null);
        pokemon.addAttack(Attack.attackFromName("Ember"));
        return pokemon;
    }
    public static Pokemon pokemonfromname(String name){
        switch (name) {
            case "Squirtle": return newSquirtle();
            case "Cinderpillar": return newCinderpiller();
            default: return newZigzagoon();
        }
    }
    public String toString() {
        var ref = new Object() {
            String PPs = "";
        };
        attacks.stream().forEach(attack -> ref.PPs += attack.getName() + " " + attack.getPp() + " ");
        return name + " " + LVL + " " + HP + " " +
                MAXHP + " " + Att + " " + MAXATT + " " +
                MAXDEF + " " + MAXSPD + " " + MAXSPCA + " " +
                MAXSPCD + " " + Def + " " + Spd + " " +  SpcA + " " +
                SpcD + " " + TypeComparator.toString(type) + " " + AbilityHandler.toString(ability) + " " +
                ref.PPs;
    }
    public static Pokemon fromString(String[] properties) {
        Pokemon pokemon = new Pokemon(properties[0], asInt(properties[1]), asInt(properties[2]), asInt(properties[3]),
                asInt(properties[4]), asInt(properties[5]), asInt(properties[6]), asInt(properties[7]),
                asInt(properties[8]), asInt(properties[9]), asInt(properties[10]), asInt(properties[11]),
                asInt(properties[12]), asInt(properties[13]), TypeComparator.fromString(properties[14]),
                AbilityHandler.fromString(properties[15]));
        for (int i = 16; i < properties.length; i+=2) {
            pokemon.addAttack(Attack.attackFromName(properties[i]), asInt(properties[i+1]));
        }
        return pokemon;
    }
    public static int asInt(String s) {
        return Integer.parseInt(s);
    }
    public static double calcDamage(Pokemon attacker, Pokemon defender, Attack attack) {
        double A = (attack.getBase() == Base.Phys ? attacker.getAtt() : attacker.getSpcA());
        double D = (attack.getBase() == Base.Phys ? defender.getDef() : defender.getSpcD());
        double Targets = 1; //Will be updated when 2 v 2 battles added
        double Weather = 1; //Will be updated when Weather(Rain/BrightSun/Sand) added
        double Badge = attacker.getBadge(); //Will be updated when Gym Badges are added
        double Critical = attacker.getCrit();
        double random = (Math.random()*0.15)+0.85; //Randomizes attack effect
        double Stab = (attack.getType() == attacker.getType() ? (attacker.getAbility() == Ability.Adaptability ? 2 : 1.5) : 1); //Same Type Attack Bonus
        double Effective = TypeComparator.getY(attack.getType(), defender.getType()); //Type effective modifier, fire -> grass -> water -> fire
        double Burn = (attacker.isBurned() && attacker.getAbility() != Ability.Guts && attack.getBase() == Base.Phys ? 0.5 : 1); //Being burned affects the damage a pokemon takes
        double other = 1; //Other effect will be updated
        return (((0.4 * attacker.getLVL() + 2) * attack.getPow() * A / (D * 50)) + 2) *
                (Targets * Weather * Badge * Critical * random * Stab * Effective * Burn * other);
    }
}
