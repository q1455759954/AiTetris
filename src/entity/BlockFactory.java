package entity;

/*
    享元模式工厂
 */
public class BlockFactory {

    static BlockFactory blockFactory = new BlockFactory();
    static BlockSize blockSize;

    public BlockFactory() {
    }

    public static BlockFactory getBlockFactory(){
        return blockFactory;
    }

    public BlockSize getBlockSize() {
        blockSize = new BlockSize(20,20);
        return blockSize;
    }

    class BlockSize implements BlockData{

        private int WIDTH = 20;
        private int HEIGTH = 20;

        public BlockSize(int WIDTH,int HEIGTH) {
            this.WIDTH=WIDTH;
            this.HEIGTH=HEIGTH;
        }

        public int getWIDTH() {
            return WIDTH;
        }

        public int getHEIGTH() {
            return HEIGTH;
        }
    }
}
