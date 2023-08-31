package src;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Stack;

public class DesktopUtil {
    final String[]  paths ={ "res/Spade.png","res/Heart.png" , "res/Club.png" ,"res/Diamond.png"};
    private final int rowOff = 24;
    private final int columnOff = 16;
    private Desktop desktop;
    private Image image;
    private Image[] images;
    private int bWidth,bHeight;
    private DeckCards[] deckCards;
    private ExtraCards extraCards;
    private SortCards[] sortCards;
//    private LinkedList<Integer> cardIdList = new LinkedList<>();
    private Stack<Desktop> store = new Stack<>();

    public int getbWidth() {
        return bWidth;
    }

    public int getbHeight() {
        return bHeight;
    }

    public DeckCards[] getDeckCards() {
        return deckCards;
    }

    public ExtraCards getExtraCards() {
        return extraCards;
    }

    public SortCards[] getSortCards() {
        return sortCards;
    }


    private JFrame jFrame;
    DesktopUtil(JFrame jFrame){
        desktop = new Desktop();
        this.jFrame = jFrame;
        initImage();
        initData();
    }

    private void initData() {
        deckCards = desktop.getDeckCards();
        extraCards = desktop.getExtraCards();
        sortCards = desktop.getSortCards();

    }

    private void initImage() {
        images = new Image[4];
        for (int i = 0;i<4;i++){
            Image image = new ImageIcon(getClass().getResource(paths[i])).getImage();
            images[i] = image;
        }
        image = images[0];
        bWidth = (image.getWidth(jFrame) - rowOff*4 )/ 5 ;
        bHeight = (image.getHeight(jFrame) - columnOff*2 ) / 3;
    }


    private int Ii = 0;
    public Image getImage(int i) {
        return images[i];
    }
    private int r = 1;
    public int[] getCardView(Card card){
        int number = card.getNumber();
        int state = card.getState();
        int suit = card.getSuit();
        if (state == Pocker.STATE_CLOSE){
            int[] t = { rowOff*4+bWidth*4,bHeight*2+columnOff*2,rowOff*4+bWidth*5,bHeight*3+columnOff*2 ,0};
            return t;
        }else{
            int c = suit % 10;
           int row = (number - 1) / 5;
           int column = (number - 1) % 5;
           int[] t = { rowOff*column+bWidth*column,columnOff*row+bHeight*row,rowOff*column+bWidth*column+bWidth, columnOff*row+bHeight*row+bHeight,c };
           return t;
        }
    }

    public LinkedList<Card> getCurrents() {
        return currents;
    }

    private LinkedList<Card> currents = new LinkedList<>();
    private int CardsId = 0;

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
//    private int E_CardId = 10;
    public void clicked(int signal){
        currents.clear();
        storeDesktop();
        if (signal == Signal.EXTRAL){
            CardsId = 4;
            Card peek = extraCards.peek();
            if (peek.getState() == Pocker.STATE_OPEN){
                currents.add(peek);
                extraCards.delete();
            }
        }else if (signal > Signal.EXTRAL){
            int i = signal % 200;
            CardsId = i + 10;
            int top = deckCards[i].top;
            while (index<=top&&top!=-1){
                Card card = deckCards[i].get(index);
                if (card.getState() == Pocker.STATE_OPEN){
                    currents.add(card);
                    deckCards[i].deleteAtIndex(index);
                    top = deckCards[i].top;
                }else{
                    break;
                }
            }
        }else {
            int i = signal % 100;
            CardsId = i;
            Card pop = sortCards[i].pop();
            currents.add(pop);
        }

    }

    private void storeDesktop(){
        Desktop temp = desktop.copy();
        store.add(temp);
    }
    public boolean undo(){
        if (!store.isEmpty()){
            desktop = store.pop();
//            desktop.toString();
            initData();
            return true;
        }
        return false;
    }

    public void recover(){
        if (CardsId  == 4){
            extraCards.add(currents.get(0));
        }else if (CardsId > 4){
            int i = CardsId % 10;
            int count = 1;
            for (Card current : currents) {
                if(count>0){
                    boolean add = deckCards[i].add(current);
                    if (!add){
                        deckCards[i].push(current);
                    }
                    count--;
                }else {
                    deckCards[i].add(current);
                }
            }
        }else {
            sortCards[CardsId].add(currents.get(0));
        }
        store.pop();
    }
    public boolean move(int signal){
        if (CardsId > 4){
            int i = CardsId % 10;
            deckCards[i].show();
        }
        for (int i1 = 0; i1 < Signal.SORTS.length; i1++) {
            if (signal == Signal.SORTS[i1]){
                 return sortCards[i1].add(currents.get(0));
            }
        }
        for (int i = 0; i < Signal.DECKS.length; i++) {
            if (signal == Signal.DECKS[i]&& (CardsId != signal % 200||CardsId == 4)){
                if(deckCards[i].add(currents.get(0))) {
                    for (int i1 = 1; i1 < currents.size(); i1++) {
                        Card card = currents.get(i1);
                        deckCards[i].add(card);
                    }
                    return true;
               }
               break;
            }
        }
        return false;
    }

    public void restart(){
        desktop.start();
        initData();
    }
}
