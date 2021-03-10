//
// Created by Bytedance on 2021/3/10.
//

#ifndef ANDROIDMEDIALEARN_SLAUDIOPLAYER_H
#define ANDROIDMEDIALEARN_SLAUDIOPLAYER_H
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

class SLAudioPlayer {
 private:
  SLEngineItf m_sl_engine;
  SLObjectItf m_sl_object;
 public:
  int play();
  int init();
  void destroy();
};


#endif //ANDROIDMEDIALEARN_SLAUDIOPLAYER_H
