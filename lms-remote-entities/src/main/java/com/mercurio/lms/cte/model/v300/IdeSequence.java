/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v300;

/**
 * Informar apenas
 * para tpEmis diferente de 1
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class IdeSequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Data e Hora da entrada em contingência
     */
    private java.lang.String _dhCont;

    /**
     * Justificativa da entrada em contingência
     */
    private java.lang.String _xJust;


      //----------------/
     //- Constructors -/
    //----------------/

    public IdeSequence() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dhCont'. The field 'dhCont' has
     * the following description: Data e Hora da entrada em
     * contingência
     * 
     * @return the value of field 'DhCont'.
     */
    public java.lang.String getDhCont(
    ) {
        return this._dhCont;
    }

    /**
     * Returns the value of field 'xJust'. The field 'xJust' has
     * the following description: Justificativa da entrada em
     * contingência
     * 
     * @return the value of field 'XJust'.
     */
    public java.lang.String getXJust(
    ) {
        return this._xJust;
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
     * Sets the value of field 'dhCont'. The field 'dhCont' has the
     * following description: Data e Hora da entrada em
     * contingência
     * 
     * @param dhCont the value of field 'dhCont'.
     */
    public void setDhCont(
            final java.lang.String dhCont) {
        this._dhCont = dhCont;
    }

    /**
     * Sets the value of field 'xJust'. The field 'xJust' has the
     * following description: Justificativa da entrada em
     * contingência
     * 
     * @param xJust the value of field 'xJust'.
     */
    public void setXJust(
            final java.lang.String xJust) {
        this._xJust = xJust;
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
     * com.mercurio.lms.cte.model.v300.IdeSequence
     */
    public static com.mercurio.lms.cte.model.v300.IdeSequence unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v300.IdeSequence) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v300.IdeSequence.class, reader);
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
