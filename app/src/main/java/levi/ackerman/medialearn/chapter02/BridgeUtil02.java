package levi.ackerman.medialearn.chapter02;

public class BridgeUtil02 {
    static {
        System.loadLibrary("native-lib");
    }
    public static native int playAudioWithOpenSL();
}
