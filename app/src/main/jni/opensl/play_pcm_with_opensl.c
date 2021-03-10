//
// Created by Bytedance on 2021/3/10.
//
#include <jni.h>
#include <log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#ifdef __cplusplus
extern "C"{
#endif


JNIEXPORT int JNICALL
Java_levi_ackerman_medialearn_chapter02_BridgeUtil02_playAudioWithOpenSL(JNIEnv *env, jobject thiz) {
  SLObjectItf engineObject;
  SLEngineItf engineEngine;
  SLEngineOption engineOption[] = {{(SLuint32) SL_ENGINEOPTION_THREADSAFE, (SLuint32) SL_BOOLEAN_TRUE}};
  //创建对象
  SLresult result = slCreateEngine(&engineObject, 1, engineOption, 0, 0, 0);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("create sl engine error: %d", result);
    return result;
  }
  //初始化
  result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("realize sl engine error: %d", result);
    return result;
  }
  result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("get interface error:%d", result);
    return result;
  }
  //销毁
  (*engineObject)->Destroy(engineObject);
  return SL_RESULT_SUCCESS;
}
#ifdef __cplusplus
};
#endif

