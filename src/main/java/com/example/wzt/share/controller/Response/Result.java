package com.example.wzt.share.controller.Response;

public class Result {
    /**
     * 错误内容
     */
    private String message;

    /**
     * 自定义错误码
     */
    private int code;


    public Result(String message, int code)
    {
        this.message = message;
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }


    public enum ErrorCode{
        /**
         * 用户不存在
         */
        USER_NOT_FOUND(404),

        /**
         * 用户已存在
         */
        USER_ALREADY_EXIST(400),
        /**
         * 分享图片失败
         */
        SHARE_FAILED(408),
        ;

        private int code;

        public int getCode()
        {
            return code;
        }

        ErrorCode(int code)
        {
            this.code = code;
        }
    }
}
