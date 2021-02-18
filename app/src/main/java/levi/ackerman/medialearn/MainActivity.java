package levi.ackerman.medialearn;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avutil");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testSo(View view) {
        System.out.println("hello test: "+test());
    }

    public native int test();
}
