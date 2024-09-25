package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.AdsmHibernateTemplate;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.municipios.dto.CopiaFluxoFilialDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.dao.FluxoFilialDAO;
import com.mercurio.lms.municipios.util.FluxoFilialHelper;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.fluxoFilialService"
 */
public class FluxoFilialService extends CrudService<FluxoFilial, Long> {
	private FilialService filialService;
	private VigenciaService vigenciaService;
	private PpeService ppeService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>FluxoFilial</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public FluxoFilial findById(java.lang.Long id) {
		return (FluxoFilial)super.findById(id);
	}

	/**
	 * Recupera uma instância de <code>FluxoFilial</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map findByIdDetalhamento(java.lang.Long id) {
		FluxoFilial fluxoFilial = (FluxoFilial)super.findById(id);
		Servico servico = fluxoFilial.getServico();
		Filial filialOrigem = fluxoFilial.getFilialByIdFilialOrigem();
		Filial filialDestino = fluxoFilial.getFilialByIdFilialDestino();
		Filial filialReembarcadora = fluxoFilial.getFilialByIdFilialReembarcadora();
		Filial filialParceira = fluxoFilial.getFilialByIdFilialParceira(); //CQPRO00022947

		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("idFluxoFilial", fluxoFilial.getIdFluxoFilial());

		if (servico != null){
			retorno.put("servico.idServico", servico.getIdServico());
			retorno.put("tpSituacao", servico.getTpSituacao().getValue());
			retorno.put("tpModal", servico.getTpModal().getValue());
			
		}
		
		retorno.put("filialByIdFilialOrigem.idFilial", filialOrigem.getIdFilial());
		retorno.put("filialByIdFilialOrigem.sgFilial", filialOrigem.getSgFilial());
		retorno.put("filialByIdFilialOrigem.pessoa.nmFantasia", filialOrigem.getPessoa().getNmFantasia());

		retorno.put("filialByIdFilialDestino.idFilial", filialDestino.getIdFilial());
		retorno.put("filialByIdFilialDestino.sgFilial", filialDestino.getSgFilial());
		retorno.put("filialByIdFilialDestino.pessoa.nmFantasia", filialDestino.getPessoa().getNmFantasia());

		Empresa empresaDestino = filialDestino.getEmpresa();
		if (empresaDestino != null) {
			Pessoa pessoa = empresaDestino.getPessoa();
			if (pessoa != null) {
				retorno.put("empresaDestino", pessoa.getNmPessoa());
				retorno.put("tpFilialDestino", empresaDestino.getTpEmpresa().getValue());
			}
		}

		if (filialReembarcadora != null) {
			retorno.put("filialByIdFilialReembarcadora.idFilial", filialReembarcadora.getIdFilial());
			retorno.put("filialByIdFilialReembarcadora.sgFilial", filialReembarcadora.getSgFilial());
			retorno.put("filialByIdFilialReembarcadora.pessoa.nmFantasia", filialReembarcadora.getPessoa().getNmFantasia());	
		}

		//CQPRO00022947
		if (filialParceira != null) {
			retorno.put("filialByIdFilialParceira.idFilial", filialParceira.getIdFilial());
			retorno.put("filialByIdFilialParceira.sgFilial", filialParceira.getSgFilial());
			retorno.put("filialByIdFilialParceira.pessoa.nmFantasia", filialParceira.getPessoa().getNmFantasia());
			
			Empresa empresaParceira = filialParceira.getEmpresa();
			if (empresaParceira != null) {
				Pessoa pessoa = empresaParceira.getPessoa();
				if (pessoa != null) {
					retorno.put("empresaParceira", pessoa.getNmPessoa());
				}
			}
		}
		// Fim CQPRO00022947
		
		retorno.put("nrDistancia", fluxoFilial.getNrDistancia());
		retorno.put("nrPrazoView", fluxoFilial.getNrPrazoView());
		retorno.put("blDomingo", fluxoFilial.getBlDomingo());
		retorno.put("blSegunda", fluxoFilial.getBlSegunda());
		retorno.put("blTerca", fluxoFilial.getBlTerca());
		retorno.put("blQuarta", fluxoFilial.getBlQuarta());
		retorno.put("blQuinta", fluxoFilial.getBlQuinta());
		retorno.put("blSexta", fluxoFilial.getBlSexta());
		retorno.put("blSabado", fluxoFilial.getBlSabado());
		retorno.put("nrGrauDificuldade", fluxoFilial.getNrGrauDificuldade());
		retorno.put("dtVigenciaInicial", fluxoFilial.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal", fluxoFilial.getDtVigenciaFinal());

		retorno.put("dsFluxoFilial", fluxoFilial.getDsFluxoFilial());
		retorno.put("nrPrazoTotal", fluxoFilial.getNrPrazoTotal());
		retorno.put("blPorto", fluxoFilial.getBlPorto());

		retorno.put("blFluxoSubcontratacao", fluxoFilial.getBlFluxoSubcontratacao());
		if (fluxoFilial.getEmpresaSubcontratada() != null){
			retorno.put("empresaSubcontratada.idPessoa", fluxoFilial.getEmpresaSubcontratada().getIdPessoa());
			retorno.put("empresaSubcontratada.nmPessoa", fluxoFilial.getEmpresaSubcontratada().getNmPessoa());
			retorno.put("empresaSubcontratada.pessoa.nrIdentificacao", fluxoFilial.getEmpresaSubcontratada().getNrIdentificacao());
		}
		
		

		if (fluxoFilial.getFilialByIdFilialReembarcadora() != null) {
			Long idFluxoFilial = fluxoFilial.getIdFluxoFilial();
			Long idFilialOrigem = filialOrigem.getIdFilial();
			Long idFilialDestino = filialDestino.getIdFilial();
			Long idFilialReembarcadora = filialReembarcadora.getIdFilial();
			
			Long idFilialParceira = null;
			if (filialParceira != null)
				idFilialParceira = filialParceira.getIdFilial();
			
			Long idServico = (servico == null) ? null : servico.getIdServico();

			YearMonthDay dtVigenciaInicial = fluxoFilial.getDtVigenciaInicial();
			YearMonthDay dtVigenciaFinal   = fluxoFilial.getDtVigenciaFinal();

			List l = findFiliaisReembarque(idFluxoFilial,idFilialOrigem,idFilialDestino,idFilialReembarcadora,
											idServico, dtVigenciaInicial, dtVigenciaFinal);

			FluxoFilial fluxoFilialAux = new FluxoFilial();
			List fluxoList = new ArrayList();
			for (Iterator i = l.iterator() ; i.hasNext() ; ) {
				fluxoFilialAux = (FluxoFilial)i.next();				
				Map fluxoMapReembarque = new HashMap();
				Map fluxoMapParceira = null;
				fluxoMapReembarque.put("fluxo",new StringBuffer().append(fluxoFilialAux.getFilialByIdFilialOrigem().getSgFilial())
						.append(" - ").append(fluxoFilialAux.getFilialByIdFilialOrigem().getPessoa().getNmFantasia()).toString());

				// Contempla a filial parceira em qualquer trecho do fluxo
				// caso exista uma filial parceira
				if (fluxoFilialAux.getFilialByIdFilialParceira() != null) {
					fluxoMapParceira = new HashMap();
					fluxoMapParceira.put("fluxo",new StringBuffer().append(fluxoFilialAux.getFilialByIdFilialParceira().getSgFilial())
							.append(" - ").append(fluxoFilialAux.getFilialByIdFilialParceira().getPessoa().getNmFantasia()).toString());					
				}
				fluxoList.add(fluxoMapReembarque);
				if (fluxoMapParceira != null)
					fluxoList.add(fluxoMapParceira);			
			}
			retorno.put("fluxoReembarque",fluxoList);
			
		} else if (fluxoFilial.getFilialByIdFilialParceira() != null) {
			// Contempla a filial parceira em qualquer trecho do fluxo
			// caso exista uma filial parceira		
			List fluxoList = new ArrayList();
				Map fluxoMap = new HashMap();
			fluxoMap.put("fluxo",new StringBuffer().append(fluxoFilial.getFilialByIdFilialParceira().getSgFilial())
				.append(" - ").append(fluxoFilial.getFilialByIdFilialParceira().getPessoa().getNmFantasia()).toString());

				fluxoList.add(fluxoMap);
			retorno.put("fluxoReembarque",fluxoList);
		}

		Integer acaoVigenciaAtual = JTVigenciaUtils.getIntegerAcaoVigencia(fluxoFilial);
		retorno.put("acaoVigenciaAtual",acaoVigenciaAtual);

		return retorno;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getFluxoFilialDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * Força a deleção de OrdemFilialFluxo através de cascade.
	 * 
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		AdsmHibernateTemplate template = getFluxoFilialDAO().getAdsmHibernateTemplate();
		FluxoFilial ff = (FluxoFilial)template.load(FluxoFilial.class,id);
		ff.getOrdemFilialFluxos().clear();

		FluxoFilial fluxoFilial = (FluxoFilial)getFluxoFilialDAO().getHibernateTemplate()
				.get(FluxoFilial.class,id);

		JTVigenciaUtils.validaVigenciaRemocao(fluxoFilial);

		if (getFluxoFilialDAO().verificaFluxosFilhos(fluxoFilial,true))
			throw new BusinessException("LMS-29116");

		template.delete(ff);
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
		Iterator it = ids.iterator();
		while (it.hasNext()) {
			this.removeById((Long)it.next());
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(FluxoFilial bean) {
		if (bean.getEmpresaSubcontratada() != null){
			String nrIdentificacao = bean.getEmpresaSubcontratada().getNrIdentificacao().substring(0, 8);
			String cnpjSubcontratacaoFedex = (String)configuracoesFacade.getValorParametro("CNPJ_SUBCONTRATACAO_FDX");
			
			if (nrIdentificacao != null && cnpjSubcontratacaoFedex != null && !nrIdentificacao.equals(cnpjSubcontratacaoFedex)){
				throw new BusinessException("LMS-29196", new Object[]{bean.getEmpresaSubcontratada().getNrIdentificacao()});
			}
		}
		
		
		if (bean.isClone()) {
			Long idFluxoCopiado = (Long) executeCopiarFluxoFilial(bean, bean.getIdFluxoFilial(), bean.getDtVigenciaInicial(), null);
			executeAtualizaFluxoClone(bean.getIdFluxoFilial(), idFluxoCopiado, bean.getDtVigenciaInicial());
			return idFluxoCopiado;
		}else {
		return super.store(bean);
	}
	}

	private void executeAtualizaFluxoClone(Long idFluxoFilialOrigem, Long idFluxoFilialClone, YearMonthDay dtVigenciaInicial) {
		FluxoFilial fluxoFilialOrigem = findById(idFluxoFilialOrigem);
		FluxoFilial fluxoFilialClone = findById(idFluxoFilialClone) ;     

		List<Map> fluxosFilaisToUpdate = findFluxoFiliaisByOrigemCopia(dtVigenciaInicial, fluxoFilialOrigem, fluxoFilialClone);

		if (CollectionUtils.isNotEmpty(fluxosFilaisToUpdate)) {
			for (Map map : fluxosFilaisToUpdate) {
				FluxoFilial fluxoFilial = (FluxoFilial)map.get("keyFluxoFilial");
				// passo nao especificado na et 
				CopiaFluxoFilialDto copiaFluxoFilialDto = new CopiaFluxoFilialDto();
				copiaFluxoFilialDto.setFluxoFilialOrigem(fluxoFilialOrigem);
				copiaFluxoFilialDto.setFluxoFilialClone(fluxoFilialClone);
				copiaFluxoFilialDto.setFiliaisRemovidas(filialService.findFiliaisDiferentesEntreFluxosFiliais(fluxoFilialOrigem, fluxoFilialClone));
				
				// passo 3
				Long idFluxoFilialCopiado = (Long) executeCopiarFluxoFilial(null, fluxoFilial.getIdFluxoFilial(), dtVigenciaInicial, copiaFluxoFilialDto);
				FluxoFilial fluxoFilialCopiaLoaded = findById(idFluxoFilialCopiado);
				
				fluxoFilialCopiaLoaded.setAtualizacaoClone(true);
				
				// passo 4
				executeAtualizaDistanciaTempoFluxoFilialCopiado(fluxoFilialCopiaLoaded);

				// passo 6 
				fluxoFilialCopiaLoaded.setDtVigenciaFinal(fluxoFilialClone.getDtVigenciaFinal());
				
				super.store(fluxoFilialCopiaLoaded);
			}
		}

	}

	private List findFluxoFiliaisByOrigemCopia(YearMonthDay dtVigenciaInicial, FluxoFilial fluxoFilialOrigem, FluxoFilial fluxoFilialClone) {
		return getFluxoFilialDAO().findFluxoFiliaisByOrigemCopia(dtVigenciaInicial, fluxoFilialOrigem, fluxoFilialClone);
	}

	private void executeAtualizaDistanciaTempoFluxoFilialCopiado(FluxoFilial fluxoFilialCopiado) {
		FluxoFilial ultimoFluxoFilial = findUltimoFluxoFilial(fluxoFilialCopiado.getFilialByIdFilialOrigem().getIdFilial(), 
				fluxoFilialCopiado.getFilialByIdFilialDestino().getIdFilial(), 
				fluxoFilialCopiado.getServico() == null ? null : fluxoFilialCopiado.getServico().getIdServico());

		fluxoFilialCopiado.setNrDistancia(ultimoFluxoFilial.getNrDistancia());
		fluxoFilialCopiado.setNrPrazo(ultimoFluxoFilial.getNrPrazo());
	}

	private Serializable executeCopiarFluxoFilial(FluxoFilial bean, Long idFluxoFilial, YearMonthDay dtVigenciaInicial, CopiaFluxoFilialDto copiaFluxoFilialDto) {
		FluxoFilial fluxoFilialLoaded = findById(idFluxoFilial);

		if (fluxoFilialLoaded.getDtVigenciaInicial().isAfter(dtVigenciaInicial)) {
			throw new BusinessException("LMS-29182");
		}

		// atualiza e salva po registro anterior
		YearMonthDay dtVigenciaFinal = FluxoFilialHelper.getDtVigenciaFinal(fluxoFilialLoaded.getDtVigenciaInicial(), dtVigenciaInicial);	
		fluxoFilialLoaded.setDtVigenciaFinal(dtVigenciaFinal);
		if (bean != null && bean.getBlPorto() != null && fluxoFilialLoaded.getBlPorto() == null) {
			fluxoFilialLoaded.setBlPorto(bean.getBlPorto());
		} else if (fluxoFilialLoaded.getBlPorto() == null) {
			fluxoFilialLoaded.setBlPorto(Boolean.FALSE);
		}
		getDao().store(fluxoFilialLoaded);

		// cria e salva a copia
		FluxoFilial fluxoFilialCopy = FluxoFilialHelper.clonarFluxo((bean != null ? bean : fluxoFilialLoaded), dtVigenciaInicial, copiaFluxoFilialDto);
		
		findOrganizaOrdemFluxo(fluxoFilialCopy, copiaFluxoFilialDto);

		Serializable idCopy = super.store(fluxoFilialCopy);
		
		return idCopy;
	}

	

	public void updateVigenciaFinal(Long idFluxoFilial, YearMonthDay vigenciaFinal){
		FluxoFilial fluxoFilial = findById(idFluxoFilial);
		if (fluxoFilial != null){
			fluxoFilial.setDtVigenciaFinal(vigenciaFinal.minusDays(1));
			if(fluxoFilial.getDtVigenciaInicial().isAfter(fluxoFilial.getDtVigenciaFinal())){
				throw new BusinessException("LMS-29182");
			}

			getDao().store(fluxoFilial);
		}
	}
	
	/**
	 * Responsável por "criar" descrição do Fluxo.
	 * Responsável por associar uma lista de ordemFluxoFilial ao fluxo.
	 * 
	 * ff.setOrdemFilialFluxos(_);
	 * ff.setDsFluxoFilial(_);
	 * 
	 * @param ff
	 * @return FluxoFilial
	 */
	public FluxoFilial findOrganizaOrdemFluxo(FluxoFilial fluxoFilial, CopiaFluxoFilialDto dto) {
		Long idFluxoFilial = fluxoFilial.getIdFluxoFilial();

		Filial filialOrigem = fluxoFilial.getFilialByIdFilialOrigem();
		Long idFilialOrigem = filialOrigem.getIdFilial();

		Filial filialDestino = fluxoFilial.getFilialByIdFilialDestino();
		Long idFilialDestino = filialDestino.getIdFilial();

		Filial filialReembarcadora = fluxoFilial.getFilialByIdFilialReembarcadora();
		Long idFilialReembarcadora = filialReembarcadora == null ? null : filialReembarcadora.getIdFilial();

		//CQPRO00022947
		Filial filialParceira = fluxoFilial.getFilialByIdFilialParceira();
		Long idFilialParceira = filialParceira == null ? null : filialParceira.getIdFilial();
		//Fim CQPRO00022947

		Servico servico = fluxoFilial.getServico();
		Long idServico = (servico == null) ? null : servico.getIdServico();

		// Cria StringBuffer para armazenar a descrição do fluxo. Já adiciona a filial de origem
		StringBuffer fluxo = new StringBuffer().append(filialOrigem.getSgFilial());

		// Lista que contém POJO's de OrdemFilialFluxo
		List ordemFluxoFiliais = new ArrayList();
		if(fluxoFilial.getOrdemFilialFluxos() != null){
			fluxoFilial.getOrdemFilialFluxos().clear();
			ordemFluxoFiliais = fluxoFilial.getOrdemFilialFluxos(); 
		}

		// Ordem da filial no fluxo:
		Byte nrOrdem = Byte.valueOf((byte)1);

		OrdemFilialFluxo ordemFilialFluxo = new OrdemFilialFluxo();
		ordemFilialFluxo.setFilial(filialOrigem);
		ordemFilialFluxo.setFluxoFilial(fluxoFilial);
		ordemFilialFluxo.setNrOrdem(nrOrdem);
		ordemFluxoFiliais.add(ordemFilialFluxo);

		// Adiciona as filiais de reembarque:
		List l = findFiliaisReembarque(idFluxoFilial, idFilialOrigem, idFilialDestino, idFilialReembarcadora, idServico, fluxoFilial.getDtVigenciaInicial(), fluxoFilial.getDtVigenciaFinal());
		for (Iterator i = l.iterator() ; i.hasNext() ; ) {
			FluxoFilial fluxoFilialAux = (FluxoFilial)i.next();				
			
			Filial filialByIdFilialOrigemAux = fluxoFilialAux.getFilialByIdFilialOrigem();
			
			if(dto != null && dto.getFiliaisRemovidas().contains(filialByIdFilialOrigemAux)){
				continue;
			}
			
			fluxo.append(" - ").append(filialByIdFilialOrigemAux.getSgFilial());

			ordemFilialFluxo = new OrdemFilialFluxo();
			ordemFilialFluxo.setFilial(filialByIdFilialOrigemAux);
			ordemFilialFluxo.setFluxoFilial(fluxoFilial);
			// incrementa um Byte
			nrOrdem = Byte.valueOf((byte)(nrOrdem.intValue() + 1));
			ordemFilialFluxo.setNrOrdem(nrOrdem);
			ordemFluxoFiliais.add(ordemFilialFluxo);
			
		}

		// Adiciona a filial de destino:
		fluxo.append(" - ").append(filialDestino.getSgFilial());
		ordemFilialFluxo = new OrdemFilialFluxo();
		ordemFilialFluxo.setFilial(filialDestino);
		ordemFilialFluxo.setFluxoFilial(fluxoFilial);

		// incrementa um Byte
		nrOrdem = Byte.valueOf((byte)(nrOrdem.intValue() + 1));
		ordemFilialFluxo.setNrOrdem(nrOrdem);
		ordemFluxoFiliais.add(ordemFilialFluxo);

		fluxoFilial.setDsFluxoFilial(fluxo.toString());
		fluxoFilial.setOrdemFilialFluxos(ordemFluxoFiliais);

		return fluxoFilial;
	}

	public List getListFluxoReembarque(TypedFlatMap map) {
		Long idFluxoFilial = map.getLong("idFluxoFilial");
		Long idFilialOrigem = map.getLong("idFilialOrigem");
		Long idFilialDestino = map.getLong("idFilialDestino");
		Long idFilialReembarcadora = map.getLong("idFilialReembarcadora");
		Long idServico = map.getLong("idServico");

		YearMonthDay dtVigenciaInicial = map.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtVigenciaFinal   = map.getYearMonthDay("dtVigenciaFinal");

		return findFiliaisReembarque(idFluxoFilial, idFilialOrigem, idFilialDestino, idFilialReembarcadora, idServico, dtVigenciaInicial, dtVigenciaFinal);
	}

	private List findFluxoReembarque(
		Long idFluxoFilial,
		Long idFilialDestino,
		Long idFilialReembarcadora,
		Long idServico, 
		YearMonthDay dtVigenciaInicial, 
		YearMonthDay dtVigenciaFinal
	) {
		return getFluxoFilialDAO().findFluxoReembarque(idFluxoFilial, idFilialDestino, idFilialReembarcadora, idServico, dtVigenciaInicial, dtVigenciaFinal);
	}

	private List findFiliaisReembarque(
			Long idFluxoFilial,
			Long idFilialOrigemPrincipal,
			Long idFilialDestino,
			Long idFilialReembarcadora,
			Long idServico,
			YearMonthDay dtVigenciaInicial, 
			YearMonthDay dtVigenciaFinal
	) {		
		return findFiliaisReembarque(idFluxoFilial, idFilialOrigemPrincipal, idFilialDestino, idFilialReembarcadora, idServico, null, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Consulta as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * 
	 * @param map
	 * @return List com pojo's de FluxoFilial
	 */
	private List findFiliaisReembarque(
			Long idFluxoFilial,
			Long idFilialOrigemPrincipal,
			Long idFilialDestino,
			Long idFilialReembarcadora,
			Long idServico,
			List idsAnteriores, 
			YearMonthDay dtVigenciaInicial,
			YearMonthDay dtVigenciaFinal
	) {

		if (idFilialReembarcadora == null)
			return new ArrayList(0);

		List lQuery = findFluxoReembarque(idFluxoFilial,idFilialDestino,idFilialReembarcadora,idServico, dtVigenciaInicial, dtVigenciaFinal);
		List result = new ArrayList();
		if (lQuery.size() > 0)
			result.add(lQuery.get(0));

		if (idsAnteriores == null) {
			idsAnteriores = new ArrayList();
			idsAnteriores.add(idFluxoFilial);
		}

		if (result.size() > 0) {
			FluxoFilial fluxoFilial = (FluxoFilial) lQuery.get(0);
			Long idFluxoFilialAux = fluxoFilial.getIdFluxoFilial();
			if (fluxoFilial.getFilialByIdFilialReembarcadora() != null) {

				idFilialDestino = fluxoFilial.getFilialByIdFilialDestino().getIdFilial();
				Long idFilialOrigem = fluxoFilial.getFilialByIdFilialOrigem().getIdFilial();
				idFilialReembarcadora = fluxoFilial.getFilialByIdFilialReembarcadora().getIdFilial();

				// Regra 3: Caso a filial de reembarque seja igual a filial de origem do fluxo ou 
				// caso o fluxo encontrado esteja no list fluxos utilizados como reembarque.
				if (idFilialReembarcadora.equals(idFilialOrigemPrincipal) || idsAnteriores.contains(idFluxoFilialAux)){
					Filial f1 = filialService.findById(idFilialOrigem);
					Filial f2 = filialService.findById(idFilialDestino);

					throw new BusinessException("LMS-29155", new Object[]{f1.getSiglaNomeFilial(), f2.getSiglaNomeFilial()});
				}

				idsAnteriores.add(idFluxoFilialAux);

				idServico = (fluxoFilial.getServico() == null) ? null : fluxoFilial.getServico().getIdServico();

				List resultReemb = findFiliaisReembarque(idFluxoFilialAux, idFilialOrigemPrincipal,
						idFilialDestino, idFilialReembarcadora, idServico, idsAnteriores, dtVigenciaInicial, dtVigenciaFinal);

				for (Iterator i = resultReemb.iterator(); i.hasNext();) {
					result.add(i.next());
				}
			}
		}

		return result;
	}

	/**
	 * Consulta as filiais Mercúrio onde ocorrem reembarque de mercadoria.
	 * 
	 * @param map
	 * @return
	 */
	public FluxoFilial findFluxoReembarqueToMap(TypedFlatMap map) {
		List fluxosFilial = new ArrayList();

		if (map.getLong("idFilialReembarcadora") != null
				&& map.getLong("idFilialDestino") != null) {

			fluxosFilial = getListFluxoReembarque(map);
			if (fluxosFilial.size() == 0) {
				if (((String) map.get("which")).equals("dest"))
					throw new BusinessException("LMS-29028");
				else if (((String) map.get("which")).equals("reemb"))
					throw new BusinessException("LMS-29027");
			}
		}

		FluxoFilial fluxoFilial = new FluxoFilial();
		List fluxoList = new ArrayList();

		for (Iterator i = fluxosFilial.iterator() ; i.hasNext() ; ) {
			fluxoFilial = (FluxoFilial)i.next();				
			Map fluxoMapReembarque = new HashMap();
			Map fluxoMapParceira = null;
			fluxoMapReembarque.put("fluxo",new StringBuffer().append(fluxoFilial.getFilialByIdFilialOrigem().getSgFilial())
					.append(" - ").append(fluxoFilial.getFilialByIdFilialOrigem().getPessoa().getNmFantasia()).toString());
			
			// Contempla a filial parceira em qualquer trecho do fluxo
			// caso exista uma filial parceira
			if (fluxoFilial.getFilialByIdFilialParceira() != null) {
				fluxoMapParceira = new HashMap();
				fluxoMapParceira.put("fluxo",new StringBuffer().append(fluxoFilial.getFilialByIdFilialParceira().getSgFilial())
						.append(" - ").append(fluxoFilial.getFilialByIdFilialParceira().getPessoa().getNmFantasia()).toString());					
		}
			fluxoList.add(fluxoMapReembarque);
			if (fluxoMapParceira != null)
				fluxoList.add(fluxoMapParceira);			
		}
		fluxoFilial.setFluxoReembarque(fluxoList);

		return fluxoFilial;
	}

	private Integer getNrPrazoTotalOnStore(List filiais) {
		Integer retorno = Integer.valueOf(0);

		Iterator i = filiais.iterator();
		while (i.hasNext()) {
			FluxoFilial f = (FluxoFilial)i.next();
			Servico servico = f.getServico();

			Integer nrPrazo = this.calcularTempoDeslocamentoEntreFiliais(
					f.getFilialByIdFilialOrigem().getIdFilial(), 
					f.getFilialByIdFilialDestino().getIdFilial(),
					(servico == null) ? null : servico.getIdServico(), 
					f.getDtVigenciaInicial());
			retorno = IntegerUtils.add(retorno,nrPrazo);
		}

		return retorno;
	}

	/**
	 * Retorna o tempo previsto de entrega e a filial responsavel por efetuar a entrega
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @return
	 */
	public Integer calcularTempoDeslocamentoEntreFiliais(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){
		Long nrTempo = ppeService.calcularTempoDeslocamentoEntreFiliais(idFilialOrigem, idFilialDestino,idServico, dtConsulta);

		return nrTempo != null ? Integer.valueOf(nrTempo.intValue()) : null;
	}

	private void validaPeloMenosUmDiaChecado(FluxoFilial fluxoFilial) {
		Map mapDias = new HashMap();
		mapDias.put("blDomingo",fluxoFilial.getBlDomingo());
		mapDias.put("blSegunda",fluxoFilial.getBlSegunda());
		mapDias.put("blTerca",fluxoFilial.getBlTerca());
		mapDias.put("blQuarta",fluxoFilial.getBlQuarta());
		mapDias.put("blQuinta",fluxoFilial.getBlQuinta());
		mapDias.put("blSexta",fluxoFilial.getBlSexta());
		mapDias.put("blSabado",fluxoFilial.getBlSabado());
		if (!JTVigenciaUtils.verificaPeloMenosUmDiaChecado(mapDias)) {
			throw new BusinessException("LMS-29020");
		}
	}

	/**
	 * Ao salvar:
	 * Verifica se pelo menos um dia foi selecionado.
	 * Valida a filial de reembarque.
	 * Verifica se vigência não entra em conflito com fluxos que utilizam este fluxo.
	 * 
	 */
	protected FluxoFilial beforeStore(FluxoFilial bean) {

		FluxoFilial fluxoFilial = bean;

		Filial filialOrigem = filialService.findById(fluxoFilial.getFilialByIdFilialOrigem().getIdFilial());
		fluxoFilial.setFilialByIdFilialOrigem(filialOrigem);

		Filial filialDestino = filialService.findById(fluxoFilial.getFilialByIdFilialDestino().getIdFilial());
		fluxoFilial.setFilialByIdFilialDestino(filialDestino);

		Filial filialReembarcadora = fluxoFilial.getFilialByIdFilialReembarcadora();
		if (filialReembarcadora != null && filialReembarcadora.getIdFilial() == null) {
			filialReembarcadora = null;
	}
		if(filialReembarcadora != null) {
			filialReembarcadora = filialService.findById(filialReembarcadora.getIdFilial());
			fluxoFilial.setFilialByIdFilialReembarcadora(filialReembarcadora);
		}

		validaPeloMenosUmDiaChecado(fluxoFilial);

		//Verifica se o periodo de vigencia esta dentro do periodo de vigencia das filiais
		vigenciaService.validaVigenciaBeforeStore(fluxoFilial);

		// se não encontrou o tipo da empresa ou se a empresa é diferente de parceira...
		if(!"P".equals(filialDestino.getEmpresa().getTpEmpresa().getValue())) {
			filialService.verificaExistenciaHistoricoFilial(
				filialDestino.getIdFilial(),
				fluxoFilial.getDtVigenciaInicial(),
				fluxoFilial.getDtVigenciaFinal()
			);
		}

		filialService.verificaExistenciaHistoricoFilial(
			filialOrigem.getIdFilial(),
			fluxoFilial.getDtVigenciaInicial(),
			fluxoFilial.getDtVigenciaFinal()
		);
		

		if (filialReembarcadora != null)
			filialService.verificaExistenciaHistoricoFilial(
				filialReembarcadora.getIdFilial(),
				fluxoFilial.getDtVigenciaInicial(),
				fluxoFilial.getDtVigenciaFinal()
			);
		

		if (getFluxoFilialDAO().verificaFluxoFilialVigentes(fluxoFilial)){
			throw new BusinessException("LMS-00003");
		}
		
		Long idFluxoFilial = fluxoFilial.getIdFluxoFilial();
		Long idFilialOrigem = filialOrigem.getIdFilial();
        Long idFilialReembarcadora = filialReembarcadora == null ? null : filialReembarcadora.getIdFilial();
		Long idFilialDestino = filialDestino.getIdFilial();
		
		Long idServico = (fluxoFilial.getServico() == null) ? null : fluxoFilial.getServico().getIdServico();
		YearMonthDay dtVigenciaInicial = fluxoFilial.getDtVigenciaInicial();
		YearMonthDay dtVigenciaFinal = fluxoFilial.getDtVigenciaFinal();
		
		
		if (!bean.isAtualizacaoClone() || filialReembarcadora == null) {
		//verifica se esse fluxo é reembarcador e se tem fluxos que o utilizam, vigentes para o periodo passado
			List<String> dscFluxosFiliais = getFluxoFilialDAO().hasFluxosAnterioresEmUso(idFluxoFilial, idServico, dtVigenciaFinal);
		//caso existir, nao pode salvar
            if (existemFilhosVigentes(dscFluxosFiliais)) {
            	throw new BusinessException("LMS-29029", new Object[]{retornarDezPrimeirosFluxosFilhosOrganizados(dscFluxosFiliais)});
		}
		}
		
		if(filialReembarcadora == null){
			fluxoFilial.setNrPrazoTotal(fluxoFilial.getNrPrazo());
		} else {
		//LMS-3298
			List<FluxoFilial> ff = getFluxoFilialDAO().findFluxoFilialByFilialDestinoOrigemServicoVigencia(idFilialOrigem, idFilialReembarcadora, idServico, dtVigenciaInicial);

			FluxoFilial flxFilial = new FluxoFilial();

			if (ff == null || ff.size() == 0) throw new BusinessException("LMS-29069");
			else flxFilial= (FluxoFilial) ff.get(0);

			if  (flxFilial.getFilialByIdFilialReembarcadora() != null) throw new BusinessException("LMS-29169");	
		//LMS-3298

				//verifica se o 1º pai do fluxo a ser salvo está vigente para o intervalo do mesmo.
				Boolean exists1PaiVigente = getFluxoFilialDAO().findFluxoPaiVigente(idFilialOrigem, idFilialReembarcadora,idServico, dtVigenciaInicial, dtVigenciaFinal);				
				
				if(exists1PaiVigente){
					//verifica se o 2º pai do fluxo a ser salvo está vigente para o intervalo do mesmo.
					Boolean exists2PaiVigente = getFluxoFilialDAO().findFluxoPaiVigente(idFilialReembarcadora, idFilialDestino,idServico, dtVigenciaInicial, dtVigenciaFinal);
					
					if(!exists2PaiVigente){
						throw new BusinessException("LMS-29027");
					}						
				}else{					
					throw new BusinessException("LMS-29115");
				}
				
				//busca o nrPrazoTotal no fluxo onde a origem = reembarcadora até o destino do fluxo a ser salvo
				Integer nrPrazoTotal = Integer.valueOf(0);
				
				List lNrPrazoTotal = getFluxoFilialDAO().findNrPrazoTotal(idFilialReembarcadora, idFilialDestino, idServico, dtVigenciaInicial, dtVigenciaFinal);
				
			if(!lNrPrazoTotal.isEmpty()) nrPrazoTotal = (Integer)lNrPrazoTotal.get(0);
				
				//soma o nrPrazoTotal com o nrPrazo do fluxo a ser salvo
				fluxoFilial.setNrPrazoTotal(IntegerUtils.add(nrPrazoTotal,fluxoFilial.getNrPrazo()));
		}
		
		fluxoFilial = (FluxoFilial)super.beforeStore(bean);
			
		return fluxoFilial;
	}

	private boolean existemFilhosVigentes(List<String> dscFluxosFiliais) {
		return (dscFluxosFiliais != null && dscFluxosFiliais.size() > 0);
	}

	private String retornarDezPrimeirosFluxosFilhosOrganizados(List<String> dscFluxosFiliais) {
		String retorno = "Fluxos Filhos(até dez): ";
		for (int i = 0; i < (dscFluxosFiliais.size() > 9 ? 10 : dscFluxosFiliais.size()); i++) {
			retorno += dscFluxosFiliais.get(i) + " | ";
		}
		return retorno.substring(0, retorno.length() - 2);
	}

	@Override
	protected FluxoFilial beforeInsert(FluxoFilial bean) {
		FluxoFilial fluxoFilial = (FluxoFilial)bean;

		findOrganizaOrdemFluxo(fluxoFilial, null);

		return super.beforeInsert(bean);
	}

	@Override
	protected FluxoFilial beforeUpdate(FluxoFilial bean) {
		FluxoFilial fluxoFilial = (FluxoFilial)bean;

		return super.beforeUpdate(bean);
	}

	/**
	 * Consulta os fluxos a partir dos parametros informados
	 * @param idFilial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public List findFluxoFilialByFilial(Long idFilial, YearMonthDay dtVigenciaFinal) {
		return getFluxoFilialDAO().findFluxoFilialByFilial(idFilial, dtVigenciaFinal);
	}

	/**
	 * Consulta os fluxos a partir das filiais de origem e destino, alem do servico
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @return
	 */
	public List findFluxoFilialByFilialDestinoOrigemServico(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){
		 return getFluxoFilialDAO().findFluxoFilialByFilialDestinoOrigemServico(idFilialOrigem, idFilialDestino, idServico, dtConsulta);
	 }

	/**
	 * Retorna a distancia total percorrida por um determinado fluxo entre duas filiais
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return Object[] 
	 * [0]: idFluxoFilial
	 * [1]: nrDistancia
	 */
	public Object[] findDistanciaTotalFluxoFilialOrigemDestino(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia, Long idServico) {
		FluxoFilial fluxoFilial = getFluxoFilialDAO().findFluxoFilial(idFilialOrigem, idFilialDestino, dtVigencia, idServico);
		if(fluxoFilial != null) {
			return new Object[]{fluxoFilial.getIdFluxoFilial(), fluxoFilial.getNrDistancia()};
		}
		return null;
	}

	/**
	 * Retorna a distancia total percorrida pelas filiais contidas na lista recebido por parâmetro.
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return Integer nrDistancia
	 */
	public Integer findDistanciaTotalFluxoFilialOrigemDestino(List filiais, YearMonthDay dtVigencia) {	
		Integer nrDistancia = Integer.valueOf(0);

		if (filiais.size() <= 1) {
			return nrDistancia;
		} else {
			Filial filialOrigem = (Filial)filiais.get(0);
			Filial filialDestino = (Filial)filiais.get(1);

			filiais.remove(0);

			nrDistancia = findDistanciaTotalFluxoFilialOrigemDestino(filiais, dtVigencia);

			Object[] obj = findDistanciaTotalFluxoFilialOrigemDestino(
					filialOrigem.getIdFilial(),
					filialDestino.getIdFilial(),
					dtVigencia,
					null
			);
			if (obj != null)
				nrDistancia = IntegerUtils.add(nrDistancia,(Integer)obj[1]);
		}

		return nrDistancia;
	}

	/**
	 * Retorna o fluxo vigênte na data informada
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtVigencia
	 * @param idServico
	 * @return fluxo filial
	 */
	public FluxoFilial findFluxoFilial(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia, Long idServico) {
		FluxoFilial fluxoFilial = getFluxoFilialDAO().findFluxoFilial(idFilialOrigem, idFilialDestino, dtVigencia, idServico);
		return fluxoFilial;
	}

	/**
	 * Retorna o fluxo vigênte na data informada
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtVigencia
	 * @param idServico
	 * @param idMunicipio
	 * @return fluxo filial
	 */
	public FluxoFilial findFluxoFilialPadraoMCD(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia, Long idServico, Long idMunicipio) {
		FluxoFilial fluxoFilial = getFluxoFilialDAO().findFluxoFilialPadraoMCD(idFilialOrigem, idFilialDestino, dtVigencia, idServico, idMunicipio);
		return fluxoFilial;
	}

	/**
	 * Retorna o fluxo com maior data de início de vigência
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @return fluxo filial
	 */
	public FluxoFilial findUltimoFluxoFilial(Long idFilialOrigem, Long idFilialDestino, Long idServico) {
		FluxoFilial fluxoFilial = getFluxoFilialDAO().findUltimoFluxoFilial(idFilialOrigem, idFilialDestino, idServico);
		if((fluxoFilial == null) && (idServico != null)) {
			fluxoFilial = getFluxoFilialDAO().findUltimoFluxoFilial(idFilialOrigem, idFilialDestino, null);
		}
		return fluxoFilial;
	}

	public Integer getRowCountFluxoInicialOrigem(TypedFlatMap tfm) {
		return getFluxoFilialDAO().getRowCountFluxoInicialOrigem(tfm);
	}

	public Integer getRowCountFluxoInicialDestino(TypedFlatMap tfm) {
		return getFluxoFilialDAO().getRowCountFluxoInicialDestino(tfm);
	}

	public ResultSetPage findPagindatedFluxoInicialOrigem(TypedFlatMap tfm) {
		filialService.verificaExistenciaHistoricoFilial(tfm.getLong("filial.idFilial"), tfm.getYearMonthDay("dtVigenciaInicial"), tfm.getYearMonthDay("dtVigenciaFinal"));

		ResultSetPage rsp = getFluxoFilialDAO().findPagindatedFluxoInicialOrigem(tfm, FindDefinition.createFindDefinition(tfm));

		Long idFilial = tfm.getLong("filial.idFilial");
		Filial filial = filialService.findById(idFilial);
		String siglaNomeFilial = filial.getSiglaNomeFilial();
		String nmFilial = filial.getPessoa().getNmFantasia();
		String sgFilial = filial.getSgFilial();

		Iterator iterator = rsp.getList().iterator();
		while (iterator.hasNext()) {
			Map map = (Map)iterator.next();
			map.put("filialByIdFilialOrigem_siglaNomeFilial", siglaNomeFilial);
			map.put("filialByIdFilialOrigem_sgFilial", sgFilial);
			map.put("filialByIdFilialOrigem_pessoa_nmFantasia", nmFilial);
		}

		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	
	public List<FluxoFilial> findDistanciaByIdOrigemDestinoVigencia(long idOrigem, long idDestino, YearMonthDay dtInicioVigencia) {
		return getFluxoFilialDAO().findDistanciaByIdOrigemDestinoVigencia(idOrigem, idDestino, dtInicioVigencia);
	}
	
	
	public ResultSetPage findPagindatedFluxoInicialDestino(TypedFlatMap tfm) {
		filialService.verificaExistenciaHistoricoFilial(tfm.getLong("filial.idFilial"), tfm.getYearMonthDay("dtVigenciaInicial"), tfm.getYearMonthDay("dtVigenciaFinal"));

		ResultSetPage rsp = getFluxoFilialDAO().findPagindatedFluxoInicialDestino(tfm, FindDefinition.createFindDefinition(tfm));		

		Long idFilial = tfm.getLong("filial.idFilial");
		Filial filial = filialService.findById(idFilial);
		String siglaNomeFilial = filial.getSiglaNomeFilial();
		String nmFilial = filial.getPessoa().getNmFantasia();
		String sgFilial = filial.getSgFilial();		

		Iterator iterator = rsp.getList().iterator();
		while (iterator.hasNext()) {
			Map map = (Map)iterator.next();
			map.put("filialByIdFilialDestino_siglaNomeFilial", siglaNomeFilial);
			map.put("filialByIdFilialDestino_sgFilial", sgFilial);
			map.put("filialByIdFilialDestino_pessoa_nmFantasia", nmFilial);
		}
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	
	/**
	 * Obtem o valor do PPE através do fluxo filial
	 * 
	 * @param  idFilialOrigem
	 * @param  idFilialDestino
	 * @param  idServico
	 * @param  dtVigencia
	 * @return prazo total
	 */
	public Integer findValorPPE(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtVigencia){
		
		if(dtVigencia == null){
			dtVigencia = JTDateTimeUtils.getDataAtual();
		}
		
		FluxoFilial fluxo = findFluxoFilial(idFilialOrigem, idFilialDestino, dtVigencia, idServico);		
		if(fluxo == null){
			return 0;
		}
		
		if(fluxo.getFilialByIdFilialReembarcadora() != null){
			Long idFilialReembarcadora = fluxo.getFilialByIdFilialReembarcadora().getIdFilial();
			return fluxo.getNrPrazo() + findValorPPE(idFilialReembarcadora,idFilialDestino,idServico,dtVigencia);
		}			
		return fluxo.getNrPrazo();
	} 
	

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setFluxoFilialDAO(FluxoFilialDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private final FluxoFilialDAO getFluxoFilialDAO() {
		return (FluxoFilialDAO) getDao();
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	/**
	 * @param ppeService The ppeService to set.
	 */
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
}
}
