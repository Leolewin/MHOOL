package com.summer.mho.models.skill;

import java.util.ArrayList;

/**
 * 包名      com.summer.mho.models.skill
 * 类名      SkillModel
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class SkillModel {

    private int PK;
    private String NAME;
    private String EQUIPMENT;
    private ArrayList<Result> resultArrayList;

    public int getPK() {
        return PK;
    }

    public void setPK(int PK) {
        this.PK = PK;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEQUIPMENT() {
        return EQUIPMENT;
    }

    public void setEQUIPMENT(String EQUIPMENT) {
        this.EQUIPMENT = EQUIPMENT;
    }

    public ArrayList<Result> getResultArrayList() {
        return resultArrayList;
    }

    public void setResultArrayList(ArrayList<Result> resultArrayList) {
        this.resultArrayList = resultArrayList;
    }

}
