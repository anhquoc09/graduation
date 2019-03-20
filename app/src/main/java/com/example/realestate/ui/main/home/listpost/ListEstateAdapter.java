package com.example.realestate.ui.main.home.listpost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class ListEstateAdapter extends RecyclerView.Adapter<ListEstateAdapter.EstateItemVH> {

    private final List<EstateDetail> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    @NonNull
    @Override
    public EstateItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_estate_item_layout, parent, false);
        return new EstateItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstateItemVH holder, int position) {
        EstateDetail item = mList.get(position);
        if (item != null) {
            holder.bindView(item);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<EstateDetail> list) {
        mList.clear();
        if (!CollectionUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    /**
     * {@link EstateDetail}
     */
    public class EstateItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {


        public EstateItemVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemSelected(getItem());
        }

        private EstateDetail getItem() {
            if (mList.get(getAdapterPosition()) != null) {
                return mList.get(getAdapterPosition());
            }
            return null;
        }

        public void bindView(EstateDetail item) {
        }
    }

    /**
     * {@link OnItemClickListener}
     */
    public interface OnItemClickListener {
        void onItemSelected(EstateDetail item);
    }
}
