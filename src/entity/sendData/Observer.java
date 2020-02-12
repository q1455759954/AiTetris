package entity.sendData;

import entity.Blocks;

import java.util.List;
import java.util.Map;

/*
    观察者模式 观察者接口
 */
public interface Observer {
    public void receiveData(Map<List<Blocks>,Integer> result);
}
