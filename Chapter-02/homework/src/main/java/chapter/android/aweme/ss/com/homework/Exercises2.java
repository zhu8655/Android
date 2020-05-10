package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {

    TextView tv_cnt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises2);
        tv_cnt = findViewById(R.id.tv_2_1);
        tv_cnt.setText(Integer.toString(getAllChildViewCount(findViewById(R.id.root_view))));
    }

    public int getAllChildViewCount(View view) {
        int cnt = 0;

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                cnt += getAllChildViewCount(((ViewGroup) view).getChildAt(i));
            }
        }
        else cnt++;

        return cnt;
    }
}
