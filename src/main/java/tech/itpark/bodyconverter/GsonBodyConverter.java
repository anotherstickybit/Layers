package tech.itpark.bodyconverter;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import tech.itpark.exception.ConvertionException;
import tech.itpark.http.ContentTypes;

import java.io.Reader;
import java.io.Writer;

@RequiredArgsConstructor
public class GsonBodyConverter implements BodyConverter{
    private final Gson gson;

    @Override
    public boolean canRead(String contentType, Class<?> clazz) {
        return ContentTypes.APPLICATION_JSON.equals(contentType);
    }

    @Override
    public boolean canWrite(String contentType, Class<?> clazz) {
        return ContentTypes.APPLICATION_JSON.equals(contentType);
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        try {
            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            throw new ConvertionException(e);
        }
    }

    @Override
    public void write(Writer writer, Object value) {
        try {
            writer.write(gson.toJson(value));
        } catch (Exception e) {
            throw new ConvertionException(e);
        }
    }
}
