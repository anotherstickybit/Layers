package tech.itpark.bodyconverter;

import tech.itpark.exception.ConvertionException;
import tech.itpark.http.ContentTypes;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StringBodyConverter implements BodyConverter {
    @Override
    public boolean canRead(String contentType, Class<?> clazz) {
        return ContentTypes.STRING.equals(contentType);
    }

    @Override
    public boolean canWrite(String contentType, Class<?> clazz) {
        return ContentTypes.STRING.equals(contentType);
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        return null;
    }

    @Override
    public void write(Writer writer, Object value) {
        try {
            writer.write(value.toString());
        } catch (IOException e) {
            throw new ConvertionException(e);
        }
    }
}
