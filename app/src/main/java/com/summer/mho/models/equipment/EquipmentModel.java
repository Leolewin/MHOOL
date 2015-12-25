package com.summer.mho.models.equipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 包名      com.summer.mho.models.equipment
 * 类名      EquipmentModel
 * 创建时间   2015/12/23
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class EquipmentModel implements Serializable{

    // 主键
    private int PK;
    // 职业 0:近战/1:远程
    private int JOB;
    // 名称
    private String NAME;
    // 头部技能一览
    private HashMap<Integer, Integer> headSkills;
    // 胸部技能一览
    private HashMap<Integer, Integer> thoraxSkills;
    // 腕部技能一览
    private HashMap<Integer, Integer> wristSkills;
    // 腰部技能一览
    private HashMap<Integer, Integer> waistSkills;
    // 腿部技能一览
    private HashMap<Integer, Integer> legSkills;
    // 宝石插槽个数
    private ArrayList<String> holdNumberArrayList;

    public int getPK() {
        return PK;
    }

    public void setPK(int PK) {
        this.PK = PK;
    }

    public int getJOB() {
        return JOB;
    }

    public void setJOB(int JOB) {
        this.JOB = JOB;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public HashMap<Integer, Integer> getHeadSkills() {
        return headSkills;
    }

    public void setHeadSkills(HashMap<Integer, Integer> headSkills) {
        this.headSkills = headSkills;
    }

    public HashMap<Integer, Integer> getThoraxSkills() {
        return thoraxSkills;
    }

    public void setThoraxSkills(HashMap<Integer, Integer> thoraxSkills) {
        this.thoraxSkills = thoraxSkills;
    }

    public HashMap<Integer, Integer> getWristSkills() {
        return wristSkills;
    }

    public void setWristSkills(HashMap<Integer, Integer> wristSkills) {
        this.wristSkills = wristSkills;
    }

    public HashMap<Integer, Integer> getWaistSkills() {
        return waistSkills;
    }

    public void setWaistSkills(HashMap<Integer, Integer> waistSkills) {
        this.waistSkills = waistSkills;
    }

    public HashMap<Integer, Integer> getLegSkills() {
        return legSkills;
    }

    public void setLegSkills(HashMap<Integer, Integer> legSkills) {
        this.legSkills = legSkills;
    }

    public ArrayList<String> getHoldNumberArrayList() {
        return holdNumberArrayList;
    }

    public void setHoldNumberArrayList(ArrayList<String> holdNumberArrayList) {
        this.holdNumberArrayList = holdNumberArrayList;
    }
}
