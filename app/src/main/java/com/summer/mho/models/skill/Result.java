package com.summer.mho.models.skill;

import java.io.Serializable;

/**
 * 包名      com.summer.mho.models.skill
 * 类名      Result
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class Result implements Serializable{

    private int NUMBER;
    private String RESULT_NAME;
    private String NOTE;

    public int getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(int NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getRESULT_NAME() {
        return RESULT_NAME;
    }

    public void setRESULT_NAME(String RESULT_NAME) {
        this.RESULT_NAME = RESULT_NAME;
    }

    public String getNOTE() {
        return NOTE;
    }

    public void setNOTE(String NOTE) {
        this.NOTE = NOTE;
    }
}
