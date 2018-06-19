package com.room.mokeys.testjava;

/**
 * Created by yhh5158 on 2018/2/6.
 */

public class TestB {
    TestA mTestA = new TestA();
    int m = 5;
    public TestA getmTestA() {
        return mTestA;
    }

    public void setmTestA(TestA mTestA) {
        this.mTestA = mTestA;
    }
    public  void setA(){
        mTestA.setA(m);
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }
}
