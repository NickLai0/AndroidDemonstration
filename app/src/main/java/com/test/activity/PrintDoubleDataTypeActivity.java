package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.test.R;
import com.test.util.AppUtils;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/2/28<br>
 * Time: 10:25<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public class PrintDoubleDataTypeActivity extends BaseActivity {

    private TextView mTv;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, PrintDoubleDataTypeActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.activity_type_data_double_print;
    }

    @Override
    protected void initView() {
        mTv = (TextView) findViewById(R.id.atddp_tv);
    }

    @Override
    protected void initData() {
        StringBuilder sb = new StringBuilder();
        sb.append("the following demo is base data type of double plus \"\" situation:").append('\n');
        double a = 3;
        double b = 3.1;
        double c = 3.14;
        double d = 3.141;
        double e = 3.1415;
        double f = 3.14159;
        sb.append("double a = 3       -> ").append(a + "").append("\n");
        sb.append("double b = 3.1     -> ").append(b + "").append("\n");
        sb.append("double c = 3.14    -> ").append(c + "").append("\n");
        sb.append("double d = 3.141   -> ").append(d + "").append("\n");
        sb.append("double e = 3.1415  -> ").append(e + "").append("\n");
        sb.append("double f = 3.14159 -> ").append(f + "").append("\n");
        sb.append("-------------------------------------------------------------").append("\n\n");
        sb.append("the following demo is base data type of double print how many decimal point by calculate:").append('\n');

        sb.append("doubleToString(a) : ").append(doubleToString(a)).append('\n')
                .append("doubleToString(b) : ").append(doubleToString(b)).append('\n')
                .append("doubleToString(c) : ").append(doubleToString(c)).append('\n')
                .append("doubleToString(d) : ").append(doubleToString(d)).append('\n')
                .append("doubleToString(e) : ").append(doubleToString(e)).append('\n')
                .append("doubleToString(f) : ").append(doubleToString(f)).append('\n')
        ;

        mTv.setText(sb.toString());
    }

    /**
     * double convert to string that like 1.0 -> 1, 1.10 -> 1.0 or 3.140 -> 3.14
     *
     * @param d double data type
     * @return remove last decimal point zero(s)
     */
    public static String doubleToString(double d) {
        String[] temp = (d + "").split("\\.");
        String result = null;
        if (temp.length == 1) {
            result = temp[0];
        } else {
            int zeroCount = 0;
            String decimalPoints = temp[1];
            for (int index = decimalPoints.length() - 1; index >= 0; index--) {
                if (decimalPoints.charAt(index) == '0') {
                    zeroCount++;
                } else {
                    break;
                }
            }
            if (zeroCount == 0) {
                result = temp[0] + "." + temp[1];
            } else if (decimalPoints.length() == zeroCount) {
                result = temp[0];
            } else if (decimalPoints.length() > zeroCount) {
                result = temp[0] + "." + temp[1].substring(0, temp[1].length() - zeroCount);
            } else {
                //Exception situation.Normally it's never go to this.
            }
        }
        return result;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
