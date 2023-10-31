import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    private String treeSHA;
    private String prevSHA;
    private String nextSHA;

    private String author;
    private String date;
    private String summary;

    public Commit(String treeSHA, String author, String summary, String prevSHA, String nextSHA) {
        this.treeSHA = treeSHA;
        this.prevSHA = prevSHA;
        this.nextSHA = null;
        this.author = author;
        this.date = getCurrentDate();
        this.summary = summary;

        if (prevSHA != null) {
            Tree prevTree = getCommitTree(prevSHA);
            String prevEntry = "tree : " + prevTree.getSHA1();
            prevTree.add(prevEntry);
            prevTree.generateBlob();
            omitEditedAndDeletedFiles(prevTree);
        }
    }

    public String createTree(String folderPath, String treeContent) {
        String sha1 = generateSHA();
        String path = folderPath + File.separator + sha1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(treeContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sha1;
    }

    public String getTreeSHA() {
        return treeSHA;
    }

    public void writeToFile(String folderPath) {
        String sha1 = generateSHA();
        String path = folderPath + File.separator + sha1;
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(generateFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (prevSHA != null) {
            Commit prev = getCommit(prevSHA);
            prev.setNextSHA(sha1);
            prev.writeToFile(folderPath);
        }
    }

    public String getPrevSHA() {
        return prevSHA;
    }

    public String getNextSHA() {
        return nextSHA;
    }

    public void setNextSHA(String next) {
        this.nextSHA = next;
    }

    public static String getCommitTreeSHA(String commitSHA) {
        try {
            Path commit = Paths.get("objects", commitSHA);
            String content = new String(Files.readAllBytes(commit));
            String[] lines = content.split("\n");
            return lines[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Commit getCommit(String commitSHA) {
        try {
            Path commit = Paths.get("objects", commitSHA);
            String content = new String(Files.readAllBytes(commit));
            String[] lines = content.split("\n");

            String treeSHA = lines[0];
            String prevSHA = lines[1];
            String nextSHA = lines[2];
            String author = lines[3];
            String date = lines[4];
            String summary = lines[5];
            return new Commit(treeSHA, author, summary, prevSHA, nextSHA);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateFile() {
        StringBuilder content = new StringBuilder();
        content.append(treeSHA).append("\n");
        content.append(prevSHA != null ? prevSHA : " ").append("\n");
        content.append(nextSHA != null ? nextSHA : " ").append("\n");
        content.append(author).append("\n");
        content.append(date).append("\n");
        content.append(summary).append("\n");
        return content.toString();
    }

    public String generateSHA() {
        String file = generateFile();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] content = md.digest(file.getBytes());
            StringBuilder sha1 = new StringBuilder();

            for (byte n : content) {
                sha1.append(String.format("%02x", n));
            }
            return sha1.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDate() {
        return date;
    }

    private String getCurrentDate() {
        SimpleDateFormat date = new SimpleDateFormat("MMM d, yyyy");
        return date.format(new Date());
    }

    public static Tree getCommitTree(String commitSHA) {
        String treeSHA = getCommitTreeSHA(commitSHA);
        return Tree.getTree(treeSHA, "objects");
    }

    private void omitEditedAndDeletedFiles(Tree tree) {
        for (String editedFile : tree.getEditedFiles()) {
            tree.remove("blob : " + editedFile);
        }
        for (String deletedFile : tree.getEditedFiles()) {
            tree.remove("blob : " + deletedFile);
        }
    }
}
