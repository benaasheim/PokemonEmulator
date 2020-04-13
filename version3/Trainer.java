import java.util.ArrayList;
import java.util.List;

public class Trainer {

    //Instance Variables
    private String name;
    private List<Pokemon> roster;

    //Class Methods
    public Trainer(String name) {
        this.name = name;
        this.roster = new ArrayList<>();
    }

    //Accessors
    public String getName() {
        return name;
    }
    public List<Pokemon> getRoster() {
        return roster;
    }
    public Pokemon getDefaultPokemon() {
        return getPokemon(0);
    }
    public Pokemon getPokemon(int i) {
        return roster.get(i);
    }

    //Mutators
    public void setName(String nam) {
        name = nam;
    }
    public void addPokemon(Pokemon pokemon) {
        roster.add(pokemon);
    }
    public void switchSpots(Pokemon pokemon1, Pokemon pokemon2) {
        int ind1 = roster.indexOf(pokemon1);
        int ind2 = roster.indexOf(pokemon2);
        roster.remove(ind2);
        roster.add(ind2, pokemon1);
        roster.remove(ind1);
        roster.add(ind1, pokemon2);
    }

}
