public class TrainerBattle extends Battle {

    private Trainer player;
    private Trainer foe;

    public TrainerBattle(Trainer player, Trainer foe, int numRows, int numCols, Background defaultBackground, ImageStore imageStore) {
        super(numRows, numCols, defaultBackground, imageStore);
        this.player = player;
        this.foe = foe;
    }

    protected Trainer getPlayerTrainer() {
        return player;
    }
    protected Trainer getFoeTrainer() { return foe; }
    protected Pokemon getFoePokemonStart() {
        return foe.getDefaultPokemon();
    }
    protected Pokemon getFoeNext() {
        return foe.getNextPokemon();
    }


    @Override
    protected String getFoeAttack() {
        for (Attack attack : foePokemon.getAttacks()) {
            if (attack.getPp() > 0) {
                return attack.getName();
            }
        }
        return " ";
    }

    @Override
    protected Pokemon getPlayerPokemonStart() {
        return player.getDefaultPokemon();
    }

    protected String openingMessage(Trainer trainer, Pokemon pokemon) {
        String string = trainer.getName() + " sent out " + pokemon.getName() + "!";
        return string;
    }

    protected String goSoNSo(Trainer trainer, Pokemon pokemon) {
        String string = "Go " + pokemon.getName() + "!";
        return string;
    }

    @Override
    protected String foeFaintedMessage(Trainer trainer, Pokemon pokemon) {
        String string = pokemon.getName() + " fainted!";
        return string;
    }

    protected Menu FoePokeFainted() {
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        removeEntity(foePAnchor);
        if (foePokemon != null) {
            Stage = 0;
            return standardAnnouncementMenu("AnnounceFoe", foeNameIfFainted + " fainted!");
        }
        else {
            Stage = 123;
            return standardAnnouncementMenu("ReturnAWinner", foeNameIfFainted + " fainted!");
        }
    }
}
