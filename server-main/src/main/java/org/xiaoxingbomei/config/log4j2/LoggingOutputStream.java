package org.xiaoxingbomei.config.log4j2;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * 重定向system.err 到 log4j2
 */
public class LoggingOutputStream extends OutputStream
{
    private static final int DEFAULT_BUFFER_LENGTH = 2048;
    private final Logger logger;
    private final Level level;
    private byte[] buffer;
    private int count;
    private int curBufLength;

    public LoggingOutputStream(final Logger logger, final Level level)
    {
        this.logger = logger;
        this.level = level;
        curBufLength = DEFAULT_BUFFER_LENGTH;
        buffer = new byte[curBufLength];
        count = 0;
    }

    @Override
    public void write(final int b) throws IOException {
        if (b == 0) {
            return;
        }

        if (count == curBufLength) {
            final int newBufLength = curBufLength + DEFAULT_BUFFER_LENGTH;
            final byte[] newBuf = new byte[newBufLength];
            System.arraycopy(buffer, 0, newBuf, 0, curBufLength);
            buffer = newBuf;
            curBufLength = newBufLength;
        }

        buffer[count] = (byte) b;
        count++;
    }

    @Override
    public void flush() {
        if (count == 0) {
            return;
        }

        final byte[] bytes = new byte[count];
        System.arraycopy(buffer, 0, bytes, 0, count);
        final String str = new String(bytes);
        logger.log(level, str);
        count = 0;
    }

    @Override
    public void close()
    {
        flush();
    }
}