package com.mercurio.lms.expedicao.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.financeiro.DoctoFaturaDMN;
import org.apache.commons.collections.MapUtils;

import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.gerarConhecimentoEletronicoXMLService"
 */
public class GerarConhecimentoEletronicoXMLService {
	
	private GerarConhecimentoEletronicoXML300Service gerarConhecimentoEletronicoXML300Service;
	private GerarConhecimentoEletronicoXML400Service gerarConhecimentoEletronicoXML400Service;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	
	public String gerarXML(final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico, Contingencia emContingencia) {
		EVersaoXMLCTE eVersaoXMLCTE = getVersaoCTEConhecimento(conhecimento);
		
		String xml;
		
		switch (eVersaoXMLCTE) {
			case VERSAO_300a:
				xml = gerarConhecimentoEletronicoXML300Service.gerarXML(conhecimento, monitoramentoDocEletronico, emContingencia);
				break;
			case VERSAO_400:
				xml = gerarConhecimentoEletronicoXML400Service.gerarXML(conhecimento, monitoramentoDocEletronico, emContingencia);
				break;
		default:
			throw new IllegalArgumentException("Versao do XML do CTE nao reconhecida pelo LMS");
		}
		
		return xml;
	}


	public String gerarXmlAbort(final Conhecimento conhecimento, final MonitoramentoDocEletronico monitoramentoDocEletronico) {
		EVersaoXMLCTE eVersaoXMLCTE = getVersaoCTEConhecimento(conhecimento);
		
		String xml;
		
		switch (eVersaoXMLCTE) {
		 	case VERSAO_300a:
				xml = gerarConhecimentoEletronicoXML300Service.gerarXmlAbort(monitoramentoDocEletronico);
				break;
			case VERSAO_400:
				xml = gerarConhecimentoEletronicoXML400Service.gerarXmlAbort(monitoramentoDocEletronico);
				break;
			default:
				throw new IllegalArgumentException("Versao do XML do CTE nao reconhecida pelo LMS");
		}
		
		return xml;
	}

	
	public String gerarChaveAcesso(final Conhecimento conhecimento, final Integer random) {
		EVersaoXMLCTE eVersaoXMLCTE = getVersaoCTEConhecimento(conhecimento);
		
		String chave;
		
		switch (eVersaoXMLCTE) {
			case VERSAO_300a:
				chave = gerarConhecimentoEletronicoXML300Service.gerarChaveAcesso(conhecimento, random);
				break;
			case VERSAO_400:
				chave = gerarConhecimentoEletronicoXML400Service.gerarChaveAcesso(conhecimento, random);
				break;
			default:
				throw new IllegalArgumentException("Versao do XML do CTE nao reconhecida pelo LMS");
		}
		
		return chave;
	}
	
	public Integer getRandom(final Conhecimento conhecimento) {
		EVersaoXMLCTE eVersaoXMLCTE = getVersaoCTEConhecimento(conhecimento);
		
		Integer random;
		
		switch (eVersaoXMLCTE) {
			case VERSAO_300a:
				random = GerarConhecimentoEletronicoXML300Service.getRandom();
				break;
			case VERSAO_400:
				random = GerarConhecimentoEletronicoXML400Service.getRandom();
				break;
			default:
				throw new IllegalArgumentException("Versao do XML do CTE nao reconhecida pelo LMS");
		}
		
		return random;
	}
	
	private EVersaoXMLCTE getVersaoCTEConhecimento(final Conhecimento conhecimento) {
		String conteudoParametro = String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), "Versão_XML_CTe", false));
		
		EVersaoXMLCTE eVersaoXMLCTE = EVersaoXMLCTE.getByConteudoParametro(conteudoParametro);
		return eVersaoXMLCTE;
	}
	
	public String findXmlCteComComplementos(Long idDoctoServico){
		Map<String,Object> map = integracaoNDDigitalService.findByDoctoServico(idDoctoServico);				
		return buildXmlCte(map);
	}

	public Map<String,String> findXmlCteComComplementosParaDropbox(Long idDoctoServico){
		Map<String,Object> map = integracaoNDDigitalService.findByDoctoServico(idDoctoServico);
		return buildXmlCteMap(map);
	}

	public String findXmlRpsComComplementosParaDropbox(Long idDoctoServico){
		return integracaoNDDigitalService.findRpsByDoctoServico(idDoctoServico);
	}

	public void findRpsComComplementosParaDropbox(DoctoFaturaDMN doctoFaturaDMN){
		//Vai setar retorno no objeto
		integracaoNDDigitalService.findRpsByDoctoFaturaDMN(doctoFaturaDMN);
	}

	private String buildXmlCte(Map<String, Object> map) {
		String result = null;
		if (null != map) {
			result = addComplementos((String) map.get("xml"), (Map) map.get("complementoXML"));
		}
		return result; 
	}
	
	private Map<String,String> buildXmlCteMap(Map<String, Object> map) {
		Map<String,String> result = null;
		if (null != map) {
			result = new HashMap<String, String>();
			result.put("xml",addComplementos((String) map.get("xml"), (Map) map.get("complementoXML")));
			result.put("xmlOriginal",(String) map.get("xmlOriginal"));
		}
		return result;
	}

	public void addListXmlCteComComplementos(List<Map<String,Object>> cteXML){
		for(Map<String,Object> map : cteXML ){
			map.put("xml",addComplementos((String)map.get("xml"),(Map)map.get("complementoXML")));
		}
	}
	
	public String addComplementos(String xml, Map complementos) {
		StringBuilder xmlBuild = new StringBuilder(xml);
		if( complementos != null ){
			StringBuilder sbTags = new StringBuilder();
			sbTags.append("<dsDivisaoCliente>" + MapUtils.getString(complementos,"dsDivisaoCliente","") + "</dsDivisaoCliente>");
			sbTags.append("<nrFatorCubagem></nrFatorCubagem>");
			sbTags.append("<nrFatorDensidade>" + MapUtils.getString(complementos,"nrFatorDensidade","") + "</nrFatorDensidade>");
			sbTags.append("<nrCotacao>" + MapUtils.getString(complementos,"nrCotacao","") + "</nrCotacao>");
			sbTags.append("<nmUsuarioLiberacao>" + MapUtils.getString(complementos,"nmUsuarioLiberacao","") + "</nmUsuarioLiberacao>");
			sbTags.append("<tpClienteTomador>" + MapUtils.getString(complementos,"tpClienteTomador","") + "</tpClienteTomador>");
			int index = xmlBuild.indexOf("</dadosAdic>");
			xmlBuild.insert(index,sbTags.toString());
		}
		return xmlBuild.toString();
	}
	
	public void setGerarConhecimentoEletronicoXML300Service(GerarConhecimentoEletronicoXML300Service gerarConhecimentoEletronicoXML300Service) {
		this.gerarConhecimentoEletronicoXML300Service = gerarConhecimentoEletronicoXML300Service;
	}
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setGerarConhecimentoEletronicoXML400Service(GerarConhecimentoEletronicoXML400Service gerarConhecimentoEletronicoXML400Service) {
		this.gerarConhecimentoEletronicoXML400Service = gerarConhecimentoEletronicoXML400Service;
	}

	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}
}
