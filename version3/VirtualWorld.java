import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld extends PApplet {

    public static String lastWorld;
    public static String lastHealWorld;
    public static Point lastHealLoc;
    public static Point lastLoc;

    private MenuStore menuStore = new MenuStore();
    private WorldStore worldStore = new WorldStore();
    public static GameState gameState = new GameState();

    private static final int TIMER_ACTION_PERIOD = 10;
    private long next_time;

    public static final int VIEW_WIDTH = 672;
    public static final int VIEW_HEIGHT = 448;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static String IMAGE_LIST_FILE_NAME = ".idea/imagelist";

    private ImageStore imageStore;
    private World world;
    public static String nextWorld;
    private Battle battle;
    public static Pokemon pokemonToBattle;
    public static Trainer trainerToBattle;
    private List<Menu> menus = new ArrayList<>();
    public static String nextMenu;
    private View view;
    private EventScheduler scheduler;
    private EventScheduler battleScheduler;
    private Controller controller;
    private Options options = new Options();
    private boolean hasLoaded = false;

    public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }
    public void setup() {
        imageStore = new ImageStore(TILE_WIDTH, TILE_HEIGHT);
        imageStore.loadImages(IMAGE_LIST_FILE_NAME, this);
        world = new World(WORLD_ROWS, WORLD_COLS, imageStore.createDefaultBackground());
        battle = null;
        battleScheduler = new EventScheduler();
        scheduler = new EventScheduler();
        view = new View(VIEW_ROWS, VIEW_COLS, this, world, battle, TILE_WIDTH, TILE_HEIGHT, menus);
        next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
        Loadable.tryload(menuStore, MenuStore.FileName, null);
        Loadable.tryload(worldStore, WorldStore.FileName, null);
        Loadable.tryload(gameState, GameState.FileName, null);
        Loadable.tryload(options, Options.FileName, null);
        SwitchtoWhich();
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
        controller = new MenuController(menus.get(menus.size()-1));
    }
    public void switchToMenu(String string) {
        menus.add(menuStore.getMenu(string, imageStore));
        controller = new MenuController(menus.get(menus.size()-1));
    }

    public void SwitchtoWhich() {
        String startupmenu = "menu_with_load";
        if (gameState.getIsNew()) {
            startupmenu = "menu";
        }
        switchToMenu(startupmenu);
    }
    public void exitMenu() {
        if (menus.size() > 1 ) {
            menus.remove(menus.size()-1);
            controller = new MenuController(menus.get(menus.size()-1));
        }
        else if (hasLoaded){
            menus.remove(menus.size()-1);
            scheduler.pause();
            world.getPlayer().enableInput();
            controller = new WorldController(world, scheduler, view);
        }
    }

    public void switchToWorld(String string) {
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&");
        hasLoaded = true;
        menus.clear();
        world = worldStore.getWorld(string, imageStore);
        view.setModel(world);
        world.scheduleActions(scheduler);
        VirtualWorld.lastWorld = string;
        world.getPlayer().enableInput();
        controller = new WorldController(world, scheduler, view);
    }

    public static void setPokemon(Pokemon pokemon) {
        pokemonToBattle = pokemon;
    }

    public static void setTrainer(Trainer trainer) {
        trainerToBattle = trainer;
    }

    public void switchToBattle() {
        if (trainerToBattle != null) {
            battle = new TrainerBattle(gameState.getTrainer(), trainerToBattle, WORLD_ROWS, WORLD_COLS, imageStore.createDefaultBackground(), imageStore);
        }
        else if (pokemonToBattle != null) {
            battle = new PokemonBattle(gameState.getTrainer(), pokemonToBattle, WORLD_ROWS, WORLD_COLS, imageStore.createDefaultBackground(), imageStore);
        }
        else {
            throw new Error();
        }
        addMenu(battle.setup());
        view.setBattleModel(battle);
    }
    public void leaveBattle() {
        scheduler.pause();
        battle = null;
        view.setBattleModel(battle);
    }
    public void ReturnAWinner() {
        menus.clear();
        leaveBattle();
        switchToWorld(VirtualWorld.lastWorld);
    }
    public void ReturnALoser() {
        menus.clear();
        leaveBattle();
        VirtualWorld.lastLoc = lastHealLoc;
        switchToWorld(VirtualWorld.lastHealWorld);
    }

    //Button Out-Functions
    public void executeButtonPush(String functionkey){
        switch (functionkey) {
            case "to_menu": SwitchtoWhich();break;
            case "boy": EnterNewGame();break;
            case "load_game": switchToWorld(VirtualWorld.lastWorld);break;
            case "AnnouncePlayer": menus.clear(); addMenu(battle.AnnouncePlayer());break;
            case "ChooseMoveStage": menus.clear(); addMenu(battle.chooseMoveStage()); break;
            case "ChooseAttackStage": addMenu(battle.chooseAttackStage()); break;
            case "ReturnAWinner": ReturnAWinner(); break;
            case "AttackAnimation": addMenu(battle.AtAni(battleScheduler)); break;
            case "Attacks": addMenu(battle.Atti()); break;
            case "AnnounceAttack": addMenu(battle.AnAt());break;
            case "FoeFainted": addMenu(battle.FoePokeFainted());break;
            case "PlayerPokeFainted": menus.clear(); addMenu(battle.PlayerPokeFainted());break;
            case "NoMorePokemon": menus.clear(); addMenu(battle.noMoPoke());break;
            case "PlayerFainted": menus.clear(); addMenu(battle.PlayerFainted());break;
            case "ReturnALoser": ReturnALoser();break;
            case "start": addMenu(gameState.getStartMenu());break;
            case "exit": exitMenu();break;
            case "save": SaveGame();break;
            default:
                if (functionkey.length() >= 10 && functionkey.substring(0, 9).equals("Switch_to")) {
                    menus.clear();
                    switchToMenu(functionkey.substring(10));
                }
                else if (functionkey.length() >= 10 && functionkey.substring(0, 9).equals("add_menu_")) {
                    switchToMenu(functionkey.substring(9));
                }
                else if (functionkey.length() >= 2 && functionkey.substring(0, 2).equals("b3")) {
                    addMenu(battle.b3(functionkey.substring(2)));
                }
                else {
                    System.out.println("SUMPN WRONG HERE, invalid buttonkey: " + functionkey);
                }
                break;
        }
    }

    public void EnterNewGame() {
        gameState = new GameState();
        lastHealWorld = "";
        lastHealLoc = null;
        lastLoc = null;
        lastWorld = "";
        switchToWorld("world2");
    }

    public void draw() {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         scheduler.updateOnTime(time);
         battleScheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
         switchCheck();
      }
      view.drawView();
    }
    public void switchCheck() {
        if (trainerToBattle != null) {
            switchToBattle();
            trainerToBattle = null;
        }
        if (pokemonToBattle != null) {
            switchToBattle();
            pokemonToBattle = null;
        }
        if (nextWorld != null) {
            switchToWorld(nextWorld);
            nextWorld = null;
        }
        if (nextMenu != null) {
            scheduler.pause();
            switchToMenu(nextMenu);
            nextMenu = null;
        }
    }

    public void SaveGame() {
        VirtualWorld.lastLoc = world.getPlayer().getPosition();
        VirtualWorld.gameState.SaveGame();
    }

    public void keyPressed() {
        if (key != CODED)
        {
            System.out.println(keyCode);
            executeButtonPush(controller.executeCommandSpec(options.codeToCommand(keyCode)));
        }
    }

    public static void main(String [] args) {
        PApplet.main(VirtualWorld.class);
    }
}
