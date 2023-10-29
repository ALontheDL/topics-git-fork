import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    public List<String> entries;

    public Tree() {
        this.entries = new ArrayList<>();
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
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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

    public void delete(String str) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("tree.txt"));
            try {
                while (reader.ready()) {
                    if (reader.readLine().equals(str)) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String addDirectory (String path){
        try {
            File direc = new File (path);
            if (!direc.exists() || !direc.isDirectory() || !direc.canRead()){
                throw new IllegalArgumentException("Invalid path: " + path);
            }

            List<Tree> children = new ArrayList<>();
            for (File fileFolder : direc.listFiles()){
                String fileFolderName = fileFolder.getName();
                if (fileFolder.isFile()){
                    String content = "blob : " + calculateSHA1(readFileContents(fileFolder)) + " : " + fileFolderName;
                    entries.add(content);
                }
                else if (fileFolder.isDirectory()){
                    Tree child = new Tree();
                    children.add(child);
                    String childSHA1 = child.addDirectory(fileFolder.getPath());
                    String content = "tree : " + childSHA1 + " : " + fileFolderName;
                    entries.add(content);
                }
            }

            generateBlob();
            return calculateSHA1(entries.toString());
        } catch (IllegalArgumentException e){
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String readFileContents(File file) throws IOException{
        StringBuilder sb = new StringBuilder();
        try (BufferedReader read = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = read.readLine()) != null){
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}