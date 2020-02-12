package entity.shape;

import entity.BlockData;
import entity.BlockFactory;
import entity.Blocks;
import entity.RecordBlocks;

import java.util.ArrayList;
import java.util.List;

public class OShape implements Builder{
    public List<Blocks> nowBlocks = new ArrayList<Blocks>();
    static final boolean[][][] Shape = {
            // O
            {
                    { true, true },
                    { true, true }
            }
    };

    public OShape() {
    }

    @Override
    public List<Blocks> buildBlocks() {
        //享元模式得到方块size
        BlockFactory blockFactory = BlockFactory.getBlockFactory();
        BlockData blockData = blockFactory.getBlockSize();
        //单例模式得到记录map用于旋转
        RecordBlocks recordBlocks = RecordBlocks.getMap();
        boolean[][] newBlockMap = new boolean[Shape[0].length][Shape[0][0].length];

        for (int i=0;i<Shape[0].length;i++){
            for (int j=0;j<Shape[0][i].length;j++){
                newBlockMap[i][j]=Shape[0][i][j];//将当前图形的形状记录下来用于旋转
                Blocks blocks = new Blocks();
                blocks.X=140+blockData.getWIDTH()*j;
                blocks.Y=blockData.getHEIGTH()*i;
                blocks.blockData=blockData;
                if (Shape[0][i][j]){
                    blocks.state=true;
                }
                nowBlocks.add(blocks);
            }
        }
        recordBlocks.setResultBlockMap(newBlockMap);
        return nowBlocks;
    }
}
