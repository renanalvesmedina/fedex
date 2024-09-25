/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400.types;

/**
 * Enumeration TTransformURI.
 * 
 * @version $Revision$ $Date$
 */
public enum TTransformURI {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant HTTP___WWW_W3_ORG_2000_09_XMLDSIG_ENVELOPED_SIGNATUR
     */
    HTTP___WWW_W3_ORG_2000_09_XMLDSIG_ENVELOPED_SIGNATURE("http://www.w3.org/2000/09/xmldsig#enveloped-signature"),
    /**
     * Constant HTTP___WWW_W3_ORG_TR_2001_REC_XML_C14N_20010315
     */
    HTTP___WWW_W3_ORG_TR_2001_REC_XML_C14N_20010315("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");

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
    private static final java.util.Map<String, TTransformURI> enumConstants = new java.util.HashMap<String, TTransformURI>();


    static {
        for (TTransformURI c: TTransformURI.values()) {
            TTransformURI.enumConstants.put(c.value, c);
        }

    };


      //----------------/
     //- Constructors -/
    //----------------/

    private TTransformURI(final String value) {
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
    public static TTransformURI fromValue(
            final String value) {
        TTransformURI c = TTransformURI.enumConstants.get(value);
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
