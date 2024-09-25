package com.mercurio.lms.sgr.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.FilialEnquadramento;
import com.mercurio.lms.sgr.model.MunicipioEnquadramento;
import com.mercurio.lms.sgr.model.PaisEnquadramento;
import com.mercurio.lms.sgr.model.UnidadeFederativaEnquadramento;
import com.mercurio.lms.sgr.model.dao.ProcedimentoGerRiscoDAO;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.procedimentoGerRiscoService"
 */
public class ProcedimentoGerRiscoService {

	private static final int INDEX_TP_MANIFESTO = 11;
	private static final int INDEX_TP_PEDIDO_COLETA = 12;
	private static final int INDEX_ID_AWB = 13;

	private static final Short EVENTO_WORKFLOW_CEMOP = Short.valueOf("0501");
	
	private DomainValueService domainValueService;
	private ExigenciaFaixaValorService exigenciaFaixaValorService;
	private ConfiguracoesFacade configuracoesFacade;
	private FaixaDeValorService faixaDeValorService;
	private EnquadramentoRegraService enquadramentoRegraService;
	private ConversaoMoedaService conversaoMoedaService;
	private ControleCargaService controleCargaService;
	private ProcedimentoGerRiscoDAO procedimentoGerRiscoDAO;
	private WorkflowPendenciaService workflowPendenciaService;

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setExigenciaFaixaValorService(ExigenciaFaixaValorService exigenciaFaixaValorService) {
		this.exigenciaFaixaValorService = exigenciaFaixaValorService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setFaixaDeValorService(FaixaDeValorService faixaDeValorService) {
		this.faixaDeValorService = faixaDeValorService;
	}
	public void setEnquadramentoRegraService(EnquadramentoRegraService enquadramentoRegraService) {
		this.enquadramentoRegraService = enquadramentoRegraService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setProcedimentoGerRiscoDAO(ProcedimentoGerRiscoDAO dao) {
    	procedimentoGerRiscoDAO = dao;
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ProcedimentoGerRiscoDAO getProcedimentoGerRiscoDAO() {
        return this.procedimentoGerRiscoDAO;
    }


	//Rotina 1
	/**
	 * A partir de um Controle de Cargas de Viagem, identifica quais as exigências de gerenciamento de risco aplicáveis.
	 * 
	 * @param idControleCarga (required) 
	 * @param idMoeda (required) Moeda para a qual os valores devem ser convertidos.
	 * @param idPais (required) País relativo à conversão da moeda do parâmetro idMoeda.
	 * @return List composta por TypedFlatMap com a seguinte estrutura: tipoSeguro, idExigenciaGerRisco, dsResumida, dsCompleta, 
	 * nrNivel, idTipoExigenciaGerRisco, dsTipoExigenciaGerRisco
	 */
	public List generateExigenciasGerRiscoParaViagem(Long idControleCarga, Long idMoeda, Long idPais) {
		List result = generateIdentificarEnquadramentoRegrasGerRiscoParaViagem(idControleCarga, idMoeda, idPais, Boolean.FALSE);
		result = generateIdentificacaoExigenciasGerRisco(result);
		return result;
		
	}


	//Rotina 2
	/**
	 * Indentificar Exigências de Gerenciamento de Risco para Viagem.
	 * 
	 * @param idControleCarga (required) 
	 * @param idMoeda (required) Moeda para a qual os valores devem ser convertidos.
	 * @param idPais (required) País relativo à conversão da moeda do parâmetro idMoeda.
	 * @param blVerificacaoGeral (required) Se for "true" faz a soma de todos documentos de serviço para obter o total geral do 
	 * controle de cargas e um único enquadramento. Caso contrário, verifica isoladamente os registros com informações iguais obtendo
	 * um enquadramento para cada um deles.
	 * @return List composta por TypedFlatMap com a seguinte estrutura: idEnquadramentoRegra, dsEnquadramentoRegra, blSeguroMercurio, 
	 * valor, idFaixaDeValor, blRequerLiberacaoCemop
	 */
	public List generateIdentificarEnquadramentoRegrasGerRiscoParaViagem(Long idControleCarga, Long idMoeda, Long idPais, Boolean blVerificacaoGeral) {
		return generateIdentificarEnquadramentoRegrasGerRisco("V", idControleCarga, idMoeda, idPais, null, blVerificacaoGeral);
	}

	
	//Rotina 3
	/**
	 * Indentificar Exigências de Gerenciamento de Risco para Coleta/Entrega.
	 * 
	 * @param idControleCarga (required) 
	 * @param idMoeda (required) Moeda para a qual os valores devem ser convertidos.
	 * @param idPais (required) País relativo à conversão da moeda do parâmetro idMoeda.
	 * @param tpConteudo "A" - atual, "P" - previsto.
	 * @return List composta por TypedFlatMap com a seguinte estrutura: tipoSeguro, idExigenciaGerRisco, dsResumida, dsCompleta, 
	 * nrNivel, idTipoExigenciaGerRisco, dsTipoExigenciaGerRisco
	 */
	public List generateExigenciasGerRiscoParaColetaEntrega(Long idControleCarga, Long idMoeda, Long idPais, String tpConteudo) {
		List result = generateIdentificarEnquadramentoRegrasGerRiscoParaColetaEntrega(idControleCarga, idMoeda, idPais, tpConteudo, Boolean.FALSE);
		result = generateIdentificacaoExigenciasGerRisco(result);
		return result;
	}


	//Rotina 4
	/**
	 * Indentificar Enquadramento de Regras de Gerenciamento de Risco para Coleta/Entrega.
	 * 
	 * @param idControleCarga (required) 
	 * @param idMoeda (required) Moeda para a qual os valores devem ser convertidos.
	 * @param idPais (required) País relativo à conversão da moeda do parâmetro idMoeda.
	 * @param tpConteudo "A" - atual, "P" - previsto.
	 * @param blVerificacaoGeral (required) Se for "true" faz a soma de todos documentos de serviço para obter o total geral do 
	 * controle de cargas e um único enquadramento. Caso contrário, verifica isoladamente os registros com informações iguais obtendo
	 * um enquadramento para cada um deles.
	 * @return List
	 */
	public List generateIdentificarEnquadramentoRegrasGerRiscoParaColetaEntrega(Long idControleCarga, Long idMoeda, Long idPais, 
			String tpConteudo, Boolean blVerificacaoGeral) 
	{
		return generateIdentificarEnquadramentoRegrasGerRisco("C", idControleCarga, idMoeda, idPais, tpConteudo, blVerificacaoGeral); 
	}


	//Rotina 5
	/**
	 * Indentifica as Exigências de gerenciamento de risco para um conjunto de enquadramento de regras com faixas de valor.
	 * 
	 * @param lista (required) 
	 * @return List composta por TypedFlatMap com a seguinte estrutura: tipoSeguro, idExigenciaGerRisco, dsResumida, dsCompleta
	 * nrNivel, idTipoExigenciaGerRisco, dsTipoExigenciaGerRisco
	 */
	public List generateIdentificacaoExigenciasGerRisco(List lista) {
		List result = new ArrayList();

		String tipoSeguro = "";
		String tipoSeguroMercurio = configuracoesFacade.getMensagem("seguroMercurio");
		String tipoSeguroCliente = configuracoesFacade.getMensagem("seguroCliente");
		for (Iterator iter=lista.iterator(); iter.hasNext();) {
			TypedFlatMap map = (TypedFlatMap)iter.next();

			if (map.getBoolean("blSeguroMercurio").booleanValue())
				tipoSeguro = tipoSeguroMercurio;
			else
			if (!map.getBoolean("blSeguroMercurio").booleanValue())
				tipoSeguro = tipoSeguroCliente;

			List listExigencias = exigenciaFaixaValorService.findByFaixaDeValor(map.getLong("idFaixaDeValor"));
			for (Iterator iterExigencias = listExigencias.iterator(); iterExigencias.hasNext();) {
				ExigenciaFaixaValor exigenciaFaixaValor = (ExigenciaFaixaValor)iterExigencias.next();
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("idFaixaDeValor", map.getLong("idFaixaDeValor"));
				tfm.put("tipoSeguro", tipoSeguro);
				tfm.put("idExigenciaGerRisco", exigenciaFaixaValor.getExigenciaGerRisco().getIdExigenciaGerRisco());
				tfm.put("nrNivel", exigenciaFaixaValor.getExigenciaGerRisco().getNrNivel());
				if (exigenciaFaixaValor.getExigenciaGerRisco().getDsResumida() != null)
				tfm.put("dsResumida", exigenciaFaixaValor.getExigenciaGerRisco().getDsResumida().getValue());
				else
					tfm.put("dsResumida", "");
				if (exigenciaFaixaValor.getExigenciaGerRisco().getDsCompleta() != null)
				tfm.put("dsCompleta", exigenciaFaixaValor.getExigenciaGerRisco().getDsCompleta().getValue());
				else
					tfm.put("dsCompleta", "");
				tfm.put("idTipoExigenciaGerRisco", exigenciaFaixaValor.getExigenciaGerRisco().getTipoExigenciaGerRisco().getIdTipoExigenciaGerRisco());
				tfm.put("dsTipoExigenciaGerRisco", exigenciaFaixaValor.getExigenciaGerRisco().getTipoExigenciaGerRisco().getDsTipoExigenciaGerRisco().toString());
				result.add(tfm);
			}
		}
		if (result.isEmpty())
			return result;
		return ordenaFiltraListaExigenciasGerRisco(result);
	}



	/**
	 * 
	 * @param tpOperacao
	 * @param idControleCarga
	 * @param idMoeda
	 * @param idPais
	 * @param tpConteudo
	 * @param blVerificacaoGeral
	 * @return
	 */
	private List generateIdentificarEnquadramentoRegrasGerRisco(String tpOperacao, Long idControleCarga, Long idMoeda, Long idPais, 
			String tpConteudo, Boolean blVerificacaoGeral) 
	{
		validateTipoControleCarga(idControleCarga, tpOperacao);
		List listaDocumentos = findDocumentos(tpOperacao, idControleCarga, idMoeda, idPais, tpConteudo);

		// LMS-6850 - verifica manifesto de coleta/entrega aeroporto
		@SuppressWarnings("unchecked")
		Boolean blExclusivaAeroporto = verificaBlExclusivaAeroporto(listaDocumentos);

		if (blVerificacaoGeral) {
			resetaListaValores(listaDocumentos);
		}
		List listaEnquadramento = enquadramentoRegraService.findByExigenciasGerRisco(tpOperacao);

		List retorno = new ArrayList();
		List listaEnquadramentoSemExigencias = new ArrayList();

		int qtdEnquadramentoSemExigencias = 0;
		int qtdMostrarExcecao = 0;
		while (true) {
			List result = filtraGerRisco(tpOperacao, listaDocumentos, listaEnquadramento);
			List listaAgrupada = agrupaGerRisco(result, retorno, blExclusivaAeroporto);

			retorno.clear();
			listaEnquadramentoSemExigencias.clear();

			for (Iterator iter = listaAgrupada.iterator(); iter.hasNext();) {
				TypedFlatMap tfm = (TypedFlatMap)iter.next();
				if (tfm.getLong("idFaixaDeValor") == null) {
					listaEnquadramentoSemExigencias.add(tfm.getLong("idEnquadramentoRegra"));
					//Como o enquadramento encontrado não contém uma faixa de valor ou exigências então esse enquadramento é removido da lista de enquadramentos
					listaEnquadramento.remove(tfm.get("enquadramentoRegra"));
				}
				else
					retorno.add(tfm);
			}

			if (listaEnquadramentoSemExigencias.isEmpty())
				break;

			//Será mostrada a msg abaixo se a rotina entrar em loop.
			if (qtdEnquadramentoSemExigencias == listaEnquadramentoSemExigencias.size()) {
				if (qtdMostrarExcecao > 20) {
					throw new IllegalStateException("Ocorreu um erro ao executar a Identificação das Regras do Gerenciamento de Riscos!");
				}
				qtdMostrarExcecao++;
			}

			qtdEnquadramentoSemExigencias = listaEnquadramentoSemExigencias.size();
		}

		verificaRegraGeralParaRequerLiberacaoCemop(retorno);
		return retorno;
	}
	
	/**
	 * LMS-6850 - Verifica condição de manifesto de coleta/entrega aeroporto,
	 * conforme regras abaixo:
	 * 
	 * 1) se manifesto de entrega (<tt>MANIFESTO.TP_MANIFESTO = 'E'</tt>) com
	 *    AWB relacionado (<tt>MANIFESTO_ENTREGA_DOCUMENTO.ID_AWB</tt>); ou
	 * 
	 * 2) se manifesto de entrega (<tt>MANIFESTO.TP_MANIFESTO = 'C'</tt>) e
	 *    pedido de coleta for tipo aéreo
	 *    (<tt>PEDIDO_COLETA.TP_PEDIDO_COLETA = 'AE'</tt>).
	 * 
	 * @param documentos
	 * @return <tt>true</tt> se manifesto de coleta/entrega aeroporto,
	 *         <tt>false</tt> caso contrário
	 */
	private Boolean verificaBlExclusivaAeroporto(List<TypedFlatMap> documentos) {
		for (TypedFlatMap map : documentos) {
			String tpManifesto = map.getString("tpmanifesto");
			Long idAwb = map.getLong("idawb");
			String tpPedidoColeta = map.getString("tppedidocoleta");
			if (ConstantesGerRisco.TP_MANIFESTO_COLETA_ENTREGA.equals(tpManifesto) && idAwb != null
					|| ConstantesGerRisco.TP_OPERACAO_COLETA.equals(tpManifesto)
					&& ConstantesGerRisco.TP_PEDIDO_COLETA_AEROPORTO.equals(tpPedidoColeta)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Se nenhuma exigência requer liberação Cemop, então é verificado se para o somatório total da exigências existe uma faixa de
	 * valor para o enquadramento de regra geral.
	 * 
	 * @param retorno
	 */
	private void verificaRegraGeralParaRequerLiberacaoCemop(List retorno) {
		BigDecimal vlSoma = BigDecimalUtils.ZERO;
		for (Iterator iter = retorno.iterator(); iter.hasNext();) {
			TypedFlatMap tfm = (TypedFlatMap)iter.next();
			if (!tfm.getBoolean("blRequerLiberacaoCemop")) {
				vlSoma = vlSoma.add(tfm.getBigDecimal("valor"));
			}
			else {
				vlSoma = BigDecimalUtils.ZERO;
				break;
			}
		}
		if (vlSoma.compareTo(BigDecimalUtils.ZERO) != 0) {
			FaixaDeValor faixaDeValor = faixaDeValorService.findByEnquadramentoRegraAndValor(Long.valueOf(1), vlSoma, null);
	    	if (faixaDeValor != null && faixaDeValor.getBlRequerLiberacaoCemop()) {
	    		for (Iterator iter = retorno.iterator(); iter.hasNext();) {
	    			TypedFlatMap tfm = (TypedFlatMap)iter.next();
	    			tfm.put("blRequerLiberacaoCemop", faixaDeValor.getBlRequerLiberacaoCemop());
	    		}
			}
		}
	}


	/**
	 * 
	 * @param tpOperacao
	 * @param idControleCarga
	 * @param idMoeda
	 * @param idPais
	 * @param tpConteudo
	 * @return
	 */
	private List findDocumentos(String tpOperacao, Long idControleCarga, Long idMoeda, Long idPais, String tpConteudo) {
		if (tpOperacao.equals("C")) {
			return findDocumentosParaColetaEntrega(idControleCarga, idMoeda, idPais, tpConteudo);
		}
		return findDocumentosParaViagem(idControleCarga, idMoeda, idPais);
	}
	
	/**
	 * 
	 * @param tpOperacao
	 * @param listaDocumentos
	 * @param listaEnquadramento
	 * @param listaEnquadramentoSemExigencias
	 * @return
	 */
	private List filtraGerRisco(String tpOperacao, List listaDocumentos, List listaEnquadramento) {
		if (tpOperacao.equals("C")) {
			return filtraGerRiscoParaColetaEntrega(listaDocumentos, listaEnquadramento);
		}
		return filtraGerRiscoParaViagem(listaDocumentos, listaEnquadramento);
	}

	
	/**
	 * 
	 * @param listaValores
	 */
	private void resetaListaValores(List listaValores) {
		List novaListaValores = new ArrayList();
		for (Iterator iter = listaValores.iterator(); iter.hasNext();){
			TypedFlatMap tfm = (TypedFlatMap)iter.next();
			TypedFlatMap tfmNovo = new TypedFlatMap();
			tfmNovo.put("vlSoma", tfm.get("vlSoma"));
			tfmNovo.put("idMoeda", tfm.get("idMoeda"));
			novaListaValores.add(tfmNovo);
		}
		listaValores = novaListaValores;
	}


	
	/**
	 * 
	 * @param idControleCarga
	 * @param idMoeda
	 * @param idPais
	 * @return
	 */
    private List findDocumentosParaViagem(Long idControleCarga, Long idMoeda, Long idPais) {
    	List lista = getProcedimentoGerRiscoDAO().findDocumentosParaViagem(idControleCarga);

		List result = new ArrayList();
		for (Iterator iter = lista.iterator(); iter.hasNext();){
			Object[] obj = (Object[])iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("vlSoma", obj[0]);
			tfm.put("idMoeda", obj[1]);
			tfm.put("idClienteRemetente", obj[2]);
			tfm.put("idClienteDestinatario", obj[3]);
			tfm.put("idReguladoraSeguro", obj[4]);
			tfm.put("idReguladoraSeguroMercurio", obj[4]);
			tfm.put("idFilialOrigem", obj[5]);
			tfm.put("idFilialDestino", obj[6]);
			tfm.put("idMunicipioOrigem", obj[7]);
			tfm.put("idMunicipioDestino", obj[8]);
			tfm.put("idUfOrigem", obj[9]);
			tfm.put("idUfDestino", obj[10]);
			tfm.put("idPaisOrigem", obj[11]);
			tfm.put("idPaisDestino", obj[12]);
			tfm.put("idNaturezaProduto", obj[15]);

			if (obj[13] != null) {
				tfm.put("tpModal", domainValueService.findDomainValueByValue("DM_MODAL", (String)obj[13]));
			}	
			if (obj[14] != null){
				tfm.put("tpAbrangencia", domainValueService.findDomainValueByValue("DM_ABRANGENCIA", (String)obj[14]));
			}	
			if (obj[16] != null){
				tfm.put("tpVinculo", domainValueService.findDomainValueByValue("DM_TIPO_VINCULO_VEICULO", (String)obj[16]));
			}	
			if (idMoeda.compareTo( tfm.getLong("idMoeda") ) != 0) {
				BigDecimal novoValor = conversaoMoedaService.findConversaoMoeda(tfm.getLong("idPaisOrigem"), 
						tfm.getLong("idMoeda"), idPais, idMoeda, JTDateTimeUtils.getDataAtual(), tfm.getBigDecimal("vlSoma"));
				
				tfm.put("vlSoma", novoValor);
			}
   			result.add(tfm);
    	}
    	return result;
	}
       
	
	/**
	 * Para os registros com mesmo Tipo de Seguro e mesmo Tipo de Exigência retorna os que possuírem maior Nr_nivel. Se o nr_nível
	 * for igual retorna todos. 
	 * 
	 * @param lista
	 * @return lista ordenada pelo Tipo de Exigência
	 */
	private List ordenaFiltraListaExigenciasGerRisco(List lista) {
		if (lista.size() == 1)
			return lista;
		
		//CQPRO00028905
		Set killDuplicated = new HashSet(lista);
		lista = new ArrayList(killDuplicated);
		//CQPRO00028905 end
		
		Collections.sort(lista, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				TypedFlatMap map1 = (TypedFlatMap)obj1;
				TypedFlatMap map2 = (TypedFlatMap)obj2;
				int valor = map1.getString("tipoSeguro").compareTo( map2.getString("tipoSeguro") );
				if (valor == 0) {
					valor = map1.getVarcharI18n("dsTipoExigenciaGerRisco").getValue().compareToIgnoreCase( map2.getVarcharI18n("dsTipoExigenciaGerRisco").getValue() );
					if (valor == 0)
						valor = map1.getLong("nrNivel").compareTo( map2.getLong("nrNivel") );
				}
				return valor;
			}    		
    	});
		
		List retorno = new ArrayList();
		for (int i = 1; i < lista.size(); i++) {
			TypedFlatMap map1 = (TypedFlatMap)lista.get(i-1);
			TypedFlatMap map2 = (TypedFlatMap)lista.get(i);
			if (map1.getString("tipoSeguro").equals( map2.getString("tipoSeguro") ) &&
				map1.getVarcharI18n("dsTipoExigenciaGerRisco").getValue().compareTo( map2.getVarcharI18n("dsTipoExigenciaGerRisco").getValue()) == 0) 
			{
				continue;
			}
			retorno.add(map1);
		}
		retorno.add( lista.get(lista.size()-1) );
		
		List result = new ArrayList();
		for (int i = 1; i < retorno.size(); i++) {
			TypedFlatMap map1 = (TypedFlatMap)retorno.get(i-1);
			TypedFlatMap map2 = (TypedFlatMap)retorno.get(i);
			if (map1.getLong("nrNivel").equals(map2.getLong("nrNivel")) &&
				map1.getVarcharI18n("dsTipoExigenciaGerRisco").getValue().compareTo( map2.getVarcharI18n("dsTipoExigenciaGerRisco").getValue()) == 0) 
			{
				continue;
			}
			result.add(map1);
		}
		result.add( retorno.get(retorno.size()-1) );

		return result;
	}


	/**
	 * 
	 * @param retorno
	 * @return
	 */
	private List agrupaGerRisco(List lista, List retorno, Boolean blExclusivaAeroporto) {
		Collections.sort(lista, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				EnquadramentoRegra er1 = (EnquadramentoRegra)((Map)obj1).get("enquadramentoRegra");
				EnquadramentoRegra er2 = (EnquadramentoRegra)((Map)obj2).get("enquadramentoRegra");
        		return er1.getIdEnquadramentoRegra().compareTo(er2.getIdEnquadramentoRegra());
			}    		
    	});

		Long idEnquadramentoRegra = null;
		BigDecimal valor = BigDecimalUtils.ZERO;

		TypedFlatMap tfmGerRisco = new TypedFlatMap();
		boolean blUltimo = false;

		List listaAgrupada = new ArrayList();
		for (int i=0; i < lista.size(); i++) {
			Map map = (Map)lista.get(i);
			EnquadramentoRegra er = (EnquadramentoRegra)map.get("enquadramentoRegra");
			blUltimo = (i == lista.size() - 1);

			if (i==0) {
				idEnquadramentoRegra = er.getIdEnquadramentoRegra();
				tfmGerRisco.putAll(criarMapGerRisco(er));
			}
			if (er.getIdEnquadramentoRegra().compareTo(idEnquadramentoRegra) != 0) {
				tfmGerRisco.put("valor",  valor);
				// LMS-6850 - inclui verificação de coleta/entrega aeroporto
				tfmGerRisco.put("blExclusivaAeroporto", blExclusivaAeroporto);
				listaAgrupada.add(tfmGerRisco);

				valor = (BigDecimal)map.get("vlSoma");
				idEnquadramentoRegra = er.getIdEnquadramentoRegra();
				tfmGerRisco = new TypedFlatMap();
				tfmGerRisco.putAll(criarMapGerRisco(er));
			}
			else
				valor = valor.add( map.get("vlSoma") == null ? BigDecimalUtils.ZERO : (BigDecimal)map.get("vlSoma") );

			if (blUltimo) {
				tfmGerRisco.put("valor",  valor);
				listaAgrupada.add(tfmGerRisco);
			}
		}

		listaAgrupada.addAll(retorno);

		Collections.sort(listaAgrupada, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				Long id1 = ((TypedFlatMap)obj1).getLong("idEnquadramentoRegra");
				Long id2 = ((TypedFlatMap)obj2).getLong("idEnquadramentoRegra");
        		return id1.compareTo(id2);
			}    		
    	});

		valor = BigDecimalUtils.ZERO;
		List novaLista = new ArrayList();
		for (int i=0; i < listaAgrupada.size(); i++) {
			TypedFlatMap tfm = (TypedFlatMap)listaAgrupada.get(i);
			blUltimo = (i == listaAgrupada.size() - 1);

			if (i==0) {
				idEnquadramentoRegra = tfm.getLong("idEnquadramentoRegra");
			}
			if (tfm.getLong("idEnquadramentoRegra").compareTo(idEnquadramentoRegra) != 0) {
				TypedFlatMap tfmAnterior = (TypedFlatMap)listaAgrupada.get(i-1);
				tfmAnterior.put("valor",  valor);
				populateFaixaValoresByGerRisco(idEnquadramentoRegra, tfmAnterior);
				novaLista.add(tfmAnterior);

				valor = tfm.getBigDecimal("valor");
				idEnquadramentoRegra = tfm.getLong("idEnquadramentoRegra");
			}
			else
				valor = valor.add(tfm.getBigDecimal("valor"));

			if (blUltimo) {
				tfm.put("valor",  valor);
				populateFaixaValoresByGerRisco(idEnquadramentoRegra, tfm);
				novaLista.add(tfm);
			}
		}
		return novaLista;
	}

	/**
	 * 
	 * @param idEnquadramentoRegra
	 * @param valor
	 * @param mapGerRisco
	 * @param er
	 * @return
	 */
	private void populateFaixaValoresByGerRisco(Long idEnquadramentoRegra, TypedFlatMap mapDados) {
		FaixaDeValor faixaDeValor = faixaDeValorService.validateByEnquadramentoRegraAndValor(
				idEnquadramentoRegra,
				mapDados.getVarcharI18n("dsEnquadramentoRegra").toString(),
				mapDados.getBigDecimal("valor"),
				mapDados.getBoolean("blExclusivaAeroporto"));

		if (faixaDeValor == null || faixaDeValor.getExigenciaFaixaValors() == null || faixaDeValor.getExigenciaFaixaValors().isEmpty()) {
			mapDados.put("idFaixaDeValor", null);
			mapDados.put("blRequerLiberacaoCemop", null);
		}
		else {
			mapDados.put("idFaixaDeValor", faixaDeValor.getIdFaixaDeValor());
			mapDados.put("blRequerLiberacaoCemop", faixaDeValor.getBlRequerLiberacaoCemop());
		}
	}

	/**
	 * 
	 * @param er
	 * @return
	 */
	private TypedFlatMap criarMapGerRisco(EnquadramentoRegra er) {
		TypedFlatMap mapDados = new TypedFlatMap();
		mapDados.put("idEnquadramentoRegra", er.getIdEnquadramentoRegra());
		mapDados.put("dsEnquadramentoRegra", er.getDsEnquadramentoRegra());
		mapDados.put("blSeguroMercurio", er.getBlSeguroMercurio());
		mapDados.put("enquadramentoRegra", er);
		return mapDados;
	}


	/**
	 * Valida o tipo do controle de carga passado por parâmetro.
	 * 
	 * @param idControleCarga
	 * @param valorTpControleCarga
	 */
	private void validateTipoControleCarga(Long idControleCarga, String valorTpControleCarga) {
		DomainValue tpControleCarga = controleCargaService.findTipoControleCarga(idControleCarga);
		if (valorTpControleCarga.equals("V")) {
			if (!tpControleCarga.getValue().equals(valorTpControleCarga))
				throw new BusinessException("LMS-11006");
		}
		else
			if (valorTpControleCarga.equals("C")) {
				if (!tpControleCarga.getValue().equals(valorTpControleCarga))
					throw new BusinessException("LMS-11007");
			}
	}


	/**
	 * @param result
	 * @param idMoeda
	 * @param idPais
	 * @return
	 */
	private List findDocumentosParaColetaEntrega(Long idControleCarga, Long idMoeda, Long idPais, String tpConteudo) {
		List result = getProcedimentoGerRiscoDAO().findDocumentosParaColetaEntrega(idControleCarga, tpConteudo);

		List retorno = new ArrayList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Object[] obj = (Object[])iter.next();
    		TypedFlatMap map = new TypedFlatMap();
			map.put("vlSoma", obj[0]);
			map.put("tpGrauRisco", obj[1]);
			map.put("idMoeda", obj[2]);
			map.put("idPais", obj[3]);
			map.put("idClienteDestinatario", obj[4]);
			map.put("idReguladoraSeguro", obj[5]);
			map.put("idReguladoraSeguroMercurio", obj[5]);
			map.put("idNaturezaProduto", obj[6]);
			map.put("idFilial", obj[7]);
			map.put("tpModal", obj[8]);
			map.put("tpVinculo", obj[9]);
			map.put("idClienteRemetente", obj[10]);

			// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
			map.put("tpmanifesto", obj[INDEX_TP_MANIFESTO]);
			map.put("tppedidocoleta", obj[INDEX_TP_PEDIDO_COLETA]);
			map.put("idawb", obj[INDEX_ID_AWB]);

			Long idMoedaCc = map.getLong("idMoeda");
			if (idMoedaCc != null && idMoeda.compareTo(idMoedaCc) != 0) {
				BigDecimal novoValor = conversaoMoedaService.findConversaoMoeda(
					map.getLong("idPais"), idMoedaCc, idPais, idMoeda, JTDateTimeUtils.getDataAtual(), map.getBigDecimal("vlSoma"));

				map.put("vlSoma", novoValor);
			}
			retorno.add(map);
		}		
		return retorno;
	}
	
	
	
	/**
	 * 
	 * @param listaValores
	 * @param listaEnquadramento
	 */
	private List filtraGerRiscoParaViagem (List documentos, final List enquadramentos) {
		List resultado = new ArrayList();

		Long idClienteRemetente = null;
		Long idClienteDestinatario = null;
		Long idReguladoraSeguro = null;
		Boolean blSeguroMercurio = null;
		Long idNaturezaProduto = null;
		Long idFilialOrigem = null;
		Long idFilialDestino = null;
		Long idPaisOrigem = null;
		Long idPaisDestino = null;
		Long idUfOrigem = null;
		Long idUfDestino = null;
		Long idMunicipioOrigem = null;
		Long idMunicipioDestino = null;
		String tpModal = null;
		String tpAbrangencia = null;
		String tpVinculo = null;
		BigDecimal vlSoma = null;

		for (Iterator iter = documentos.iterator(); iter.hasNext();) {
			List temporaria = new ArrayList();

			TypedFlatMap documento = (TypedFlatMap)iter.next();

			idClienteRemetente = documento.getLong("idClienteRemetente");
			idClienteDestinatario = documento.getLong("idClienteDestinatario");
			idReguladoraSeguro = documento.getLong("idReguladoraSeguro");
			blSeguroMercurio = documento.getLong("idReguladoraSeguroMercurio") == null? true:false;
			idNaturezaProduto = documento.getLong("idNaturezaProduto");
			idFilialOrigem = documento.getLong("idFilialOrigem");
			idFilialDestino = documento.getLong("idFilialDestino");
			idPaisOrigem = documento.getLong("idPaisOrigem");
			idPaisDestino = documento.getLong("idPaisDestino");
			idUfOrigem = documento.getLong("idUfOrigem");
			idUfDestino = documento.getLong("idUfDestino");
			idMunicipioOrigem = documento.getLong("idMunicipioOrigem");
			idMunicipioDestino = documento.getLong("idMunicipioDestino");
			tpModal = null;
			if( documento.getDomainValue("tpModal") != null ){
			tpModal = (documento.getDomainValue("tpModal")).getValue();
			}
			tpAbrangencia = null;
			if( documento.getDomainValue("tpAbrangencia") != null ){
			tpAbrangencia = (documento.getDomainValue("tpAbrangencia")).getValue();
			}
			tpVinculo = null;
			if (documento.getDomainValue("tpVinculo") != null) {
				tpVinculo = (documento.getDomainValue("tpVinculo")).getValue();
			}
			vlSoma = documento.getBigDecimal("vlSoma");

			int c = 0;
			while (c < 30) {
				c++;

				temporaria = filtraCliente(enquadramentos, idClienteRemetente, idClienteDestinatario);
				if (temporaria.isEmpty()) {
					continue;
				}

				temporaria = filtraReguladoraSeguroByBlSeguroMercurio(temporaria, enquadramentos, blSeguroMercurio);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraReguladoraSeguro(temporaria, enquadramentos, idReguladoraSeguro);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraNaturezaProduto(temporaria, enquadramentos, idNaturezaProduto);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominio(temporaria, enquadramentos, tpVinculo, "tpVinculo");
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraFilialOrigem(temporaria, enquadramentos, idFilialOrigem);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraPaisOrigem(temporaria, enquadramentos, idPaisOrigem);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraPaisDestino(temporaria, enquadramentos, idPaisDestino);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraUfOrigem(temporaria, enquadramentos, idUfOrigem);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraUfDestino(temporaria, enquadramentos, idUfDestino);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraFilialDestino(temporaria, enquadramentos, idFilialDestino);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraMunicipioOrigem(temporaria, enquadramentos, idMunicipioOrigem);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraMunicipioDestino(temporaria, enquadramentos, idMunicipioDestino);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominio(temporaria, enquadramentos, tpModal, "tpModal");
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominioUltimoTeste(temporaria, enquadramentos, tpAbrangencia, "tpAbrangencia");
				if (temporaria.isEmpty()) {
				continue;
			}
			}
			;

			for (Object object : temporaria) {
				resultado.add(mountEnquadramentoMap(object,vlSoma));
			}
		}
		return resultado;
	}


	/**
	 * 
	 * @param documentos
	 * @param listaEnquadramento
	 */
	private List filtraGerRiscoParaColetaEntrega (List documentos, final List enquadramentos) {
		List resultado = new ArrayList();
		
		BigDecimal vlSoma = null;
		String tpGrauRisco = null;
		Long idClienteDestinatario = null;
		Long idReguladoraSeguro = null;
		Boolean blSeguroMercurio = null;
		Long idNaturezaProduto = null;
		Long idFilial = null;
		String tpModal = null;
		String tpVinculo = null;
		Long idClienteRemetente = null;

		for (Iterator iter = documentos.iterator(); iter.hasNext();) {
			List temporaria = new ArrayList();

			TypedFlatMap documento = (TypedFlatMap)iter.next();

			vlSoma = documento.getBigDecimal("vlSoma");
			tpGrauRisco = documento.getString("tpGrauRisco");
			idClienteDestinatario = documento.getLong("idClienteDestinatario");
			blSeguroMercurio = documento.getLong("idReguladoraSeguroMercurio")==null?true:false;
			idNaturezaProduto = documento.getLong("idNaturezaProduto");
			idFilial = documento.getLong("idFilial");
			tpModal = documento.getString("tpModal");
			tpVinculo = documento.getString("tpVinculo");
			idClienteRemetente = documento.getLong("idClienteRemetente");
		
			int c = 0;
			while (c < 16) {
				c++;
				
				temporaria = filtraCliente(enquadramentos, idClienteRemetente, idClienteDestinatario);
				if (temporaria.isEmpty()) {
				continue;
				}

				temporaria = filtraReguladoraSeguroByBlSeguroMercurio(temporaria, enquadramentos, blSeguroMercurio);
				if (temporaria.isEmpty()) {
					continue;
			}

				temporaria = filtraReguladoraSeguro(temporaria, enquadramentos, idReguladoraSeguro);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraNaturezaProduto(temporaria, enquadramentos, idNaturezaProduto);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominio(temporaria, enquadramentos, tpVinculo, "tpVinculo");
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraFilialOrigem(temporaria, enquadramentos, idFilial);
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominio(temporaria, enquadramentos, tpGrauRisco, "tpGrauRiscoColetaEntrega");
				if (temporaria.isEmpty()) {
				continue;
			}

				temporaria = filtraTpDominioUltimoTeste(temporaria, enquadramentos, tpModal, "tpModal");
				if (temporaria.isEmpty()) {
				continue;
			}
			}
			;

			if (temporaria.isEmpty()) {
				continue;
			}

			for (Object object : temporaria) {
				resultado.add(mountEnquadramentoMap(object, vlSoma));
			}
		}
		
		return resultado;
	}


	/**
	 * 
	 * @param listaResultado
	 * @param listaEnquadramento
	 * @param vlSoma
	 * @return 
	 */
	private Map mountEnquadramentoMap(Object enquadramento, BigDecimal vlSoma) {
		Map mapResultado = new HashMap();
		mapResultado.put("enquadramentoRegra", enquadramento);
		mapResultado.put("vlSoma", vlSoma);
		return mapResultado;
	}



	// REGRAS RESPONSÁVEIS POR APLICAR OS FILTROS NA LISTA
	private List filtraCliente(List listaEnquadramento, Long idCliente) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramento, new PredicateToIdCliente(idCliente), novaLista);
		if (novaLista.isEmpty() && idCliente != null) {
			CollectionUtils.select(listaEnquadramento, new PredicateToIdCliente(null), novaLista);
		}

		return novaLista;
		}



	private List filtraCliente(List listaEnquadramento, Long idClienteRemetente, Long idClienteDestinatario) {
		List novaLista = new ArrayList();
		if (idClienteRemetente != null) {
			CollectionUtils.select(listaEnquadramento, new PredicateToIdCliente(idClienteRemetente), novaLista);
		}
		if (idClienteDestinatario != null && novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramento, new PredicateToIdCliente(idClienteDestinatario), novaLista);
		}
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramento, new PredicateToIdCliente(null), novaLista);
		}

		return novaLista;
		}


	private List filtraReguladoraSeguroByBlSeguroMercurio(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Boolean blSeguroMercurio) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdReguladoraSeguroByBlSeguroMercurio(blSeguroMercurio), novaLista);
		if (novaLista.isEmpty()) {
			listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}

		return novaLista;
		}


	private List filtraReguladoraSeguro(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idReguladoraSeguro) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdReguladoraSeguro(idReguladoraSeguro), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdReguladoraSeguro(null), novaLista);

			if (novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
	}

		return novaLista;
	}


	private List filtraNaturezaProduto(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idNaturezaProduto) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdNaturezaProduto(idNaturezaProduto), novaLista);
		if (novaLista.isEmpty() && idNaturezaProduto != null) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdNaturezaProduto(null), novaLista);

			if (novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
	}

		return novaLista;
	}
	
	
	private List filtraFilialOrigem(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idFilialOrigem) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdFilialOrigem(idFilialOrigem), novaLista);

		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdFilialOrigem(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
	}

		return novaLista;
	}


	private List filtraFilialDestino(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idFilialDestino) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdFilialDestino(idFilialDestino), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdFilialDestino(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraPaisOrigem(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idPaisOrigem) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdPaisOrigem(idPaisOrigem), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdPaisOrigem(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraPaisDestino(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idPaisDestino) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdPaisDestino(idPaisDestino), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdPaisDestino(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraUfOrigem(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idUfOrigem) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdUfOrigem(idUfOrigem), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdUfOrigem(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraUfDestino(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idUfDestino) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdUfDestino(idUfDestino), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdUfDestino(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraMunicipioOrigem(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idMunicipioOrigem) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdMunicipioOrigem(idMunicipioOrigem), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdMunicipioOrigem(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraMunicipioDestino(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, Long idMunicipioDestino) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdMunicipioDestino(idMunicipioDestino), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToIdMunicipioDestino(null), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}


	private List filtraTpDominio(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, String valorTpModal, String tipoDominio) {
		List novaLista = new ArrayList();
		CollectionUtils.select(listaEnquadramentoAtual, new PredicateToTpDominio(valorTpModal, tipoDominio), novaLista);
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToTpDominio(null, tipoDominio), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}

		return novaLista;
		}
	
	
	private List filtraTpDominioUltimoTeste(List listaEnquadramentoAtual, List listaEnquadramentoOriginal, String valorTp, String tipoDominio) {
		List novaLista = new ArrayList();
		if (valorTp != null) { 
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToTpDominio(valorTp, tipoDominio), novaLista);
		}
		if (novaLista.isEmpty()) {
			CollectionUtils.select(listaEnquadramentoAtual, new PredicateToTpDominio(null, tipoDominio), novaLista);
			if(novaLista.isEmpty()) {
				listaEnquadramentoOriginal.removeAll(listaEnquadramentoAtual);
		}
		}
		
		return novaLista;
		}
	
	@Transactional (propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void generatePendencia(Long idControleCarga) {
		workflowPendenciaService.generatePendencia(SessionUtils.getFilialSessao().getIdFilial(), EVENTO_WORKFLOW_CEMOP, idControleCarga, "", JTDateTimeUtils.getDataHoraAtual());
	}
}



class PredicateToIdCliente implements Predicate {
	private Long idCliente;

    public PredicateToIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
	
    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idCliente != null) { 
			return er.getClienteEnquadramentos() != null && 
				CollectionUtils.exists(er.getClienteEnquadramentos(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	ClienteEnquadramento clienteEnquadramento = (ClienteEnquadramento)obj;
		            	return clienteEnquadramento.getCliente().getIdCliente().compareTo(idCliente) == 0;
		            }
				});
    	}
		else
	   		return (er.getClienteEnquadramentos() == null || er.getClienteEnquadramentos().isEmpty());
    }
}


class PredicateToIdReguladoraSeguroByBlSeguroMercurio implements Predicate {
	private Boolean blSeguroMercurio;

    public PredicateToIdReguladoraSeguroByBlSeguroMercurio(Boolean blSeguroMercurio) {
        this.blSeguroMercurio = blSeguroMercurio;
    }
	
    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	return er.getBlSeguroMercurio().booleanValue() == blSeguroMercurio.booleanValue();
    }
}


class PredicateToIdReguladoraSeguro implements Predicate {
	private Long idReguladoraSeguro;

    public PredicateToIdReguladoraSeguro(Long idReguladoraSeguro) {
        this.idReguladoraSeguro = idReguladoraSeguro;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idReguladoraSeguro != null)
    		return (er.getReguladoraSeguro() != null && er.getReguladoraSeguro().getIdReguladora().compareTo(idReguladoraSeguro) == 0);
    	else
    		return (er.getReguladoraSeguro() == null);
    }
}


class PredicateToIdNaturezaProduto implements Predicate {
	private Long idNaturezaProduto;

    public PredicateToIdNaturezaProduto(Long idNaturezaProduto) {
        this.idNaturezaProduto = idNaturezaProduto;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idNaturezaProduto != null)
    		return (er.getNaturezaProduto() != null && er.getNaturezaProduto().getIdNaturezaProduto().compareTo(idNaturezaProduto) == 0);
    	else
    		return (er.getNaturezaProduto() == null);
    }
}


class PredicateToIdFilialOrigem implements Predicate {
	private Long idFilialOrigem;

    public PredicateToIdFilialOrigem(Long idFilialOrigem) {
        this.idFilialOrigem = idFilialOrigem;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idFilialOrigem != null) { 
			return er.getFilialEnquadramentosOrigem() != null &&
				CollectionUtils.exists(er.getFilialEnquadramentosOrigem(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	FilialEnquadramento filialEnquadramento = (FilialEnquadramento)obj;
		            	return filialEnquadramento.getFilial().getIdFilial().compareTo(idFilialOrigem) == 0;
		            }
				});
    	}
		else
	   		return (er.getFilialEnquadramentosOrigem() == null || er.getFilialEnquadramentosOrigem().isEmpty());
    }
}


class PredicateToIdFilialDestino implements Predicate {
	private Long idFilialDestino;

    public PredicateToIdFilialDestino(Long idFilialDestino) {
        this.idFilialDestino = idFilialDestino;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idFilialDestino != null) { 
			return er.getFilialEnquadramentosDestino() != null &&
				CollectionUtils.exists(er.getFilialEnquadramentosDestino(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	FilialEnquadramento filialEnquadramento = (FilialEnquadramento)obj;
		            	return filialEnquadramento.getFilial().getIdFilial().compareTo(idFilialDestino) == 0;
		            }
				});
    	}
		else
	   		return (er.getFilialEnquadramentosDestino() == null || er.getFilialEnquadramentosDestino().isEmpty());
    }
}


class PredicateToIdPaisOrigem implements Predicate {
	private Long idPaisOrigem;

    public PredicateToIdPaisOrigem(Long idPaisOrigem) {
        this.idPaisOrigem = idPaisOrigem;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idPaisOrigem != null) { 
			return er.getPaisEnquadramentosOrigem() != null &&
				CollectionUtils.exists(er.getPaisEnquadramentosOrigem(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	PaisEnquadramento paisEnquadramento = (PaisEnquadramento)obj;
		            	return paisEnquadramento.getPais().getIdPais().compareTo(idPaisOrigem) == 0;
		            }
				});
    	}
		else
	   		return (er.getPaisEnquadramentosOrigem() == null || er.getPaisEnquadramentosOrigem().isEmpty());
    }
}


class PredicateToIdPaisDestino implements Predicate {
	private Long idPaisDestino;

    public PredicateToIdPaisDestino(Long idPaisDestino) {
        this.idPaisDestino = idPaisDestino;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idPaisDestino != null) { 
			return er.getPaisEnquadramentosDestino() != null &&
				CollectionUtils.exists(er.getPaisEnquadramentosDestino(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	PaisEnquadramento paisEnquadramento = (PaisEnquadramento)obj;
		            	return paisEnquadramento.getPais().getIdPais().compareTo(idPaisDestino) == 0;
		            }
				});
    	}
		else
	   		return (er.getPaisEnquadramentosDestino() == null || er.getPaisEnquadramentosDestino().isEmpty());
    }
}


class PredicateToIdUfOrigem implements Predicate {
	private Long idUfOrigem;

    public PredicateToIdUfOrigem(Long idUfOrigem) {
        this.idUfOrigem = idUfOrigem;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idUfOrigem != null) { 
			return er.getUnidadeFederativaEnquadramentosOrigem() != null &&
				CollectionUtils.exists(er.getUnidadeFederativaEnquadramentosOrigem(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = (UnidadeFederativaEnquadramento)obj;
		            	return unidadeFederativaEnquadramento.getUnidadeFederativa().getIdUnidadeFederativa().compareTo(idUfOrigem) == 0;
		            }
				});
    	}
		else
	   		return (er.getUnidadeFederativaEnquadramentosOrigem() == null || er.getUnidadeFederativaEnquadramentosOrigem().isEmpty());
    }
}


class PredicateToIdUfDestino implements Predicate {
	private Long idUfDestino;

    public PredicateToIdUfDestino(Long idUfDestino) {
        this.idUfDestino = idUfDestino;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idUfDestino != null) { 
			return er.getUnidadeFederativaEnquadramentosDestino() != null &&
				CollectionUtils.exists(er.getUnidadeFederativaEnquadramentosDestino(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = (UnidadeFederativaEnquadramento)obj;
		            	return unidadeFederativaEnquadramento.getUnidadeFederativa().getIdUnidadeFederativa().compareTo(idUfDestino) == 0;
		            }
				});
    	}
		else
	   		return (er.getUnidadeFederativaEnquadramentosDestino() == null || er.getUnidadeFederativaEnquadramentosDestino().isEmpty());
    }
}


class PredicateToIdMunicipioOrigem implements Predicate {
	private Long idMunicipioOrigem;

    public PredicateToIdMunicipioOrigem(Long idMunicipioOrigem) {
        this.idMunicipioOrigem = idMunicipioOrigem;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idMunicipioOrigem != null) { 
			return er.getMunicipioEnquadramentosOrigem() != null &&
				CollectionUtils.exists(er.getMunicipioEnquadramentosOrigem(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	MunicipioEnquadramento municipioEnquadramento = (MunicipioEnquadramento)obj;
		            	return municipioEnquadramento.getMunicipio().getIdMunicipio().compareTo(idMunicipioOrigem) == 0;
		            }
				});
    	}
		else
	   		return (er.getMunicipioEnquadramentosOrigem() == null || er.getMunicipioEnquadramentosOrigem().isEmpty());
    }
}


class PredicateToIdMunicipioDestino implements Predicate {
	private Long idMunicipioDestino;

    public PredicateToIdMunicipioDestino(Long idMunicipioDestino) {
        this.idMunicipioDestino = idMunicipioDestino;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (idMunicipioDestino != null) { 
			return er.getMunicipioEnquadramentosDestino() != null && 
				CollectionUtils.exists(er.getMunicipioEnquadramentosDestino(), new Predicate() {
		            public boolean evaluate(Object obj) {
		            	MunicipioEnquadramento municipioEnquadramento = (MunicipioEnquadramento)obj;
		            	return municipioEnquadramento.getMunicipio().getIdMunicipio().compareTo(idMunicipioDestino) == 0;
		            }
				});
    	}
		else
	   		return (er.getMunicipioEnquadramentosDestino() == null || er.getMunicipioEnquadramentosDestino().isEmpty());
    }
}


class PredicateToTpDominio implements Predicate {
	private String valor;
	private String tipoDominio;

    public PredicateToTpDominio(String valor, String tipoDominio) {
        this.valor = valor;
        this.tipoDominio = tipoDominio;
    }

    public boolean evaluate(Object obj) {
    	EnquadramentoRegra er = (EnquadramentoRegra)obj;
    	if (tipoDominio.equals("tpModal")) {
	    	if (valor != null)
	    		return er.getTpModal() != null && er.getTpModal().getValue().compareTo(valor) == 0;
			else
		   		return er.getTpModal() == null;
		}
		else
		if (tipoDominio.equals("tpAbrangencia")) {
		    if (valor != null) 
    			return er.getTpAbrangencia() != null && er.getTpAbrangencia().getValue().compareTo(valor) == 0;
			else
	   			return er.getTpAbrangencia() == null;
		}
		else
		if (tipoDominio.equals("tpGrauRiscoColetaEntrega")) {
		    if (valor != null) 
    			return er.getTpGrauRiscoColetaEntrega() != null && er.getTpGrauRiscoColetaEntrega().getValue().compareTo(valor) == 0;
			else
	   			return er.getTpGrauRiscoColetaEntrega() == null;
		}
		else
    	if (tipoDominio.equals("tpVinculo")) {
	    	if (valor != null)
	    		return er.getTpVinculoMeioTransporte() != null && er.getTpVinculoMeioTransporte().getValue().compareTo(valor) == 0;
			else
		   		return er.getTpVinculoMeioTransporte() == null;
		}
		throw new IllegalStateException("Tipo do domínio inválido");
    }
}