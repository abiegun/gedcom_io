package ab.genea.gedcom.io.writer;


import ab.genea.gedcom.io.model.Field;
import ab.genea.gedcom.io.model.RowField;
import ab.genea.gedcom.io.reader.Reader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WriterTest {

    String writeTo = "test/555SAMPLE-rewritten.GED";

    @Test
    void readWrite() throws Exception {
        List<Field> ged = new Reader("test/555SAMPLE_ab.GED").parse();
        try {
            new Writer(writeTo).write(ged);
            List<Field> ged2 = new Reader(writeTo).parse();
            assertEquals(ged.size(), ged2.size());
            for (int i=0; i<ged.size(); ++i) {
                assertEquals(ged.get(i), ged2.get(i));
            }

        } finally {
            new File(writeTo).deleteOnExit();
        }
    }
}