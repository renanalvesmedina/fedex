/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.util.AwbUtils;

/**
 * @author Luis Carlos Poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.expedicao.digitarPreAWBObservacoesAction"
 */
public class DigitarPreAWBObservacoesAction extends CrudAction {
	
	public void storeInSession(TypedFlatMap data) {
		Awb awb = (Awb) AwbUtils.getAwbInSession();
		awb.setObAwb(data.getString("obAwb"));
		AwbUtils.setAwbInSession(awb);
	}
	
	public TypedFlatMap findInSession() {
		TypedFlatMap result = new TypedFlatMap();
		Awb awb = AwbUtils.getAwbInSession();
		result.put("obAwb", awb.getObAwb());
		return result;
	}

}
