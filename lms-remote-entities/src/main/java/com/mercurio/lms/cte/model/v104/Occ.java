/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v104;

/**
 * Ordens de Coleta associados
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Occ implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Série da OCC
     */
    private java.lang.String _serie;

    /**
     * Número da Ordem de coleta
     */
    private java.lang.String _nOcc;

    /**
     * Data de emissão da ordem de coleta
     */
    private java.lang.String _dEmi;

    /**
     * Field _emiOcc.
     */
    private com.mercurio.lms.cte.model.v104.EmiOcc _emiOcc;


      //----------------/
     //- Constructors -/
    //----------------/

    public Occ() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão da ordem de coleta
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'emiOcc'.
     * 
     * @return the value of field 'EmiOcc'.
     */
    public com.mercurio.lms.cte.model.v104.EmiOcc getEmiOcc(
    ) {
        return this._emiOcc;
    }

    /**
     * Returns the value of field 'nOcc'. The field 'nOcc' has the
     * following description: Número da Ordem de coleta
     * 
     * @return the value of field 'NOcc'.
     */
    public java.lang.String getNOcc(
    ) {
        return this._nOcc;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série da OCC
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
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de emissão da ordem de coleta
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'emiOcc'.
     * 
     * @param emiOcc the value of field 'emiOcc'.
     */
    public void setEmiOcc(
            final com.mercurio.lms.cte.model.v104.EmiOcc emiOcc) {
        this._emiOcc = emiOcc;
    }

    /**
     * Sets the value of field 'nOcc'. The field 'nOcc' has the
     * following description: Número da Ordem de coleta
     * 
     * @param nOcc the value of field 'nOcc'.
     */
    public void setNOcc(
            final java.lang.String nOcc) {
        this._nOcc = nOcc;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série da OCC
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
     * @return the unmarshaled com.mercurio.lms.cte.model.Occ
     */
    public static com.mercurio.lms.cte.model.v104.Occ unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v104.Occ) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v104.Occ.class, reader);
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
