package src;

import javax.xml.crypto.KeySelector;

public class Pocker {
    public static final int SPADE = 10; //黑桃
    public static final int HEART = 11; //红心
    public static final int CLUB = 12; //草花
    public static final int DIAMOND = 13; //方片
    public static final int[] suits = {SPADE,HEART,CLUB,DIAMOND};
    public static final int STATE_OPEN = 1;
    public static final int STATE_CLOSE = 2;
    public static final String[] suitsStrings = {"♠","♡","♣","♢"};
    public static final String[] numberString = {"*","A","2","3","4","5","6","7","8","9","10","J","Q","K"};
}
