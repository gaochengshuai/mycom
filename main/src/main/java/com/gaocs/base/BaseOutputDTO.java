package com.gaocs.base;

import java.io.Serializable;

public class BaseOutputDTO implements Serializable{

    private static final long serialVersionUID = -3597989533705213877L;
    /**
     * 返回结果编码
     */
    private String code;

    /**
     * 错误描述
     */
    private String msg;
    

	
    public BaseOutputDTO() {
        this.code = "000000";
        this.msg = "success";
    }

    public BaseOutputDTO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
