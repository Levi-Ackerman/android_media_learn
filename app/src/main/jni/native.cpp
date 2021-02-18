//
// Created by ali on 2020/8/6.
//

#include <stdio.h>
#include <jni.h>
#include <android/log.h>
#include <memory>
extern "C" {
#include "libavutil/log.h"
#include "libavformat/avformat.h"
#include "pcm.h"
}
#include "audio_decoder.h"
#include "media_file.h"
#define null NULL

static const char *filename = "/sdcard/1/marvel.mp4";

void dump_format() {
  AVFormatContext *context = NULL;
  int ret = avformat_open_input(&context, filename, NULL, NULL);

  av_log_set_level(AV_LOG_DEBUG);
  av_log(NULL, AV_LOG_INFO, "hello ffmpeg %d\n", ret);
  av_dump_format(context, 0, filename, 0);
  ret = av_find_best_stream(context, AVMEDIA_TYPE_AUDIO, -1, -1, null, 0);
  __android_log_print(ANDROID_LOG_INFO, "ndk", "hello ndk %d\n", ret);
  avformat_close_input(&context);
}

void play_audio() {
  std::atomic_bool value(true);
  AudioDecoder *decoder = new AudioDecoder(filename, &value, [](PCM *pcm) {
    if (pcm == nullptr) {
      __android_log_print(ANDROID_LOG_INFO, "ndk", "pcm_callback null");
    } else {
      __android_log_print(ANDROID_LOG_INFO, "ndk", "pcm_callback %d, %ld\n", pcm->size, pcm->pts_us);
    }
  });
  decoder->start();
}

extern "C" JNIEXPORT jint JNICALL
Java_levi_ackerman_medialearn_MainActivity_test(JNIEnv *env, jobject thiz) {
  av_register_all();
//  dump_format();
  play_audio();
  return 10;
}

void hello() {
  printf("hello");
}

extern "C"
JNIEXPORT jlong JNICALL
Java_levi_ackerman_medialearn_MediaFile_createNativeObj(JNIEnv *env, jobject thiz, jstring file_name) {
  jsize length = env->GetStringUTFLength(file_name);
  char* c_file_name = new char[length];
  env->GetStringUTFRegion(file_name,0,length,c_file_name);
  std::shared_ptr<char> sp(c_file_name);
  auto mediaFile = new MediaFile(sp);
  sp.reset();
  return reinterpret_cast<jlong>(mediaFile);
}

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_MediaFile_recoverNativeObj(JNIEnv *env, jobject thiz, jlong ptr) {
  auto mediaFile = reinterpret_cast<MediaFile*>(ptr);
  mediaFile->print();
}