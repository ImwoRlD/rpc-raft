package exception;

public class RpcException extends RuntimeException{
    public RpcException(){
    }
    public RpcException(String message,Throwable cause){
        super(message,cause);
    }
}
