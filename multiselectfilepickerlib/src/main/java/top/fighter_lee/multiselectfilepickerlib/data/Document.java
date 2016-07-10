package top.fighter_lee.multiselectfilepickerlib.data;


import top.fighter_lee.multiselectfilepickerlib.utils.Utils;

/**
 * Created by droidNinja on 29/07/16.
 */
public class Document extends BaseFile {
    public boolean isDir = false;
    private String mimeType;
    private String size;
    private FileType fileType;

    public Document(int id, String title, String path) {
        super(id,title,path);
    }

    public Document() {
        super(0,null,null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }

        Document document = (Document) o;

        return id == document.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
//        return new File(this.path).getName();
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public boolean isThisType(String[] types)
    {
        return Utils.contains(types, this.path);
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
