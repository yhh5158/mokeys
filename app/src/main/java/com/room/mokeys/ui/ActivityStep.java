package com.room.mokeys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.room.mokeys.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by yhh5158 on 2017/5/25.
 */

public class ActivityStep extends XActivity {
    @BindView(R.id.stepview)
    HorizontalStepView stepView;
    @Override
    public void initData(Bundle savedInstanceState) {
            initStepView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_step;
    }
private void initStepView(){
    //-----------------------------this data is example and you can also get data from server-----------------------------
    List<StepBean> stepsBeanList = new ArrayList<>();
    StepBean stepBean0 = new StepBean("手机认证",1);
    StepBean stepBean1 = new StepBean("实名认证",0);
    StepBean stepBean2 = new StepBean("充值押金",-1);
    StepBean stepBean3 = new StepBean("开始看房",-1);
    stepsBeanList.add(stepBean0);
    stepsBeanList.add(stepBean1);
    stepsBeanList.add(stepBean2);
    stepsBeanList.add(stepBean3);
    //-----------------------------this data is example and you can also get data from server-----------------------------

    stepView.setStepViewTexts(stepsBeanList)
            .setTextSize(16)//set textSize
            .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
            .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
            .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), android.R.color.white))//设置StepsView text完成线的颜色
            .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
            .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
            .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
            .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
}
    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.stepbutton,R.id.backlin})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.stepbutton:
                Intent mIntent = new Intent(context,SignatureActivity.class);
                startActivity(mIntent);
                break;
            case R.id.backlin:
                finish();
                break;
            default:
                break;
        }
    }
}
