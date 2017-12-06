package com.lbi.map;
/**
 *
 * 瓦片类，该类为基础类（单位：块）。
 * <p>地图瓦片坐标系（Tile Coordinates）单位。</p>
 * <p>瓦片坐标系以左上角为原点(0, 0)，到右下角(2 ^ 图像级别 - 1, 2 ^ 图像级别 - 1)为止。</p>
 * @version	1.0
 * @author liumk
 */
public class Tile {
    /**
     * 横向瓦片数
     */
    private int x;
    /**
     * 纵向瓦片数
     */
    private int y;
    /**
     * 级别
     */
    private int z;

    /**
     * 根据给定参数构造Tile的新实例
     * @param x 横向瓦片数
     * @param y 纵向瓦片数
     */
    public Tile(int x,int y){
        this.x=x;
        this.y=y;
    }
    /**
     * 根据给定参数构造Tile的新实例
     * @param x 横向瓦片数
     * @param y 纵向瓦片数
     * @param z 级别
     */
    public Tile(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
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
    public int getZ() {
        return z;
    }
    public void setZ(int z) {
        this.z = z;
    }
    /**
     * 将Tile对象转换为字符串
     */
    public String toString(){
        return x+","+y+","+z;
    }
}
