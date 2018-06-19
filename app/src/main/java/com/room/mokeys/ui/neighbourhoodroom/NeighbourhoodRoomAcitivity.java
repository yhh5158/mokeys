package com.room.mokeys.ui.neighbourhoodroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.maps.ui.maps.MapsFragment;
import com.room.mokeys.model.MokeysEstateDetailInfo;
import com.room.mokeys.model.MokeysEstateInfo;
import com.room.mokeys.model.MokeysHouseInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.NeighbourhoodRoomInfo;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
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

public class NeighbourhoodRoomAcitivity extends XActivity {
    double longitude ;
    double latitude ;
    @BindView(R.id.neighbourhood_room_detail_contentLayout)
    XRecyclerContentLayout contentLayout;
    @BindView(R.id.neighbourhood_room_detail_custom_title)
    CustomTitleView mCustomTitleVie;
    StateView errorView;
    @Override
    public void initData(Bundle savedInstanceState) {
        initAdapter();
        longitude = getIntent().getDoubleExtra("x",0);
        latitude = getIntent().getDoubleExtra("y",0);
        loadData(0,10);
//        getRoomList((String)SPUtil.get(context,Constants.MOKEYS_A,""),longitude,latitude);
    }
//    private void getRoomList(String token,double x,double y){
//        Api.getMokeysRoomListService().getData(token,x,y)
//                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getApiTransformer())
//                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getScheduler())
//                .compose(this.<MokeysListModel<VerificationCodeInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysListModel<VerificationCodeInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//
//                        Log.d("yuhh","onFail!!!" + error);
//                    }
//                    @Override
//                    public void onNext(MokeysListModel<VerificationCodeInfo> gankResults) {
//                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
//                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
//                        }
//                    }
//                });
//    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_neighbourhood_room;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.title_right_icon,R.id.backlin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_icon:
                break;
            case R.id.backlin:
                finish();
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
        contentLayout.emptyView(View.inflate(this, R.layout.view_empty, null));
//        contentLayout.getRecyclerView().useDefLoadMoreView();
        contentLayout.getRecyclerView().loadMoreFooterView(new LoadmoreView(context));
        contentLayout.showLoading();
    }
    List<MokeysHouseInfo> mlist = new ArrayList<MokeysHouseInfo>();
    public void loadData(final int page, final int number) {
        Log.d("yhh","mytestloadData " );
//        Api.getCollectionService().getData(page,10)
//                .compose(XApi.<ArtistBaseModel<CollectionInfo>>getApiTransformer())
//                .compose(XApi.<ArtistBaseModel<CollectionInfo>>getScheduler())
//                .compose(this.<ArtistBaseModel<CollectionInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<ArtistBaseModel<CollectionInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//                        Log.d("yhh","error type = " +error.getType() + " error = "+ error.getMessage());
//                        showError(error);
//                    }
//
//                    @Override
//                    public void onNext(final ArtistBaseModel<CollectionInfo> gankResults) {
//
//                        showData(page,gankResults);
//                    }
//                });
            Api.getMokeysRoomListService().getData((String)SPUtil.get(context,Constants.MOKEYS_A,""),longitude,latitude)
                    .compose(XApi.<MokeysListModel<MokeysEstateDetailInfo>>getApiTransformer())
                    .compose(XApi.<MokeysListModel<MokeysEstateDetailInfo>>getScheduler())
                    .compose(this.<MokeysListModel<MokeysEstateDetailInfo>>bindToLifecycle())
                    .subscribe(new ApiSubscriber<MokeysListModel<MokeysEstateDetailInfo>>() {
                        @Override
                        protected void onFail(NetError error) {
                            showError(error);
                            Log.d("yuhh","onFail!!!" + error);
                        }
                        @Override
                        public void onNext(MokeysListModel<MokeysEstateDetailInfo> gankResults) {
                            Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                            if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                                MokeysEstateDetailInfo info =  gankResults.getData().get(0);
                                mlist = info.getHouses();
                                mCustomTitleVie.setTitle(gankResults.getData().get(0).getEstate_name());
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
public void showData(final int page, final List<MokeysHouseInfo> model) {
    contentLayout.postDelayed(new Runnable() {
        @Override
        public void run() {
            if (page > 0) {
                getAdapter().addData(model);
            } else {
                getAdapter().setData(model);
            }

            if (getAdapter().getItemCount() < 1) {
                contentLayout.showEmpty();
                return;
            }
        }
    },400L);

}
    NeighbourhoodRoomInfoAdapter adapter;
    public SimpleRecAdapter getAdapter() {
        if (adapter == null) {
            adapter = new NeighbourhoodRoomInfoAdapter(context);
            adapter.setRecItemClick(new RecyclerItemCallback<MokeysHouseInfo, NeighbourhoodRoomInfoAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position,MokeysHouseInfo model, int tag, NeighbourhoodRoomInfoAdapter.ViewHolder holder) {
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
        return adapter;
    }
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.verticalLayoutManager(context);

    }
}
