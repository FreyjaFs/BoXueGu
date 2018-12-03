package boxuegu.com.boxuegu.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import boxuegu.com.boxuegu.R;
import boxuegu.com.boxuegu.utils.AnalysisUtils;
import boxuegu.com.boxuegu.utils.DBUtils;
import boxuegu.com.boxuegu.bean.UserBean;

public class UseInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_back;
    private TextView tv_main_title;
    private TextView tv_nickName,tv_user_name, tv_sex,tv_signature;
    private RelativeLayout rl_nickName,rl_sex,rl_signature,rl_title_bar;
    private String spUserName;
    private static final int CHANGE_NICKNAME=1;
    private static final int CHANGE_SIGNATURE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spUserName = AnalysisUtils.readLoginUserName(this);
        init();
        getNewGoodsInfo();
        initData();
        setListener();
    }

    private void getNewGoodsInfo(){

    }

    private void initData() {
        UserBean bean = null;
        bean = DBUtils.getInstance(this).getUserInfo(spUserName);
        if (bean == null){
            bean=new UserBean();
            bean.userName = spUserName;
            bean.nickName = "问答精灵";
            bean.signature = "问答精灵";
            bean.sex = "男";
            DBUtils.getInstance(this).saveUserInfo(bean);
        }setValue(bean);
    }

    private void init() {
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_main_title = (TextView)findViewById(R.id.tv_main_title);
        tv_main_title.setText("个人资料");
        rl_title_bar = (RelativeLayout)findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        rl_nickName = (RelativeLayout)findViewById(R.id.rl_nickName);
        rl_sex = (RelativeLayout)findViewById(R.id.rl_sex);
        rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
        tv_nickName = (TextView)findViewById(R.id.tv_nickName);
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_signature = (TextView)findViewById(R.id.tv_signature);


    }

    public void setValue(UserBean bean) {
        tv_nickName.setText(bean.nickName);
        tv_user_name.setText(bean.userName);
        tv_sex.setText(bean.sex);
        tv_signature.setText(bean.signature);
    }
    private void setListener() {
        tv_back.setOnClickListener(this);
        rl_signature.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_nickName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                this.finish();
                break;
            case R.id.rl_nickName:
                String name=tv_nickName.getText().toString();
                Bundle bdName=new Bundle();
                bdName.putString("content",name);
                bdName.putInt("flag",1);
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_NICKNAME,bdName);
                break;

            case R.id.rl_sex:
                String sex=tv_sex.getText().toString();
                sexDiaglog(sex);

                break;
            case R.id.rl_signature:
                String signature=tv_signature.getText().toString();
                Bundle bdsignature=new Bundle();
                bdsignature.putString("content",signature);
                bdsignature.putInt("flag",2);
                enterActivityForResult(ChangeUserInfoActivity.class,CHANGE_SIGNATURE,bdsignature);

                break;
            default:
                break;



        }
    }

    private void sexDiaglog(String sex) {
        int sexFlag=0;
        if("男".equals(sex)){
            sexFlag=0;
        }else if("女".equals(sex)){
            sexFlag=1;
        }
        final String items[]={"男","女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("性别");
        builder.setSingleChoiceItems(items, sexFlag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UseInfoActivity.this,items[which],Toast.LENGTH_SHORT).show();
                setSex(items[which]);
            }
        });
        builder.create().show();
    }

    private void setSex(String sex) {
        tv_sex.setText(sex);
        DBUtils.getInstance(UseInfoActivity.this).updateUserInfo("sex",sex,spUserName);
    }

    public void enterActivityForResult(Class<?>to,int requestCode,Bundle b){
        Intent i = new Intent(this,to);
        i.putExtras(b);
        startActivityForResult(i,requestCode);
    }

    private String new_info;

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case CHANGE_NICKNAME:
            if(data!=null){
                new_info=data.getStringExtra("nickName");
                if (TextUtils.isEmpty(new_info)){
                    return;
                }
                tv_nickName.setText(new_info);
                DBUtils.getInstance(UseInfoActivity.this).updateUserInfo("nickName",new_info,
                        spUserName);

            }break;
            case CHANGE_SIGNATURE:
                if (data!=null){
                    new_info=data.getStringExtra("signature");
                    if(TextUtils.isEmpty(new_info)){
                        return;
                    }
                    tv_signature.setText(new_info);
                    DBUtils.getInstance(UseInfoActivity.this).updateUserInfo("signature",new_info,
                            spUserName);
                }break;
        }
    }
}
