package ab.genea.gedcom.io.model;

import ab.genea.gedcom.io.tools.GedcomIORuntimeException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.*;

import static ab.genea.gedcom.io.model.FieldType.CONC;
import static ab.genea.gedcom.io.model.FieldType.CONT;


@Data
@Accessors(chain = true)
public class RowField {

    private int level;
    private String id;
    private FieldType type;
    private String value;

    @Getter(value = AccessLevel.NONE) @Setter(value = AccessLevel.NONE)
    protected Map<FieldType, List<RowField>> subData = new HashMap<>();

    public RowField() {
    }

    public RowField(FieldType type, String value) {
        this(null, type, value);
    }

    public RowField(String id, FieldType type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public RowField appendValue(String... data) {
        StringBuilder sb = new StringBuilder();
        sb.append(getValue());
        for (String d : data) {
            sb.append(d);
        }
        return setValue(sb.toString());
    }

    public List<RowField> get(FieldType type) {
        List<RowField> list = subData.get(type);
        if (list == null) return new ArrayList<>();
        return list;
    }

    public RowField get(FieldType type, int index) {
        List<RowField> list = subData.get(type);
        if (list == null || list.size() < index + 1) return null;
        return list.get(index);
    }

    public List<RowField> getSubDataList() {
        List<RowField> result = new ArrayList<>();
        for (Collection<RowField> list : subData.values()) {
            result.addAll(list);
        }
        return result;
    }
    public RowField getSingle(FieldType type) {
        List<RowField> list = get(type);
        if (list == null) return null;
        if (list.size() > 1) throw new GedcomIORuntimeException("More then one field of type " + type);
        return list.get(0);
    }

    public RowField addSubfield(FieldType type, String value) {
        return add(new RowField(null, type, value).setLevel(getLevel() + 1));
    }

    public RowField add(RowField field) {
        FieldType type = field.getType();
        if (getLevel() + 1 != field.getLevel()) {
            System.err.println("WARN:  Wrong subfield level (" + getLevel() + ") for " + this + " and subfield " + field + " (" + field.getLevel() + ")");
        }
        if (field.getType() == CONC) {
            value = value + field.getValue();
        } else if (field.getType() == CONT) {
            value = value + "\n" + field.getValue();
        } else {
            List<RowField> list = subData.computeIfAbsent(type, k -> new ArrayList<>());
            list.add(field);
        }
        return this;
    }

    public List<RowField> getByTypes(String types) {
        return getByTypes(new ArrayList<>(), types);
    }

    protected List<RowField> getByTypes(List<RowField> result, String types) {
        if (types.isEmpty()) {
            result.add(this);
        } else {
            int dot = types.indexOf('.');
            if (dot < 0) {
                List<RowField> list = subData.get(FieldType.get(types));
                if (list != null) {
                    result.addAll(list);
                }
            } else {
                List<RowField> list = subData.get(FieldType.get(types.substring(0, dot)));
                if (list != null) {
                    for (RowField rf : list) {
                        rf.getByTypes(result, types.substring(dot + 1));
                    }
                }

            }
        }
        return result;
    }

    public String toString(boolean full) {
        if (full) return type + ": " + value + (subData.size() > 0 ? " " + subData : "");
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getLevel() == 0) {
            sb.append(type);
        }
        if (value != null && !value.isEmpty()) {
            if (getLevel() == 0) {
                sb.append(" ");
            }
            sb.append(value);
        }
        boolean started = false;
        for (FieldType type : subData.keySet()) {
            if (!started) {
                started = true;
                sb.append(" {");
            } else {
                sb.append(", ");
            }
            sb.append(type).append("=").append(subData.get(type));
        }
        if (started) {
            sb.append("}");
        }
        return sb.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowField rowField)) return false;
        return getLevel() == rowField.getLevel() && Objects.equals(getId(), rowField.getId()) && getType().equals(rowField.getType()) && Objects.equals(getValue(), rowField.getValue()) &&
                subData.equals(rowField.subData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLevel(), getId(), getType(), getValue(), subData);
    }
}


