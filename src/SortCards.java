package src;

import java.util.ArrayList;

public class SortCards extends Cards implements MoveAble {
    private int suit;

    @Override
    public void delete() {
        pop();
    }

    @Override
    public boolean add(Card card) {
        int number = card.getNumber();
        int suit = card.getSuit();
        if (top < 0){
            if ( number == 1){
                push(card);
                this.suit = suit;
                return true;
            }
        }else if(top < 13){
            if ( suit == this.suit && number == top+2 ){
                push(card);
                return true;
            }
        }
        return false;
    }

    public SortCards copy(){
        SortCards sortCards = new SortCards();
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            Card t = cards.get(i).copy();
            list.add(t);
        }
        sortCards.cards =  list;
        sortCards.top = top;
        sortCards.suit = suit;
        return sortCards;
    }
}
