package com.example.customdialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    View getlistview;
    String[] mlistText={"全选","选择1","选择1","选择1","选择1","选择1","选择1","选择1","选择1","选择1","选择1"};
    ArrayList<Map<String,Object>> mData=new ArrayList<Map<String, Object>>();
    //创建一个对话框
    AlertDialog.Builder builder;
    AlertDialog builder2;
    //创建一个SimpleAdapter适配器
    SimpleAdapter adapter;
    //设置记录按钮是否选中的数组
    Boolean[]bl={false,false,false,false,false,false,false,false,false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
       // int lengh=mlistText.length;
        for (int i=0;i<mlistText.length;i++){
            Map<String,Object> item=new HashMap<String, Object>();
            item.put("text",mlistText[i]);
            mData.add(item);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }
    class ItemOnclick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取item单选框
            CheckBox checkBox=view.findViewById(R.id.X_checkbox);
            //判断按下状态，再次点击修改状态
            if (checkBox.isChecked()){
                checkBox.setChecked(false);
            }else {
                checkBox.setChecked(true);
            }
            if (position==0&&(checkBox.isChecked())){
                //判断全选按钮是否选中，若选中，则对adapter进行更新
                for (int i=0;i<bl.length;i++){
                    bl[i]=true;
                }
                //更新adapter
                adapter.notifyDataSetInvalidated();
            }else if (position==0&&(!checkBox.isChecked())){
                //若全选状态修改，则将所有子项改为未选中
                for (int i=0;i<bl.length;i++){
                    bl[i]=false;
                }
                adapter.notifyDataSetInvalidated();
            }else if (position!=0&&(!checkBox.isChecked())){
                //如果其他选项取消，则将全选状态取消
                bl[0]=false;
                bl[position]=false;
                adapter.notifyDataSetInvalidated();
            }else if (position!=0&&(checkBox.isChecked())){
                //如果其它选项全部选中，则点亮全选按钮，
                bl[position]=true;
                int a=0;
                for (int i=1;i<bl.length;i++){
                    if (bl[i]==false){
                        break;
                    }else {
                        a++;
                        if (a==bl.length-1){
                            bl[0]=true;
                            adapter.notifyDataSetInvalidated();
                        }
                    }
                }
            }
        }
    }
    private void createDialog() {
        //动态加载listview布局
        LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
        getlistview =inflater.inflate(R.layout.listview,null);
        //为listview绑定数据以及adapter
        ListView listView=getlistview.findViewById(R.id.X_listview);
        adapter=new SimpleAdapter(MainActivity.this,mData,R.layout.listitem,new String[]{"text"},new int[]{ R.id.X_item_text });
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //创建监听器
        listView.setOnItemClickListener(new ItemOnclick());

        builder=new AlertDialog.Builder(this);
        builder.setTitle("请选择查询类型");
        builder.setIcon(R.drawable.ic_launcher_background);
        //设置加载的listview
        builder.setView(getlistview);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您选择了确认", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您选择了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
    class SetSimpleAdapter extends SimpleAdapter{
        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */
        public SetSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= LinearLayout.inflate(getBaseContext(),R.layout.listitem,null);
            }
            CheckBox checkBox=convertView.findViewById(R.id.X_checkbox);
            //根据bl[]更新CheckBox
            if (bl[position]==true){
                checkBox.setChecked(true);
            }else if (bl[position]==false){
                checkBox.setChecked(false);
            }
            return super.getView(position, convertView, parent);
        }
    }
}
