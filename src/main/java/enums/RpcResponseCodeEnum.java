package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCodeEnum {
    SUCCESS(200,"Remote Call is SUCCESS"),
    FAIL(500,"Remote Call is Fail");
    private final int code;

    private final String message;
}
