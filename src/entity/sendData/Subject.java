package entity.sendData;


/*
    观察者模式 主题接口
 */
public interface Subject {
    public void addObserver(Observer o);
    public void notifyObservers();
}
