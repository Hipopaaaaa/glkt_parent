package com.ohj;

public class TestGetKey {
    public static void main(String[] args) {
        String url="https://glkt-1304791648.cos.ap-guangzhou.myqcloud.com/2022/11/12/a287003405104c9faa5e9b2cbd11530dWechatIMG38.png";

        int index = url.indexOf("com/");
        String key = url.substring(index+4);

        System.out.println(key);
    }
}
