package src;

import sun.print.PeekGraphics;

import java.util.ArrayList;
import java.util.Scanner;

public class Desktop {
    private PockerCards pockerCards ;

    public DeckCards[] getDeckCards() {
        return deckCards;
    }

    public ExtraCards getExtraCards() {
        return extraCards;
    }

    public SortCards[] getSortCards() {
        return sortCards;
    }


    public void setDeckCards(DeckCards[] deckCards) {
        this.deckCards = deckCards;
    }

    private DeckCards[] deckCards;

    public void setExtraCards(ExtraCards extraCards) {
        this.extraCards = extraCards;
    }

    private ExtraCards extraCards;

    public void setSortCards(SortCards[] sortCards) {
        this.sortCards = sortCards;
    }

    private SortCards[] sortCards;
    private int step = 0;
    public Desktop(){
        initCards();
        start();
    }

    private void initCards() {
        pockerCards = new PockerCards();
        deckCards = new DeckCards[7];
        extraCards = new ExtraCards();
        sortCards = new SortCards[4];
        step = 0;
    }

    public int getStep(){
        return step;
    }

    protected void start() {
        initCards();
        pockerCards.shuffle();
        for (int i = 0; i < deckCards.length; i++) {
            deckCards[i] = new DeckCards();
            for (int j = 0;j<i+1;j++){
                try {
                    Card extract = pockerCards.extract();
                    deckCards[i].push(extract);
                } catch (Exception e) {
                    break;
                }
            }
        }
        ArrayList<Card> cards = pockerCards.cards;
        for (Card card : cards) {
            extraCards.push(card);
        }

        for (int i = 0; i < sortCards.length; i++) {
            SortCards sortCard = new SortCards();
            sortCards[i] = sortCard;
        }
    }

    @Override
    public String toString() {
        for (SortCards sortCard : sortCards) {
            sortCard.toString();
            System.out.println();
        }
        for (DeckCards deckCard : deckCards) {
            deckCard.toString();
            System.out.println("");
        }
        return null;
    }

    public Desktop copy(){
        Desktop desktop = new Desktop();
        DeckCards[] deckCards = new DeckCards[7];
        for (int i = 0; i < deckCards.length; i++) {
            deckCards[i] = this.deckCards[i].copy();
        }
        SortCards[] sortCards = new SortCards[4];
        for (int i = 0; i < sortCards.length; i++) {
            sortCards[i] = this.sortCards[i].copy();
        }
        ExtraCards extraCards = this.extraCards.copy();
        desktop.sortCards = sortCards;
        desktop.deckCards = deckCards;
        desktop.extraCards = extraCards;
        return desktop;
    }
}
