import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server implements Serializable{

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel =ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(19030));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            System.out.println("Waiting for new connection");
            selector.select();
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeySet.iterator();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                System.out.print("new request is ");
                if(key.isAcceptable()){
                    System.out.println("Acceptable");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                    System.out.println("Connection Accepted : " + inetSocketAddress.getHostName() + " : " + inetSocketAddress.getPort());
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                else if(key.isReadable()){
                    System.out.println("Readable");
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    System.out.println("channel connected");
                    System.out.println("try to read");
                    socketChannel.read(byteBuffer);
                    System.out.println(byteBuffer.toString());
                    byte[] byteArray = new byte[byteBuffer.position()];
                    System.out.println(byteBuffer.position());
                    byteBuffer.position(0);
                    byteBuffer.get(byteArray);
                    ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Object object = ois.readObject();
                    SampleClass test = (SampleClass) object;
                    System.out.println("object : " + test.intarray);
                    byteBuffer.clear();
                }
            }
        }



    }
}
