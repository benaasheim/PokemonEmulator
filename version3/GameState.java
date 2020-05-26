import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameState implements Loadable {
    public static final String FileName = "idea/GameState";
    private static final String POKEMON_KEY = "POKEMON";
    private static final String DEFEATED_KEY = "DEF";
    private static final String BADGE_KEY = "BDG";
    private static final String PROGRESS_KEY = "PRG";
    private static final String NAME_KEY = "NAME";
    private static final String LASTLOC_KEY = "LL";
    private static final String LASTHEALLOC_KEY = "LH";
    private static final String LASTWORLD_KEY = "LW";
    private static final String LASTHEALWORLD_KEY = "HW";

    //Instance Variables
    private List<String> defeated, badges, progress;
    private Trainer trainer;
    private boolean isNew;

    //Class Methods
    public GameState() {
        this.defeated = new ArrayList<>();
        this.badges = new ArrayList<>();
        this.progress = new ArrayList<>();
        this.trainer = new Trainer("Ash");
        this.isNew = true;
    }

    //Accessors
    public boolean haveBeaten(String string) {
        return defeated.contains(string);
    }
    public boolean hasprogress(String string) {
        return progress.contains(string);
    }
    public boolean getIsNew() {
        return isNew;
    }
    public Trainer getTrainer() { return trainer; }

    //Mutators
    public void addDefeated(String name) {
        defeated.add(name);
    }
    public void addBadge(String name) {
        badges.add(name);
    }
    public void addProgress(String name) {
        if (!progress.contains(name))
            progress.add(name);
    }

    public void heal(Point point) {
        VirtualWorld.lastHealLoc = point;
        VirtualWorld.lastHealWorld = VirtualWorld.lastWorld;
        trainer.getRoster().stream().forEach(Pokemon::restoreAllStatPP);
    }

    public boolean processLine(String line, ImageStore imageStore) {
        this.isNew = false;
        String[] properties = line.split("\\s");
        try {
            if (properties.length > 0) {
                switch (properties[Functions.PROPERTY_KEY]) {
                    case NAME_KEY: trainer.setName(properties[1]);return true;
                    case POKEMON_KEY:
                        properties = Arrays.copyOfRange(properties, 1, properties.length);
                        System.out.println(Arrays.toString(properties));
                        Pokemon pokemon = Pokemon.fromString(properties);
                        trainer.addPokemon(pokemon);
                        return true;
                    case DEFEATED_KEY: addDefeated(properties[1]);return true;
                    case BADGE_KEY: addBadge(properties[1]);return true;
                    case PROGRESS_KEY: addProgress(properties[1]);return true;
                    case LASTLOC_KEY: VirtualWorld.lastLoc = Point.fromString(properties[1]);return true;
                    case LASTHEALLOC_KEY: VirtualWorld.lastHealLoc = Point.fromString(properties[1]);return true;
                    case LASTHEALWORLD_KEY: VirtualWorld.lastHealWorld = properties[1];return true;
                    case LASTWORLD_KEY: VirtualWorld.lastWorld = properties[1];return true;
                    default: return false;
                }
            }
            return false;
        }
        catch (Exception e) {
            throw new LineProcessingError(e, line);
        }
    }
    public void SaveGame() {
        List<String> lines = new LinkedList<>();
        badges.stream().forEach(badge -> lines.add(BADGE_KEY + " " + badge));
        progress.stream().forEach(badge -> lines.add(PROGRESS_KEY + " " + badge));
        defeated.stream().forEach(badge -> lines.add(DEFEATED_KEY + " " + badge));
        trainer.getRoster().stream().forEach(badge -> lines.add(POKEMON_KEY + " " + badge));
        lines.add(LASTLOC_KEY + " " + VirtualWorld.lastLoc);
        lines.add(LASTHEALLOC_KEY + " " + VirtualWorld.lastHealLoc);
        lines.add(LASTWORLD_KEY + " " + VirtualWorld.lastWorld);
        lines.add(LASTHEALWORLD_KEY + " " + VirtualWorld.lastHealWorld);
        WritetoNew(lines);
    }

    private static void WritetoNew(List<String> lines)  {
        try {
            Path file = Paths.get(FileName);
            Files.write(file, lines, StandardCharsets.UTF_8);
        }
        catch (java.io.IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    //0, 168, 336, 504, 672
    //340, 394, 448 - 54
    public Menu getStartMenu() {
        Menu menu = new Menu(new Point(475, 0), 672-475, 320, 8, 1, new Cursor(new Point(0, 7)), "exit");
        Button PokedexButton = new Button(new Point(475, 0), 672-475, 40, "Pokédex", new Point(0, 7), "Pokédex");
        Button PokemonButton = new Button(new Point(475, 40), 672-475, 40, "Pokémon", new Point(0, 6), "Pokémon");
        Button BagButton = new Button(new Point(475, 80), 672-475, 40, "Bag", new Point(0, 5), "Bag");
        Button PokenavButton = new Button(new Point(475, 120), 672-475, 40, "Pokénav", new Point(0, 4), "Pokénav");
        Button PlayerButton = new Button(new Point(475, 160), 672-475, 40, trainer.getName(), new Point(0, 3), trainer.getName());
        Button SaveButton = new Button(new Point(475, 200), 672-475, 40, "save", new Point(0, 2), "Save");
        Button OptionButton = new Button(new Point(475, 240), 672-475, 40, "add_menu_other", new Point(0, 1), "Options");
        Button ExitButton = new Button(new Point(475, 280), 672-475, 40, "exit", new Point(0, 0), "exit");
        menu.addButton(PokedexButton);
        menu.addButton(PokemonButton);
        menu.addButton(BagButton);
        menu.addButton(PokenavButton);
        menu.addButton(PlayerButton);
        menu.addButton(SaveButton);
        menu.addButton(OptionButton);
        menu.addButton(ExitButton);
        menu.getButton(menu.getCursor().getPos()).Highlight();
        return menu;
    }
}
