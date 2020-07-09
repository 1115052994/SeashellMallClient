package com.liemi.seashellmallclient.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.liemi.seashellmallclient.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by Bingo on 2018/9/7.
 */

public class SearchRecordAdapter extends TagAdapter<String> {


    public SearchRecordAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tvLabel= (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.sharemall_item_search_record,parent,false);
        tvLabel.setText(getItem(position));
        return tvLabel;
    }

}
