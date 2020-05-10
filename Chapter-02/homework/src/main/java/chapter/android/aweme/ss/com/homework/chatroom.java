package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class chatroom extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        TextView tv = findViewById(R.id.tv_content_info);

        int pos = getIntent().getIntExtra("pos", 0);
        tv.setText("这是第" + pos + "个item");

    }
}
