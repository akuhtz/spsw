package de.ibapl.jnrheader.api.windows;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import de.ibapl.jnrheader.JnrHeader;
import de.ibapl.jnrheader.Wrapper;
import java.lang.ref.SoftReference;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.byref.ByReference;

@Wrapper("minwindef.h")
public abstract class Minwindef_H implements JnrHeader {

    private final static ThreadLocal<SoftReference<CharsetEncoder>> WIDE_ENCODER = new ThreadLocal<>();
    private final static ThreadLocal<SoftReference<CharsetDecoder>> WIDE_DECODER = new ThreadLocal<>();
    
    private static CharsetEncoder getWideEncoder() {
        SoftReference<CharsetEncoder> cse = WIDE_ENCODER.get();
        if (cse == null) {
            cse = new SoftReference<>(Charset.forName(JnrHeader.UTF16_LE_ENCODING).newEncoder());
            WIDE_ENCODER.set(cse);
        }
        return cse.get();
    }
    
    private static CharsetDecoder getWideDecoder() {
        SoftReference<CharsetDecoder> csd = WIDE_DECODER.get();
        if (csd == null) {
            csd = new SoftReference<>(Charset.forName(JnrHeader.UTF16_LE_ENCODING).newDecoder());
            WIDE_DECODER.set(csd);
        }
        return csd.get();
    }
    
    
    
    public static class LPVOID {

    }

    public static class HKEY extends HANDLE {

        private HKEY(long value) {
            super(value);
        }

        public static HKEY ofLong(long value) {
            return new HKEY(value);
        }

    }

    private static class PHKEYImpl extends PHKEY implements ByReference<HKEY> {
        
        @Override
        public int nativeSize(Runtime runtime) {
            return 4; //TODO WIN 32 ??
        }

        @Override
        public void toNative(Runtime runtime, Pointer memory, long offset) {
            memory.putLong(offset, value.value);
        }

        @Override
        public void fromNative(Runtime runtime, Pointer memory, long offset) {
            value.value = memory.getLong(offset);
        }
        
        @Override
        public HKEY getValue() {
            return value;
        }

    }

    public static class PHKEY {

        public HKEY value;
        
        private PHKEY() {
        }
        
        public static PHKEY newPHKEY() {
            return new PHKEYImpl();
        }
        
    }

    public static class HRESULT {

        public static HRESULT of(int hresult) {
            return new HRESULT(hresult);
        }

        public final int hresult;

        private HRESULT(int hresult) {
            this.hresult = hresult;
        }

    }

    private static class LPByteImpl extends LPBYTE implements ByReference<Byte>{

        @Override
        public int nativeSize(Runtime runtime) {
            return 1;
        }

        @Override
        public void toNative(Runtime runtime, jnr.ffi.Pointer memory, long offset) {
            memory.putByte(offset, value);
        }

        @Override
        public void fromNative(Runtime runtime, jnr.ffi.Pointer memory, long offset) {
            value = memory.getByte(offset);
        }

        @Override
        public Byte getValue() {
            return Byte.valueOf(value);
        }
        
    }
    
    public static class LPBYTE {

        public byte value;

        private LPBYTE() {
        }
        
        public static LPBYTE ofValue(byte value) {
            final LPBYTE result = new LPByteImpl();
            result.value = value;
            return result;
        }
        
        
    }
    
    private static class LPDWord_impl extends LPDWORD implements ByReference<Long> {
        
        @Override
        public int nativeSize(Runtime runtime) {
            return 4;
        }

        @Override
        public void toNative(Runtime runtime, jnr.ffi.Pointer memory, long offset) {
            memory.putInt(offset, ((int)value) & 0xFFFFFFFF);
        }

        @Override
        public void fromNative(Runtime runtime, jnr.ffi.Pointer memory, long offset) {
            value = memory.getInt(offset) & 0xFFFFFFFF;
        }

        @Override
        public Long getValue() {
            return Long.valueOf(value);
        }
        
    }
    
    
    public static class LPDWORD {

        
        public long value;
        
        private LPDWORD () {
            
        }

        public static LPDWORD ofValue(long value) {
        	LPDWord_impl result = new LPDWord_impl();
        	result.value = value;
        	return result;
        }

    }

    public static class HANDLE {

        public long value;

        protected HANDLE(long value) {
            this.value = value;
        }

        public static HANDLE of(long value) {
            return new HANDLE(value);
        }
    }

    
        /**
         * 
         * The wrapper for a ByteBuffer.
         * The position of the buffer is always 0! It must be reset to 0 if changed.
         * The limit of the buffer is always amount of valid bytes in the buffer and must be set if the amount of valid bytes changed.
         */
    public static class LPWSTR {
        
        private ByteBuffer buffer;
        
        private LPWSTR () {
        }
        
        
        public static LPWSTR of(String value) {
            LPWSTR result = new LPWSTR();
            CharsetEncoder cse = getWideEncoder();
            assert (int)Math.ceil(cse.maxBytesPerChar()) == 2;
            result.buffer = ByteBuffer.allocateDirect(value.length() * 2);
            CharBuffer cb = CharBuffer.wrap(value);
            cse.encode(cb, result.buffer, true);
            result.buffer.flip();
            return result;
        }
        
        public static LPWSTR allocate(int capacity) {
            LPWSTR result = new LPWSTR();
            result.buffer = ByteBuffer.allocateDirect(capacity);
            return result;
        }
        
        
        public String toString() {
            CharsetDecoder csd = getWideDecoder();
            CharBuffer cb;
            try {
                cb = csd.decode(buffer);
                buffer.position(0);
                return cb.toString();
            } catch (CharacterCodingException ex) {
                Logger.getLogger(Minwindef_H.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }

        public Buffer backingBuffer() {
            return buffer;
        }
    }
    /*
         *Naming of Strings:
        LP == Long Pointer. Just think pointer or char*

C = Const, in this case, I think they mean the character string is a const, not the pointer being const.

STR is string

the T is for a wide character or char (TCHAR) depending on compile options.
     */
 }
