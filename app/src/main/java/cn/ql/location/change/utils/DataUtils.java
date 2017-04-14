package cn.ql.location.change.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ql.location.change.bean.CityBean;

/**
 * Created by Administrator on 2017-4-13.
 */
public class DataUtils {
    public static List<CityBean> getCityList(String cityJson){
        try {
            JSONArray jsonArray = new JSONArray(cityJson);
            List<CityBean> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                /*
                 * "pr" : "安徽",
                 * "code" : "0553",
                 * "city" : "芜湖"
                 */
               JSONObject obj =  jsonArray.getJSONObject(i);
                String pr = obj.getString("pr");
                String code = obj.getString("code");
                String city = obj.getString("city");
                CityBean bean = new CityBean(pr,code,city);
                list.add(bean);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
