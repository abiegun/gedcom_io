package ab.genea.gedcom.io.reader;

import ab.genea.gedcom.io.model.Field;
import ab.genea.gedcom.io.model.FieldType;
import ab.genea.gedcom.io.model.RowField;
import ab.genea.gedcom.io.tools.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static ab.genea.gedcom.io.model.Const.*;

public class Reader {

    private final List<Field> gedData = new ArrayList<>();
    private final Stack<RowField> currentRows = new Stack<>();
    private final File parsedFile;

    private final static String UTF8_BOM = "\uFEFF";
    private final static String UTF16BE_BOM = "\ufffd";
    private final static String UTF16LE_BOM = "\uffFe";

    private boolean restartParsing;

    public Reader(String filename) {
        parsedFile = new File(filename);
    }

    public List<Field> parse() throws ParseException {
        return parse(GEDCOM_ENCODING);
    }

    private String unescapeAfterRead(String line) {
        if (line.indexOf('@') >= 0) {
            return line.replaceAll("@@", "@");
        }
        return line;
    }

    private List<Field> parse(String encoding) throws ParseException {
        int lineNo = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(parsedFile), Charset.forName(encoding)))) {
            boolean firstLine = true;
            for (String line; (line = reader.readLine()) != null; ) {
                if (firstLine) {
                    if (line.startsWith(UTF8_BOM)) {
                        line = line.substring(1);
                    } else if (line.startsWith(UTF16BE_BOM)) {
                        reader.close();
                        return parse(UTF16BE);
                    } else if (line.startsWith(UTF16LE_BOM)) {
                        reader.close();
                        return parse(UTF16LE);
                    }
                    firstLine = false;
                }
                if (processLine(++lineNo, line)) break;
            }
        } catch (IOException ex) {
            throw new ParseException(lineNo, "Reading problem: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ParseException(lineNo, "Parse error: " + ex.getMessage(), ex);
        }
        return gedData;
    }


    private boolean processLine(int lineNo, String line) {
        ParsedLine pl;
        if (line.length() == 0) return false;
        line = line.replaceAll("@@", "@");
        if (Character.isDigit(line.charAt(0))) {
            if (line.length() >= 3) {
                pl = new ParsedLine(line).setLineNo(lineNo);
                pl.setLevel(Integer.parseInt(line.substring(0, 1)));
                line = line.substring(2);
                int offs = 0;
                if (line.charAt(0) == '@') {
                    while (offs < line.length() && line.charAt(offs) != ' ') ++offs;
                    pl.setId(line.substring(0, offs));
                    line = line.substring(offs + 1);
                }
                offs = 0;
                while (offs < line.length() && line.charAt(offs) != ' ') ++offs;
                pl.setType(FieldType.get(line.substring(0, offs)));
                if (offs + 1 < line.length()) {
                    pl.setContent(line.substring(offs + 1));
                }
                return parseRowData(pl);
            }
        }
        currentRows.peek().appendValue(line);
        return false;
    }

    private boolean parseRowData(ParsedLine pl) {
        Field newData = new Field(pl.getLevel(), pl.getId(), pl.getType(), pl.getContent());
        if (pl.getLevel() == 0) {
            currentRows.clear();
            currentRows.push(newData);
            gedData.add(newData);
            return (pl.getType() == FieldType.TRLR);
        }
        while (pl.getLevel() + 1 <= currentRows.size()) {
            currentRows.pop();
        }
        RowField curr = currentRows.peek();
        curr.add(newData);
        currentRows.push(newData);
        return false;
    }

}
