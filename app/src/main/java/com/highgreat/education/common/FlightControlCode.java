package com.highgreat.education.common;

public class FlightControlCode {


    //10: throw fly. 11：one key circle entry. 12：one key circle exit. 13. 360 spin. 14. flip mode. 15. jump mode entry. 16. jump mode exit

    public static final short MODE_THROW_FLY = 10;//抛飞模式
    public static final short MODE_THROW_FLY_EXIT = 11;//退出抛飞模式
    public static final short MODE_CIRCLE = 12;//一键环绕
    public static final short MODE_CIRCLE_EXIT = 13;//一键环绕退出
    public static final short MODE_360 = 14;//360旋转
    public static final short MODE_360_EXIT = 15;//360旋转
    public static final short MODE_JUMP = 16;//弹跳模式
    public static final short MODE_JUMP_EXIT = 17;//弹跳模式退出

    public static final short MODE_FLIP_RIGHT = 18;//翻滚模式
    public static final short MODE_FLIP_LEFT = 19;//翻滚模式
    public static final short MODE_FLIP_BACK = 20;//翻滚模式
    public static final short MODE_FLIP_FORWARD = 21;//翻滚模式

}
