package getLocation.DFS;

import entity.Blocks;
import entity.RecordBlocks;
import util.BlocksUtil;
import util.CalculateUtil;

import java.awt.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class TurnLeft implements Handler {
    private Handler handler;
    private Handler rootHandler;
    private BlocksUtil blocksUtil = new BlocksUtil();
    private CalculateUtil calculateUtil = new CalculateUtil();
    private RecordBlocks record = RecordBlocks.getMap();

    @Override
    public void handleWay(List<Blocks> nowBlocks, boolean[][] map) {
//        try {
//            record.nowBlocks=blocksUtil.copyBloks(nowBlocks);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        try {
//            sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            List<Blocks> copyBlocks = blocksUtil.copyBloks(nowBlocks);
            //用于记录X方向左移动一个单位后的方块
            List<Blocks> hasMovedXL = blocksUtil.moveX(-1,copyBlocks);
            //必须等到不能向下了再向左检测，大部分情况下不能向左移动，但有特例
            if (!blocksUtil.judgeHitX(-1,nowBlocks,map)&&!record.visitedWay.contains(blocksUtil.getLeftPoint(hasMovedXL))){
                rootHandler.handleWay(hasMovedXL, map);
            }else{
                record.visitedWay.add(blocksUtil.getLeftPoint(nowBlocks));//将这个点加入到已经遍历过的里面
                calculateUtil.recordThisState(nowBlocks,map);
            }
            if (handler != null)
                handler.handleWay(nowBlocks, map);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNextHandler(Handler handler) {
        this.handler=handler;
    }

    @Override
    public void setRootHandler(Handler handler) {
        this.rootHandler=handler;
    }
}
