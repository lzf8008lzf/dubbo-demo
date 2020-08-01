package com.tunion.cores.result;

public class Results<T> implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 成功标识
     */
    private Boolean isSuccess;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 返回结果对象
     */
    private T data;

    private void setError(boolean isSuccess) {

        this.setSuccess(isSuccess);

        if (isSuccess) {
            this.errorCode = CommConstants.RSP_ERROR_CODE.SUCCESS.value();
            this.errorMsg = CommConstants.RSP_ERROR_CODE.SUCCESS.desc();
        } else {
            this.errorCode = CommConstants.RSP_ERROR_CODE.ERROR.value();
            this.errorMsg = CommConstants.RSP_ERROR_CODE.ERROR.desc();
        }
    }

    public Results() {
    }

    public static Results ok() {
        return new Results(CommConstants.SUCCESS);
    }

    public static <T> Results<T> ok(T data) {
        return new Results(CommConstants.SUCCESS, data);
    }

    public static Results error(String errorMsg) {
        return Results.error(CommConstants.RSP_ERROR_CODE.ERROR.value(), errorMsg);
    }

    public static Results error(String errorCode, String errorMsg) {
        return new Results(errorCode, errorMsg);
    }

    public void setError(String errorCode, String errorMsg){
        this.isSuccess = CommConstants.ERROR;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Results(Boolean isSuccess) {
        this.setError(isSuccess);//设置错误代码及错误消息
    }

    public Results(Boolean isSuccess, T data) {
        this.setError(isSuccess);//设置错误代码及错误消息
        this.data = data;
    }

    public Results(String errorCode, String errorMsg) {
        this.setError(errorCode,errorMsg);
    }

    public Boolean isOk() {
        return isSuccess;
    }

    public Boolean isError() {
        return !isSuccess;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
