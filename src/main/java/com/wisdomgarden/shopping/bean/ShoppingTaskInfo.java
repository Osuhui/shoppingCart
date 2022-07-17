package com.wisdomgarden.shopping.bean;

/**
 * @Function 购物任务信息类
 *
 * @Author huiwl
 * @Date 2022/7/16
 **/
public class ShoppingTaskInfo {
    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 输入信息
     */
    private InputInfo inputInfo;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public InputInfo getInputInfo() {
        return inputInfo;
    }

    public void setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    @Override
    public String toString() {
        return "ShoppingTaskInfo{" +
                "filePath='" + filePath + '\'' +
                ", inputInfo=" + inputInfo +
                '}';
    }
}
