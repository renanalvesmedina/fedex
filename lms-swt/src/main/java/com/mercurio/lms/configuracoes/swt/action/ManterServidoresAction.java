/**
 * 
 */
package com.mercurio.lms.configuracoes.swt.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ServidorFilial;
import com.mercurio.lms.configuracoes.model.service.ServidorFilialService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author marcelosc
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.swt.manterServidoresAction"
 */
public class ManterServidoresAction extends CrudAction {

	ServidorFilialService servidorFilialService = null;

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = servidorFilialService.findPaginated(criteria);
		if (rsp != null) {
			List<HashMap<String, Object>> controles = rsp.getList();
			if (controles != null) {
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				for (HashMap<String, Object> controle : controles) {
					Map<String, Object> mapControle = new HashMap<String, Object>();
					
					mapControle.put("idServidorFilial", controle.get("idServidorFilial"));
					mapControle.put("nrIpInicial", FormatUtils.convertNumberToIp(BigInteger.valueOf((Long) controle.get("nrIpInicial"))));
					mapControle.put("nrIpFinal", FormatUtils.convertNumberToIp(BigInteger.valueOf((Long) controle.get("nrIpFinal"))));
					mapControle.put("nrIpServidor", FormatUtils.convertNumberToIp(BigInteger.valueOf((Long) controle.get("nrIpServidor"))));
					
					Map filial = (Map) controle.get("filial");
					mapControle.put("sgFilial", filial.get("sgFilial"));
					mapControle.put("idFilial", filial.get("idFilial"));
					
					result.add(mapControle);
				}
				rsp.setList(result);
			}
		}
		return rsp;
	}
	
	public Integer getRowCount(Map criteria) {
		return servidorFilialService.getRowCount(criteria);
	}
	
	public Map findById(Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		ServidorFilial servidorFilial = servidorFilialService.findById(id);
		
		result.put("idServidorFilial", servidorFilial.getIdServidorFilial());
		result.put("nrIpInicial", FormatUtils.convertNumberToIp(BigInteger.valueOf(servidorFilial.getNrIpInicial())));
		result.put("nrIpFinal", FormatUtils.convertNumberToIp(BigInteger.valueOf(servidorFilial.getNrIpFinal())));
		result.put("nrIpServidor", FormatUtils.convertNumberToIp(BigInteger.valueOf(servidorFilial.getNrIpServidor())));
		result.put("idFilial", servidorFilial.getFilial().getIdFilial());
		result.put("sgFilial", servidorFilial.getFilial().getSgFilial());
		result.put("nmFantasia", servidorFilial.getFilial().getPessoa().getNmFantasia());
		
		return result;
	}
	
	public Map store(Map data) {
		ServidorFilial bean = new ServidorFilial();
		
		Filial filial = new Filial();
		filial.setIdFilial((Long)data.get("idFilial"));
		bean.setFilial(filial);
		
		bean.setIdServidorFilial((Long) data.get("idServidorFilial"));
		bean.setNrIpInicial(FormatUtils.convertIpToNumber((String) data.get("nrIpInicial")).longValue());
		bean.setNrIpFinal(FormatUtils.convertIpToNumber((String) data.get("nrIpFinal")).longValue());
		bean.setNrIpServidor(FormatUtils.convertIpToNumber((String) data.get("nrIpServidor")).longValue());
		
		Long idServidorFilial = (Long) servidorFilialService.store(bean);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idServidorFilial", idServidorFilial);
		return result;
	}
	
	public void removeById(Long id) {
		servidorFilialService.removeById(id);
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		servidorFilialService.removeByIds(ids);
	}

	/**
	 * Busca o IP do servidor da Filial onde o IP do computador esta incluido (nrIpInicial <= IP <= nrIpFinal). 
	 * 
	 * @param criteria Map com a propriedade "ip" contendo o IP do computador.
	 * @return Map com a propriedade "ipServidor", contendo o IP do servidor da filial do usuario.
	 */
	public Map findServerByIp(Map criteria) {
		Map<String, String> map = new HashMap<String, String>();
		
		Long ip = FormatUtils.convertIpToNumber((String) criteria.get("ip")).longValue();
		ip = servidorFilialService.findServerByIp(ip);
		if(ip != null) {
			map.put("ipServidor", FormatUtils.convertNumberToIp(BigInteger.valueOf(ip)));
		}
		return map;
	}
	
	public Map findDadosSessao() {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Filial filial = SessionUtils.getFilialSessao();
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return retorno;
	}
	
	
	public Map<String, Object> executeCalculoFreteTabelaDiferenciada() {
		return new HashMap<String, Object>();
	}


	/**
	 * @return the manterServidoresService
	 */
	public ServidorFilialService getManterServidoresService() {
		return servidorFilialService;
	}

	/**
	 * @param manterServidoresService the manterServidoresService to set
	 */
	public void setManterServidoresService(
			ServidorFilialService manterServidoresService) {
		this.servidorFilialService = manterServidoresService;
	}
}