package com.alfame.ville.memo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> categories;
    private HashMap<String, List<String> > categoryHashMap;//Should be HashMap<String, List<Note>>
    private static ILaunchEditDialogListener launchEditDialogListener;

    public CategoryAdapter(Context context, List<String> categories, HashMap<String, List<String>> categoryHashMap) {
        this.context = context;
        this.categories = categories;
        this.categoryHashMap = categoryHashMap;
    }

    public interface ILaunchEditDialogListener {
        abstract void launchEditDialog(int groupPosition, int childPosition);
    }

    public static void setLaunchEditDialogListener(ILaunchEditDialogListener listener) {
        launchEditDialogListener = listener;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryHashMap.get(categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryHashMap.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryHashMap.get(categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView tv = convertView.findViewById(R.id.categoryName);
        tv.setText(categories.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView tv = convertView.findViewById(R.id.noteTitle);
        Button btn = convertView.findViewById(R.id.itemBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditDialogListener.launchEditDialog(groupPosition, childPosition);
            }
        });
        tv.setText(getChild(groupPosition, childPosition).toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
