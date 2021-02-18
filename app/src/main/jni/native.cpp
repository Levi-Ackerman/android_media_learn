//
// Created by ali on 2020/8/6.
//

#include <stdio.h>
#include <jni.h>
#include <android/log.h>
#include <memory>
#include "log.h"
extern "C" {
#include "libavutil/log.h"
#include "libavformat/avformat.h"
#include "pcm.h"
}
#include "audio_decoder.h"
#include "media_file.h"
#define null NULL

std::shared_ptr<MediaFile> m_media_file ;

// 释放Native对象
void nativeDestroy(JNIEnv *env, jobject obj) {
  m_media_file.reset();
}

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_MediaFile_createNativeObj(JNIEnv *env, jobject thiz, jstring file_name) {
  jsize length = env->GetStringUTFLength(file_name);
  char *c_file_name = new char[length];
  env->GetStringUTFRegion(file_name, 0, length, c_file_name);
  std::shared_ptr<char> sp(c_file_name, [](const char *arr) {
    LOGI("delete it");
    delete[] arr;
  });
  m_media_file = std::make_shared<MediaFile>(sp);
}

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_MediaFile_startDecode(JNIEnv *env, jobject thiz) {
  auto mediaFile = m_media_file;
  mediaFile->start_audio_decode();
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_levi_ackerman_medialearn_MediaFile_popPcm(JNIEnv *env, jobject thiz) {
  auto mediaFile = m_media_file;
  auto pcm = mediaFile->pop_pcm();
  jbyteArray byte_arr = null;
  if (pcm->size >= 0) {
    byte_arr = env->NewByteArray(sizeof(*pcm));
    env->SetByteArrayRegion(byte_arr, 0, sizeof(*pcm), reinterpret_cast<const jbyte *>(pcm));
  } else {
    byte_arr = null;
  }
  delete pcm;
  return byte_arr;
}