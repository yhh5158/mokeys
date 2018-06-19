package com.room.mokeys.testjava;

import android.util.Log;

/**
 * Created by yhh5158 on 2018/2/6.
 */

public class TestC {
    TestB mTestB = new TestB();

    public TestB getmTestB() {
        return mTestB;
    }

    public void setmTestB(TestB mTestB) {
        this.mTestB = mTestB;
    }

    public void setClassA(){
//        mTestB.setmTestA(null);
//        Log.d("yhh", "ClassA: "+mTestB.getmTestA());
        Log.d("yhh","start class a = " +mTestB.getmTestA().getA());
        new TestA().setA(100);
        Log.d("yhh","class a = " +mTestB.getmTestA().getA());
    }
//    public void testWhile(){
//        ko:
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                System.out.println("i=" + i + ",j=" + j);
//                if (j == 5)
//                    break ko;
//            }
//        }
//    }
}
