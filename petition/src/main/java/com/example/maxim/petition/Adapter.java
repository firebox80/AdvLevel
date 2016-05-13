package com.example.maxim.petition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.Categories;
import models.Petition;

/**
 * Created by Maxim on 06.04.2016.
 */
public class Adapter extends ArrayAdapter<Petition> {
    Context context;
    ArrayList<Petition> currentPetition;
    private String petition_category;

    public Adapter(Context context, int resource, List<Petition> currentPetition) {
        super(context, resource, currentPetition);
        this.context = context;
        this.currentPetition = new ArrayList<Petition>(currentPetition);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.item_custom, parent, false);
        } else{
            rootView = convertView;
        }

//        ImageView ivFolderIcon = (ImageView)rootView.findViewById(R.id.ivFolderIcon);
//        ivFolderIcon.setImageResource(R.drawable.picd);
        TextView tvCategoryName = (TextView)rootView.findViewById(R.id.tv1);

        petition_category = currentPetition.get(position).petition_category;

//        tvCategoryName.setText();

        TextView tvPetitionTitle = (TextView)rootView.findViewById(R.id.tv2);
        tvPetitionTitle.setText(currentPetition.get(position).title);

        TextView tvPetitionCreated = (TextView)rootView.findViewById(R.id.tv5);
        tvPetitionTitle.setText(currentPetition.get(position).created);

        TextView tvPetitionVote = (TextView)rootView.findViewById(R.id.tv6);
        tvPetitionTitle.setText(currentPetition.get(position).votes);


        TextView tvTypeName = (TextView)rootView.findViewById(R.id.tv3);
        TextView tvTypeNeed = (TextView)rootView.findViewById(R.id.tv8);
        TextView tvNeedVote  = (TextView)rootView.findViewById(R.id.tv10);
        TextView tvNeedDay = (TextView)rootView.findViewById(R.id.tv12);

        return rootView;
    }

    public String findParameter() {
        String categoryName = "1";

        for (Categories value : MainActivity.listCategories) {
            if (value.id.equals(petition_category)) {
                categoryName = value.name_ua;
            } else {
                categoryName = "2";
            }
        }
        return categoryName;
    }
}
