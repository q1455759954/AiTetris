package getLocation.DFS;

import entity.Blocks;

import java.awt.*;
import java.util.List;

/*
    责任链模式
 */
public  interface Handler {

    public abstract void handleWay(List<Blocks> nowBlocks, boolean[][] map);
    public abstract void setNextHandler(Handler handler);
    public abstract void setRootHandler(Handler handler);

}
