package analyse;

import entity.Blocks;
import entity.RecordBlocks;
import util.BlocksUtil;
import util.CalculateUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    第三次分析：1.如果能消去行直接返回结果
                2.不能消去行，对他们进行分析：如果不会形成3个或以上死区随便返回一个，会的话接着进行分析
                3.将第0次分析保存的方块哪来重新做第一次，第二次分析，能消去行直接返回，不能随便返回一个
 */
public class DoThirdAnalyse implements Strategy {

    private CalculateUtil calculateUtil;
    private BlocksUtil blocksUtil;
    private ResultData adapter;
    private Strategy doFirstAnalyse;
    private Strategy doSecondAnalyse;

    @Override
    public Map<List<List<Blocks>>, Point> doAnalyse(Map<List<List<Blocks>>, Point> data, boolean[][] map) {
        adapter = new Adapter();
        doFirstAnalyse = new DoFirstAnalyse();
        doSecondAnalyse = new DoSecondAnalyse();
        List<List<Blocks>> finallyBlocksList = adapter.giveResult(data,true);
        System.out.println(finallyBlocksList.size());
        blocksUtil = new BlocksUtil();
        calculateUtil = new CalculateUtil();
        RecordBlocks recordBlocks = RecordBlocks.getMap();
        List<Blocks> re = canOverLine(finallyBlocksList,map);
        if (re!=null)
            return adapter.giveResult(re,1);

        boolean[][] curMap = calculateUtil.getNewBlocksMap(map);
        for (Blocks blocks: finallyBlocksList.get(0)){
            if (blocks.state){
                int i = (blocks.X/20)-1;
                int j = (blocks.Y/20);
                curMap[i][j]=true;
            }
        }
        for (List<Blocks> blocks : finallyBlocksList){
            int before = TreeOrMoreBlocksDead(calculateUtil.getNewBlocksMap(map),blocks);
            int now = TreeOrMoreBlocksDead(curMap,blocks);
            if (now<=before){
                return adapter.giveResult(blocks,1);
            }
        }
//        for (List<Blocks> blocks : falseNumResult.keySet()){
//            curBlocks = blocks;
//            repaint();
//            JOptionPane.showMessageDialog(null, 2333, "  好好学习", 0);//弹窗显示
//        }
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        System.out.println(recordBlocks.oneOrTwoDeadResultList.size());
        if (recordBlocks.oneOrTwoDeadResultList.size()==0)
            return adapter.giveResult(finallyBlocksList.get(0),1);
        Map<List<List<Blocks>>, Point> again = adapter.giveResult(recordBlocks.oneOrTwoDeadResultList,1);
        Map<List<List<Blocks>>, Point> firstResult = doFirstAnalyse.doAnalyse(again,map);
        Map<List<List<Blocks>>, Point> secondResult =  doSecondAnalyse.doAnalyse(firstResult,map);
        List<List<Blocks>> finallyList = adapter.giveResult(secondResult,true);
        List<Blocks> res = canOverLine(finallyList,map);
        if (res!=null)
            return adapter.giveResult(res,1);
        return adapter.giveResult(finallyList.get(0),1);

    }

    private int TreeOrMoreBlocksDead(boolean[][] curMap, List<Blocks> lists) {
        int count = 0;
        int top = blocksUtil.getTop(curMap);
        for (int i=top+1;i<=24;i++){
            for (int j=0;j<=13;j++){
                if (!curMap[j][i]){
                    if (!(goLeft(j,i,top,curMap)||goRight(j,i,top,curMap))){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean goLeft(int j, int i, int top, boolean[][] curMap) {
        if (i==top-1)
            return true;
        if (i-1>=0&&!curMap[j][i-1]){
            if (goLeft(j,i-1,top,curMap))
                return true;
        }
        if (j-1>=0&&!curMap[j-1][i]){
            if (goLeft(j-1,i,top,curMap))
                return true;
        }
        return false;
    }
    private boolean goRight(int j, int i, int top, boolean[][] curMap) {
        if (i==top-1)
            return true;
        if (i-1>=0&&!curMap[j][i-1]){
            if (goRight(j,i-1,top,curMap))
                return true;
        }
        if (j+1<14&&!curMap[j+1][i]){
            if (goRight(j+1,i,top,curMap))
                return true;
        }
        return false;
    }

    private List<Blocks> canOverLine(List<List<Blocks>> finallyBloksList, boolean[][] map) {
        for (List<Blocks> lists :finallyBloksList){
            //判断能不能消去行，能的话直接返回
            boolean[][] curMap = calculateUtil.getNewBlocksMap(map);
            for (Blocks blocks: lists){
                if (blocks.state){
                    int i = (blocks.X/20)-1;
                    int j = (blocks.Y/20);
                    curMap[i][j]=true;
                }
            }
            if (judgeLines(curMap)!=-1){
                return lists;
            }
        }
        return null;
    }

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
}
