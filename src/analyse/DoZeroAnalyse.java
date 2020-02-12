package analyse;

import entity.Blocks;
import entity.RecordBlocks;
import util.BlocksUtil;
import util.CalculateUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
    第零次分析：1.将不能到达底部的去掉
                2.将能形成1个或2个死区的去掉并把他们记录下来用作最后的分析
    策略模式与适配器模式
 */
public class DoZeroAnalyse implements Strategy {
    private CalculateUtil calculateUtil;
    private BlocksUtil blocksUtil;
    private ResultData adapter;
    @Override
    public Map<List<List<Blocks>>, Point> doAnalyse(Map<List<List<Blocks>>, Point> data, boolean[][] map) {
        adapter = new Adapter();
        //适配器模式改变格式
        Map<List<Blocks>, Integer> zeroAnalyse = adapter.giveResult(data,"a");
        System.out.println("-----------------------");
        System.out.println(zeroAnalyse.size());
        blocksUtil = new BlocksUtil();
        calculateUtil = new CalculateUtil();

        RecordBlocks recordBlocks = RecordBlocks.getMap();
        recordBlocks.oneOrTwoDeadResultList.clear();
        Iterator entries = zeroAnalyse.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            List<Blocks> key = (List<Blocks>) entry.getKey();
            int value = (int) entry.getValue();
            if (!blocksUtil.judgeHitY(key,map)){
                entries.remove();
            }else {
                int before = hasDead(null,map);
                int now = hasDead(key,map);
                if (now>before){
                    recordBlocks.oneOrTwoDeadResultList.put(key,value);
                    entries.remove();
                }
            }
        }
        //用适配器模式得到返回的结果类型Map<List<Blocks>,Integer> --》 Map<List<List<Blocks>>, Point>
        return adapter.giveResult(zeroAnalyse,1);
    }

    //判断这种情况会不会导致出现死的空格
    private int hasDead(List<Blocks> key,boolean[][] map) {
        int count = 0;
        //复制当前的map
        boolean[][] newBlocksMap = calculateUtil.getNewBlocksMap(map);
        if (key!=null){
            for (Blocks blocks: key){
                if (blocks.state){
                    int i = (blocks.X/20)-1;
                    int j = (blocks.Y/20);
                    newBlocksMap[i][j]=true;
                }
            }
        }
        List<Blocks> judgeBloks =new ArrayList<Blocks>();
        int top = blocksUtil.getTop(newBlocksMap);
        for (int i=top+1;i<=24;i++){
            for (int j=0;j<=13;j++){
                if(!newBlocksMap[j][i]){
                    judgeBloks.clear();
                    Blocks blocks = new Blocks();
                    blocks.X=j*20+20;
                    blocks.Y=i*20;
                    judgeBloks.add(blocks);
                    //得到这个false周围false的数量
                    int num = calculateUtil.getThisStateFalseNum(judgeBloks,newBlocksMap);
                    //如果false周围false的数量等于0就认为死了，这里可以优化，没思路！！
                    if (num==0){
                        count++;
                        break;
                    }
                    if (num==1){
                        //判断两个或以上死方块的情况
                        if (judgeTwoFalseState(j,i,newBlocksMap)){
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    private boolean judgeTwoFalseState(int x, int y, boolean[][] newBlocksMap) {
        List<Blocks> judgeBloks =new ArrayList<Blocks>();
        if (x+1<14&&!newBlocksMap[x+1][y]){
            Blocks blocks = new Blocks();
            blocks.X=(x+1)*20+20;
            blocks.Y=y*20;
            judgeBloks.add(blocks);
            int num = calculateUtil.getThisStateFalseNum(judgeBloks,newBlocksMap);
            if (num==1){
                return true;
            }
        }
        if (x-1>=0&&!newBlocksMap[x-1][y]){
            Blocks blocks = new Blocks();
            blocks.X=(x-1)*20+20;
            blocks.Y=y*20;
            judgeBloks.add(blocks);
            int num = calculateUtil.getThisStateFalseNum(judgeBloks,newBlocksMap);
            if (num==1){
                return true;
            }
        }
        if (y+1<25&&!newBlocksMap[x][y+1]){
            Blocks blocks = new Blocks();
            blocks.X=x*20+20;
            blocks.Y=(y+1)*20;
            judgeBloks.add(blocks);
            int num = calculateUtil.getThisStateFalseNum(judgeBloks,newBlocksMap);
            if (num==1){
                return true;
            }
        }
        if (y-1>=0&&!newBlocksMap[x][y-1]){
            Blocks blocks = new Blocks();
            blocks.X=x*20+20;
            blocks.Y=(y-1)*20;
            judgeBloks.add(blocks);
            int num = calculateUtil.getThisStateFalseNum(judgeBloks,newBlocksMap);
            if (num==1){
                return true;
            }
        }
        return false;
    }
}
