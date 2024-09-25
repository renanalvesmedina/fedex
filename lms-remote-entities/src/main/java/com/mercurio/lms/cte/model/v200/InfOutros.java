/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v200;

/**
 * Informações dos demais documentos
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfOutros implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tipo de documento originário
     */
    private com.mercurio.lms.cte.model.v200.types.TpDocType _tpDoc;

    /**
     * Descrição quando se tratar de 99-Outros
     */
    private java.lang.String _descOutros;

    /**
     * Número 
     */
    private java.lang.String _nDoc;

    /**
     * Data de Emissão
     */
    private java.lang.String _dEmi;

    /**
     * Valor do documento
     */
    private java.lang.String _vDocFisc;

    /**
     * Data prevista de entrega
     */
    private java.lang.String _dPrev;

    /**
     * Field _infOutrosChoice.
     */
    private com.mercurio.lms.cte.model.v200.InfOutrosChoice _infOutrosChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfOutros() {
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
     * Returns the value of field 'dPrev'. The field 'dPrev' has
     * the following description: Data prevista de entrega
     * 
     * @return the value of field 'DPrev'.
     */
    public java.lang.String getDPrev(
    ) {
        return this._dPrev;
    }

    /**
     * Returns the value of field 'descOutros'. The field
     * 'descOutros' has the following description: Descrição
     * quando se tratar de 99-Outros
     * 
     * @return the value of field 'DescOutros'.
     */
    public java.lang.String getDescOutros(
    ) {
        return this._descOutros;
    }

    /**
     * Returns the value of field 'infOutrosChoice'.
     * 
     * @return the value of field 'InfOutrosChoice'.
     */
    public com.mercurio.lms.cte.model.v200.InfOutrosChoice getInfOutrosChoice(
    ) {
        return this._infOutrosChoice;
    }

    /**
     * Returns the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número 
     * 
     * @return the value of field 'NDoc'.
     */
    public java.lang.String getNDoc(
    ) {
        return this._nDoc;
    }

    /**
     * Returns the value of field 'tpDoc'. The field 'tpDoc' has
     * the following description: Tipo de documento originário
     * 
     * @return the value of field 'TpDoc'.
     */
    public com.mercurio.lms.cte.model.v200.types.TpDocType getTpDoc(
    ) {
        return this._tpDoc;
    }

    /**
     * Returns the value of field 'vDocFisc'. The field 'vDocFisc'
     * has the following description: Valor do documento
     * 
     * @return the value of field 'VDocFisc'.
     */
    public java.lang.String getVDocFisc(
    ) {
        return this._vDocFisc;
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
     * Sets the value of field 'dPrev'. The field 'dPrev' has the
     * following description: Data prevista de entrega
     * 
     * @param dPrev the value of field 'dPrev'.
     */
    public void setDPrev(
            final java.lang.String dPrev) {
        this._dPrev = dPrev;
    }

    /**
     * Sets the value of field 'descOutros'. The field 'descOutros'
     * has the following description: Descrição quando se tratar
     * de 99-Outros
     * 
     * @param descOutros the value of field 'descOutros'.
     */
    public void setDescOutros(
            final java.lang.String descOutros) {
        this._descOutros = descOutros;
    }

    /**
     * Sets the value of field 'infOutrosChoice'.
     * 
     * @param infOutrosChoice the value of field 'infOutrosChoice'.
     */
    public void setInfOutrosChoice(
            final com.mercurio.lms.cte.model.v200.InfOutrosChoice infOutrosChoice) {
        this._infOutrosChoice = infOutrosChoice;
    }

    /**
     * Sets the value of field 'nDoc'. The field 'nDoc' has the
     * following description: Número 
     * 
     * @param nDoc the value of field 'nDoc'.
     */
    public void setNDoc(
            final java.lang.String nDoc) {
        this._nDoc = nDoc;
    }

    /**
     * Sets the value of field 'tpDoc'. The field 'tpDoc' has the
     * following description: Tipo de documento originário
     * 
     * @param tpDoc the value of field 'tpDoc'.
     */
    public void setTpDoc(
            final com.mercurio.lms.cte.model.v200.types.TpDocType tpDoc) {
        this._tpDoc = tpDoc;
    }

    /**
     * Sets the value of field 'vDocFisc'. The field 'vDocFisc' has
     * the following description: Valor do documento
     * 
     * @param vDocFisc the value of field 'vDocFisc'.
     */
    public void setVDocFisc(
            final java.lang.String vDocFisc) {
        this._vDocFisc = vDocFisc;
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
     * com.mercurio.lms.cte.model.v200.InfOutros
     */
    public static com.mercurio.lms.cte.model.v200.InfOutros unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v200.InfOutros) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v200.InfOutros.class, reader);
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
