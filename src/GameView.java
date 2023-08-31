package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class GameView extends JFrame implements MouseMotionListener,MouseListener {
    public static final String TITLE = "纸牌游戏";
    private DesktopUtil desktopUtil;
    private int bWidth,bHeight;
    private DeckCards[] deckCards;
    private ExtraCards extraCards;
    private SortCards[] sortCards;
    private Image imageA ,imageTime,imageRefresh,image_undo;


    public void start(){
        desktopUtil = new DesktopUtil(this);
        initDesktop();
        initFrame();
    }



    private void initDesktop() {
        bWidth = (int) (desktopUtil.getbWidth()*Dimen.times);
        bHeight = (int) (desktopUtil.getbHeight()*Dimen.times);
        deckCards = desktopUtil.getDeckCards();
        extraCards = desktopUtil.getExtraCards();
        sortCards = desktopUtil.getSortCards();
        imageA = new ImageIcon(this.getClass().getResource("res/A.png")).getImage();
        imageTime = new ImageIcon(this.getClass().getResource("res/Time.png")).getImage();
        imageRefresh = new ImageIcon(this.getClass().getResource("res/refresh.png")).getImage();
        image_undo = new ImageIcon(this.getClass().getResource("res/Undo.png")).getImage();
    }

    private boolean isClose = false;
    private void initFrame() {
        setTitle(TITLE);
        setSize(Dimen.WIDTH,Dimen.HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initPanel();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                isClose = true;
                System.exit(0);
            }
        });
        setVisible(true);
    }

    private int currentX = -1,currentY = -1,moveX,moveY;
    JPanel gamePanel,panelInfo;
    private void initPanel() {
         gamePanel = new GamePanel(this);
//         panelInfo = new PanelInfo();
        gamePanel.addMouseMotionListener(this);
        gamePanel.addMouseListener(this);
        this.setContentPane(gamePanel);
    }

    public  void restart(){
        desktopUtil.restart();
        count = -1;
        isOver = false;
        gamePanel.repaint();
//        isRestart = false;

    }
    private boolean isMove = false;
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - Dimen.offX;
        int y = e.getY() - Dimen.offY;
        if (x>0){
                moveX = x - currentX;
                moveY = y - currentY;
        }
        gamePanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        isMove =false;
        if (x > E_EstartX && x < E_EstartX+bWidth&&y<bHeight&&y>0){
            extraCards.open();
            gamePanel.repaint();
        }else if(x>buttonX&&x<buttonX+Dimen.BUTTON_WIDTH){
            if (y>buttonY&&y<buttonY+Dimen.BUTTON_HEIGHT){
                restart();
            }else if(y>undoY&&y<undoY + Dimen.BUTTON_HEIGHT){
                this.undo();
            }
        }
    }

    private void undo(){
        if (desktopUtil.undo()){
            step--;
        }
        gamePanel.repaint();
    }

    private int startX,startY;

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        currentX = x;
        currentY = y;
        moveX = 0;
        moveY = 0;
        if ( y<bHeight ){
            if (x>0&&x<=bWidth*4+3*Dimen.sortOff*3){
                isMove = true;
                int i = x / (bWidth + Dimen.sortOff);
                startX = i*bWidth+i*Dimen.sortOff;
                startY = 0;
                desktopUtil.clicked(Signal.SORTS[i]);
            }
            if (x > E_startX && x<E_startX+bWidth+2*Dimen.E_E){
                int showNum = extraCards.getShowNum();
                int d = x - E_startX;
                if (d>((showNum-1)*Dimen.extraOff)){
                    isMove = true;
                    startX = E_startX + (showNum-1)*Dimen.extraOff;
                    startY = 0;
                    desktopUtil.clicked(Signal.EXTRAL);
                }
            }
        }else{
            int j = x / (bWidth+Dimen.D_D);
            int i;
            int length = y - D_startY;
            if(j<7){
                int size = deckCards[j].getSize();
                int front = (size - 1) * Dimen.deckOff;
                if (length<=front+bHeight){
                    isMove = true;
                    if (length>front){
                        i = size - 1;
                    }else {
                        i = length / Dimen.deckOff;
                    }
                    desktopUtil.setIndex(i);
                    startX = j*Dimen.D_D+j*bWidth;
                    startY = D_startY + i*Dimen.deckOff;
                    desktopUtil.clicked(Signal.DECKS[j]);
                }
            }
        }
        gamePanel.repaint();
    }

    private int count = -1;
    private int step = 0;
    private final int sum = 1;
    private boolean isWin(){
        int count = 0;
        for (SortCards sortCard : sortCards) {
            try {
                Card peek = sortCard.peek();
                int number = peek.getNumber();
                if (number == 13){
                    count++;
                }
            }catch (IndexOutOfBoundsException e){

            }catch (NullPointerException e){

            }
        }
        if (count == 4){return true;}
        return false;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (isMove){
            if (y<bHeight){
                if (x<E_startX){
                    int i = x / (bWidth+Dimen.sortOff);
                    if (!desktopUtil.move(Signal.SORTS[i])){
                        desktopUtil.recover();
                    }else {
                        step++;
                    }
                    if (isWin()){
                        isMove =false;
                        isOver = true;
                        int j  = JOptionPane.showConfirmDialog(null, "恭喜通关成功!", "", JOptionPane.YES_OPTION);
                    }
                }else{
                    desktopUtil.recover();
                }
            }else{
                int i = x / (bWidth + Dimen.D_D);
                if (i>7||!desktopUtil.move(Signal.DECKS[i])){
                    desktopUtil.recover();
                }else{
                    step++;
                }
            }
            isMove = false;
            gamePanel.repaint();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private int  E_startX;
    private int  D_startY;
    private int  E_EstartX;
    private Font font_time;
    private Image button_image;
    private int buttonX,buttonY;
    private Thread time_thread;
    private boolean isOver = false;
    private Font font_step;
    private int undoX,undoY;
    class GamePanel extends JPanel{
        JFrame jFrame;
        GamePanel(JFrame jFrame){
            super();
            this.jFrame = jFrame;
            initDimen();
            initTime();
            initImage();
        }

        private void initImage(){
            button_image = new ImageIcon(getClass().getResource("res/button.png")).getImage();
            int timeHeight = imageTime.getHeight(this);
            buttonX = Dimen.WIDTH - Dimen.BUTTON_WIDTH - Dimen.offY*2;
            buttonY = timeHeight + Dimen.FONT_TIME_SIZE + Dimen.offX + 20;

            undoX = buttonX;
            undoY = buttonY + 250;
        }

        private void initDimen() {
            E_startX = Dimen.sortOff*3+bWidth*4+Dimen.S_E;
            D_startY = bHeight+Dimen.SE_D;
            E_EstartX = E_startX+Dimen.E_E+bWidth+Dimen.extraOff*2;
        }





        private int arc = 23;
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.translate(Dimen.offX,Dimen.offY);

            drawBackground(g);
            drawButton(g);
            drawTime(g);
            //Sort
            drawSort(g);
            //Extra
            drawExtra(g);
            // deck
            drawDeck(g);

            drowMove(g);

            drawStep(g);

            drawUndo(g);
        }

        private void drawUndo(Graphics g) {
            g.drawImage(image_undo,undoX,undoY,Dimen.BUTTON_WIDTH,Dimen.BUTTON_HEIGHT ,this);
        }


        private void drawStep(Graphics g){
            font_step = new Font("Default",Font.BOLD,Dimen.FONT_STEP_SIZE);
            g.setFont(font_step);
            g.drawString("Step: "+step,Dimen.WIDTH - 210,380);
        }
        private String time = "00:00";

        private void drawTime(Graphics g) {
            int width = imageTime.getWidth(this);
            int height = imageTime.getHeight(this);
            g.drawImage(imageTime,Dimen.WIDTH - width - Dimen.TIME_OFFY,Dimen.offX,width,height,this);
            g.setColor(Color.WHITE);
            g.setFont(font_time);
            g.drawString(time,Dimen.WIDTH - width - Dimen.TIME_OFFY,Dimen.offX + height + 20);

        }
        private void drawButton(Graphics g){
            g.drawImage(button_image,buttonX,buttonY,Dimen.BUTTON_WIDTH,Dimen.BUTTON_HEIGHT ,this);
        }
        private String getTime(int count){
            String time = null;
            if (count<60){
                if(count<10){
                    time = "00:0"+count;
                }else{
                    time = "00:"+count;
                }
            }else{
                int minute = count / 60;
                int second = count % 60;
                if (minute <10){
                    if (second <10){
                        time = "0"+minute+":"+"0"+second;
                    }else{
                        time = "0"+minute+":"+second;
                    }
                }else{
                    if (second <10){
                        time = minute+":"+"0"+second;
                    }else{
                        time = minute+":"+second;
                    }
                }
            }
            return time;
        }

        private void initTime() {
            font_time = new Font("Default",Font.BOLD,Dimen.FONT_TIME_SIZE);
//            "tt"
            time_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isClose){
                        if (!isOver){
                            count++;
                            time = getTime(count);
                            try {
                                Thread.sleep(1000);
                                GamePanel.this.repaint();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            time_thread.start();
        }

        private void drawBackground(Graphics g) {
           this.setBackground(new Color(0x17,0x5C,0x46));
        }

        private void drowMove(Graphics g) {
            if (isMove){
                LinkedList<Card> currents = desktopUtil.getCurrents();
                for (int i = 0; i < currents.size(); i++) {
                    Card card = currents.get(i);
                    int[] cardView = desktopUtil.getCardView(card);
                    Image image = desktopUtil.getImage(cardView[4]);
                    g.drawImage(image,startX+moveX,startY+moveY+i*Dimen.deckOff,startX+moveX+bWidth,startY+moveY+bHeight+i*Dimen.deckOff,cardView[0],cardView[1],cardView[2],cardView[3],this);
                }
            }
        }

        private void drawDeck(Graphics g) {
            deckCards = desktopUtil.getDeckCards();
            for(int i=0;i<7;i++){
                int x = Dimen.D_D * i + bWidth * i;
                g.drawRoundRect(x,D_startY,bWidth,bHeight,arc,arc);
                for (int j = 0; j < GameView.this.deckCards[i].cards.size(); j++) {
                    Card card = deckCards[i].cards.get(j);
                    int[] cardView = desktopUtil.getCardView(card);
                    Image image = desktopUtil.getImage(cardView[4]);
                    g.drawImage(image,x,D_startY+j*Dimen.deckOff,x+bWidth,D_startY+bHeight+j*Dimen.deckOff,cardView[0],cardView[1],cardView[2],cardView[3],this);
                }
            }
        }

        private void drawExtra(Graphics g) {
            g.drawRoundRect(E_startX+Dimen.E_E+bWidth+Dimen.extraOff*2,0,bWidth,bHeight,arc,arc);
            g.drawImage(imageRefresh,E_EstartX,bHeight / 6,bWidth,bWidth,this);

            extraCards = desktopUtil.getExtraCards();
            int index = extraCards.getIndex();
            int showNum = extraCards.getShowNum();
            for (int i = 0,j = showNum - 1;i<showNum;i++,j--){
                Card card = extraCards.cards.get(index-j);
                int[] cardView = desktopUtil.getCardView(card);
                Image image = desktopUtil.getImage(cardView[4]);
                g.drawImage(image,E_startX+Dimen.E_E*i,0,E_startX+Dimen.E_E*i+bWidth,bHeight,cardView[0],cardView[1],cardView[2],cardView[3],this);
            }
            for (int i = index+1;i<extraCards.cards.size();i++){
                Card card = extraCards.cards.get(i);
                int[] cardView = desktopUtil.getCardView(card);
                Image image = desktopUtil.getImage(cardView[4]);
                g.drawImage(image,E_EstartX,0,E_EstartX+bWidth,bHeight,cardView[0],cardView[1],cardView[2],cardView[3],this);
            }
        }

        private void drawSort(Graphics g) {
            sortCards = desktopUtil.getSortCards();
            for (int i=0;i<4;i++){
                int x = Dimen.sortOff * i + bWidth * i;
                g.drawRoundRect(x,0,bWidth,bHeight,arc,arc);
                g.drawImage(imageA,x+bWidth / 5,0 +bHeight / 4,(int) (bWidth * 0.6),bHeight / 2,this);
                for (Card card : sortCards[i].cards) {
                    int[] cardView = desktopUtil.getCardView(card);
                     Image image = desktopUtil.getImage(cardView[4]);
                    g.drawImage(image,x,0,x+bWidth,bHeight,cardView[0],cardView[1],cardView[2],cardView[3],this);
                }
            }
        }
    }
//    class PanelInfo extends JPanel{
//        public PanelInfo(){
//            FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT,0,0);
//            this.setLayout(flowLayout);
//            Button start = new Button("Start");
//            start.setBackground(new Color(0,0,0));
//            this.add(start);
//            this.setBackground(new Color(225,225,225));
//        }
//    }
}


