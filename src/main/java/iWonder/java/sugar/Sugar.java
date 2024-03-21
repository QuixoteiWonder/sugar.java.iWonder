package iWonder.java.sugar;

import iWonder.java.sugar.kit.DateKit;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class Sugar {
    /**
     * NOTE 时间处理函数
     */
    public static final DateKit Date = new DateKit();

    /**
     * NOTE: 格式化命名字符串.
     *  第一个参数为格式化模板. 以{diyName}的方式指定并命名一个要替换的模块.
     *  剩下的参数两两一对. 分别表示模板中的名字 和 要实际替换的值
     * 示例:
     *  Sugar.format("name: {name}, age: {age}",
     *  "name", "Anne",
     *  "age", 15)
     *  -----> "name: Anne, age: 15"
     */
    public static String format(String format, Object... args) {
        for (int i = 0; i < args.length; i += 2) {
            format = format.replace(String.format("{%s}", args[i].toString()), args[i + 1].toString());
        }
        return format;
    }

    /**
     * NOTE 同时打印多个任意数据类型的值.
     *  并添加了数组打印支持, 即默认将数组中内容打印出来.
     * 示例:
     *  String[] arr2 = {"dashi", "baiqun", "wangxiang", "wennuan", "ni"};
     *  ArrayList list = new ArrayList<>();
     *  list.add(1);
     *  list.add(2);
     *  Sugar.print(11, "asfs", arr2, list);
     *  -->  11    asfs    [dashi, baiqun, wangxiang, wennuan, ni]    [1, 2]
     */
    public static void print(Object... args) {
        StringBuilder s = new StringBuilder();
        for (Object arg : args) {
            if (arg.getClass().isArray()) {
                arg = Arrays.asList((Object[]) arg).toString();
            }
            s.append(arg.toString());
            s.append("    ");
        }
        System.out.println(s);
    }

    /**
     * NOTE 以秒为单位, 睡一会儿~
     * 示例:
     *  Sugar.sleep(5);
     */
    public static void sleep(int seconds) {

        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * NOTE 返回给定对象的布尔值
     *  null, false, 数字类型的0值, 长度为0的数组|列表|集合|map|字符串 等等 认定其bool值为false.
     *  反之则为true.
     * 示例:
     *  Sugar.bool("hello")  -> true.
     *  Sugar.bool(0)        -> false.
     */
    public static Boolean bool(Object o) {
        // 最终返回的结果
        Boolean flag = null;

        // NOTE null判断
        if (o == null) {
            return false;
        }

        switch (o.getClass().getSimpleName()) {
            // NOTE 基础数据类型系列判断
            case "Boolean":
                flag = (boolean) o;
                break;
            case "String":
            case "Character":
                flag = !String.valueOf(o).isEmpty();
                break;
            case "Short":
                flag = Short.parseShort(o.toString()) != 0;
                break;
            case "Integer":
                flag = Integer.parseInt(o.toString()) != 0;
                break;
            case "Long":
                flag = Long.parseLong(o.toString()) != 0L;
                break;
            case "Float":
                flag = Float.parseFloat(o.toString()) != 0F;
                break;
            case "Double":
                flag = Double.parseDouble(o.toString()) != 0D;
                break;

            // NOTE 数组系列判断
            case "boolean[]":
                flag = !(((boolean[]) o).length == 0);
                break;
            case "Boolean[]":
                flag = !(((Boolean[]) o).length == 0);
                break;
            case "String[]":
                flag = !(((String[]) o).length == 0);
                break;
            case "char[]":
                flag = !(((char[]) o).length == 0);
                break;
            case "Character[]":
                flag = !(((Character[]) o).length == 0);
                break;
            case "short[]":
                flag = !(((short[]) o).length == 0);
                break;
            case "Short[]":
                flag = !(((Short[]) o).length == 0);
                break;
            case "int[]":
                flag = !(((int[]) o).length == 0);
                break;
            case "Integer[]":
                flag = !(((Integer[]) o).length == 0);
                break;
            case "long[]":
                flag = !(((long[]) o).length == 0);
                break;
            case "Long[]":
                flag = !(((Long[]) o).length == 0);
                break;
            case "float[]":
                flag = !(((float[]) o).length == 0);
                break;
            case "Float[]":
                flag = !(((Float[]) o).length == 0);
                break;
            case "double[]":
                flag = !(((double[]) o).length == 0);
                break;
            case "Double[]":
                flag = !(((Double[]) o).length == 0);
                break;

            // NOTE 集合系列判断
            case "ArrayList":
            case "LinkedList":
            case "Vector":
            case "HashSet":
            case "TreeSet":
                flag = !(((Collection<Object>) o).isEmpty());
                break;

            // NOTE Map系列判断
            case "HashMap":
            case "Hashtable":
            case "TreeMap":
                flag = !(((Map<Object, Object>) o).isEmpty());
                break;
        }
        return flag;
    }

    /**
     * NOTE 依次判断传入参数的bool值,
     *  若为false, 则继续判断下一个参数的bool值.
     *  直到找到bool值为true的参数, 返回该参数
     *  或者 当前已经是最后一个参数, 返回该参数
     * 示例:
     *  or("1", "a", "a", "", "c", "sdfs")  返回"1"
     *  or("", "", "", "", "", "")          返回""
     */
    public static <T> T or(T a, T b, T... args) {
        // r: 最终要返回的值
        T r = bool(a) ? a : b;
        for (T i : args) {
            r = bool(r) ? r : i;
            if (bool(r)) break;
        }
        return r;
    }

    /**
     * NOTE 依次判断传入参数的bool值,
     *  若为true, 则继续判断下一个参数的bool值.
     *  直到找到bool值为false的参数, 返回该参数
     *  或者 当前已经是最后一个参数, 返回该参数
     * 示例:
     *  and("1", "a", "a", "", "c", "sdfs")   返回""
     *  and("1", "a", "a", "d", "c", "sdfs")  返回"sdfs"
     */
    public static <T> T and(T a, T b, T... args) {
        // r: 最终要返回的值
        T r = bool(a) ? b : a;
        for (T i : args) {
            r = bool(r) ? i : r;
            if (!bool(r)) break;
        }
        return r;
    }
}


