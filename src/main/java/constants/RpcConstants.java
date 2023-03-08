package constants;

public class RpcConstants {


    /**
     * Magic number,Verify RpcMessage
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'g',(byte) 'r',(byte) 'p',(byte) 'c'};
    public static final byte VERSION = 1;


    public static final int HEAD_LENGTH = 16;
    /**
     *  head 字段 messageType
     */
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    //ping
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;



    public static final String PING ="ping";
    public static final String PONG ="pong";
}
