package com.mercurio.lms.mww.model.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.ServidorFilial;
import com.mercurio.lms.configuracoes.model.service.ServidorFilialService;
import com.mercurio.lms.util.FormatUtils;
/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.mww.UpdaterUtilService"
 */
public class UpdaterUtilService {

	private ServidorFilialService servidorFilialService;
	
/**
 * Busca o caminho onde se encontram as atualizações do MWW na Filial do Usuário logado
 * @return
 */
	public String findPathToNewVersions(Long idFilial) {
		
		Long ipServidor = null;

		ServidorFilial servidorFilial =	findServidorFilial(idFilial);
		if (servidorFilial!=null){
			ipServidor = servidorFilial.getNrIpServidor();	
		
			String url = convertNumberToIp(BigInteger.valueOf(ipServidor));
			
			url = "http://"+ url + ":81/mww/";
			
			return url;
		}
		return "http://nt-swdep01.mercurio.local:81/mww/";
		
	}
  	
	private ServidorFilial findServidorFilial(Long idFilial){
		Map criteria = new HashMap();
		criteria.put("filial.idFilial", idFilial);
		List<ServidorFilial> lstServidorFilial = getServidorFilialService().find(criteria);
		if(lstServidorFilial==null || lstServidorFilial.isEmpty()){
			return null;
		}
		return lstServidorFilial.get(0);
	}

	private static String convertNumberToIp(BigInteger number) {

		if(number == null)
			throw new IllegalArgumentException("O número do ip não pode ser nulo.");
		
		String ip = new String();
		String fullBinaryIp = FormatUtils.convertNumberToBinary(number, 32);
		String binaryIp = null;
		for (int i = 0; i < 4; i++) {
			binaryIp = fullBinaryIp.substring(i * 8, (i + 1) * 8);
			ip = ip.concat(FormatUtils.convertBinaryToNumber(binaryIp).toString());
			if(i < 3) {
				ip = ip.concat(".");
			}
		}
		return ip;
	}


	/**
	 * @param servidorFilialService the servidorFilialService to set
	 */
	public void setServidorFilialService(ServidorFilialService servidorFilialService) {
		this.servidorFilialService = servidorFilialService;
	}


	/**
	 * @return the servidorFilialService
	 */
	public ServidorFilialService getServidorFilialService() {
		return servidorFilialService;
	}
	
}
