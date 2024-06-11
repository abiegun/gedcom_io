package ab.genea.gedcom.io.reader;

import ab.genea.gedcom.io.model.Const;
import ab.genea.gedcom.io.model.Field;
import ab.genea.gedcom.io.model.FieldType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ab.genea.gedcom.io.model.FieldType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReaderTest {

    private void assert_Header(Field header) {
        assertEquals(FieldType.HEAD, header.getType());
        assertEquals(Const.GEDCOM_SPEC, header.getSingle(GEDC).getSingle(VERS).getValue());
        assertEquals("gedcom.org", header.getSingle(SOUR).getSingle(CORP).getValue());
        assertEquals("English", header.getSingle(LANG).getValue());
        assertEquals("@U1@", header.getSingle(SUBM).getValue());
    }

    @Test
    void parse() throws Exception {
        Reader reader = new Reader("test/555SAMPLE.GED");
        List<Field> parsed = reader.parse();
        assert_Header(parsed.get(0));
        int lastIndex = parsed.size() - 1;
        assertEquals(SUBM, parsed.get(1).getType());
        assertEquals("@U1@", parsed.get(1).getId());
        assertEquals(INDI, parsed.get(2).getType());
        assertEquals(2, parsed.get(2).get(FAMS).size());
        assertEquals(REPO, parsed.get(lastIndex - 1).getType());
        assertEquals(TRLR, parsed.get(lastIndex).getType());
    }

    @Test
    void parseCONS_CONT() throws Exception {
        Reader reader = new Reader("test/555SAMPLE_ab.GED");
        List<Field> parsed = reader.parse();
        String expected = "Sample text with newline.\nSecond line. Still second line.\nThird line@.";
        assertEquals(expected, parsed.get(2).getSingle(TEXT).getValue());
        assertEquals("email@address.com", parsed.get(2).getSingle(RESI).getSingle(EMAIL).getValue());

    }

    @Test
    void parse16BE() throws Exception {
        Reader reader = new Reader("test/555SAMPLE16BE.GED");
        List<Field> parsed = reader.parse();
        assert_Header(parsed.get(0));
    }

    @Test
    void parse16LE() throws Exception {
        Reader reader = new Reader("test/555SAMPLE16LE.GED");
        List<Field> parsed = reader.parse();
        assert_Header(parsed.get(0));
    }

    @Test
    void parseREMARR() throws Exception {
        Reader reader = new Reader("test/REMARR.GED");
        List<Field> parsed = reader.parse();
        assert_Header(parsed.get(0));
    }

    @Test
    void parseSSMARR() throws Exception {
        Reader reader = new Reader("test/SSMARR.GED");
        List<Field> parsed = reader.parse();
        assert_Header(parsed.get(0));
    }

}