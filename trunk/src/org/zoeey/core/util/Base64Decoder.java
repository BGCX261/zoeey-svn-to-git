/*
 * MoXie (SysTem128@GMail.Com) 2009-8-10 11:01:02
 * $Id: Base64Decoder.java 75 2010-03-16 02:21:41Z MoXie $
 * 
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zoeey.core.constant.EnvConstants;

/**
 * <pre>
 * Base64 解码
 * Wikipedia: <a href="http://en.wikipedia.org/wiki/Base64" >Base64</a>
 * </pre>
 * @author MoXie
 */
class Base64Decoder {

    /**
     * 64位字母表
     */
    private final static char[] alphabet = {
        // 0    1    2    3    4    5    6    7
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
        'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
        '4', '5', '6', '7', '8', '9', '+', '/' // 7
    };
    /**
     * 对位表
     */
    private static byte alphabet_byte[] = new byte[256];

    static {
        for (int i = 0; i < 255; i++) {
            alphabet_byte[i] = -1;
        }
        for (int i = 0; i < alphabet.length; i++) {
            alphabet_byte[alphabet[i]] = (byte) i;
        }
    }

    /**
     * 对字符串进行解码
     * @param str
     * @return
     */
    public String decode(String str) {
        try {
            if (str == null) {
                return str;
            }
            return new String(decode(str.getBytes(EnvConstants.CHARSET)), EnvConstants.CHARSET);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Base64Decoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 对位解码
     * @param pre
     * @param suf
     * @param pos
     * @return
     */
    private byte decode(byte pre, byte suf, int pos) {
        int outInt = 0;
        pre = alphabet_byte[pre & 0xFF];
        suf = alphabet_byte[suf & 0xFF];
        if (pos % 3 == 1) {
            pos = 1;
        } else if (pos % 3 == 2) {
            pos = 2;
        } else {
            pos = 3;
        }
        switch (pos) {
            case 1:
                outInt = (((pre << 2) & 0xFC) | ((suf >>> 4) & 3));
                break;
            case 2:
                outInt = (((pre << 4) & 0xF0) | ((suf >>> 2) & 0xF));
                break;
            case 3:
                outInt = (((pre << 6) & 0xC0) | (suf & 0x3F));
                break;
        }
        return (byte) outInt;

    }

    /**
     * 对字节序列解码
     * @param inBytes
     * @return
     */
    public byte[] decode(byte[] inBytes) {
        int inLength = inBytes.length;
        int outLengt = (inBytes.length / 4) * 3;
        if (outLengt < 4) {
            return inBytes;
        }
        int trim = 0;
        if (inBytes[inBytes.length - 1] == '=') {
            trim++;
        }
        if (inBytes[inBytes.length - 2] == '=') {
            trim++;
        }
        outLengt -= trim;
        byte[] outBytes = new byte[outLengt];
        int inIndex = 0;
        int outIndex = 0;
        int index = 0;
        while (index < inLength) {
            if (outIndex == outLengt) {
                break;
            }
            outBytes[outIndex++] = decode(inBytes[inIndex], inBytes[++inIndex], index + 1);
            if (inIndex == inLength || inBytes[inIndex] == '=') {
                break;
            }
            if ((index + 1) % 3 == 0) {
                inIndex++;
            }
            index++;
        }
        return outBytes;
    }
}
