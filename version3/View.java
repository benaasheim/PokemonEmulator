import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;


public class View {

    private PApplet screen;
    private World world;
    private Battle battle;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;
    private List<Menu> menus;

    public View(int numRows, int numCols, PApplet screen, World world, Battle battle, int tileWidth, int tileHeight, List<Menu> menus) {
        this.screen = screen;
        this.world = world;
        this.battle = battle;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
        this.menus = menus;
    }

    //Mutators
    public void setModel(World world) {
        this.world = world;
    }
    public void setBattleModel(Battle battleModel) {
        this.battle = battleModel;
    }

    //viewport ops
    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(viewport.getCol() + colDelta, 0,
                world.getNumCols() - viewport.getNumCols());
        int newRow = clamp(viewport.getRow() + rowDelta, 0,
                world.getNumRows() - viewport.getNumRows());
        viewport.shift(newCol, newRow);
    }
    private static int clamp(int value, int low, int high)
    {
        return Math.min(high, Math.max(value, low));
    }

    //drawing
    private void drawBackground() {
        for (int row = 0; row < viewport.getNumRows(); row++)
        {
            for (int col = 0; col < viewport.getNumCols(); col++)
            {
                Point worldPoint = viewport.viewportToWorld(col, row);
                Optional<PImage> image = world.getBackgroundImage(worldPoint);
                if (image.isPresent())
                {
                    screen.image(image.get(), col * tileWidth,
                            row * tileHeight);
                }
            }
        }
    }
    protected void drawEntities() {
        List<Entity> entityList = new ArrayList<>(world.getEntities());
        entityList.sort(Entity::compareTo);
        entityList.stream()
                .filter(entity -> viewport.contains(entity.getPosition()))
                .forEach(entity -> entity.draw(screen, tileWidth, tileHeight, viewport));
    }
    public void drawWorld() {
        drawBackground();
        drawEntities();
        drawGrass();
    }
    public void drawMenus() {
        menus.stream().forEach(menu -> menu.addTo(screen));
    }
    public void drawView() {
        drawWorld();
        if (battle != null) {
            drawBattle();
        }
        drawMenus();
    }
    public void addText(String string, int x, int y) {
        screen.text(string, x, y);
    }
    protected void drawGrass() {
        for (int row = 0; row < viewport.getNumRows(); row++)
        {
            for (int col = 0; col < viewport.getNumCols(); col++)
            {
                Point worldPoint = viewport.viewportToWorld(col, row);

                Optional<PImage> image = world.getBackgroundOverlay(worldPoint);
                if (image.isPresent())
                {
                    screen.image(image.get(), col * tileWidth,
                            row * tileHeight);
                }
            }
        }
    }
    public boolean sss(char d){
        Point pos = world.getPlayer().getPosition();
        Point vpos = viewport.worldToViewport(pos.x, pos.y);
        switch (d) {
            case 'u': return (vpos.y < 8);
            case 'd': return (vpos.y > 7);
            case 'l': return (vpos.x <= (viewport.getNumRows()/2)+2);
            case 'r': return (vpos.x >= (viewport.getNumRows()/2)+4);
            default: return false;
        }
    }
    public void drawBattle() {
        drawBattleEntities();
        drawBattleGrass();
    }
    public void drawBattleEntities() {
        List<Entity> entityList = new ArrayList<>(battle.getEntities());
        entityList.sort(Entity::compareTo);
        entityList.stream()
                .filter(entity -> entity instanceof Obstacle)
                .forEach(entity -> entity.draw(screen, tileWidth, tileHeight));
        entityList.stream()
                .filter(entity -> entity instanceof Quake)
                .forEach(entity -> entity.draw(screen, tileWidth, tileHeight));
    }
    protected void drawBattleGrass(){
        List<String> list = battle.getLoadout();
        addText(list.get(3) + " HP:   " + list.get(4) + "              LVL: " + list.get(5), 7, 75);
        addText(list.get(0) + "    " + list.get(1), 420, 245);
    }
}
