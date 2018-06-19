package com.room.mokeys.net;

import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by wanglei on 2016/12/31.
 */

public class Api {
    public static final String API_BASE_URL = "http://gank.io/api/";

    private static GankService gankService;

    public static GankService getGankService() {
        if (gankService == null) {
            synchronized (Api.class) {
                if (gankService == null) {
                    gankService = XApi.getInstance().getRetrofit(API_BASE_URL, true).create(GankService.class);
                }
            }
        }
        return gankService;
    }

//    public static final String MOKEYS_BASE_URL = "http://47.104.204.27:8777/";
public static final String MOKEYS_BASE_URL = "http://47.104.204.27";
    private static MokeysLoginService mMokeysLoginService;

    public static MokeysLoginService getMokeysLoginService() {
        if (mMokeysLoginService == null) {
            synchronized (Api.class) {
                if (mMokeysLoginService == null) {
                    mMokeysLoginService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysLoginService.class);
                }
            }
        }
        return mMokeysLoginService;
    }
    //获取验证码
    private static MokeysVerificationService mMokeysVerificationService;

    public static MokeysVerificationService getMokeysVerificationService() {
        if (mMokeysVerificationService == null) {
            synchronized (Api.class) {
                if (mMokeysLoginService == null) {
                    mMokeysVerificationService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysVerificationService.class);
                }
            }
        }
        return mMokeysVerificationService;
    }
    //登出
    private static MokeysLogoutService mMokeysLogoutService;

    public static MokeysLogoutService getMokeysLogoutService() {
        if (mMokeysLogoutService == null) {
            synchronized (Api.class) {
                if (mMokeysLoginService == null) {
                    mMokeysLogoutService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysLogoutService.class);
                }
            }
        }
        return mMokeysLogoutService;
    }

//预定列表
    private static MokeysReserverRoomService mMokeysReserverRoomService;

    public static MokeysReserverRoomService getMokeysReserverRoomService() {
        if (mMokeysReserverRoomService == null) {
            synchronized (Api.class) {
                if (mMokeysReserverRoomService == null) {
                    mMokeysReserverRoomService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysReserverRoomService.class);
                }
            }
        }
        return mMokeysReserverRoomService;
    }

    private static MokeysUserInfoService mMokeysUserInfoService;

    public static MokeysUserInfoService getMokeysUserInfoService() {
        if (mMokeysUserInfoService == null) {
            synchronized (Api.class) {
                if (mMokeysUserInfoService == null) {
                    mMokeysUserInfoService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysUserInfoService.class);
                }
            }
        }
        return mMokeysUserInfoService;
    }

    private static MokeysAccessTokenInfoService mMokeysAccessTokenInfoService;

    public static MokeysAccessTokenInfoService getMokeysAccessTokenInfoService() {
        if (mMokeysAccessTokenInfoService == null) {
            synchronized (Api.class) {
                if (mMokeysAccessTokenInfoService == null) {
                    mMokeysAccessTokenInfoService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysAccessTokenInfoService.class);
                }
            }
        }
        return mMokeysAccessTokenInfoService;
    }

    private static MokeysAuthenticationService mMokeysAuthenticationService;
    public static MokeysAuthenticationService getMokeysAuthenticationService() {
        if (mMokeysAuthenticationService == null) {
            synchronized (Api.class) {
                if (mMokeysAuthenticationService == null) {
                    mMokeysAuthenticationService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysAuthenticationService.class);
                }
            }
        }
        return mMokeysAuthenticationService;
    }

    private static MokeysWeChatPayService mMokeysWeChatPayService;
    public static MokeysWeChatPayService getMokeysWeChatPayService() {
        if (mMokeysWeChatPayService == null) {
            synchronized (Api.class) {
                if (mMokeysWeChatPayService == null) {
                    mMokeysWeChatPayService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysWeChatPayService.class);
                }
            }
        }
        return mMokeysWeChatPayService;
    }

    private static MokeysRoomListService mMokeysRoomListService;
    public static MokeysRoomListService getMokeysRoomListService() {
        if (mMokeysRoomListService == null) {
            synchronized (Api.class) {
                if (mMokeysRoomListService == null) {
                    mMokeysRoomListService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysRoomListService.class);
                }
            }
        }
        return mMokeysRoomListService;
    }

    private static MokeysEstateListService mMokeysEstateListService;
    public static MokeysEstateListService getMokeysEstateListService() {
        if (mMokeysEstateListService == null) {
            synchronized (Api.class) {
                if (mMokeysEstateListService == null) {
                    mMokeysEstateListService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysEstateListService.class);
                }
            }
        }
        return mMokeysEstateListService;
    }
    //充值押金接口
    private static MokeyscashPledgeService mMokeyscashPledgeService;
    public static MokeyscashPledgeService getMokeyscashPledgeService() {
        if (mMokeyscashPledgeService == null) {
            synchronized (Api.class) {
                if (mMokeyscashPledgeService == null) {
                    mMokeyscashPledgeService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeyscashPledgeService.class);
                }
            }
        }
        return mMokeyscashPledgeService;
    }

    //定金配置接口
    private static MokeysDepositConfigService mMokeysDepositConfigService;
    public static MokeysDepositConfigService getMokeysDepositConfigService() {
        if (mMokeysDepositConfigService == null) {
            synchronized (Api.class) {
                if (mMokeysDepositConfigService == null) {
                    mMokeysDepositConfigService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysDepositConfigService.class);
                }
            }
        }
        return mMokeysDepositConfigService;
    }

    //交定金接口
    private static MokeysDepositService mMokeysDepositService;
    public static MokeysDepositService getMokeysDepositService() {
        if (mMokeysDepositService == null) {
            synchronized (Api.class) {
                if (mMokeysDepositService == null) {
                    mMokeysDepositService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysDepositService.class);
                }
            }
        }
        return mMokeysDepositService;
    }
    //获取合同接口
    private static MokeysContratService mMokeysContratService;
    public static MokeysContratService getMokeysContratService() {
        if (mMokeysContratService == null) {
            synchronized (Api.class) {
                if (mMokeysContratService == null) {
                    mMokeysContratService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysContratService.class);
                }
            }
        }
        return mMokeysContratService;
    }
    //获取签约房子
    private static MokeysContractRoomService mMokeysContractRoomService;
    public static MokeysContractRoomService getMokeysContractRoomService() {
        if (mMokeysContractRoomService == null) {
            synchronized (Api.class) {
                if (mMokeysContractRoomService == null) {
                    mMokeysContractRoomService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysContractRoomService.class);
                }
            }
        }
        return mMokeysContractRoomService;
    }
    //上传图片文件
    private static MokeysUploadFileService mMokeysUploadFileService;
    public static MokeysUploadFileService getMokeysUploadFileService() {
        if (mMokeysUploadFileService == null) {
            synchronized (Api.class) {
                if (mMokeysUploadFileService == null) {
                    mMokeysUploadFileService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysUploadFileService.class);
                }
            }
        }
        return mMokeysUploadFileService;
    }
    //充值接口
    private static MokeysRechargeService mMokeysRechargeService;
    public static MokeysRechargeService getMokeysRechargeService() {
        if (mMokeysRechargeService == null) {
            synchronized (Api.class) {
                if (mMokeysRechargeService == null) {
                    mMokeysRechargeService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysRechargeService.class);
                }
            }
        }
        return mMokeysRechargeService;
    }

    //支付房租
    private static MokeysPayConstractService mMokeysPayConstractService;
    public static MokeysPayConstractService getMokeysPayConstractService() {
        if (mMokeysPayConstractService == null) {
            synchronized (Api.class) {
                if (mMokeysPayConstractService == null) {
                    mMokeysPayConstractService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysPayConstractService.class);
                }
            }
        }
        return mMokeysPayConstractService;
    }
    private static MokeysUnlockService mMokeysUnlockService;
    public static MokeysUnlockService getMokeysUnlockService() {
        if (mMokeysUnlockService == null) {
            synchronized (Api.class) {
                if (mMokeysUnlockService == null) {
                    mMokeysUnlockService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysUnlockService.class);
                }
            }
        }
        return mMokeysUnlockService;
    }
//判断是否芝麻认证通过
    private static MokeysCheckAliAuthService mMokeysCheckAliAuthService;
    public static MokeysCheckAliAuthService getMokeysCheckAliAuthService() {
        if (mMokeysCheckAliAuthService == null) {
            synchronized (Api.class) {
                if (mMokeysCheckAliAuthService == null) {
                    mMokeysCheckAliAuthService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysCheckAliAuthService.class);
                }
            }
        }
        return mMokeysCheckAliAuthService;
    }

    //获取芝麻认证URL
    private static MokeysGetAliAuthUrlService mMokeysGetAliAuthUrlService;
    public static MokeysGetAliAuthUrlService getMokeysGetAliAuthUrlService() {
        if (mMokeysGetAliAuthUrlService == null) {
            synchronized (Api.class) {
                if (mMokeysGetAliAuthUrlService == null) {
                    mMokeysGetAliAuthUrlService = XApi.getInstance().getRetrofit(MOKEYS_BASE_URL, true).create(MokeysGetAliAuthUrlService.class);
                }
            }
        }
        return mMokeysGetAliAuthUrlService;
    }

}
