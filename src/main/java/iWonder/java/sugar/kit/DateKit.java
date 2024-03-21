package iWonder.java.sugar.kit;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateKit {
    /**
     * NOTE 时间处理相关函数工具集
     *  基本原则: 不用过时的Date库 使用DateTime库
     *  命名时: dt代表datetime  ts代表timestamp(Long类型 毫秒为单位)  str代表时间序列化后的字符串  format代表时间序列化的模板 2代表to
     *  str dt ts 等关键字全部用小写
     *  所有 setXXX parse函数会修改全局属性
     *  其他 函数不会修改全局属性, 只作为临时使用
     *  dt ts 都是utc概念. 只有在与str相互转换时,才会涉及时区(zone)和格式化字符串(format)
     * NOTE
     *  链式用法, 函数名统一前缀to关键字
     *  函数式用法, 函数名
     * 示例:
     *  1. 获取当前时间戳
     *       Sugar.Date.now2ts();
     *         --> 1710931966273
     *  2. 获取当前时间字符串
     *       Sugar.Date.now2str("yyyy-MM", "UTC+7");
     *         --> 2024-03
     *  3. 时间字符串计算(获取去年的字符串)
     *       Sugar.Date.strPlusYears("2024-03-20","yyyy-MM-dd", -1L)
     *         --> 2023-03-20
     *  4. 时间字符串重新格式化
     *       Sugar.Date.str2str("2024-03-20 14:52:51", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "UTC+8", "UTC+7")
     *         --> 2024-01-20 14:52:51
     *  5. 链式用法
     *       Sugar.Date.parse("2024-03-20 14:52:51").toTs()
     *         --> 1710917571000
     *       Sugar.Date.setZone("UTC+8").setFormat("yyyy-MM-dd~~").parse("2024-03-20-").toPlusDays(12L).toStr()
     *         --> 2024-04-01~~
     */
    // 链式调用时用于存储parse之后的时间值
    private ZonedDateTime dt = null;
    // 默认时区: 东八区
    protected ZoneId zone = ZoneId.of("UTC+8");
    // 默认format: yyyy-MM-dd HH:mm:ss
    protected String format = "yyyy-MM-dd HH:mm:ss";


    /**
     * NOTE 动态设置时区, 全局有效
     *  可链式调用
     */
    public DateKit setZone(String zone) {
        this.zone = ZoneId.of(zone);
        return this;
    }

    /**
     * NOTE 动态设置默认时间格式化字符串, 全局有效
     *  可链式调用
     */
    public DateKit setFormat(String format) {
        this.format = format;
        return this;
    }

    private DateKit setDt(ZonedDateTime dt) {
        this.dt = dt;
        return this;
    }

    /**
     * NOTE 获取当前时间 ZonedDateTime类型
     */
    public ZonedDateTime now2dt() {
        return ZonedDateTime.now(zone);
    }

    /**
     * NOTE 获取当前时间戳 Long类型
     */
    public Long now2ts() {
        return dt2ts(now2dt());
    }

    /**
     * NOTE 获取当前时间 字符串类型, 可选择使用默认格式
     */
    public String now2str(String format, ZoneId zone) {
        return dt2str(now2dt(), format, zone);
    }

    public String now2str(String format, String zone) {
        return now2str(format, ZoneId.of(zone));
    }

    public String now2str(String format) {
        return now2str(format, zone);
    }

    public String now2str() {
        return now2str(format);
    }

    /**
     * NOTE 指定日期 dt类型 转为 str类型, 可选使用 默认格式 时区
     */
    public String dt2str(ZonedDateTime dt, String format, ZoneId zone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(zone);
        return formatter.format(dt);
    }

    public String dt2str(ZonedDateTime dt, String format, String zone) {
        return dt2str(dt, format, ZoneId.of(zone));
    }

    public String dt2str(ZonedDateTime dt, String format) {
        return dt2str(dt, format, zone);
    }

    public String dt2str(ZonedDateTime dt) {
        return dt2str(dt, format);
    }

    /**
     * NOTE 指定日期 dt类型 转为 时间戳Long类型
     */
    public Long dt2ts(ZonedDateTime dt) {
        return dt.toInstant().toEpochMilli();
    }

    /**
     * NOTE 指定日期 str类型 转为 dt类型  可选默认时区和格式化字符串
     */
    public ZonedDateTime str2dt(String s, String format, ZoneId zone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(zone);
        ZonedDateTime dt;
        // 如果输入的是一个仅日期的格式化模块, 因为没有时分秒自然也没有时区概念,会触发异常
        // 解决思路:
        //  如果仅日期, 则视为此时为 当前日期的当前时区的零点零分零秒.
        try {
            dt = ZonedDateTime.parse(s, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Catch DateTimeParseException: \n" +
                    "  in project -> iWonder.java.sugar\n" +
                    "    in package -> iWonder.java.sugar.kit\n" +
                    "      in function -> { iWonder.java.sugar.Kit.DateKit.str2dt(String s, String format, ZoneId zone) }");
            dt = LocalDate.parse(s, formatter).atStartOfDay(zone);
        }
        return dt;
    }

    public ZonedDateTime str2dt(String s, String format, String zone) {
        return str2dt(s, format, ZoneId.of(zone));
    }

    public ZonedDateTime str2dt(String s, String format) {
        return str2dt(s, format, zone);
    }

    public ZonedDateTime str2dt(String s) {
        return str2dt(s, format);
    }


    /**
     * NOTE 指定日期 str类型 转为 时间戳  可选默认时区和格式化字符串
     */
    public Long str2ts(String s, String format, ZoneId zone) {
        return dt2ts(str2dt(s, format, zone));
    }

    public Long str2ts(String s, String format, String zone) {
        return str2ts(s, format, ZoneId.of(zone));
    }

    public Long str2ts(String s, String format) {
        return str2ts(s, format, zone);
    }

    public Long str2ts(String s) {
        return str2ts(s, format);
    }

    /**
     * NOTE 指定日期 str类型 转为 str类型  可选默认时区和格式化字符串
     */
    public String str2str(String s, String formatSRC, String formatDST, ZoneId zoneSRC, ZoneId zoneDST) {
        return dt2str(str2dt(s, formatSRC, zoneSRC), formatDST, zoneDST);
    }

    public String str2str(String s, String formatSRC, String formatDST, String zoneSRC, String zoneDST) {
        return str2str(s, formatSRC, formatDST, ZoneId.of(zoneSRC), ZoneId.of(zoneDST));
    }

    public String str2str(String s, String formatSRC, String formatDST) {
        return str2str(s, formatSRC, formatDST, zone, zone);
    }


    /**
     * NOTE 指定日期 时间戳类型 转为 dt类型
     */
    public ZonedDateTime ts2dt(Long ts) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), zone);
    }

    /**
     * NOTE 指定日期 时间戳类型 转为 str类型 可选默认时区和格式化字符串
     */
    public String ts2str(Long ts, String format, ZoneId zone) {
        return dt2str(ts2dt(ts), format, zone);
    }

    public String ts2str(Long ts, String format, String zone) {
        return ts2str(ts, format, ZoneId.of(zone));
    }

    public String ts2str(Long ts, String format) {
        return ts2str(ts, format, zone);
    }

    public String ts2str(Long ts) {
        return ts2str(ts, format);
    }


    /**
     * NOTE 将指定类型的 str|ts|java.util.Date|ZonedDateTime 解析为 Date类型
     *  通常是是链式调用的起点
     */

    public DateKit parse(String s, String format, ZoneId zone) {
        this.dt = str2dt(s, format, zone);
        return this;
    }

    public DateKit parse(String s, String format, String zone) {
        return parse(s, format, ZoneId.of(zone));
    }

    public DateKit parse(String s, String format) {
        return parse(s, format, zone);
    }

    public DateKit parse(String s) {
        return parse(s, format);
    }

    public DateKit parse(Long ts) {
        this.dt = ts2dt(ts);
        return this;
    }

    public DateKit parse(ZonedDateTime dt) {
        this.dt = dt;
        return this;
    }

    public DateKit parse(java.util.Date dt, ZoneId zone) {
        this.dt = ZonedDateTime.ofInstant(dt.toInstant(), zone);
        return this;
    }

    public DateKit parse(java.util.Date dt, String zone) {
        return parse(dt, ZoneId.of(zone));
    }

    public DateKit parse(java.util.Date dt) {
        return parse(dt, zone);
    }


    /**
     * NOTE 链式调用 Sugar.Date类型 转成 str类型 可指定时区
     */
    public String toStr(String format, ZoneId zone) {
        return dt2str(dt, format, zone);
    }

    public String toStr(String format, String zone) {
        return toStr(format, ZoneId.of(zone));
    }

    public String toStr(String format) {
        return toStr(format, zone);
    }

    public String toStr() {
        return toStr(format);
    }

    public Long toTs() {
        return dt2ts(dt);
    }


    /**
     * NOTE 链式调用 Sugar.Date类型 转成 dt类型
     */
    public ZonedDateTime toDt() {
        return dt;
    }


    /**
     * NOTE 时间计算函数 分别以秒和天为单位 返回dt类型
     */
    public ZonedDateTime dtPlusNanosSeconds(ZonedDateTime dt, int nanosSeconds) {
        return dt.plusNanos(nanosSeconds);
    }

    public ZonedDateTime dtPlusMicroSeconds(ZonedDateTime dt, int microSeconds) {
        return dtPlusNanosSeconds(dt, microSeconds * 1000);
    }

    public ZonedDateTime dtPlusMilliSeconds(ZonedDateTime dt, int milliSeconds) {
        return dtPlusMicroSeconds(dt, milliSeconds * 1000);
    }

    public ZonedDateTime dtPlusSeconds(ZonedDateTime dt, int seconds) {
        return dt.plusSeconds(seconds);
    }

    public ZonedDateTime dtPlusMinutes(ZonedDateTime dt, int minutes) {
        return dt.plusMinutes(minutes);
    }

    public ZonedDateTime dtPlusHours(ZonedDateTime dt, int hours) {
        return dt.plusHours(hours);
    }

    public ZonedDateTime dtPlusDays(ZonedDateTime dt, int days) {
        return dt.plusDays(days);
    }

    public ZonedDateTime dtPlusMonths(ZonedDateTime dt, int months) {
        return dt.plusMonths(months);
    }

    public ZonedDateTime dtPlusYears(ZonedDateTime dt, int years) {
        return dt.plusYears(years);
    }

    /**
     * NOTE 时间计算函数 分别以秒和天为单位 返回str类型
     */

    public String strPlusNanosSeconds(String s, String format, int nanosSeconds) {
        return dt2str(dtPlusNanosSeconds(str2dt(s, format), nanosSeconds), format);
    }

    public String strPlusNanosSeconds(String s, int nanosSeconds) {
        return strPlusNanosSeconds(s, format, nanosSeconds);
    }

    public String strPlusMicroSeconds(String s, String format, int microSeconds) {
        return dt2str(dtPlusMicroSeconds(str2dt(s, format), microSeconds), format);
    }

    public String strPlusMicroSeconds(String s, int microSeconds) {
        return strPlusMicroSeconds(s, format, microSeconds);
    }

    public String strPlusMilliSeconds(String s, String format, int milliSeconds) {
        return dt2str(dtPlusMilliSeconds(str2dt(s, format), milliSeconds), format);
    }

    public String strPlusMilliSeconds(String s, int milliSeconds) {
        return strPlusMilliSeconds(s, format, milliSeconds);
    }


    public String strPlusSeconds(String s, String format, int seconds) {
        return dt2str(dtPlusSeconds(str2dt(s, format), seconds), format);
    }

    public String strPlusSeconds(String s, int seconds) {
        return strPlusSeconds(s, format, seconds);
    }

    public String strPlusMinutes(String s, String format, int minutes) {
        return dt2str(dtPlusMinutes(str2dt(s, format), minutes), format);
    }

    public String strPlusMinutes(String s, int minutes) {
        return strPlusMinutes(s, format, minutes);
    }

    public String strPlusHours(String s, String format, int hours) {
        return dt2str(dtPlusHours(str2dt(s, format), hours), format);
    }

    public String strPlusHours(String s, int hours) {
        return strPlusHours(s, format, hours);
    }

    public String strPlusDays(String s, String format, int days) {
        return dt2str(dtPlusDays(str2dt(s, format), days), format);
    }

    public String strPlusDays(String s, int days) {
        return strPlusDays(s, format, days);
    }

    public String strPlusMonths(String s, String format, int months) {
        return dt2str(dtPlusMonths(str2dt(s, format), months), format);
    }

    public String strPlusMonths(String s, int months) {
        return strPlusMonths(s, format, months);
    }

    public String strPlusYears(String s, String format, int years) {
        return dt2str(dtPlusYears(str2dt(s, format), years), format);
    }

    public String strPlusYears(String s, int years) {
        return strPlusYears(s, format, years);
    }


    /**
     * NOTE 时间计算函数 分别以秒和天为单位 链式调用专属
     */
    public DateKit toPlusNanosSeconds(int nanosSeconds) {
        return setDt(dtPlusNanosSeconds(dt, nanosSeconds));
    }
    public DateKit toPlusMicroSeconds(int microSeconds) {
        return setDt(dtPlusSeconds(dt, microSeconds));
    }
    public DateKit toPlusMilliSeconds(int milliSeconds) {
        return setDt(dtPlusSeconds(dt, milliSeconds));
    }


    public DateKit toPlusSeconds(int seconds) {
        return setDt(dtPlusSeconds(dt, seconds));
    }

    public DateKit toPlusMinutes(int minutes) {
        return setDt(dtPlusMinutes(dt, minutes));

    }

    public DateKit toPlusHours(int hours) {
        return setDt(dtPlusHours(dt, hours));
    }

    public DateKit toPlusDays(int days) {
        return setDt(dtPlusDays(dt, days));
    }

    public DateKit toPlusMonths(int months) {
        return setDt(dtPlusMonths(dt, months));
    }

    public DateKit toPlusYears(int years) {
        return setDt(dtPlusYears(dt, years));
    }

}
