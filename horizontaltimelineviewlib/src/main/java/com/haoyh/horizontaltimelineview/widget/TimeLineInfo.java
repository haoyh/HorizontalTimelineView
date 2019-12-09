package com.haoyh.horizontaltimelineview.widget;

/**
 * 横向时间轴控件数据实体类，要使用控件，必须构造该类型的数据
 * @author haoyh
 */
public class TimeLineInfo {

    /** id 暂未使用，可以不赋值 **/
    private int id;
    private String date;
    private String info;
    private boolean isPass;

    public TimeLineInfo() {

    }

    public TimeLineInfo(String date, String info, boolean isPass) {
        this.id = id;
        this.date = date;
        this.info = info;
        this.isPass = isPass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }
}
