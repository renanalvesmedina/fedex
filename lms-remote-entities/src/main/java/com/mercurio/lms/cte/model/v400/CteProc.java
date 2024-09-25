/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * CT-e processado
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class CteProc implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _versao.
     */
    private String _versao;

    /**
     * Field _CTe.
     */
    private CTe _CTe;

    /**
     * Field _protCTe.
     */
    private ProtCTe _protCTe;


      //----------------/
     //- Constructors -/
    //----------------/

    public CteProc() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'CTe'.
     *
     * @return the value of field 'CTe'.
     */
    public CTe getCTe(
    ) {
        return this._CTe;
    }

    /**
     * Returns the value of field 'protCTe'.
     *
     * @return the value of field 'ProtCTe'.
     */
    public ProtCTe getProtCTe(
    ) {
        return this._protCTe;
    }

    /**
     * Returns the value of field 'versao'.
     *
     * @return the value of field 'Versao'.
     */
    public String getVersao(
    ) {
        return this._versao;
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
     * Sets the value of field 'CTe'.
     *
     * @param CTe the value of field 'CTe'.
     */
    public void setCTe(
            final CTe CTe) {
        this._CTe = CTe;
    }

    /**
     * Sets the value of field 'protCTe'.
     *
     * @param protCTe the value of field 'protCTe'.
     */
    public void setProtCTe(
            final ProtCTe protCTe) {
        this._protCTe = protCTe;
    }

    /**
     * Sets the value of field 'versao'.
     *
     * @param versao the value of field 'versao'.
     */
    public void setVersao(
            final String versao) {
        this._versao = versao;
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
     * com.mercurio.lms.cte.model.v400.CteProc
     */
    public static CteProc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (CteProc) org.exolab.castor.xml.Unmarshaller.unmarshal(CteProc.class, reader);
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
