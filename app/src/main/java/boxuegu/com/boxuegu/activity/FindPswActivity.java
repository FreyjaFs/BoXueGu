package boxuegu.com.boxuegu.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import boxuegu.com.boxuegu.R;
import boxuegu.com.boxuegu.utils.AnalysisUtils;
import boxuegu.com.boxuegu.utils.MD5Utils;

public class FindPswActivity extends AppCompatActivity {

    private EditText et_validate_name,et_user_name;
    private Button btn_validate;
    private TextView tv_main_title;
    private TextView tv_back;
    private String from;
    private TextView tv_reset_psw,tv_user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw);
        from=getIntent().getStringExtra("from");
        init();
    }

    private void init() {
        tv_main_title=(TextView)findViewById(R.id.tv_main_title);
        tv_back=(TextView)findViewById(R.id.tv_back);
        et_validate_name =(EditText)findViewById(R.id.et_validtate_name);
        btn_validate=(Button)findViewById(R.id.btn_validate);
        tv_reset_psw=(TextView) findViewById(R.id.tv_reset_psw);
        et_user_name=(EditText)findViewById(R.id.et_user_name);
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        if("security".equals(from)){
            tv_main_title.setText("设置密保");
        }else {
            tv_main_title.setText("找回密码");
            tv_user_name.setVisibility(View.VISIBLE);
            et_user_name.setVisibility(View.VISIBLE);
        }
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindPswActivity.this.finish();
            }
        });
        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //此处有错误
                String validateNmae = String.valueOf(et_validate_name.getText());
                toString().trim();
                if ("security".equals(from)){
                    if(TextUtils.isEmpty(validateNmae)){
                        Toast.makeText(FindPswActivity.this,"请输入要验证的名字",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Toast.makeText(FindPswActivity.this,"密保设置成功",Toast.LENGTH_SHORT).show();
                        saveSecurity(validateNmae);
                        FindPswActivity.this.finish();
                    }
                }else {
                    String userName = et_user_name.getText().toString().trim();
                    String sp_security = readSecurity(userName);

                    if(TextUtils.isEmpty(userName)){
                        Toast.makeText(FindPswActivity.this,"请输入您的用户名",Toast.LENGTH_SHORT).show();
                        return;
                    }else if(!isExistUserName(userName)){
                        Toast.makeText(FindPswActivity.this,"您输入的用户名不存在",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }else if(TextUtils.isEmpty(validateNmae)){
                        Toast.makeText(FindPswActivity.this,"请输入要验证的姓名",
                                Toast.LENGTH_SHORT).show();
                    }if(!validateNmae.equals(sp_security)){
                        Toast.makeText(FindPswActivity.this,"输入的密保不正确",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        tv_reset_psw.setVisibility(View.VISIBLE);
                        tv_reset_psw.setText("初始密码：123456");
                        savePsw(userName);
                    }
                }
            }
        });
    }

    private void savePsw(String userName) {
        String md5Psw = MD5Utils.md5("123456");
        SharedPreferences sp = getSharedPreferences("LoginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString(userName,md5Psw);
        editor.commit();
    }

    private String readSecurity(String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String security = sp.getString(userName+"_security","");
        return security;
    }

    private boolean isExistUserName(String userName) {
        boolean hasUserName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String spPsw= sp.getString(userName,"");
        if(!TextUtils.isEmpty(spPsw)){
            hasUserName=true;
        }
        return hasUserName;
    }

    private void saveSecurity(String validateNmae) {
        SharedPreferences sp =getSharedPreferences("loginInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(AnalysisUtils.readLoginUserName(this)+"_security",
                validateNmae);
        editor.commit();
    }
}