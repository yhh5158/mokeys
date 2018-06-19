package com.room.mokeys.ui.contractroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.maps.ui.maps.MokeysMainActivity;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysContractDetailInfo;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysReservesInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.ui.MokeysSignatureWebActivity;
import com.room.mokeys.ui.reserveroom.ReserverRoomInfoAdapter;
import com.room.mokeys.widget.CustomTitleView;
import com.room.mokeys.widget.LoadmoreView;
import com.room.mokeys.widget.StateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by yhh5158 on 2017/6/8.
 */

public class ContractRoomAcitivity extends XActivity {
    @BindView(R.id.contract_room_detail_contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.contract_room_custom_title)
    CustomTitleView mCustomTitleVie;
    StateView errorView;
    @BindView(R.id.contract_hourse_lin)
    RelativeLayout mHourseLin;
    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        loadData(0,10);
//        getRoomList((String)SPUtil.get(context,Constants.MOKEYS_A,""),longitude,latitude);
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_contract_room;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.title_right_icon,R.id.backlin,R.id.contract_hourse_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_icon:
                break;
            case R.id.backlin:
                finish();
                break;
            case R.id.contract_hourse_lin:
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setClass(this, MokeysMainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public void initAdapter(){
        setLayoutManager(contentLayout.getRecyclerView());

        contentLayout.getRecyclerView()
                .setAdapter(getAdapter());
        contentLayout.getRecyclerView()
                .setOnRefreshAndLoadMoreListener(new XRecyclerView.OnRefreshAndLoadMoreListener() {
                    @Override
                    public void onRefresh() {
//                        getP().loadData(getType(), 1);
                        loadData(0,10);
                    }

                    @Override
                    public void onLoadMore(int page) {
//                        getP().loadData(getType(), page);
//                        loadData(adapter.getItemCount(),10);
                    }
                });
        contentLayout.getRecyclerView().horizontalDivider(R.color.comment_divider_color,R.dimen.devider_width);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        if (errorView == null) {
            errorView = new StateView(context);
        }
        contentLayout.errorView(errorView);
        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.emptyView(View.inflate(this, R.layout.view_constract_empty, null));
//        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().loadMoreFooterView(new LoadmoreView(context));
        contentLayout.showLoading();
    }
    List<MokeysContractInfo> mlist = new ArrayList<MokeysContractInfo>();
    public void loadData(final int page, final int number) {
        Log.d("yhh","ContractRoomAcitivity loaddata " );
            Api.getMokeysContractRoomService().getData((String)SPUtil.get(context,Constants.MOKEYS_A,""))
                    .compose(XApi.<MokeysListModel<MokeysContractInfo>>getApiTransformer())
                    .compose(XApi.<MokeysListModel<MokeysContractInfo>>getScheduler())
                    .compose(this.<MokeysListModel<MokeysContractInfo>>bindToLifecycle())
                    .subscribe(new ApiSubscriber<MokeysListModel<MokeysContractInfo>>() {
                        @Override
                        protected void onFail(NetError error) {
                            showError(error);
                            Log.d("yuhh","onFail!!!" + error);
                        }
                        @Override
                        public void onNext(MokeysListModel<MokeysContractInfo> gankResults) {
                            Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                            if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                                MokeysEstateDetailInfo info =  gankResults.getData().get(0);
//                                mlist = info.getHouses();
//                                mCustomTitleVie.setTitle(gankResults.getData().get(0).getEstate_name());
                                mlist = gankResults.getData();
                                showData(page,mlist);
                            }else{
//                                getvDelegate().toastShort(gankResults.getMsg());
                                Constants.handErrorCode(gankResults.getCode(),context);
                                showData(page,mlist);
                            }
                        }
                    });


    }
    public void showError(NetError error) {
        if (error != null) {
            switch (error.getType()) {
                case NetError.ParseError:
                    errorView.setMsg("数据解析异常");
                    break;

                case NetError.AuthError:
                    errorView.setMsg("身份验证异常");
                    break;
                case NetError.BusinessError:
                    errorView.setMsg("业务异常");
                    break;

                case NetError.NoConnectError:
                    errorView.setMsg("网络无连接");
                    break;

                case NetError.NoDataError:
                    errorView.setMsg("数据为空");
                    break;

                case NetError.OtherError:
                    errorView.setMsg("其他异常");
                    break;
            }
            contentLayout.showError();
        }
    }

//    public void showData(final int page, final ArtistBaseModel<CollectionInfo> model) {
//        contentLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (page > 0) {
//                    getAdapter().addData(model.getData());
//                } else {
//                    getAdapter().setData(model.getData());
//                }
//
//                if (getAdapter().getItemCount() < 1) {
//                    contentLayout.showEmpty();
//                    return;
//                }
//            }
//        },400L);
//
//    }
public void showData(final int page, final List<MokeysContractInfo> model) {
    contentLayout.postDelayed(new Runnable() {
        @Override
        public void run() {
            if (page > 0) {
                mHourseLin.setVisibility(View.GONE);
                getAdapter().addData(model);
            } else {
                mHourseLin.setVisibility(View.GONE);
                getAdapter().setData(model);
            }

            if (getAdapter().getItemCount() < 1) {
                contentLayout.showEmpty();
                mHourseLin.setVisibility(View.VISIBLE);
                return;
            }
        }
    },400L);

}
    ContractRoomInfoAdapter adapter;

    public SimpleRecAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ContractRoomInfoAdapter(context);
            adapter.setRecItemClick(new RecyclerItemCallback<MokeysContractInfo, ContractRoomInfoAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position,MokeysContractInfo model, int tag, ContractRoomInfoAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
//                    Toast.makeText(context,model.getColumnDetail(),Toast.LENGTH_SHORT).show();
//                    switch (tag) {
//                        case HomeAdapter.TAG_VIEW:
//                            WebActivity.launch(context, model.getUrl(), model.getDesc());
//                            break;
//                    }
//                    if(model.getActivity()!=null){
//                        YiTangWebActivity.launch(context, model.getActivity().getPagePath(), model.getActivity().getTitle(),model.getActivity(),false,"",null);
//                    }else if(model.getColumn()!=null){
//                        Intent mIntent = new Intent();
//                        mIntent.setClass(context, TeacherColumnDetailActivity.class);
//                        mIntent.putExtra("column",model.getColumn());
//                        startActivity(mIntent);
//                    }else{
//
//                    }

                }
            });
        }
        adapter.setOnClickListener(new ContractRoomInfoAdapter.OnButtonClickListener() {
            @Override
            public void onClick(MokeysContractInfo item) {
//                getContrat((String) SPUtil.get(context, Constants.MOKEYS_A,""),item.getHid(),item.getRid(),1);
                String url = item.getSigned_contract() + "&token=" + SPUtil.get(context, Constants.MOKEYS_A, "");
                MokeysSignatureWebActivity.launch(context,url,"合同",item,true);
            }
        });
        return adapter;
    }
//    private void getContrat(String token,String horse_id,String room_id,int type){
//        Api.getMokeysContratService().getData(token,horse_id,room_id,type)
//                .compose(XApi.<MokeysBaseModel<MokeysContractDetailInfo>>getApiTransformer())
//                .compose(XApi.<MokeysBaseModel<MokeysContractDetailInfo>>getScheduler())
//                .compose(this.<MokeysBaseModel<MokeysContractDetailInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysBaseModel<MokeysContractDetailInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//                        Log.d("yhh","onFail = "+error.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(MokeysBaseModel<MokeysContractDetailInfo> gankResults) {
//                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                            MokeysContractInfo info =  gankResults.getData().getContract();
//                            String url = gankResults.getData().getContract_url() + "&token=" + SPUtil.get(context, Constants.MOKEYS_A, "");
//                            MokeysSignatureWebActivity.launch(context,url,"合同",info);
//                        }else{
////                            getvDelegate().toastShort(gankResults.getMsg());
//                            Constants.handErrorCode(gankResults.getCode(),context);
//                        }
//                    }
//                });
//    }
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);

    }
}
