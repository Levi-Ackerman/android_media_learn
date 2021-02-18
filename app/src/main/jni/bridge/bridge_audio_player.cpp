//
// Created by Bytedance on 2021/2/18.
//

#include <jni.h>
#include "audio_decoder.h"
#include "memory"
#include "pcm.h"
#include "log.h"
#include "block_list.h"

std::shared_ptr<AudioDecoder> m_audio_decoder;
std::shared_ptr<BlockList<PCM *>> m_pcm_list;

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_AudioPlayer_create(JNIEnv *env, jobject thiz, jstring filename) {
  int size = env->GetStringUTFLength(filename);
  char *c_file_name = new char[size];
  env->GetStringUTFRegion(filename, 0, size, c_file_name);
  m_pcm_list = std::make_shared<BlockList<PCM *>>();
  m_audio_decoder = std::make_shared<AudioDecoder>(c_file_name, [](PCM *pcm) {
    m_pcm_list->push_back(pcm);
  });
}

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_AudioPlayer_destroy(JNIEnv *env, jobject thiz) {
  m_audio_decoder.reset();
  while (m_pcm_list->size() > 0) {
    PCM *p = m_pcm_list->pop_front();
    delete p;
  }
  m_pcm_list.reset();
}

extern "C"
JNIEXPORT void JNICALL
Java_levi_ackerman_medialearn_AudioPlayer_nativePlay(JNIEnv *env, jobject thiz) {
  if (m_audio_decoder.use_count()) {
    m_audio_decoder->start();
  }
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_levi_ackerman_medialearn_AudioPlayer_nativePopPcm(JNIEnv *env, jobject thiz) {
  LOGI("pop pcm");
  auto pcm = m_pcm_list->pop_front();
  LOGI("pop pcm after %d", pcm->size);
  if (pcm->size < 0) {
    return nullptr;
  }
  size_t length = sizeof(pcm->pts_us) + sizeof(pcm->size) + pcm->size;
  jbyteArray bytes = env->NewByteArray(length);
  env->SetByteArrayRegion(bytes, 0, length, reinterpret_cast<const jbyte *>(pcm));
  delete pcm;
  return bytes;
}