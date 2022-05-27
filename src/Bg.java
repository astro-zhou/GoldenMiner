import java.awt.*;

public class Bg {

    //关卡数
    static int level = 1;
    //目标得分
    int goal = level*8;
    //总分
    static int count=0;
    //药水数量
    static int waterNum = 3;
    //药水状态,默认F,T 正在使用 F 无法使用
    static boolean waterFlag = false;
    //开始时间
    long startTime;
    //结束时间
    long endTime;
    //药水价格
   public static int price = (int) (Math.random()*10);
    //是否进入商店 f 不购买
    boolean shop = false;

    //载入图片
    Image bg = Toolkit.getDefaultToolkit().getImage("imgs/bg.jpg");
    Image bg1 = Toolkit.getDefaultToolkit().getImage("imgs/bg1.jpg");
    Image peo = Toolkit.getDefaultToolkit().getImage("imgs/peo.png");
    Image water = Toolkit.getDefaultToolkit().getImage("imgs/water.png");


    //界面显示文字的方法
    public static void drawWord(Graphics g,int size,Color color,String str,int x,int y){
        g.setColor(color);
        g.setFont(new Font("仿宋",Font.BOLD,size));
        g.drawString(str,x,y);
    }

    //绘制
    //0 bg和bg1一直存在，开始界面有“按下鼠标右键进入游戏”
    //1 游戏界面加载药水图片和矿工显示“倒计时”“获得分数”“目标分数”“药水数量”“当前关卡数”,倒计时不仅要显示文字还要实时的倒计时
    //2 商店界面显示“价格”和“是否购买”“鼠标左键购买，鼠标右键不购买”
    //3 成功界面显示“成功”“积分”
    //4 失败界面显示“失败”“积分”
    //
    void paintSelf(Graphics g){
        g.drawImage(bg1,0,0,null);
        g.drawImage(bg,0,200,null);
        switch (GameView.state){
            case 0:
                g.setColor(Color.BLACK);
                g.setFont(new Font("仿宋",Font.BOLD,30));
                g.drawString("游戏开始",328,450);
                g.drawString("点击鼠标右键进入游戏",235,550);

                break;
            case 1:
                g.drawImage(peo,310,50,null);
                g.drawImage(water,510,40,null);

                g.setColor(Color.BLACK);
                g.setFont(new Font("仿宋",Font.BOLD,20));
                g.drawString("目标分数："+goal,30,95);
                g.drawString("获得分数："+count,30,150);
                g.drawString("* "+waterNum,575,75);
                endTime = System.currentTimeMillis();
                long tim = 10-(endTime-startTime)/1000;
                drawWord(g,30,Color.black,"时间:"+(tim>0?tim:0),520,150);

                g.setFont(new Font("仿宋",Font.BOLD,15));
                g.drawString("第"+level+"关",30,55);
                break;
            case 2:

                g.drawImage(water,300,400,null);
                drawWord(g,30,Color.black,"价格:"+price,300,500);
                drawWord(g,30,Color.black,"是否购买?",300,550);
                drawWord(g,30,Color.black,"按下鼠标左键进行购买",220,600);
                drawWord(g,30,Color.black,"没钱，按下右键直接进入下一关",220,650);
                if(shop){
                    count = count-price;
                    waterNum++;
                    shop=false;
                    GameView.state=1;
                    startTime=System.currentTimeMillis();
                }
                break;
            case 3:
                drawWord(g,80,Color.cyan,"失败",250,350);
                drawWord(g,80,Color.cyan,"积分:"+count,200,450);
                break;
            case 4:
                drawWord(g,80,Color.red,"成功",250,350);
                drawWord(g,80,Color.red,"积分:"+count,200,450);
                break;
            default:
        }


    }
    //20秒倒计时，倒计时完成返回true 正在倒计时返回false
    boolean gameTime(){
        long tim = (endTime-startTime)/1000;
        if(tim>10){return true;}
        return false;

    }

    //游戏失败和通过了3关游戏成功之后就要重新游戏，此时需要重置关卡数 目标得分 总分 药水数量 药水状态（true正在使用，false没使用）
    void reGame(){
        //重置Bg类中的所有参数，包括关卡数、目标得分、已得分数、药水数与状态
        level = 1;
        goal = level*8;
        count = 0;
        waterNum = 3;
        waterFlag = false;
    }

}