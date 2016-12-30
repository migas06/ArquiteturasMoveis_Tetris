package com.isec.tetris.Learning;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isec.tetris.R;

public class TetrominoFragment extends Fragment {


    Canvas canvas;
    Paint paint;

    public TetrominoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tetromino, container, false);



        return view;
    }

}
