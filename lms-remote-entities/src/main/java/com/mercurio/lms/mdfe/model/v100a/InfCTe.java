/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v100a;

/**
 * Conhecimentos de Tranporte - usar este grupo quando for
 * prestador de serviço de transporte
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfCTe implements java.io.Serializable {

    /**
     * Conhecimento Eletrônico - Chave de Acesso
     */
    private java.lang.String chCTe;

    /**
     * Segundo código de barras
     */
    private java.lang.String segCodBarra;

    /**
     * Informações das Unidades de Transporte
     * (Carreta/Reboque/Vagão)
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfUnidTransp> infUnidTranspList;

    public InfCTe() {
        super();
        this.infUnidTranspList = new java.util.Vector<com.mercurio.lms.mdfe.model.v100a.InfUnidTransp>();
    }

    /**
     * 
     * 
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidTransp(final com.mercurio.lms.mdfe.model.v100a.InfUnidTransp vInfUnidTransp) throws java.lang.IndexOutOfBoundsException {
        this.infUnidTranspList.addElement(vInfUnidTransp);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfUnidTransp(final int index,final com.mercurio.lms.mdfe.model.v100a.InfUnidTransp vInfUnidTransp) throws java.lang.IndexOutOfBoundsException {
        this.infUnidTranspList.add(index, vInfUnidTransp);
    }

    /**
     * Method enumerateInfUnidTransp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v100a.InfUnidTransp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v100a.InfUnidTransp> enumerateInfUnidTransp() {
        return this.infUnidTranspList.elements();
    }

    /**
     * Returns the value of field 'chCTe'. The field 'chCTe' has
     * the following description: Conhecimento Eletrônico - Chave
     * de Acesso
     * 
     * @return the value of field 'ChCTe'.
     */
    public java.lang.String getChCTe() {
        return this.chCTe;
    }

    /**
     * Method getInfUnidTransp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v100a.InfUnidTransp at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v100a.InfUnidTransp getInfUnidTransp(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("getInfUnidTransp: Index value '" + index + "' not in range [0.." + (this.infUnidTranspList.size() - 1) + "]");
        }

        return infUnidTranspList.get(index);
    }

    /**
     * Method getInfUnidTransp.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v100a.InfUnidTransp[] getInfUnidTransp() {
        com.mercurio.lms.mdfe.model.v100a.InfUnidTransp[] array = new com.mercurio.lms.mdfe.model.v100a.InfUnidTransp[0];
        return this.infUnidTranspList.toArray(array);
    }

    /**
     * Method getInfUnidTranspCount.
     * 
     * @return the size of this collection
     */
    public int getInfUnidTranspCount() {
        return this.infUnidTranspList.size();
    }

    /**
     * Returns the value of field 'segCodBarra'. The field
     * 'segCodBarra' has the following description: Segundo código
     * de barras
     * 
     * @return the value of field 'SegCodBarra'.
     */
    public java.lang.String getSegCodBarra() {
        return this.segCodBarra;
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
     */
    public void removeAllInfUnidTransp() {
        this.infUnidTranspList.clear();
    }

    /**
     * Method removeInfUnidTransp.
     * 
     * @param vInfUnidTransp
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfUnidTransp(final com.mercurio.lms.mdfe.model.v100a.InfUnidTransp vInfUnidTransp) {
        boolean removed = infUnidTranspList.remove(vInfUnidTransp);
        return removed;
    }

    /**
     * Method removeInfUnidTranspAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v100a.InfUnidTransp removeInfUnidTranspAt(final int index) {
        java.lang.Object obj = this.infUnidTranspList.remove(index);
        return (com.mercurio.lms.mdfe.model.v100a.InfUnidTransp) obj;
    }

    /**
     * Sets the value of field 'chCTe'. The field 'chCTe' has the
     * following description: Conhecimento Eletrônico - Chave de
     * Acesso
     * 
     * @param chCTe the value of field 'chCTe'.
     */
    public void setChCTe(final java.lang.String chCTe) {
        this.chCTe = chCTe;
    }

    /**
     * 
     * 
     * @param index
     * @param vInfUnidTransp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfUnidTransp(final int index,final com.mercurio.lms.mdfe.model.v100a.InfUnidTransp vInfUnidTransp) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infUnidTranspList.size()) {
            throw new IndexOutOfBoundsException("setInfUnidTransp: Index value '" + index + "' not in range [0.." + (this.infUnidTranspList.size() - 1) + "]");
        }

        this.infUnidTranspList.set(index, vInfUnidTransp);
    }

    /**
     * 
     * 
     * @param vInfUnidTranspArray
     */
    public void setInfUnidTransp(final com.mercurio.lms.mdfe.model.v100a.InfUnidTransp[] vInfUnidTranspArray) {
        //-- copy array
        infUnidTranspList.clear();

        for (int i = 0; i < vInfUnidTranspArray.length; i++) {
                this.infUnidTranspList.add(vInfUnidTranspArray[i]);
        }
    }

    /**
     * Sets the value of field 'segCodBarra'. The field
     * 'segCodBarra' has the following description: Segundo código
     * de barras
     * 
     * @param segCodBarra the value of field 'segCodBarra'.
     */
    public void setSegCodBarra(final java.lang.String segCodBarra) {
        this.segCodBarra = segCodBarra;
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
     * com.mercurio.lms.mdfe.model.v100a.InfCTe
     */
    public static com.mercurio.lms.mdfe.model.v100a.InfCTe unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v100a.InfCTe) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v100a.InfCTe.class, reader);
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
