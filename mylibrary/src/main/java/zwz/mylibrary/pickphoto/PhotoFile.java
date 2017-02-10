package zwz.mylibrary.pickphoto;

/**
 * Created by 朱伟志 on 2017/2/10 0010 16:47.
 */
public class PhotoFile {
    public String path;
    public String parentDirName;

    public PhotoFile(String path, String parentDirName) {
        this.path = path;
        this.parentDirName = parentDirName;
    }
}
