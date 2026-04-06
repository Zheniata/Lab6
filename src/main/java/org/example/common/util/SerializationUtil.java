package org.example.common.util;

import java.io.*;
import java.nio.ByteBuffer;

public class SerializationUtil {
    public static ByteBuffer serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();

        byte[] bytes = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public static Object deserialize(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}
