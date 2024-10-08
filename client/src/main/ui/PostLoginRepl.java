package ui;

public class PostLoginRepl extends Repl {

    public PostLoginRepl() throws Exception {
        super();
        validLengths.put("create", 2);
        validLengths.put("list", 1);
        validLengths.put("join", 3);
        validLengths.put("observe", 2);
        validLengths.put("logout", 1);
        validLengths.put("help", 1);
        validLengths.put("clear", 1);
    }

}
