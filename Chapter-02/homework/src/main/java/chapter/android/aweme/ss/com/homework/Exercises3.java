package chapter.android.aweme.ss.com.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.PullParser;
import chapter.android.aweme.ss.com.homework.widget.CircleImageView;

/**
 * 大作业:实现一个抖音消息页面,
 * 1、所需的data数据放在assets下面的data.xml这里，使用PullParser这个工具类进行xml解析即可
 * <p>如何读取assets目录下的资源，可以参考如下代码</p>
 * <pre class="prettyprint">
 *
 *         @Override
 *     protected void onCreate(@Nullable Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_xml);
 *         //load data from assets/data.xml
 *         try {
 *             InputStream assetInput = getAssets().open("data.xml");
 *             List<Message> messages = PullParser.pull2xml(assetInput);
 *             for (Message message : messages) {
 *
 *             }
 *         } catch (Exception exception) {
 *             exception.printStackTrace();
 *         }
 *     }
 * </pre>
 * 2、所需UI资源已放在res/drawable-xxhdpi下面
 *
 * 3、作业中的会用到圆形的ImageView,可以参考 widget/CircleImageView.java
 */
public class Exercises3 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        //load data from assets/data.xml
        try {
            InputStream assetInput = getAssets().open("data.xml");
            List<Message> messages = PullParser.pull2xml(assetInput);

            RecyclerView rv = findViewById(R.id.rv_list);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(new _Adapter(messages));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public class _Adapter extends RecyclerView.Adapter<_Adapter.ViewHolder> {
        private List<Message> msgList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView iv;
            ImageView robot_notice;
            TextView tv_1, tv_2, tv_3;

            public ViewHolder (View view) {
                super(view);
                iv = view.findViewById(R.id.iv_avatar);
                robot_notice = view.findViewById(R.id.robot_notice);
                tv_1 = view.findViewById(R.id.tv_title);
                tv_2 = view.findViewById(R.id.tv_description);
                tv_3 = view.findViewById(R.id.tv_time);
            }
        }

        public _Adapter(List<Message> list) {
            this.msgList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.im_list_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    startActivity(
                            new Intent(Exercises3.this, chatroom.class)
                            .putExtra("pos", position)
                    );
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int pos) {
            Message msg = this.msgList.get(pos);
            switch (msg.getIcon()) {
                case "TYPE_ROBOT":
                    holder.iv.setImageResource(R.drawable.session_robot); break;
                case "TYPE_GAME":
                    holder.iv.setImageResource(R.drawable.icon_micro_game_comment); break;
                case "TYPE_SYSTEM":
                    holder.iv.setImageResource(R.drawable.session_system_notice); break;
                case "TYPE_STRANGER":
                    holder.iv.setImageResource(R.drawable.session_stranger); break;
                case "TYPE_USER":
                    holder.iv.setImageResource(R.drawable.icon_girl); break;
                default:
                    break;
            }
            holder.robot_notice.setVisibility(msg.isOfficial() ? View.VISIBLE : View.INVISIBLE);
            holder.tv_1.setText(msg.getTitle());
            holder.tv_2.setText(msg.getDescription());
            holder.tv_3.setText(msg.getTitle());
        }

        @Override
        public int getItemCount() {
            return this.msgList.size();
        }
    }

}
