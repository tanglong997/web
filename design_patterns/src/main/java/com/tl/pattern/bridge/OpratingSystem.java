package com.tl.pattern.bridge;

/**
 * 抽象的操作系统类（抽象化角色）
 */
public abstract class OpratingSystem {
    //声明videFile变量
    protected VideoFile videoFile;

    public OpratingSystem(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    public abstract void play(String fileName);
}
