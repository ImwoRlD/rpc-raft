package dto;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    private static final long serialVersionUID=1923811283912L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] paramters;
    private Class<?>[] paramTypes;
//    private RpcMessageType rpcMessageType;
    private String version;
    private String group;

}
