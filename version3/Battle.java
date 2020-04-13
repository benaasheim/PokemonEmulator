import java.util.ArrayList;
import java.util.List;

public abstract class Battle extends World{

    public static final Point BackAnchor = new Point(0, 0);
    public static final Point PLAYPOKEAnchor = new Point(1, 6);
    public static final Point FOEPOKEAnchor = new Point(13, 2);
    public static final Point PLAYATTKAnchor = new Point(1, 7);
    public static final Point FOEATTKAnchor = new Point(12, 2);


    private ImageStore imageStore;
    private String playerAttack;
    private String foeAttack;
    public boolean lastkeypress;
    private Trainer playerTrainer;
    private Trainer foeTrainer;
    private Pokemon playerPokemon;
    private Pokemon foePokemon;
    private Pokemon fasterPokemon;
    private Pokemon slowerPokemon;
    private String fasterAttack;
    private String slowerAttack;
    private Quake fasterQuake;
    private Quake slowerQuake;
    public String a, b, c, d, e, f, g, h;
    private int Stage;
    private Obstacle playerPAnchor;
    private Obstacle foePAnchor;
    private String playerNameIfFainted;
    private String playerLevelIfFainted;
    private String foeNameIfFainted;
    private String foeLevelIfFainted;


    public Battle(int numRows, int numCols, Background defaultBackground, ImageStore imageStore) {
        super(numRows, numCols, defaultBackground);
        this.imageStore = imageStore;
        this.lastkeypress = false;
        this.playerPAnchor = null;
        this.foePAnchor = null;
    }

    public List<String> getLoadout() {
        List<String> list = new ArrayList<>();
        if (playerPokemon == null) {
            list.add(playerNameIfFainted);
            list.add(""+0);
            list.add(playerLevelIfFainted);
        }
        else {
            list.add(playerPokemon.getName());
            list.add(Integer.toString(playerPokemon.getHP()));
            list.add(Integer.toString(playerPokemon.getLVL()));
        }
        if (foePokemon == null) {
            list.add(foeNameIfFainted);
            list.add(""+0);
            list.add(foeLevelIfFainted);
        }
        else {
            list.add(foePokemon.getName());
            list.add(Integer.toString(foePokemon.getHP()));
            list.add(Integer.toString(foePokemon.getLVL()));
        }
        return list;
    }

    public Menu setup() {
        System.out.println("HERE");
        playerTrainer = getPlayerTrainer();
        foeTrainer = getFoeTrainer();
        playerPokemon = getPlayerPokemonStart();
        foePokemon = getFoePokemonStart();

        Obstacle backgroundAnchor = Obstacle.createObstacle("backk", BackAnchor, imageStore.getImageList("battlebackk"));
        playerPAnchor = Obstacle.createObstacle("playeranchor", PLAYPOKEAnchor, imageStore.getImageList(playerPokemon.getName() + "_back"));
        foePAnchor = Obstacle.createObstacle("foeanchor", FOEPOKEAnchor, imageStore.getImageList(foePokemon.getName()+"_front"));
        addEntity(backgroundAnchor);
        addEntity(playerPAnchor);
        addEntity(foePAnchor);

        return AnnounceFoe();
    }

    public void saveNameNLevel() {
        playerNameIfFainted = playerPokemon.getName();
        foeNameIfFainted = foePokemon.getName();
        playerLevelIfFainted = ""+ playerPokemon.getLVL();
        foeLevelIfFainted = ""+ foePokemon.getLVL();
    }

    private Menu standardAnnouncementMenu(String buttonFunc, String buttonLabel) {
        Menu menu = new Menu(new Point(0, 340), 672, 108, 1, 1, new Cursor(), buttonFunc);
        Button button = new Button(new Point(0, 340), 672, 108, buttonFunc, new Point(0, 0), buttonLabel);
        menu.addButton(button);
        menu.getButton(menu.getCursor().getPos()).Highlight();
        return menu;
    }

    public Menu AnnounceFoe() {
        Stage = 0;
        return standardAnnouncementMenu("AnnouncePlayer", openingMessage(foeTrainer, foePokemon));
    }
    public Menu AnnouncePlayer() {
        Stage = 1;
        return standardAnnouncementMenu("ChooseMoveStage", goSoNSo(playerTrainer, playerPokemon));
    }
    //0, 168, 336, 504, 672
    //340, 394, 448 - 54
    public Menu fourPanelMenu(int shiftover, String exit, String buttonFuncA, String buttonLabelA, String buttonFuncB, String buttonLabelB, String buttonFuncC, String buttonLabelC, String buttonFuncD, String buttonLabelD) {
        Menu menu = new Menu(new Point(0, 340), 672, 108, 2, 2, new Cursor(new Point(0, 1)), exit);
        Button fightButton = new Button(new Point(0+shiftover, 340), 168, 54, buttonFuncA, new Point(0, 1), buttonLabelA);
        Button bagButton = new Button(new Point(168+shiftover, 340), 168, 54, buttonFuncB, new Point(1, 1), buttonLabelB);
        Button pokemonButton = new Button(new Point(0+shiftover, 394), 168, 54, buttonFuncC, new Point(0, 0), buttonLabelC);
        Button runButton = new Button(new Point(168+shiftover, 394), 168, 54, buttonFuncD, new Point(1, 0), buttonLabelD);
        menu.addButton(runButton);
        menu.addButton(bagButton);
        menu.addButton(fightButton);
        menu.addButton(pokemonButton);
        menu.getButton(menu.getCursor().getPos()).Highlight();
        return menu;
    }
    public Menu chooseMoveStage() {
        Stage = 2;
        return fourPanelMenu(336, "",
                "ChooseAttackStage", "FIGHT",
                "", "BAG",
                "", "POKéMON",
                "ReturnAWinner", "RUN");
    }
    public Menu chooseAttackStage() {
        List<Attack> list = playerPokemon.getAttacks();
        Stage = 3;
        return fourPanelMenu(0, "exit",
                "b3"+assignAttack(list, 0), assignAttack(list, 0),
                "b3"+assignAttack(list, 1), assignAttack(list, 1),
                "b3"+assignAttack(list, 2), assignAttack(list, 2),
                "b3"+assignAttack(list, 3), assignAttack(list, 3));
    }
    public String assignAttack(List<Attack> list, int i) {
        try {
            if (list.get(i).getPp() > 0) {
                return list.get(i).getName();
            }
            return  " ";
        }
        catch (IndexOutOfBoundsException e) {
            return  " ";
        }
    }
    public Menu Attackk(String string) {
        playerAttack = string;
        foeAttack = getFoeAttack();
        getAttackAnimations(string, foeAttack);
        Stage = 4;
        return AnAt();
    }
    public void getAttackAnimations(String string, String foeAttack) {
        Quake pquake = Attack.quakeFromName(FOEATTKAnchor, imageStore, string);
        Quake fquake = Attack.quakeFromName(PLAYATTKAnchor, imageStore, foeAttack);
        if (playerPokemon.fasterThan(foePokemon)) {
            fasterPokemon = playerPokemon;
            slowerPokemon = foePokemon;
            fasterAttack = playerAttack;
            slowerAttack = foeAttack;
            fasterQuake = pquake;
            slowerQuake = fquake;
        }
        else {
            fasterPokemon = foePokemon;
            slowerPokemon = playerPokemon;
            fasterAttack = foeAttack;
            slowerAttack = playerAttack;
            fasterQuake = fquake;
            slowerQuake = pquake;
        }
    }
    public Menu AnAt() {
        switch (Stage) {
            case 4: return AnnounceAttack(fasterPokemon, fasterAttack);
            case 7: return AnnounceAttack(slowerPokemon, slowerAttack);
            default: throw new Error("Wrong Stage" + "(" + Stage + ")" + " For This Function");
        }
    }
    public Menu AnnounceAttack(Pokemon pokemon, String string) {
        Stage++;
        return standardAnnouncementMenu("AttackAnimation", pokemon.getName() + " uses " + string);
    }
    public Menu AtAni(EventScheduler scheduler) {
        switch (Stage) {
            case 5: return AttackAnimation(fasterQuake, fasterPokemon.getName(), scheduler);
            case 8: return AttackAnimation(slowerQuake, slowerPokemon.getName(), scheduler);
            default: throw new Error("Wrong Stage" + "(" + Stage + ")" + " For This Function");
        }
    }
    public Menu AttackAnimation(Quake quake, String s, EventScheduler scheduler) {
        quake.AtS(scheduler, this);
        Stage++;
        return standardAnnouncementMenu("Attacks", s + " uses " + quake.getId());
    }
    public Menu Atti() {
        saveNameNLevel();
        switch (Stage) {
            case 6: return Attack(fasterPokemon, slowerPokemon, fasterAttack);
            case 9: return Attack(slowerPokemon, fasterPokemon, slowerAttack);
            default: throw new Error("Wrong Stage" + "(" + Stage + ")" + " For This Function");
        }
    }
    public Menu Attack(Pokemon Pokemon1, Pokemon Pokemon2, String string) {
        Stage++;
        String functionkey = "AnnounceAttack";
        Pokemon1.pickAttack(string, Pokemon2);
        if (playerPokemon.getHP() <= 0) {
            Stage = 663;
            functionkey = "PlayerPokeFainted";
        }
        else if (foePokemon.getHP() <= 0) {
            foePokemon = getFoeNext();
            if (foePokemon == null) {
                Stage = 122;
                functionkey = "FoeFainted";
            }
        }
        if (Stage == 10) {
            functionkey = "ChooseMoveStage";
        }
        return standardAnnouncementMenu(functionkey, Pokemon1.getName() + " uses " + string);
    }
    public Menu PlayerPokeFainted() {
        Stage = 664;
        removeEntity(playerPAnchor);
        return standardAnnouncementMenu("NoMorePokemon", playerNameIfFainted + " fainted!");
    }
    public Menu noMoPoke() {
        Stage = 665;
        return standardAnnouncementMenu("PlayerFainted", "No more usable Pokémon!");
    }
    public Menu PlayerFainted() {
        Stage = 666;
        return standardAnnouncementMenu("ReturnALoser", "You Fainted!");
    }
    public Menu FoePokeFainted() {
        removeEntity(foePAnchor);
        Stage = 123;
        return standardAnnouncementMenu("ReturnAWinner", foeNameIfFainted + " fainted!");
    }
    public Menu b3(String string) {
        if (!string.equals(" ") && playerPokemon.stillPP(string)) {
            return Attackk(string);
        }
        else  {
            return chooseMoveStage();
        }
    }
    protected abstract Pokemon getPlayerPokemonStart();
    protected abstract Pokemon getFoePokemonStart();
    protected abstract String openingMessage(Trainer trainer, Pokemon pokemon);
    protected abstract String goSoNSo(Trainer trainer, Pokemon pokemon);
    protected abstract String foeFaintedMessage(Trainer trainer, Pokemon pokemon);
    protected abstract Trainer getPlayerTrainer();
    protected abstract Trainer getFoeTrainer();
    protected abstract Pokemon getFoeNext();
    protected abstract String getFoeAttack();
}
