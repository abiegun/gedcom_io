package ab.genea.gedcom.io.model;

import java.util.*;

public class FieldType implements Comparable<FieldType> {

    private static final Map<String,FieldType> types = new HashMap<>();

    private int order;
    private final String name;
    private FieldType(String name) {
        this.name = name;
        setOrder(types.size());
    }
    private FieldType setOrder(int order) {
        this.order = order;
        return this;
    }

    //0 level records
    public static final FieldType HEAD = get("HEAD");
    public static final FieldType INDI = get("INDI");
    public static final FieldType FAM  = get("FAM" );
    public static final FieldType SOUR = get("SOUR");
    public static final FieldType REPO = get("REPO");
    public static final FieldType SUBM = get("SUBM");
    public static final FieldType TRLR = get("TRLR").setOrder(999999);

    //header
    public static final FieldType GEDC = get("GEDC");
    public static final FieldType CHAR = get("CHAR");
    public static final FieldType LANG = get("LANG");
    public static final FieldType VERS = get("VERS");
    public static final FieldType FORM = get("FORM");
    public static final FieldType DEST = get("DEST");  //destination system


    public static final FieldType ADDR = get("ADDR");
    public static final FieldType NAME = get("NAME");
    public static final FieldType DATE = get("DATE");
    public static final FieldType TIME = get("TIME");
    public static final FieldType CHAN = get("CHAN"); //change time?
    public static final FieldType FILE = get("FILE");
    public static final FieldType OBJE = get("OBJE");  //multimedia
    public static final FieldType TYPE = get("TYPE");
    public static final FieldType REFN = get("REFN");
    public static final FieldType NOTE = get("NOTE");
    public static final FieldType TITL = get("TITL");
    public static final FieldType EVEN = get("EVEN"); //event
    public static final FieldType DATA = get("DATA");
    public static final FieldType TEXT = get("TEXT");
    public static final FieldType CORP = get("CORP");
    public static final FieldType GIVN = get("GIVN");
    public static final FieldType SURN = get("SURN");
    public static final FieldType SEX  = get("SEX");
    public static final FieldType RESI = get("RESI");
    public static final FieldType EMAIL= get("EMAIL");
    public static final FieldType FAMC = get("FAMC");
    public static final FieldType BIRT = get("BIRT");
    public static final FieldType PLAC = get("PLAC");
    public static final FieldType DEAT = get("DEAT");
    public static final FieldType AGE  = get("AGE");
    public static final FieldType FAMS = get("FAMS");
    public static final FieldType PAGE = get("PAGE");
    public static final FieldType ROLE = get("ROLE");
    public static final FieldType BURI = get("BURI");
    public static final FieldType CHR  = get("CHR");
    public static final FieldType CAUS = get("CAUS");
    public static final FieldType ADR1 = get("ADR1");
    public static final FieldType CITY = get("CITY");
    public static final FieldType BAPM = get("BAPM");
    public static final FieldType RELI = get("RELI");
    public static final FieldType DSCR = get("DSCR");
    public static final FieldType CTRY = get("CTRY");
    public static final FieldType POST = get("POST");
    public static final FieldType IMMI = get("IMMI");
    public static final FieldType ADR2 = get("ADR2");
    public static final FieldType OCCU = get("OCCU");
    public static final FieldType HUSB = get("HUSB");
    public static final FieldType WIFE = get("WIFE");
    public static final FieldType CHIL = get("CHIL");
    public static final FieldType MARR = get("MARR");
    public static final FieldType DIV  = get("DIV");
    public static final FieldType MARL = get("MARL");
    public static final FieldType NCHI = get("NCHI");
    public static final FieldType AUTH = get("AUTH");
    public static final FieldType PUBL = get("PUBL");
    public static final FieldType CONC = get("CONC"); //concatenation with prev line
    public static final FieldType CONT = get("CONT"); //concatenation with prev line with new line
    public static final FieldType QUAY = get("QUAY"); //Certainty 0-3
    public static final FieldType RIN  = get("RIN" );

    //=== MyHeritage extension
    public static final FieldType _MARNM = get("_MARNM");
    public static final FieldType _RTLSAVE = get("_RTLSAVE");
    public static final FieldType _PROJECT_GUID = get("_PROJECT_GUID");
    public static final FieldType _EXPORTED_FROM_SITE_ID = get("_EXPORTED_FROM_SITE_ID");
    public static final FieldType _UPD  = get("_UPD");
    public static final FieldType _UID  = get("_UID");
    public static final FieldType _TYPE = get("_TYPE");
    public static final FieldType _MEDI = get("_MEDI");

    //=== ab.GEDCOM extension
    public static final FieldType _ESTIMATION = get("_ESTIMATION"); //subfields: BIRT yyyy-yyyy, DEAT yyyy-yyyy


    private static final Set<FieldType> LEVEL0TYPES = new HashSet<>(Arrays.asList(INDI,FAM,SOUR,HEAD,REPO,TRLR));
    public static boolean isTypeLevel0(FieldType type) {
        return LEVEL0TYPES.contains(type);
    }

    public static synchronized FieldType get(String type) {
        FieldType result = types.get(type);
        if (result == null) {
            types.put(type,result = new FieldType(type) );
        }
        return result;
    }


    @Override
    public String toString() {
        return name;
    }

    public int length() {
        return name.length();
    }

    @Override
    public int compareTo(FieldType o) {
        return order - o.order;
    }
}
