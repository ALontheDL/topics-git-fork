import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Git {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Git newGit = new Git();
        newGit.init();
        newGit.add("testGit.txt");
        newGit.delete("testGit.txt");
    }

    public void init() throws IOException {
        File newFile = new File("Git");
        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        File folder = new File("./objects");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void add(String fileName) throws NoSuchAlgorithmException, IOException {
        Blob blubber = new Blob(fileName);
        String sha = blubber.getShaName();
        if (existsAlready(fileName, sha)) {
            if (fileExistsInObjects(sha)) {
                FileWriter fileWriter = new FileWriter("Git", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("*edited* " + fileName + " : " + sha + '\n');
                bufferedWriter.close();
                fileWriter.close();
            }
            return;
        }
        delete(fileName);
        FileWriter fileWriter = new FileWriter("Git", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileName + " : " + sha + '\n');
        bufferedWriter.close();
        fileWriter.close();

    }

    public void delete(String fileName) throws IOException, NoSuchAlgorithmException {
        File ogFile = new File("Git");
        File temp = new File("temp.txt");
    
        BufferedReader br = new BufferedReader(new FileReader(ogFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
    
        String line;
        while (br.ready()) {
            line = br.readLine();
    
            String[] name = line.split("\\s+");
    
            if (!name[0].equals(fileName)) {
                bw.write(line);
                bw.write('\n');
            } else {
                if (fileExistsInObjects(name[2])) {
                    bw.write("*deleted* " + name[0] + '\n');
                }
            }
        }
    
        ogFile.delete();
        temp.renameTo(ogFile);
        br.close();
        bw.close();
    }
    
    private boolean fileExistsInObjects(String sha) {
        File file = new File("./objects/" + sha + ".zip");
        return file.exists();
    }
    

    public boolean existsAlready(String fileName, String hash) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Git"));

        String line;
        while (br.ready()) {
            line = br.readLine();
            String[] name = line.split("\\s+");
            if (name[0].equals(fileName) && name[2].equals(hash)) {
                br.close();
                return true;
            }

        }
        br.close();
        return false;

    }

    public void checkout(String commitSHA) {
        Commit commit = Commit.getCommit(commitSHA);

        if (commit == null) {
            System.out.println("Commit not found: " + commitSHA);
            return;
        }

        Tree tree = Tree.getTree(commit.getTreeSHA(), "objects");
        List<String> entries = tree.entries;

        for (String entry : entries) {
            String[] parts = entry.split(" : ");
            if (parts.length == 3 && parts[0].equals("blob")) {
                String sha1 = parts[1];
                String filename = parts[2];

                String filePath = "./objects/" + sha1;
                String destPath = "./" + filename;
                try {
                    Files.copy(Paths.get(filePath), Paths.get(destPath));
                } catch (IOException e) {
                    System.out.println("Failed to copy file: " + filename);
                    e.printStackTrace();
                }
            } else if (parts.length == 3 && parts[0].equals("tree")) {
                // Handle directories if needed
            }
        }
    }
}