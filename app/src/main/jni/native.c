//
// Created by ali on 2020/8/6.
//

#include <stdio.h>
#include "main.h"
#include <jni.h>

JNIEXPORT jint JNICALL
Java_levi_ackerman_medialearn_MainActivity_test(JNIEnv *env, jobject thiz) {
    return 10 + testPoint();
}

void hello(){
    printf("hello");
}

