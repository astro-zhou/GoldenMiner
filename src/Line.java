import java.awt.*;

public class Line {
    //起点坐标
    int x=380;
    int y=180;
    //终点坐标
    int endx=500;
    int endy=500;
    //线长
    double length = 80;
    //线长最小值
    double MIN_length = 80;
    //线长最大值
    double MAX_length = 750;
    //角度百分比
    double n = 0;
    //方向
    int dir = 1;
    //状态 0 摇摆 1 向下伸长 2 没抓到物体收回 3 抓取到物体收回
    int state=0;
    //钩爪图片
    Image hook = Toolkit.getDefaultToolkit().getImage("imgs/hook.png");

    GameView frame;

    Line(GameView frame){this.frame=frame;}

    //碰撞检测,检测物体是否被抓取,若物体被抓取,则state=3，且修改flag该物体变为可移动
    void logic(){
        //判断线的终点坐标（endx,endy）是否落在金块/石头表示的矩形之内
        for(Object obj:this.frame.objectList){
            if(endx >= obj.x && endx <= obj.x + obj.width && endy >= obj.y && endy <= obj.y + obj.width){
                //改变物体使其能移动
                obj.flag = true;
                //改变线的状态
                state = 3;

            }
        }
    }

    //绘制绳子的各种情况 012/3
    void paintSelf(Graphics g){
        logic();
        switch (state){
            case 0://摇摆
                if(n < 0.2){
                    dir = 1;
                }else if(n > 0.8){
                    dir = -1;
                }
                n = n + 0.005 * dir;
                endx = (int) (x + length * Math.cos(n * Math.PI));
                endy = (int) (y + length * Math.sin(n * Math.PI));
                g.drawLine(x,y,endx,endy);
                g.drawImage(hook,endx-36,endy,null);
                g.setColor(Color.red);

                break;
            case 1://向下伸长
                if(length < 750){
                    length = length + 10;
                    endx = (int) (x + length * Math.cos(n * Math.PI));
                    endy = (int) (y + length * Math.sin(n * Math.PI));
                    g.drawLine(x,y,endx,endy);
                    g.drawImage(hook,endx-36,endy,null);
                    g.setColor(Color.red);
                }else{
                    state = 2;
                }
                break;
            case 2://没抓到物体收回
                if(length > 100){
                    length = length - 10;
                    endx = (int) (x + length * Math.cos(n * Math.PI));
                    endy = (int) (y + length * Math.sin(n * Math.PI));
                    g.drawLine(x,y,endx,endy);
                    g.drawImage(hook,endx-36,endy,null);
                    g.setColor(Color.red);
                }else {
                    state = 0;
                }
                break;
            case 3://抓取到物体收回,线长达到MIN_length之后物体自动消失,
                // 若使用了药水，药水数量减一，抓取金子速度变快，抓石头则石头消失（模拟爆破）
                // 此时logic里面已经判断了state=3，且flag=true
            //抓取到物体收回,线长达到MIN_length之后物体自动消失,
                // 若使用了药水，药水数量减一，抓取金子速度变快，抓石头则石头消失（模拟爆破）
                // 此时logic里面已经判断了state=3，且flag=true
                int m = 1;
                if(length==MIN_length){
                    state=0;
                }
                else if (length > MIN_length){
                    length = length - 10;
                    endx = (int) (x + length * Math.cos(n * Math.PI));
                    endy = (int) (y + length * Math.sin(n * Math.PI));
                    g.drawLine(x,y,endx,endy);
                    g.drawImage(hook,endx-36,endy,null);
                    g.setColor(Color.red);
                    //处理偏移量
                    for (Object obj: this.frame.objectList){
                        if (obj.flag == true){
                            m = obj.m;
                            //减去偏移量，金块长度1/2
                            obj.x = endx - obj.getWidth()/2;
                            obj.y = endy;
                            if (length <= MIN_length){
                                //抓取到物体收回,线长达到MIN_length之后物体自动消失
                                obj.x = -200;
                                obj.y = -200;
                                obj.flag = false;
                                Bg.waterFlag=false;
                                //总分 == 现在的分数 + 抓到的物体分数
                                Bg.count = Bg.count+obj.count;
                                state = 0;
                            }
                            if (Bg.waterFlag){
                                if(obj.type == 1){
                                    m =1;
                                }
                                if(obj.type == 2){
                                    obj.x = -200;
                                    obj.y = -200;
                                    obj.flag = false;
                                    Bg.waterFlag=false;
                                    state=2;
                                }
                            }

                        }
                    }
                }
                try {
                    Thread.sleep(m);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            default:
        }
    }
    //重置线
    void reGame(){
        //将线长与角度置为初始值
        n = 0;
        length = 100;
    }
}
