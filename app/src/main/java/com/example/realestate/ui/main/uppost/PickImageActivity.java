//package com.example.realestate.ui.main.uppost;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//
//import com.example.realestate.BuildConfig;
//import com.example.realestate.ui.BaseActivity;
//import com.example.realestate.utils.AndroidUtilities;
//import com.vng.live.R;
//import com.vng.live.utils.DialogUtils;
//import com.vng.live.utils.PermissionUtils;
//import com.vng.live.utils.StringUtils;
//import com.vng.live.utils.permission.PermissionHelper;
//import com.vng.live.utils.permission.PermissionInfo;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.FileProvider;
//
///**
// * Copyright (C) 2017, VNG Corporation.
// * @author namnt4
// * @since 27/02/2018
// */
//public abstract class PickImageActivity extends BaseActivity {
//
//    public static final int CAPTURE_CODE = 200;
//    public static final int PICK_IMAGE_CODE = 201;
//    public static final int CROPPED_IMAGE_CODE = 202;
//    public static final String ARGS_IMAGE_PATH = "key_path";
//
//    private File mImageFile;
//
//    private boolean mIsCamera = false;
//
//    private PermissionHelper mCameraPermissionHelper;
//
//    private PermissionHelper mGalleryPermissionHelper;
//
//    private final Runnable mGalleryPermissionGrantedRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mIsCamera = false;
//            Intent i = new Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            i.setType("image/*");
//            if (i.resolveActivity(PickImageActivity.this.getPackageManager()) != null) {
//                startActivityForResult(i, PICK_IMAGE_CODE);
//            }
//        }
//    };
//
//    private final Runnable mGalleryPermissionDeniedRunnable = () -> AndroidUtilities.showToast(getString(R.string.storage_avatar_permission_deny_toast));
//
//    private final Runnable mCameraPermissionGrantedRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mIsCamera = true;
//            mImageFile = createImageFile();
//            Context context = getApplicationContext();
//            Intent capturedIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri photoUri = FileProvider.getUriForFile(context,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    mImageFile);
//            capturedIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//            if (capturedIntent.resolveActivity(context.getPackageManager()) != null) {
//                startActivityForResult(capturedIntent, CAPTURE_CODE);
//            }
//        }
//    };
//
//    private final Runnable mCameraPermissionDeniedRunnable = () -> AndroidUtilities.showToast(getString(R.string.camera_avatar_permission_deny_toast));
//
//    protected abstract void cropImageSuccess(String imagePath);
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        initPermissionHelper();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mCameraPermissionHelper != null) {
//            mCameraPermissionHelper.cleanUp();
//        }
//
//        if (mGalleryPermissionHelper != null) {
//            mGalleryPermissionHelper.cleanUp();
//        }
//
//        super.onDestroy();
//    }
//
//    private void initPermissionHelper() {
//        if (mGalleryPermissionHelper == null) {
//            ArrayList<PermissionInfo> permissionInfoList = buildGalleryPermissionInfoList();
//
//            PermissionHelper.Builder builder = new PermissionHelper.Builder();
//            builder.setActivity(this)
//                    .setRationaleDialogTitle(R.string.storage_rationale_dialog_title)
//                    .setRequestPermissionCode(PermissionHelper.EXTERNAL_STORAGE_REQUEST_CODE)
//                    .setPermissionInfoList(permissionInfoList)
//                    .setDeniedRunnable(mGalleryPermissionDeniedRunnable)
//                    .setGrantedRunnable(mGalleryPermissionGrantedRunnable);
//            mGalleryPermissionHelper = builder.build();
//        }
//
//        if (mCameraPermissionHelper == null) {
//            ArrayList<PermissionInfo> permissionInfoList = buildCameraPermissionInfoList();
//
//            PermissionHelper.Builder builder = new PermissionHelper.Builder();
//            builder.setActivity(this)
//                    .setRationaleDialogTitle(R.string.general_permission_dialog_title)
//                    .setRequestPermissionCode(PermissionHelper.CAMERA_REQUEST_CODE)
//                    .setPermissionInfoList(permissionInfoList)
//                    .setDeniedRunnable(mCameraPermissionDeniedRunnable)
//                    .setGrantedRunnable(mCameraPermissionGrantedRunnable);
//            mCameraPermissionHelper = builder.build();
//        }
//    }
//
//    private ArrayList<PermissionInfo> buildGalleryPermissionInfoList() {
//        ArrayList<PermissionInfo> ret = new ArrayList<>();
//
//        String[] nameArray = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//
//        PermissionInfo storagePermission = new PermissionInfo.Builder()
//                .setNameArray(nameArray)
//                .setRationaleDialogTitle(R.string.storage_rationale_dialog_title)
//                .setRationaleTitle(R.string.storage_rationale_title)
//                .setRationaleMessage(R.string.storage_avatar_rationale_dialog_message)
//                .build();
//
//        ret.add(storagePermission);
//
//        return ret;
//    }
//
//    private ArrayList<PermissionInfo> buildCameraPermissionInfoList() {
//        ArrayList<PermissionInfo> ret = new ArrayList<>();
//
//        PermissionInfo cameraPermission = new PermissionInfo.Builder()
//                .setName(Manifest.permission.CAMERA)
//                .setRationaleDialogTitle(R.string.camera_rationale_dialog_title)
//                .setRationaleTitle(R.string.camera_rationale_title)
//                .setRationaleMessage(R.string.camera_avatar_rationale_dialog_message)
//                .build();
//
//        ret.add(cameraPermission);
//
//        return ret;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case CAPTURE_CODE:
//                    startActivityForResult(ImageCropperActivity.intentFor(this, getImageResultUri(data)), CROPPED_IMAGE_CODE);
//                    break;
//
//                case PICK_IMAGE_CODE:
//                    startActivityForResult(ImageCropperActivity.intentFor(this, getImageResultUri(data)), CROPPED_IMAGE_CODE);
//                    break;
//
//                case CROPPED_IMAGE_CODE:
//                    String path = data.getStringExtra(ARGS_IMAGE_PATH);
//                    cropImageSuccess(path);
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    }
//
//    private File createImageFile() {
//        String fileName = StringUtils.format("captured_%d.jpg", System.currentTimeMillis());
//        return new File(getExternalCacheDir(), fileName);
//    }
//
//    private Uri getOutputImageFileUri() {
//        if (mImageFile == null) {
//            mImageFile = createImageFile();
//        }
//        return Uri.fromFile(mImageFile);
//    }
//
//    private Uri getImageResultUri(Intent data) {
//        if (data == null) {
//            return getOutputImageFileUri();
//        }
//        return mIsCamera ? getOutputImageFileUri() : data.getData();
//    }
//
//    protected void checkStoragePermission() {
//        if (mGalleryPermissionHelper.hasPermissions()) {
//            mGalleryPermissionGrantedRunnable.run();
//        } else {
//            mGalleryPermissionHelper.requestPermissions();
//        }
//    }
//
//    protected void checkCapturePermission() {
//        if (mCameraPermissionHelper.hasPermissions()) {
//            mCameraPermissionGrantedRunnable.run();
//        } else {
//            mCameraPermissionHelper.requestPermissions();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        boolean isGalleryPermission = requestCode == PermissionUtils.EXTERNAL_STORAGE_REQUEST_CODE;
//        boolean isCameraPermission = requestCode == PermissionUtils.CAMERA_REQUEST_CODE;
//
//        if (isGalleryPermission) {
//            mGalleryPermissionHelper.handlePermissionsResult(isGalleryPermission, permissions, grantResults);
//        } else if (isCameraPermission) {
//            mCameraPermissionHelper.handlePermissionsResult(isCameraPermission, permissions, grantResults);
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    protected void chooseTypePickImage() {
//        PickerImageDialog dialog = PickerImageDialog.newInstance();
//        dialog.setOnClickListener(new PickerImageDialog.OnClickListener() {
//            @Override
//            public void onClickCapture() {
//                checkCapturePermission();
//            }
//
//            @Override
//            public void onClickGallery() {
//                checkStoragePermission();
//            }
//        });
////        dialog.show(getSupportFragmentManager(), "pick");
//        DialogUtils.showDialogFragment(this, dialog, "pick");
//    }
//}
