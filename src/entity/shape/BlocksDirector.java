package entity.shape;

import entity.Blocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/*
    生成器模式指挥者，随机得到一个确定的图形
 */
public class BlocksDirector {

    public BlocksDirector() {
    }

    public List<Blocks> getBlocks() {
        List<Blocks> blocks = null;
        String[] shape = {"I","J","L","O","S","T","Z"};
        int m =  (int) (Math.random() * 1000) % 28;
        int ran = m/4;//随机值
        //通过反射机制得到方块类
        try {
            Class cla = Class.forName("entity.shape."+shape[ran]+"Shape");
            Method method = cla.getMethod("buildBlocks");
            Constructor c = cla.getConstructor();
            //根据构造器，实例化出对象
            Object service = c.newInstance();
            //调用对象的指定方法
            blocks = (List<Blocks>) method.invoke(service);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blocks;
    }

}
