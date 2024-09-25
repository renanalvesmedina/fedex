package com.mercurio.lms.expedicao.util;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

public class EmailCartaUtils {
	
	private EmailCartaUtils() {}
    
	public static String[] getEmails(TypedFlatMap criteria, String key){
    	return criteria.getString(key).split(";");
    }

	public static String buildObContato(final Contato contato) {
		return contato != null && contato.getObContato() != null ? contato.getObContato() : "";
	}
	
	public static String buildDepartamento(final Contato contato) {
		return contato != null && contato.getDsDepartamento() != null ? contato.getDsDepartamento() : "";
	}
	
	public static String buildRemetente(Contato contato, ParametroGeralService parametroGeralService) {
		if (contato == null || StringUtils.isBlank(contato.getDsEmail())){
			return (parametroGeralService.findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
		}
		return contato.getDsEmail();
	}
    
    public static String buildEmailSubject(String titulo, String notasFiscais, String tpDoctoServico, String sgfilialOrigem, String nrDoctoServico) {
		StringBuilder assunto = new StringBuilder();
		assunto.append(titulo);
		assunto.append(" - ");
		assunto.append(tpDoctoServico).append(" ");
		assunto.append(sgfilialOrigem).append(" ");
		assunto.append(nrDoctoServico);
		assunto.append(" - ").append(notasFiscais);
		return assunto.toString();
	}


}
