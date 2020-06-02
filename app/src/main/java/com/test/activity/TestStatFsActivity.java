package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.TextView;

import com.test.R;
import com.test.util.DiskUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/5/28<br>
 * Time: 20:36<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class TestStatFsActivity extends BaseActivity {

    private TextView mTv;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestStatFsActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_fs_stat_test;
    }

    @Override
    protected void initView() {
        mTv = findViewById(R.id.afst_tv);
    }

    @Override
    protected void initData() {
        StringBuilder sb = new StringBuilder();
        String packagePath = getFilesDir().getParent();
        sb
                .append("internalPath              ").append('=').append(Environment.getRootDirectory().getAbsolutePath()).append('\n')
                .append("internalTotalSizeFormatted").append('=').append(DiskUtils.bytesToHuman(DiskUtils.internalTotalBytes())).append('\n')
                .append("internalBusySizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.internalBusyBytes())).append('\n')
                .append("internalFreeSizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.internalFreeBytes())).append('\n')
//                .append("internalTotalSize         ").append('=').append(DiskUtils.internalTotalBytes()).append('\n')
//                .append("internalBusySize          ").append('=').append(DiskUtils.internalBusyBytes()).append('\n')
//                .append("internalFreeSize          ").append('=').append(DiskUtils.internalFreeBytes()).append('\n')
                .append("\n\n")
                .append("ExternalPath              ").append('=').append(Environment.getExternalStorageDirectory().getAbsolutePath()).append('\n')
                .append("externalTotalSizeFormatted").append('=').append(DiskUtils.bytesToHuman(DiskUtils.externalTotalBytes())).append('\n')
                .append("externalBusySizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.externalBusyBytes())).append('\n')
                .append("externalFreeSizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.externalFreeBytes())).append('\n')
//                .append("externalTotalSize         ").append('=').append(DiskUtils.externalTotalBytes()).append('\n')
//                .append("externalBusySize          ").append('=').append(DiskUtils.externalBusyBytes()).append('\n')
//                .append("externalFreeSize          ").append('=').append(DiskUtils.externalFreeBytes()).append('\n')
                .append("\n\n")
                .append("packagePath              ").append('=').append(packagePath).append('\n')
                .append("packageTotalSizeFormatted").append('=').append(DiskUtils.bytesToHuman(DiskUtils.getTotalBytes(packagePath))).append('\n')
                .append("packageBusySizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.getBusyBytes(packagePath))).append('\n')
                .append("packageFreeSizeFormatted ").append('=').append(DiskUtils.bytesToHuman(DiskUtils.getFreeBytes(packagePath))).append('\n')
//                .append("packageTotalSize         ").append('=').append(DiskUtils.getTotalBytes(packagePath)).append('\n')
//                .append("packageBusySize          ").append('=').append(DiskUtils.getBusyBytes(packagePath)).append('\n')
//                .append("packageFreeSize          ").append('=').append(DiskUtils.getFreeBytes(packagePath)).append('\n')
        ;
        mTv.setText(sb.toString());
    }

    @Override
    protected void initListener() {

    }
}
