/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v103;

/**
 * Valores da prestação de serviço
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class VPresComp implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Valor Total da Prestação de Serviço Complementado
     */
    private java.lang.String _vTPrest;

    /**
     * Field _VPresCompSequence.
     */
    private com.mercurio.lms.cte.model.v103.VPresCompSequence _VPresCompSequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public VPresComp() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'VPresCompSequence'.
     * 
     * @return the value of field 'VPresCompSequence'.
     */
    public com.mercurio.lms.cte.model.v103.VPresCompSequence getVPresCompSequence(
    ) {
        return this._VPresCompSequence;
    }

    /**
     * Returns the value of field 'vTPrest'. The field 'vTPrest'
     * has the following description: Valor Total da Prestação de
     * Serviço Complementado
     * 
     * @return the value of field 'VTPrest'.
     */
    public java.lang.String getVTPrest(
    ) {
        return this._vTPrest;
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
     * Sets the value of field 'VPresCompSequence'.
     * 
     * @param VPresCompSequence the value of field
     * 'VPresCompSequence'.
     */
    public void setVPresCompSequence(
            final com.mercurio.lms.cte.model.v103.VPresCompSequence VPresCompSequence) {
        this._VPresCompSequence = VPresCompSequence;
    }

    /**
     * Sets the value of field 'vTPrest'. The field 'vTPrest' has
     * the following description: Valor Total da Prestação de
     * Serviço Complementado
     * 
     * @param vTPrest the value of field 'vTPrest'.
     */
    public void setVTPrest(
            final java.lang.String vTPrest) {
        this._vTPrest = vTPrest;
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
     * com.mercurio.lms.cte.model.v103.VPresComp
     */
    public static com.mercurio.lms.cte.model.v103.VPresComp unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.cte.model.v103.VPresComp) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.cte.model.v103.VPresComp.class, reader);
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
