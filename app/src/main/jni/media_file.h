//
// Created by Bytedance on 2021/2/18.
//

#ifndef ANDROIDMEDIALEARN_MEDIA_FILE_H
#define ANDROIDMEDIALEARN_MEDIA_FILE_H
#include <memory>
#include "audio_decoder.h"
#include "block_list.h"

class MediaFile {

 private:
  std::shared_ptr<AudioDecoder> m_audio_decoder;
  std::shared_ptr<char> file_name;
  std::shared_ptr<BlockList<PCM*>> m_pcm_list;

 public:
  MediaFile(std::shared_ptr<char> fileName);
  void start_audio_decode();
  PCM* pop_pcm();
};


#endif //ANDROIDMEDIALEARN_MEDIA_FILE_H
