package ui;

public class PreloginRepl extends Repl {

    public PreloginRepl() throws Exception {
        super();
        validLengths.put("register", 4);
        validLengths.put("login", 3);
        validLengths.put("quit", 1);
        validLengths.put("help", 1);
        validLengths.put("clear", 1);
    }


}
