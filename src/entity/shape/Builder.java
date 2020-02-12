package entity.shape;


import entity.Blocks;

import java.util.List;

/*
    生成器模式
 */
public interface Builder {

    public abstract List<Blocks> buildBlocks();

}
