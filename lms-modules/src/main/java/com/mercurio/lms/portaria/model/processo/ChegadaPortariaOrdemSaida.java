package com.mercurio.lms.portaria.model.processo;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Query;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.evento.EventoPortariaMeioTransporte;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;

/**
 * LMSA-768 - Processo "Informar Chegada na Portaria" de {@link OrdemSaida}s.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class ChegadaPortariaOrdemSaida extends ChegadaPortaria {

	/**
	 * Factory method para "Informar Chegada na Portaria" tipo Ordem de Saída.
	 * 
	 * @param service
	 *            Classe de serviços para processo.
	 * @param dao
	 *            DAO para carga, validação e processamento.
	 * @param parameters
	 *            Mapa de parâmetros gerados na action.
	 * @return Instância de {@link ChegadaPortariaOrdemSaida}.
	 */
	public static ChegadaPortaria createChegadaPortaria(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		return new ChegadaPortariaOrdemSaida(service, dao, parameters);
	}

	private OrdemSaida ordemSaida;


	private ChegadaPortariaOrdemSaida(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		super(service, dao, parameters);
	}

	/**
	 * Carrega dados da {@link OrdemSaida} a processar.
	 */
	@Override
	protected void carregarDados() {
		super.carregarDados();
		ordemSaida = dao.findOrdemSaida(parameters.getLong("idOrdemSaida"));
	}

	/**
	 * Valida processamento de {@link OrdemSaida} verificando atributos
	 * {@code dhSaida}, {@code dhChegada} e {@code blSemRetorno}.
	 */
	@Override
	protected boolean validarInformarChegada() {
		if (ordemSaida.getDhSaida() == null || ordemSaida.getDhChegada() != null
				|| BooleanUtils.isTrue(ordemSaida.getBlSemRetorno())) {
			throw new BusinessException("LMS-06048");
		}
		return true;
	}

	/**
	 * Prepara o processamento dos {@link MeioTransporte}s relacionados à
	 * {@link OrdemSaida}.
	 */
	@Override
	protected void prepararMeioTransporte() {
		transportado = ordemSaida.getMeioTransporteRodoviarioByIdMeioTransporte().getMeioTransporte();
		semiReboque = ordemSaida.getMeioTransporteRodoviarioByIdSemiReboque() != null
				? ordemSaida.getMeioTransporteRodoviarioByIdSemiReboque().getMeioTransporte() : null;
	}

	/**
	 * Concluí geração do {@link EventoMeioTransporte} definindo atributo
	 * {@code tpSituacao} com valor {@code 'ADFR'} (A Disposição da Frota).
	 */
	@Override
	protected EventoPortariaMeioTransporte gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		return super.gerarEventoMeioTransporte(meioTransporte)
				.setTpSituacao(ConstantesPortaria.TP_SITUACAO_A_DISPOSICAO_DA_FROTA);
	}

	/**
	 * Registra atualização para {@link OrdemSaida}.
	 */
	@Override
	protected void registrarAtualizacoes() {
		registrarAtualizacao(gerarAtualizacaoOrdemSaida());
	}

	private Query gerarAtualizacaoOrdemSaida() {
		return dao.queryUpdateOrdemSaida(ordemSaida, dhChegada);
	}

}
