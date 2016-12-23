package com.isec.tetris.DataScoresRelated;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isec.tetris.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Miguel on 23-12-2016.
 */

public class ScoreAdapter extends ArrayAdapter<Score> {

    Context context;
    ArrayList<Score> scoreList;
    Score score;

    LinearLayout linearLayout;
    TextView textViewScore;
    TextView textViewDate;

    public ScoreAdapter(Context context, ArrayList<Score> scoreList) {
        super(context, R.layout.listview_adapter, scoreList);
        this.context = context;
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(itemView == null)
            itemView = inflater.inflate(R.layout.listview_adapter, parent, false);

        linearLayout = (LinearLayout) itemView.findViewById(R.id.main_linear_layout);
        textViewScore = (TextView) itemView.findViewById(R.id.scoreTextView);
        textViewDate  = (TextView) itemView.findViewById(R.id.dateTextView);

        score = scoreList.get(position);
        if(position%2!=0)
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.trans_back_for_words));

        textViewScore.setText(position+1 +". "+score.getScore() + " "+ context.getResources().getString(R.string.points));
        textViewDate.setText(makeDate());

        return itemView;
    }

    private String makeDate() {

        Calendar cal = score.getToday();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM HH:mm");
        String formatted = format.format(cal.getTime());

        return  formatted;
    }
}
