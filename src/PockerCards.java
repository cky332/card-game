package src;

import src.Cards;

import java.util.ArrayList;
import java.util.Random;

public class PockerCards extends Cards {
    public PockerCards(){
        initCard();
    }

    private void initCard() {
        for (int i = 0; i < 4 ; i++){
            for (int j = 0;j < 13; j++){
                Card card = new Card(Pocker.suits[i],j+1);
                push(card);
            }
        }
    }

    public void shuffle(){
        ArrayList<Card> temp = new ArrayList<>();
        int count = 52;
        try {
            for ( int i=0;i<count;i++){
                Card extractCard = extract();
                temp.add(extractCard);
            }
            cards = temp;
            top = cards.size()-1;
        }catch (NullPointerException e){

        }catch (IndexOutOfBoundsException e){
            System.out.println("不合法");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    protected Card extract(){
        if (top < 0){
            throw new NullPointerException();
        }else{
            Random random = new Random();
            int i = random.nextInt(top+1);
            Card card = deleteAtIndex(i);
            return card;
        }
    }
}
