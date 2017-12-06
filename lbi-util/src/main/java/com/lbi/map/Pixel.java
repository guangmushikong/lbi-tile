package com.lbi.map;
/**
 *
 * 屏幕像素坐标类，该类为基础类（单位：像素）。
 * <p>像素坐标系（Pixel Coordinates）单位。</p>
 * <p>以左上角为原点(0,0)，向右向下为正方向。</p>
 * @version	1.0
 * @author liumk
 */
public class Pixel {
    /**
     * 横向像素
     */
    public int x;
    /**
     * 纵向像素
     */
    public int y;

    /**
     * 根据给定参数构造Pixel的新实例
     * @param x 横向像素
     * @param y 纵向像素
     */
    public Pixel(int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    /**
     * 将Pixel对象转换为字符串
     */
    public String toString(){
        return x+","+y;
    }
}
