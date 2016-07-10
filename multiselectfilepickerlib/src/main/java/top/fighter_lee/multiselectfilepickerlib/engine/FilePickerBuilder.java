package top.fighter_lee.multiselectfilepickerlib.engine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import top.fighter_lee.multiselectfilepickerlib.activity.FilePickerActivity;
import top.fighter_lee.multiselectfilepickerlib.data.FilePickerConst;
import top.fighter_lee.multiselectfilepickerlib.data.FileType;

import static top.fighter_lee.multiselectfilepickerlib.data.FilePickerConst.EXTRA_PICKER_TYPE;


public class FilePickerBuilder {

    private final Bundle mPickerOptionsBundle;

    public FilePickerBuilder() {
        mPickerOptionsBundle = new Bundle();
    }

    public static FilePickerBuilder getInstance() {
        return new FilePickerBuilder();
    }

    public FilePickerBuilder setMaxCount(int maxCount) {
        PickerManager.getInstance().setMaxCount(maxCount);
        return this;
    }

    public FilePickerBuilder setTitle(String title) {
        PickerManager.getInstance().setTitle(title);
        return this;
    }

    public FilePickerBuilder setActivityTheme(int theme) {
        PickerManager.getInstance().setTheme(theme);
        return this;
    }

    /**
     * 设置已选路径
     * @param selectedFiles
     * @return
     */
    public FilePickerBuilder setSelectedFiles(ArrayList<String> selectedFiles) {
        mPickerOptionsBundle.putStringArrayList(FilePickerConst.KEY_SELECTED_FILES, selectedFiles);
        return this;
    }

    public FilePickerBuilder showFolderView(boolean status) {
        PickerManager.getInstance().setShowFolderView(status);
        return this;
    }

    public FilePickerBuilder enableDocSupport(boolean status) {
        PickerManager.getInstance().setDocSupport(status);
        return this;
    }

    public FilePickerBuilder addFileSupport(String title, String[] extensions, @DrawableRes int drawable) {
        PickerManager.getInstance().addFileType(new FileType(title, extensions, drawable));
        return this;
    }

    /**
     * 只显示添加的文件
     * @param title
     * @param extensions
     * @return
     */
    public FilePickerBuilder addFileSupport(String title, String[] extensions) {
        PickerManager.getInstance().addFileType(new FileType(title, extensions, 0));
        return this;
    }

    public FilePickerBuilder setShowAllFile() {
        PickerManager.getInstance().addAllFileType();
        return this;
    }

    public boolean isShowAll() {
        return PickerManager.getInstance().isShowAllFile();
    }

    public void pickFile(Activity context) {
        mPickerOptionsBundle.putInt(EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER);
        start(context, FilePickerConst.DOC_PICKER);
    }

    public void pickFile(Fragment context) {
        mPickerOptionsBundle.putInt(EXTRA_PICKER_TYPE, FilePickerConst.DOC_PICKER);
        start(context, FilePickerConst.DOC_PICKER);
    }

    private void start(Activity context, int pickerType) {
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        context.startActivityForResult(intent, FilePickerConst.REQUEST_CODE_DOC);
    }

    private void start(Fragment fragment, int pickerType) {

        Intent intent = new Intent(fragment.getActivity(), FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        fragment.startActivityForResult(intent, FilePickerConst.REQUEST_CODE_DOC);
    }

}
