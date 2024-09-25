/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Totalizadores da carga transportada e seus documentos fiscais
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Tot implements java.io.Serializable {

    /**
     * Quantidade total de CT-e relacionados no Manifesto
     */
    private java.lang.String qCTe;

    /**
     * Quantidade total de NF-e relacionadas no Manifesto
     */
    private java.lang.String qNFe;

    /**
     * Quantidade total de MDF-e relacionados no Manifesto
     * Aquaviário
     */
    private java.lang.String qMDFe;

    /**
     * Valor total da carga / mercadorias transportadas
     */
    private java.lang.String vCarga;

    /**
     * Codigo da unidade de medida do Peso Bruto da Carga /
     * Mercadorias transportadas
     */
    private com.mercurio.lms.mdfe.model.v300.types.CUnidType cUnid;

    /**
     * Peso Bruto Total da Carga / Mercadorias transportadas
     */
    private java.lang.String qCarga;

    public Tot() {
        super();
    }

    /**
     * Returns the value of field 'cUnid'. The field 'cUnid' has
     * the following description: Codigo da unidade de medida do
     * Peso Bruto da Carga / Mercadorias transportadas
     * 
     * @return the value of field 'CUnid'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.CUnidType getCUnid() {
        return this.cUnid;
    }

    /**
     * Returns the value of field 'qCTe'. The field 'qCTe' has the
     * following description: Quantidade total de CT-e relacionados
     * no Manifesto
     * 
     * @return the value of field 'QCTe'.
     */
    public java.lang.String getQCTe() {
        return this.qCTe;
    }

    /**
     * Returns the value of field 'qCarga'. The field 'qCarga' has
     * the following description: Peso Bruto Total da Carga /
     * Mercadorias transportadas
     * 
     * @return the value of field 'QCarga'.
     */
    public java.lang.String getQCarga() {
        return this.qCarga;
    }

    /**
     * Returns the value of field 'qMDFe'. The field 'qMDFe' has
     * the following description: Quantidade total de MDF-e
     * relacionados no Manifesto Aquaviário
     * 
     * @return the value of field 'QMDFe'.
     */
    public java.lang.String getQMDFe() {
        return this.qMDFe;
    }

    /**
     * Returns the value of field 'qNFe'. The field 'qNFe' has the
     * following description: Quantidade total de NF-e relacionadas
     * no Manifesto
     * 
     * @return the value of field 'QNFe'.
     */
    public java.lang.String getQNFe() {
        return this.qNFe;
    }

    /**
     * Returns the value of field 'vCarga'. The field 'vCarga' has
     * the following description: Valor total da carga /
     * mercadorias transportadas
     * 
     * @return the value of field 'VCarga'.
     */
    public java.lang.String getVCarga() {
        return this.vCarga;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'cUnid'. The field 'cUnid' has the
     * following description: Codigo da unidade de medida do Peso
     * Bruto da Carga / Mercadorias transportadas
     * 
     * @param cUnid the value of field 'cUnid'.
     */
    public void setCUnid(final com.mercurio.lms.mdfe.model.v300.types.CUnidType cUnid) {
        this.cUnid = cUnid;
    }

    /**
     * Sets the value of field 'qCTe'. The field 'qCTe' has the
     * following description: Quantidade total de CT-e relacionados
     * no Manifesto
     * 
     * @param qCTe the value of field 'qCTe'.
     */
    public void setQCTe(final java.lang.String qCTe) {
        this.qCTe = qCTe;
    }

    /**
     * Sets the value of field 'qCarga'. The field 'qCarga' has the
     * following description: Peso Bruto Total da Carga /
     * Mercadorias transportadas
     * 
     * @param qCarga the value of field 'qCarga'.
     */
    public void setQCarga(final java.lang.String qCarga) {
        this.qCarga = qCarga;
    }

    /**
     * Sets the value of field 'qMDFe'. The field 'qMDFe' has the
     * following description: Quantidade total de MDF-e
     * relacionados no Manifesto Aquaviário
     * 
     * @param qMDFe the value of field 'qMDFe'.
     */
    public void setQMDFe(final java.lang.String qMDFe) {
        this.qMDFe = qMDFe;
    }

    /**
     * Sets the value of field 'qNFe'. The field 'qNFe' has the
     * following description: Quantidade total de NF-e relacionadas
     * no Manifesto
     * 
     * @param qNFe the value of field 'qNFe'.
     */
    public void setQNFe(final java.lang.String qNFe) {
        this.qNFe = qNFe;
    }

    /**
     * Sets the value of field 'vCarga'. The field 'vCarga' has the
     * following description: Valor total da carga / mercadorias
     * transportadas
     * 
     * @param vCarga the value of field 'vCarga'.
     */
    public void setVCarga(final java.lang.String vCarga) {
        this.vCarga = vCarga;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v300.Tot
     */
    public static com.mercurio.lms.mdfe.model.v300.Tot unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.Tot) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.Tot.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
