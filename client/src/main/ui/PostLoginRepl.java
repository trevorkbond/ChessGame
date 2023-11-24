package ui;

import client.ChessClient;
import client.InvalidResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class PostLoginRepl extends Repl{

    public PostLoginRepl() {
        super();
        validLengths.put("create", 2);
        validLengths.put("list", 1);
        validLengths.put("join", 3);
        validLengths.put("observe", 2);
        validLengths.put("logout", 1);
        validLengths.put("quit", 1);
        validLengths.put("help", 1);
    }

}
