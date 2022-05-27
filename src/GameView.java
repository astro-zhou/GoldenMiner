
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
/*
刚开始为开始界面，点击鼠标左键进入游戏，
游戏页面设有10秒倒计时，10秒内没达到目标分数进入失败界面，
10秒内获得分数大于目标分数则进入商店界面，鼠标点击买药水或不买药水之后进入下一关，
下一关的目标分数会更高，一共三关，三关都达到目标分数游戏成功，显示成功界面
 */


public class GameView extends JFrame {

    //0开始界面 1 游戏界面 2 商店界面 3 失败界面 4 成功界面
    static int state=0;
    Bg bg = new Bg();
    Line line = new Line(this);
    List<Object> objectList = new ArrayList<>();
     GameView(){
        boolean isPlace = true;
        //布置金块，同时避免堆叠
        for (int i = 0; i < 7; i++) {
            double random = Math.random();
            Object gold;
            if (random < 0.3D) {
                gold = new GoldMini();
            }
            else if (random < 0.7D) {
                gold = new Gold();
            } else {
                gold = new GoldPlus();
            }
            for(Object obj:objectList){
                if(gold.getRec().intersects(obj.getRec())){
                    //如果重叠不能放置，需要重新生成
                    isPlace = false;
                }
            }
            if(isPlace){objectList.add(gold);}
            else{isPlace = true;i--;}

        }


        int i ;
        for(i = 0; i < 5; ++i) {
            Rock rock = new Rock();
            Iterator var4 = this.objectList.iterator();

            while(var4.hasNext()) {
                Object obj = (Object)var4.next();
                if (rock.getRec().intersects(obj.getRec())) {
                    isPlace = false;
                }
            }

            if (isPlace) {
                this.objectList.add(rock);
            } else {
                isPlace = true;
                --i;
            }
        }

    }
    //窗口设置
    void launch(){
        this.setVisible(true);
        this.setSize(768,1000);
        this.setLocationRelativeTo(null);
        this.setTitle("黄金矿工");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (state){
                    case 0:
                        if(e.getButton()==3){
                            state=1;
                            bg.startTime = System.currentTimeMillis();
                        }
                        break;
                    case 1:
                        //左右摇摆,点击左键
                        if(e.getButton()==1&&line.state==0)
                        {line.state=1;}
                        //抓取返回,点击右键
                        if(e.getButton()==3&&line.state==3&&Bg.waterNum>0){
                            Bg.waterFlag=true;
                            Bg.waterNum--;
                        }
                        break;
                    case 2:
                        if(e.getButton()==1){
                            bg.shop=true;
                        }
                        if (e.getButton()==3){
                            state=1;
                            bg.startTime = System.currentTimeMillis();
                        }
                        break;
                    case 3:
                    case 4:
                        if(e.getButton()==1){
                            state=0;
                            bg.reGame();
                            line.reGame();
                        }
                        break;
                    default:
                }

            }
        });

        while (true){
            repaint();
            nextLevel();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //如果倒计时结束分数达到目标分数，进入下一关，没达到的话就进入失败界面
    public void nextLevel(){
        if(bg.gameTime() && state==1){
            if(Bg.count >= bg.goal)
            {
                if(Bg.level==3) {
                    state=4;
                }
                else {
                    state = 2;
                    Bg.level++;
                }
            }else {state=3;}
            dispose();
            GameView gameWin1 = new GameView();
            gameWin1.launch();
        }

    }

    //缓存画布
    Image offScreenImage;
    @Override
    public void paint(Graphics g) {
        offScreenImage = this.createImage(768,1000);
        Graphics gImage = offScreenImage.getGraphics();
        //先绘制背景
        bg.paintSelf(gImage);

        if(state==1){
            for(Object obj:objectList)
            {//绘制物体
                obj.paintSelf(gImage);
            }
            //绘制线条
            line.paintSelf(gImage);
        }
        g.drawImage(offScreenImage,0,0,null);
    }

    public static void main(String[] args) {
        GameView gameView =new GameView();
        gameView.launch();
    }
}