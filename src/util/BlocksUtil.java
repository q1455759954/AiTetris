package util;

import entity.Blocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlocksUtil {

    /*
        得到最上边的方块
     */
    public int getTop(boolean[][] curMap) {
        for (int j=1;j<25;j++){
            for (int i=0;i<14;i++){
                if (curMap[i][j]==true)
                    return j;
            }
        }
        return 1;
    }

    /*
        得到当前整个方块最左边，最上边的那个点，将他记录下来用于visited
     */
    public Point getLeftPoint(List<Blocks> blocks) {
        int minX=10000;//随便定一个比较大的值
        int minY=10000;//随便定一个比较大的值
        for (Blocks block :blocks){
            if (block.state){
                if (block.X<minX||block.Y<minY){
                    minX=block.X;
                    minY=block.Y;
                }
            }
        }
        Point point = new Point(minX,minY);
        return point;
    }


    /*
        纵方向向下移动一个单位
     */
    public List<Blocks> moveY(List<Blocks> moveYBlocks) {
        for (Blocks blocks:moveYBlocks){
            blocks.Y+=20;
        }
        return moveYBlocks;
    }
    /*
        横方向移动一个单位,lengh代表方向
     */
    public List<Blocks> moveX(int length,List<Blocks> moveXBlocks) {
        if (length==1){
            for (Blocks blocks:moveXBlocks){
                blocks.X+=20;
            }
        }else if (length==-1){
            for (Blocks blocks:moveXBlocks){
                blocks.X-=20;
            }
        }
        return moveXBlocks;
    }

    /*
        将方块移动到最左边开始寻路
     */
    public List<Blocks> moveToLeft(List<Blocks> judgeBlocks, boolean[][] map) {
        while (!judgeHitX(-1,judgeBlocks,map)){
            for (Blocks blocks:judgeBlocks){
                blocks.X-=20;
            }
        }
        return judgeBlocks;
    }

    /*
        水平方向检测碰撞,length向右为正，向左为负
     */
    public boolean judgeHitX(int length, List<Blocks> currentBlocks, boolean[][] map) {
        for (Blocks blocks:currentBlocks){
            if (blocks.state) {
                int i = (blocks.X/20)-1+length;
                int j = blocks.Y/20;
                if (i<0||i>13||map[i][j])
                    return true;
            }
        }
        return false;
    }

    /*
        垂直方向检测碰撞,传过来要检测的方块列表
     */
    public boolean judgeHitY(List<Blocks> currentBlocks, boolean[][] map) {
        for (Blocks blocks:currentBlocks){
            if (blocks.state){
                int i = (blocks.X/20)-1;
                int j = (blocks.Y/20)+1;
                if (j>24||map[i][j])
                    return true;
            }
        }
        return false;
    }

    /*
        原型模式克隆方块
     */
    public List<Blocks> copyBloks(List<Blocks> nowBlocks) throws CloneNotSupportedException {
        List<Blocks> result = new ArrayList<Blocks>();
        for (Blocks blocks :nowBlocks){
            result.add((Blocks)blocks.clone());
        }
        return result;
    }

}
