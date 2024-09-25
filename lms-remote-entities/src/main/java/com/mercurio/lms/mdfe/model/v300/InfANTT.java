/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300;

/**
 * Grupo de informa��es para Ag�ncia Reguladora 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InfANTT implements java.io.Serializable {

    /**
     * Registro Nacional de Transportadores Rodoviários de Carga
     */
    private java.lang.String RNTRC;
   
    /**
     * Código Identificador da Operação de Transporte
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfCIOT> infCIOTList;
    
    /**
     * informa��es dos contratantes do servi�o de transporte
     */
    private java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfContratante> infContratanteList;
    
    /**
     * Informações de Vale Pedágio
     */
    private com.mercurio.lms.mdfe.model.v300.ValePed valePed;
    
    public InfANTT() {
        super();
        this.infCIOTList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfCIOT>();
        this.infContratanteList = new java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfContratante>();
    }
    
    /**
     * 
     * 
     * @param vInfCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCIOT(final com.mercurio.lms.mdfe.model.v300.InfCIOT vInfCIOT) throws java.lang.IndexOutOfBoundsException {
        this.infCIOTList.addElement(vInfCIOT);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfCIOT(final int index,final com.mercurio.lms.mdfe.model.v300.InfCIOT vInfCIOT) throws java.lang.IndexOutOfBoundsException {
        this.infCIOTList.add(index, vInfCIOT);
    }
    
    /**
     * Method enumerateInfCIOT.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.InfCIOT> enumerateInfCIOT() {
        return this.infCIOTList.elements();
    }
    
    /**
     * Method getInfCIOT.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public com.mercurio.lms.mdfe.model.v300.InfCIOT getInfCIOT(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infCIOTList.size()) {
            throw new IndexOutOfBoundsException("getInfCIOT: Index value '" + index + "' not in range [0.." + (this.infCIOTList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v300.InfCIOT) infCIOTList.get(index);
    }


    /**
     * Method getInfCIOTCount.
     * 
     * @return the size of this collection
     */
    public int getInfCIOTCount() {
        return this.infCIOTList.size();
    }
    
    /**
     * 
     * 
     * @param vInfContratante
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfContratante(final com.mercurio.lms.mdfe.model.v300.InfContratante vInfContratante) throws java.lang.IndexOutOfBoundsException {
        this.infContratanteList.addElement(vInfContratante);
    }

    /**
     * 
     * 
     * @param index
     * @param vInfContratante
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInfContratante(final int index,final com.mercurio.lms.mdfe.model.v300.InfContratante vInfContratante) throws java.lang.IndexOutOfBoundsException {
        this.infContratanteList.add(index, vInfContratante);
    }
    
    /**
     * Method enumerateInfContratante.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration<? extends com.mercurio.lms.mdfe.model.v300.InfContratante> enumerateInfContratante() {
        return this.infContratanteList.elements();
    }
    
    /**
     * Method getInfCIOT.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public com.mercurio.lms.mdfe.model.v300.InfContratante getInfContratante(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infContratanteList.size()) {
            throw new IndexOutOfBoundsException("getInfContratante: Index value '" + index + "' not in range [0.." + (this.infContratanteList.size() - 1) + "]");
        }

        return (com.mercurio.lms.mdfe.model.v300.InfContratante) infContratanteList.get(index);
    }


    /**
     * Method getInfContratanteCount.
     * 
     * @return the size of this collection
     */
    public int getInfContratanteCount() {
        return this.infContratanteList.size();
    }
    
    /**
     * Returns the value of field 'RNTRC'. The field 'RNTRC' has
     * the following description: Registro Nacional de
     * Transportadores Rodoviários de Carga
     * 
     * @return the value of field 'RNTRC'.
     */
    public java.lang.String getRNTRC() {
        return this.RNTRC;
    }

    /**
     * Returns the value of field 'valePed'. The field 'valePed'
     * has the following description: Informações de Vale
     * Pedágio
     * 
     * @return the value of field 'ValePed'.
     */
    public com.mercurio.lms.mdfe.model.v300.ValePed getValePed() {
        return this.valePed;
    }
    
    /**
     */
    public void removeAllInfCIOT() {
        this.infCIOTList.clear();
    }
    
    /**
     * Method removeInfCIOT.
     * 
     * @param vInfCIOT
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfCIOT(final java.lang.String vInfCIOT) {
        boolean removed = infCIOTList.remove(vInfCIOT);
        return removed;
    }

    /**
     * Method removeInfCIOTAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeInfCIOTAt(final int index) {
        java.lang.Object obj = this.infCIOTList.remove(index);
        return (java.lang.String) obj;
    }
    
    /**
     * 
     * 
     * @param index
     * @param vInfCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfCIOT(final int index,final com.mercurio.lms.mdfe.model.v300.InfCIOT vInfCIOT) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infCIOTList.size()) {
            throw new IndexOutOfBoundsException("setInfCIOT: Index value '" + index + "' not in range [0.." + (this.infCIOTList.size() - 1) + "]");
        }

        this.infCIOTList.set(index, vInfCIOT);
    }

    /**
     * 
     * 
     * @param vInfCIOTArray
     */
    public void setInfCIOT(final com.mercurio.lms.mdfe.model.v300.InfCIOT[] vInfCIOTArray) {
        //-- copy array
        infCIOTList.clear();

        for (int i = 0; i < vInfCIOTArray.length; i++) {
                this.infCIOTList.add(vInfCIOTArray[i]);
        }
    }
    
    /**
     */
    public void removeAllInfContratante() {
        this.infContratanteList.clear();
    }
    
    /**
     * Method removeInfContratante.
     * 
     * @param vInfContratante
     * @return true if the object was removed from the collection.
     */
    public boolean removeInfContratante(final java.lang.String vInfContratante) {
        boolean removed = infContratanteList.remove(vInfContratante);
        return removed;
    }

    /**
     * Method removeInfContratanteAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeInfContratanteAt(final int index) {
        java.lang.Object obj = this.infContratanteList.remove(index);
        return (java.lang.String) obj;
    }
    
    /**
     * 
     * 
     * @param index
     * @param vInfCIOT
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInfContratante(final int index,final com.mercurio.lms.mdfe.model.v300.InfContratante vInfContratante) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.infContratanteList.size()) {
            throw new IndexOutOfBoundsException("setInfContratante: Index value '" + index + "' not in range [0.." + (this.infContratanteList.size() - 1) + "]");
        }

        this.infContratanteList.set(index, vInfContratante);
    }

    /**
     * 
     * 
     * @param vInfCIOTArray
     */
    public void setInfContratante(final com.mercurio.lms.mdfe.model.v300.InfContratante[] vInfContratanteArray) {
        //-- copy array
        infContratanteList.clear();

        for (int i = 0; i < vInfContratanteArray.length; i++) {
                this.infContratanteList.add(vInfContratanteArray[i]);
        }
    }
    
    /**
     * Sets the value of field 'RNTRC'. The field 'RNTRC' has the
     * following description: Registro Nacional de Transportadores
     * Rodoviários de Carga
     * 
     * @param RNTRC the value of field 'RNTRC'.
     */
    public void setRNTRC(final java.lang.String RNTRC) {
        this.RNTRC = RNTRC;
    }

    /**
     * Sets the value of field 'valePed'. The field 'valePed' has
     * the following description: Informações de Vale Pedágio
     * 
     * @param valePed the value of field 'valePed'.
     */
    public void setValePed(final com.mercurio.lms.mdfe.model.v300.ValePed valePed) {
        this.valePed = valePed;
    }
    
	public java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfCIOT> getInfCIOTList() {
		return infCIOTList;
	}

	public void setInfCIOTList(
			java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfCIOT> vInfCIOTList) {
		infCIOTList = vInfCIOTList;
	}

    /**
     * Method getInfCIOT.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.InfCIOT[] getInfCIOT() {
    	com.mercurio.lms.mdfe.model.v300.InfCIOT[] array = new com.mercurio.lms.mdfe.model.v300.InfCIOT[0];
    	return this.infCIOTList.toArray(array);
    }
    
    public java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfContratante> getInfContratanteList() {
		return infContratanteList;
	}

	public void setInfContratanteList(
			java.util.Vector<com.mercurio.lms.mdfe.model.v300.InfContratante> vInfContratanteList) {
		infContratanteList = vInfContratanteList;
	}

    /**
     * Method getInfContratante.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.mercurio.lms.mdfe.model.v300.InfContratante[] getInfContratante() {
    	com.mercurio.lms.mdfe.model.v300.InfContratante[] array = new com.mercurio.lms.mdfe.model.v300.InfContratante[0];
    	return this.infContratanteList.toArray(array);
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
     * com.mercurio.lms.mdfe.model.v300.InfANTT
     */
    public static com.mercurio.lms.mdfe.model.v300.InfANTT unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.mercurio.lms.mdfe.model.v300.InfANTT) org.exolab.castor.xml.Unmarshaller.unmarshal(com.mercurio.lms.mdfe.model.v300.InfANTT.class, reader);
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
