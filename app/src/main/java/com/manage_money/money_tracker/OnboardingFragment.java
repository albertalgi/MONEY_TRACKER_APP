package com.manage_money.money_tracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.manage_money.money_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ImageView imageView1;
    private Button button1;

    int position = 1;

    public OnboardingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnboardingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnboardingFragment newInstance(String param1, String param2) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.onboarding_fragment_layout, container, false);
        imageView1 = view.findViewById(R.id.onboardingImgView);
        button1 = view.findViewById(R.id.onbiardingButton);
        switch (this.position) {
            case 1:
                imageView1.setImageResource(R.mipmap.a_1);
                button1.setText("NEXT");
                break;
            case 2:
                imageView1.setImageResource(R.mipmap.a_2);
                button1.setText("NEXT");
                break;
            case 3:
                imageView1.setImageResource(R.mipmap.a_3);
                button1.setText("CONTINUE");
                break;
            case 4:
                imageView1.setImageResource(R.mipmap.a_4);
                button1.setText("CONTINUE");
                break;
            case 5:
                imageView1.setImageResource(R.mipmap.a_5);
                button1.setText("NEXT");
                break;
            case 6:
                imageView1.setImageResource(R.mipmap.a_6);
                button1.setText("NEXT");
                break;

        }
        return view;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}