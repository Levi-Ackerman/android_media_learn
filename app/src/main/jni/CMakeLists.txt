cmake_minimum_required(VERSION 3.4.1)
#add_library(test-lib SHARED IMPORTED)
#set_target_properties(
#        test-lib
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/../jniLibs/arm64-v8a/libtest-lib.so)
#message(哈哈${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI})
include_directories(${CMAKE_SOURCE_DIR})
link_libraries(${CMAKE_SOURCE_DIR}/../jniLibs/arm64-v8a/libtest-lib.so)
#link_directories(${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI})
#target_link_libraries(
#        native-lib
#        test-lib
#)

add_library(
        native-lib
        SHARED
        native.c
)