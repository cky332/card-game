package src;

import src.Cards;

import java.util.ArrayList;

public class ExtraCards extends Cards implements MoveAble{
    private int bounds = 3;

    public int getShowNum() {
        return showNum;
    }

    private int showNum = 0;

    public int getIndex() {
        return index;
    }

    private int index = -1;



    public void open(){
        index++;
        if (index <= top){
            if (showNum<bounds){
                if (showNum<=top+1){
                    showNum++;
                    Card card = cards.get(index);
                    card.setState(Pocker.STATE_OPEN);
                }
            }else{
                Card card = cards.get(index);
                card.setState(Pocker.STATE_OPEN);
                Card card1 = cards.get(index - 3);
                card1.setState(Pocker.STATE_CLOSE);
            }
        }else if (top !=-1){
            for (int i = top;i>top-showNum;i--){
                Card card = cards.get(i);
                card.setState(Pocker.STATE_CLOSE);
            }
            index = -1;
            showNum = 0;
        }

    }

    @Override
    protected Card peek() {
        if (index>=0&&index<=top){
            return cards.get(index);
        }else {
            throw new NullPointerException();
        }
    }

    @Override
    public void delete() {
        cards.remove(index);
        index--;
        showNum--;
        top--;
    }

    @Override
    public boolean add(Card card) {
        index++;
        showNum++;
        cards.add(index,card);
        top++;
        return false;
    }

    public ExtraCards copy(){
        ExtraCards extraCards = new ExtraCards();
        extraCards.top = top;
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            Card t = cards.get(i).copy();
            list.add(t);
        }
        extraCards.cards = list;
        extraCards.showNum = showNum;
        extraCards.index = index;
        return extraCards;
    }

}
