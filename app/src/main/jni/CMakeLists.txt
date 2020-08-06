cmake_minimum_required(VERSION 3.4.1)
add_library(
        native-lib
        SHARED
        native.c
)
find_library(
        log-lib
        log
)
add_library(test_so SHARED IMPORTED)
message("位置 ${CMAKE_SOURCE_DIR}/../jniLibs/arm64-v8a/libTest.so")
set_target_properties(test_so PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/arm64-v8a/libTest.so)
target_link_libraries(
        native-lib
        ${log-lib}
        test_so
)