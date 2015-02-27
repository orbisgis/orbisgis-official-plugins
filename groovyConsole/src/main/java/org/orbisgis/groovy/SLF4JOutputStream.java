package org.orbisgis.groovy;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Log stream to SLF4J Api
 * @author Nicolas Fortin
 */
public class SLF4JOutputStream extends OutputStream {
    private Logger logger;
    public enum Level { ERROR, INFO , WARNING}
    private Level level;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public SLF4JOutputStream(Logger logger, Level level) {
        this.level = level;
        this.logger = logger;
    }

    @Override
    public void write(int i) throws IOException {
        buffer.write(i);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if(len > 0 && '\n' == b[len-1]) {
            // Print at each end of line
            buffer.write(b,off,len - 1);
            flush();
        } else {
            buffer.write(b,off,len);
        }
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        // Fetch lines in the byte array
        String messages = buffer.toString();
        if(!messages.isEmpty()) {
            switch (level) {
                case INFO:
                    logger.info(messages);
                    break;
                case WARNING:
                    logger.warn(messages);
                    break;
                case ERROR:
                    logger.error(messages);
                    break;
            }
        }
        buffer.reset();
    }
}
