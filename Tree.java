import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tree {
    public List<String> entries;
    private List<String> deletedFiles;
    private List<String> editedFiles;

    public Tree() {
        this.entries = new ArrayList<>();
        this.deletedFiles = new ArrayList<>();
        this.editedFiles = new ArrayList<>();
    }

    public void add(String str) {
        entries.add(str);
    }

    public void remove(String target) {
        List<String> toRemove = new ArrayList<>();
        for (String entry : entries) {
            if (entry.equals(target) || entry.endsWith(" : " + target)) {
                toRemove.add(entry);
            }
        }
        entries.removeAll(toRemove);
    }

    public String calculateSHA1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 40) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSHA1() {
        StringBuilder contentBuilder = new StringBuilder();
        for (String entry : entries) {
            contentBuilder.append(entry).append("\n");
        }
        return calculateSHA1(contentBuilder.toString());
    }

    public void printTree() {
        for (String entry : entries) {
            System.out.println(entry);
        }
    }

    public void generateBlob() {
        try {
            String folderPath = "objects";
            File objectsFolder = new File(folderPath);
            if (!objectsFolder.exists()) {
                objectsFolder.mkdir();
            }

            StringBuilder contentBuilder = new StringBuilder();
            for (String entry : entries) {
                contentBuilder.append(entry).append("\n");
            }

            String content = contentBuilder.toString();
            String sha1 = calculateSHA1(content);
            String blobFileName = folderPath + File.separator + sha1;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(blobFileName))) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addDirectory(String path) {
        try {
            File direc = new File(path);
            if (!direc.exists() || !direc.isDirectory() || !direc.canRead()) {
                throw new IllegalArgumentException("Invalid path: " + path);
            }

            List<Tree> children = new ArrayList<>();
            for (File fileFolder : direc.listFiles()) {
                String fileFolderName = fileFolder.getName();
                if (fileFolder.isFile()) {
                    String content = "blob : " + calculateSHA1(readFileContents(fileFolder)) + " : " + fileFolderName;
                    entries.add(content);
                } else if (fileFolder.isDirectory()) {
                    Tree child = new Tree();
                    children.add(child);
                    String childSHA1 = child.addDirectory(fileFolder.getPath());
                    String content = "tree : " + childSHA1 + " : " + fileFolderName;
                    entries.add(content);
                }
            }

            entries.removeAll(deletedFiles);
            entries.removeAll(editedFiles);

            generateBlob();
            return calculateSHA1(entries.toString());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String readFileContents(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    public static Tree getTree(String treeSHA, String folderPath) {
        try {
            String path = folderPath + File.separator + treeSHA;
            Path treePath = Paths.get(path);
            String content = new String(Files.readAllBytes(treePath));
            Tree tree = new Tree();
            for (String line : content.split("\n")) {
                tree.add(line);
            }
            return tree;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String str) {
        entries.remove(str);
        deletedFiles.add(str);
    }

    public void edit(String str) {
        editedFiles.add(str);
    }

    public List<String> getEditedFiles() {
        return editedFiles;
    }

    public List<String> getDeletedFiles() {
        return deletedFiles;
    }

    public Blob getBlob(String fileName) {
        for (String entry : entries) {
            String[] parts = entry.split(" : ");
            if (parts.length >= 3) {
                String type = parts[0];
                String sha1 = parts[1];
                String name = parts[2];
                if (type.equals("blob") && name.equals(fileName)) {
                    return Blob.getAccBlob(sha1, null);
                }
            }
        }
        return null;
    }
}
