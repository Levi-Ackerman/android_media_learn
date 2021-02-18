package levi.ackerman.medialearn;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private AudioPlayer mAudioPlayer;

    public void test2(View view) {
        mAudioPlayer.play();
    }

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avutil");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudioPlayer = new AudioPlayer("/sdcard/1/marvel.mp4");
    }

}
