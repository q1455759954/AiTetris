import analyse.*;
import entity.Blocks;
import entity.RecordBlocks;
import entity.sendData.Observer;
import entity.sendData.Subject;
import entity.shape.BlocksDirector;
import getLocation.GetAllLocation;
import util.BlocksUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/*
    观察者
 */
public class GameWindow extends JFrame implements Observer{

    public int WIDTH =20;
    public int HEIGHT =20;
    private int SCORE = 0;
    public List<Blocks> nowBlocks = new ArrayList<Blocks>();//当前方块
    private List<Blocks> curBlocks = new ArrayList<Blocks>();//当前固定的方块
    private  Map<List<Blocks>,Integer> falseNumResult = null;
    private Subject subject;

    private boolean[][] map = new boolean[14][25];//横向14个方块，纵向25个方块

    //整体是一个外观模式
    private BlocksDirector blocksDirector;//随机得到一个方块：生成器模式，享元模式，单例模式
    private GetAllLocation getAllLocation;//得到方块位置：责任链模式，原型模式，单例模式
    private Strategy doZeroAnalyse;//分析方块位置：适配器模式，策略模式
    private Strategy doFirstAnalyse;
    private Strategy doSecondAnalyse;
    private Strategy doThirdAnalyse;
    private ResultData adapter;

    public GameWindow(Subject subject) {
        super("俄罗斯方块");
        this.subject = subject;
        subject.addObserver(this);
        init();
        getNowBlock();
    }

    //随机得到一个图形作为当前方块
    private void getNowBlock() {
        try {
            blocksDirector = new BlocksDirector();
            nowBlocks = blocksDirector.getBlocks();//得到当前方块
            getAllLocation = new GetAllLocation();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    RecordBlocks recordBlocks = RecordBlocks.getMap();
//                    BlocksUtil blocksUtil = new BlocksUtil();
//                    while (true) {
//                        while (recordBlocks.nowBlocks == null) {
//                            System.out.println("mmmmmmmmmmmm");
//
//                        }
//                        System.out.println("nnnnnnnnnnnnnn");
//                        try {
//                            nowBlocks = blocksUtil.copyBloks(recordBlocks.nowBlocks);
//                        } catch (CloneNotSupportedException e) {
//                            e.printStackTrace();
//                        }
//                        recordBlocks.nowBlocks = null;
//                        repaint();
//                    }
//                }
//            }).start();
            getAllLocation.getResult(nowBlocks,map);
            RecordBlocks recordBlocks = RecordBlocks.getMap();
            recordBlocks.notifyObservers();//观察者模式将结果返回到falseNumResult
            doZeroAnalyse = new DoZeroAnalyse();
            doFirstAnalyse = new DoFirstAnalyse();
            doSecondAnalyse = new DoSecondAnalyse();
            doThirdAnalyse = new DoThirdAnalyse();
            adapter = new Adapter();

            System.out.println(falseNumResult.size());
            Map<List<List<Blocks>>, Point> zeroResult = doZeroAnalyse.doAnalyse(adapter.giveResult(falseNumResult,1),map);
            Map<List<List<Blocks>>, Point> firstResult = doFirstAnalyse.doAnalyse(zeroResult,map);
            Map<List<List<Blocks>>, Point> secondResult = doSecondAnalyse.doAnalyse(firstResult,map);
            Map<List<List<Blocks>>, Point> thirdResult = doThirdAnalyse.doAnalyse(secondResult,map);
            List<Blocks> finallyResult = adapter.giveResult(thirdResult,"1".getBytes()[0]);
            curBlocks = finallyResult;
            repaint();//刷新界面
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fixBlocks(curBlocks);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /*
        观察者模式
     */
    @Override
    public void receiveData(Map<List<Blocks>, Integer> result) {
        this.falseNumResult = result;
    }

    private void drawBlocks(Graphics g) {

        //画墙
        for(int i=0;i<16;i++){
            g.setColor(Color.gray);
            g.fill3DRect(WIDTH*i,500,WIDTH,HEIGHT,true);
        }
        for(int i=0;i<26;i++){
            g.setColor(Color.gray);
            g.fill3DRect(0,HEIGHT*i,WIDTH,HEIGHT,true);
            g.fill3DRect(300,HEIGHT*i,WIDTH,HEIGHT,true);
        }


        //画固定好的方块
        for(int i=0;i<14;i++){
            for (int j=0;j<25;j++){
                if (map[i][j]){
                    g.setColor(Color.green);
                    g.fill3DRect(i*WIDTH+WIDTH,HEIGHT*j,WIDTH,HEIGHT,true);
                }
            }
        }

        //画当前要固定的方块
        for (Blocks blocks:curBlocks){
            if (blocks.state){
                g.setColor(Color.pink);
                g.fill3DRect(blocks.X,blocks.Y,WIDTH,HEIGHT,true);
            }
        }

        //画当前方块
        for (Blocks blocks:nowBlocks){
            if (blocks.state){
                g.setColor(Color.pink);
                g.fill3DRect(blocks.X,blocks.Y,WIDTH,HEIGHT,true);
            }
        }

        g.setFont(new Font("黑体", Font.PLAIN, 20));
        g.drawString("游戏分数："+this.SCORE,350,250);

    }

    /*
        固定方块
     */
    private void fixBlocks(List<Blocks> fixBlocks) {
        for (Blocks blocks: fixBlocks){
            if (blocks.state){
                int i = (blocks.X/WIDTH)-1;
                int j = (blocks.Y/HEIGHT);
                map[i][j]=true;
            }
        }
        nowBlocks.clear();//清空当前方块
        int clearNum = judgeLines(map);
        while (clearNum!=-1){
            clearLines(clearNum);
            clearNum = judgeLines(map);
        }
        getNowBlock();//得到当前方块
    }

    /*
        判断能不能消去行
     */
    private int judgeLines(boolean[][] judgeMap) {
        int j;
        for (int i=0;i<25;i++){
            for (j=0;j<14;j++){
                if (!judgeMap[j][i])
                    break;
            }
            if (j==14){
                return i;
            }
        }
        return -1;
    }

    /*
        消去行
     */
    private void clearLines(int x) {
        for (int i=x;i>0;i--){
            for (int j=0;j<14;j++){
                map[j][i]=map[j][i-1];
            }
        }
        this.SCORE+=10;
    }
    /*
        初始化
     */
    private void init() {
        setVisible(true);
        setResizable(false);//放大缩小
        setSize(500,600);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);

        JPanel jPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBlocks(g);
            }
        };
        add(jPanel);
    }

}
