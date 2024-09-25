/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Informações do modal Rodoviário
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Rodo implements java.io.Serializable {

	
	private com.mercurio.lms.mdfe.model.v300.InfANTT InfANTT;
	
    /**
     * Dados do Veículo com a Tração
     */
    private com.mercurio.lms.mdfe.model.v300.VeicTracao veicTracao;

    /**
     * Dados dos reboques
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.VeicReboque> veicReboqueList;



    /**
     * Código de Agendamento no porto
     */
    private java.lang.String codAgPorto;

    public Rodo() {
        super();
        this.veicReboqueList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.VeicReboque>();
    }

    
    public com.mercurio.lms.mdfe.model.v300.InfANTT getInfANTT() {
		return InfANTT;
	}



	public void setInfANTT(com.mercurio.lms.mdfe.model.v300.InfANTT infANTT) {
		InfANTT = infANTT;
	}



	/**
     * 
     * 
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(final com.mercurio.lms.mdfe.model.v300.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this.veicReboqueList.addElement(vVeicReboque);
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVeicReboque(final int index,final com.mercurio.lms.mdfe.model.v300.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this.veicReboqueList.size() >= 3) {
            throw new IndexOutOfBoundsException("addVeicReboque has a maximum of 3");
        }

        this.veicReboqueList.add(index, vVeicReboque);
    }

    /**
     * Method enumerateVeicReboque.
     * 
     * @return an Enumeration over all
     * com.mercurio.lms.mdfe.model.v300.VeicReboque elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.VeicReboque> enumerateVeicReboque() {
        return this.veicReboqueList.elements();
    }

 

    /**
     * Returns the value of field 'codAgPorto'. The field
     * 'codAgPorto' has the following description: Código de
     * Agendamento no porto
     * 
     * @return the value of field 'CodAgPorto'.
     */
    public java.lang.String getCodAgPorto() {
        return this.codAgPorto;
    }

    /**
     * Method getVeicReboque.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.mercurio.lms.mdfe.model.v300.VeicReboque at the given
     * index
     */
    public com.mercurio.lms.mdfe.model.v300.VeicReboque getVeicReboque(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("getVeicReboque: Index value '" + index + "' not in range [0.." + (this.veicReboqueList.size() - 1) + "]");
        }

        return veicReboqueList.get(index);
    }

    /**
     * Method getVeicReboque.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.VeicReboque[] getVeicReboque() {
        com.mercurio.lms.mdfe.model.v300.VeicReboque[] array = new com.mercurio.lms.mdfe.model.v300.VeicReboque[0];
        return this.veicReboqueList.toArray(array);
    }

    /**
     * Method getVeicReboqueCount.
     * 
     * @return the size of this collection
     */
    public int getVeicReboqueCount() {
        return this.veicReboqueList.size();
    }

    /**
     * Returns the value of field 'veicTracao'. The field
     * 'veicTracao' has the following description: Dados do
     * Veículo com a Tração
     * 
     * @return the value of field 'VeicTracao'.
     */
    public com.mercurio.lms.mdfe.model.v300.VeicTracao getVeicTracao() {
        return this.veicTracao;
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
    public void removeAllVeicReboque() {
        this.veicReboqueList.clear();
    }

    /**
     * Method removeVeicReboque.
     * 
     * @param vVeicReboque
     * @return true if the object was removed from the collection.
     */
    public boolean removeVeicReboque(final com.mercurio.lms.mdfe.model.v300.VeicReboque vVeicReboque) {
        boolean removed = veicReboqueList.remove(vVeicReboque);
        return removed;
    }

    /**
     * Method removeVeicReboqueAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.mercurio.lms.mdfe.model.v300.VeicReboque removeVeicReboqueAt(final int index) {
        java.lang.Object obj = this.veicReboqueList.remove(index);
        return (com.mercurio.lms.mdfe.model.v300.VeicReboque) obj;
    }



    /**
     * Sets the value of field 'codAgPorto'. The field 'codAgPorto'
     * has the following description: Código de Agendamento no
     * porto
     * 
     * @param codAgPorto the value of field 'codAgPorto'.
     */
    public void setCodAgPorto(final java.lang.String codAgPorto) {
        this.codAgPorto = codAgPorto;
    }

    /**
     * 
     * 
     * @param index
     * @param vVeicReboque
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVeicReboque(final int index,final com.mercurio.lms.mdfe.model.v300.VeicReboque vVeicReboque) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.veicReboqueList.size()) {
            throw new IndexOutOfBoundsException("setVeicReboque: Index value '" + index + "' not in range [0.." + (this.veicReboqueList.size() - 1) + "]");
        }

        this.veicReboqueList.set(index, vVeicReboque);
    }

    /**
     * 
     * 
     * @param vVeicReboqueArray
     */
    public void setVeicReboque(final com.mercurio.lms.mdfe.model.v300.VeicReboque[] vVeicReboqueArray) {
        //-- copy array
        veicReboqueList.clear();

        for (int i = 0; i < vVeicReboqueArray.length; i++) {
                this.veicReboqueList.add(vVeicReboqueArray[i]);
        }
    }

    /**
     * Sets the value of field 'veicTracao'. The field 'veicTracao'
     * has the following description: Dados do Veículo com a
     * Tração
     * 
     * @param veicTracao the value of field 'veicTracao'.
     */
    public void setVeicTracao(final com.mercurio.lms.mdfe.model.v300.VeicTracao veicTracao) {
        this.veicTracao = veicTracao;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.mercurio.lms.mdfe.model.v300.Rod
     */
    public static com.mercurio.lms.mdfe.model.v300.Rodo unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.Rodo) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.Rodo.class, reader);
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
