package analyse;

import entity.Blocks;
import entity.RecordBlocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    第二次分析后在不同的方块集合中找到最优解，即某行true多并且行数靠向下
 */
public class DoSecondAnalyse implements Strategy {

    private ResultData adapter;

    @Override
    public Map<List<List<Blocks>>, Point> doAnalyse(Map<List<List<Blocks>>, Point> data, boolean[][] map) {
        adapter = new Adapter();
        Map<List<Blocks>,Point> firstAnalyseResult = adapter.giveResult(data,1.0);
        System.out.println(firstAnalyseResult.size());
        List<List<Blocks>> finallyBlocksList = new ArrayList<List<Blocks>>();

        RecordBlocks recordBlocks = RecordBlocks.getMap();
        int XNum=0;
        int max=0;
//        List<Blocks> finallyBlocks = null;
        //用ponit（X,Y)表示，X表示行数，Y代表数量
        for (List<Blocks> key:firstAnalyseResult.keySet()){
            if (firstAnalyseResult.get(key).y>max){
                finallyBlocksList.clear();
                max=firstAnalyseResult.get(key).y;
                XNum=firstAnalyseResult.get(key).x;
//                finallyBlocks=key;
                finallyBlocksList.add(key);
            }else if (firstAnalyseResult.get(key).y==max&&firstAnalyseResult.get(key).x>XNum){
                finallyBlocksList.clear();
                max=firstAnalyseResult.get(key).y;
                XNum=firstAnalyseResult.get(key).x;
//                finallyBlocks=key;
                finallyBlocksList.add(key);
            } else if (firstAnalyseResult.get(key).y==max&&firstAnalyseResult.get(key).x==XNum){
                finallyBlocksList.add(key);
            }
        }
        //将这次排除的情况也加入到集合，用作第四次分析
        for (List<Blocks> key:firstAnalyseResult.keySet()){
            if (!finallyBlocksList.contains(firstAnalyseResult.get(key))){
                recordBlocks.oneOrTwoDeadResultList.put(key,recordBlocks.falseNumResult.get(key));
            }
        }
        //适配器模式  List<List<Blocks>> -- 》 Map<List<List<Blocks>>, Point>
        return adapter.giveResult(finallyBlocksList);
    }
}
