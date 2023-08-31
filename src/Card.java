package src;

public class Card {

    private int number; //数值

    private int suit; // 花色

    private int state; // 状态 (翻开,背面两种 )

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public int getNumber() {
        return number;
    }
    public int getSuit() {
        return suit;
    }

    public Card(int suit,int number) throws NullPointerException{  //构造函数
        if(number > 0 && number < 14 ){ //取值有效才行
            this.number = number;
        }else {
            throw new IndexOutOfBoundsException();
        }
        if (suit == Pocker.CLUB || suit == Pocker.DIAMOND || suit == Pocker.HEART || suit == Pocker.SPADE){
            this.suit = suit;
        }else {
            throw new NullPointerException();
        }
        this.state = Pocker.STATE_CLOSE;
    }

    public int getColor(){
        if ( suit == Pocker.SPADE || suit == Pocker.CLUB ){
            return 1;
        }else{
            return 2;
        }
    }

    public String getSuitString(){
        for (int i = 0 ; i<4;i++){
            if (suit == Pocker.suits[i]){
                return Pocker.suitsStrings[i];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        int number = getNumber();
        String suitString = getSuitString();
        int state = getState();
        if (state == Pocker.STATE_CLOSE){
            System.out.print(Pocker.numberString[0]+" ");
        }else{
            System.out.print(suitString + Pocker.numberString[number] + " ");
        }
        return null;
    }

    public Card copy(){
        Card card = new Card(suit,number);
        card.state = state;
        return card;
    }
}
