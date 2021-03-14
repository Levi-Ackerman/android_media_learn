package levi.ackerman.medialearn.chapter03

import android.media.MediaExtractor

class MediaMutexHard : IMediaMutext {
    private val mediaExtractor = MediaExtractor()

    fun getVideoInfo() {
        for(i in 0..mediaExtractor.trackCount){
            val trackFormat = mediaExtractor.getTrackFormat(i)
            trackFormat
        }
    }
}