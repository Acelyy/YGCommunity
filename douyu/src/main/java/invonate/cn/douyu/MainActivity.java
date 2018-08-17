package invonate.cn.douyu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.jzvd.JZVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {

    JZVideoPlayerStandard player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player=findViewById(R.id.player);

        //String url="rtsp://zhyluser:zhyluser@10.85.12.70:554/h264/ch1/main/av_stream";
        String url="http://ygoa.yong-gang.cn/ygoa/upload/lomo/021274/2018/03/06/f17ba5d47f024ec7a42f32a212099223.mp4";
        player.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "斗鱼测试");
    }
}
