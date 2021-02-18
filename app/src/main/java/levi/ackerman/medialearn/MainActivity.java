package levi.ackerman.medialearn;

import android.media.AudioTrack;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MediaFile mMediaFile;

    public void test2(View view) {
        mMediaFile.recover();
    }

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avutil");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaFile = new MediaFile("/sdcard/1/marvel.mp4");
    }

    public void testSo(View view) {
        System.out.println("hello test: "+test());
    }

    public native int test();
}
