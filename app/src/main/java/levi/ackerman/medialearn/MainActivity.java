package levi.ackerman.medialearn;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import levi.ackerman.medialearn.chapter01.MediaPlayerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpChapter01(View view) {
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                MediaPlayerFragment.instantiate(this, MediaPlayerFragment.class.getName()))
                .addToBackStack(MediaPlayerFragment.class.getName()).commit();
    }
}
