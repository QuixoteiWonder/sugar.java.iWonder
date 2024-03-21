import iWonder.java.sugar.Sugar;

public class SugarTest {
    public static void main(String[] args) {
        System.out.println(Sugar.bool("hello world"));
        // --> true
        System.out.println(Sugar.bool(new int[]{}));
        // --> false

        System.out.println(Sugar.Date.now2str("yyyy-MM-dd HH:mm:ss"));
        // --> 2024-03-21 15:51:53
        System.out.println(Sugar.Date.parse("2022-02-02", "yyyy-MM-dd").toPlusHours(3).toStr());
        // --> 2022-02-02 03:00:00
    }
}
