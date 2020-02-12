package entity;


import entity.sendData.Observer;
import entity.sendData.Subject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    单例模式：记录resultBlockMap形状用于旋转
 */
public class RecordBlocks implements Subject {

//    public List<Blocks> nowBlocks = new ArrayList<>();
    private ArrayList<Observer> list= new ArrayList<Observer>();//存放观察者
    private static RecordBlocks recordBlocks;//这个类本身
    private  boolean[][] resultBlockMap;//记录的图形用于旋转
    public Map<List<Blocks>,Integer> falseNumResult= new HashMap<List<Blocks>,Integer>();//寻路完得到的结果 观察者模式返回
    public Map<List<Blocks>,Integer> oneOrTwoDeadResultList= new HashMap<List<Blocks>,Integer>();//第0次分析得到的
    public List<Point> visitedWay = new ArrayList<Point>();//用来判断方块移动时是否遍历过

    private RecordBlocks() {
        recordBlocks=this;
    }

    public static synchronized RecordBlocks getMap(){
        if (recordBlocks==null){
            recordBlocks=new RecordBlocks();
        }
        return recordBlocks;
    }

    public boolean[][] getResultBlockMap() {
        return resultBlockMap;
    }

    public void setResultBlockMap(boolean[][] resultBlockMap) {
        this.resultBlockMap = resultBlockMap;
    }

    @Override
    public void addObserver(Observer o) {
        if (!(list.contains(o))){
            list.add(o);
        }
    }

    @Override
    public void notifyObservers() {
        for (int i=0;i<list.size();i++){
            Observer observer = list.get(i);
            observer.receiveData(falseNumResult);
        }
    }
}
