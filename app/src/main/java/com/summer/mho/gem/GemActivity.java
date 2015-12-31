package com.summer.mho.gem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sixth.adwoad.AdwoAdView;
import com.summer.mho.R;
import com.summer.mho.base.BaseActivity;
import com.summer.mho.models.gem.GemModel;
import com.summer.mho.models.gem.GemSpinnerAdapter;
import com.summer.mho.utils.DBHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

public class GemActivity extends BaseActivity implements OnClickListener{

    private int holderNumber = 0;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.spinner1)
    private Spinner spinner1;
    @ViewInject(R.id.edit_text_1)
    private EditText editText1;

    @ViewInject(R.id.spinner2)
    private Spinner spinner2;
    @ViewInject(R.id.edit_text_2)
    private EditText editText2;

    @ViewInject(R.id.spinner3)
    private Spinner spinner3;
    @ViewInject(R.id.edit_text_3)
    private EditText editText3;

    @ViewInject(R.id.spinner4)
    private Spinner spinner4;
    @ViewInject(R.id.edit_text_4)
    private EditText editText4;

    @ViewInject(R.id.spinner5)
    private Spinner spinner5;
    @ViewInject(R.id.edit_text_5)
    private EditText editText5;

    @ViewInject(R.id.ok)
    private Button ok;

    @ViewInject(R.id.cancel)
    private Button cancel;

    private ArrayList<GemModel> gemModelArrayList = new ArrayList<>();
    private GemSpinnerAdapter gemSpinnerAdapter;

    private HashMap<Integer, Integer> totalSkills = new HashMap<>();



    private HashMap<Integer, Integer> skill1;
    private HashMap<Integer, Integer> skill2;
    private HashMap<Integer, Integer> skill3;
    private HashMap<Integer, Integer> skill4;
    private HashMap<Integer, Integer> skill5;

    private int level1;
    private int level2;
    private int level3;
    private int level4;
    private int level5;

    private boolean resultIsOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gem);
        ViewUtils.inject(this);

        showAD();

        holderNumber = getIntent().getIntExtra("holderNumber", 0);
        setHolderNumber(0);
        getSpinnerData();
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

    private void setHolderNumber(int curNum) {
        if (curNum > holderNumber) {
            title.setTextColor(Color.RED);
            resultIsOk = false;
        } else {
            title.setTextColor(Color.BLACK);
            resultIsOk = true;
        }
        title.setText("技能珠" + String.valueOf(curNum) + "/" +String.valueOf(holderNumber));
    }

    private void getSpinnerData() {
        gemModelArrayList = DBHelper.getInstance().getGemInfo(gemModelArrayList);
        gemSpinnerAdapter = new GemSpinnerAdapter(this, gemModelArrayList);
    }

    private void initView() {
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);


        spinner1.setAdapter(gemSpinnerAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill1 = ((GemModel)parent.getItemAtPosition(position)).getGEM_SKILLS();
                level1 = ((GemModel)parent.getItemAtPosition(position)).getGEM_LEVEL();
                countHolderNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setAdapter(gemSpinnerAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill2 = ((GemModel)parent.getItemAtPosition(position)).getGEM_SKILLS();
                level2 = ((GemModel)parent.getItemAtPosition(position)).getGEM_LEVEL();
                countHolderNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setAdapter(gemSpinnerAdapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill3 = ((GemModel)parent.getItemAtPosition(position)).getGEM_SKILLS();
                level3 = ((GemModel)parent.getItemAtPosition(position)).getGEM_LEVEL();
                countHolderNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setAdapter(gemSpinnerAdapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill4 = ((GemModel)parent.getItemAtPosition(position)).getGEM_SKILLS();
                level4 = ((GemModel)parent.getItemAtPosition(position)).getGEM_LEVEL();
                countHolderNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner5.setAdapter(gemSpinnerAdapter);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skill5 = ((GemModel)parent.getItemAtPosition(position)).getGEM_SKILLS();
                level5 = ((GemModel)parent.getItemAtPosition(position)).getGEM_LEVEL();
                countHolderNumber();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countHolderNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countHolderNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countHolderNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countHolderNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countHolderNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 计算宝石等级
     */
    private void countHolderNumber() {
        if (editText1.getText().toString().equals("")
                || editText2.getText().toString().equals("")
                || editText3.getText().toString().equals("")
                || editText4.getText().toString().equals("")
                || editText5.getText().toString().equals("")) {
            return;
        }
        int curNum = level1 * Integer.valueOf(editText1.getText().toString()) +
                level2 * Integer.valueOf(editText2.getText().toString()) +
                level3 * Integer.valueOf(editText3.getText().toString()) +
                level4 * Integer.valueOf(editText4.getText().toString()) +
                level5 * Integer.valueOf(editText5.getText().toString());
        setHolderNumber(curNum);
    }

    /**
     * 处理数据
     */
    private void manageTotalData() {



        int a = Integer.valueOf(editText1.getText().toString());
        int b = Integer.valueOf(editText2.getText().toString());
        int c = Integer.valueOf(editText3.getText().toString());
        int d = Integer.valueOf(editText4.getText().toString());
        int e = Integer.valueOf(editText5.getText().toString());

        if (a > 0) {
            Object[] keys = skill1.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                skill1.put((Integer) keys[i], skill1.get(keys[i])*a);
                if (totalSkills.containsKey(keys[i])){
                    totalSkills.put((Integer) keys[i], totalSkills.get(keys[i]) + skill1.get(keys[i]));
                } else {
                    totalSkills.put((Integer) keys[i], skill1.get(keys[i]));
                }
            }
        }

        if (b > 0) {
            Object[] keys = skill2.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                skill2.put((Integer) keys[i], skill2.get(keys[i])*b);
                if (totalSkills.containsKey(keys[i])){
                    totalSkills.put((Integer) keys[i], totalSkills.get(keys[i]) + skill2.get(keys[i]));
                } else {
                    totalSkills.put((Integer) keys[i], skill2.get(keys[i]));
                }
            }
        }

        if (c > 0) {
            Object[] keys = skill3.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                skill3.put((Integer) keys[i], skill3.get(keys[i])*c);
                if (totalSkills.containsKey(keys[i])){
                    totalSkills.put((Integer) keys[i], totalSkills.get(keys[i]) + skill3.get(keys[i]));
                } else {
                    totalSkills.put((Integer) keys[i], skill3.get(keys[i]));
                }
            }
        }

        if (d > 0) {
            Object[] keys = skill4.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                skill4.put((Integer) keys[i], skill4.get(keys[i])*d);
                if (totalSkills.containsKey(keys[i])){
                    totalSkills.put((Integer) keys[i], totalSkills.get(keys[i]) + skill4.get(keys[i]));
                } else {
                    totalSkills.put((Integer) keys[i], skill4.get(keys[i]));
                }
            }
        }

        if (e > 0) {
            Object[] keys = skill5.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                skill5.put((Integer) keys[i], skill5.get(keys[i])*e);
                if (totalSkills.containsKey(keys[i])){
                    totalSkills.put((Integer) keys[i], totalSkills.get(keys[i]) + skill5.get(keys[i]));
                } else {
                    totalSkills.put((Integer) keys[i], skill5.get(keys[i]));
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (resultIsOk) {
                    manageTotalData();
                    Intent intent = new Intent();
                    intent.putExtra("resultData",totalSkills);
                    setResult(RESULT_OK, intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "宝珠超过限制", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    // 广告相关
    @ViewInject(R.id.ad_layout)
    private RelativeLayout ad_layout;

    private void showAD() {
        AdwoAdView adwoAdView = new AdwoAdView(this, "2c74632a76b44a2c83158e13c42b8c6b", true, 40);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ad_layout.addView(adwoAdView, layoutParams);
    }
}
