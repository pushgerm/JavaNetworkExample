import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.Serializable;

import static java.lang.Thread.sleep;

public class Client implements  Serializable{

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        System.out.println("Connection requested");
        socketChannel.connect(new InetSocketAddress("223.194.70.127", 19030));
        System.out.println("Connection succeed");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        SampleClass test = new SampleClass();
        oos.writeObject(test);
        ByteBuffer byteBuffer = ByteBuffer.wrap(baos.toByteArray());
        socketChannel.write(byteBuffer);
        System.out.println("buffer size : " + byteBuffer.position());
        sleep(10000);
    }
}
