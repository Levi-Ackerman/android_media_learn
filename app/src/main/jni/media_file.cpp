//
// Created by Bytedance on 2021/2/18.
//

#include "media_file.h"
#include <atomic>
extern "C" {
#include "libavformat/avformat.h"
}
#include <thread>

#include <utility>
#include "log.h"
void MediaFile::start_audio_decode() {
    m_audio_decoder->start();
}

MediaFile::MediaFile(std::shared_ptr<char> filename) {
  this->file_name = std::move(filename);
  m_pcm_list = std::make_shared<BlockList<PCM *>>();
  this->m_audio_decoder = std::make_shared<AudioDecoder>(filename.get(), [this](PCM *pcm) {
    m_pcm_list->push_back(pcm);
  });
}

PCM *MediaFile::pop_pcm() {
  return m_pcm_list->pop_front();
}
