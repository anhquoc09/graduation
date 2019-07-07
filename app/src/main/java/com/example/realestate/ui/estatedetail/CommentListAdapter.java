package com.example.realestate.ui.estatedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.CommentDetail;
import com.example.realestate.data.model.CommentResponseDetail;
import com.example.realestate.data.model.UserComment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by quocha2
 * On 05/07/2019
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentVH> {

    private List<CommentDetail> mCommentList = new ArrayList<>();

    private User mUser = UserManager.getCurrentUser();

    private OnCommentItemClickListener mListener;

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            holder.bindView(mCommentList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public void setData(List<CommentDetail> data) {
        mCommentList.clear();
        if (!data.isEmpty()) {
            mCommentList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void appendData(CommentResponseDetail data) {
        if (data != null) {
            UserComment userComment = new UserComment();
            userComment.setAvatar(mUser.getAvatar());
            userComment.setEmail(mUser.getEmail());
            userComment.setId(mUser.getId());
            userComment.setFullname(mUser.getFullname());

            CommentDetail commentDetail = new CommentDetail();
            commentDetail.setStar(data.getStar());
            commentDetail.setContent(data.getContent());
            commentDetail.setId(data.getId());
            commentDetail.setCreateTime(data.getCreateTime());
            commentDetail.setUpdateTime(data.getUpdateTime());
            commentDetail.setUser(userComment);
            mCommentList.add(commentDetail);
        }
        notifyItemChanged(mCommentList.size() - 1);
    }

    public void setCommentClickListener(OnCommentItemClickListener listener) {
        mListener = listener;
    }

    public void onEditCompleted(int position, boolean isSuccess, CommentResponseDetail detail) {
        if (position >= 0 && position < mCommentList.size()) {
            if (isSuccess && detail != null) {
                mCommentList.get(position).setUpdateTime(detail.getUpdateTime());
                mCommentList.get(position).setContent(detail.getContent());
                mCommentList.get(position).setStar(detail.getStar());
            }
            notifyItemChanged(position);
        }
    }

    public void onDeleteCompleted(int position, boolean isSuccess) {
        if (position >= 0 && position < mCommentList.size()) {
            if (isSuccess) {
                mCommentList.remove(position);
                notifyItemRemoved(position);
            } else {
                notifyItemChanged(position);
            }
        }
    }

    class CommentVH extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        CircleImageView mAvatar;

        @BindView(R.id.name)
        TextView mName;

        @BindView(R.id.time)
        TextView mTime;

        @BindView(R.id.comment)
        EditText mComment;

        @BindView(R.id.rating)
        RatingBar mRatingBar;

        @BindView(R.id.btn_menu)
        ImageView mBtnMenu;

        @BindView(R.id.btn_save)
        View mBtnSave;

        @BindView(R.id.btn_cancel)
        View mBtnCancel;

        @BindView(R.id.progress)
        View mProgess;

        private PopupMenu mPopupMenu;

        private CommentDetail mCommentDetail;

        public CommentVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPopupMenu = new PopupMenu(itemView.getContext(), mBtnMenu);
            mPopupMenu.inflate(R.menu.comment_item_menu_owner);
            mPopupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.item_menu_edit:
                        mComment.setEnabled(true);
                        mRatingBar.setIsIndicator(false);
                        mBtnSave.setVisibility(View.VISIBLE);
                        mBtnCancel.setVisibility(View.VISIBLE);
                        return true;

                    case R.id.item_menu_delete:
                        if (mListener != null) {
                            mProgess.setVisibility(View.VISIBLE);
                            mListener.onDeleteComment(getAdapterPosition(), mCommentDetail.getId());
                        }
                        return true;
                }
                return false;
            });
        }

        public void bindView(CommentDetail detail) {
            mCommentDetail = detail;

            Glide.with(itemView)
                    .load(mCommentDetail.getUser().getAvatar())
                    .placeholder(R.color.silver)
                    .error(R.drawable.avatar_default_small)
                    .into(mAvatar);

            mName.setText(mCommentDetail.getUser().getFullname());

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            Date date = new Date(mCommentDetail.getUpdateTime() * 1000);
            mTime.setText(String.format("%s ", dateFormat.format(date)));
            mComment.setText(mCommentDetail.getContent());
            mRatingBar.setRating(mCommentDetail.getStar());

            mBtnMenu.setVisibility(mUser.getId().equals(detail.getUser().getId()) ? View.VISIBLE : View.GONE);
            mProgess.setVisibility(View.GONE);
            mRatingBar.setIsIndicator(true);
        }

        @OnClick(R.id.btn_menu)
        public void onMenuClick() {
            mPopupMenu.show();
        }

        @OnClick(R.id.btn_save)
        public void onSaveClick() {
            if (mListener != null) {
                mBtnMenu.setVisibility(View.GONE);
                mProgess.setVisibility(View.VISIBLE);
                mCommentDetail.setContent(mComment.getText().toString());
                mCommentDetail.setStar((int) mRatingBar.getRating());
                mListener.onEditComment(getAdapterPosition(), mCommentDetail);
            }
            mRatingBar.setClickable(false);
            mComment.setEnabled(false);
            mBtnSave.setVisibility(View.GONE);
            mBtnCancel.setVisibility(View.GONE);
        }

        @OnClick(R.id.btn_cancel)
        public void onCancelClick() {
            mRatingBar.setClickable(false);
            mComment.setEnabled(false);
            mBtnSave.setVisibility(View.GONE);
            mBtnCancel.setVisibility(View.GONE);
            notifyItemChanged(getAdapterPosition());
        }
    }

    interface OnCommentItemClickListener {
        void onEditComment(int position, CommentDetail detail);

        void onDeleteComment(int position, String id);
    }
}
