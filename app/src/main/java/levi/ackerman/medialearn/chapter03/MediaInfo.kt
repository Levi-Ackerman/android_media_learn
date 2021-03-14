package levi.ackerman.medialearn.chapter03

import android.media.MediaFormat

class MediaInfo() {
    var videoInfo: VideoInfo? = null
    var audioInfo: AudioInfo? = null
}

class VideoInfo(val mediaFormat: MediaFormat, val trackIndex: Int, val mineType: String, width: Int, height: Int, frameRate: Int) {

}

class AudioInfo(
    val mediaFormat: MediaFormat,
    val trackIndex:Int,
    val mineType: String,
    val sampleRate: Int,
    val channelCount: Int
) {

}