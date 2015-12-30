package com.summer.mho.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.summer.mho.application.MyApplication;
import com.summer.mho.models.equipment.EquipmentModel;
import com.summer.mho.models.gem.GemModel;
import com.summer.mho.models.skill.Result;
import com.summer.mho.models.skill.SkillModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 包名      com.summer.mho.utils
 * 类名      DBHelper
 * 创建时间   2015/12/22
 * 创建人     Summer
 * 类描述
 * 所属类     {@link }
 */
public class DBHelper {

    private static final String DB_NAME = "MHOOL.sqlite";
    private static String DB_PATH = "";

    private static DBHelper dbHelper;

    private static SQLiteDatabase sqLiteDatabase;

    private Context context;

    private DBHelper() {
        context = MyApplication.getMyApplicationContext();
    }

    public static DBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DBHelper();
        }
        return dbHelper;
    }

    /**
     * copy DB from assets to data
     */
    public Boolean copyDBIfNotExist() {
        File dbFile = context.getDatabasePath(DB_NAME);
        DB_PATH = dbFile.getAbsolutePath();

        if (dbFile.exists()) {
            // 如果文件存在则返回true
            return true;
        } else {
            // copy db file
            try {

                File file = new File(DB_PATH.substring(0, DB_PATH.lastIndexOf("/")));
                if (!file.exists()) {
                    file.mkdirs();
                }

                InputStream inputStream = context.getAssets().open("db/" + DB_NAME);
                OutputStream outputStream = new FileOutputStream(DB_PATH);
                byte[] buffer = new byte[inputStream.available()];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return true;
            } catch (Exception e) {
                // TODO 写Log日志
                return false;
            }
        }

    }

    /**
     * 打开DB
     *
     * @return boolean
     */
    public boolean openDB() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        }
        return sqLiteDatabase.isOpen();
    }


    /**
     * 查询技能列表
     *
     * @return
     */
    public ArrayList<SkillModel> getSkillList(ArrayList<SkillModel> skillModelArrayList) {
        if (skillModelArrayList != null) {
            skillModelArrayList.clear();
        }

        String sql = "select * from SKILL order by PK";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                SkillModel skillModel = new SkillModel();
                skillModel.setPK(cursor.getInt(0));
                skillModel.setNAME(cursor.getString(1));
                skillModel.setEQUIPMENT(cursor.getString(2));
                skillModel.setResultArrayList(getSkillResult(cursor.getString(3)));
                skillModelArrayList.add(skillModel);
            } while (cursor.moveToNext());
        }

        return skillModelArrayList;
    }

    /**
     * 根据技能PK获取技能信息
     *
     * @param pk
     * @return
     */
    public SkillModel getSkillList(int pk) {
        SkillModel skillModel = new SkillModel();
        String sql = "select * from SKILL where PK = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(pk)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            skillModel.setPK(cursor.getInt(0));
            skillModel.setNAME(cursor.getString(1));
            skillModel.setEQUIPMENT(cursor.getString(2));
            skillModel.setResultArrayList(getSkillResult(cursor.getString(3)));
        }
        return skillModel;
    }

    /**
     * 处理技能效果集合
     *
     * @param resultStr
     * @return
     */
    private ArrayList<Result> getSkillResult(String resultStr) {
        ArrayList<Result> resultArrayList = new ArrayList<>();
        String[] resultArray = resultStr.split(";");
        for (int i = 0; i < resultArray.length; i++) {
            String eachResultStr = resultArray[i];
            String[] resultInfo = eachResultStr.split(",");
            Result result = new Result();
            result.setNUMBER(Integer.valueOf(resultInfo[0]));
            result.setRESULT_NAME(resultInfo[1]);
            result.setNOTE(resultInfo[2]);
            resultArrayList.add(result);
        }
        return resultArrayList;
    }

    /**
     * 获取装备信息
     *
     * @param pk  装备主键 eg:1,2,3
     * @param job 0:近战 1:远程
     * @return
     */
    public ArrayList<EquipmentModel> getEquipment(String pk, int job, ArrayList<EquipmentModel> equipmentModelArrayList) {

        if (null != equipmentModelArrayList)
            equipmentModelArrayList.clear();

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM EQUIPMENT where PK in (");
        sb.append(pk);
        sb.append(") and JOB =");
        sb.append(job);
        Cursor cursor = sqLiteDatabase.rawQuery(sb.toString(), null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                EquipmentModel equipmentModel = new EquipmentModel();
                equipmentModel.setPK(cursor.getInt(0));
                equipmentModel.setJOB(cursor.getInt(1));
                equipmentModel.setNAME(cursor.getString(2));
                equipmentModel.setHeadSkills(getSkill(cursor.getString(3)));
                equipmentModel.setThoraxSkills(getSkill(cursor.getString(4)));
                equipmentModel.setWristSkills(getSkill(cursor.getString(5)));
                equipmentModel.setWaistSkills(getSkill(cursor.getString(6)));
                equipmentModel.setLegSkills(getSkill(cursor.getString(7)));
                equipmentModel.setHoldNumberArrayList(getHoldNumberArrayList(cursor.getString(8)));

                equipmentModelArrayList.add(equipmentModel);
            } while (cursor.moveToNext());
        }
        return equipmentModelArrayList;
    }


    /**
     * 拆解套装技能
     *
     * @param skillStr 套装的各个部位所拥有的技能
     * @return
     */
    private HashMap<Integer, Integer> getSkill(String skillStr) {
        HashMap<Integer, Integer> skillHashMap = new HashMap<>();
        String[] skills = skillStr.split(",");
        for (int i = 0; i < skills.length; i++) {
            String[] each = skills[i].split(":");
            skillHashMap.put(Integer.valueOf(each[0]), Integer.valueOf(each[1]));
        }
        return skillHashMap;
    }

    /**
     * 处理宝石插槽数
     *
     * @param holdNumber
     * @return
     */
    private ArrayList<Integer> getHoldNumberArrayList(String holdNumber) {
        ArrayList<Integer> holdNumberArrayList = new ArrayList<>();
        String[] temp = holdNumber.split(",");
        for (int i = 0; i < temp.length; i++) {
            holdNumberArrayList.add(Integer.valueOf(temp[i]));
        }
        return holdNumberArrayList;
    }

    /**
     * 获取技能珠信息
     * @param gemModelArrayList
     * @return
     */
    public ArrayList<GemModel> getGemInfo(ArrayList<GemModel> gemModelArrayList) {
        gemModelArrayList.clear();
        String sql = "select * from GEM";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                GemModel gemModel = new GemModel();
                gemModel.setPK(cursor.getInt(0));
                gemModel.setGEM_NAME(cursor.getString(1));
                gemModel.setGEM_SKILLS(getSkill(cursor.getString(2)));
                gemModel.setGEM_LEVEL(cursor.getInt(3));
                gemModelArrayList.add(gemModel);
            }while (cursor.moveToNext());
        }

        return gemModelArrayList;
    }
}
