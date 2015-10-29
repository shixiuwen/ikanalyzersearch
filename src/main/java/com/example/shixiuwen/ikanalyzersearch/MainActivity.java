package com.example.shixiuwen.ikanalyzersearch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;


public class MainActivity extends ActionBarActivity {

    private EditText etSearch;
    private ListView lvResult;
    IKSegmenter ik;
    StringBuilder sb;

    String s1 = "光谷软件创新科技产业园";
    String s2 = "光谷产业园";
    String s3 = "华中科技大学";
    String s4 = "光谷软件园";
    String s5 = "软件科技产业园";
    String s6 = "光谷科技软件园";

    //定义一个String数组存放待分割的字符串
    private String[] searchResult = {s1,s2,s3,s4,s5,s6};

    int count01, count02, count03, count04, count05, count06;

    private String[] destination = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = (EditText) findViewById(R.id.id_et_des);
        Button btnSearch = (Button) findViewById(R.id.id_btn_search);
        lvResult = (ListView) findViewById(R.id.id_lv_result);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对目的地进行分词
                destination = split(etSearch.getText().toString().trim());
                //进行匹配排序
                String[] sort = sort(searchResult);
                lvResult.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, sort));
            }
        });
    }

    public String[] sort(String[] strArray) {

        //定义一个二维数组存放分词结果数组
        String[][] array2D = new String[6][];

        for (int i = 0; i < strArray.length; i++) {
            array2D[i] = split(strArray[i]);
        }

        //计数值初始化
        count01 = count02 = count03 = count04 = count05 = count06 = 0;
        int[] count = {count01, count02, count03, count04, count05, count06};

        for (int arr2D = 0; arr2D < array2D.length; arr2D++) {
            if (destination != null) {
                for (int i = 0; i < array2D[arr2D].length; i++) {
                    for (String aDestination : destination) {
                        if (array2D[arr2D][i].equals(aDestination)) {
                            count[arr2D]++;
                        }
                    }
                }
            }
        }

        //匹配计数完成，对结果进行排序,对计数排序的同时对选项字符数组同时进行了排序
        for (int i = 0; i < count.length; i++) {
            for (int j = i + 1; j < count.length; j++) {
                int temp;
                String Stems ;
                String[] SStems;
                if (count[i] < count[j]) {
                    //交换计数值的同时交换二维数组中字符串的顺序
                    temp = count[i];
                    Stems = strArray[i];
                    SStems = array2D[i];
                    count[i] = count[j];
                    strArray[i] = strArray[j];
                    array2D[i] = array2D[j];
                    count[j] = temp;
                    strArray[j] = Stems;
                    array2D[j] = SStems;
                } else if (count[i] == count[j]) {
                    //如果计数相等，把匹配最短的字符串前置
                    if (array2D[i].length > array2D[j].length) {
                        Stems = strArray[i];
                        SStems = array2D[i];
                        strArray[i] = strArray[j];
                        array2D[i] = array2D[j];
                        strArray[j] = Stems;
                        array2D[j] = SStems;
                    }
                }
            }
        }

        return strArray;

    }

    public String[] split(String str) {

        String[] resArray = null;

        StringReader sr = new StringReader(str);
        ik = new IKSegmenter(sr, true);
        sb = new StringBuilder();
        //开始分词
        Lexeme lex ;
        try {
            while ((lex = ik.next()) != null) {
                //分词结果以空格分开
                sb.append(lex.getLexemeText()).append(" ");
            }
            if (str.equals(etSearch.getText().toString().trim())) {
                Toast.makeText(MainActivity.this, sb, Toast.LENGTH_SHORT).show();
            }
            //得到解析结果
            resArray = sb.toString().split(" ");
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "数据转换异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return resArray;
    }
}
//我对文件进行了修改
