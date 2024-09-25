package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.service.InformacaoDocServicoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DominioAgrupamento;
import com.mercurio.lms.vendas.model.FormaAgrupamento;
import com.mercurio.lms.vendas.model.FormaAgrupamentoListBoxElement;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.dao.FormaAgrupamentoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.formaAgrupamentoService"
 */
public class FormaAgrupamentoService extends CrudService<FormaAgrupamento, Long> {

	private DominioAgrupamentoService dominioAgrupamentoService;
	private DomainValueService domainValueService;
	private InformacaoDocServicoService informacaoDocServicoService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;

	public void setInformacaoDocServicoService(
			InformacaoDocServicoService informacaoDocServicoService) {
		this.informacaoDocServicoService = informacaoDocServicoService;
	}

	public void setInformacaoDoctoClienteService(
			InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	/**
	 * Recupera uma instância de <code>FormaAgrupamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map findByIdMap(Long id) {
		TypedFlatMap retorno = new TypedFlatMap();
		FormaAgrupamento fagrup = (FormaAgrupamento)super.findById(id);

		retorno.put("idFormaAgrupamento",id);
		retorno.put("cliente.idCliente",fagrup.getCliente().getIdCliente());
		retorno.put("cliente.pessoa.nrIdentificacao",fagrup.getCliente().getPessoa().getNrIdentificacao());
		retorno.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(fagrup.getCliente().getPessoa().getTpIdentificacao(), fagrup.getCliente().getPessoa().getNrIdentificacao()));
		retorno.put("cliente.pessoa.nmPessoa",fagrup.getCliente().getPessoa().getNmPessoa());
		retorno.put("tpSituacao",fagrup.getTpSituacao().getValue());
		retorno.put("dsFormaAgrupamento",fagrup.getDsFormaAgrupamento());
		retorno.put("blAutomatico",fagrup.getBlAutomatico());
		retorno.put("nrOrdemPrioridade",fagrup.getNrOrdemPrioridade());
		retorno.put("sqCorporativo",fagrup.getSqCorporativo());

		List<DominioAgrupamento> dominioAgrupamentos = fagrup.getDominioAgrupamentos();
		
		Collections.sort(dominioAgrupamentos, new Comparator<DominioAgrupamento>() {
			public int compare(DominioAgrupamento o1, DominioAgrupamento o2) {
				return o1.getNrOrdemPrioridade().compareTo(o2.getNrOrdemPrioridade());
			}
			
		});
		
		int i = 0;
		List list = new ArrayList();
		for(Iterator it=dominioAgrupamentos.iterator();it.hasNext();){
			DominioAgrupamento da = (DominioAgrupamento)it.next();
			FormaAgrupamentoListBoxElement falb = dominioAgrupamento2FormaAgrupamentoListBoxElement(da);

			TypedFlatMap mapRet = new TypedFlatMap();

			mapRet.put("idComposto",falb.getIdComposto());
			mapRet.put("descricao",falb.getDescricao()); 
			mapRet.put("tipo",falb.getTipo());
			mapRet.put("valor",falb.getValor());
			mapRet.put("nrOrdemPrioridade",da.getNrOrdemPrioridade());
			mapRet.put("idDominioAgrupamento",da.getIdDominioAgrupamento());
			mapRet.put("id",falb.getId());
			i++;

			list.add(mapRet);
		}

		retorno.put("aux_source", list);

		return retorno;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TypedFlatMap valores) {
		FormaAgrupamento fagrup = new FormaAgrupamento();
		fagrup.setBlAutomatico(valores.getBoolean("blAutomatico"));
		fagrup.setDsFormaAgrupamento(valores.getVarcharI18n("dsFormaAgrupamento"));
		fagrup.setIdFormaAgrupamento(valores.getLong("idFormaAgrupamento"));
		fagrup.setTpSituacao(valores.getDomainValue("tpSituacao"));
		fagrup.setCliente(new Cliente(valores.getLong("cliente.idCliente")));
   		fagrup.setDominioAgrupamentos(dominioAgrupamentosFromLBEs(valores.getList("aux_source"),fagrup));
   		fagrup.setNrOrdemPrioridade(valores.getLong("nrOrdemPrioridade"));
   		fagrup.setSqCorporativo(valores.getLong("sqCorporativo"));
		return super.store(fagrup);
	}

	/**
	 * Monta uma lista de DominiosAgrupamentos através dos dados da classa genérica FormaAgrupamentoListBoxElement
	 * @param lbes
	 * @param fagrup
	 * @return
	 */
	private List dominioAgrupamentosFromLBEs(List lbes, FormaAgrupamento fagrup) {
		dominioAgrupamentoService.removeDominiosAgrupamentosByFormaAgrupamento(fagrup.getIdFormaAgrupamento());

		List dominioAgrupamentos = new LinkedList();

   		if(lbes!=null) {
   			byte nrOrdem = 1;
			for(Iterator it=lbes.iterator();it.hasNext();){
				FormaAgrupamentoListBoxElement falb = new FormaAgrupamentoListBoxElement((Map) it.next());

				DominioAgrupamento dominioAgrupamento = new DominioAgrupamento();
				InformacaoDocServico informacaoDocServico = null;
				InformacaoDoctoCliente informacaoDctoCliente = null;

				if(falb.getTipo().equals("dominio")) {
					dominioAgrupamento.setTpCampo(new DomainValue(falb.getValor()));
				}

				if(falb.getTipo().equals("info_dcto_servico")){
					dominioAgrupamento.setTpCampo(new DomainValue("CS"));

					informacaoDocServico = informacaoDocServicoService.findById(falb.getId());
					dominioAgrupamento.setInformacaoDocServico(informacaoDocServico);
				}

				if(falb.getTipo().equals("info_dcto_cliente")){
					dominioAgrupamento.setTpCampo(new DomainValue("CS"));

					informacaoDctoCliente = informacaoDoctoClienteService.findById(falb.getId());
					dominioAgrupamento.setInformacaoDoctoCliente(informacaoDctoCliente);
				}

				if( falb.getNrOrdemPrioridade() != null ){
					dominioAgrupamento.setNrOrdemPrioridade(falb.getNrOrdemPrioridade());	
				} else {
					dominioAgrupamento.setNrOrdemPrioridade(Byte.valueOf(nrOrdem++));
				}

				dominioAgrupamento.setFormaAgrupamento(fagrup);
				dominioAgrupamentos.add(dominioAgrupamento);
			}
   		}

		return dominioAgrupamentos;
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

	protected void beforeRemoveByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long element = (Long) iter.next();
			dominioAgrupamentoService.removeDominiosAgrupamentosByFormaAgrupamento(element);			
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		if(id != null){
			dominioAgrupamentoService.removeDominiosAgrupamentosByFormaAgrupamento((Long)id);
		}
		super.beforeRemoveById(id);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setFormaAgrupamentoDAO(FormaAgrupamentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private FormaAgrupamentoDAO getFormaAgrupamentoDAO() {
		return (FormaAgrupamentoDAO) getDao();
	}

	/**
	 * Busca a lista de Formas de Agrupamento, buscando :
	 * -	valores do domínio DM_CAMPO_AGRUPAMENTO
	 * -	valores da tabela INFORMACAO_DOCTO_CLIENTE
	 * -	valores da tabela INFORMACAO_DOC_SERVICO
	 * @param criterions
	 * @return 
	 */
	public List findFormasAgrupamento(Map criterions){
		List domains = null;
		List informacaoDocServico = null;
		List informacaoDocCliente = null;
		List formasAgrupamento = new ArrayList();
		FormaAgrupamentoListBoxElement listBox = null;

		domains = getFormaAgrupamentoDAO().findFormasAgrupamentoDomain(criterions);
		informacaoDocServico = getFormaAgrupamentoDAO().findFormasAgrupamentoInformacaoDocServico(criterions);

		for (Iterator iterator = domains.iterator(); iterator.hasNext();) {
			DomainValue element = (DomainValue) iterator.next();
			VarcharI18n varcharI18n = element.getDescription();

			listBox = new FormaAgrupamentoListBoxElement(element.getId(),
														 element.getValue(),
														 varcharI18n.toString(),
														 "dominio");

			formasAgrupamento.add(listBox);		
		}

		formasAgrupamento.addAll(informacaoDocServico);
		if( criterions != null ){
			if( criterions.get("idCliente") != null && !((String)criterions.get("idCliente")).equals("") ){
				informacaoDocCliente = getFormaAgrupamentoDAO().findFormasAgrupamentoInformacaoDocCliente(criterions);
			}
			if( informacaoDocCliente != null ){
				formasAgrupamento.addAll(informacaoDocCliente);
			}
		}

		return formasAgrupamento;
	}

	public FormaAgrupamentoListBoxElement dominioAgrupamento2FormaAgrupamentoListBoxElement(DominioAgrupamento da){
		
		FormaAgrupamentoListBoxElement falb = new FormaAgrupamentoListBoxElement();
		DomainValue dv = null;
		
		if(da.getInformacaoDocServico() != null) {
			falb.setId(da.getInformacaoDocServico().getIdInformacaoDocServico());
			falb.setValor("CS");
			falb.setDescricao(da.getInformacaoDocServico().getDsCampo());
			falb.setTipo("info_dcto_servico");
			falb.setNrOrdemPrioridade(da.getNrOrdemPrioridade());
			falb.setIdDominioAgrupamento(da.getIdDominioAgrupamento());
		} else if(da.getInformacaoDoctoCliente() != null) {
			falb.setId(da.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente());
			falb.setValor("CS");
			falb.setDescricao(da.getInformacaoDoctoCliente().getDsCampo());
			falb.setTipo("info_dcto_cliente");
			falb.setNrOrdemPrioridade(da.getNrOrdemPrioridade());
			falb.setIdDominioAgrupamento(da.getIdDominioAgrupamento());
		} else {
			
			dv = getFormaAgrupamentoDAO().findDomainValueByNameAndValue("DM_FORMA_AGRUPAMENTO",da.getTpCampo().getValue());
						
			falb.setId(dv.getId());
			falb.setValor(da.getTpCampo().getValue());
			falb.setDescricao(da.getTpCampo().getDescription().getValue());
			falb.setTipo("dominio");
			falb.setNrOrdemPrioridade(da.getNrOrdemPrioridade());
			falb.setIdDominioAgrupamento(da.getIdDominioAgrupamento());
		}
		return falb;
	}

	public List find(Map criterions){
		return findFormasAgrupamento(criterions);
	}

	public List findByCliente(Map criterions){
		   	List listaOrder = new ArrayList();
			listaOrder.add("dsFormaAgrupamento:asc");
			return getFormaAgrupamentoDAO().findListByCriteria(criterions,listaOrder);
	}

	/**
	 * @param dominioAgrupamentoService The dominioAgrupamentoService to set.
	 */
	public void setDominioAgrupamentoService(DominioAgrupamentoService dominioAgrupamentoService) {
		this.dominioAgrupamentoService = dominioAgrupamentoService;
	}

	/**
	 * @return Returns the dominioAgrupamentoService.
	 */
	public DominioAgrupamentoService getDominioAgrupamentoService() {
		return dominioAgrupamentoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	/**
	 * FindPaginated para manter Formas Agrupamento
	 *
	 * @author José Rodrigo Moraes
	 * @since 03/11/2006
	 *
	 * @param idCliente
	 * @param situacao
	 * @param dsFormaAgrupamento
	 * @param blAutomatico
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedSpecific(Long idCliente, String situacao,
			String dsFormaAgrupamento, String blAutomatico,
			FindDefinition findDef) {
		return getFormaAgrupamentoDAO().findPaginatedSpecific(idCliente,
				situacao, dsFormaAgrupamento, blAutomatico, findDef);
	}

	/**
	 * Row Count para Manter formas Agrupamento
	 *
	 * @author José Rodrigo Moraes
	 * @since 03/11/2006
	 *
	 * @param idCliente
	 * @param situacao
	 * @param dsFormaAgrupamento
	 * @param blAutomatico
	 * @return
	 */
	public Integer getRowCountSpecific(Long idCliente, String situacao,
			String dsFormaAgrupamento, String blAutomatico) {
		return getFormaAgrupamentoDAO().getRowCountSpecific(idCliente,
				situacao, dsFormaAgrupamento, blAutomatico);
	}

}
