package top.fighter_lee.multiselectfilepickerlib.engine;


import java.util.ArrayList;

import top.fighter_lee.multiselectfilepickerlib.R;
import top.fighter_lee.multiselectfilepickerlib.data.BaseFile;
import top.fighter_lee.multiselectfilepickerlib.data.FilePickerConst;
import top.fighter_lee.multiselectfilepickerlib.data.FileType;


public class PickerManager {
    private static PickerManager ourInstance = new PickerManager();
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private int currentCount;
    private String title;

    public static PickerManager getInstance() {
        return ourInstance;
    }

    private ArrayList<String> mediaFiles;
    private ArrayList<String> docFiles;

    private ArrayList<FileType> fileTypes;

    private int theme = R.style.LibAppTheme;

    private boolean docSupport = true;

    private boolean showFolderView = true;

    private boolean showAllFile = true;

    private PickerManager() {
        mediaFiles = new ArrayList<>();
        docFiles = new ArrayList<>();
        fileTypes = new ArrayList<>();
    }

    /**
     * 设置可选最多数量
     * @param count
     */
    public void setMaxCount(int count) {
        clearSelections();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void add(String path, int type) {
        if (path != null && shouldAdd()) {
            if (!mediaFiles.contains(path) && type == FilePickerConst.FILE_TYPE_MEDIA) {
                mediaFiles.add(path);
            } else if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
                docFiles.add(path);
            } else {
                return;
            }

            currentCount++;
        }
    }

    public void add(ArrayList<String> paths, int type) {
        for (int index = 0; index < paths.size(); index++) {
            add(paths.get(index), type);
        }
    }

    public void remove(String path, int type) {
        if ((type == FilePickerConst.FILE_TYPE_MEDIA) && mediaFiles.contains(path)) {
            mediaFiles.remove(path);
            currentCount--;

        } else if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
            docFiles.remove(path);

            currentCount--;
        }
    }

    public boolean shouldAdd() {
        return currentCount < maxCount;
    }

    public ArrayList<String> getSelectedPhotos() {
        return mediaFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void clearSelections() {
        docFiles.clear();
        mediaFiles.clear();
        fileTypes.clear();
        currentCount = 0;
        maxCount = 0;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

    public void addFileType(FileType fileType)
    {
        fileTypes.add(fileType);
    }

    public void addAllFileType() {
        fileTypes.clear();
        showAllFile = true;
    }

    public boolean isShowAllFile() {
        return showAllFile;
    }

    public void addDocTypes()
    {
        String[] pdfs = {"pdf"};
        fileTypes.add(new FileType(FilePickerConst.PDF,pdfs,R.mipmap.ic_pdf));

        String[] docs = {"doc","docx", "dot","dotx"};
        fileTypes.add(new FileType(FilePickerConst.DOC,docs,R.mipmap.ic_word));

        String[] ppts = {"ppt","pptx"};
        fileTypes.add(new FileType(FilePickerConst.PPT,ppts,R.mipmap.ic_ppt));

        String[] xlss = {"xls","xlsx"};
        fileTypes.add(new FileType(FilePickerConst.XLS,xlss,R.mipmap.ic_excel));

        String[] txts = {"txt"};
        fileTypes.add(new FileType(FilePickerConst.TXT,txts,R.mipmap.ic_txt));
    }

    public ArrayList<FileType> getFileTypes()
    {
        return fileTypes;
    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }


}
