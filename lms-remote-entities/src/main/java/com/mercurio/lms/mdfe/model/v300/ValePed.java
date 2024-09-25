/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações de Vale PedágioOutras informações sobre
 * Vale-Pedágio obrigatório que não tenham campos específicos
 * devem ser informadas no campo de observações gerais de uso
 * livre pelo contribuinte, visando atender as determinações
 * legais vigentes.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ValePed implements java.io.Serializable {

    /**
     * Informações dos dispositivos do Vale Pedágio
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.Disp> dispList;

    public ValePed() {
        super();
        this.dispList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.Disp>();
    }

    /**
     * 
     * 
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(final com.mercurio.lms.mdfe.model.v300.Disp vDisp) throws java.lang.IndexOutOfBoundsException {
        this.dispList.addElement(vDisp);
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDisp(final int index,final com.mercurio.lms.mdfe.model.v300.Disp vDisp) throws java.lang.IndexOutOfBoundsException {
        this.dispList.add(index, vDisp);
    }

    /**
     * Method enumerateDisp.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.Disp elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.Disp> enumerateDisp() {
        return this.dispList.elements();
    }

    /**
     * Method getDisp.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.Disp at the given index
     */
    public com.mercurio.lms.mdfe.model.v300.Disp getDisp(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.dispList.size()) {
            throw new IndexOutOfBoundsException("getDisp: Index value '" + index + "' not in range [0.." + (this.dispList.size() - 1) + "]");
        }

        return dispList.get(index);
    }

    /**
     * Method getDisp.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.Disp[] getDisp() {
        com.mercurio.lms.mdfe.model.v300.Disp[] array = new com.mercurio.lms.mdfe.model.v300.Disp[0];
        return this.dispList.toArray(array);
    }

    /**
     * Method getDispCount.
     * 
     * @return the size of this collection
     */
    public int getDispCount() {
        return this.dispList.size();
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
    public void removeAllDisp() {
        this.dispList.clear();
    }

    /**
     * Method removeDisp.
     * 
     * @param vDisp
     * @return true if the object was removed from the collection.
     */
    public boolean removeDisp(final com.mercurio.lms.mdfe.model.v300.Disp vDisp) {
        boolean removed = dispList.remove(vDisp);
        return removed;
    }

    /**
     * Method removeDispAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.Disp removeDispAt(final int index) {
        java.lang.Object obj = this.dispList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.Disp) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDisp
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDisp(final int index,final com.mercurio.lms.mdfe.model.v300.Disp vDisp) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.dispList.size()) {
            throw new IndexOutOfBoundsException("setDisp: Index value '" + index + "' not in range [0.." + (this.dispList.size() - 1) + "]");
        }

        this.dispList.set(index, vDisp);
    }

    /**
     * 
     * 
     * @param vDispArray
     */
    public void setDisp(final com.mercurio.lms.mdfe.model.v300.Disp[] vDispArray) {
        //-- copy array
        dispList.clear();

        for (int i = 0; i < vDispArray.length; i++) {
                this.dispList.add(vDispArray[i]);
        }
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
     * com.mercurio.lms.mdfe.model.v300.ValePed
     */
    public static com.mercurio.lms.mdfe.model.v300.ValePed unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.ValePed) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.ValePed.class, reader);
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
