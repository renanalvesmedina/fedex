/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Informação da NF ou CT emitido pelo Tomador
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class RefNF implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Informar o CNPJ do emitente do Documento Fiscal
     */
    private java.lang.String _CNPJ;

    /**
     * Informar o código do modelo do Documento fiscal
     */
    private com.mercurio.lms.cte.model.v103.types.TModDoc _mod;

    /**
     * Informar a série do documento fiscal (informar zero se
     * inexistente).
     */
    private java.lang.String _serie;

    /**
     * Informar a sub série do documento fiscal.
     */
    private java.lang.String _subserie;

    /**
     * Informar o número do documento fiscal
     */
    private java.lang.String _nro;

    /**
     * Informar o valor do documento fiscal.
     */
    private java.lang.String _valor;

    /**
     * Informar a data de emissão do documento fiscal.
     */
    private java.lang.String _dEmi;


      //----------------/
     //- Constructors -/
    //----------------/

    public RefNF() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: Informar o CNPJ do emitente do
     * Documento Fiscal
     * 
     * @return the value of field 'CNPJ'.
     */
    public java.lang.String getCNPJ(
    ) {
        return this._CNPJ;
    }

    /**
     * Returns the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Informar a data de emissão do
     * documento fiscal.
     * 
     * @return the value of field 'DEmi'.
     */
    public java.lang.String getDEmi(
    ) {
        return this._dEmi;
    }

    /**
     * Returns the value of field 'mod'. The field 'mod' has the
     * following description: Informar o código do modelo do
     * Documento fiscal
     * 
     * @return the value of field 'Mod'.
     */
    public com.mercurio.lms.cte.model.v103.types.TModDoc getMod(
    ) {
        return this._mod;
    }

    /**
     * Returns the value of field 'nro'. The field 'nro' has the
     * following description: Informar o número do documento
     * fiscal
     * 
     * @return the value of field 'Nro'.
     */
    public java.lang.String getNro(
    ) {
        return this._nro;
    }

    /**
     * Returns the value of field 'serie'. The field 'serie' has
     * the following description: Informar a série do documento
     * fiscal (informar zero se inexistente).
     * 
     * @return the value of field 'Serie'.
     */
    public java.lang.String getSerie(
    ) {
        return this._serie;
    }

    /**
     * Returns the value of field 'subserie'. The field 'subserie'
     * has the following description: Informar a sub série do
     * documento fiscal.
     * 
     * @return the value of field 'Subserie'.
     */
    public java.lang.String getSubserie(
    ) {
        return this._subserie;
    }

    /**
     * Returns the value of field 'valor'. The field 'valor' has
     * the following description: Informar o valor do documento
     * fiscal.
     * 
     * @return the value of field 'Valor'.
     */
    public java.lang.String getValor(
    ) {
        return this._valor;
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
     * Sets the value of field 'CNPJ'. The field 'CNPJ' has the
     * following description: Informar o CNPJ do emitente do
     * Documento Fiscal
     * 
     * @param CNPJ the value of field 'CNPJ'.
     */
    public void setCNPJ(
            final java.lang.String CNPJ) {
        this._CNPJ = CNPJ;
    }

    /**
     * Sets the value of field 'dEmi'. The field 'dEmi' has the
     * following description: Informar a data de emissão do
     * documento fiscal.
     * 
     * @param dEmi the value of field 'dEmi'.
     */
    public void setDEmi(
            final java.lang.String dEmi) {
        this._dEmi = dEmi;
    }

    /**
     * Sets the value of field 'mod'. The field 'mod' has the
     * following description: Informar o código do modelo do
     * Documento fiscal
     * 
     * @param mod the value of field 'mod'.
     */
    public void setMod(
            final com.mercurio.lms.cte.model.v103.types.TModDoc mod) {
        this._mod = mod;
    }

    /**
     * Sets the value of field 'nro'. The field 'nro' has the
     * following description: Informar o número do documento
     * fiscal
     * 
     * @param nro the value of field 'nro'.
     */
    public void setNro(
            final java.lang.String nro) {
        this._nro = nro;
    }

    /**
     * Sets the value of field 'serie'. The field 'serie' has the
     * following description: Informar a série do documento fiscal
     * (informar zero se inexistente).
     * 
     * @param serie the value of field 'serie'.
     */
    public void setSerie(
            final java.lang.String serie) {
        this._serie = serie;
    }

    /**
     * Sets the value of field 'subserie'. The field 'subserie' has
     * the following description: Informar a sub série do
     * documento fiscal.
     * 
     * @param subserie the value of field 'subserie'.
     */
    public void setSubserie(
            final java.lang.String subserie) {
        this._subserie = subserie;
    }

    /**
     * Sets the value of field 'valor'. The field 'valor' has the
     * following description: Informar o valor do documento fiscal.
     * 
     * @param valor the value of field 'valor'.
     */
    public void setValor(
            final java.lang.String valor) {
        this._valor = valor;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.cte.model.v103.RefNF
     */
    public static com.mercurio.lms.cte.model.v103.RefNF unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.RefNF) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.RefNF.class, reader);
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
