/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.3.3</a>, using an XML
 * Schema.
 * $Id$
 */

package com.mercurio.lms.mdfe.model.v300.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import com.mercurio.lms.mdfe.model.v300.InfLotacao;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
public class InfLotacaoDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;

    public InfLotacaoDescriptor() {
        super();
        _nsURI = "http://www.portalfiscal.inf.br/mdfe";
        _xmlName = "infLotacao";
        _elementDefinition = true;

        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors

        //-- initialize element descriptors

        //-- infLocalCarrega
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.mdfe.model.v300.InfLocalCarrega.class, "infLocalCarrega", "infLocalCarrega", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                InfLotacao target = (InfLotacao) object;
                return target.getInfLocalCarrega();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                	InfLotacao target = (InfLotacao) object;
                    target.setInfLocalCarrega( (com.mercurio.lms.mdfe.model.v300.InfLocalCarrega) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
            	return new com.mercurio.lms.mdfe.model.v300.InfLocalCarrega();
            }
        };
        desc.setSchemaType("com.mercurio.lms.mdfe.model.v300.InfLocalCarrega");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/mdfe");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: InfLocalCarrega
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        
      //-- infLocalDescarrega
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega.class, "infLocalDescarrega", "infLocalDescarrega", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                InfLotacao target = (InfLotacao) object;
                return target.getInfLocalDescarrega();
            }
            @Override
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                	InfLotacao target = (InfLotacao) object;
                    target.setInfLocalDescarrega( (com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            @Override
            @SuppressWarnings("unused")
            public java.lang.Object newInstance(java.lang.Object parent) {
            	return new com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega();
            }
        };
        desc.setSchemaType("com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega");
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.portalfiscal.inf.br/mdfe");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        //-- validation code for: InfLocalDescarrega
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        
    }

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    @Override()
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    @Override()
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    @Override()
    public java.lang.Class getJavaClass() {
        return com.mercurio.lms.mdfe.model.v300.InfLotacao.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    @Override()
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    @Override()
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    @Override()
    public java.lang.String getXMLName() {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
        return _elementDefinition;
    }

}
