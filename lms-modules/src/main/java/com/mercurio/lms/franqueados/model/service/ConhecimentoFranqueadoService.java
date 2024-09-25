package com.mercurio.lms.franqueados.model.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.franqueados.model.dao.ConhecimentoFranqueadoDAO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoLocalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoLocalFranqueadoSimulacaoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoNacionalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoNacionalFranqueadoSimuacaoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoReembarqueFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoReembarqueFranqueadoSimulacaoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoServicoAdicionalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.ConhecimentoFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.RecalculoLocalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.RecalculoNacionalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.RecalculoServicoAdicionalFranqueadoDTO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ConhecimentoFranqueadoService {

	private ConhecimentoFranqueadoDAO conhecimentoFranqueadoDAO;

	public List<TypedFlatMap> findDocumentoReembarcado(Long idFranquia, YearMonthDay dtInicioCompetencia, YearMonthDay dtFimCompetencia) {
		return getConhecimentoFranqueadoDAO().findDocumentoReembarcado(idFranquia, dtInicioCompetencia, dtFimCompetencia);
	}

	
	public ConhecimentoFranqueadoDAO getConhecimentoFranqueadoDAO() {
		return conhecimentoFranqueadoDAO;
	}

	public void setConhecimentoFranqueadoDAO(
			ConhecimentoFranqueadoDAO conhecimentoFranqueadoDAO) {
		this.conhecimentoFranqueadoDAO = conhecimentoFranqueadoDAO;
	}

	@SuppressWarnings("unchecked")
	public List<CalculoServicoAdicionalFranqueadoDTO> findServicoAdicionalFranqueado(Long idFranquia,
			YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia, Long psMinFreteCarreteiro) {
		List<ListOrderedMap> list = conhecimentoFranqueadoDAO.findDadosServicosAdicionaisFranqueado(idFranquia,
				dtIniCompetencia, dtFimCompetencia);
		List<CalculoServicoAdicionalFranqueadoDTO> _return = null;
		if (CollectionUtils.isNotEmpty(list)) {
			_return = new LinkedList<CalculoServicoAdicionalFranqueadoDTO>();
			for (ListOrderedMap m : list) {
				CalculoServicoAdicionalFranqueadoDTO calculo = new CalculoServicoAdicionalFranqueadoDTO();
				calculo.setIdDoctoServico(MapUtilsPlus.getLong(m,"id_docto_servico"));
				calculo.setVlTotalDocServico(MapUtilsPlus.getBigDecimal(m,"vl_total_doc_servico"));
				calculo.setIdDevedorDocServFat(MapUtilsPlus.getLong(m,"id_devedor_doc_serv_fat"));
				calculo.setIdDesconto(MapUtilsPlus.getLong(m,"id_desconto"));
				calculo.setVlDesconto(MapUtilsPlus.getBigDecimal(m,"vl_desconto"));
				calculo.setIdMotivoDesconto(MapUtilsPlus.getLong(m,"id_motivo_desconto"));
				calculo.setTpSituacaoAprovacao(MapUtilsPlus.getString(m,"tp_situacao_aprovacao"));
				calculo.setVlMercadoria(MapUtilsPlus.getBigDecimal(m,"vl_mercadoria"));
				calculo.setVlImposto(MapUtilsPlus.getBigDecimal(m,"vl_imposto"));
				calculo.setIdServicoAdicional(MapUtilsPlus.getLong(m,"id_servico_adicional"));
				_return.add(calculo);
			}
		}
		return _return;
	}

	public List<CalculoReembarqueFranqueadoDTO> findReembarcadoFranqueado(Long idFranquia,
			YearMonthDay dtInicioCompetencia, YearMonthDay dtFimCompetencia, Boolean simulacao) {
		List<TypedFlatMap> list = conhecimentoFranqueadoDAO.findDocumentoReembarcado(idFranquia, dtInicioCompetencia,
				dtFimCompetencia);

		List<CalculoReembarqueFranqueadoDTO> _return = null;
		if (CollectionUtils.isNotEmpty(list)) {
			_return = new LinkedList<CalculoReembarqueFranqueadoDTO>();
			for (TypedFlatMap map : list) {
				CalculoReembarqueFranqueadoDTO calculo = null;
				if( simulacao ){
					calculo = new CalculoReembarqueFranqueadoSimulacaoDTO();
				}else{
					calculo = new CalculoReembarqueFranqueadoDTO();					
				}
				calculo.setIdDoctoServico(MapUtilsPlus.getLong(map,"id_docto_servico"));
				calculo.setIdManifesto(MapUtilsPlus.getLong(map,"id_manifesto"));
				calculo.setPsReal(MapUtilsPlus.getBigDecimal(map,"ps_real"));
				_return.add(calculo);
			}
		}
		return _return;
	}

	@SuppressWarnings("unchecked")
	public List<RecalculoServicoAdicionalFranqueadoDTO> findRecalculoServicoAdicionalFranqueado(Long idFranquia,
			YearMonthDay dataInicioCompetencia, YearMonthDay dataFimCompetencia) {
		List<ListOrderedMap> list = conhecimentoFranqueadoDAO.findDadosServicosAdicionaisRecalculo(idFranquia,dataInicioCompetencia, dataFimCompetencia);
		List<RecalculoServicoAdicionalFranqueadoDTO> _return = null;
		if (CollectionUtils.isNotEmpty(list)) {
			_return = new LinkedList<RecalculoServicoAdicionalFranqueadoDTO>();
			for (ListOrderedMap m : list) {
				YearMonthDay dtCompetencia = JTDateTimeUtils.convertDataStringToYearMonthDay(MapUtilsPlus.getString(m, "dt_competencia"));
				RecalculoServicoAdicionalFranqueadoDTO calculo = new RecalculoServicoAdicionalFranqueadoDTO();
				calculo.setIdDoctoServico(MapUtilsPlus.getLong(m,"id_docto_servico"));
				calculo.setVlTotalDocServico(MapUtilsPlus.getBigDecimal(m,"vl_total_doc_servico"));
				calculo.setIdDevedorDocServFat(MapUtilsPlus.getLong(m,"id_devedor_doc_serv_fat"));
				calculo.setIdDesconto(MapUtilsPlus.getLong(m,"id_desconto"));
				calculo.setVlDesconto(MapUtilsPlus.getBigDecimal(m,"vl_desconto"));
				calculo.setIdMotivoDesconto(MapUtilsPlus.getLong(m,"id_motivo_desconto"));
				calculo.setTpSituacaoAprovacao(MapUtilsPlus.getString(m,"tp_situacao_aprovacao"));
				calculo.setVlMercadoria(MapUtilsPlus.getBigDecimal(m,"vl_mercadoria"));
				calculo.setVlImposto(MapUtilsPlus.getBigDecimal(m,"vl_imposto"));
				calculo.setIdServicoAdicional(MapUtilsPlus.getLong(m,"id_servico_adicional"));
				calculo.setIdDoctoServicoFranqueadoOriginal(MapUtilsPlus.getLong(m,"id_conhecimento_original"));
				calculo.setVlParticipacaoOriginal(MapUtilsPlus.getBigDecimal(m,"vl_participacao_original"));
				calculo.setDtCompetenciaRecalculo(dtCompetencia);
				_return.add(calculo);
			}
		}
		return _return;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ConhecimentoFranqueadoDTO> findConhecimentoFranqueado(Long idFranquia, YearMonthDay dtIniCompetencia,
		YearMonthDay dtFimCompetencia,Long psMinFreteCarreteiro, List<Long> idsMunicipios, Boolean simulacao) {
		
		List<ListOrderedMap> list = this.conhecimentoFranqueadoDAO.findConhecimentoFranqueado(idFranquia,dtIniCompetencia, dtFimCompetencia,psMinFreteCarreteiro,idsMunicipios);
		List<ConhecimentoFranqueadoDTO> _return = new LinkedList<>();
		
		if (CollectionUtils.isNotEmpty(list)) {
			list.forEach( m -> {
				ConhecimentoFranqueadoDTO calculo = null;

				if( (idFranquia.equals(MapUtilsPlus.getLong(m,"id_filial_origem")) && idFranquia.equals(MapUtilsPlus.getLong(m,"id_filial_destino"))) ){
					calculo = simulacao ? new CalculoLocalFranqueadoSimulacaoDTO() : new CalculoLocalFranqueadoDTO();

				}else{
					calculo = simulacao ? new CalculoNacionalFranqueadoSimuacaoDTO() : new CalculoNacionalFranqueadoDTO();
					calculo.setNrDistancia(MapUtilsPlus.getInteger(m,"nr_distancia"));
					calculo.setVlCustoCarreteiro(MapUtilsPlus.getBigDecimal(m,"custo_carreteiro"));
					calculo.setVlCustoAereo(MapUtilsPlus.getBigDecimal(m,"custo_aereo"));
					calculo.setQtFranquias(MapUtilsPlus.getInteger(m,"qt_franquias"));
				}

				calculo.setIdDoctoServico(MapUtilsPlus.getLong(m,"id_docto_servico"));
				calculo.setIdFilialOrigem(MapUtilsPlus.getLong(m,"id_filial_origem"));
				calculo.setIdFilialDestino(MapUtilsPlus.getLong(m,"id_filial_destino"));
				calculo.setTpFrete(MapUtilsPlus.getString(m,"tp_frete"));
				calculo.setVlTotalDocServico(MapUtilsPlus.getBigDecimal(m,"vl_total_doc_servico"));
				calculo.setIdDevedorDocServFat(MapUtilsPlus.getLong(m,"id_devedor_doc_serv_fat"));
				calculo.setIdDesconto(MapUtilsPlus.getLong(m,"id_desconto"));
				calculo.setVlDesconto(MapUtilsPlus.getBigDecimal(m,"vl_desconto"));
				calculo.setIdMotivoDesconto(MapUtilsPlus.getLong(m,"id_motivo_desconto"));
				calculo.setTpSituacaoAprovacao(MapUtilsPlus.getString(m,"tp_situacao_aprovacao"));
				calculo.setVlMercadoria(MapUtilsPlus.getBigDecimal(m,"vl_mercadoria"));
				calculo.setVlImposto(MapUtilsPlus.getBigDecimal(m,"vl_imposto"));
				calculo.setIdMunicipioEntrega(MapUtilsPlus.getLong(m,"id_municipio_entrega"));
				calculo.setIdMunicipioColeta(MapUtilsPlus.getLong(m,"id_municipio_coleta"));
				calculo.setIdClienteDestinatario(MapUtilsPlus.getLong(m,"id_cliente_destinatario"));
				calculo.setIdClienteRemetente(MapUtilsPlus.getLong(m,"id_cliente_remetente"));
				_return.add(calculo);
			});
		}
		return _return;
	}

	@SuppressWarnings("unchecked")
	public List<ConhecimentoFranqueadoDTO> findRecalculoConhecimentoFranqueado(Long idFranquia,
			YearMonthDay dataInicioCompetencia, YearMonthDay dataFimCompetencia, Long psMinFreteCarreteiro) {
		List<ListOrderedMap> list = conhecimentoFranqueadoDAO.findConhecimentoRecalculo(idFranquia, dataInicioCompetencia, dataFimCompetencia, psMinFreteCarreteiro);
		List<ConhecimentoFranqueadoDTO> _return = null;
		if (CollectionUtils.isNotEmpty(list)) {
			_return = new LinkedList<ConhecimentoFranqueadoDTO>();
			for (ListOrderedMap m : list) {
				ConhecimentoFranqueadoDTO calculo = null;
				YearMonthDay dtCompetencia = JTDateTimeUtils.convertDataStringToYearMonthDay(MapUtilsPlus.getString(m, "dt_competencia"));
				if( (idFranquia.equals(MapUtilsPlus.getLong(m,"id_filial_origem")) && idFranquia.equals(MapUtilsPlus.getLong(m,"id_filial_destino"))) ){
					calculo = new RecalculoLocalFranqueadoDTO();
					((RecalculoLocalFranqueadoDTO)calculo).setDtCompetenciaRecalculo(dtCompetencia);
				}else{
					calculo = new RecalculoNacionalFranqueadoDTO();
					calculo.setNrDistancia(MapUtilsPlus.getInteger(m,"nr_distancia"));
					calculo.setVlCustoCarreteiro(MapUtilsPlus.getBigDecimal(m,"custo_carreteiro"));
					calculo.setVlCustoAereo(MapUtilsPlus.getBigDecimal(m,"custo_aereo"));
					calculo.setQtFranquias(MapUtilsPlus.getInteger(m,"qt_franquias"));
					((RecalculoNacionalFranqueadoDTO)calculo).setDtCompetenciaRecalculo(dtCompetencia);
				}
				calculo.setIdDoctoServico(MapUtilsPlus.getLong(m,"id_docto_servico"));
				calculo.setIdFilialOrigem(MapUtilsPlus.getLong(m,"id_filial_origem"));
				calculo.setIdFilialDestino(MapUtilsPlus.getLong(m,"id_filial_destino"));
				calculo.setTpFrete(MapUtilsPlus.getString(m,"tp_frete"));
				calculo.setVlTotalDocServico(MapUtilsPlus.getBigDecimal(m,"vl_total_doc_servico"));
				calculo.setIdDevedorDocServFat(MapUtilsPlus.getLong(m,"id_devedor_doc_serv_fat"));
				calculo.setIdDesconto(MapUtilsPlus.getLong(m,"id_desconto"));
				calculo.setVlDesconto(MapUtilsPlus.getBigDecimal(m,"vl_desconto"));
				calculo.setIdMotivoDesconto(MapUtilsPlus.getLong(m,"id_motivo_desconto"));
				calculo.setTpSituacaoAprovacao(MapUtilsPlus.getString(m,"tp_situacao_aprovacao"));
				calculo.setVlMercadoria(MapUtilsPlus.getBigDecimal(m,"vl_mercadoria"));
				calculo.setVlImposto(MapUtilsPlus.getBigDecimal(m,"vl_imposto"));
				calculo.setIdMunicipioEntrega(MapUtilsPlus.getLong(m,"id_municipio_entrega"));
				calculo.setIdMunicipioColeta(MapUtilsPlus.getLong(m,"id_municipio_coleta"));
				calculo.setIdClienteDestinatario(MapUtilsPlus.getLong(m,"id_cliente_destinatario"));
				calculo.setIdClienteRemetente(MapUtilsPlus.getLong(m,"id_cliente_remetente"));
				calculo.setIdDoctoServicoFranqueadoOriginal(MapUtilsPlus.getLong(m,"id_conhecimento_original"));
				calculo.setVlParticipacaoOriginal(MapUtilsPlus.getBigDecimal(m,"vl_participacao_original"));
				_return.add(calculo);
			}
		}
		return _return;
	}
	
}