package top.fighter_lee.multiselectfilepickerlib.utils;

import android.support.v4.app.FragmentActivity;

import java.util.List;

import top.fighter_lee.multiselectfilepickerlib.data.Document;
import top.fighter_lee.multiselectfilepickerlib.data.FileType;
import top.fighter_lee.multiselectfilepickerlib.engine.DocScannerTask;
import top.fighter_lee.multiselectfilepickerlib.inter.FileResultCallback;


public class MediaStoreHelper {

    public static void getDocs(FragmentActivity activity, String path, List<FileType> fileTypes, FileResultCallback<Document> fileResultCallback) {

        new DocScannerTask(activity, fileResultCallback, path, fileTypes).execute();

    }
}