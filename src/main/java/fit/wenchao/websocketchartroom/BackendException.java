package fit.wenchao.websocketchartroom;

import fit.wenchao.websocketchartroom.utils.ResultCodeEnum;
import lombok.Getter;

@Getter
public class BackendException extends RuntimeException {
    String exName;
    String detail;
    ResultCodeEnum resultCodeEnum;
    Object data;

    public BackendException(Object data, ResultCodeEnum resultCodeEnum, String detail) {
        this.exName = resultCodeEnum.getMessage();
        this.data = data;
        this.detail = detail;
        this.resultCodeEnum = resultCodeEnum;
    }

}
