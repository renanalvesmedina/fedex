/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Totalizadores da carga transportada e seus documentos fiscais
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Tot implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Quantidade total de CT-e relacionados no Manifesto
     */
    private java.lang.String _qCTe;

    /**
     * Quantidade total de Conhecimentos Papel relacionados no
     * Manifesto
     */
    private java.lang.String _qCT;

    /**
     * Quantidade total de NF-e relacionadas no Manifesto
     */
    private java.lang.String _qNFe;

    /**
     * Quantidade total de Nota Fiscal mod 1/1A relacionadas no
     * Manifesto
     */
    private java.lang.String _qNF;

    /**
     * Valor total da carga / mercadorias transportadas
     */
    private java.lang.String _vCarga;

    /**
     * Codigo da unidade de medida do Peso Bruto da Carga /
     * Mercadorias transportadas
     */
    private com.mercurio.lms.mdfe.model.v100.types.CUnidType _cUnid;

    /**
     * Peso Bruto Total da Carga / Mercadorias transportadas
     */
    private java.lang.String _qCarga;


      //----------------/
     //- Constructors -/
    //----------------/

    public Tot() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cUnid'. The field 'cUnid' has
     * the following description: Codigo da unidade de medida do
     * Peso Bruto da Carga / Mercadorias transportadas
     * 
     * @return the value of field 'CUnid'.
     */
    public com.mercurio.lms.mdfe.model.v100.types.CUnidType getCUnid(
    ) {
        return this._cUnid;
    }

    /**
     * Returns the value of field 'qCT'. The field 'qCT' has the
     * following description: Quantidade total de Conhecimentos
     * Papel relacionados no Manifesto
     * 
     * @return the value of field 'QCT'.
     */
    public java.lang.String getQCT(
    ) {
        return this._qCT;
    }

    /**
     * Returns the value of field 'qCTe'. The field 'qCTe' has the
     * following description: Quantidade total de CT-e relacionados
     * no Manifesto
     * 
     * @return the value of field 'QCTe'.
     */
    public java.lang.String getQCTe(
    ) {
        return this._qCTe;
    }

    /**
     * Returns the value of field 'qCarga'. The field 'qCarga' has
     * the following description: Peso Bruto Total da Carga /
     * Mercadorias transportadas
     * 
     * @return the value of field 'QCarga'.
     */
    public java.lang.String getQCarga(
    ) {
        return this._qCarga;
    }

    /**
     * Returns the value of field 'qNF'. The field 'qNF' has the
     * following description: Quantidade total de Nota Fiscal mod
     * 1/1A relacionadas no Manifesto
     * 
     * @return the value of field 'QNF'.
     */
    public java.lang.String getQNF(
    ) {
        return this._qNF;
    }

    /**
     * Returns the value of field 'qNFe'. The field 'qNFe' has the
     * following description: Quantidade total de NF-e relacionadas
     * no Manifesto
     * 
     * @return the value of field 'QNFe'.
     */
    public java.lang.String getQNFe(
    ) {
        return this._qNFe;
    }

    /**
     * Returns the value of field 'vCarga'. The field 'vCarga' has
     * the following description: Valor total da carga /
     * mercadorias transportadas
     * 
     * @return the value of field 'VCarga'.
     */
    public java.lang.String getVCarga(
    ) {
        return this._vCarga;
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
     * Sets the value of field 'cUnid'. The field 'cUnid' has the
     * following description: Codigo da unidade de medida do Peso
     * Bruto da Carga / Mercadorias transportadas
     * 
     * @param cUnid the value of field 'cUnid'.
     */
    public void setCUnid(
            final com.mercurio.lms.mdfe.model.v100.types.CUnidType cUnid) {
        this._cUnid = cUnid;
    }

    /**
     * Sets the value of field 'qCT'. The field 'qCT' has the
     * following description: Quantidade total de Conhecimentos
     * Papel relacionados no Manifesto
     * 
     * @param qCT the value of field 'qCT'.
     */
    public void setQCT(
            final java.lang.String qCT) {
        this._qCT = qCT;
    }

    /**
     * Sets the value of field 'qCTe'. The field 'qCTe' has the
     * following description: Quantidade total de CT-e relacionados
     * no Manifesto
     * 
     * @param qCTe the value of field 'qCTe'.
     */
    public void setQCTe(
            final java.lang.String qCTe) {
        this._qCTe = qCTe;
    }

    /**
     * Sets the value of field 'qCarga'. The field 'qCarga' has the
     * following description: Peso Bruto Total da Carga /
     * Mercadorias transportadas
     * 
     * @param qCarga the value of field 'qCarga'.
     */
    public void setQCarga(
            final java.lang.String qCarga) {
        this._qCarga = qCarga;
    }

    /**
     * Sets the value of field 'qNF'. The field 'qNF' has the
     * following description: Quantidade total de Nota Fiscal mod
     * 1/1A relacionadas no Manifesto
     * 
     * @param qNF the value of field 'qNF'.
     */
    public void setQNF(
            final java.lang.String qNF) {
        this._qNF = qNF;
    }

    /**
     * Sets the value of field 'qNFe'. The field 'qNFe' has the
     * following description: Quantidade total de NF-e relacionadas
     * no Manifesto
     * 
     * @param qNFe the value of field 'qNFe'.
     */
    public void setQNFe(
            final java.lang.String qNFe) {
        this._qNFe = qNFe;
    }

    /**
     * Sets the value of field 'vCarga'. The field 'vCarga' has the
     * following description: Valor total da carga / mercadorias
     * transportadas
     * 
     * @param vCarga the value of field 'vCarga'.
     */
    public void setVCarga(
            final java.lang.String vCarga) {
        this._vCarga = vCarga;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.exolab.castor.builder.binding.Tot
     */
    public static com.mercurio.lms.mdfe.model.v100.Tot unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.Tot) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.Tot.class, reader);
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
