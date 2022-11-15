package com.ohj.glkt.utils.result;

import lombok.Data;

//统一返回结果
@Data
public class Result<T>{

    private Integer code;
    private String message;
    private T data;

    public static<T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        result.setCode(20000);
        result.setMessage("成功");
        if(data!=null){
            result.setData(data);
        }
        return  result;
    }

    public static<T> Result<T> ok(){
        Result<T> result = new Result<>();
        result.setCode(20000);
        result.setMessage("成功");
        return  result;
    }


    public static<T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(20001);
        result.setMessage("失败");
        return  result;
    }

    public Result<T> code(Integer code){
        this.code=code;
        return this;
    }

    public Result<T> message(String message){
        this.message=message;
        return this;
    }
}
