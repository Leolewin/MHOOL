package com.summer.mho.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.summer.mho.R;
import com.summer.mho.base.BaseActivity;
import com.summer.mho.gem.GemActivity;
import com.summer.mho.models.equipment.EquipmentModel;
import com.summer.mho.models.skill.Result;
import com.summer.mho.models.skill.SkillModel;
import com.summer.mho.utils.DBHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.HashMap;

import cn.domob.android.ads.AdEventListener;
import cn.domob.android.ads.AdManager;
import cn.domob.android.ads.AdView;

public class MainActivity extends BaseActivity implements OnClickListener{


    // 近战,默认选中
    @ViewInject(R.id.checkBox1)
    private CheckBox checkBox1;

    // 远程
    @ViewInject(R.id.checkBox2)
    private CheckBox checkBox2;

    // 默认近战被选中  0:近战  1:远程
    private int isCloseCombat = 0;

    /**
     * ---------------------------------技能--------------------------------------
     */

    // 查询所有技能结果
    private ArrayList<SkillModel> skillModelArrayList = new ArrayList<>();

    // 技能下拉适配器
    private SkillSpinnerAdapter skillSpinnerAdapter = new SkillSpinnerAdapter(this, skillModelArrayList);

    // 技能选择1
    @ViewInject(R.id.spinner1)
    private Spinner spinner1;

    // 技能选择2
    @ViewInject(R.id.spinner2)
    private Spinner spinner2;

    // 第一个被选择的技能
    private SkillModel skill_1;


    // 技能二是否被选
    @ViewInject(R.id.checkbox_skill_2)
    private CheckBox checkbox_skill_2;
    // 第二个被选择的技能
    private SkillModel skill_2;

    /**
     * ---------------------------------套装--------------------------------------
     */
    @ViewInject(R.id.head)
    private Spinner head;
    private HashMap<Integer, Integer> headInfo;

    @ViewInject(R.id.thorax)
    private Spinner thorax;
    private HashMap<Integer, Integer> thoraxInfo;

    @ViewInject(R.id.wrist)
    private Spinner wrist;
    private HashMap<Integer, Integer> wristInfo;

    @ViewInject(R.id.waist)
    private Spinner waist;
    private HashMap<Integer, Integer> waistInfo;

    @ViewInject(R.id.leg)
    private Spinner leg;
    private HashMap<Integer, Integer> legInfo;

    @ViewInject(R.id.gem)
    private TextView gem;
    private HashMap<Integer, Integer> gemInfo = new HashMap<>();

    @ViewInject(R.id.jewelry1)
    private Spinner jewelry1;
    private HashMap<Integer, Integer> jewelry1Info;

    @ViewInject(R.id.jewelry2)
    private Spinner jewelry2;
    private HashMap<Integer, Integer> jewelry2Info;

    // 装备部位宝石插槽数量集合,初始化为0
    private int holderNumber = 0;

    // 筛选出的装备信息
    private ArrayList<EquipmentModel> equipmentModelArrayList = new ArrayList<>();

    private EquipmentSpinnerAdapter equipmentSpinnerAdapter = new EquipmentSpinnerAdapter(this, equipmentModelArrayList);

    /**
     * ------------------------------------结果列表--------------------------
     */
    @ViewInject(R.id.resultList)
    private LinearLayout resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        PushAgent.getInstance(this).onAppStart();

        showAD();
        skillsSelected();
        initView();

    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 获取技能列表数据
     */
    private void skillsSelected() {
        skillModelArrayList = DBHelper.getInstance().getSkillList(skillModelArrayList);
        skillSpinnerAdapter.notifyDataSetChanged();


    }

    private void initView() {
        checkBox1.setOnClickListener(this);
        checkBox2.setOnClickListener(this);
        checkbox_skill_2.setOnClickListener(this);


        // 近战/远程初始化
        checkBox1.setChecked(true);
        checkBox2.setChecked(false);

        // 技能选择下拉菜单初始化
        spinner1.setAdapter(skillSpinnerAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill_1 = (SkillModel) parent.getItemAtPosition(position);
                // 初始化时直走第二个技能未被选中时,之后当第二个技能也被选中时会走双技能筛选
                if (!checkbox_skill_2.isChecked()) {
                    //TODO 查询数据
                    equipmentSelected(skill_1.getEQUIPMENT());
                } else {
                    // 双技能筛选
                    skill2Selected(skill_1.getEQUIPMENT(), skill_2.getEQUIPMENT());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setAdapter(skillSpinnerAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill_2 = (SkillModel) parent.getItemAtPosition(position);
                if (checkbox_skill_2.isChecked()) {
                    //TODO 双技能筛选
                    skill2Selected(skill_1.getEQUIPMENT(), skill_2.getEQUIPMENT());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        head.setAdapter(equipmentSpinnerAdapter);
        head.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                headInfo = ((EquipmentModel) parent.getItemAtPosition(position)).getHeadSkills();
                holderNumber += ((EquipmentModel) parent.getItemAtPosition(position)).getHoldNumberArrayList().get(0);
                setResultList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                headInfo = null;
            }
        });

        thorax.setAdapter(equipmentSpinnerAdapter);
        thorax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thoraxInfo = ((EquipmentModel) parent.getItemAtPosition(position)).getThoraxSkills();
                holderNumber += ((EquipmentModel) parent.getItemAtPosition(position)).getHoldNumberArrayList().get(1);
                setResultList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                thoraxInfo = null;
            }
        });
        wrist.setAdapter(equipmentSpinnerAdapter);
        wrist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wristInfo = ((EquipmentModel) parent.getItemAtPosition(position)).getWristSkills();
                holderNumber += ((EquipmentModel) parent.getItemAtPosition(position)).getHoldNumberArrayList().get(2);
                setResultList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wristInfo = null;
            }
        });
        waist.setAdapter(equipmentSpinnerAdapter);
        waist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                waistInfo = ((EquipmentModel) parent.getItemAtPosition(position)).getWaistSkills();
                holderNumber += ((EquipmentModel) parent.getItemAtPosition(position)).getHoldNumberArrayList().get(3);
                setResultList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                waistInfo = null;
            }
        });
        leg.setAdapter(equipmentSpinnerAdapter);
        leg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                legInfo = ((EquipmentModel) parent.getItemAtPosition(position)).getLegSkills();
                holderNumber += ((EquipmentModel) parent.getItemAtPosition(position)).getHoldNumberArrayList().get(4);
                setResultList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                legInfo = null;
            }
        });

        gem.setOnClickListener(this);

        jewelry1.setAdapter(skillSpinnerAdapter);
        jewelry1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jewelry1PK = ((SkillModel) parent.getItemAtPosition(position)).getPK();

                jewelry1Info = new HashMap<>();
                jewelry1Info.put(((SkillModel) parent.getItemAtPosition(position)).getPK(), 0);

                // 弹出popup
                if (firstShowPopup_1) {
                    showPopupToSelectJewelry((SkillModel) parent.getItemAtPosition(position), 1);
                }
                firstShowPopup_1 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        jewelry2.setAdapter(skillSpinnerAdapter);
        jewelry2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jewelry2PK = ((SkillModel) parent.getItemAtPosition(position)).getPK();

                jewelry2Info = new HashMap<>();
                jewelry2Info.put(((SkillModel) parent.getItemAtPosition(position)).getPK(), 0);

                // 弹出popup
                if (firstShowPopup_2) {
                    showPopupToSelectJewelry((SkillModel) parent.getItemAtPosition(position), 2);
                }
                firstShowPopup_2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // 控制护石选择popup,默认第一次不弹出
    private boolean firstShowPopup_1 = false;
    private boolean firstShowPopup_2 = false;

    // 护石加的技能PK
    private int jewelry1PK;
    // 护石加的技能PK
    private int jewelry2PK;

    /**
     * 弹出画面选择护石属性
     *
     * @param skill 护石选择的技能信息
     */
    private void showPopupToSelectJewelry(SkillModel skill, final int num) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_window_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        TextView pop_textView = (TextView) view.findViewById(R.id.pop_textView);
        pop_textView.setText(skill.getNAME() + "+");

        final EditText jewelryNumberEditText = (EditText) view.findViewById(R.id.pop_edittext);

        Button ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 1) {
                    jewelry1Info.put(jewelry1PK, Integer.valueOf(jewelryNumberEditText.getText().toString()));
                } else {
                    jewelry2Info.put(jewelry2PK, Integer.valueOf(jewelryNumberEditText.getText().toString()));
                }
                popupWindow.dismiss();
                setResultList();
            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 获取符合条件的装备信息
     *
     * @param pk
     */
    private void equipmentSelected(String pk) {
        equipmentModelArrayList = DBHelper.getInstance().getEquipment(pk, isCloseCombat, equipmentModelArrayList);
        refreshEquipmentInfo();
    }


    /**
     * 绑定装备信息
     */
    private void refreshEquipmentInfo() {
        equipmentSpinnerAdapter.notifyDataSetChanged();
        setResultList();
    }


    /**
     * 近战远程选择
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkBox1:
                clearData();
                checkBox1.setChecked(true);
                checkBox2.setChecked(false);
                isCloseCombat = 0;
                if (checkbox_skill_2.isChecked()) {
                    // 双技能
                    skill2Selected(skill_1.getEQUIPMENT(), skill_2.getEQUIPMENT());
                } else {
                    // 单技能
                    equipmentSelected(skill_1.getEQUIPMENT());
                }
                break;
            case R.id.checkBox2:
                clearData();
                checkBox1.setChecked(false);
                checkBox2.setChecked(true);
                isCloseCombat = 1;
                if (checkbox_skill_2.isChecked()) {
                    // 双技能
                    skill2Selected(skill_1.getEQUIPMENT(), skill_2.getEQUIPMENT());
                } else {
                    // 单技能
                    equipmentSelected(skill_1.getEQUIPMENT());
                }
                break;
            case R.id.checkbox_skill_2:
                //TODO 判定是否被选中,如果被选中则查询双技能套装
                clearData();
                if (checkbox_skill_2.isChecked()) {
                    // 双技能
                    skill2Selected(skill_1.getEQUIPMENT(), skill_2.getEQUIPMENT());
                } else {
                    // 单技能
                    equipmentSelected(skill_1.getEQUIPMENT());
                }
                break;
            case R.id.gem:
                Intent intent = new Intent(MainActivity.this, GemActivity.class);
                intent.putExtra("holderNumber", holderNumber);
                startActivityForResult(intent, 0);
                break;
            default:
        }
    }

    /**
     * 清空数据
     */
    private void clearData() {
        headInfo = null;
        thoraxInfo = null;
        wristInfo = null;
        waistInfo = null;
        legInfo = null;
    }


    /**
     * 第二个技能被选中,筛选两个技能中共同的套装
     */
    private void skill2Selected(String skill1_equipment, String skill2_equipment) {
        String[] str1 = skill1_equipment.split(",");
        String[] str2 = skill2_equipment.split(",");

        ArrayList<String> arr1 = new ArrayList<>();
        ArrayList<String> arr2 = new ArrayList<>();

        for (int i = 0; i < str1.length; i++) {
            arr1.add(str1[i]);
        }
        for (int i = 0; i < str2.length; i++) {
            arr2.add(str2[i]);
        }

        StringBuffer sb = new StringBuffer();

        if (arr1.size() < arr2.size()) {
            for (int i = 0; i < arr1.size(); i++) {
                if (arr2.contains(arr1.get(i))) {
                    sb.append(arr1.get(i));
                    if (i < arr1.size() - 1) {
                        sb.append(",");
                    }
                }
            }
        } else {
            for (int i = 0; i < arr2.size(); i++) {
                if (arr1.contains(arr2.get(i))) {
                    sb.append(arr2.get(i));
                    if (i < arr2.size() - 1) {
                        sb.append(",");
                    }
                }
            }
        }
        equipmentSelected(sb.toString());
    }

    /**
     * 配装结果一览
     */
    private void setResultList() {
        resultList.removeAllViews();
        if (headInfo != null && thoraxInfo != null && wristInfo != null && waistInfo != null && legInfo != null) {
            // 做数据处理
            // 拿到所有部位的技能HashMap的key,也就是技能的PK
            // 处理所有PK,找出所有技能
            ArrayList<Integer> keysArray = new ArrayList<>();
            keysArray.addAll(headInfo.keySet());

            Object[] thoraxKeys = thoraxInfo.keySet().toArray();
            for (int i = 0; i < thoraxKeys.length; i++) {
                if (!keysArray.contains(thoraxKeys[i])) {
                    keysArray.add((Integer) thoraxKeys[i]);
                }
            }

            Object[] wristKeys = wristInfo.keySet().toArray();
            for (int i = 0; i < wristKeys.length; i++) {
                if (!keysArray.contains(wristKeys[i])) {
                    keysArray.add((Integer) wristKeys[i]);
                }
            }
            Object[] waistKeys = waistInfo.keySet().toArray();
            for (int i = 0; i < waistKeys.length; i++) {
                if (!keysArray.contains(waistKeys[i])) {
                    keysArray.add((Integer) waistKeys[i]);
                }
            }
            Object[] legKeys = legInfo.keySet().toArray();
            for (int i = 0; i < legKeys.length; i++) {
                if (!keysArray.contains(legKeys[i])) {
                    keysArray.add((Integer) legKeys[i]);
                }
            }

            Object[] jewelry1Keys = jewelry1Info.keySet().toArray();
            for (int i = 0; i < jewelry1Keys.length; i++) {
                if (!keysArray.contains(jewelry1Keys[i])) {
                    keysArray.add((Integer) jewelry1Keys[i]);
                }
            }

            Object[] jewelry2Keys = jewelry2Info.keySet().toArray();
            for (int i = 0; i < jewelry2Keys.length; i++) {
                if (!keysArray.contains(jewelry2Keys[i])) {
                    keysArray.add((Integer) jewelry2Keys[i]);
                }
            }

            Object[] gemKeys = gemInfo.keySet().toArray();
            for (int i = 0; i < gemKeys.length; i++) {
                if (!keysArray.contains(gemKeys[i])) {
                    keysArray.add((Integer) gemKeys[i]);
                }
            }


            if (keysArray.size() > 0) {


                for (int i = 0; i < keysArray.size(); i++) {

                    LinearLayout resultView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.result_item_layout, null);
                    TextView text1 = (TextView) resultView.findViewById(R.id.text1);
                    TextView text2 = (TextView) resultView.findViewById(R.id.text2);
                    TextView text3 = (TextView) resultView.findViewById(R.id.text3);
                    TextView text4 = (TextView) resultView.findViewById(R.id.text4);
                    TextView text5 = (TextView) resultView.findViewById(R.id.text5);
                    TextView text6 = (TextView) resultView.findViewById(R.id.text6);
                    TextView text7 = (TextView) resultView.findViewById(R.id.text7);
                    TextView text8 = (TextView) resultView.findViewById(R.id.text8);
                    TextView text9 = (TextView) resultView.findViewById(R.id.text9);
                    TextView text10 = (TextView) resultView.findViewById(R.id.text10);

                    Integer pk = keysArray.get(i);

                    SkillModel curSkillModel = DBHelper.getInstance().getSkillList(pk);
                    text1.setText(curSkillModel.getNAME());
                    text1.setTag(curSkillModel.getResultArrayList());


                    int a = headInfo.get(pk) == null ? 0 : headInfo.get(pk);
                    int b = thoraxInfo.get(pk) == null ? 0 : thoraxInfo.get(pk);
                    int c = wristInfo.get(pk) == null ? 0 : wristInfo.get(pk);
                    int d = waistInfo.get(pk) == null ? 0 : waistInfo.get(pk);
                    int e = legInfo.get(pk) == null ? 0 : legInfo.get(pk);
                    int f = gemInfo.get(pk) == null ? 0 : gemInfo.get(pk);
                    int g = jewelry1Info.get(pk) == null ? 0 : jewelry1Info.get(pk);
                    int h = jewelry2Info.get(pk) == null ? 0 : jewelry2Info.get(pk);


                    text2.setText(String.valueOf(a));
                    text3.setText(String.valueOf(b));
                    text4.setText(String.valueOf(c));
                    text5.setText(String.valueOf(d));
                    text6.setText(String.valueOf(e));
                    text7.setText(String.valueOf(f));
                    text8.setText(String.valueOf(g+h));
                    // 总技能点
                    int total = a + b + c + d + e + f + g + h;
                    text9.setText(String.valueOf(total));

                    // 判断当前技能和为什么效果
                    ArrayList<Result> resultArrayList = curSkillModel.getResultArrayList();
                    ArrayList<Result> minArrayList = new ArrayList<>(); // 比当前值小的集合
                    Result result = null;
                    // 区分技能总和的正负
                    if (total > 0) {
                        // 循环技能效果列表
                        for (int k = 0; k < resultArrayList.size(); k++) {
                            if (resultArrayList.get(k).getNUMBER() < total && resultArrayList.get(k).getNUMBER() > 0) {
                                minArrayList.add(resultArrayList.get(k));
                            } else if (resultArrayList.get(k).getNUMBER() == total) {
                                result = resultArrayList.get(k); // 如果当前技能的和等于某一个效果则初始化
                            }
                        }
                        if (minArrayList.size() > 1) {
                            for (int j = 1; j < minArrayList.size(); j++) {
                                if (minArrayList.get(j - 1).getNUMBER() < minArrayList.get(j).getNUMBER()) {
                                    result = minArrayList.get(j);
                                } else {
                                    result = minArrayList.get(j - 1);
                                }
                            }
                        } else if (minArrayList.size() == 1) {
                            result = minArrayList.get(0);
                        }
                    } else {

                        for (int p = 0; p < resultArrayList.size(); p++) {
                            if (resultArrayList.get(p).getNUMBER() > total && resultArrayList.get(p).getNUMBER() < 0) {
                                minArrayList.add(resultArrayList.get(p));
                            } else if (resultArrayList.get(p).getNUMBER() == total) {
                                result = resultArrayList.get(p); // 如果当前技能的和等于某一个效果则初始化
                            }
                        }
                        if (minArrayList.size() > 1) {
                            for (int z = 0; z < minArrayList.size(); z++) {
                                if (minArrayList.get(z - 1).getNUMBER() < minArrayList.get(z).getNUMBER()) {
                                    result = minArrayList.get(z - 1);
                                }
                            }
                        } else if (minArrayList.size() == 1) {
                            result = minArrayList.get(0);
                        }
                    }

                    if (result != null) {
                        text10.setText(result.getNOTE());
                    } else {
                        text10.setText("");
                    }

                    resultList.addView(resultView);
                }
            }
        }

    }


    @Override
    public void onBackPressed() {
        BaseActivity.exitApp(this);
    }



    // 广告相关
    @ViewInject(R.id.ad_layout)
    private RelativeLayout ad_layout;

    private AdView adView;

    private void showAD() {
        adView = new AdView(this, "56OJ2oOIuNwtwqy74p", "16TLPUloApSdSNUUv_WITfvi");
        adView.setKeyword("game");
        adView.setUserGender("male");
        adView.setUserBirthdayStr("1988-06-07");
        adView.setUserPostcode("116000");
        adView.setAdEventListener(new AdEventListener() {
            @Override
            public void onEventAdReturned(AdView adView) {

            }

            @Override
            public void onAdFailed(AdView adView, AdManager.ErrorCode errorCode) {

            }

            @Override
            public void onAdOverlayPresented(AdView adView) {

            }

            @Override
            public void onAdOverlayDismissed(AdView adView) {

            }

            @Override
            public void onLeaveApplication(AdView adView) {

            }

            @Override
            public void onAdClicked(AdView adView) {

            }

            @Override
            public Context onAdRequiresCurrentContext() {
                return null;
            }
        });

        ad_layout.addView(adView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // 处理宝石信息
                    gemInfo = (HashMap<Integer, Integer>) data.getSerializableExtra("resultData");
                    setResultList();
                }
                break;
        }
    }
}
