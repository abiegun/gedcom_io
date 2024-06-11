package ab.genea.gedcom.io.reader;

import ab.genea.gedcom.io.model.FieldType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
class ParsedLine {
    private int lineNo;
    private String line;
    private int level;
    private String id;
    private FieldType type;
    private String content = "";

    ParsedLine(String line) {
        this.line = line;
    }
    @Override
    public String toString() {
        if (id==null) {
            return ""+level+" "+ type + content;
        }
        return ""+level+" "+ type + id+" "+content;
    }

}
