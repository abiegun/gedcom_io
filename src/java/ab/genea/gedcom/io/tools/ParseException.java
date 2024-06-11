package ab.genea.gedcom.io.tools;

public class ParseException extends GedcomIOException {

    public ParseException(int lineNo, String message, Throwable cause) {
        super("Parsing ["+lineNo+"]: "+message, cause);
    }
}

