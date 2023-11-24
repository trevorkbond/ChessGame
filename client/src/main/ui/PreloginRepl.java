package ui;

import client.ChessClient;
import client.InvalidResponseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class PreloginRepl extends Repl {

    public PreloginRepl() {
        super();
        validLengths.put("register", 4);
        validLengths.put("login", 3);
        validLengths.put("quit", 1);
        validLengths.put("help", 1);
    }


}
