package fit.wenchao.websocketchartroom;

import fit.wenchao.websocketchartroom.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {


    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handleException(Exception e) {
        // 打印异常堆栈信息
        log.error(e.getMessage(), e);
        return JsonResult.getInstance(null, ResultCodeEnum.UNKNOWN_ERROR, e.getMessage());
    }

    /**
     * 处理所有业务异常
     *
     * @param e 业务异常
     * @return json结果
     */
    @ExceptionHandler(BackendException.class)
    @ResponseBody
    public JsonResult handleOpdRuntimeException(BackendException e) {
        // 不打印异常堆栈信息
        if(e.getDetail() == null || e.getDetail().equals("")) {
            log.error(e.getResultCodeEnum().getMessage());
        }
        log.error(e.getDetail());
        return JsonResult.getInstance(e.data, e.getResultCodeEnum(), e.getDetail());
    }
}
