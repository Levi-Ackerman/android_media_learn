package levi.ackerman.medialearn;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

public class MediaFile {

    private final String mFileName;

    private final AudioTrack mAudioTrack;

    public MediaFile(String fileName) {
        this.mFileName = fileName;
        //这句会反射设置mNativeObjPtr
         createNativeObj(fileName);
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT),
                AudioTrack.MODE_STREAM);
    }

    public void play() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                startDecode();
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    PcmData data = new PcmData();
                    byte[] pcm = popPcm();
                    if (pcm == null) {
                        break;
                    }
                    //8字节pts，4字节长度，n字节pcm数据
                    final ByteBuffer buf = ByteBuffer.wrap(pcm).order(ByteOrder.nativeOrder());
                    data.pts = buf.getLong();
                    data.size = buf.getInt();
                    data.data = new byte[data.size];
                    buf.get(data.data);
                    mAudioTrack.write(data.data, 0, data.size);
                }
            }
        });
    }

    private native byte[] popPcm();

    private native void createNativeObj(String fileName);

    private native void startDecode();
}
