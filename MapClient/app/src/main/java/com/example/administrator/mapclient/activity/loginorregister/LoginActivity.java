package com.example.administrator.mapclient.activity.loginorregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.activity.homepage.HomeActivity;

import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登录页
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    static Socket soc;
    private EditText ed_user;
    private EditText ed_passw;
    private CheckBox checkbox;
    private TextView Forgotpassw;
    private Button butlogin;
    private TextView text_register;
    static String YES = "yes";
    static String NO = "no";
    static String name, password;
    private String isMemory = "";//isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
    private String FILE = "saveUserNamePwd";//用于保存SharedPreferences的文件
    private SharedPreferences sp = null;//声明一个SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initdate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == 111) {
            ed_user.setText(data.getStringExtra("phon"));
            ed_passw.setText(data.getStringExtra("pasw"));
        }
    }
    private void initdate() {
        findview();
        ed_user.setOnClickListener(this);
        ed_passw.setOnClickListener(this);
        text_register.setOnClickListener(this);
        checkbox.setOnClickListener(this);
        Forgotpassw.setOnClickListener(this);
        butlogin.setOnClickListener(this);
        shardate();
    }
    /**
     * 判断SharedPreferences中的user和passw是否有数据
     */
    private void shardate() {
        if (isMemory.equals(YES)) {
            name = sp.getString("names", "");
            password = sp.getString("password", "");
            ed_user.setText(name);
            ed_passw.setText(password);
        }else {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, ed_user.toString());
        editor.putString(password, ed_user.toString());
        editor.commit();
        }
    }
    private void findview() {
        ed_user = (EditText) findViewById(R.id.ed_user);
        ed_passw = (EditText) findViewById(R.id.ed_passw);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        Forgotpassw = (TextView) findViewById(R.id.Forgotpassw);
        butlogin = (Button) findViewById(R.id.butlogin);
        text_register = (TextView) findViewById(R.id.text_register);
        sp = getSharedPreferences(FILE, MODE_PRIVATE);
        isMemory = sp.getString("isMemory", NO);
    }
    /**
     * 控件点击监听的方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        int Idkey =v.getId();
        switch (Idkey){
            case R.id.butlogin: //登录
                remenber();
                name = ed_user.getText().toString();
                password = ed_passw.getText().toString();
                butlogin();
            /*    User user = new User(1,"18387390390","abc123");
                JsonBean jsonBean = new JsonBean(200, user.getUid(),"","");
                jsonBean.setUser(user);
                soc = new Socket();
                try {
                    soc.connect(new InetSocketAddress("10.7.184.56", 10090),5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter printWriter = null;
                try {
                    printWriter = new PrintWriter(new OutputStreamWriter(soc.getOutputStream(),"utf-8"),true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printWriter.println(JSON.toJSONString(jsonBean)+"\n");
                
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(
                            new InputStreamReader(soc.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String readLine = null;
                try {
                    readLine = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
             */
            break;
            case R.id.text_register: //免费注册
               Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,222);
                //finish();
            break;
            case R.id.Forgotpassw: //忘记密码
              Intent intent1 = new Intent(LoginActivity.this,ChangPaswActivity.class);
                startActivityForResult(intent1,222);
                //finish();
            break;
            
        }
    }
    /**
     * 判断是否要记住密码  
     */
    public void remenber() { 
        if (checkbox.isChecked()) {
            if (sp == null) {
                sp = getSharedPreferences(FILE, MODE_PRIVATE);
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("names", ed_user.getText().toString());
            edit.putString("password", ed_passw.getText().toString());
            edit.putString("isMemory",YES);
            edit.commit();
          
        } else if (!checkbox.isChecked()) {
           // checkbox.setChecked(false);
            if (sp == null) {
                sp = getSharedPreferences(FILE, MODE_PRIVATE);
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("isMemory", NO);
            edit.commit();
        }
    }
    /**
     * 点击登录前的判断
     */
    private void butlogin() {
        if (TextUtils.isEmpty(ed_user.getText().toString())){
            ed_user.setError("手机号不能为空！");
            ed_user.requestFocus();
        }else if (!isMobileNO(ed_user.getText().toString())){
            ed_user.setError("手机号码格式错误");
            ed_user.requestFocus();
        }else if (TextUtils.isEmpty(ed_passw.getText().toString())){
            ed_passw.setError("密码不能为空！");
            ed_passw.requestFocus();
        }else if (!isNumericAZ(ed_passw.getText().toString())){
            ed_passw.setError("密码由数字或字母构成且最少6位！");
            ed_passw.requestFocus(); 
        }else {
           Intent intent = new Intent(LoginActivity.this,
                   HomeActivity.class);
            startActivityForResult(intent,222);
            finish();
        }
    }

    /**
     * 验证号码
     * @param mobileNums
     * @return
     */
    public  boolean isMobileNO(String mobileNums) {
        String telRegex = "^[1][34578][0-9]{9}$";//[0-9]* "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    /**
     * 正则表达式判断输入的内容只能是数字或字母
     * @param str
     * @return
     */
    public boolean isNumericAZ(String str){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{6,30}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches()){
            return false;
        }
        return true;
    }
}
