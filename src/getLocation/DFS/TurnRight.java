package getLocation.DFS;

import entity.Blocks;
import entity.RecordBlocks;
import util.BlocksUtil;
import util.CalculateUtil;

import java.awt.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class TurnRight implements Handler {

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
            //用于记录X方向上移动一个单位后的方块
            List<Blocks> hasMovedXR = blocksUtil.moveX(1,copyBlocks);
            //向右检测
            if (!blocksUtil.judgeHitX(1,nowBlocks,map)&&!record.visitedWay.contains(blocksUtil.getLeftPoint(hasMovedXR))){
                rootHandler.handleWay(hasMovedXR, map);
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
