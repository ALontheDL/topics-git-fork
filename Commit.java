import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

public class Commit {
    private String treeSHA;
    private String author;
    private String date;
    private String summary;

    public Commit (String treeSHA, String author, String summary, String prevSHA){
        this.treeSHA = treeSHA;
        this.author = author;
        this.date = ;
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(file);
    }

    private String generateFile(){
        StringBuilder content = new StringBuilder();

        return content.toString;
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

    public String createTree(){
        
    }

    private String getCurrentDate(){
        
    }
}
