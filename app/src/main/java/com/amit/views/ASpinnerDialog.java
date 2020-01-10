package com.amit.views;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.R;

/**
 * Created by AMIT JANGID on 25/02/2019.
**/
public class ASpinnerDialog extends DialogFragment
{
    private View mRoot;
    private CardView mCard;
    private ASpinner mView;
    private ImageView mReset;
    private TextView mTvTitle;
    private EditText mEdtSearch;
    private RecyclerView mRecyclerView;

    private ASpinner.OnItemClickListener mListener;
    private ASpinnerDialogAdapter mSpinnerDialogAdapter;

    private int mSelected;
    private String mTitle;
    private String[] mData;

    @ColorInt
    private int mTitleColor;

    public ASpinnerDialog()
    {

    }

    void setListener(ASpinner.OnItemClickListener listener, ASpinner view)
    {
        this.mView = view;
        this.mListener = listener;
    }

    static ASpinnerDialog newInstance(String title, String[] data, int selected, int titleColor)
    {
        ASpinnerDialog instance = new ASpinnerDialog();

        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putStringArray("data", data);
        arguments.putInt("selected", selected);
        arguments.putInt("titleColor", titleColor);

        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SpinnerDialogStyle);

        if (getArguments() != null && getArguments().getStringArray("data") != null &&
                getArguments().getString("title") != null)
        {
            mTitle = getArguments().getString("title");
            mData = getArguments().getStringArray("data");
            mSelected = getArguments().getInt("selected");
            mTitleColor = getArguments().getInt("titleColor");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_spinner_dialog, container, false);

        mRoot = view.findViewById(R.id.root);
        mCard = view.findViewById(R.id.card);
        mReset = view.findViewById(R.id.reset);
        mTvTitle = view.findViewById(R.id.title);
        mEdtSearch = view.findViewById(R.id.search);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (mData != null)
        {
            mTvTitle.setText(mTitle);
            mTvTitle.setTextColor(mTitleColor);

            mSpinnerDialogAdapter = new ASpinnerDialogAdapter((Pair<Integer, String> item, int position) ->
            {
                ASpinnerDialog.this.mView.setText(item.second);
                ASpinnerDialog.this.mView.setSelected(item.first);

                if (mListener != null)
                {
                    mListener.onItemClick(item.second, position);
                }

                dismiss();
            });

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mSpinnerDialogAdapter.update(mData, mSelected);
            mRecyclerView.setAdapter(mSpinnerDialogAdapter);

            mEdtSearch.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {
                    mSpinnerDialogAdapter.update(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable)
                {
                    if (!editable.toString().isEmpty())
                    {
                        mReset.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mReset.setVisibility(View.GONE);
                    }
                }
            });

            mReset.setOnClickListener(view1 ->
            {
                mSpinnerDialogAdapter.reset();
                mEdtSearch.setText("");
            });

            mRoot.setOnClickListener(view12 -> dismiss());

            mCard.setOnClickListener(view13 ->
            {
                //do nothing
            });
        }
        else
        {
            dismiss();
        }
    }
}
