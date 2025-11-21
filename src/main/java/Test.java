// 抽象类
abstract class Door {
    abstract void open(); // 开门

    abstract void close(); // 关门
}

// 接口
interface IAlarmable {
    void alarm(); // 抽象方法: 报警功能
}
interface IPlayingMusic{
    void playMusic();
}

class SafeDoor extends Door implements IAlarmable ,IPlayingMusic {
    @Override
    void open() {
        System.out.println("安全门打开");
    }

    @Override
    void close() {
        System.out.println("安全门关闭");
    }

    @Override
    public void alarm() {
        System.out.println("安全门报警");
    }

    @Override
    public void playMusic() {
        System.out.println("安全门播放音乐");
    }
}

class KitchenAlarm implements IAlarmable{

    @Override
    public void alarm() {
        System.out.println("厨房防烟报警");
    }
}

public class Test  {
    public static void main(String[] args) {
        SafeDoor door = new SafeDoor();
        door.open();
        door.alarm();
        door.close();
        door.playMusic();
        KitchenAlarm kitchenAlarm = new KitchenAlarm();
        kitchenAlarm.alarm();

    }
}