/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100;

/**
 * Conhecimentos de Tranporte - usar este grupo quando for
 * prestador de serviço de transporte
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCTe implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Conhecimento Eletrônico - Chave de Acesso
     */
    private java.lang.String _chCTe;

    /**
     * Segundo código de barras
     */
    private java.lang.Object _segCodBarra;


      //----------------/
     //- Constructors -/
    //----------------/

    public InfCTe() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'chCTe'. The field 'chCTe' has
     * the following description: Conhecimento Eletrônico - Chave
     * de Acesso
     * 
     * @return the value of field 'ChCTe'.
     */
    public java.lang.String getChCTe(
    ) {
        return this._chCTe;
    }

    /**
     * Returns the value of field 'segCodBarra'. The field
     * 'segCodBarra' has the following description: Segundo código
     * de barras
     * 
     * @return the value of field 'SegCodBarra'.
     */
    public java.lang.Object getSegCodBarra(
    ) {
        return this._segCodBarra;
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
     * Sets the value of field 'chCTe'. The field 'chCTe' has the
     * following description: Conhecimento Eletrônico - Chave de
     * Acesso
     * 
     * @param chCTe the value of field 'chCTe'.
     */
    public void setChCTe(
            final java.lang.String chCTe) {
        this._chCTe = chCTe;
    }

    /**
     * Sets the value of field 'segCodBarra'. The field
     * 'segCodBarra' has the following description: Segundo código
     * de barras
     * 
     * @param segCodBarra the value of field 'segCodBarra'.
     */
    public void setSegCodBarra(
            final java.lang.Object segCodBarra) {
        this._segCodBarra = segCodBarra;
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
     * org.exolab.castor.builder.binding.InfCTe
     */
    public static com.mercurio.lms.mdfe.model.v100.InfCTe unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100.InfCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100.InfCTe.class, reader);
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
