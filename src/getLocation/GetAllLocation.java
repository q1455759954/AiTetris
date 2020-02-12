package getLocation;

import entity.Blocks;
import entity.RecordBlocks;
import getLocation.DFS.Handler;
import getLocation.DFS.TurnDown;
import getLocation.DFS.TurnLeft;
import getLocation.DFS.TurnRight;
import sun.applet.Main;
import util.BlocksUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetAllLocation {

    private BlocksUtil blocksUtil = new BlocksUtil();
    private List<Point> visitedWay = new ArrayList<Point>();//用来判断方块移动时是否遍历过
    private RecordBlocks record = RecordBlocks.getMap();
    private Handler turnRight,turnLeft,turnDown;



    /*
        当前方块，方块形状map，整个图形的map
     */
    public Map<List<Blocks>,Integer> getResult(List<Blocks> nowBlocks,boolean[][] map) throws CloneNotSupportedException {

        //责任链模式设置优先级
        turnRight = new TurnRight();
        turnDown = new TurnDown();
        turnLeft = new TurnLeft();
        turnDown.setNextHandler(turnLeft);
        turnLeft.setNextHandler(turnRight);
        turnDown.setRootHandler(turnDown);
        turnLeft.setRootHandler(turnDown);
        turnRight.setRootHandler(turnDown);
        record.falseNumResult.clear();//先清空单例模式中上次的结果
        for (int i=0;i<4;i++){
            List<Blocks> judgeBlocks;//用于判断的方块
            record.visitedWay.clear();//旋转完再次遍历，先初始化visitedWay列表

            judgeBlocks = blocksUtil.copyBloks(nowBlocks);//将当前方块赋给用于判断的方块
            judgeBlocks = blocksUtil.moveToLeft(judgeBlocks,map);//得到最左边靠墙时的状态
            //开始寻路
            turnDown.handleWay(judgeBlocks,map);
            nowBlocks = rotateBlock(nowBlocks,map);//旋转
        }

        return record.falseNumResult;
    }


    /*
        旋转
     */
    public List<Blocks> rotateBlock(List<Blocks> nowBlocks, boolean[][] map) {
        double angel =Math.PI/2;//旋转角度
        int Width = record.getResultBlockMap().length;
        int Heigth = record.getResultBlockMap()[0].length;

        // 新矩阵存储结果
        boolean[][] newBlockMap = new boolean[Width][Heigth];
        // 计算旋转中心
        float CenterX = (Width - 1) / 2f;
        float CenterY = (Heigth - 1) / 2f;
        // 逐点计算变换后的位置
        for (int i = 0; i < Width; i++) {
            for (int j = 0; j < Heigth; j++) {
                //计算相对于旋转中心的坐标
                float RelativeX = j - CenterX;
                float RelativeY = i - CenterY;
                float ResultX = (float) (Math.cos(angel) * RelativeX - Math.sin(angel) * RelativeY);
                float ResultY = (float) (Math.cos(angel) * RelativeY + Math.sin(angel) * RelativeX);
				/* 调试信息
				System.out.println("RelativeX:" + RelativeX + "RelativeY:" + RelativeY);
				System.out.println("ResultX:" + ResultX + "ResultY:" + ResultY);
				*/
                //将结果坐标还原
                newBlockMap[Math.round(CenterY+ResultY)][Math.round(CenterX+ResultX)]=record.getResultBlockMap()[i][j];
            }
        }

        //不能设为全局变量，因为下面是直接=的，会出现地址问题，他妈的找了两个小时的bug！！
        List<Blocks> rotateBlocks = new ArrayList<Blocks>();//旋转后的方块,用来判断旋转后是否会发生碰撞
        int x = nowBlocks.get(0).X;
        int y = nowBlocks.get(0).Y;
        rotateBlocks.clear();//清空旋转的方块
        for (int i=0;i<newBlockMap.length;i++){
            for (int j=0;j<newBlockMap[i].length;j++){
                Blocks blocks = new Blocks();
                blocks.X=x+20*j;
                blocks.Y=y+20*i;
                if (newBlockMap[i][j]){
                    blocks.state=true;
                }
                rotateBlocks.add(blocks);
            }
        }
        if (!judgeRotateHit(rotateBlocks,map)){
            nowBlocks=rotateBlocks;
            //记录当前矩阵
            record.setResultBlockMap(newBlockMap);
        }
        return nowBlocks;
    }

    /*
        判断旋转时是否碰撞
     */
    public boolean judgeRotateHit(List<Blocks> currentBlocks, boolean[][] map) {
        for (Blocks blocks:currentBlocks){
            if (blocks.state) {
                int i = (blocks.X/20)-1;
                int j = blocks.Y/20;
                if (i<0||i>13||j>24)
                    return true;
                if (map[i][j])
                    return true;
            }
        }
        return false;
    }
}
