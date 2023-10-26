import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    private String treeSHA;
    private String prevSHA;
    private String nextSHA;

    private String author;
    private String date;
    private String summary;

    public Commit (String treeSHA, String author, String summary, String prevSHA){
        this.treeSHA = createTree("objects", "");;
        this.prevSHA = prevSHA;
        this.nextSHA = null;
        this.author = author;
        this.date = getCurrentDate();
        this.summary = summary;
    }

    public Commit(String treeSHA, String author, String summary){
        this(treeSHA, author, summary, null);
    }

    public String getTreeSHA(){
        return treeSHA;
    }

    public void writeToFile(String folderPath){
        String file = generateFile();
        String sha1 = generateSHA(file);

        File objectsFolder = new File(folderPath);
        if (!objectsFolder.exists()){
            objectsFolder.mkdirs();
        }

        String path = folderPath + File.separator + sha1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))){
            writer.write(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String generateFile(){
        StringBuilder content = new StringBuilder();
        content.append(treeSHA).append("\n");
        content.append(prevSHA != null ? prevSHA : " ").append("\n");
        content.append(nextSHA != null ? nextSHA : " ").append("\n");
        content.append(author).append("\n");
        content.append(date).append("\n");
        content.append(summary).append("\n");
        return content.toString();
    }

    public String generateSHA(String file){
        //String file = summary + "\n" + date + "\n" + author + "\n" + treeSHA + "\n";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] content = md.digest(file.getBytes());
            StringBuilder sha1 = new StringBuilder();

            for (byte n : content){
                sha1.append(String.format("%02x", n));
            }
            return sha1.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getDate(){
        return date;
    }

    public void createTree(String folderPath, String content){
        Tree tree = new Tree(content);
        return tree.saveToObjectsFolder(folderPath);
    }

    private String getCurrentDate(){
        SimpleDateFormat date = new SimpleDateFormat("MMM d, yyyy");
        return date.format(new Date());
    }
}
