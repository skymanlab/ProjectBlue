package com.skyman.billiarddata.management.billiard.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skyman.billiarddata.R;
import com.skyman.billiarddata.management.billiard.data.BilliardData;
import com.skyman.billiarddata.management.billiard.data.BilliardDataFormatter;

import java.util.ArrayList;

public class BilliardLvAdapter extends BaseAdapter {

    // value : list View 에 뿌려줄 데이터가 담긴 array
    private ArrayList<BilliardData> billiardDataArrayList = new ArrayList<>();

    @Override
    public int getCount() {
        return billiardDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return billiardDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /* 'activity_select_item' Layout을 inflate 하여 converView 참조 획득*/
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_lv_billiard_data, parent, false);
        }

        /* 'activity_select_item' 에 정의된 위젯에 대한 참조 획득
        * 1. id
        * 2. date
        * 3. target score
        * 4. play time
        * 5. victoree
        * 6. score
        * 7. cost
        * */
        TextView id = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_id);                              // 0. id
        TextView date = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_date);                          // 1. date
        TextView target_score = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_target_score);          // 2. target score
        TextView speciality = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_speciality);              // 3. speciality
        TextView play_time = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_play_time);                // 4. play time
        TextView victoree = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_winner);                    // 5. winner
        TextView score = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_score);                        // 6. score
        TextView cost = (TextView) convertView.findViewById(R.id.c_lv_billiard_data_cost);                          // 7. cost

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 DataListItem 재활용 */
        BilliardData billiardData = (BilliardData) getItem(position);

        /* 각 위젯에 셋팅된 아이템을 뿌려준다. */
        id.setText(billiardData.getId() +"");                                                               // 0. id
        date.setText(billiardData.getDate());                                                               // 1. date
        target_score.setText(billiardData.getTargetScore());                                                // 2. target score
        speciality.setText(billiardData.getSpeciality());                                                   // 3. speciality
        play_time.setText(BilliardDataFormatter.setFormatToPlayTime(billiardData.getPlayTime()));           // 4. play time
        victoree.setText(billiardData.getWinner());                                                         // 5. winner
        score.setText(billiardData.getScore());                                                             // 6. score
        cost.setText(BilliardDataFormatter.setFormatToCost(billiardData.getCost()));                        // 7. cost

        return convertView;
    }

    // method : billiard 테이블에서 읽어온 내용을 ArrayList<BilliardData> 에 추가한다.
    public void addItem(long id, String date, String target_score, String speciality, String play_time, String winner, String score, String cost){
        BilliardData billiardData = new BilliardData();

        /* DataListItem 에 아이템을 setting 한다.*/
        billiardData.setId(id);
        billiardData.setDate(date);

        billiardData.setTargetScore(target_score);
        billiardData.setSpeciality(speciality);
        billiardData.setPlayTime(play_time);

        billiardData.setWinner(winner);
        billiardData.setScore(score);
        billiardData.setCost(cost);

        /* items 에 dataListItem 을 추가한다.*/
        billiardDataArrayList.add(billiardData);
    }
}
