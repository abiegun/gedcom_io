package ab.genea.gedcom.io.model;


import java.util.Objects;

public class Field extends RowField {

    private String dataSourceDescriptor;

    public Field(int level, String id, FieldType type, String value) {
        super(id, type, value);
        setLevel(level);
    }

    public Field(int level, FieldType type, String id, String value, String dataSourceDescriptor) {
        super(id, type, value);
        setLevel(level);
        this.dataSourceDescriptor = dataSourceDescriptor;
    }

    public String getDataSourceDescriptor() {
        return dataSourceDescriptor;
    }


    @Override
    public String toString() {
        return super.toString();
    }

}
