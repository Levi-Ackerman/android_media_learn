//
// Created by Bytedance on 2021/2/18.
//

#ifndef ANDROIDMEDIALEARN_LOG_H
#define ANDROIDMEDIALEARN_LOG_H
#include <android/log.h>

#define LOG_TAG "loglee"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#endif //ANDROIDMEDIALEARN_LOG_H
