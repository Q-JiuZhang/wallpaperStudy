package com.example.myapplication.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class FindFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slide_with_buttons, container, false);
        TextView tv = root.findViewById(R.id.tv_title);
        tv.setText("Find");
        Button b1 = root.findViewById(R.id.btn_transparent_1);
        Button b2 = root.findViewById(R.id.btn_transparent_2);
        b1.setOnClickListener(v -> Toast.makeText(requireContext(), "Find 按钮1", Toast.LENGTH_SHORT).show());
        b2.setOnClickListener(v -> Toast.makeText(requireContext(), "Find 按钮2", Toast.LENGTH_SHORT).show());
        return root;
    }
}