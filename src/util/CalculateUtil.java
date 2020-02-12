package util;

import entity.Blocks;
import entity.RecordBlocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CalculateUtil {

    private List<Point> visitedBlocks = new ArrayList<Point>();//用来判断计算false数量时候是否重复,记录的是方块的坐标

    /*
        记录下这个状态
     */
    public void recordThisState(List<Blocks> recordBlocks, boolean[][] map) {

        //复制当前的map
        boolean[][] newBlocksMap = getNewBlocksMap(map);
        //改变newBlockMap为recordBlocks下落完成后的
        for (Blocks blocks: recordBlocks){
            if (blocks.state){
                int i = (blocks.X/20)-1;
                int j = (blocks.Y/20);
                newBlocksMap[i][j]=true;
            }
        }
        List<Blocks> allBlocks = getAllBlocks(newBlocksMap);//得到当前所有方块

        //得到了这个状态下的false数量，下面通过mMap记录
        int num = getThisStateFalseNum(allBlocks,newBlocksMap);
        RecordBlocks record = RecordBlocks.getMap();//使用单例模式记录下来
        record.falseNumResult.put(recordBlocks,num);
    }


    /*
        得到false数量
     */
    public int getThisStateFalseNum(List<Blocks> allBlocks, boolean[][] newBlocksMap) {
        int num=0;
        visitedBlocks.clear();//清空用于新的计数
        for (Blocks blocks:allBlocks){
            //向左
            num+=getDirectionNum(blocks,-1,0,newBlocksMap);
            //向右
            num+=getDirectionNum(blocks,1,0,newBlocksMap);
            //向上，y为-1
            num+=getDirectionNum(blocks,0,-1,newBlocksMap);
            //向下，y为1
            num+=getDirectionNum(blocks,0,1,newBlocksMap);
        }
        return num;
    }

    /*
        得到某一个方向上false的数量，上下左右
     */
    private int getDirectionNum(Blocks blocks, int x, int y,boolean[][] newBlocksMap) {
        int num = 0;
        int i = (blocks.X/20)-1+x;
        int j = (blocks.Y/20)+y;
        //如果过界，直接返回
        if (i>13||i<0||j>24){
            return num;
        }
        //如果j小于0，那就代表它紧贴着顶部，所以要给他一个大的值，不然会出现下面没填满就优先填上边的情况
        if (j<0){
            return 100;
        }

        Point point = new Point();
        point.x=i;
        point.y=j;
        //如果地图为false并且没有访问过
        if (!newBlocksMap[i][j]&&!visitedBlocks.contains(point)){
            num++;
            visitedBlocks.add(point);
        }
        return num;
    }

    /*
        得到当前地图所有的方块
     */
    private List<Blocks> getAllBlocks(boolean[][] newBlocksMap) {
        List<Blocks> allBlocks = new ArrayList<Blocks>();//当前地图中所有的方块
        for (int i=0;i<14;i++){
            for (int j=0;j<25;j++){
                if (newBlocksMap[i][j]){
                    Blocks blocks = new Blocks();
                    blocks.X=i*20+20;
                    blocks.Y=j*20;
                    allBlocks.add(blocks);
                }
            }
        }
        return allBlocks;
    }

    /*
        复制一个map
     */
    public boolean[][] getNewBlocksMap(boolean[][] needCopyMap) {
        boolean[][] newBlocksMap = new boolean[14][25];//横向14个方块，纵向25个方块
        for (int i=0;i<14;i++){
            for (int j=0;j<25;j++){
                newBlocksMap[i][j]=needCopyMap[i][j];
            }
        }
        return newBlocksMap;
    }

    /*
        得到这个方块集合最下面那个方块的Y值
     */
    public int getBottomY(List<Blocks> key) {
        int maxY = 0;//随便定一个小的值
        for (Blocks blocks: key){
            if (blocks.state){
                if (blocks.Y>maxY){
                    maxY=blocks.Y;
                }
            }
        }
        return maxY/20;
    }
    public int getTopY(List<Blocks> key) {
        int minY = 1000;//随便定一个大的值
        for (Blocks blocks: key){
            if (blocks.state){
                if (blocks.Y<minY){
                    minY=blocks.Y;
                }
            }
        }
        return minY/20;
    }
}
