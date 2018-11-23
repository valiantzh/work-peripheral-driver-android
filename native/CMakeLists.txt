# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             CommAndModbus

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/com_hzdongcheng_alxdrivers_jni_JNISerialPort.cpp
             src/main/cpp/com_hzdongcheng_alxdrivers_jni_JNIModbus.cpp
             src/main/cpp/SerialPortBase.cpp
             src/main/cpp/SerialPort4J.cpp
             src/main/cpp/Modbus4j.cpp
             src/main/cpp/modbus-tcp.c
             src/main/cpp/modbus-rtu.c
             src/main/cpp/modbus-data.c
             src/main/cpp/modbus.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       CommAndModbus

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )