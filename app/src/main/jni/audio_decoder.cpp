//
// Created by lee on 2020/12/26.
//

#include "audio_decoder.h"
#include "audio_params.h"

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libswresample/swresample.h"
#include "libavutil/imgutils.h"
#include "libavutil/mem.h"
#include "pcm.h"
}
#include "log.h"

AudioDecoder::AudioDecoder(const char *file_name, std::function<void(PCM*)> callback) {
    this->m_input_file_name = file_name;
    this->m_callback = callback;
}

void AudioDecoder::start() {
    AVFormatContext *fmt_ctx = nullptr;
    AVCodecContext *codec_ctx = nullptr;
    AVPacket *packet = nullptr;
    AVFrame *frame = nullptr;
    SwrContext *swr_ctx = nullptr;

    int ret = avformat_open_input(&fmt_ctx, this->m_input_file_name, nullptr, nullptr);
    if (ret < 0){
      LOGI("error when open input %s",av_err2str(ret));
      return ;
    }
    avformat_find_stream_info(fmt_ctx, nullptr);
    int audio_stream_index = av_find_best_stream(fmt_ctx, AVMEDIA_TYPE_AUDIO, -1, -1, nullptr, 0);
    AVCodecParameters *audio_param = fmt_ctx->streams[audio_stream_index]->codecpar;
    AVCodec *codec = avcodec_find_decoder(audio_param->codec_id);
    AVRational audio_timebase = fmt_ctx->streams[audio_stream_index]->time_base;

    codec_ctx = avcodec_alloc_context3(nullptr);
    avcodec_parameters_to_context(codec_ctx, audio_param);
    avcodec_open2(codec_ctx, codec, nullptr);

    packet = av_packet_alloc();
    frame = av_frame_alloc();
    swr_ctx = swr_alloc();
    swr_alloc_set_opts(swr_ctx, AUDIO_OUT_LAYOUT, AUDIO_OUT_FORMAT, AUDIO_SAMPLE_RATE, codec_ctx->channel_layout,
                       codec_ctx->sample_fmt, codec_ctx->sample_rate, 0,
                       nullptr);
    swr_init(swr_ctx);
    av_init_packet(packet);
    while (true) {
        if (av_read_frame(fmt_ctx, packet) == 0) {
            if (packet->stream_index == audio_stream_index) {
                avcodec_send_packet(codec_ctx, packet);
                if (avcodec_receive_frame(codec_ctx, frame) == 0) {
                    const int nb_channels = av_get_channel_layout_nb_channels(AUDIO_OUT_LAYOUT);
                    const int out_buf_size = av_samples_get_buffer_size(nullptr, nb_channels, frame->nb_samples,
                                                                        AUDIO_OUT_FORMAT, 1);
                    uint8_t *out_buf = static_cast<uint8_t *>(av_malloc(sizeof(uint8_t) * out_buf_size));
                    swr_convert(swr_ctx, &out_buf, out_buf_size, (const uint8_t **) frame->data, frame->nb_samples);
                    int64_t pts_us = 1000000 * frame->pts * audio_timebase.num / audio_timebase.den;
                    this->m_callback(new PCM{pts_us,out_buf_size,out_buf});
                }
            }
        } else {
            this->m_callback(new PCM{-1,-1, nullptr});
            break;
        }
    }

    swr_free(&swr_ctx);
    av_frame_free(&frame);
    av_packet_free(&packet);
    avcodec_free_context(&codec_ctx);
    avformat_close_input(&fmt_ctx);
}

