package fit.wenchao.websocketchartroom;

import fit.wenchao.websocketchartroom.utils.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 全局统一返回结果类，包括返回代码（便于前端检查核对），简要说明，详细说明，返回数据。其中，
 * 返回代码、简要说明与 {@code ResultCodeEnum} 中的常量应该一一对应，详细说明作为补充信息。
 *
 * @author wc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JsonResult {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回简要说明
     */
    private String message;

    /**
     * 返回详细说明，如果message已经足够，detail有时可以省略
     */
    private String detail;

    /**
     * 返回的数据
     */
    private Object data;

    // region getInstance工厂方法

    public static JsonResult build(Object data) {
        JsonResult result = new JsonResult();
        result.setData(data);
        return result;
    }



    public static JsonResult getInstance(Object body, Integer code, String message)  {
        JsonResult result = build(body);
        result.setDetail(null);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static JsonResult getInstance(Object body, Integer code, String message,
                                         String detail)  {
        JsonResult result = build(body);
        result.setDetail(detail);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static JsonResult getInstance(Object body, ResultCodeEnum resultCodeEnum, String detail)  {
        JsonResult result = build(body);
        result.setDetail(detail);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    // endregion

    public static JsonResult ok(Object data){
        return getInstance(data, 200, "success", null);
    }

}