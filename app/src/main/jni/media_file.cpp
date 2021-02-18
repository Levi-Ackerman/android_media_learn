//
// Created by Bytedance on 2021/2/18.
//

#include "media_file.h"
#include "log.h"
void MediaFile::print() {
  LOGI("%s\n",file_name.get());
}

MediaFile::MediaFile(std::shared_ptr<char> filename) {
  this->file_name = filename;
}

MediaFile::~MediaFile() {
  this->file_name.reset();
}
