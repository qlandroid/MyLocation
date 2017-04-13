package www.ql.com.mylocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.List;

import www.ql.com.mylocation.adapter.TextAdapter;

public class QueryAty extends AppCompatActivity implements Inputtips.InputtipsListener {

    private EditText etKeyWord, etCity;
    private ListView lv;
    private TextAdapter textAdapter;

    List<Tip> list;
    private String cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_aty);
        lv = (ListView) findViewById(R.id.lv);
        etKeyWord = (EditText) findViewById(R.id.et_address);
        etCity = (EditText) findViewById(R.id.et_city);

        textAdapter = new TextAdapter(this, null, android.R.layout.simple_list_item_1);
        lv.setAdapter(textAdapter);
        cityCode = etCity.getText().toString();

        etKeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
                InputtipsQuery inputquery = new InputtipsQuery(s.toString(), cityCode);
                Inputtips inputTips = new Inputtips(QueryAty.this, inputquery);
                inputTips.setInputtipsListener(QueryAty.this);
                inputTips.requestInputtipsAsyn();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tip t = list.get(position);

            }
        });

    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        textAdapter.update(list);
    }
}
