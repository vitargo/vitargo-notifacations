package com.vitargo.vitargonotifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.vitargo.vitargonotifications.databinding.FragmentSecondBinding;
import com.vitargo.vitargonotifications.db.NotificationDBHelper;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private NotificationDBHelper helper;

    private TextView title;
    private TextView text;
    private TextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        helper = new NotificationDBHelper(SecondFragment.this.getActivity());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPref = SecondFragment.this.getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedResult = sharedPref.getInt(getString(R.string.checked_notification), -1);
        if(savedResult > -1){
            title = SecondFragment.this.getActivity().findViewById(R.id.title_one_value);
            title.setText(sharedPref.getString(getString(R.string.cb_title), ""));
            text = SecondFragment.this.getActivity().findViewById(R.id.text_one_value);
            text.setText(sharedPref.getString(getString(R.string.cb_text), ""));
            date = SecondFragment.this.getActivity().findViewById(R.id.date_one_value);
            date.setText(sharedPref.getString(getString(R.string.cb_date), ""));
        }

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        binding.buttonSecondDelele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.deleteNotificationByNameAndDate(title.getText().toString(), date.getText().toString());
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}