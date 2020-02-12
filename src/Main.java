import entity.RecordBlocks;
import entity.sendData.Subject;

public class Main {

    private int a = 0;
    public static void main(String[] args){
        RecordBlocks recordBlocks = RecordBlocks.getMap();
        new GameWindow(recordBlocks);

    }
}
