/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103.types;

/**
 * Tipo Modelo do Documento
 * 
 * @version $Revision$ $Date$
 */
public enum TModDoc {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_0
     */
    VALUE_0("01"),
    /**
     * Constant VALUE_1
     */
    VALUE_1("1B"),
    /**
     * Constant VALUE_2
     */
    VALUE_2("02"),
    /**
     * Constant VALUE_3
     */
    VALUE_3("2D"),
    /**
     * Constant VALUE_4
     */
    VALUE_4("2E"),
    /**
     * Constant VALUE_5
     */
    VALUE_5("04"),
    /**
     * Constant VALUE_6
     */
    VALUE_6("06"),
    /**
     * Constant VALUE_7
     */
    VALUE_7("07"),
    /**
     * Constant VALUE_8
     */
    VALUE_8("08"),
    /**
     * Constant VALUE_9
     */
    VALUE_9("8B"),
    /**
     * Constant VALUE_10
     */
    VALUE_10("09"),
    /**
     * Constant VALUE_11
     */
    VALUE_11("10"),
    /**
     * Constant VALUE_12
     */
    VALUE_12("11"),
    /**
     * Constant VALUE_13
     */
    VALUE_13("13"),
    /**
     * Constant VALUE_14
     */
    VALUE_14("14"),
    /**
     * Constant VALUE_15
     */
    VALUE_15("15"),
    /**
     * Constant VALUE_16
     */
    VALUE_16("16"),
    /**
     * Constant VALUE_17
     */
    VALUE_17("17"),
    /**
     * Constant VALUE_18
     */
    VALUE_18("18"),
    /**
     * Constant VALUE_19
     */
    VALUE_19("20"),
    /**
     * Constant VALUE_20
     */
    VALUE_20("21"),
    /**
     * Constant VALUE_21
     */
    VALUE_21("22"),
    /**
     * Constant VALUE_22
     */
    VALUE_22("23"),
    /**
     * Constant VALUE_23
     */
    VALUE_23("24"),
    /**
     * Constant VALUE_24
     */
    VALUE_24("25"),
    /**
     * Constant VALUE_25
     */
    VALUE_25("26"),
    /**
     * Constant VALUE_26
     */
    VALUE_26("27"),
    /**
     * Constant VALUE_27
     */
    VALUE_27("28"),
    /**
     * Constant VALUE_28
     */
    VALUE_28("55");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, TModDoc> enumConstants = new java.util.HashMap<java.lang.String, TModDoc>();


    static {
        for (TModDoc c: TModDoc.values()) {
            TModDoc.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TModDoc(final java.lang.String value) {
        this.value = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.mercurio.lms.cte.model.v103.types.TModDoc fromValue(
            final java.lang.String value) {
        TModDoc c = TModDoc.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * 
     * 
     * @param value
     */
    public void setValue(
            final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString(
    ) {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value(
    ) {
        return this.value;
    }

}
