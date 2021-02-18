//
// Created by lee on 2020/12/26.
//

#ifndef CDEMO_AUDIO_DECODER_H
#define CDEMO_AUDIO_DECODER_H
#include "functional"
#include <atomic>

struct AVFrame;
struct PCM;

class AudioDecoder {
private:
    const char* m_input_file_name;
    std::function<void(PCM*)> m_callback;
public:
    void start();
    AudioDecoder(const char* filename,std::function<void(PCM*)> callback);
};


#endif //CDEMO_AUDIO_DECODER_H
