/*-
 * #%L
 * SPSW Provider
 * %%
 * Copyright (C) 2009 - 2017 Arne Plöse
 * %%
 * SPSW - Drivers for the serial port, https://github.com/aploese/spsw/
 * Copyright (C) 2009-2018, Arne Plöse and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * #L%
 */

/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010-2014.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: http://scream3r.org | http://code.google.com/p/java-simple-serial-connector/
 */
#include <jni.h>
#include <stdlib.h>
#include <windows.h>
#include "de_ibapl_spsw_provider_GenericWinSerialPortSocket.h"

//#include <iostream>

#undef SPSW_PORT_MODE_UNCHANGED
#define SPSW_PORT_MODE_UNCHANGED de_ibapl_spsw_provider_GenericWinSerialPortSocket_PORT_MODE_UNCHANGED
#undef SPSW_PORT_MODE_RAW
#define SPSW_PORT_MODE_RAW de_ibapl_spsw_provider_GenericWinSerialPortSocket_PORT_MODE_RAW

#undef SPSW_FLOW_CONTROL_NONE
#define SPSW_FLOW_CONTROL_NONE de_ibapl_spsw_provider_GenericWinSerialPortSocket_FLOW_CONTROL_NONE
#undef SPSW_FLOW_CONTROL_RTS_CTS_IN
#define SPSW_FLOW_CONTROL_RTS_CTS_IN de_ibapl_spsw_provider_GenericWinSerialPortSocket_FLOW_CONTROL_RTS_CTS_IN
#undef SPSW_FLOW_CONTROL_RTS_CTS_OUT
#define SPSW_FLOW_CONTROL_RTS_CTS_OUT de_ibapl_spsw_provider_GenericWinSerialPortSocket_FLOW_CONTROL_RTS_CTS_OUT
#undef SPSW_FLOW_CONTROL_XON_XOFF_IN
#define SPSW_FLOW_CONTROL_XON_XOFF_IN de_ibapl_spsw_provider_GenericWinSerialPortSocket_FLOW_CONTROL_XON_XOFF_IN
#undef SPSW_FLOW_CONTROL_XON_XOFF_OUT
#define SPSW_FLOW_CONTROL_XON_XOFF_OUT de_ibapl_spsw_provider_GenericWinSerialPortSocket_FLOW_CONTROL_XON_XOFF_OUT

#undef SPSW_STOP_BITS_1
#define SPSW_STOP_BITS_1 de_ibapl_spsw_provider_GenericWinSerialPortSocket_STOP_BITS_1
#undef SPSW_STOP_BITS_1_5
#define SPSW_STOP_BITS_1_5 de_ibapl_spsw_provider_GenericWinSerialPortSocket_STOP_BITS_1_5
#undef SPSW_STOP_BITS_2
#define SPSW_STOP_BITS_2 de_ibapl_spsw_provider_GenericWinSerialPortSocket_STOP_BITS_2

#undef SPSW_PARITY_NONE
#define SPSW_PARITY_NONE de_ibapl_spsw_provider_GenericWinSerialPortSocket_PARITY_NONE
#undef SPSW_PARITY_ODD
#define SPSW_PARITY_ODD de_ibapl_spsw_provider_GenericWinSerialPortSocket_PARITY_ODD
#undef SPSW_PARITY_EVEN
#define SPSW_PARITY_EVEN de_ibapl_spsw_provider_GenericWinSerialPortSocket_PARITY_EVEN
#undef SPSW_PARITY_MARK
#define SPSW_PARITY_MARK de_ibapl_spsw_provider_GenericWinSerialPortSocket_PARITY_MARK
#undef SPSW_PARITY_SPACE
#define SPSW_PARITY_SPACE de_ibapl_spsw_provider_GenericWinSerialPortSocket_PARITY_SPACE

jfieldID spsw_portName; /* id for field 'portName'  */
jfieldID spsw_fd; /* id for field 'fd'  */
jclass spswClass;
jclass serialPortSocketFactoryImpl;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    jint getEnvResult = ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_2));
    if (getEnvResult != JNI_OK) {
        return getEnvResult;
    }

    spswClass = (*env)->FindClass(env, "Lde/ibapl/spsw/provider/GenericWinSerialPortSocket;");
    spsw_fd = (*env)->GetFieldID(env, spswClass, "fd", "J");
    spsw_portName = (*env)->GetFieldID(env, spswClass, "portName", "Ljava/lang/String;");

    // mark that the lib was loaded
    serialPortSocketFactoryImpl = (*env)->FindClass(env, "Lde/ibapl/spsw/provider/SerialPortSocketFactoryImpl;");
    jfieldID spsw_libLoaded = (*env)->GetStaticFieldID(env, serialPortSocketFactoryImpl, "libLoaded", "Z");
    (*env)->SetStaticBooleanField(env, serialPortSocketFactoryImpl, spsw_libLoaded, JNI_TRUE);
    return JNI_VERSION_1_2;
}

JNIEXPORT void JNICALL JNI_OnUnLoad(JavaVM *jvm, void *reserved) {
    JNIEnv *env;
    spsw_fd = 0;
    spsw_portName = 0;

    jint getEnvResult = ((*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_2));
    if (getEnvResult != JNI_OK) {
        return;
    }

    // mark that the lib was unloaded
    jfieldID spsw_libLoaded = (*env)->GetStaticFieldID(env, serialPortSocketFactoryImpl, "libLoaded", "Z");
    (*env)->SetStaticBooleanField(env, serialPortSocketFactoryImpl, spsw_libLoaded, JNI_FALSE);

    (*env)->DeleteLocalRef(env, spswClass);
    (*env)->DeleteLocalRef(env, serialPortSocketFactoryImpl);
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getWindowsBasedPortNames
 * Signature: (Z)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getWindowsBasedPortNames
(JNIEnv *env, jclass clazz, jboolean value) {
    HKEY phkResult;
    LPCSTR lpSubKey = "HARDWARE\\DEVICEMAP\\SERIALCOMM\\";
    jobjectArray returnArray = NULL;
    if (RegOpenKeyExA(HKEY_LOCAL_MACHINE, lpSubKey, 0, KEY_READ, &phkResult) == ERROR_SUCCESS) {
        boolean hasMoreElements = TRUE;
        DWORD keysCount = 0;
        char valueName[256];
        DWORD valueNameSize;
        DWORD enumResult;
        while (hasMoreElements) {
            valueNameSize = 256;
            enumResult = RegEnumValueA(phkResult, keysCount, valueName, &valueNameSize, NULL, NULL, NULL, NULL);
            if (enumResult == ERROR_SUCCESS) {
                keysCount++;
            } else if (enumResult == ERROR_NO_MORE_ITEMS) {
                hasMoreElements = FALSE;
            } else {
                hasMoreElements = FALSE;
            }
        }
        if (keysCount > 0) {
            jclass stringClass = (*env)->FindClass(env, "java/lang/String");
            returnArray = (*env)->NewObjectArray(env, (jsize) keysCount, stringClass, NULL);
            char lpValueName[256];
            DWORD lpcchValueName;
            byte lpData[256];
            DWORD lpcbData;
            DWORD result;
            for (DWORD i = 0; i < keysCount; i++) {
                lpcchValueName = 256;
                lpcbData = 256;
                result = RegEnumValueA(phkResult, i, lpValueName, &lpcchValueName, NULL, NULL, lpData, &lpcbData);
                if (result == ERROR_SUCCESS) {
                    (*env)->SetObjectArrayElement(env, returnArray, i, (*env)->NewStringUTF(env, (char*) lpData));
                }
            }
        }
        CloseHandle(phkResult);
    }
    return returnArray;
}


// Helper method

static void throw_SerialPortException_NativeError(JNIEnv *env, const char *msg) {
    char buf[2048];
    snprintf(buf, 2048, "%s: Unknown port error %d", msg, (int) GetLastError());
    const jclass speClass = (*env)->FindClass(env, "de/ibapl/spsw/api/SerialPortException");
    if (speClass != NULL) {
        (*env)->ThrowNew(env, speClass, buf);
        (*env)->DeleteLocalRef(env, speClass);
    }
}

static void throw_Illegal_Argument_Exception(JNIEnv *env, const char *msg) {
    const jclass cls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, msg);
        (*env)->DeleteLocalRef(env, cls);
    }
}

static void throw_PortBusyException(JNIEnv *env, jstring portName) {
    const char* port = (*env)->GetStringUTFChars(env, portName, JNI_FALSE);
    const jclass cls = (*env)->FindClass(env, "de/ibapl/spsw/api/PortBusyException");
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, port);
        (*env)->DeleteLocalRef(env, cls);
    }
    (*env)->ReleaseStringUTFChars(env, portName, port);
}

static void throw_PortNotFoundException(JNIEnv *env, jstring portName) {
    const char* port = (*env)->GetStringUTFChars(env, portName, JNI_FALSE);
    const jclass cls = (*env)->FindClass(env, "de/ibapl/spsw/api/PortNotFoundException");
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, port);
        (*env)->DeleteLocalRef(env, cls);
    }
    (*env)->ReleaseStringUTFChars(env, portName, port);
}

static void throw_NotASerialPortException(JNIEnv *env, jstring portName) {
    const char* port = (*env)->GetStringUTFChars(env, portName, JNI_FALSE);
    const jclass cls = (*env)->FindClass(env, "de/ibapl/spsw/api/NotASerialPortException");
    if (cls != NULL) {
        (*env)->ThrowNew(env, cls, port);
        (*env)->DeleteLocalRef(env, cls);
    }
    (*env)->ReleaseStringUTFChars(env, portName, port);
}

static void throw_SerialPortException_Closed(JNIEnv *env) {
    const jclass speClass = (*env)->FindClass(env, "de/ibapl/spsw/api/SerialPortException");
    if (speClass != NULL) {
        const jfieldID spe_spsc = (*env)->GetStaticFieldID(env, speClass, "SERIAL_PORT_SOCKET_CLOSED", "Ljava/lang/String;");
        const jmethodID speConstructor = (*env)->GetMethodID(env, speClass, "<init>", "(Ljava/lang/String;)V");
        const jobject speEx = (*env)->NewObject(env, speClass, speConstructor, (*env)->GetStaticObjectField(env, speClass, spe_spsc));
        (*env)->Throw(env, speEx);
        (*env)->DeleteLocalRef(env, speClass);
    }
}

static void throw_InterruptedIOExceptionWithError(JNIEnv *env, int bytesTransferred, const char *msg) {
    char buf[2048];
    snprintf(buf, 2048, "%s: Unknown port native win error: %d", msg, (int) GetLastError());
    const jclass iioeClass = (*env)->FindClass(env, "java/io/InterruptedIOException");
    if (iioeClass != NULL) {
        const jmethodID iioeConstructor = (*env)->GetMethodID(env, iioeClass, "<init>", "(Ljava/lang/String;)V");
        const jobject iioeEx = (*env)->NewObject(env, iioeClass, iioeConstructor, (*env)->NewStringUTF(env, buf));
        const jfieldID bytesTransferredId = (*env)->GetFieldID(env, iioeClass, "bytesTransferred", "I");
        (*env)->SetIntField(env, iioeEx, bytesTransferredId, bytesTransferred);
        (*env)->Throw(env, iioeEx);
        (*env)->DeleteLocalRef(env, iioeClass);
    }
}

static void throw_TimeoutIOException(JNIEnv *env, int bytesTransferred) {
    const jclass tioeClass = (*env)->FindClass(env, "de/ibapl/spsw/api/TimeoutIOException");
    if (tioeClass != NULL) {
        const jmethodID tioeConstructor = (*env)->GetMethodID(env, tioeClass, "<init>", "(Ljava/lang/String;)V");
        const jobject tioeEx = (*env)->NewObject(env, tioeClass, tioeConstructor, (*env)->NewStringUTF(env, "Timeout"));
        const jfieldID bytesTransferredId = (*env)->GetFieldID(env, tioeClass, "bytesTransferred", "I");
        (*env)->SetIntField(env, tioeEx, bytesTransferredId, bytesTransferred);
        (*env)->Throw(env, tioeEx);
        (*env)->DeleteLocalRef(env, tioeClass);
    }
}

static jboolean getCommModemStatus(JNIEnv *env, jobject object, DWORD bitMask) {
    DWORD lpModemStat;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommModemStatus(hFile, &lpModemStat)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return JNI_FALSE;
        } else {
            throw_SerialPortException_NativeError(env, "Can't get GetCommModemStatus");
            return JNI_FALSE;
        }
    }
    if ((lpModemStat & bitMask) == bitMask) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

/*
 * Port opening.
 *
 * In 2.2.0 added useTIOCEXCL (not used in Windows, only for compatibility with _nix version)
 * 
 *  
 * Use overlapped otherwise any blocked ReadFile will block a WriteFile.
 * 
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_open(JNIEnv *env, jobject object, jstring portName, jint portMode) {
    //Do not try to reopen port and therefor failing and overriding the filedescriptor
#if defined(__i386)
    if ((*env)->GetLongField(env, object, spsw_fd) != (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
    if ((*env)->GetLongField(env, object, spsw_fd) != (jlong) INVALID_HANDLE_VALUE) {
#endif
        throw_SerialPortException_NativeError(env, "serial port socket has valid filedescriptor!");
        return;
    }

    char prefix[] = "\\\\.\\";
    const char* port = (*env)->GetStringUTFChars(env, portName, JNI_FALSE);

    //string concat fix
    char portFullName[strlen(prefix) + strlen(port) + 1];
    strcpy(portFullName, prefix);
    strcat(portFullName, port);
    (*env)->ReleaseStringUTFChars(env, portName, port);

    HANDLE hFile = CreateFile(portFullName,
            GENERIC_READ | GENERIC_WRITE,
            0,
            NULL,
            OPEN_EXISTING,
            FILE_FLAG_OVERLAPPED,
            NULL);

    if (hFile == INVALID_HANDLE_VALUE) {

#if defined(__i386)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

        switch (GetLastError()) {
            case ERROR_ACCESS_DENIED:
                throw_PortBusyException(env, portName);
                break;
            case ERROR_FILE_NOT_FOUND:
                throw_PortNotFoundException(env, portName);
                break;
            default:
                throw_SerialPortException_NativeError(env, "Open");
        }
        return;
    }

    DCB dcb;
    if (!GetCommState(hFile, &dcb)) {
        CloseHandle(hFile); //since 2.7.0

#if defined(__i386)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

        throw_NotASerialPortException(env, portName);
        return;
    }

    COMMTIMEOUTS lpCommTimeouts;
    if (!GetCommTimeouts(hFile, &lpCommTimeouts)) {
        CloseHandle(hFile);

#if defined(__i386)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

        throw_SerialPortException_NativeError(env, "Open GetCommTimeouts");
        return;
    }

    switch (portMode) {
        case SPSW_PORT_MODE_UNCHANGED:
            break;
        case SPSW_PORT_MODE_RAW:
            lpCommTimeouts.ReadIntervalTimeout = 100;
            lpCommTimeouts.ReadTotalTimeoutConstant = 0;
            lpCommTimeouts.ReadTotalTimeoutMultiplier = 0;
            lpCommTimeouts.WriteTotalTimeoutConstant = 0;
            lpCommTimeouts.WriteTotalTimeoutMultiplier = 0;
            break;
        default:
            CloseHandle(hFile);

#if defined(__i386)
            (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
            (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

            throw_SerialPortException_NativeError(env, "Unknown terminal mode giving up");
            return;
    }

    if (!SetCommTimeouts(hFile, &lpCommTimeouts)) {
        CloseHandle(hFile);

#if defined(__i386)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
        (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

        throw_SerialPortException_NativeError(env, "Open SetCommTimeouts");
        return;
    }

#if defined(__i386)
    (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) hFile);
#elif defined(__x86_64)
    (*env)->SetLongField(env, object, spsw_fd, (jlong) hFile);
#endif
}

/*
 * Port closing
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_close0
(JNIEnv *env, jobject object) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
    (*env)->SetLongField(env, object, spsw_fd, (jlong) (DWORD) INVALID_HANDLE_VALUE);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
    (*env)->SetLongField(env, object, spsw_fd, (jlong) INVALID_HANDLE_VALUE);
#endif

    if (!CloseHandle(hFile)) {
        throw_SerialPortException_NativeError(env, "Can't close port");
    }
}

/*
 * Change RTS line state (ON || OFF)
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setRTS
(JNIEnv *env, jobject object, jboolean enabled) {
    DWORD dwFunc = (enabled == JNI_TRUE) ? SETRTS : CLRRTS;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!EscapeCommFunction(hFile, dwFunc)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "Can't set/clear RTS");
            return;
        }
    }
}

JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_sendXON
(JNIEnv *env, jobject object) {
    throw_SerialPortException_NativeError(env, "setXON not implementred yet");
}

JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_sendXOFF
(JNIEnv *env, jobject object) {
    throw_SerialPortException_NativeError(env, "setXOFF not implementred yet");
}

/*
 * Change DTR line state (ON || OFF)
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setDTR
(JNIEnv *env, jobject object, jboolean enabled) {
    DWORD dwFunc = (enabled == JNI_TRUE) ? SETDTR : CLRDTR;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!EscapeCommFunction(hFile, dwFunc)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "Can't set/clear DTR");
            return;
        }
    }
}

/* 
 * BRK line status changing (ON || OFF)
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setBreak
(JNIEnv *env, jobject object, jboolean enabled) {
    DWORD dwFunc = (enabled == JNI_TRUE) ? SETBREAK : CLRBREAK;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!EscapeCommFunction(hFile, dwFunc)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "Can't set/clear BREAK");
            return;
        }
    }
}

JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setXONChar
(JNIEnv *env, jobject object, jchar c) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setXONChar GetCommState");
            return;
        }
    }
    dcb.XonChar = c;
    if (!SetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setXONChar SetCommState");
            return;
        }
    }
}

JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setXOFFChar
(JNIEnv *env, jobject object, jchar c) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setXOFFChar GetCommState");
            return;
        }
    }
    dcb.XoffChar = c;
    if (!SetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setXOFFChar SetCommState");
            return;
        }
    }
}

JNIEXPORT jchar JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getXONChar
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return 0;
        } else {
            throw_SerialPortException_NativeError(env, "getXONChar GetCommState");
            return 0;
        }
    }
    return dcb.XonChar;
}

JNIEXPORT jchar JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getXOFFChar
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return 0;
        } else {
            throw_SerialPortException_NativeError(env, "getXOFFChar GetCommState");
            return 0;
        }
    }
    return dcb.XoffChar;
}

JNIEXPORT jboolean JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_isCTS
(JNIEnv *env, jobject object) {
    return getCommModemStatus(env, object, MS_CTS_ON);
}

JNIEXPORT jboolean JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_isDSR
(JNIEnv *env, jobject object) {
    return getCommModemStatus(env, object, MS_DSR_ON);
}

JNIEXPORT jboolean JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_isIncommingRI
(JNIEnv *env, jobject object) {
    return getCommModemStatus(env, object, MS_RING_ON);
}

JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_writeSingle
(JNIEnv *env, jobject object, jint b) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    OVERLAPPED overlapped;
    DWORD dwBytesWritten;
    overlapped.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);

    if (!WriteFile(hFile, &b, 1, NULL, &overlapped)) {

        if (GetLastError() != ERROR_IO_PENDING) {
            CloseHandle(overlapped.hEvent);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                throw_SerialPortException_Closed(env);
            } else {
                throw_SerialPortException_NativeError(env, "Error writeSingle (GetLastError)");
            }
            return;
        }

        if (WaitForSingleObject(overlapped.hEvent, INFINITE) != WAIT_OBJECT_0) {
            CloseHandle(overlapped.hEvent);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                throw_SerialPortException_Closed(env);
                return;
            } else {
                throw_SerialPortException_NativeError(env, "Error writeSingle (WaitForSingleObject)");
                return;
            }
        }

    }

    if (!GetOverlappedResult(hFile, &overlapped, &dwBytesWritten, FALSE)) {
        CloseHandle(overlapped.hEvent);
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "Error writeSingle (GetOverlappedResult)");
            return;
        }
    }

    CloseHandle(overlapped.hEvent);
    if (dwBytesWritten != 1) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            if (GetLastError() == ERROR_IO_PENDING) {
                throw_TimeoutIOException(env, dwBytesWritten);
            } else {
                throw_SerialPortException_NativeError(env, "Error writeSingle");
            }
            return;
        }
    }

    //Success
    return;
}

/*
 * Write data to port
 * portHandle - port handle
 * buffer - byte array for sending
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_writeBytes
(JNIEnv *env, jobject object, jbyteArray bytes, jint off, jint len) {

    jbyte *buf = (jbyte*) malloc(len);
    (*env)->GetByteArrayRegion(env, bytes, off, len, buf);

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    DWORD dwBytesWritten;
    OVERLAPPED overlapped;
    overlapped.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);

    if (!WriteFile(hFile, buf, len, NULL, &overlapped)) {

        if (GetLastError() != ERROR_IO_PENDING) {
            CloseHandle(overlapped.hEvent);
            free(buf);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                throw_SerialPortException_Closed(env);
                return;
            } else {
                throw_SerialPortException_NativeError(env, "Error writeBytes (GetLastError)");
                return;
            }
        }

        if (WaitForSingleObject(overlapped.hEvent, INFINITE) != WAIT_OBJECT_0) {
            CloseHandle(overlapped.hEvent);
            free(buf);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                throw_SerialPortException_Closed(env);
                return;
            } else {
                throw_SerialPortException_NativeError(env, "Error writeBytes (WaitForSingleObject)");
                return;
            }
        }

    }

    if (!GetOverlappedResult(hFile, &overlapped, &dwBytesWritten, FALSE)) {
        CloseHandle(overlapped.hEvent);
        free(buf);
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "Error writeBytes (GetOverlappedResult)");
            return;
        }
    }

    CloseHandle(overlapped.hEvent);
    free(buf);
    if (dwBytesWritten != len) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            if (GetLastError() == ERROR_IO_PENDING) {
                throw_TimeoutIOException(env, dwBytesWritten);
            } else {
                throw_SerialPortException_NativeError(env, "Error writeBytes too view written");
            }
            return;
        }
    }

    //Success
    return;
}

JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_readSingle
(JNIEnv *env, jobject object) {

    jbyte lpBuffer;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    OVERLAPPED overlapped;
    DWORD dwBytesRead;
    overlapped.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);

    if (!ReadFile(hFile, &lpBuffer, 1, NULL, &overlapped)) {

        if (GetLastError() != ERROR_IO_PENDING) {
            CloseHandle(overlapped.hEvent);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                return -1;
            } else {
                throw_SerialPortException_NativeError(env, "Error readSingle (GetLastError)");
                return -1;
            }
        }

        if (WaitForSingleObject(overlapped.hEvent, INFINITE) != WAIT_OBJECT_0) {
            CloseHandle(overlapped.hEvent);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                return -1;
            } else {
                throw_SerialPortException_NativeError(env, "Error readSingle (WaitForSingleObject)");
                return -1;
            }
        }

    }

    if (!GetOverlappedResult(hFile, &overlapped, &dwBytesRead, FALSE)) {
        CloseHandle(overlapped.hEvent);
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            return -1;
        } else {
            throw_TimeoutIOException(env, dwBytesRead);
            return -1;
        }
    }

    CloseHandle(overlapped.hEvent);
    if (dwBytesRead == 1) {
        //Success
        return lpBuffer & 0xFF;
    } else if (dwBytesRead == 0) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_TimeoutIOException(env, dwBytesRead);
            return -1;
        }
    } else {
        throw_InterruptedIOExceptionWithError(env, dwBytesRead, "Should never happen! readSingle dwBytes < 0");
        return -1;
    }

    throw_SerialPortException_NativeError(env, "Should never happen! readSingle falltrough");
    return -1;
}

/*
 * Read data from port
 * portHandle - port handle
 * byteCount - count of bytes for reading
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_readBytes
(JNIEnv *env, jobject object, jbyteArray bytes, jint off, jint len) {

    jbyte *lpBuffer = (jbyte*) malloc(len);

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    DWORD dwBytesRead;
    OVERLAPPED overlapped;
    overlapped.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);

    if (!ReadFile(hFile, lpBuffer, len, NULL, &overlapped)) {

        if (GetLastError() != ERROR_IO_PENDING) {
            CloseHandle(overlapped.hEvent);
            free(lpBuffer);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                return -1;
            } else {
                throw_SerialPortException_NativeError(env, "Error readBytes (GetLastError)");
                return -1;
            }
        }

        //overlapped path
        if (WaitForSingleObject(overlapped.hEvent, INFINITE) != WAIT_OBJECT_0) {
            CloseHandle(overlapped.hEvent);
            free(lpBuffer);
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                return -1;
            } else {
                throw_SerialPortException_NativeError(env, "Error readBytes (WaitForSingleObject)");
                return -1;
            }
        }

    }

    if (!GetOverlappedResult(hFile, &overlapped, &dwBytesRead, FALSE)) {
        CloseHandle(overlapped.hEvent);
        free(lpBuffer);
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            return -1;
        } else {
            throw_TimeoutIOException(env, dwBytesRead);
            return -1;
        }
    }

    CloseHandle(overlapped.hEvent);

    (*env)->SetByteArrayRegion(env, bytes, off, dwBytesRead, lpBuffer);

    free(lpBuffer);

    if (dwBytesRead > 0) {
        //Success
        return dwBytesRead;
    } else if (dwBytesRead == 0) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            return -1;
        } else {
            throw_TimeoutIOException(env, dwBytesRead);
            return -1;
        }
    } else {
        throw_SerialPortException_NativeError(env, "Should never happen! readBytes dwBytes < 0");
        return -1;
    }


    throw_SerialPortException_NativeError(env, "Should never happen! readBytes falltrough");
    return -1;
}

JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getInBufferBytesCount
(JNIEnv *env, jobject object) {
    DWORD lpErrors;
    COMSTAT comstat;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (ClearCommError(hFile, &lpErrors, &comstat)) {
        return (jint) comstat.cbInQue;
    } else {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getInBufferBytesCount ClearCommError");
            return -1;
        }
    }
}

JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getOutBufferBytesCount
(JNIEnv *env, jobject object) {
    DWORD lpErrors;
    COMSTAT comstat;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (ClearCommError(hFile, &lpErrors, &comstat)) {
        return (jint) comstat.cbOutQue;
    } else {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getOutBufferBytesCount ClearCommError");
            return -1;
        }
    }
}

/*
 * Setting flow control mode
 *
 * since 0.8
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setFlowControl
(JNIEnv *env, jobject object, jint mask) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setFlowControl GetCommState");
            return;
        }
    }
    dcb.fRtsControl = RTS_CONTROL_ENABLE;
    dcb.fOutxCtsFlow = FALSE;
    dcb.fOutX = FALSE;
    dcb.fInX = FALSE;
    if (mask != SPSW_FLOW_CONTROL_NONE) {
        if ((mask & SPSW_FLOW_CONTROL_RTS_CTS_IN) == SPSW_FLOW_CONTROL_RTS_CTS_IN) {
            dcb.fRtsControl = RTS_CONTROL_HANDSHAKE;
        }
        if ((mask & SPSW_FLOW_CONTROL_RTS_CTS_OUT) == SPSW_FLOW_CONTROL_RTS_CTS_OUT) {
            dcb.fOutxCtsFlow = TRUE;
        }
        if ((mask & SPSW_FLOW_CONTROL_XON_XOFF_IN) == SPSW_FLOW_CONTROL_XON_XOFF_IN) {
            dcb.fInX = TRUE;
        }
        if ((mask & SPSW_FLOW_CONTROL_XON_XOFF_OUT) == SPSW_FLOW_CONTROL_XON_XOFF_OUT) {
            dcb.fOutX = TRUE;
        }
    }
    if (!SetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setFlowControl SetCommState");
            return;
        }
    }

}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    setBaudrate
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setBaudrate
(JNIEnv *env, jobject object, jint baudRate) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setBaudrate GetCommState");
            return;
        }
    }

    dcb.BaudRate = baudRate;

    if (!SetCommState(hFile, &dcb)) {
        if (GetLastError() == ERROR_INVALID_PARAMETER) {
            throw_Illegal_Argument_Exception(env, "setBaudrate: Wrong Baudrate");
            return;
        }
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setBaudrate SetCommState");
            return;
        }
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    setTimeouts
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setTimeouts
(JNIEnv *env, jobject object, jint interByteReadTimeout, jint overallReadTimeout, jint overallWriteTimeout) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    COMMTIMEOUTS lpCommTimeouts;
    if (!GetCommTimeouts(hFile, &lpCommTimeouts)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setTimeouts");
            return;
        }
    }

    if (overallWriteTimeout < 0) {
        throw_Illegal_Argument_Exception(env, "setTimeouts: overallWriteTimeout must >= 0");
        return;
    } else if (overallWriteTimeout == MAXDWORD) {
        //MAXDWORD has a special meaning...
        overallWriteTimeout = MAXDWORD - 1;
    }

    if (overallReadTimeout < 0) {
        throw_Illegal_Argument_Exception(env, "setTimeouts: overallReadTimeout must >= 0");
        return;
    } else if (overallReadTimeout == MAXDWORD) {
        //MAXDWORD has a special meaning...
        overallReadTimeout = MAXDWORD - 1;
    }

    if (interByteReadTimeout < 0) {
        throw_Illegal_Argument_Exception(env, "setReadTimeouts: interByteReadTimeout must >= 0");
        return;
    } else if (interByteReadTimeout == MAXDWORD) {
        //MAXDWORD has a special meaning...
        interByteReadTimeout = MAXDWORD - 1;
    }

    if ((interByteReadTimeout == 0) && (overallReadTimeout > 0)) {
        //This fits best for wait a timeout and have no interByteReadTimeout see also getInterbyteReadTimeout for reading back
        lpCommTimeouts.ReadIntervalTimeout = MAXDWORD;
        lpCommTimeouts.ReadTotalTimeoutMultiplier = MAXDWORD;
        lpCommTimeouts.ReadTotalTimeoutConstant = overallReadTimeout;
    } else {
        lpCommTimeouts.ReadIntervalTimeout = interByteReadTimeout;
        lpCommTimeouts.ReadTotalTimeoutMultiplier = 0;
        lpCommTimeouts.ReadTotalTimeoutConstant = overallReadTimeout;
    }


    lpCommTimeouts.WriteTotalTimeoutMultiplier = 0;
    lpCommTimeouts.WriteTotalTimeoutConstant = overallWriteTimeout;


    if (!SetCommTimeouts(hFile, &lpCommTimeouts)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setTimeouts SetCommTimeouts");
            return;
        }
    }
}

/*
 * Send break for setted duration
 *
 * since 0.8
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_sendBreak
(JNIEnv *env, jobject object, jint duration) {
#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (duration < 0) {
        throw_SerialPortException_NativeError(env, "sendBreak duration must be grater than 0");
        return;
    }

    if (!SetCommBreak(hFile)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "sendBreak SetCommBreak");
            return;
        }
    }

    Sleep(duration);

    if (!ClearCommBreak(hFile)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "sendBreak ClearCommBreak");
            return;
        }
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    setDataBits
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setDataBits
(JNIEnv *env, jobject object, jint dataBits) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setDataBits GetCommState");
            return;
        }
    }

    dcb.ByteSize = dataBits;

    if (!SetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setDataBits SetCommState");
            return;
        }
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    setStopBits
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setStopBits
(JNIEnv *env, jobject object, jint stopBits) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setStopBits GetCommState");
            return;
        }
    }

    switch (stopBits) {
        case SPSW_STOP_BITS_1:
            dcb.StopBits = ONESTOPBIT;
            break;
        case SPSW_STOP_BITS_1_5:
            dcb.StopBits = ONE5STOPBITS;
            break;
        case SPSW_STOP_BITS_2:
            dcb.StopBits = TWOSTOPBITS;
            break;
        default:
            throw_Illegal_Argument_Exception(env, "setStopBits Unknown stopbits");
            return;
    }


    if (!SetCommState(hFile, &dcb)) {
        if (GetLastError() == ERROR_INVALID_PARAMETER) {
            throw_Illegal_Argument_Exception(env, "setStopbits value not supported");
            return;
        } else {
#if defined(__i386)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
            if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
                throw_SerialPortException_Closed(env);
                return;
            } else {
                throw_SerialPortException_NativeError(env, "setStopBits SetCommState");
                return;
            }
        }
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    setParity
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_setParity
(JNIEnv *env, jobject object, jint parity) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setParity GetCommState");
            return;
        }
    }

    switch (parity) {
        case SPSW_PARITY_NONE:
            dcb.Parity = NOPARITY; // switch parity input checking off
            break;
        case SPSW_PARITY_ODD:
            dcb.Parity = ODDPARITY;
            break;
        case SPSW_PARITY_EVEN:
            dcb.Parity = EVENPARITY;
            break;
        case SPSW_PARITY_MARK:
            dcb.Parity = MARKPARITY;
            break;
        case SPSW_PARITY_SPACE:
            dcb.Parity = SPACEPARITY;
            break;
        default:
            throw_SerialPortException_NativeError(env, "setParity Wrong Parity");
            return;
    }

    if (!SetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return;
        } else {
            throw_SerialPortException_NativeError(env, "setParity SetCommState");
            return;
        }
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getBaudrate
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getBaudrate0
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getBaudrate0 GetCommState");
            return -1;
        }
    }
    return dcb.BaudRate;
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getDataBits
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getDataBits0
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return-1;
        } else {
            throw_SerialPortException_NativeError(env, "getDataBits0 GetCommState");
            return -1;
        }
    }
    return dcb.ByteSize;
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getStopBits
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getStopBits0
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return-1;
        } else {
            throw_SerialPortException_NativeError(env, "getStopBits0 GetCommState");
            return -1;
        }
    }

    switch (dcb.StopBits) {
        case ONESTOPBIT:
            return SPSW_STOP_BITS_1;
        case ONE5STOPBITS:
            return SPSW_STOP_BITS_1_5;
        case TWOSTOPBITS:
            return SPSW_STOP_BITS_2;
        default:
            throw_Illegal_Argument_Exception(env, "getStopBits0 Unknown stopbits");
            return -1;
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getParity0
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getParity0
(JNIEnv *env, jobject object) {
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return-1;
        } else {
            throw_SerialPortException_NativeError(env, "getParity0 GetCommState");
            return -1;
        }
    }

    switch (dcb.Parity) {
        case NOPARITY:
            return SPSW_PARITY_NONE;
        case ODDPARITY:
            return SPSW_PARITY_ODD;
        case EVENPARITY:
            return SPSW_PARITY_EVEN;
        case MARKPARITY:
            return SPSW_PARITY_MARK;
        case SPACEPARITY:
            return SPSW_PARITY_SPACE;
        default:
            throw_Illegal_Argument_Exception(env, "getParity0 unknown Parity");
            return -1;
    }
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getOverallWriteTimeout
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getOverallWriteTimeout
(JNIEnv *env, jobject object) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    COMMTIMEOUTS lpCommTimeouts;
    if (!GetCommTimeouts(hFile, &lpCommTimeouts)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getOverallWriteTimeout");
            return -1;
        }
    }
    return lpCommTimeouts.WriteTotalTimeoutConstant;
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getOverallReadTimeout
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getOverallReadTimeout
(JNIEnv *env, jobject object) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    COMMTIMEOUTS lpCommTimeouts;
    if (!GetCommTimeouts(hFile, &lpCommTimeouts)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getOverallReadTimeout");
            return -1;
        }
    }
    return lpCommTimeouts.ReadTotalTimeoutConstant;
}

/*
 * Class:     de_ibapl_spsw_provider_GenericWinSerialPortSocket
 * Method:    getInterByteReadTimeout
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getInterByteReadTimeout
(JNIEnv *env, jobject object) {

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    COMMTIMEOUTS lpCommTimeouts;
    if (!GetCommTimeouts(hFile, &lpCommTimeouts)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return -1;
        } else {
            throw_SerialPortException_NativeError(env, "getInterByteReadTimeout");
            return -1;
        }
    }
    if ((lpCommTimeouts.ReadIntervalTimeout == MAXDWORD) && (lpCommTimeouts.ReadTotalTimeoutMultiplier == MAXDWORD)) {
        return 0;
    } else {
        return lpCommTimeouts.ReadIntervalTimeout;
    }
}

/*
 * Getting flow control mode
 *
 * since 0.8
 */
JNIEXPORT jint JNICALL Java_de_ibapl_spsw_provider_GenericWinSerialPortSocket_getFlowControl0
(JNIEnv *env, jobject object) {
    jint returnValue = 0;
    DCB dcb;

#if defined(__i386)
    HANDLE hFile = (HANDLE) (DWORD) (*env)->GetLongField(env, object, spsw_fd);
#elif defined(__x86_64)
    HANDLE hFile = (HANDLE) (*env)->GetLongField(env, object, spsw_fd);
#endif

    if (!GetCommState(hFile, &dcb)) {
#if defined(__i386)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) (DWORD) INVALID_HANDLE_VALUE) {
#elif defined(__x86_64)
        if ((*env)->GetLongField(env, object, spsw_fd) == (jlong) INVALID_HANDLE_VALUE) {
#endif
            throw_SerialPortException_Closed(env);
            return-1;
        } else {
            throw_SerialPortException_NativeError(env, "getFlowControl0 GetCommState");
            return -1;
        }
    }

    if (dcb.fRtsControl == RTS_CONTROL_HANDSHAKE) {
        returnValue |= SPSW_FLOW_CONTROL_RTS_CTS_IN;
    }
    if (dcb.fOutxCtsFlow == TRUE) {
        returnValue |= SPSW_FLOW_CONTROL_RTS_CTS_OUT;
    }
    if (dcb.fInX == TRUE) {
        returnValue |= SPSW_FLOW_CONTROL_XON_XOFF_IN;
    }
    if (dcb.fOutX == TRUE) {
        returnValue |= SPSW_FLOW_CONTROL_XON_XOFF_OUT;
    }
    return returnValue;
}
