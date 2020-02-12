package analyse;

import entity.Blocks;

import java.awt.*;
import java.util.List;
import java.util.Map;

/*
    策略模式接口
 */
public interface Strategy {
    public Map<List<List<Blocks>>, Point> doAnalyse(Map<List<List<Blocks>>, Point> data, boolean[][] map);
}
