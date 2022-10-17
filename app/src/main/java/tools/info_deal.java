package tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class info_deal {
    /**
     * 保存用户信息
     */
    public static void saveUserInfo(Context context, String filename, String tag, String data){
        SharedPreferences userInfo = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        //得到Editor后，写入需要保存的数据
        editor.putString(tag, data);
        editor.commit();//提交修改
        Log.d(tag,data+"已保存");
    }
    /**
     * 读取用户信息
     */
    public static String getUserInfo(Context context, String filename, String tag){
        String info = null;
        SharedPreferences Info = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        info = Info.getString(tag, null);//读取username
        Log.i(tag, info+"被读取");
        return info;
    }
    /**
     * 移除信数据
     */
    public static void removeUserInfo(Context context, String filename, String tag){
        SharedPreferences userInfo = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.remove(tag);
        editor.commit();
        Log.i("TAG", tag+"移除");

    }

    /**
     * 清空数据
     */
    public static void clearUserInfo(Context context, String filename){
        SharedPreferences userInfo = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.clear();
        editor.commit();
        Log.i("TAG", "清空数据");
    }
}
