package levi.ackerman.medialearn;

public class MediaFile {

    private final String mFileName;

    private final long mNativeObjPtr;

    public MediaFile(String fileName){
        this.mFileName = fileName;
        mNativeObjPtr = createNativeObj(fileName);
    }

    public void recover() {
        recoverNativeObj(mNativeObjPtr);
    }

    private native long createNativeObj(String fileName);

    private native void recoverNativeObj(long ptr);
}
