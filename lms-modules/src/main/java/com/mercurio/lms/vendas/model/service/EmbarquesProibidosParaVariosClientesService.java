package com.mercurio.lms.vendas.model.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.embarquesProibidosParaVariosClientesService"
 */

public class EmbarquesProibidosParaVariosClientesService {

	private Logger log = LogManager.getLogger(this.getClass());
	private PessoaService pessoaService;
	private ProibidoEmbarqueService proibidoEmbarqueService;
	
	/**
	 * Executa a Importacao arquivo TXT e retorna uma lista
	 * 
	 * @param parameters
	 * @return Map
	 */
	public Map executar(Map map) {
		String funcao = MapUtils.getString(map, "funcao");
		String descricao = MapUtils.getString(map, "descricao");
		List<Map> listGrid = (List<Map>)map.get("listGrid");
		Long paramIdMotivoProibidoEmbarque = MapUtils.getLong(map, "idMotivoProibidoEmbarque");
		YearMonthDay data = (YearMonthDay)map.get("dsData");
		
		// Usuario Bloqueio e Desbloqueio
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		StringWriter arquivoLog = new StringWriter();
		
		List<ProibidoEmbarque> listaProibidoEmbarque = new ArrayList<ProibidoEmbarque>();
		if ("B".equals(funcao)) {
			
			arquivoLog.append("Identificação;");
			arquivoLog.append("Data do Bloqueio;");
			arquivoLog.append("Motivo do Bloqueio;");
			arquivoLog.append("Resp. Bloqueio;");
			arquivoLog.append("\r\n");
			
			for (Map registro : listGrid) {
				String dtBloqueio = MapUtils.getString(registro, "dsDataBloqueio");
				if (dtBloqueio == null) {
					// MotivoProibidoEmbarque
					MotivoProibidoEmbarque motivoProibidoEmbarque = new MotivoProibidoEmbarque();
					motivoProibidoEmbarque.setIdMotivoProibidoEmbarque(paramIdMotivoProibidoEmbarque);
					ProibidoEmbarque proibidoEmbarque = new ProibidoEmbarque();
					proibidoEmbarque.setMotivoProibidoEmbarque(motivoProibidoEmbarque);
					proibidoEmbarque.setCliente(new Cliente(MapUtils.getLong(registro, "idPessoa")));
					proibidoEmbarque.setUsuarioByIdUsuarioBloqueio(usuario);
					proibidoEmbarque.setDtBloqueio(data);
					proibidoEmbarque.setDsBloqueio(descricao);

					listaProibidoEmbarque.add(proibidoEmbarque);
				} else {
					try {
						dtBloqueio =  JTDateTimeUtils.convertFrameworkDateToFormat(dtBloqueio,"dd/MM/yyyy");
					} catch (Exception e) {
						dtBloqueio = "";
					}
					
					arquivoLog.append(MapUtils.getString(registro, "tpIdentificacao")+MapUtils.getString(registro, "dsIdentificacao")+";");
					arquivoLog.append(dtBloqueio+";");
					arquivoLog.append(MapUtils.getString(registro, "dsMotivoBloqueio")+";");
					arquivoLog.append(MapUtils.getString(registro, "dsRespBloqueio")+";");
					arquivoLog.append("\r\n");
				}
			}
		} else {
			
			arquivoLog.append("Identificação;");
			arquivoLog.append("Data do Bloqueio;");
			arquivoLog.append("Motivo do Bloqueio;");
			arquivoLog.append("Resp. Bloqueio;");
			arquivoLog.append("\r\n");
			
			for (Map registro : listGrid) {
				String dataBloqueio = MapUtils.getString(registro, "dsDataBloqueio");
				Long idmotivoBloqueio = MapUtils.getLong(registro, "idMotivoProibidoEmbarque");
				Long idProibidoEmbarque = MapUtils.getLong(registro, "idProibidoEmbarque");
				if (dataBloqueio != null && idmotivoBloqueio.equals(paramIdMotivoProibidoEmbarque)) {
					ProibidoEmbarque proibidoEmbarque = proibidoEmbarqueService.findById(idProibidoEmbarque);
					proibidoEmbarque.setDtDesbloqueio(data);
					proibidoEmbarque.setUsuarioByIdUsuarioDesbloqueio(usuario);
					proibidoEmbarque.setDsDesbloqueio(descricao);
					listaProibidoEmbarque.add(proibidoEmbarque);
				} else {
					try {
						dataBloqueio =  JTDateTimeUtils.convertFrameworkDateToFormat(dataBloqueio,"dd/MM/yyyy");
					} catch (Exception e) {
						dataBloqueio = "";
					}
					arquivoLog.append(MapUtils.getString(registro, "tpIdentificacao")+MapUtils.getString(registro, "dsIdentificacao")+";");
					arquivoLog.append(dataBloqueio +";");
					if (! StringUtils.isBlank(dataBloqueio)) {
						arquivoLog.append(MapUtils.getString(registro, "dsMotivoBloqueio")+";");
						arquivoLog.append(MapUtils.getString(registro, "dsRespBloqueio")+";");
					}
					arquivoLog.append("\r\n");
				}
			}
		}
		
		if (!listaProibidoEmbarque.isEmpty()) {
			proibidoEmbarqueService.storeAll(listaProibidoEmbarque);
		}
		
		Map result = new HashMap();
		result.put("arquivoLog", arquivoLog.toString());
		result.put("nomeArquivoLog", MapUtils.getString(map, "dsFuncao")+"_"+JTDateTimeUtils.getDataHoraAtual().toString("yyyyMMddHHmm").toString()+".CSV");
		return result;
	}
	
	/**
	 * Executa a Importacao arquivo TXT e retorna uma lista
	 * 
	 * @param parameters
	 * @return Map
	 */
	public Map importarArquivoTXT(TypedFlatMap parameters) {
		
		Scanner sc = null;
		Map map = null;
		try {
			byte[] tmpArquivo = Base64Util.decode(MapUtils.getString(parameters, "arquivo"));
			//remove nome do arquivo
			byte[] arquivo = Arrays.copyOfRange(tmpArquivo, 1024, tmpArquivo.length);
			InputStream is = new ByteArrayInputStream(arquivo);
			sc = new Scanner(is).useDelimiter("[\r\n]+");
			map = this.processaArquivoTxt(sc);
		} catch	(Exception e) {
			log.error(e);
			throw new BusinessException("LMS-36274");
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
			} catch (Exception e) {	
				
			}
		}
		
		return map;
	}
	
	/**
	 * Processa o arquivo txt
	 * 
	 * @param sc
	 * @return Map
	 */
	private Map processaArquivoTxt(Scanner sc) {
		
		List<String> listaCnpjCpf = new ArrayList<String>();
		
		TypedFlatMap result = new TypedFlatMap();
		
		while (sc.hasNext()) {
			String cnpjCpf = sc.next();
			
			if (cnpjCpf.length() > 11) {
				listaCnpjCpf.add("('CNPJ','"+StringUtils.leftPad(cnpjCpf, 14, "0")+"')");
			} else {
				listaCnpjCpf.add("('CPF','"+StringUtils.leftPad(cnpjCpf, 11, "0")+"')");
			}

		}
		listaCnpjCpf = pessoaService.findPessoaByCnpjCpf(listaCnpjCpf);
		result.put("listaCnpjCpf", listaCnpjCpf);
		
		return result;
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public ProibidoEmbarqueService getProibidoEmbarqueService() {
		return proibidoEmbarqueService;
	}

	public void setProibidoEmbarqueService(
			ProibidoEmbarqueService proibidoEmbarqueService) {
		this.proibidoEmbarqueService = proibidoEmbarqueService;
	}
}
