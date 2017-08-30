package com.ljw.uitest.main;

/**
 * Created by ljw on 2017/8/30.
 */

public class JumpEntity {

    public String btnText = "";
    public Class jumpClass = MainActivity.class;

    public JumpEntity() {
    }

    public JumpEntity(String btnText, Class jumpClass) {
        this.btnText = btnText;
        this.jumpClass = jumpClass;
    }
}
