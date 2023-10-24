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
import java.util.HashMap;
import java.util.List;

public class Tree {
    private HashMap<String, String> blobEntries;
    private HashMap<String, String> treeEntries;

    public Tree() {
        this.blobEntries = new HashMap<String, String>();
        this.treeEntries = new HashMap<String, String>();
    }

    public void add(String str) throws Exception {
        String [] array = str.split(" : ");
        if (array.length != 3) {
            throw new Exception("Wrong string format.");
        }
        String type = array[0];
        String sha = array[1];
        String name = array[2];

        if (type.equals("blob")){
            if (blobEntries.containsKey(name)){
                throw new Exception("Exists.");
            }
            if (!blobEntries.containsValue(sha)){
                blobEntries.put(name, sha);
            }
        }
        else if (type.equals("tree")){
            if (treeEntries.containsKey(name)){
                throw new Exception("Exists.");
            }
            if(!treeEntries.containsValue(sha)){
                treeEntries.put(name, sha);
            }
        else{
            throw new Exception ("Invalid input.");
            }
        }
    }

    public boolean remove(String target) {
        if (blobEntries.containsKey(target)){
            blobEntries.remove(target);
            return true;
        }
        else if(treeEntries.containsKey(target)){
            treeEntries.remove(target);
            return true;
        }
        else{
            return false;
        }
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

    public void generateBlob() {
        try {
            String folderPath = "objects";
            File objectsFolder = new File(folderPath);
            if (!objectsFolder.exists()) {
                objectsFolder.mkdir();
            }

            StringBuilder contentBuilder = new StringBuilder();
            for (HashMap.Entry<String, String> entry : blobEntries.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                contentBuilder.append("blob : " + value + " : " + key + "\n");
            }

            for (HashMap.Entry<String, String> entry: treeEntries.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                contentBuilder.append("tree : " + value + " : " + key + "\n");
            }

            if (contentBuilder.length() > 0){
                contentBuilder.deleteCharAt(contentBuilder.length() - 1);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
