//
// Created by lee on 2020/12/26.
//

#ifndef CDEMO_PCM_H
#define CDEMO_PCM_H

typedef struct PCM{
  int64_t pts_us;
  int size;
  uint8_t * data;
} PCM;

#endif //CDEMO_PCM_H
