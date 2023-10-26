public class Commit {
    private String treeSHA;
    private String author;
    private String date;
    private String summary;

    public Commit (String treeSha1){
        this.treeSHA = treeSHA;
        this.author = author;
        this.date = ;
        this.summary = summary;
    }

    public String getTreeSHA(){
        return treeSHA;
    }

    public String getDate(){
        return date;
    }

    public String createTree(){
        Tree tree = new Tree();
        return tree.generateSHA();
    }

    public String generateSHA(){
        String file = summary + "\n" + date + "\n" + author + "\n" + treeSHA + "\n";
        
    }


}
