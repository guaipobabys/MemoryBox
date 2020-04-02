package com.pobaby.memorybox.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pobaby.common.utils.common.FileUtils;
import com.pobaby.common.utils.common.PathUtils;
import com.pobaby.common.utils.common.PrefUtils;
import com.pobaby.common.utils.view.ToastUtils;
import com.pobaby.memorybox.R;
import com.pobaby.memorybox.db.DaoMaster;
import com.pobaby.memorybox.db.DaoSession;
import com.pobaby.memorybox.db.help.DatabaseContext;
import com.pobaby.memorybox.db.help.XDaoMaster;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.io.File;
import java.io.IOException;

/**
 * 自定义应用程序，配置各种工具信息
 *
 * @author chenqh
 * @email 403167386@qq.com
 * @created 2017/12/12 16:23
 */
public class MainApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initLibs();
    }

    public static Context getContext() {
        return context;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorLabel);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    private void initLibs() {
        PrefUtils.init(getContext());
        ToastUtils.init(getContext());
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    public static void initGreenDao() {
        DatabaseContext databaseContext = new DatabaseContext(getContext());
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(databaseContext, AppConfig.DB_LOCAL_NAME);
        XDaoMaster helper = new XDaoMaster(databaseContext, AppConfig.DB_LOCAL_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }

}
