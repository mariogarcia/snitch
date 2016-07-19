package snitch.common;

import static java.util.stream.Collectors.toMap;

import java.io.InputStream;
import java.io.IOException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * @since 0.1.0
 */
public class Props {

    private Props() { }

    /**
     * @param clazz
     * @param fileName
     * @return
     * @since 0.1.0
     */
    public static Properties load(final Class<?> clazz, final String fileName) {
        Properties props = new Properties();
        try(InputStream inputStream = clazz.getResourceAsStream(fileName)) {
            props.load(inputStream);
        } catch(IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return props;
    }

    /**
     * @param fileName
     * @return
     * @since 0.1.0
     */
    public static Properties load(final String fileName) {
        return load(Props.class, fileName);
    }

    /**
     * @param fileName
     * @return
     * @since 0.1.0
     */
    public static Map<String,Object> loadAsMap(final String fileName) {
        return load(Props.class, fileName)
            .entrySet()
            .stream()
            .collect(toMap(Props::getKeyAsString, Entry::getValue));
    }

    private static String getKeyAsString(final Entry<Object,Object> entry) {
        return entry.getKey().toString();
    }
}
