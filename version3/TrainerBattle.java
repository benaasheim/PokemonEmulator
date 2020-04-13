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
    protected Trainer getFoeTrainer() { return null; }
    protected Pokemon getFoePokemonStart() {
        return foe.getDefaultPokemon();
    }
    protected Pokemon getFoeNext() {
        return null;
    }


    @Override
    protected String getFoeAttack() {
        for (Attack attack : foe.getDefaultPokemon().getAttacks()) {
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
        String string = "Wild " + pokemon.getName() + " appeared!";
        return string;
    }

    protected String goSoNSo(Trainer trainer, Pokemon pokemon) {
        String string = "Go " + pokemon.getName() + "!";
        return string;
    }

    @Override
    protected String foeFaintedMessage(Trainer trainer, Pokemon pokemon) {
        String string = "Wild " + pokemon.getName() + " fainted!";
        return string;
    }

}
