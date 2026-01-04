package com.example.studentmanagement;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class CustomMarkerView extends MarkerView {

    private final TextView tvContent;
    private final String[] months = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun"};

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
    }

    // This runs every time the popup is shown
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        String month = (index >= 0 && index < months.length) ? months[index] : "Unknown";
        int val = (int) e.getY();

        // Format the text: "Month: Jan \n Income: $12,000"
        tvContent.setText("Month: " + month + "\nIncome: $" + val);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        // Center the marker above the bar
        return new MPPointF(-(getWidth() / 2f), -getHeight());
    }
}