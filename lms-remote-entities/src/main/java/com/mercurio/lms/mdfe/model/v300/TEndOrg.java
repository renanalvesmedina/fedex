/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Tipo Dados do Endereço
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TEndOrg implements java.io.Serializable {

    /**
     * Logradouro
     */
    private java.lang.String xLgr;

    /**
     * Número
     */
    private java.lang.String nro;

    /**
     * Complemento
     */
    private java.lang.String xCpl;

    /**
     * Bairro
     */
    private java.lang.String xBairro;

    /**
     * Código do município (utilizar a tabela do IBGE), informar
     * 9999999 para operações com o exterior.
     */
    private java.lang.String cMun;

    /**
     * Nome do município, , informar EXTERIOR para operações com
     * o exterior.
     */
    private java.lang.String xMun;

    /**
     * CEP
     */
    private java.lang.String CEP;

    /**
     * Sigla da UF, , informar EX para operações com o exterior.
     */
    private com.mercurio.lms.mdfe.model.v300.types.TUf UF;

    /**
     * Código do país
     */
    private java.lang.String cPais;

    /**
     * Nome do país
     */
    private java.lang.String xPais;

    /**
     * Telefone
     */
    private java.lang.String fone;

    public TEndOrg() {
        super();
    }

    /**
     * Returns the value of field 'CEP'. The field 'CEP' has the
     * following description: CEP
     * 
     * @return the value of field 'CEP'.
     */
    public java.lang.String getCEP() {
        return this.CEP;
    }

    /**
     * Returns the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do município (utilizar a
     * tabela do IBGE), informar 9999999 para operações com o
     * exterior.
     * 
     * @return the value of field 'CMun'.
     */
    public java.lang.String getCMun() {
        return this.cMun;
    }

    /**
     * Returns the value of field 'cPais'. The field 'cPais' has
     * the following description: Código do país
     * 
     * @return the value of field 'CPais'.
     */
    public java.lang.String getCPais() {
        return this.cPais;
    }

    /**
     * Returns the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     * 
     * @return the value of field 'Fone'.
     */
    public java.lang.String getFone() {
        return this.fone;
    }

    /**
     * Returns the value of field 'nro'. The field 'nro' has the
     * following description: Número
     * 
     * @return the value of field 'Nro'.
     */
    public java.lang.String getNro() {
        return this.nro;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF, , informar EX para
     * operações com o exterior.
     * 
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.mdfe.model.v300.types.TUf getUF() {
        return this.UF;
    }

    /**
     * Returns the value of field 'xBairro'. The field 'xBairro'
     * has the following description: Bairro
     * 
     * @return the value of field 'XBairro'.
     */
    public java.lang.String getXBairro() {
        return this.xBairro;
    }

    /**
     * Returns the value of field 'xCpl'. The field 'xCpl' has the
     * following description: Complemento
     * 
     * @return the value of field 'XCpl'.
     */
    public java.lang.String getXCpl() {
        return this.xCpl;
    }

    /**
     * Returns the value of field 'xLgr'. The field 'xLgr' has the
     * following description: Logradouro
     * 
     * @return the value of field 'XLgr'.
     */
    public java.lang.String getXLgr() {
        return this.xLgr;
    }

    /**
     * Returns the value of field 'xMun'. The field 'xMun' has the
     * following description: Nome do município, , informar
     * EXTERIOR para operações com o exterior.
     * 
     * @return the value of field 'XMun'.
     */
    public java.lang.String getXMun() {
        return this.xMun;
    }

    /**
     * Returns the value of field 'xPais'. The field 'xPais' has
     * the following description: Nome do país
     * 
     * @return the value of field 'XPais'.
     */
    public java.lang.String getXPais() {
        return this.xPais;
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
     * Sets the value of field 'CEP'. The field 'CEP' has the
     * following description: CEP
     * 
     * @param CEP the value of field 'CEP'.
     */
    public void setCEP(final java.lang.String CEP) {
        this.CEP = CEP;
    }

    /**
     * Sets the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do município (utilizar a
     * tabela do IBGE), informar 9999999 para operações com o
     * exterior.
     * 
     * @param cMun the value of field 'cMun'.
     */
    public void setCMun(final java.lang.String cMun) {
        this.cMun = cMun;
    }

    /**
     * Sets the value of field 'cPais'. The field 'cPais' has the
     * following description: Código do país
     * 
     * @param cPais the value of field 'cPais'.
     */
    public void setCPais(final java.lang.String cPais) {
        this.cPais = cPais;
    }

    /**
     * Sets the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     * 
     * @param fone the value of field 'fone'.
     */
    public void setFone(final java.lang.String fone) {
        this.fone = fone;
    }

    /**
     * Sets the value of field 'nro'. The field 'nro' has the
     * following description: Número
     * 
     * @param nro the value of field 'nro'.
     */
    public void setNro(final java.lang.String nro) {
        this.nro = nro;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF, , informar EX para
     * operações com o exterior.
     * 
     * @param UF the value of field 'UF'.
     */
    public void setUF(final com.mercurio.lms.mdfe.model.v300.types.TUf UF) {
        this.UF = UF;
    }

    /**
     * Sets the value of field 'xBairro'. The field 'xBairro' has
     * the following description: Bairro
     * 
     * @param xBairro the value of field 'xBairro'.
     */
    public void setXBairro(final java.lang.String xBairro) {
        this.xBairro = xBairro;
    }

    /**
     * Sets the value of field 'xCpl'. The field 'xCpl' has the
     * following description: Complemento
     * 
     * @param xCpl the value of field 'xCpl'.
     */
    public void setXCpl(final java.lang.String xCpl) {
        this.xCpl = xCpl;
    }

    /**
     * Sets the value of field 'xLgr'. The field 'xLgr' has the
     * following description: Logradouro
     * 
     * @param xLgr the value of field 'xLgr'.
     */
    public void setXLgr(final java.lang.String xLgr) {
        this.xLgr = xLgr;
    }

    /**
     * Sets the value of field 'xMun'. The field 'xMun' has the
     * following description: Nome do município, , informar
     * EXTERIOR para operações com o exterior.
     * 
     * @param xMun the value of field 'xMun'.
     */
    public void setXMun(final java.lang.String xMun) {
        this.xMun = xMun;
    }

    /**
     * Sets the value of field 'xPais'. The field 'xPais' has the
     * following description: Nome do país
     * 
     * @param xPais the value of field 'xPais'.
     */
    public void setXPais(final java.lang.String xPais) {
        this.xPais = xPais;
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
     * com.mercurio.lms.mdfe.model.v300.TEndOrg
     */
    public static com.mercurio.lms.mdfe.model.v300.TEndOrg unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.TEndOrg) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.TEndOrg.class, reader);
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
