/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Tipo Dados do Local de Retirada ou Entrega
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class TEndReEnt implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _TEndReEntChoice.
     */
    private TEndReEntChoice _TEndReEntChoice;

    /**
     * Razão Social ou Nome
     */
    private String _xNome;

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
     * Sigla da UF
     */
    private com.mercurio.lms.cte.model.v400.types.TUf _UF;


      //----------------/
     //- Constructors -/
    //----------------/

    public TEndReEnt() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'TEndReEntChoice'.
     *
     * @return the value of field 'TEndReEntChoice'.
     */
    public TEndReEntChoice getTEndReEntChoice(
    ) {
        return this._TEndReEntChoice;
    }

    /**
     * Returns the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     *
     * @return the value of field 'UF'.
     */
    public com.mercurio.lms.cte.model.v400.types.TUf getUF(
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
     * Returns the value of field 'xNome'. The field 'xNome' has
     * the following description: Razão Social ou Nome
     *
     * @return the value of field 'XNome'.
     */
    public String getXNome(
    ) {
        return this._xNome;
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
     * Sets the value of field 'TEndReEntChoice'.
     *
     * @param TEndReEntChoice the value of field 'TEndReEntChoice'.
     */
    public void setTEndReEntChoice(
            final TEndReEntChoice TEndReEntChoice) {
        this._TEndReEntChoice = TEndReEntChoice;
    }

    /**
     * Sets the value of field 'UF'. The field 'UF' has the
     * following description: Sigla da UF
     *
     * @param UF the value of field 'UF'.
     */
    public void setUF(
            final com.mercurio.lms.cte.model.v400.types.TUf UF) {
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
     * Sets the value of field 'xNome'. The field 'xNome' has the
     * following description: Razão Social ou Nome
     *
     * @param xNome the value of field 'xNome'.
     */
    public void setXNome(
            final String xNome) {
        this._xNome = xNome;
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
     * com.mercurio.lms.cte.model.v400.TEndReEnt
     */
    public static TEndReEnt unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (TEndReEnt) org.exolab.castor.xml.Unmarshaller.unmarshal(TEndReEnt.class, reader);
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
