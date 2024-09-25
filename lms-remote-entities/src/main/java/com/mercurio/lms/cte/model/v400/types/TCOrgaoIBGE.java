/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Tipo Código de orgão (UF da tabela do IBGE + 90 SUFRAMA)
 * 
 * @version $Revision$ $Date$
 */
public enum TCOrgaoIBGE {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant VALUE_11
     */
    VALUE_11("11"),
    /**
     * Constant VALUE_12
     */
    VALUE_12("12"),
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
     * Constant VALUE_21
     */
    VALUE_21("21"),
    /**
     * Constant VALUE_22
     */
    VALUE_22("22"),
    /**
     * Constant VALUE_23
     */
    VALUE_23("23"),
    /**
     * Constant VALUE_24
     */
    VALUE_24("24"),
    /**
     * Constant VALUE_25
     */
    VALUE_25("25"),
    /**
     * Constant VALUE_26
     */
    VALUE_26("26"),
    /**
     * Constant VALUE_27
     */
    VALUE_27("27"),
    /**
     * Constant VALUE_28
     */
    VALUE_28("28"),
    /**
     * Constant VALUE_29
     */
    VALUE_29("29"),
    /**
     * Constant VALUE_31
     */
    VALUE_31("31"),
    /**
     * Constant VALUE_32
     */
    VALUE_32("32"),
    /**
     * Constant VALUE_33
     */
    VALUE_33("33"),
    /**
     * Constant VALUE_35
     */
    VALUE_35("35"),
    /**
     * Constant VALUE_41
     */
    VALUE_41("41"),
    /**
     * Constant VALUE_42
     */
    VALUE_42("42"),
    /**
     * Constant VALUE_43
     */
    VALUE_43("43"),
    /**
     * Constant VALUE_50
     */
    VALUE_50("50"),
    /**
     * Constant VALUE_51
     */
    VALUE_51("51"),
    /**
     * Constant VALUE_52
     */
    VALUE_52("52"),
    /**
     * Constant VALUE_53
     */
    VALUE_53("53"),
    /**
     * Constant VALUE_90
     */
    VALUE_90("90");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<String, TCOrgaoIBGE> enumConstants = new java.util.HashMap<String, TCOrgaoIBGE>();


    static {
        for (TCOrgaoIBGE c: TCOrgaoIBGE.values()) {
            TCOrgaoIBGE.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TCOrgaoIBGE(final String value) {
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
    public static TCOrgaoIBGE fromValue(
            final String value) {
        TCOrgaoIBGE c = TCOrgaoIBGE.enumConstants.get(value);
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
            final String value) {
    }

    /**
     * Method toString.
     *
     * @return the value of this constant
     */
    public String toString(
    ) {
        return this.value;
    }

    /**
     * Method value.
     *
     * @return the value of this constant
     */
    public String value(
    ) {
        return this.value;
    }

}
