package levi.ackerman.medialearn;

import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class VideoPlayer {

    public VideoPlayer(String fileName, SurfaceView surfaceView){
        surfaceView.getHolder().addCallback(new Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(final SurfaceHolder holder, final int format, final int width,
                    final int height) {

            }

            @Override
            public void surfaceDestroyed(final SurfaceHolder holder) {

            }
        });
    }
}
