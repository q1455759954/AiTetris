package entity;

public class Blocks implements Cloneable{

    public int X;
    public int Y;
    public boolean state = false;
    public BlockData blockData;

    /*
        原型模式克隆自己
     */
    public Object clone() throws CloneNotSupportedException {
        Blocks blocks = (Blocks)super.clone();
        return blocks;
    }

}
