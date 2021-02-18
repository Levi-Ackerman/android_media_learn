//
// Created by Bytedance on 2021/2/18.
//

#ifndef ANDROIDMEDIALEARN_MEDIA_FILE_H
#define ANDROIDMEDIALEARN_MEDIA_FILE_H
#include <memory>

class MediaFile {

 private:
  std::shared_ptr<char> file_name;

 public:
  MediaFile(std::shared_ptr<char> fileName);
  ~MediaFile();
  void print();
};


#endif //ANDROIDMEDIALEARN_MEDIA_FILE_H
