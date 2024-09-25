/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Tipo Dados do Endereço
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TEndeEmi implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Logradouro
     */
    private String _xLgr;

    /**
     * Número
     */
    private String _nro;

    /**
     * Complemento
     */
    private String _xCpl;

    /**
     * Bairro
     */
    private String _xBairro;

    /**
     * Código do município (utilizar a tabela do IBGE)
     */
    private String _cMun;

    /**
     * Nome do município
     */
    private String _xMun;

    /**
     * CEP
     */
    private String _CEP;

    /**
     * Sigla da UF
     */
    private com.mercurio.lms.cte.model.v400.types.TUF_sem_EX _UF;

    /**
     * Telefone
     */
    private String _fone;


      //----------------/
     //- Constructors -/
    //----------------/

    public TEndeEmi() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CEP'. The field 'CEP' has the
     * following description: CEP
     *
     * @return the value of field 'CEP'.
     */
    public String getCEP(
    ) {
        return this._CEP;
    }

    /**
     * Returns the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do município (utilizar a
     * tabela do IBGE)
     *
     * @return the value of field 'CMun'.
     */
    public String getCMun(
    ) {
        return this._cMun;
    }

    /**
     * Returns the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     *
     * @return the value of field 'Fone'.
     */
    public String getFone(
    ) {
        return this._fone;
    }

    /**
     * Returns the value of field 'nro'. The field 'nro' has the
     * following description: Número
     *
     * @return the value of field 'Nro'.
     */
    public String getNro(
    ) {
        return this._nro;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     *
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.cte.model.v400.types.TUF_sem_EX getUF(
    ) {
        return this._UF;
    }

    /**
     * Returns the value of field 'xBairro'. The field 'xBairro'
     * has the following description: Bairro
     *
     * @return the value of field 'XBairro'.
     */
    public String getXBairro(
    ) {
        return this._xBairro;
    }

    /**
     * Returns the value of field 'xCpl'. The field 'xCpl' has the
     * following description: Complemento
     *
     * @return the value of field 'XCpl'.
     */
    public String getXCpl(
    ) {
        return this._xCpl;
    }

    /**
     * Returns the value of field 'xLgr'. The field 'xLgr' has the
     * following description: Logradouro
     *
     * @return the value of field 'XLgr'.
     */
    public String getXLgr(
    ) {
        return this._xLgr;
    }

    /**
     * Returns the value of field 'xMun'. The field 'xMun' has the
     * following description: Nome do município
     *
     * @return the value of field 'XMun'.
     */
    public String getXMun(
    ) {
        return this._xMun;
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
     * Sets the value of field 'CEP'. The field 'CEP' has the
     * following description: CEP
     *
     * @param CEP the value of field 'CEP'.
     */
    public void setCEP(
            final String CEP) {
        this._CEP = CEP;
    }

    /**
     * Sets the value of field 'cMun'. The field 'cMun' has the
     * following description: Código do município (utilizar a
     * tabela do IBGE)
     *
     * @param cMun the value of field 'cMun'.
     */
    public void setCMun(
            final String cMun) {
        this._cMun = cMun;
    }

    /**
     * Sets the value of field 'fone'. The field 'fone' has the
     * following description: Telefone
     *
     * @param fone the value of field 'fone'.
     */
    public void setFone(
            final String fone) {
        this._fone = fone;
    }

    /**
     * Sets the value of field 'nro'. The field 'nro' has the
     * following description: Número
     *
     * @param nro the value of field 'nro'.
     */
    public void setNro(
            final String nro) {
        this._nro = nro;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     *
     * @param UF the value of field 'UF'.
     */
    public void setUF(
            final com.mercurio.lms.cte.model.v400.types.TUF_sem_EX UF) {
        this._UF = UF;
    }

    /**
     * Sets the value of field 'xBairro'. The field 'xBairro' has
     * the following description: Bairro
     *
     * @param xBairro the value of field 'xBairro'.
     */
    public void setXBairro(
            final String xBairro) {
        this._xBairro = xBairro;
    }

    /**
     * Sets the value of field 'xCpl'. The field 'xCpl' has the
     * following description: Complemento
     *
     * @param xCpl the value of field 'xCpl'.
     */
    public void setXCpl(
            final String xCpl) {
        this._xCpl = xCpl;
    }

    /**
     * Sets the value of field 'xLgr'. The field 'xLgr' has the
     * following description: Logradouro
     *
     * @param xLgr the value of field 'xLgr'.
     */
    public void setXLgr(
            final String xLgr) {
        this._xLgr = xLgr;
    }

    /**
     * Sets the value of field 'xMun'. The field 'xMun' has the
     * following description: Nome do município
     *
     * @param xMun the value of field 'xMun'.
     */
    public void setXMun(
            final String xMun) {
        this._xMun = xMun;
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
     * com.mercurio.lms.cte.model.v400.TEndeEmi
     */
    public static TEndeEmi unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TEndeEmi) org.exolab.castor.xml.Unmarshaller.unmarshal(TEndeEmi.class, reader);
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
