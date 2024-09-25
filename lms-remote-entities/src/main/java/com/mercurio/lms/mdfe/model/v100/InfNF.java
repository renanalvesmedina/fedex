/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Informações da NF mod 1 e 1A
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfNF implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informações do Emitente da NF
     */
    private com.mercurio.lms.mdfe.model.v100.Emi _emi;

    /**
     * Informações do Destinatário da NF
     */
    private com.mercurio.lms.mdfe.model.v100.Dest _dest;

    /**
     * Série
     */
    private java.lang.String _serie;

    /**
     * Número 
     */
    private java.lang.String _nNF;

    /**
     * Data de Emissão
     */
    private java.lang.String _dEmi;

    /**
     * Valor Total da NF
     */
    private java.lang.String _vNF;

    /**
     * PIN SUFRAMA
     */
    private java.lang.String _PIN;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfNF() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Data de Emissão
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'dest'. The field 'dest' has the
     * following description: Informações do Destinatário da NF
     * 
     * @return the value of field 'Dest'.
     */
    public com.mercurio.lms.mdfe.model.v100.Dest getDest(
    ) {
        return this._dest;
    }

    /**
     * Returns the value of field 'emi'. The field 'emi' has the
     * following description: Informações do Emitente da NF
     * 
     * @return the value of field 'Emi'.
     */
    public com.mercurio.lms.mdfe.model.v100.Emi getEmi(
    ) {
        return this._emi;
    }

    /**
     * Returns the value of field 'nNF'. The field 'nNF' has the
     * following description: Número 
     * 
     * @return the value of field 'NNF'.
     */
    public java.lang.String getNNF(
    ) {
        return this._nNF;
    }

    /**
     * Returns the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     * 
     * @return the value of field 'PIN'.
     */
    public java.lang.String getPIN(
    ) {
        return this._PIN;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Série
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'vNF'. The field 'vNF' has the
     * following description: Valor Total da NF
     * 
     * @return the value of field 'VNF'.
     */
    public java.lang.String getVNF(
    ) {
        return this._vNF;
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
     * following description: Data de Emissão
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'dest'. The field 'dest' has the
     * following description: Informações do Destinatário da NF
     * 
     * @param dest the value of field 'dest'.
     */
    public void setDest(
            final com.mercurio.lms.mdfe.model.v100.Dest dest) {
        this._dest = dest;
    }

    /**
     * Sets the value of field 'emi'. The field 'emi' has the
     * following description: Informações do Emitente da NF
     * 
     * @param emi the value of field 'emi'.
     */
    public void setEmi(
            final com.mercurio.lms.mdfe.model.v100.Emi emi) {
        this._emi = emi;
    }

    /**
     * Sets the value of field 'nNF'. The field 'nNF' has the
     * following description: Número 
     * 
     * @param nNF the value of field 'nNF'.
     */
    public void setNNF(
            final java.lang.String nNF) {
        this._nNF = nNF;
    }

    /**
     * Sets the value of field 'PIN'. The field 'PIN' has the
     * following description: PIN SUFRAMA
     * 
     * @param PIN the value of field 'PIN'.
     */
    public void setPIN(
            final java.lang.String PIN) {
        this._PIN = PIN;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Série
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'vNF'. The field 'vNF' has the
     * following description: Valor Total da NF
     * 
     * @param vNF the value of field 'vNF'.
     */
    public void setVNF(
            final java.lang.String vNF) {
        this._vNF = vNF;
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
     * org.exolab.castor.builder.binding.InfNF
     */
    public static com.mercurio.lms.mdfe.model.v100.InfNF unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfNF) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfNF.class, reader);
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
