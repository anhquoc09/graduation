package com.example.realestate.ui.estatedetail;

import com.example.realestate.EstateApplication;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.CommentDetail;
import com.example.realestate.data.remote.response.CommentResponse;
import com.example.realestate.data.remote.response.ListCommentResponse;
import com.example.realestate.data.remote.response.SaveEstateResponse;
import com.example.realestate.data.remote.response.UnSaveEstateResponse;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.data.remote.rest.SchedulerProvider;
import com.example.realestate.data.remote.rest.SimpleResponse;
import com.example.realestate.ui.BasePresenter;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.example.realestate.EstateApplication.BEARER_TOKEN;

public class EstateDetailPresenter extends BasePresenter<EstateDetailView> {

    private EstateService mService;

    private User mUser;

    private Subscription mSaveSub;

    private Subscription mGetCommentSub;

    private CompositeSubscription mSubscriptions;

    public EstateDetailPresenter() {
        mService = ServiceProvider.getEstateService();
        mUser = UserManager.getCurrentUser();
        mSubscriptions = new CompositeSubscription();
    }

    public void savePost(String fullName, String projectId, long createTime) {
        mSaveSub = mService.savePost(BEARER_TOKEN + mUser.getAccessToken(), fullName, projectId, createTime)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SaveEstateSubscriber());

        mSubscriptions.add(mSaveSub);
    }

    public void unSavePost(String projectId) {
        mSaveSub = mService.unSavePost(BEARER_TOKEN + mUser.getAccessToken(), projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new UnSaveEstateSubscriber(projectId));

        mSubscriptions.add(mSaveSub);
    }

    private void showNoNetwork() {
        if (isViewAttached()) {
            mView.showNoNetworkConnection();
        }
    }

    public void getAllComment(String projectId) {
        if (mGetCommentSub != null && mGetCommentSub.isUnsubscribed()) {
            mGetCommentSub.unsubscribe();
        }

        mGetCommentSub = mService.getAllComment(projectId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListCommentSubscriber());
        mSubscriptions.add(mGetCommentSub);
    }

    public void addComment(String projectId, String comment, int rating) {
        mSubscriptions.add(mService.addComment(BEARER_TOKEN + mUser.getAccessToken(),
                mUser.getId(),
                projectId,
                Calendar.getInstance().getTimeInMillis() / 1000,
                Calendar.getInstance().getTimeInMillis() / 1000,
                comment,
                rating)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new AddCommentSubscriber()));
    }

    public void editComment(int position, CommentDetail detail) {
        mSubscriptions.add(mService.editComment(BEARER_TOKEN + mUser.getAccessToken(),
                detail.getId(),
                mUser.getId(),
                detail.getProjectId(),
                detail.getCreateTime(),
                Calendar.getInstance().getTimeInMillis() / 1000,
                detail.getContent(),
                detail.getStar())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new EditCommentSubscriber(position)));
    }

    public void deleteComment(int position, String commentId) {
        mSubscriptions.add(mService.deleteComment(BEARER_TOKEN + mUser.getAccessToken(),
                commentId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new DeleteCommentSubscriber(position)));
    }

    @Override
    public void detachView() {
        mSubscriptions.clear();
        super.detachView();
    }

    class SaveEstateSubscriber extends Subscriber<SaveEstateResponse> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(SaveEstateResponse saveEstateResponse) {
            EstateApplication.addSavedPostId(saveEstateResponse.getSaveResult().getPost());
            if (isViewAttached()) {
                mView.saveEstateSuccess();
            }
        }
    }

    class UnSaveEstateSubscriber extends Subscriber<UnSaveEstateResponse> {

        private String mPostId;

        public UnSaveEstateSubscriber(String postId) {
            mPostId = postId;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(UnSaveEstateResponse saveEstateResponse) {
            EstateApplication.removeSavedPostId(mPostId);
            if (isViewAttached()) {
                mView.unSaveEstateSuccess();
            }
        }
    }

    class ListCommentSubscriber extends Subscriber<ListCommentResponse> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
        }

        @Override
        public void onNext(ListCommentResponse listCommentResponse) {
            if (listCommentResponse != null) {
                if (isViewAttached()) {
                    mView.setComment(listCommentResponse.getCommentList());
                }
            }
        }
    }

    class AddCommentSubscriber extends Subscriber<CommentResponse> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
            if (isViewAttached()) {
                mView.addCommentError();
            }
        }

        @Override
        public void onNext(CommentResponse commentResponse) {
            if (commentResponse != null) {
                if (isViewAttached()) {
                    mView.addCommentSuccess(commentResponse.getComment());
                }
            }
        }
    }

    class EditCommentSubscriber extends Subscriber<CommentResponse> {

        private int mPosition;

        protected EditCommentSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
            if (isViewAttached()) {
                mView.editCommentError(mPosition);
            }
        }

        @Override
        public void onNext(CommentResponse commentResponse) {
            if (commentResponse != null) {
                if (isViewAttached()) {
                    mView.editCommentSuccess(mPosition, commentResponse.getComment());
                }
            }
        }
    }

    class DeleteCommentSubscriber extends Subscriber<SimpleResponse> {

        private int mPosition;

        public DeleteCommentSubscriber(int position) {
            mPosition = position;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException) {
                showNoNetwork();
            }
            if (isViewAttached()) {
                mView.deleteCommentError(mPosition);
            }
        }

        @Override
        public void onNext(SimpleResponse response) {
            if (response != null) {
                if (isViewAttached()) {
                    mView.deleteCommentSuccess(mPosition);
                }
            }
        }
    }

}
