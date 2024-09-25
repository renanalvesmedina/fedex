package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.dao.OcorrenciaEntregaDAO;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.util.WarningCollector;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.ocorrenciaEntregaService"
 */
public class OcorrenciaEntregaService extends CrudService<OcorrenciaEntrega, Long> {

	private ControleCargaService controleCargaService;
	private DoctoServicoService doctoServicoService;
	private EventoService eventoService;
	private ConfiguracoesFacade configuracoesFacade;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private EntregaNotaFiscalService entregaNotaFiscalService;

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	public void setEntregaNotaFiscalService(EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setOcorrenciaEntregaDAO(OcorrenciaEntregaDAO dao) {
		setDao( dao );
	}


	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private OcorrenciaEntregaDAO getOcorrenciaEntregaDAO() {
		return (OcorrenciaEntregaDAO) getDao();
	}

	/**
	 * Recupera uma instância de <code>OcorrenciaEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public OcorrenciaEntrega findById(java.lang.Long id) {
		return (OcorrenciaEntrega)super.findById(id);
	}

	/**
	 * Procura uma ocorrencia por id.
	 * Este método seta o evento novamente para trazer junto a concatenação dos campos para descrição.
	 * Não foi criado outro método porque o findloookup... já existe e faz o necessário.
	 * */
	public OcorrenciaEntrega findOcorrenciaEntregaById(java.lang.Long id){
		OcorrenciaEntrega ocorrenciaEntrega = findById(id);
		if (ocorrenciaEntrega != null ){
			if (ocorrenciaEntrega.getEvento() != null){
				Map map = new TypedFlatMap();
				map.put("cdEvento", ocorrenciaEntrega.getEvento().getCdEvento());
				Evento evento = (Evento)(eventoService.find(map)).get(0);
				ocorrenciaEntrega.setEvento(evento);
			}
			if (ocorrenciaEntrega.getOcorrenciaPendencia() != null){
				Map map = new TypedFlatMap();
				map.put("cdOcorrencia", ocorrenciaEntrega.getOcorrenciaPendencia().getCdOcorrencia());
				OcorrenciaPendencia ocorrenciaPendencia = (OcorrenciaPendencia)(ocorrenciaPendenciaService.find(map)).get(0);
				ocorrenciaEntrega.setOcorrenciaPendencia(ocorrenciaPendencia);
			}
		}
		return ocorrenciaEntrega;
	}

	public OcorrenciaEntrega findLastOcorrenciaEntregaByIdVolumeAndFilial(Long idVolume, Long idFilial){
		return getOcorrenciaEntregaDAO().findLastOcorrenciaEntregaByIdVolumeAndFilial(idVolume, idFilial);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByCodigoTipo(Short cdOcorrenciaEntrega, Object[] tpOcorrencia ){
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaByCodigoTipo(cdOcorrenciaEntrega, tpOcorrencia);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByCodigoTipo(Short cdOcorrenciaEntrega){
		return getOcorrenciaEntregaDAO().findOcorrenciaEntrega(cdOcorrenciaEntrega);
	}

	/**
	 * Verifica se o documento de servico ja foi entregue
	 * @param idDoctoServico
	 * @return
	 */
	public Integer validateDoctoServicoEntregue(Long idDoctoServico){
		return doctoServicoService.getRowCountDoctoServicoEntregues(idDoctoServico);
	}


	public Evento findEventoAssociado(Long idOcorrenciaEntrega){
		return getOcorrenciaEntregaDAO().findEventoAssociado(idOcorrenciaEntrega);
	}


	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(OcorrenciaEntrega bean) {
		return super.store(bean);
	}


	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getOcorrenciaEntregaDAO().findPaginatedCustom(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getOcorrenciaEntregaDAO().getRowCountCustom(criteria);
	}

	public Integer getRowCountTotaisPorMotivo(TypedFlatMap criteria){
		return getOcorrenciaEntregaDAO().getRowCountTotaisPorMotivo(criteria);
	}

	public ResultSetPage findPaginatedTotaisPorMotivo(TypedFlatMap criteria){
		return getOcorrenciaEntregaDAO().findPaginatedTotaisPorMotivo(criteria);
	}

	/**
	 * Consulta todas as ocorrências de entrega associadas a um determinado documento de serviço.
	 * @param idDoctoServico
	 * @param tpOcorrencia
	 * @return List lista da entidade OcorrenciaEntrega
	 */
	public List findOcorrenciasEntegaByDoctoServicoAndOcorrencia(Long idDoctoServico, String tpOcorrencia) {
		return getOcorrenciaEntregaDAO().findOcorrenciasEntegaByDoctoServicoAndOcorrencia(idDoctoServico,tpOcorrencia);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByIdDoctoServico(Long idDoctoServico) {
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaByIdDoctoServico(idDoctoServico);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaNaoCanceladaByIdDoctoServico(Long idDoctoServico) {
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaNaoCanceladaByIdDoctoServico(idDoctoServico);
	}

	/**
	 * Solicitação CQPRO00006012 da integração.
	 * Método que retorna uma instancia da classe OcorrenciaEntrega de acordo com código da ocorrência de entrega.
	 * @param cdOcorrenciaEntrega
	 * @return
	 */
	public OcorrenciaEntrega findOcorrenciaEntrega(Short cdOcorrenciaEntrega){
		return getOcorrenciaEntregaDAO().findOcorrenciaEntrega(cdOcorrenciaEntrega);
	}

	public List<Long> findIdsDoctosServicoManifestoViagemEntregaParcial(Long idControleCarga, Long idFilialDestino) {
		return getOcorrenciaEntregaDAO().findIdsDoctosServicoManifestoViagemEntregaParcial(idControleCarga, idFilialDestino);
	}

	/**
	 * Método que retorna os Documentos de Serviço (vinculados a um Manifesto de Viagem cujo tipo é entrega direta)
	 * de um Controle de Carga e que não possuem uma Ocorrência de Entrega.
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public boolean existsDoctosServicoManifestoViagemSemOcorrenciaEntrega(Long idControleCarga, Long idFilialDestino) {
		return getOcorrenciaEntregaDAO().existsDoctosServicoManifestoViagemSemOcorrenciaEntrega(idControleCarga, idFilialDestino);
	}

	/**
	 * Método que retorna os Documentos de Serviço (vinculados a um Manifesto de Entrega)
	 * de um Controle de Carga e que não possuem uma Ocorrência de Entrega.
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public boolean existsDoctosServicoManifestoEntregaSemOcorrenciaEntrega(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().existsDoctosServicoManifestoEntregaSemOcorrenciaEntrega(idControleCarga);
	}

	/**
	 * Validações para regras 5.1 e 5.2 da ET 03.01.01.01
	 * @param idControleCarga
	 * @param idFilialDestino
	 */
	public void validateDoctoServicoComOcorrenciaVinculada(Long idControleCarga, Long idFilialDestino) {
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

		if (controleCarga.getTpControleCarga().getValue().equals("V")){
			// FIXME - testar - rotina:
			// se GM, e tipo igual a "V"
			// Se a filial do usuário logado é uma filial com LMS implantado (restrição geral para LMS, ja EXISTE?)
			// entao chama a rotina ValidaVolEtiquetadoGMDireto
			if (validaVolEtiquetadoGMDireto(idControleCarga)) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-46005"));
			}

			if (!existsVolGM(idControleCarga)){
				if (this.existsDoctosServicoManifestoViagemSemOcorrenciaEntrega(idControleCarga, idFilialDestino)){
					throw new BusinessException("LMS-03014");
				}

				List<Long> idsDoctoServico = this.findIdsDoctosServicoManifestoViagemEntregaParcial(idControleCarga, idFilialDestino);
				if (!idsDoctoServico.isEmpty() && entregaNotaFiscalService.existsNotaFiscalSemEntregaByIdsDoctoServico(idsDoctoServico, idControleCarga)) {
					throw new BusinessException("LMS-03014");
				}
			}
		} else {
			if (controleCarga.getTpControleCarga().getValue().equals("C")) {
				this.validateColetaEntrega(idControleCarga);
			}
		}
	}

	private void validateColetaEntrega(Long idControleCarga) {
		if (this.existsDoctosServicoManifestoEntregaSemOcorrenciaEntrega(idControleCarga) || this.existsVolumeManifestoEntregaSemOcorrenciaEntrega(idControleCarga)) {
			throw new BusinessException("LMS-03015");
		}

		List<Long> idsDoctoServico = new ArrayList<>();
		idsDoctoServico.addAll(this.findIdsDoctoServicoManifestoEntregaParcial(idControleCarga));

		if (!idsDoctoServico.isEmpty() && entregaNotaFiscalService.existsNotaFiscalSemEntregaByIdsDoctoServico(idsDoctoServico, idControleCarga)) {
			throw new BusinessException("LMS-03015");
		}
	}

	public List<OcorrenciaEntrega> findAllOcorrenciaEntregaAtivo() {
		return getOcorrenciaEntregaDAO().findAllOcorrenciaEntregaAtivo();
	}

	public List<OcorrenciaEntrega> findOcorrenciaEntregaRegistrarBaixaNotaAtivo() {
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaRegistrarBaixaNotaAtivo();
	}

	/**
	 * Metodo para buscar se existem algum volume, para GM, sem etiqueta. Se existir para essa descarga algum
	 * volume sem etiqueta retorna TRUE.
	 * @param idControleCarga
	 * @return
	 */
	private boolean validaVolEtiquetadoGMDireto(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().existsVolEtiquetadoGMDireto(idControleCarga);
	}

	private boolean existsVolGM(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().existsVolGM(idControleCarga);
	}

	/**
	 * Metodo para buscar se existem algum volume, para GM, sem etiqueta. Se existir para essa descarga algum
	 * volume sem etiqueta retorna TRUE.
	 * @param idControleCarga
	 * @return
	 */
	public Boolean existsVolEtiquetadoGMDireto(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().existsVolEtiquetadoGMDireto(idControleCarga);
	}


	/**
	 * Mesma chamada do metodo existsVolEtiquetadoGMDireto porém respeitando as regras de
	 * nomenclatura para metodos transacionais.
	 *
	 * @param idControleCarga
	 * @return
	 */
	public Boolean validateVolEtiquetadoGMDireto(Long idControleCarga){
		return getOcorrenciaEntregaDAO().existsVolEtiquetadoGMDireto(idControleCarga);
	}
	/**
	 * Método que retorna a existencia de vinculo de Volume e Manifesto de Entrega.
	 * @param idControleCarga
	 * @param idFilialDestino
	 * @return
	 */
	public Boolean existsVolumeManifestoEntregaSemOcorrenciaEntrega(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().existsVolumeManifestoEntregaSemOcorrenciaEntrega(idControleCarga);
	}

	public List<Long> findIdsDoctoServicoManifestoEntregaParcial(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().findIdsDoctoServicoManifestoEntregaParcial(idControleCarga);
	}

	public List<Long> findIdsDoctoServicoVolumeManifestoEntregaParcial(Long idControleCarga) {
		return getOcorrenciaEntregaDAO().findIdsDoctoServicoVolumeManifestoEntregaParcial(idControleCarga);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaByIdDoctoServicoByIdManifesto(Long idDoctoServico, Long idManifestoEntrega) {
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaByIdDoctoServicoByIdManifesto(idDoctoServico, idManifestoEntrega);
	}

	public OcorrenciaEntrega findOcorrenciaEntregaDoctoServico(Long idDoctoServico, Long idManifesto) {
		List<String> tpManifestoEntregaList = new ArrayList<>();
		tpManifestoEntregaList.add(ConstantesEntrega.DM_MANIFESTO_ENTREGA_NORMAL.getValue());
		tpManifestoEntregaList.add(ConstantesEntrega.DM_MANIFESTO_ENTREGA_PARCEIRA.getValue());
		tpManifestoEntregaList.add(ConstantesEntrega.DM_MANIFESTO_ENTREGA_DIRETA.getValue());

		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaDoctoServico(
				idDoctoServico, idManifesto,
				ConstantesEntrega.STATUS_MANIFESTO_CANCELADO,
				tpManifestoEntregaList);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	/**
	 * Retorna código de ocorrência das ocorrências ativas
	 * JIRA: EDI-1061
	 *
	 * @return List código de ocorrencia de entrega
	 *
	 **/
	public List findCdOcorrenciaEntregaAtiva(){
		return getOcorrenciaEntregaDAO().findCdOcorrenciaEntregaAtiva();
	}

	public List<OcorrenciaEntrega> findOcorrenciaEntregaByIdDoctoServicoCdOcorrenciaEntregaNaoCancelado(Long idDoctoServico, Short cdOcorrenciaEntrega) {
		return getOcorrenciaEntregaDAO().findOcorrenciaEntregaByIdDoctoServicoCdOcorrenciaEntregaNaoCancelado(idDoctoServico, cdOcorrenciaEntrega);
	}

	public Long findIdOcorrenciaEntregaByCdOcorrenciaEntrega(Short cdOcorrenciaEntrega){
		return getOcorrenciaEntregaDAO().findIdOcorrenciaEntregaByCdOcorrenciaEntrega(cdOcorrenciaEntrega);
	}

	public Response dowloadImagem(Map<String, Object> parametros){
		StringBuilder path = new StringBuilder("/all");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(parametros.get("urlImagemDigital").toString()).path(path.toString())
				.queryParam("cdFilial", parametros.get("cdFilial"))
				.queryParam("dhEmissao", parametros.get("dhEmissaoFormatado"))
				.queryParam("nrDoctoServico", parametros.get("nrDoctoServico"))
				.queryParam("idDoctoServico", parametros.get("idDoctoServico"));
		return target.request().get();
	}

}
