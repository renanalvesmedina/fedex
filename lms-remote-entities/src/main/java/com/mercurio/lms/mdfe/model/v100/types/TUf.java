/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100.types;

/**
 * Tipo Sigla da UF
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public enum TUf implements java.io.Serializable {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant AC
     */
    AC("AC"),
    /**
     * Constant AL
     */
    AL("AL"),
    /**
     * Constant AM
     */
    AM("AM"),
    /**
     * Constant AP
     */
    AP("AP"),
    /**
     * Constant BA
     */
    BA("BA"),
    /**
     * Constant CE
     */
    CE("CE"),
    /**
     * Constant DF
     */
    DF("DF"),
    /**
     * Constant ES
     */
    ES("ES"),
    /**
     * Constant GO
     */
    GO("GO"),
    /**
     * Constant MA
     */
    MA("MA"),
    /**
     * Constant MG
     */
    MG("MG"),
    /**
     * Constant MS
     */
    MS("MS"),
    /**
     * Constant MT
     */
    MT("MT"),
    /**
     * Constant PA
     */
    PA("PA"),
    /**
     * Constant PB
     */
    PB("PB"),
    /**
     * Constant PE
     */
    PE("PE"),
    /**
     * Constant PI
     */
    PI("PI"),
    /**
     * Constant PR
     */
    PR("PR"),
    /**
     * Constant RJ
     */
    RJ("RJ"),
    /**
     * Constant RN
     */
    RN("RN"),
    /**
     * Constant RO
     */
    RO("RO"),
    /**
     * Constant RR
     */
    RR("RR"),
    /**
     * Constant RS
     */
    RS("RS"),
    /**
     * Constant SC
     */
    SC("SC"),
    /**
     * Constant SE
     */
    SE("SE"),
    /**
     * Constant SP
     */
    SP("SP"),
    /**
     * Constant TO
     */
    TO("TO"),
    /**
     * Constant EX
     */
    EX("EX");

      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field value.
     */
    private final java.lang.String value;


      //----------------/
     //- Constructors -/
    //----------------/

    private TUf(final java.lang.String value) {
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
    public static com.mercurio.lms.mdfe.model.v100.types.TUf fromValue(
            final java.lang.String value) {
        for (TUf c: TUf.values()) {
            if (c.value.equals(value)) {
                return c;
            }
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
