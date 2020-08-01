package com.tunion.cores.result;

/**
 * Created by Think on 2018/1/26.
 */
public class CommConstants {

    public static final Boolean SUCCESS = true;
    public static final Boolean ERROR = false;
    public static final String TRACE_ID = "traceId";

    /**
     * 接口调用返回状态码
     */
    public enum RSP_ERROR_CODE {
        SUCCESS("0", "成功"),
        ERROR("1", "失败"),
        ;

        private String value;
        private String desc;

        RSP_ERROR_CODE(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String value() {
            return this.value;
        }

        public String desc() {
            return this.desc;
        }
    }

}
