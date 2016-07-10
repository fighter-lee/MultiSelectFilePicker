package top.fighter_lee.multiselectfilepickerlib.engine;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.fighter_lee.multiselectfilepickerlib.R;
import top.fighter_lee.multiselectfilepickerlib.data.Document;
import top.fighter_lee.multiselectfilepickerlib.data.FileType;
import top.fighter_lee.multiselectfilepickerlib.inter.FileResultCallback;

public class DocScannerTask extends AsyncTask<Void, Void, List<Document>> {

    private final FileResultCallback<Document> resultCallback;

    private final Context context;
    private List<FileType> fileTypes;
    private String path;
    private static final String TAG = "DocScannerTask";

    public DocScannerTask(Context context, FileResultCallback<Document> fileResultCallback, String path, @Nullable List<FileType> fileTypes) {
        Log.d(TAG, "DocScannerTask() path:"+path+","+fileTypes.size());
        this.context = context;
        this.resultCallback = fileResultCallback;
        this.path = path;
        this.fileTypes = fileTypes;
    }

    @Override
    protected List<Document> doInBackground(Void... voids) {
        ArrayList<Document> documents = new ArrayList<>();
        if (FilePickerBuilder.getInstance().isShowAll()) {
            ArrayList<Document> documents1 = listFiles(new File(path), null, null, null);
            documents.addAll(documents1);
        } else {
            if (fileTypes != null) {
                for (FileType fileType : fileTypes) {
                    ArrayList<Document> docs = listFiles(new File(path), null, fileType, fileType.extensions);
                    documents.addAll(docs);
                }
            }
        }
        Collections.sort(documents, new FileSorter());
        return documents;
    }

    @Override
    protected void onPostExecute(List<Document> documents) {
        super.onPostExecute(documents);
        if (resultCallback != null) {
            resultCallback.onResultCallback(documents);
        }
    }

    private FileType getFileType(ArrayList<FileType> types, String path) {
        for (int index = 0; index < types.size(); index++) {
            for (String string : types.get(index).extensions) {
                if (path.endsWith(string)) {
                    {
                        return types.get(index);
                    }
                }
            }
        }
        return null;
    }

    ArrayList<Document> listFiles(@NonNull File parentFolder, @Nullable String mimeType, @Nullable FileType fileType, @Nullable String[] extensions) {
        ArrayList<Document> documents = new ArrayList<>();
        File[] contents = parentFolder.listFiles();
        if (contents != null) {
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            for (File fi : contents) {
                if (fi.isDirectory()) {
                    addFiles(documents, fi, true, fileType);
                } else {
                    if (extensions != null) {
                        boolean found = false;
                        for (String ext : extensions) {
                            if (fi.getName().toLowerCase().endsWith(ext.toLowerCase())) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            addFiles(documents, fi, false, fileType);
                        }
                    } else if (mimeType != null) {
                        if (fileIsMimeType(fi, mimeType, mimeTypeMap)) {
                            addFiles(documents, fi, false, fileType);
                        }
                    } else {
                        addFiles(documents, fi, false, fileType);
                    }
                }
            }
            return documents;
        }
        return null;
    }

    private void addFiles(ArrayList<Document> documents, File file, boolean isDir, @Nullable FileType fileType) {
        Document document = new Document(0, file.getName(), file.getAbsolutePath());
        document.isDir = isDir;
        if (!isDir) {
            String fileSize = String.valueOf(file.length());
            document.setSize(fileSize);
        }
        if (fileType == null) {
            if (file.isDirectory()){
                document.setFileType(new FileType("dir",new String[]{}, R.mipmap.ic_dir));
            }else{
                document.setFileType(new FileType("dir",new String[]{}, R.mipmap.ic_file));
            }
        }else{
            document.setFileType(fileType);
        }
        documents.add(document);
    }

    /**
     * 过滤文件类型
     *
     * @param file
     * @param mimeType
     * @param mimeTypeMap
     * @return
     */
    boolean fileIsMimeType(File file, String mimeType, MimeTypeMap mimeTypeMap) {
        if (mimeType == null || "*/*".equals(mimeType)) {
            return true;
        } else {
            // get the file mime type
            String filename = file.toURI().toString();
            int dotPos = filename.lastIndexOf('.');
            if (dotPos == -1) {
                return false;
            }
            String fileExtension = filename.substring(dotPos + 1);
            if (fileExtension.endsWith("json")) {
                return mimeType.startsWith("application/json");
            }
            String fileType = mimeTypeMap.getMimeTypeFromExtension(fileExtension);
            if (fileType == null) {
                return false;
            }
            // check the 'type/subtype' pattern
            if (fileType.equals(mimeType)) {
                return true;
            }
            // check the 'type/*' pattern
            int mimeTypeDelimiter = mimeType.lastIndexOf('/');
            if (mimeTypeDelimiter == -1) {
                return false;
            }
            String mimeTypeMainType = mimeType.substring(0, mimeTypeDelimiter);
            String mimeTypeSubtype = mimeType.substring(mimeTypeDelimiter + 1);
            if (!"*".equals(mimeTypeSubtype)) {
                return false;
            }
            int fileTypeDelimiter = fileType.lastIndexOf('/');
            if (fileTypeDelimiter == -1) {
                return false;
            }
            String fileTypeMainType = fileType.substring(0, fileTypeDelimiter);
            if (fileTypeMainType.equals(mimeTypeMainType)) {
                return true;
            }
        }
        return false;
    }

    private static class FileSorter implements Comparator<Document> {

        @Override
        public int compare(Document o1, Document o2) {
            if (o1.isDir && !o2.isDir) {
                return -1;
            } else if (!o1.isDir && o2.isDir) {
                return 1;
            } else {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        }
    }
}
