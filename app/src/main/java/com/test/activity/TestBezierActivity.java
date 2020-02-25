package com.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bezier.bean.AnimationInfo;
import com.bezier.bean.Coordinate;
import com.bezier.helper.CoordinateAnimationHelper;
import com.bezier.util.BezierCurveUtils;
import com.bezier.view.BezierCurveView;
import com.cd.CountdownInfo;
import com.cd.OnCountdownListener;
import com.test.R;
import com.test.app.CountdownManager;
import com.test.app.LogMgr;
import com.test.util.ActivityUtil;
import com.test.util.ScreenUtils;

import java.util.ArrayList;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2019/12/26<br>
 * Time: 18:05<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */

/**
 * b1(t) = (1-t)p0 + tp1, t=[0,1]
 * <p>
 * <p>
 * b2(t)
 * = (1-t)((1-t)p0 + tp1)  +  t((1-t)p1 + tp2)
 * = (1-t)^2p0 + (1-t)tp1 + (1-t)tp1 + t^2p2
 * = (1-t)^2p0 + 2t(1-t)p1 + t^2p2
 * 即 b2(t) = (1-t)^2p0 + 2t(1-t)p1 + t^2p2, t=[0,1]
 * <p>
 * <p>
 * b3(t)
 * = (1-t)( (1-t) ( (1-t)p0+tp1) + t( (1-t)p1 + tp2 ) ) + t( (1-t) (( 1-t)p1 + tp2 ) + t( (1-t)p2+tp3) )
 * = (1-t)((1-t)^2p0+(1-t)tp1 + (1-t)tp1 + t^2p2) + t((1-t)^2p1 + (1-t)tp2 + (1-t)tp2 + t^2p3 )
 * = (1-t)^3p0 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)t^2p2 + t^3p3
 * = (1-t)^3p0 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)^2tp1 + (1-t)t^2p2 + (1-t)t^2p2 + (1-t)t^2p2 + t^3p3
 * = (1-t)^3p0 + 3t(1-t)^2p1 + 3t^2(1-t)p2 + t^3p3
 * 即 b3(t) = (1-t)^3p0 + 3t(1-t)^2p1 + 3t^2(1-t)p2 + t^3p3, t = [0,1]
 */
public class TestBezierActivity extends BaseActivity implements CoordinateAnimationHelper.OnAnimationRefreshListener {

    private LinearLayout mLlTestView;

    private View mBtnTestBezierAlgorithm;
    private View mBtnTestBezierAlgorithmB3;
    private Button mBtnShowOrHideTheTestView;
    private Button mBtnShowOrHideTheCurveView;
    private Button mBtnDrawBezier;
    private Button mBtnSendAHeart;

    private BezierCurveView mBcv;
    private ImageView mIvSmileHeartGray;
    private ImageView mIvNormalHeartRed;
    private ArrayList<Coordinate> mCoordinateList;
    private FrameLayout mFlRootContainer;
    private CoordinateAnimationHelper mCoordinateAnimationHelper;
    private CountdownInfo mCi;

    public static void start(Activity a) {
        a.startActivity(new Intent(a, TestBezierActivity.class));
    }

    @Override
    protected int provideLayoutId() {
        ActivityUtil.immersiveNavigation(this);
        return R.layout.activity_bezier_test;
    }

    @Override
    protected void initView() {
        mFlRootContainer = findViewById(R.id.abt_fl_root_container);
        mBcv = findViewById(R.id.abt_bcv);
        mLlTestView = findViewById(R.id.abt_ll_test_view);
        mBtnTestBezierAlgorithm = findViewById(R.id.abt_btn_test_bezier_algorithm);
        mBtnTestBezierAlgorithmB3 = findViewById(R.id.abt_btn_test_bezier_algorithm_b3);
        mBtnShowOrHideTheTestView = findViewById(R.id.abt_btn_show_or_hide_the_test_view);
        mBtnDrawBezier = findViewById(R.id.abt_btn_draw_bezier);
        mBtnSendAHeart = findViewById(R.id.abt_btn_send_a_heart);
        mBtnShowOrHideTheCurveView = findViewById(R.id.abt_btn_show_or_hide_the_curve_view);
        mIvSmileHeartGray = findViewById(R.id.abt_iv_smile_heart_gray);
        mIvNormalHeartRed = findViewById(R.id.abt_iv_normal_heart_red);
    }

    private int mHeartWidth;
    private int mHeartHeight;

    @Override
    protected void initData() {
        initBezierRelated();
        mCoordinateAnimationHelper = new CoordinateAnimationHelper();
        mCoordinateAnimationHelper.init();
        mCoordinateAnimationHelper.setFrameFrequency(10);
        mFlRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Coordinate start = mCoordinateList.get(0);
                mHeartWidth = mIvSmileHeartGray.getWidth();
                mHeartHeight = mIvNormalHeartRed.getHeight();
                mIvSmileHeartGray.setX(start.x - mHeartWidth / 2);
                mIvSmileHeartGray.setY(start.y);
                mIvNormalHeartRed.setX(start.x - mHeartHeight / 2);
                mIvNormalHeartRed.setY(start.y);
                LogMgr.i().logT(TAG, "onGlobalLayout -> start=" + start);
                mFlRootContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    protected void initListener() {
        mBtnTestBezierAlgorithm.setOnClickListener(this);
        mBtnTestBezierAlgorithmB3.setOnClickListener(this);
        mBtnShowOrHideTheTestView.setOnClickListener(this);
        mBtnDrawBezier.setOnClickListener(this);
        mBtnShowOrHideTheCurveView.setOnClickListener(this);
        mBtnSendAHeart.setOnClickListener(this);
        mIvSmileHeartGray.setOnClickListener(this);
    }

    private static final int START_OFFSET = 100;
    private static final int END_OFFSET = 300;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abt_btn_test_bezier_algorithm:
                testBesierAlgorithm();
                break;
            case R.id.abt_btn_test_bezier_algorithm_b3:
                testBesierAlgorithmB3();
                break;

            case R.id.abt_btn_show_or_hide_the_test_view:
                mLlTestView.setVisibility(mLlTestView.isShown() ? View.GONE : View.VISIBLE);
                break;

            case R.id.abt_btn_draw_bezier:
                initBezierRelated();
                mBcv.drawCurve(mCoordinateList);
                break;

            case R.id.abt_btn_show_or_hide_the_curve_view:
                mBcv.setVisibility(mBcv.isShown() ? View.GONE : View.VISIBLE);
                break;

            case R.id.abt_iv_smile_heart_gray:
            case R.id.abt_btn_send_a_heart:
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.ic_normal_heart_red);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mIvNormalHeartRed.getWidth(), mIvNormalHeartRed.getHeight());
                iv.setLayoutParams(lp);
                mFlRootContainer.addView(iv);
                //solution 1
                //mCoordinateAnimationHelper.start(iv, 100, 100, 200, 200, 3 * 1000, this);
                //solution 2
                mCi = CountdownManager.i().add(System.currentTimeMillis(), System.currentTimeMillis() + 3 * 1000, 20, iv, subThreadOnCountdownListener, mainThreadOnCountdownListener);
                break;

        }
    }

    private void initBezierRelated() {
        int baseX = ScreenUtils.getWidth(this) / 2;
        int baseY = ScreenUtils.getHeight(this) / 2;

        Coordinate start = Coordinate.obtain(baseX, baseY - START_OFFSET);
        Coordinate control1 = Coordinate.obtain(baseX + START_OFFSET / 2, baseY - START_OFFSET - START_OFFSET / 2);
        Coordinate control2 = Coordinate.obtain(baseX + START_OFFSET / 2, baseY - START_OFFSET - START_OFFSET);
        Coordinate end = Coordinate.obtain(baseX, baseY - END_OFFSET);

        mCoordinateList = new ArrayList<>();
        mCoordinateList.add(start);
        mCoordinateList.add(control1);
        mCoordinateList.add(control2);
        mCoordinateList.add(end);
    }

    private void testBesierAlgorithm() {
        int p0x = 100;
        int p0y = 100;
        int p1x = 240;
        int p1y = 60;
        int p2x = 200;
        int p2y = 200;

        for (double t = 0.1; t <= 1.0; t += 0.1) {
            int p0_1x = (int) ((1 - t) * p0x + t * p1x);
            int p0_1y = (int) ((1 - t) * p0y + t + p1y);
            int p1_1x = (int) ((1 - t) * p1x + t * p2x);
            int p1_1y = (int) ((1 - t) * p1y + t + p2y);
            int p0_2x = (int) ((1 - t) * p0_1x + t * p1_1x);
            int p0_2y = (int) ((1 - t) * p0_1y + t * p1_1y);

            LogMgr.i().logT(TAG, String.format("p0(%d,%d)  p1(%d,%d)  p2(%d,%d)  t=%f  final position(%d,%d)"
                    , p0x, p0y
                    , p1x, p1y
                    , p2x, p2y
                    , t
                    , p0_2x, p0_2y
                    )
            );
        }

    }


    private void testBesierAlgorithmB3() {

        StringBuilder sb = new StringBuilder();

        String tLeftPercent = "(1-t)";
        String tPercent = "t";

        String p0 = "p0";
        String p1 = "p1";
        String p2 = "p2";
        String p3 = "p3";

        String p0_1 = sb.append(tLeftPercent).append(p0).append("+").append(tPercent).append(p1).toString();
        sb.setLength(0);
        String p1_1 = sb.append(tLeftPercent).append(p1).append("+").append(tPercent).append(p2).toString();
        sb.setLength(0);
        String p2_1 = sb.append(tLeftPercent).append(p2).append("+").append(tPercent).append(p3).toString();
        sb.setLength(0);

        p0_1 = "(" + p0_1 + ")";
        p1_1 = "(" + p1_1 + ")";
        p2_1 = "(" + p2_1 + ")";

        LogMgr.i().logT(TAG, "p0_1 : " + p0_1);
        LogMgr.i().logT(TAG, "p1_1 : " + p1_1);
        LogMgr.i().logT(TAG, "p2_1 : " + p2_1);

        String p0_2 = sb.append(tLeftPercent).append(p0_1).append("+").append(tPercent).append(p1_1).toString();
        sb.setLength(0);
        String p1_2 = sb.append(tLeftPercent).append(p1_1).append("+").append(tPercent).append(p2_1).toString();
        sb.setLength(0);
        p0_2 = "(" + p0_2 + ")";
        p1_2 = "(" + p1_2 + ")";

        LogMgr.i().logT(TAG, "p0_2 : " + p0_2);
        LogMgr.i().logT(TAG, "p1_2 : " + p1_2);

        String p0_3 = sb.append(tLeftPercent).append(p0_2).append("+").append(tPercent).append(p1_2).toString();

        LogMgr.i().logT(TAG, "p0_3 = " + p0_3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoordinateAnimationHelper.uninit();
        if (mCi != null) {
            CountdownManager.i().remove(mCi);
        }
    }

    private double mFromAlpha = 1;
    private double mToAlpha = 0.25;
    private double mFromScale = 1;
    private double mToScale = 0.5;

    @Override
    public boolean onAnimationRefresh(AnimationInfo a) {
        float curAlpha = (float) ((1 - a.spentMillisPercent) * mFromAlpha + a.spentMillisPercent * mToAlpha);
        float curScale = (float) ((1 - a.spentMillisPercent) * mFromScale + a.spentMillisPercent * mToScale);
        Coordinate curCoordinate = BezierCurveUtils.calculate4nOrder(mCoordinateList, a.spentMillisPercent);
        View v = a.v;
        v.setAlpha(curAlpha);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.width = (int) (curScale * mHeartWidth);
        lp.height = (int) (curScale * mHeartHeight);
        v.setLayoutParams(lp);
        v.setX(curCoordinate.x - (lp.width / 2));
        v.setY(curCoordinate.y);
        if (a.spentMillisPercent == 1) {
            mFlRootContainer.removeView(a.v);
        }
        return true;
    }

    private OnCountdownListener subThreadOnCountdownListener = new OnCountdownListener() {
        @Override
        public void onCountdownUpdate(CountdownInfo ci) {
            LogMgr.i().logT(TAG, "sub thread onCountdownUpdate -> ");
        }
    };

    private OnCountdownListener mainThreadOnCountdownListener = new OnCountdownListener() {
        @Override
        public void onCountdownUpdate(CountdownInfo ci) {
            LogMgr.i().logT(TAG, "onCountdownUpdate -> ");
            double spentMillisPercent = ci.getSpentMillisPercent();
            float curAlpha = (float) ((1 - spentMillisPercent) * mFromAlpha + spentMillisPercent * mToAlpha);
            float curScale = (float) ((1 - spentMillisPercent) * mFromScale + spentMillisPercent * mToScale);
            Coordinate curCoordinate = BezierCurveUtils.calculate4nOrder(mCoordinateList, spentMillisPercent);
            View v = (View) ci.getTag();
            v.setAlpha(curAlpha);
            ViewGroup.LayoutParams lp = v.getLayoutParams();
            lp.width = (int) (curScale * mHeartWidth);
            lp.height = (int) (curScale * mHeartHeight);
            v.setLayoutParams(lp);
            v.setX(curCoordinate.x - (lp.width / 2));
            v.setY(curCoordinate.y);
            if (spentMillisPercent == 1) {
                Message msg = Message.obtain();
                msg.what = WHAT_REMOVE_VIEW;
                msg.obj = v;
                mHandler.sendMessageDelayed(msg, 5 * 1000);
            }
        }
    };

    private final int WHAT_REMOVE_VIEW = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REMOVE_VIEW:
                    mFlRootContainer.removeView((View) msg.obj);
                    break;
            }
        }
    };

}
