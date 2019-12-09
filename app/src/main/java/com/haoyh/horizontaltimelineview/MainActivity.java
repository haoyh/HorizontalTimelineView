package com.haoyh.horizontaltimelineview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.haoyh.horizontaltimelineview.widget.HorizontalTimelineView;
import com.haoyh.horizontaltimelineview.widget.TimeLineInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 构建数据
        List<TimeLineInfo> dataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dataList.add(new TimeLineInfo("2019.12.0" + (i + 1), "时间信息", i > 2));
        }

        HorizontalTimelineView timelineView = findViewById(R.id.timeline);
        timelineView.setDataList(dataList);
    }
}
