package com.amit.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amit.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by AMIT JANGID on 25/02/2019.
**/
public class ASpinnerDialogAdapter extends RecyclerView.Adapter<ASpinnerDialogAdapter.SpinnerItemViewHolder>
{
    private ArrayList<Pair<Integer, String>> mItems = new ArrayList<>();
    private ArrayList<Pair<Integer, String>> mAllItems = new ArrayList<>();
    private ArrayList<Pair<Integer, String>> mTempItems = new ArrayList<>();

    private SpinnerDialogListener mDialogListener;

    private int mSelected;

    /*@ColorInt
    private int mDividerColor, mItemTextColor;*/

    ASpinnerDialogAdapter(SpinnerDialogListener dialogListener)
    {
        // this.mDividerColor = dividerColor;
        // this.mItemTextColor = itemTextColor;
        this.mDialogListener = dialogListener;
    }

    public void update(String[] items, int selected)
    {
        this.mItems.clear();
        this.mAllItems.clear();
        this.mSelected = selected;

        for (int i = 0; i < items.length; i++)
        {
            this.mItems.add(new Pair<>(i, items[i]));
            this.mAllItems.add(new Pair<>(i, items[i]));
        }
    }

    public void update(String query)
    {
        // Pair<Integer, String> selectedItem;

        for (Pair<Integer, String> item : mAllItems)
        {
            if (item.second.toLowerCase().contains(query.toLowerCase()))
            {
                mTempItems.add(item);
            }
        }

        mItems.clear();
        mItems.addAll(mTempItems);

        mTempItems.clear();
        notifyDataSetChanged();
    }

    void reset()
    {
        mItems.clear();
        mItems.addAll(mAllItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SpinnerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_dialog_list, parent, false);
        return new SpinnerItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerItemViewHolder holder, int position)
    {
        Pair<Integer, String> item = mItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    class SpinnerItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView label;
        private ImageView ivSelected;

        SpinnerItemViewHolder(@NonNull View itemView)
        {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            ivSelected = itemView.findViewById(R.id.selected);
        }

        void bind(final Pair<Integer, String> item)
        {
            if (item.first == mSelected)
            {
                ivSelected.setVisibility(VISIBLE);
            }
            else
            {
                ivSelected.setVisibility(GONE);
            }

            label.setText(item.second);
            itemView.setOnClickListener(v -> mDialogListener.onClick(item, item.first));
        }
    }

    interface SpinnerDialogListener
    {
        void onClick(Pair<Integer, String> item, int position);
    }
}
