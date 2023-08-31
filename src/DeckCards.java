package src;

import java.util.ArrayList;

public class DeckCards extends Cards implements MoveAble {

    private int openNum = 0;
    @Override
    public void delete() {
        for (int i = 0 ;i < openNum;i++){
            pop();
        }
        if (top>=0){
            Card card1 = cards.get(top);
            card1.setState(Pocker.STATE_OPEN);
            openNum++;
        }
    }

    @Override
    public boolean add(Card card) {

        try {
            Card topCard = peek();
            if (verify(topCard,card)){
                super.push(card);
                return true;
            }
        }catch (NullPointerException e){
            Card topCard = null;
            if (verify(topCard,card)){
                super.push(card);
                return true;
            }
        }


        return false;
    }


    @Override
    protected void push(Card card) {
        super.push(card);
        card.setState(Pocker.STATE_OPEN);
        openNum++;
        if (top !=0){
            cards.get(top-1).setState(Pocker.STATE_CLOSE);
            openNum--;
        }
    }


    private boolean verify(Card top, Card card) {
        int cardColor = card.getColor();
        int cardNumber = card.getNumber();
        if (top != null){
            int topColor = top.getColor();
            int topNumber = top.getNumber();
            if (topNumber == cardNumber + 1){
                if (topColor != cardColor ){
                    return true;
                }
            }
        }else {
            if (cardNumber == 13){
                return true;
            }
        }

        return false;
    }


    public void show(){
        if (top >=0){
            cards.get(top).setState(Pocker.STATE_OPEN);
        }
    }

    public DeckCards copy(){
        DeckCards deckCards = new DeckCards();
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            Card t = cards.get(i).copy();
            list.add(t);
        }
        deckCards.cards = list;
        deckCards.top = top;
        deckCards.openNum = openNum;
        return deckCards;
    }
}
