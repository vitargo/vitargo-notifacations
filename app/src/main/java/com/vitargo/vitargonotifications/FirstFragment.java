package com.vitargo.vitargonotifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.vitargo.vitargonotifications.databinding.FragmentFirstBinding;
import com.vitargo.vitargonotifications.db.CustomNotification;
import com.vitargo.vitargonotifications.db.NotificationDBHelper;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private NotificationDBHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("onCreateView", "Start onCreateView FirstFragment...");
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        helper = new NotificationDBHelper(FirstFragment.this.getActivity());
        updateTable();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("onViewCreated", "Start onViewCreated FirstFragment...");
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = FirstFragment.this.getActivity().getPreferences(Context.MODE_PRIVATE);
                int savedResult = sharedPref.getInt(getString(R.string.check_box_state), -1);
                Log.d("Saved Notification", String.valueOf(savedResult));
                if(savedResult > -1){
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                } else {
                    FirstFragment.this.getActivity().runOnUiThread(() -> Toast.makeText(FirstFragment.this.getActivity(), "Please, make a choice!", Toast.LENGTH_LONG).show());
                }
            }
        });
        binding.buttonFirstDelele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.deleteAllNotifications();
                FirstFragment.this.getActivity().recreate();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void updateTable(){
        clearCheckBoxSavedResult();
        TableLayout table = binding.getRoot().findViewById(R.id.notification);
        List<CustomNotification> notifications = helper.getAllNotifications();

        for (CustomNotification notification : notifications){
            Log.d("Notification", notification.toString());
            TableRow row = new TableRow(table.getContext());
            row.setId(View.generateViewId());
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);
            CheckBox box = new CheckBox(table.getContext());
            box.setId(View.generateViewId());
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("isChecked", String.valueOf(isChecked));
                    SharedPreferences sharedPref = FirstFragment.this.getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    if(isChecked){
                        int savedResult = sharedPref.getInt(getString(R.string.check_box_state), -1);
                        Log.d("savedResult", String.valueOf(savedResult));
                        if(savedResult > -1){
                            CheckBox prev = FirstFragment.this.getActivity().findViewById(savedResult);
                            prev.setChecked(false);
                        }
                        int newBox = box.getId();
                        Log.d("newBox", String.valueOf(newBox));
                        editor.putInt(getString(R.string.check_box_state), newBox);
                        editor.putInt(getString(R.string.checked_notification), row.getId());
                        editor.putString(getString(R.string.cb_text),notification.getText());
                        editor.putString(getString(R.string.cb_title),notification.getTitle());
                        editor.putString(getString(R.string.cb_date),notification.getDate());
                    } else {
                        editor.putInt(getString(R.string.check_box_state), -1);
                        editor.putInt(getString(R.string.checked_notification), -1);
                    }
                    editor.apply();
                }
            });
            row.addView(box);
            TextView title = new TextView(table.getContext());
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setText(notification.getTitle());
            row.addView(title);
            TextView text = new TextView(table.getContext());
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setText(notification.getText());
            row.addView(text);
            TextView date = new TextView(table.getContext());
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            date.setText(notification.getDate());
            row.addView(date);
            table.addView(row);
        }
    }

    private void clearCheckBoxSavedResult(){
        SharedPreferences sharedPref = FirstFragment.this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.check_box_state), -1);
        editor.apply();
    }

}