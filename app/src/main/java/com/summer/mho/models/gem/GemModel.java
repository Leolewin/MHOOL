package com.summer.mho.models.gem;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 包名      com.summer.mho.models.gem
 * 类名      GemModel
 * 创建时间   2015/12/30
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class GemModel implements Serializable {

    // 主键
    private int PK;
    // 技能珠名称
    private String GEM_NAME;
    // 技能珠技能
    private HashMap<Integer, Integer> GEM_SKILLS;
    // 技能珠等级
    private int GEM_LEVEL;

    public int getPK() {
        return PK;
    }

    public void setPK(int PK) {
        this.PK = PK;
    }

    public String getGEM_NAME() {
        return GEM_NAME;
    }

    public void setGEM_NAME(String GEM_NAME) {
        this.GEM_NAME = GEM_NAME;
    }

    public HashMap<Integer, Integer> getGEM_SKILLS() {
        return GEM_SKILLS;
    }

    public void setGEM_SKILLS(HashMap<Integer, Integer> GEM_SKILLS) {
        this.GEM_SKILLS = GEM_SKILLS;
    }

    public int getGEM_LEVEL() {
        return GEM_LEVEL;
    }

    public void setGEM_LEVEL(int GEM_LEVEL) {
        this.GEM_LEVEL = GEM_LEVEL;
    }
}
