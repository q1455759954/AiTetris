package analyse;

import entity.Blocks;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    适配器模式
 */
public class Adapter implements ResultData{

    @Override
    public Map<List<List<Blocks>>, Point> giveResult(Map<List<Blocks>, Integer> data, int a) {
        Map<List<List<Blocks>>, Point> result = new HashMap<List<List<Blocks>>,Point>();
        for (List<Blocks> key:data.keySet()){
            List<List<Blocks>> lists = new ArrayList<>();
            lists.add(key);
            result.put(lists,new Point(data.get(key),0));
        }
        return result;
    }

    @Override
    public Map<List<Blocks>, Integer> giveResult(Map<List<List<Blocks>>, Point> data, String a) {
        Map<List<Blocks>,Integer> result= new HashMap<List<Blocks>,Integer>();//寻路完得到的结果
        for (List<List<Blocks>> key:data.keySet()){
            result.put(key.get(0),data.get(key).x);
        }
        return result;
    }

    @Override
    public Map<List<List<Blocks>>, Point> giveResult(Map<List<Blocks>, Point> data) {
        Map<List<List<Blocks>>, Point> result = new HashMap<List<List<Blocks>>,Point>();
        for (List<Blocks> key:data.keySet()){
            List<List<Blocks>> lists = new ArrayList<>();
            lists.add(key);
            result.put(lists,data.get(key));
        }
        return result;
    }


    @Override
    public Map<List<Blocks>, Point> giveResult(Map<List<List<Blocks>>, Point> data, double a) {
        Map<List<Blocks>,Point> result= new HashMap<List<Blocks>,Point>();//寻路完得到的结果
        for (List<List<Blocks>> key:data.keySet()){
            result.put(key.get(0),data.get(key));
        }
        return result;
    }
    @Override
    public Map<List<List<Blocks>>, Point> giveResult(List<List<Blocks>> data) {
        Map<List<List<Blocks>>, Point> result = new HashMap<List<List<Blocks>>,Point>();
        result.put(data,new Point(0,0));
        return result;
    }

    @Override
    public List<List<Blocks>> giveResult(Map<List<List<Blocks>>, Point> data, boolean a) {
        List<List<Blocks>> result =  data.keySet().iterator().next();
        return result;
    }

    @Override
    public Map<List<List<Blocks>>, Point> giveResult(List<Blocks> data, int a) {
        Map<List<List<Blocks>>, Point> result = new HashMap<List<List<Blocks>>,Point>();
        List<List<Blocks>> lists = new ArrayList<>();
        lists.add(data);
        result.put(lists,new Point(0,0));
        return result;
    }

    @Override
    public List<Blocks> giveResult(Map<List<List<Blocks>>, Point> data, byte a) {
        List<Blocks> result = data.keySet().iterator().next().get(0);
        return result;
    }
}
