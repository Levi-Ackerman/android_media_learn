//
// Created by Bytedance on 2021/2/18.
//

#ifndef ANDROIDMEDIALEARN_AUDIO_PARAMS_H
#define ANDROIDMEDIALEARN_AUDIO_PARAMS_H
extern "C"{
#include <libavutil/channel_layout.h>
#include <libavutil/samplefmt.h>
}
static const int AUDIO_SAMPLE_RATE = 44100;
static const int AUDIO_OUT_LAYOUT = AV_CH_LAYOUT_STEREO;

static const int AUDIO_CHANNELS = av_get_channel_layout_nb_channels(AUDIO_OUT_LAYOUT);
static const AVSampleFormat AUDIO_OUT_FORMAT = AV_SAMPLE_FMT_S16;

#endif //ANDROIDMEDIALEARN_AUDIO_PARAMS_H
