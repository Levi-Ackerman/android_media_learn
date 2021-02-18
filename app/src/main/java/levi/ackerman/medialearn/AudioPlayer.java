package levi.ackerman.medialearn;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

public class AudioPlayer {

    private final AudioTrack mAudioTrack;

    public AudioPlayer(String filename){
        create(filename);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT),
                AudioTrack.MODE_STREAM);
        mAudioTrack.setVolume(16f);
    }
    public native void create(String filename);
    public native void destroy();
    public void play(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                mAudioTrack.play();
                nativePlay();
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                byte[] data;
                while ((data = nativePopPcm()) != null){
                    PcmData pcmData = new PcmData();
                    ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.nativeOrder());
                    pcmData.pts = buffer.getLong();
                    pcmData.size = buffer.getInt();
                    pcmData.data = new byte[pcmData.size];
                    buffer.get(pcmData.data);
                    int ret = mAudioTrack.write(pcmData.data,0,pcmData.size);
                    switch (ret){
                        case AudioTrack.ERROR_INVALID_OPERATION:
                        case AudioTrack.ERROR_BAD_VALUE:
                        case AudioManager.ERROR_DEAD_OBJECT:
                            Log.i("loglee","播放失败"+ret);
                            return;
                        default:
                            Log.i("loglee","播放成功");
                            break;
                    }
                }
            }
        });
    }
    private native void nativePlay();
    private native byte[] nativePopPcm();
}
