package edu.wctc;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiceGame {
    private	final List<Player> players = new ArrayList<>();
    private	final	List<Die>	dice = new ArrayList<>();
    private	final	int	maxRolls;
    private		Player	currentPlayer;


    public DiceGame(int countPlayers, int countDice, int maxRolls){

        this.maxRolls = maxRolls;
        for (int i = 0; i < countPlayers; i++) {
           Player player = new Player();
           players.add(player);
        }
        for (int i = 0; i < countDice; i++) {
            Die die = new Die(6);
            dice.add(die);
        }

    }

    private boolean allDiceHeld(){
        if(dice.stream().allMatch(Die::isBeingHeld)){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean autoHold(int faceValue){

        List<Integer> heldDieFace = dice.stream()
                .filter(x->x.isBeingHeld())
                .map(x->x.getFaceValue())
                .collect(Collectors.toList());


        Optional<Integer> y = heldDieFace.stream()
                .filter(x -> x.equals(faceValue))
                .findFirst();

        if(y.isPresent()){
            return true;
        }
        else{
            List<Integer> unheldDieFace = dice.stream()
                    .filter(x->!x.isBeingHeld())
                    .map(x->x.getFaceValue())
                    .collect(Collectors.toList());


            Optional<Integer> z = unheldDieFace.stream()
                    .filter(x -> x.equals(faceValue))
                    .findFirst();

            if(z.isPresent()){
                List<Die> zz = dice.stream()

                        .filter(x->!x.isBeingHeld())
                        .filter(x->x.getFaceValue()==faceValue)
                        .collect(Collectors.toList());

                if(!zz.isEmpty()){
                    zz.get(0).holdDie();
                    return true;
                }


            }
            else{
                return false;
            }
        }
        return false;
    }

    public boolean currentPlayerCanRoll(){

        if(currentPlayer.getRollsUsed() < maxRolls && !dice.stream().allMatch(Die::isBeingHeld)){
            return true;}
        else{
            return false;}
    }

    public int getCurrentPlayerNumber(){
        return currentPlayer.getPlayerNumber();
    }

    public int getCurrentPlayerScore(){
        return currentPlayer.getScore();
    }
    public String getDiceResults(){
String diceResults = "";
        for (Die die : dice) {
            diceResults += die.toString() + " ";
        }
        return diceResults;
    }

    public String getFinalWinner(){


        Collections.sort(players,
                Comparator.comparingInt(
                        Player::getScore).reversed());
       return (String) players.get(0).toString();

    }

    public String getGameResults(){
        Collections.sort(players,
                Comparator.comparingInt(
                        Player::getScore).reversed());
        players.get(0).addWin();
        for (int i = 1; i < players.size() ; i++) {
            players.get(i).addLoss();
        }
        String playerResults = "";
        for (Player player : players) {
            playerResults += player.toString() + " ";
        }
        return playerResults;

    }

    private boolean isHoldingDie(int faceValue){
        List<Integer> heldDieFace = dice.stream()
                .filter(x->x.isBeingHeld())
                .map(x->x.getFaceValue())
                .collect(Collectors.toList());


        Optional<Integer> y = heldDieFace.stream()
                .filter(x -> x.equals(faceValue))
                .findFirst();

        if(y.isPresent()){
            return true;
    }

        return false;
    }

    public boolean nextPlayer(){
        //If there are more players in the list after the current player,
        //updates currentPlayer to be the next player and returns true.
            //Otherwise, returns false.

       int playerNumber = currentPlayer.getPlayerNumber();
       int listCount = players.size();

      int counter = listCount - playerNumber;

      if (counter >= 1) {
          currentPlayer = players.get(playerNumber);
         // currentPlayer = players.get(currentPlayer.getPlayerNumber() + 1);
          return true;
      }
        return false;
    }

    public void playerHold(char dieNum){

       Optional<Die> dieObj = dice.stream()
                .filter(x->x.getDieNum() ==dieNum)
               .findFirst();
        // filter, findFirst, isPresent
        dieObj.ifPresent(Die::holdDie);
    }

    public void resetDice(){
        dice.forEach(Die::resetDie);
    }
public void resetPlayers(){
        players.forEach(Player::resetPlayer);
}

public void rollDice(){
        currentPlayer.roll();
        dice.forEach(Die::rollDie);
}

public void scoreCurrentPlayer(){
int sum = 0;
    List<Die> heldDiceList = dice.stream().filter(x->x.isBeingHeld()).collect(Collectors.toList());
    List<Integer> cargoDiceList = dice.stream().map(x->x.getFaceValue()).collect(Collectors.toList());
if (heldDiceList.stream().anyMatch(die -> die.getFaceValue() ==6) && heldDiceList.stream().anyMatch(die -> die.getFaceValue() ==5) && heldDiceList.stream().anyMatch(die -> die.getFaceValue() ==4)){
    sum = cargoDiceList.stream().mapToInt(x->x).sum() - 15;
    currentPlayer.setScore(sum);
}


else{currentPlayer.setScore(sum);}


}
public void startNewGame(){
        currentPlayer = players.get(0);
        resetPlayers();
}

}


