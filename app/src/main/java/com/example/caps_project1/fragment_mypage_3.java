package com.example.caps_project1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;


/* 다이어리 : 달력이 나오고 날짜를 클릭해서 글 쓰기 (일기장) */
public class fragment_mypage_3 extends Fragment {
    private Context mContext;

    MaterialCalendarView calendarView;

    DatePicker datePicker; // 날짜를 선택
    TextView viewDatePick; // 선택한 날짜를 보여줌
    EditText editDiary;     // 선택한 날짜의 일기를 쓴다, 저장된 일기가 있다면 보여준다
    Button btn_save; // 선택한 날짜의 일기 저장, 수정
    String fileName; // 선택된 날짜의 파일 이름


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_3, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        viewDatePick = view.findViewById(R.id.viewDatePick);
        editDiary = view.findViewById(R.id.editDiary);
        btn_save = view.findViewById(R.id.btn_save);
        
        // 오늘 날짜 받아오기
        Calendar cal = Calendar.getInstance();
        int calYear = cal.get(Calendar.YEAR);
        int calMonth = cal.get(Calendar.MONTH) + 1;
        int calDay = cal.get(Calendar.DAY_OF_MONTH);
        
        // 시작 : 오늘 날짜 
        checkedDay(calYear, calMonth, calDay);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // 첫 시작에 글이 있는지 체크
                checkedDay(year, monthOfYear, dayOfMonth);
            }
        });

        // 저장 / 수정 버튼
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fileName 을 넣고 저장시키는 메소드
                saveDiary(fileName);
            }
        });


        return view;
    }


    // 다이어리 저장
    private void saveDiary(String readDay) {

        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(readDay, mContext.MODE_PRIVATE);
            String content = editDiary.getText().toString();

            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(mContext, "저장되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "저장 실패", Toast.LENGTH_SHORT).show();

        }
    }


    // 파일 읽기
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {

        // 선택한 날짜 보여주기
        viewDatePick.setText(year + " - " + monthOfYear + " - " + dayOfMonth);

        // 파일 이름
        fileName = year + "" + monthOfYear + "" + dayOfMonth + ".txt";


        FileInputStream fm = null;

        try {
            fm = mContext.openFileInput(fileName);
            byte[] fileData = new byte[fm.available()];
            fm.read(fileData); // fileData 읽음
            fm.close();

            String str = new String(fileData, "UTF-8");

            editDiary.setText(str);
            btn_save.setText("수정하기");
        } catch (Exception e) {

//            Toast.makeText(mContext, "저장된 글이 없습니다.", Toast.LENGTH_SHORT).show();
            editDiary.setText("");
            btn_save.setText("새 글 저장");
            e.printStackTrace();
        }

    }
}
