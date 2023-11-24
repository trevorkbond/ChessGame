package ui;

public class PreloginRepl extends Repl {

    public PreloginRepl() {
        super();
        validLengths.put("register", 4);
        validLengths.put("login", 3);
        validLengths.put("quit", 1);
        validLengths.put("help", 1);
    }


}
