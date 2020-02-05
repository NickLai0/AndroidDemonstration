package com.wav.util;

/**
 * ******************(^_^)***********************<br>
 * User: Nick<br>
 * Date: 2020/2/4<br>
 * Time: 16:33<br>
 * <P>DESC:
 * </p >
 * ******************(^_^)***********************
 */
public final class WavUtils {

    public static byte[] makeSimpleWavHeader(short bitsPerSample, short channels, int sampleRate) {
        byte[] wavHeader = new byte[44];
        //[0-3]Marks the file as a riff file. Characters are each 1 byte long.
        wavHeader[0] = 'R';
        wavHeader[1] = 'I';
        wavHeader[2] = 'F';
        wavHeader[3] = 'F';

        //[4-7]	File size, but the default value is empty.
        //Size of the overall file - 8 bytes, in bytes (32-bit integer). Typically, you'd fill this in after creation.

        //[8-11] File Type Header. For our purposes, it always equals "WAVE".
        wavHeader[8] = 'W';
        wavHeader[9] = 'A';
        wavHeader[10] = 'V';
        wavHeader[11] = 'E';

        //[12-15] Format chunk marker. Includes trailing null
        wavHeader[12] = 'f';
        wavHeader[13] = 'm';
        wavHeader[14] = 't';
        wavHeader[15] = ' ';

        //[16-19] Length of format data.  Always 16
        wavHeader[16] = 16;
        //wavHeader[17]
        //wavHeader[18]
        //wavHeader[19]

        //[20-21] Type of format (1 is PCM) - 2 byte integer
        wavHeader[20] = 1;
        //wavHeader[21]

        //[22-23]Number of Channels - 2 byte integer
        wavHeader[22] = (byte) channels;
        wavHeader[23] = (byte) (channels >> 8);

        //[24-27]Sample Rate - 32 byte integer. Common values are 44100 (CD), 48000 (DAT). Sample Rate = Number of Samples per second, or Hertz.
        wavHeader[24] = (byte) sampleRate;
        wavHeader[25] = (byte) (sampleRate >> 8);
        wavHeader[26] = (byte) (sampleRate >> 16);
        wavHeader[27] = (byte) (sampleRate >> 24);

        //[28-31] (Sample Rate * BitsPerSample * Channels) / 8.
        int sampleBytesPerSecond = sampleRate * bitsPerSample * channels / 8;
        wavHeader[28] = (byte) sampleBytesPerSecond;
        wavHeader[29] = (byte) (sampleBytesPerSecond >> 8);
        wavHeader[30] = (byte) (sampleBytesPerSecond >> 16);
        wavHeader[31] = (byte) (sampleBytesPerSecond >> 24);

        //[32-33] (BitsPerSample * Channels) / 8.
        //1 - 8 bit mono
        //2 - 8 bit stereo
        //3 - 16 bit mono
        //4 - 16 bit stereo
        short channelSampleBytes = (short) (bitsPerSample * channels / 8);
        wavHeader[32] = (byte) channelSampleBytes;
        wavHeader[33] = (byte) (channelSampleBytes >> 8);

        //[34-35] Bits per sample
        wavHeader[34] = (byte) bitsPerSample;
        wavHeader[35] = (byte) (bitsPerSample >> 8);

        //[36-39] "data" chunk header. Marks the beginning of the data section.
        wavHeader[36] = 'd';
        wavHeader[37] = 'a';
        wavHeader[38] = 't';
        wavHeader[39] = 'a';

        //40-43	File size (data). Size of the data section.

        return wavHeader;
    }

}
