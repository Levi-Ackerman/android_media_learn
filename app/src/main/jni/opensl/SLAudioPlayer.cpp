//
// Created by Bytedance on 2021/3/10.
//

#include "SLAudioPlayer.h"
#include "log.h"
int SLAudioPlayer::init() {
  SLEngineOption engineOption[] = {{(SLuint32) SL_ENGINEOPTION_THREADSAFE, (SLuint32) SL_BOOLEAN_TRUE}};
  //创建对象
  SLresult result = slCreateEngine(&m_sl_object, 1, engineOption, 0, 0, 0);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("create sl engine error: %d", result);
    return result;
  }
  //初始化
  result = (*m_sl_object)->Realize(m_sl_object, SL_BOOLEAN_FALSE);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("realize sl engine error: %d", result);
    return result;
  }
  result = (*m_sl_object)->GetInterface(m_sl_object, SL_IID_ENGINE, &m_sl_engine);
  if (result != SL_RESULT_SUCCESS) {
    LOGI("get interface error:%d", result);
    return result;
  }
  return SL_RESULT_SUCCESS;
}

int SLAudioPlayer::play() {
  init();

  return 0;
}
void SLAudioPlayer::destroy() {
  //销毁
  (*m_sl_object)->Destroy(m_sl_object);
}
