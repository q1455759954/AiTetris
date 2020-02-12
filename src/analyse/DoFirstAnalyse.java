package analyse;

import entity.Blocks;
import util.BlocksUtil;
import util.CalculateUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    第一次分析：false数量最少的那些方块集合，并计算最多的那行true的数量，记录行数以及这行true的数量
 */
public class DoFirstAnalyse implements Strategy {

    private CalculateUtil calculateUtil;
    private BlocksUtil blocksUtil;
    private Map<List<Blocks>,Point> firstAnalyseResult = new HashMap<List<Blocks>,Point>();
    private ResultData adapter;

    @Override
    public Map<List<List<Blocks>>, Point> doAnalyse(Map<List<List<Blocks>>, Point> data, boolean[][] map) {
        adapter = new Adapter();
        Map<List<Blocks>, Integer> falseNumResult = adapter.giveResult(data,"a");
        System.out.println(falseNumResult.size());
        blocksUtil = new BlocksUtil();
        calculateUtil = new CalculateUtil();

        int min=1000;//随便定一个比较大的值
        firstAnalyseResult.clear();
        for(List<Blocks> key:falseNumResult.keySet()){
            if (falseNumResult.get(key)<min){
                min=falseNumResult.get(key);
                firstAnalyseResult.clear();
                Point point = getXAndTrueNum(key,map);
                firstAnalyseResult.put(key,point);
            }
            if (falseNumResult.get(key)==min){
                Point point = getXAndTrueNum(key,map);
                firstAnalyseResult.put(key,point);
            }
        }
        //适配器模式  Map<List<Blocks>,Point> --》 Map<List<List<Blocks>>, Point>
        System.out.println("第一次分析的的结果："+firstAnalyseResult.size());
        return adapter.giveResult(firstAnalyseResult);
    }

    //得到true最多的那行的行数以及true的数量，用ponit（X,Y)表示，X表示行数，Y代表数量
    private Point getXAndTrueNum(List<Blocks> key, boolean[][] map) {
        //复制当前的map
        boolean[][] newBlocksMap = calculateUtil.getNewBlocksMap(map);
        for (Blocks blocks: key){
            if (blocks.state){
                int i = (blocks.X/20)-1;
                int j = (blocks.Y/20);
                newBlocksMap[i][j]=true;
            }
        }
        //第一次优化的地方
        int top = calculateUtil.getTopY(key);
        int bottom = calculateUtil.getBottomY(key);

        List<Point> pointList = new ArrayList<Point>();
        for (int i=top;i<=bottom;i++){
            int num=0;
            for (int j=0;j<14;j++){
                if (newBlocksMap[j][i]){
                    num++;
                }
            }
            Point point = new Point(i,num);
            pointList.add(point);
        }
        int XNum=0;
        int max=0;
        //得到拥有最多true的那一行
        for (Point point:pointList){
            if (point.y>max){
                XNum=point.x;
                max=point.y;
            }
            if (point.y==max&&point.x>XNum){
                XNum=point.x;
                max=point.y;
            }
        }
        return new Point(XNum,max);
    }
}
