package withrules;
import java.util.*;
import java.io.*;
public class FileHandlerr {
    private BufferedReader reader;
    private BufferedWriter writer;

    public FileHandlerr(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public BufferedReader getReader() { return reader; }
    public BufferedWriter getWriter() { return writer; }
}

