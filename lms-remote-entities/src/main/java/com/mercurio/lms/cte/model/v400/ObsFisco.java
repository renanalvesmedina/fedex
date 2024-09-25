/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.cte.model.v400;

/**
 * Campo de uso livre do contribuinteInformar o nome do campo no
 * atributo xCampo e o conteúdo do campo no XTexto
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ObsFisco implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Identificação do campo
     */
    private String _xCampo;

    /**
     * Conteúdo do campo
     */
    private String _xTexto;


      //----------------/
     //- Constructors -/
    //----------------/

    public ObsFisco() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'xCampo'. The field 'xCampo' has
     * the following description: Identificação do campo
     *
     * @return the value of field 'XCampo'.
     */
    public String getXCampo(
    ) {
        return this._xCampo;
    }

    /**
     * Returns the value of field 'xTexto'. The field 'xTexto' has
     * the following description: Conteúdo do campo
     *
     * @return the value of field 'XTexto'.
     */
    public String getXTexto(
    ) {
        return this._xTexto;
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
     * Sets the value of field 'xCampo'. The field 'xCampo' has the
     * following description: Identificação do campo
     *
     * @param xCampo the value of field 'xCampo'.
     */
    public void setXCampo(
            final String xCampo) {
        this._xCampo = xCampo;
    }

    /**
     * Sets the value of field 'xTexto'. The field 'xTexto' has the
     * following description: Conteúdo do campo
     *
     * @param xTexto the value of field 'xTexto'.
     */
    public void setXTexto(
            final String xTexto) {
        this._xTexto = xTexto;
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
     * com.mercurio.lms.cte.model.v400.ObsFisco
     */
    public static ObsFisco unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (ObsFisco) org.exolab.castor.xml.Unmarshaller.unmarshal(ObsFisco.class, reader);
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
