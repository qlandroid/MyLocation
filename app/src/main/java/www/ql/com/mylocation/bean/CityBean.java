package www.ql.com.mylocation.bean;

/**
 * Created by Administrator on 2017-4-13.
 */
public class CityBean {
    private String pr;//省份
    private String code;//城市code
    private String city;//城市名称；

    @Override
    public String toString() {
        return "CityBean{" +
                "pr='" + pr + '\'' +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public CityBean() {
    }

    public CityBean(String pr, String code, String city) {
        this.pr = pr;
        this.code = code;
        this.city = city;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
