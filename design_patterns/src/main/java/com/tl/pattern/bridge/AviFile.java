package com.tl.pattern.bridge;

/**
 * avi视频文件（具体的是实现化角色）
 */
public class AviFile implements VideoFile{

    @Override
    public void decode(String fileName) {
        System.out.println("avi视频文件:" + fileName);
    }

}
