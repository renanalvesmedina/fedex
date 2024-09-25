/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Contrato de Transporte Rodoviário de BensTambém conhecido como
 * Carta Frete ou Vale Frete
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class CTRB implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Série do CTRB
     */
    private java.lang.String _serie;

    /**
     * Número do Contrato de Transporte Rodoviário de Bens
     */
    private java.lang.String _nCTRB;


      //----------------/
     //- Constructors -/
    //----------------/

    public CTRB() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'nCTRB'. The field 'nCTRB' has
     * the following description: Número do Contrato de Transporte
     * Rodoviário de Bens
     * 
     * 
     * @return the value of field 'NCTRB'.
     */
    public java.lang.String getNCTRB(
    ) {
        return this._nCTRB;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série do CTRB
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'nCTRB'. The field 'nCTRB' has the
     * following description: Número do Contrato de Transporte
     * Rodoviário de Bens
     * 
     * 
     * @param nCTRB the value of field 'nCTRB'.
     */
    public void setNCTRB(
            final java.lang.String nCTRB) {
        this._nCTRB = nCTRB;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série do CTRB
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.CTRB
     */
    public static com.mercurio.lms.cte.model.v103.CTRB unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.CTRB) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.CTRB.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
