package com.mercurio.lms.integracao.util;

import javax.xml.namespace.QName;

import com.mercurio.adsm.framework.integration.model.IRegistroGeral;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
public class MessageConverter {

	private XStream xstream;
	private StaxDriver xmlDriver;

	public MessageConverter() {
		xmlDriver = new StaxDriver();
		xstream = new XStream(new JVM().bestReflectionProvider(), xmlDriver);
		xstream.registerConverter(new StringConverter());
	}

	public Object fromMessage(String message) {
		return xstream.fromXML(message);
	}

	public String toMessage(Object object, String pi){
		this.doSetXSNamespace(object, pi);
		return xstream.toXML(object);
	}

	private void doSetXSNamespace(Object outputObj, String pi) {
		final QNameMap map = new QNameMap();
		/*TODO verificar o namespace do objeto   
		 * http://com.mercurio/lms/integration/pilmsc001e/jms
		 */
		final String namespace = outputObj instanceof IRegistroGeral ? "http://com.mercurio/lms/integration/"+pi.toLowerCase()+"/jms" : "";
		final QName name = new QName(namespace, outputObj.getClass().getName(), "");
		map.registerMapping(name, outputObj.getClass());
		xmlDriver.setQnameMap(map);
	}
}