package levi.ackerman.medialearn;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import levi.ackerman.medialearn.chapter01.MediaPlayerFragment;
import levi.ackerman.medialearn.chapter02.SoundPlayerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpChapter01(View view) {
        jumpFragment(MediaPlayerFragment.class);
    }

    public void jumpChapter02(View view) {
        jumpFragment(SoundPlayerFragment.class);
    }

    private void jumpFragment(Class fragmentClazz) {
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                Fragment.instantiate(this, fragmentClazz.getName()))
                .addToBackStack(fragmentClazz.getName()).commit();
    }
}
