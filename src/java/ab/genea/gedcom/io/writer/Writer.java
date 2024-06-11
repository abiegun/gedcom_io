package ab.genea.gedcom.io.writer;


import ab.genea.gedcom.io.model.Const;
import ab.genea.gedcom.io.model.Field;
import ab.genea.gedcom.io.model.RowField;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static ab.genea.gedcom.io.model.Const.GEDCOM_ENCODING;
import static ab.genea.gedcom.io.model.Const.MAX_VALUE_LENGTH;
import static ab.genea.gedcom.io.model.FieldType.CONC;
import static ab.genea.gedcom.io.model.FieldType.CONT;

public class Writer {

    String escapBeforeWrite(String value) {
        if (value.indexOf('@') >= 0) {
            value = value.replaceAll("@", "@@");
        }
        if (value.indexOf('\r') >= 0) {
            value = value.replaceAll("\n", "");
        }
        return value;
    }

    private final String outputFile;

    public Writer(String outputFile) {
        this.outputFile = outputFile;
    }

    public void write(List<Field> fields) throws FileNotFoundException, UnsupportedEncodingException {
        try (final PrintStream out = new PrintStream(new FileOutputStream(outputFile), true, GEDCOM_ENCODING)) {
            for (Field field : fields) {
                write(out, field);
            }
            out.flush();
        }
    }

    private void write(PrintStream out, RowField field) {
        out.print(field.getLevel());
        out.print(" ");
        if (field.getId() != null) {
            out.print(field.getId());
            out.print(" ");
        }
        out.print(field.getType());
        if (field.getValue() != null) {
            out.print(" ");
            String value = escapBeforeWrite(field.getValue());
            while (value.length() > 0) {
                int offs = value.indexOf('\n');
                if (offs >= 0 && offs < MAX_VALUE_LENGTH) {
                    out.println(value.substring(0, offs));
                    value = value.substring(offs + 1);
                    out.print("" + (field.getLevel() + 1) + " " + CONT + " ");
                    continue;
                }
                if (value.length() <= MAX_VALUE_LENGTH) {
                    out.print(value);
                    break;
                }
                out.println(value.substring(0, MAX_VALUE_LENGTH));
                value = value.substring(MAX_VALUE_LENGTH);
                out.print("" + (field.getLevel() + 1) + " " + CONC + " ");
            }
        }
        out.println();
        TreeSet<RowField> sorted = new TreeSet<>(comparatorRowField);
        sorted.addAll(field.getSubDataList());
        for (RowField rf : sorted) {
            write(out, rf);
        }
    }


    Comparator<RowField> comparatorRowField = new Comparator<>() {
        private int compare(String s1, String s2) {
            if (s1 != null) {
                if (s2 == null) return 1;
                return s1.compareTo(s2);
            } else if (s2 != null) return -1;
            return 0;
        }

        @Override
        public int compare(RowField o1, RowField o2) {
            int diff;
            if (0!=(diff = o1.getType().compareTo(o2.getType()))) return diff;
            if (0!=(diff = compare(o1.getId(), o2.getId()))) return diff;
            if (0!=(diff = compare(o1.getValue(), o2.getValue()))) return diff;
            return o1.hashCode() - o2.hashCode();
        }
    };

}
