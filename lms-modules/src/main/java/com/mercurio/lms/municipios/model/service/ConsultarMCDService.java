package com.mercurio.lms.municipios.model.service;

import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO;
import com.mercurio.lms.municipios.model.param.MCDParam;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.municipios.consultarMCDService"
 */
public class ConsultarMCDService {

	private ConsultarMCDDAO consultarMCDDAO;
	private FluxoFilialService fluxoFilialService;
	private McdService mcdService;
	private FilialService filialService;
	private PpeService ppeService;
	private MunicipioService municipioService;

	/**
	 * Find Paginated da tela de consulta de MCD.
	 * 
	 * @param dados
	 *            MCDParam classe contendo os parâmtros aceitáveis.
	 * @param pageNumber
	 *            número da página atual na grid.
	 * @param pageSize
	 *            número de registros a aparecer em cada página.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginatedCustom(MCDParam dados, Integer pageNumber, Integer pageSize) {

		/*
		 * Se houver filial origem, mas nao houver municipio origem, setar como
		 * municipio origem, o municipio da filial
		 */
		if ((dados.getIdFilialOrigem() != null) && (dados.getIdMunicipioOrigem() == null)) {
			Filial filial = filialService.findById(dados.getIdFilialOrigem());
			dados.setIdMunicipioOrigem(filial.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio());
		}

		/*
		 * Se houver filial destino, mas nao houver municipio destino, setar
		 * como municipio destino, o municipio da filial
		 */
		if ((dados.getIdFilialDestino() != null) && (dados.getIdMunicipioDestino() == null)) {
			Filial filial = filialService.findById(dados.getIdFilialDestino());
			dados.setIdMunicipioDestino(filial.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio());
		}

		ResultSetPage rsp = getConsultarMCDDAO().findPaginatedCustom(dados, pageNumber, pageSize);
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			@Override
			public Map filterItem(Object item) {
				TypedFlatMap row = new TypedFlatMap();
				Object[] valores = (Object[]) item;

				row.put("blDistritoDestino", valores[MCDParam.BL_DISTRITO_DESTINO]);
				row.put("blDistritoOrigem", valores[MCDParam.BL_DISTRITO_ORIGEM]);
				row.put("blDomingoDestino", valores[MCDParam.BL_DOMINGO_DESTINO]);
				row.put("blDomingoOrigem", valores[MCDParam.BL_DOMINGO_ORIGEM]);
				row.put("blQuartaDestino", valores[MCDParam.BL_QUARTA_DESTINO]);
				row.put("blQuartaOrigem", valores[MCDParam.BL_QUARTA_ORIGEM]);
				row.put("blQuintaDestino", valores[MCDParam.BL_QUINTA_DESTINO]);
				row.put("blQuintaOrigem", valores[MCDParam.BL_QUINTA_ORIGEM]);
				row.put("blSabadoDestino", valores[MCDParam.BL_SABADO_DESTINO]);
				row.put("blSabadoOrigem", valores[MCDParam.BL_SABADO_ORIGEM]);
				row.put("blSegundaDestino", valores[MCDParam.BL_SEGUNDA_DESTINO]);
				row.put("blSegundaOrigem", valores[MCDParam.BL_SEGUNDA_ORIGEM]);
				row.put("blSextaDestino", valores[MCDParam.BL_SEXTA_DESTINO]);
				row.put("blSextaOrigem", valores[MCDParam.BL_SEXTA_ORIGEM]);
				row.put("blTercaDestino", valores[MCDParam.BL_TERCA_DESTINO]);
				row.put("blTercaOrigem", valores[MCDParam.BL_TERCA_ORIGEM]);
				row.put("idFilialDestino", valores[MCDParam.ID_FILIAL_DESTINO]);
				row.put("idFilialOrigem", valores[MCDParam.ID_FILIAL_ORIGEM]);
				row.put("idMunicipioDestino", valores[MCDParam.ID_MUNICIPIO_DESTINO]);
				row.put("idMunicipioOrigem", valores[MCDParam.ID_MUNICIPIO_ORIGEM]);
				row.put("idServico", valores[MCDParam.ID_SERVICO]);
				row.put("municipioFilialByIdMunicipioFilialDestino_cdIBGE", valores[MCDParam.CD_IBGE_DESTINO]);
				row.put("municipioFilialByIdMunicipioFilialDestino_nmMunicipio", valores[MCDParam.NM_MUNICIPIO_DESTINO]);
				row.put("municipioFilialByIdMunicipioFilialDestino_unidadeFederativa_nmUnidadeFederativa", valores[MCDParam.NM_UNIDADE_FEDERATIVA_DESTINO]);
				row.put("municipioFilialByIdMunicipioFilialDestino_unidadeFederativa_pais_nmPais", valores[MCDParam.NM_PAIS_DESTINO]);
				row.put("municipioFilialByIdMunicipioFilialOrigem_cdIBGE", valores[MCDParam.CD_IBGE_ORIGEM]);
				row.put("municipioFilialByIdMunicipioFilialOrigem_nmMunicipio", valores[MCDParam.NM_MUNICIPIO_ORIGEM]);
				row.put("municipioFilialByIdMunicipioFilialOrigem_unidadeFederativa_nmUnidadeFederativa", valores[MCDParam.NM_UNIDADE_FEDERATIVA_ORIGEM]);
				row.put("municipioFilialByIdMunicipioFilialOrigem_unidadeFederativa_pais_nmPais", valores[MCDParam.NM_PAIS_ORIGEM]);
				row.put("nmFilialDestino", valores[MCDParam.NM_FILIAL_DESTINO]);
				row.put("nmFilialOrigem", valores[MCDParam.NM_FILIAL_ORIGEM]);
				row.put("nrPpe", valores[MCDParam.NR_PPE]);
				row.put("qtPedagio", valores[MCDParam.QT_PEDAGIO]);
				row.put("servico_dsServico", valores[MCDParam.DS_SERVICO]);
				row.put("sgFilialDestino", valores[MCDParam.SG_FILIAL_DESTINO]);
				row.put("sgFilialOrigem", valores[MCDParam.SG_FILIAL_ORIGEM]);
				row.put("tarifa_cdTarifa", valores[MCDParam.CD_TARIFA_PRECO]);
				row.put("tarifa_cdTarifa_atual", valores[MCDParam.CD_TARIFA_PRECO_ATUAL]);


				return row;
			}

		};

		return (ResultSetPage) frsp.doFilter();
    }

	/**
	 * Retorna número de registros da consulta de MCD.
	 * 
	 * @param dados
	 * @return
	 */
    public Integer getRowCountCustom(MCDParam dados) {
    	return getConsultarMCDDAO().getRowCountCustom(dados);
    }

	/**
     * Rotina que calcula a quantidade de dias de atendimento de um município de destino relacionado ao
     * objeto passado como parametro OperacaoServicoLocaliza
     * 
     * ATENÇÃO! Caso essa regra seja alterada algum dia deve ser alterada a query do método {@link com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO#getSqlBasic(MCDParam)}</br>
     * onde faz esta regra dentro da consulta
     * 
     * @param operacaoServicoLocaliza relacionada ao municipio de destino que se quer saber os dias de atendimento
     * @return
     */
    public Long verificaDiasAtendimento(OperacaoServicoLocaliza operacaoServicoLocaliza) {
    	//dias atendimento operação
    	int countDiasAtendimentoOperacao = 0;
    	int retorno = 0 ;
    	
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlSegunda());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlTerca());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlQuarta());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlQuinta());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlSexta());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlSabado());
    	countDiasAtendimentoOperacao+=somaDiaAtendimentoOperacao(operacaoServicoLocaliza.getBlDomingo());
		
    	//De acordo com a quantidade de dias de atendimento retornar a quantidade de dias de atendimento
    	switch (countDiasAtendimentoOperacao) {
		case 1:
			retorno = 5;
			break;
		case 2:
			retorno = 2;
			break;
		case 3:
			retorno = 1;
			break;
		default:
			retorno = 0;
			break;

		}
    	
    	return Integer.valueOf(retorno).longValue();
    }
    
    /**
     * Testa o parametro booleano que representa o dia de atendimento da OperacaoServicoLocaliza
     * @param diaOperacao
     * @return
     */
    private int somaDiaAtendimentoOperacao(Boolean diaOperacao) {
    	if(diaOperacao != null && diaOperacao) {
    		return 1;
    	}
    	return 0;
    }
    
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
     * 
	 * @param Instância
	 *            do DAO.
     */
    public void setConsultarMCDDAO(ConsultarMCDDAO dao) {
        this.consultarMCDDAO = dao;
    }

    /**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
     * @return Instância do DAO.
     */
    private ConsultarMCDDAO getConsultarMCDDAO() {
        return consultarMCDDAO;
    }

	public FluxoFilialService getFluxoFilialService() {
		return fluxoFilialService;
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	public McdService getMcdService() {
		return mcdService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public PpeService getPpeService() {
		return ppeService;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

}