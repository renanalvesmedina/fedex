/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Class InfCteSubChoice.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCteSubChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tomador é contribuinte do ICMS
     */
    private com.mercurio.lms.cte.model.v103.TomaICMS _tomaICMS;

    /**
     * Tomador não é contribuinte do ICMS
     */
    private com.mercurio.lms.cte.model.v103.TomaNaoICMS _tomaNaoICMS;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCteSubChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'tomaICMS'. The field 'tomaICMS'
     * has the following description: Tomador é contribuinte do
     * ICMS
     * 
     * @return the value of field 'TomaICMS'.
     */
    public com.mercurio.lms.cte.model.v103.TomaICMS getTomaICMS(
    ) {
        return this._tomaICMS;
    }

    /**
     * Returns the value of field 'tomaNaoICMS'. The field
     * 'tomaNaoICMS' has the following description: Tomador não é
     * contribuinte do ICMS
     * 
     * @return the value of field 'TomaNaoICMS'.
     */
    public com.mercurio.lms.cte.model.v103.TomaNaoICMS getTomaNaoICMS(
    ) {
        return this._tomaNaoICMS;
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
     * Sets the value of field 'tomaICMS'. The field 'tomaICMS' has
     * the following description: Tomador é contribuinte do ICMS
     * 
     * @param tomaICMS the value of field 'tomaICMS'.
     */
    public void setTomaICMS(
            final com.mercurio.lms.cte.model.v103.TomaICMS tomaICMS) {
        this._tomaICMS = tomaICMS;
    }

    /**
     * Sets the value of field 'tomaNaoICMS'. The field
     * 'tomaNaoICMS' has the following description: Tomador não é
     * contribuinte do ICMS
     * 
     * @param tomaNaoICMS the value of field 'tomaNaoICMS'.
     */
    public void setTomaNaoICMS(
            final com.mercurio.lms.cte.model.v103.TomaNaoICMS tomaNaoICMS) {
        this._tomaNaoICMS = tomaNaoICMS;
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
     * com.mercurio.lms.cte.model.v103.InfCteSubChoice
     */
    public static com.mercurio.lms.cte.model.v103.InfCteSubChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.InfCteSubChoice) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.InfCteSubChoice.class, reader);
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
