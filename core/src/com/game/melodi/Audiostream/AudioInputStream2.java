package com.game.melodi.Audiostream;

import java.io.IOException;

import javazoom2.jl.decoder.Bitstream;
import javazoom2.jl.decoder.Decoder;
import javazoom2.jl.decoder.Header;

/*
 * Copyright (c) 2013, Slick2D
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the Slick2D nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


public abstract class AudioInputStream2 {

    /**
     * Get the number of channels used by the audio
     *
     * @return The number of channels used by the audio
     */
    public abstract int getChannels();

    /**
     * The play back rate described in the underling audio file
     *
     * @return The playback rate
     */

    public abstract short[] data();
    /**
     *The buffer described in the underlying audio file
     *
     * @return the buffer data
     */

    public abstract String feed();

    /**
     * Attempt to receive frequency feed
     *
     * @return the frequency feed
     */

    public abstract Header getHeader();

    public abstract Decoder getDecoder();

    public abstract Bitstream getBitStream();

    public abstract int fullFeed();

    public abstract int getRate();

    /**
     * Read a single byte from the stream
     *
     * @return The single byte read
     * @throws IOException Indicates a failure to read the underlying media
     * @see java.io.InputStream#read()
     */


    public abstract int read() throws IOException;

    /**
     * Read up to data.length short from the stream
     *
     * //@param data The array to read into
     * @return The number of short read or -1 to indicate no more short are available
     * @throws IOException Indicates a failure to read the underlying media
     * @see java.io.InputStream#read(byte[])
     */
    public int read(short[] b, int off, int len) throws IOException {
        int i = -1;
        try {
            for (i = 0; i < len; i++) {
                int value = read();
                if (value >= 0)
                    b[i] = (short) value;
                else
                    return (i == 0) ? -1 : i;

            }
        } catch (IOException e) {
            Log.error(e);
            return i;
        }

        return len;
    }
    /**
     * Read up to len short from the stream
     *
     * //@param data The array to read into
     * //@param ofs The offset into the array at which to start writing
     * //@param len The maximum number of shorts to read
     * @return The number of shorts read or -1 to indicate no more short are available
     * @throws IOException Indicates a failure to read the underlying media
     */
    public int read(short[] b) throws IOException { return read(b, 0, b.length); }

    /**
     * Check if the stream is at the end, i.e. end of file or URL
     *
     * @return True if the stream has no more data available
     */
    public abstract boolean atEnd();

    /**
     * Close the stream
     *
     * @see java.io.InputStream#close()
     * @throws IOException Indicates a failure to access the resource
     */
    public abstract void close() throws IOException;

    /**
     * Skips over and discards n short of data from this input stream.
     *
     * @param n the number of short to be skipped.
     * @return the actual number of short skipped.
     * @throws IOException
     * @see java.io.InputStream#skip(long)
     */
    public long skip(long n) throws IOException {
        for(int i=0; i<n; i++)
            read();
        return n;
    }
}
