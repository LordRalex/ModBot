package us.lordralex.modbot;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class DownloadFile {

    public final long ID;
    public final int thread_id;
    public final String fileName;

    public DownloadFile(String thread, String file) {
        ID = System.currentTimeMillis();
        thread_id = Integer.parseInt(thread);
        fileName = file;
    }    
}
