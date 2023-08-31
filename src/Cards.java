package src;

import java.util.ArrayList;


public  class Cards {
    protected ArrayList<Card> cards;
    protected int top = -1;
//    private int openNum = 0;

    public Cards(){
        cards = new ArrayList<>();
    }
    protected Card pop() throws NullPointerException{ // 删除栈顶卡片并返回
        Card card = peek();
        cards.remove(top);
        top--;
        return card;
    }

    protected Card peek() throws NullPointerException{
        Card card = null;
        if (cards.isEmpty()) throw new NullPointerException();
        else card = cards.get(top);
        return card;
    }
    protected void push(Card card){
        cards.add(card);
        top++;
    }


    protected Card deleteAtIndex(int i) throws IndexOutOfBoundsException{
        if (i<0||i>top){throw new IndexOutOfBoundsException();}
        Card card = cards.get(i);
        cards.remove(i);
        top--;
        return card;
    }
    protected int getSize(){
        return cards.size();
    }

    protected Card get(int index) throws IndexOutOfBoundsException{
        if (index>=0&&index<=top){
            return cards.get(index);
        }else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        print();
        return null;
    }

    public void print(){
        if (!cards.isEmpty()){
            for (Card card : cards) {
                int number = card.getNumber();
                String suitString = card.getSuitString();
                int state = card.getState();
                if (state == Pocker.STATE_CLOSE){
                    System.out.print(Pocker.numberString[0]+" ");
                }else{
                    System.out.print(suitString + Pocker.numberString[number] + " ");
                }
            }
            System.out.println();
        }else {
            System.out.println("");
        }
    }
}
