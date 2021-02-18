//
// Created by ali on 2020/8/6.
//

#include <stdio.h>
#include <jni.h>
#include "ffmpeg/libavutil/log.h"
#include <android/log.h>
#include "ffmpeg/libavformat/avformat.h"
#define null NULL

JNIEXPORT jint JNICALL
Java_levi_ackerman_medialearn_MainActivity_test(JNIEnv *env, jobject thiz) {
  av_register_all();
  AVFormatContext *context = NULL;
  char *filename = "/sdcard/1/marvel.mp4";
  int ret = avformat_open_input(&context, filename, NULL, NULL);

  av_log_set_level(AV_LOG_DEBUG);
  av_log(NULL, AV_LOG_INFO, "hello ffmpeg %d\n",ret);
  av_dump_format(context,0,filename,0);
  ret = av_find_best_stream(context, AVMEDIA_TYPE_AUDIO,-1,-1,null,0);
  __android_log_print(ANDROID_LOG_INFO, "ndk","hello ndk %d\n",ret);
  avformat_close_input(&context);
  return 10;
}

void hello() {
  printf("hello");
}

