package analyse;

import entity.Blocks;

import java.awt.*;
import java.util.Map;
import java.util.List;

/*
    适配器模式接口
 */
public interface ResultData {
    public Map<List<List<Blocks>>, Point> giveResult(Map<List<Blocks>,Integer> data,int a);
    public Map<List<Blocks>,Integer> giveResult(Map<List<List<Blocks>>, Point> data,String a);
    public Map<List<List<Blocks>>, Point> giveResult(Map<List<Blocks>,Point> data);
    public Map<List<Blocks>,Point> giveResult(Map<List<List<Blocks>>, Point> data,double a);
    public Map<List<List<Blocks>>, Point> giveResult(List<List<Blocks>> data);
    public List<List<Blocks>> giveResult(Map<List<List<Blocks>>, Point> data,boolean a);
    public Map<List<List<Blocks>>, Point> giveResult(List<Blocks> data,int a);
    public List<Blocks> giveResult(Map<List<List<Blocks>>, Point> data,byte a);
}
