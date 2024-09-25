/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Detalhamento do CT-e do tipo Anulação
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCteAnu implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Chave de acesso do CT-e original a ser anulado e substituído
     */
    private String _chCte;

    /**
     * Data de emissão da declaração do tomador não
     * contribuinte do ICMS
     */
    private String _dEmi;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCteAnu() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chCte'. The field 'chCte' has
     * the following description: Chave de acesso do CT-e original
     * a ser anulado e substituído
     *
     * @return the value of field 'ChCte'.
     */
    public String getChCte(
    ) {
        return this._chCte;
    }

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão da declaração do
     * tomador não contribuinte do ICMS
     *
     * @return the value of field 'DEmi'.
     */
    public String getDEmi(
    ) {
        return this._dEmi;
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
     * Sets the value of field 'chCte'. The field 'chCte' has the
     * following description: Chave de acesso do CT-e original a
     * ser anulado e substituído
     *
     * @param chCte the value of field 'chCte'.
     */
    public void setChCte(
            final String chCte) {
        this._chCte = chCte;
    }

    /**
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão da declaração do
     * tomador não contribuinte do ICMS
     *
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.mercurio.lms.cte.model.v400.InfCteAnu
     */
    public static InfCteAnu unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (InfCteAnu) org.exolab.castor.xml.Unmarshaller.unmarshal(InfCteAnu.class, reader);
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
