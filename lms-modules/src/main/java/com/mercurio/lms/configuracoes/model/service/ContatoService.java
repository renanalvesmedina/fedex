package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.dao.ContatoDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.facade.radar.impl.WhiteListFacadeImpl;
import com.mercurio.lms.facade.radar.impl.whitelist.Contatos;
import com.mercurio.lms.facade.radar.impl.whitelist.Request;
import com.mercurio.lms.facade.radar.impl.whitelist.TipoWhiteList;
import com.mercurio.lms.tracking.util.TrackingContantsUtil;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.contatoService"
 */
public class ContatoService extends CrudService<Contato, Long> {
	private EspecializacaoPessoaService especializacaoPessoaService;
	private PessoaService pessoaService;
	private TelefoneContatoService telefoneContatoService;
	private DispositivoContatoService dispositivoContatoService;
	private BlackListService blackListService;
	private static final String TP_CONTATO_CN = "RD";
	private static final Logger LOGGER = LogManager.getLogger(WhiteListFacadeImpl.class);
	private static final String DM_TIPO_CONTATO_PESSOA = "DM_TIPO_CONTATO_PESSOA";

	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>Contato</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Contato findById(java.lang.Long id) {
		return (Contato) super.findById(id);
	}

	/**
	 * Valida quantidade mínima de Contato por Pessoa.<BR>
	 * Este serviço verifica quantos contatos uma pessoa possui, validando com a quantidade minima informada.<BR>
	 * O retorno será verdadeiro se a quantidade de contatos for igual ou maior que o mínimo informado. 
	 *@author Robson Edemar Gehl
	 * @param pessoa Pessoa a serem verificados seus endereços 
	 * @param quantidadeMinima quantidade mínima de contatos que uma Pessoa pode ter (para verificação)
	 * @return Boolean.TRUE se quantidade mínima contemplada com o parametro; Boolean.FALSE, caso contrário.
	 */
	public Boolean validateQuantidadeMinima(Pessoa pessoa, long quantidadeMinima){
		return getContatoDAO().validateQuantidadeMinima(pessoa, quantidadeMinima);
	}

	/**
	 * Recupera uma instância de <code>Contato</code> a partir do dsEmail.
	 * @param email
	 * @return idContato
	 */
	public Contato findIdContatoByEmail(String email){
		List result = getContatoDAO().findIdContatoByEmail(email);
		if(!result.isEmpty()){
			Iterator iterator = result.iterator();
			Contato contato = (Contato)iterator.next();
			return contato;
		}	
		return null;
	}

	public List find(Map criteria) {
		List orderBy = new ArrayList();
		orderBy.add("nmContato");
		return this.getContatoDAO().findListByCriteria(criteria, orderBy);
	}

	/**
	 * Apaga uma entidade através do Id. Remove antes os telefones do contato
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeByIdCascade(java.lang.Long id) {
		Contato contato = findById(id);
		//Remover os telefones do contato
		for (Iterator it = contato.getTelefoneContatos().iterator(); it.hasNext();) {
			telefoneContatoService.removeById(((TelefoneContato)it.next()).getIdTelefoneContato());
		}
		removeById(id);
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			removeById((Long) iter.next());
		}
	}

	@Override
	protected Contato beforeStore(Contato bean) {
		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(((Contato)bean).getPessoa().getIdPessoa());
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Contato bean) {
		return super.store(bean);
	}

	protected void beforeRemoveById(Long id) {
		Contato contato = this.get(id);

		Short cdEspecializacao = null;

		//Se é o último contato, não pode apagar ele se ele pretence a uma dos tipos de pessoa informado.
		if (isLastContatoPessoa(contato.getPessoa().getIdPessoa())){
			List lstCdEspecializacao = new ArrayList();

			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_FILIAL);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_EMPRESA_COBRANCA);
			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_CLIENTE_ESPECIAL);

			cdEspecializacao = especializacaoPessoaService.isEspecializado(contato.getPessoa().getIdPessoa(), lstCdEspecializacao);
		}

		//Se o contato a apagar é uma referência professional e que tem 3 ou menos contatos de referência professional nessa pessoa 
		//e que a pessoa é um motorista eventual ou agregado, lançar uma exception
		if (cdEspecializacao == null && contato.getTpContato().getValue().equals("RP") && isThirdContatoProfessionalPessoa(contato.getPessoa().getIdPessoa())){
			List lstCdEspecializacao = new ArrayList();

			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA_EVENTUAL);

			cdEspecializacao = especializacaoPessoaService.isEspecializado(contato.getPessoa().getIdPessoa(), lstCdEspecializacao);			
		}

		//Se o contato a apagar é uma referência pessoal e que tem 3 ou menos contatos de referência pessoal nessa pessoa 
		//e que a pessoa é um motorista eventual ou agregado, lançar uma exception
		if (cdEspecializacao == null && contato.getTpContato().getValue().equals("RS") && isThirdContatoPessoalPessoa(contato.getPessoa().getIdPessoa())){
			List lstCdEspecializacao = new ArrayList();

			lstCdEspecializacao.add(ConstantesConfiguracoes.IS_MOTORISTA_EVENTUAL);

			cdEspecializacao = especializacaoPessoaService.isEspecializado(contato.getPessoa().getIdPessoa(), lstCdEspecializacao);			
		}			

		if (cdEspecializacao != null){
			throw new BusinessException("LMS-27067", new Object[]{especializacaoPessoaService.getLabel(cdEspecializacao)});
		}

		//Valida se o usuario logado pode alterar a pessoa
		pessoaService.validateAlteracaoPessoa(contato.getPessoa().getIdPessoa());

		super.beforeRemoveById(id);
	}

	/**
	 * Método que busca todos os Contatos da pessoa pelo ID da Pessoa.
	 */
	public List findContatosByIdPessoa(Long idPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(Contato.class);
		dc.add( Restrictions.eq("pessoa.idPessoa", idPessoa));

		return getContatoDAO().findByDetachedCriteria(dc);	
	}

	/**
	 * Método que busca todos os Contatos da pessoa pelo ID da Pessoa.
	 */
	public List<Contato> findContatosByIdPessoaTpContato(Long idPessoa, String tpContato) {
		DetachedCriteria dc = DetachedCriteria.forClass(Contato.class);
		dc.add( Restrictions.eq("pessoa.idPessoa", idPessoa));
		dc.add( Restrictions.eq("tpContato", tpContato));

		return getContatoDAO().findByDetachedCriteria(dc);	
	}

	/**
	 * Busca os dados de contato para montagem do combo
	 * @param criterions
	 * @return List com os dados do combo de contato 
	 */
	public List findComboContatos(Map criterions){
		return getContatoDAO().findComboContatos(criterions);
	}

	/**
	 * Busca os dados de contato para montagem do combo
	 * @param idPessoa
	 * @return List com os dados do combo de contato 
	 */
	public List findComboContato(Long idPessoa){
		return getContatoDAO().findComboContato(idPessoa);
	}

	/**
	 * Informa se é o último contato da pessoa.
	 * 
	 * @author Mickaël Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isLastContatoPessoa(Long idPessoa){
		List lstContato = findContatosByIdPessoa(idPessoa);
		if (lstContato.size() < 2){
			return true;
		}
		return false;
	}

	/**
	 * Informa se tem 3 referências professionais ou menos no contato da pessoa.
	 * 
	 * @author Mickaël Jalbert
	 * @since 25/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isThirdContatoProfessionalPessoa(Long idPessoa){
		List lstContato = findContatosByIdPessoaTpContato(idPessoa, "RP");
		if (lstContato.size() <= 3){
			return true;
		}
		return false;
	} 

	/**
	 * Informa se tem 3 referências pessoais ou menos no contato da pessoa.
	 * 
	 * @author Mickaël Jalbert
	 * @since 25/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isThirdContatoPessoalPessoa(Long idPessoa){
		List lstContato = findContatosByIdPessoaTpContato(idPessoa, "RS");
		// Caso a pessoa tenha 3 ou menos de 3 contatos, retorna true
		if (lstContato.size() <= 3){
			return true;
		}
		return false;
	}

	public List<Contato> findContato(Long idUsuario, String tpContato, Long idPessoa) {
		return getContatoDAO().findContato(idUsuario, tpContato, idPessoa);
	}

	public Contato findContato(Long idContato, Long idPessoa, String tpContato) {
		return getContatoDAO().findContato(idContato, idPessoa, tpContato);
	}

	public List<Contato>findContatoByIdMonitoramentoDocEletronic(Long idMonitoramentoDocEletronic, String tpContato){
		return getContatoDAO().findContatoByIdMonitoramentoDocEletronic(idMonitoramentoDocEletronic, tpContato);
	}

	public Contato findContatoByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		return getContatoDAO().findContatoByIdPessoaAndTipoContato(idPessoa, tpContato);
	}

	public List<Contato> findContatosByIdPessoaAndTipoContato(Long idPessoa, String tpContato) {
		return getContatoDAO().findContatosByIdPessoaAndTipoContato(idPessoa, tpContato);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setContatoDAO(ContatoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ContatoDAO getContatoDAO() {
		return (ContatoDAO) getDao();
	}

	public void setEspecializacaoPessoaService(EspecializacaoPessoaService especializacaoPessoaService) {
		this.especializacaoPessoaService = especializacaoPessoaService;
	}
	public void setTelefoneContatoService(TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	@SuppressWarnings("unchecked")
	public void removeSemValidacaoById(Long idContato) {
		getDao().removeById(idContato);
		
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedContatoPessoa(TypedFlatMap tfm) {
		return super.findPaginated(tfm);
	}

	public Integer getRowCountContato(TypedFlatMap tfm) {
		return super.getRowCount(tfm);
	}
	
	/**
	 * Busca contato por id e da feth na pessoa
	 * @param id
	 * @return
	 */
	public Contato findContatoById(Long id) {				
		return  getContatoDAO().findContatoById(id);		
	}
    
    public List<Map<String, Object>> findContatoCrm(Map<String, Object> params) {

        List<Object[]> retornoFindLocation = getContatoDAO().findContatoCrm(params);

        List<Map<String, Object>> listaRetorno = new ArrayList<Map<String, Object>>();
        if (retornoFindLocation != null && !retornoFindLocation.isEmpty()) {
            for (Object[] data : retornoFindLocation) {
                Map<String, Object> contato = new HashMap<String, Object>();
                contato.put(ContatoDAO.NOME_CONTATO, data[0]);
                contato.put(ContatoDAO.DS_FUNCAO, data[1]);
                contato.put(ContatoDAO.DS_EMAIL, data[2]);
                contato.put(ContatoDAO.FONE_CEL, data[3]);
                contato.put(ContatoDAO.FONE_COM, data[4]);
                contato.put(ContatoDAO.FONE_RES, data[5]);

                listaRetorno.add(contato);
            }
        }

        return listaRetorno;
    }
    
    /**
	 * 
	 * @param bean
	 * @param listContatos
	 * 
	 * @return java.io.Serializable
	 */
	public java.io.Serializable storeContato(Contato bean, Map<String, Object> listTelefone) {
		Long idContato = (Long) this.store(bean);
		
		/* 
		 * Depois de atualizar o contato, realiza dos telefones.
		 */
		updateTelefone(idContato, listTelefone);
		
		return idContato;
	}
	
	/**
	 * Este método foi criado para substituir uma tela de CRUD, baseado em um
	 * componente select multiple values, para dizer os telefones de um contato.
	 * 
	 * @param toAdd
	 * @param toRemove
	 */
	@SuppressWarnings("unchecked")
	private void updateTelefone(Long idContato, Map<String, Object> listTelefone){
		if(listTelefone == null || listTelefone.isEmpty()){
			return;
		}
		
		List<Map<String, Object>> added = (List<Map<String, Object>>) listTelefone.get("added");
		List<Map<String, Object>> removed = (List<Map<String, Object>>) listTelefone.get("removed");
		List<Map<String, Object>> updated = (List<Map<String, Object>>) listTelefone.get("updated");
		
		if(!removed.isEmpty()){
			List<Long> toRemove = new ArrayList<Long>();
			
			for (Map<String, Object> telefonePessoa : removed) {
				toRemove.add(MapUtils.getLong(MapUtils.getMap(telefonePessoa, "telefone"), "idTelefoneContato"));
			}
			
			telefoneContatoService.removeByIds(toRemove);
		}
		
		if(!updated.isEmpty()){
			added.addAll(updated);
		}
		
		if(!added.isEmpty()){
			Contato contato = new Contato();
			contato.setIdContato(idContato);
			
			List<TelefoneContato> toAdd = new ArrayList<TelefoneContato>();
			for (Map<String, Object> telefonePessoa : added) {
				TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
				telefoneEndereco.setIdTelefoneEndereco(MapUtils.getLong(MapUtils.getMap(telefonePessoa, "telefone"), "idTelefoneEndereco"));
				
				TelefoneContato telefoneContato = new TelefoneContato();
				telefoneContato.setIdTelefoneContato(MapUtils.getLong(MapUtils.getMap(telefonePessoa, "telefone"), "idTelefoneContato"));
				telefoneContato.setNrRamal(MapUtils.getString(telefonePessoa, "nrRamal"));
				telefoneContato.setContato(contato);
				telefoneContato.setTelefoneEndereco(telefoneEndereco);
								
				toAdd.add(telefoneContato);
			}
			
			telefoneContatoService.storeAll(toAdd);
		}		
	}
	
	public List<Contato> createContatosWhiteList(TypedFlatMap criteria, DoctoServico doctoServico, List<String> msgErro) {
		return TipoWhiteList.from(criteria).isForEmail()
				? populaContato(criteria, doctoServico, msgErro)
				: dispositivoContato(Request.from(criteria), doctoServico);
	}

	private List<Contato> dispositivoContato(Request request, DoctoServico documentoServico) {
		return Contatos.from(dispositivoContatoService.findDispositivoByToken(request.getToken()))
				.plataforma(domainValueService.findDomainValueByValue("DM_TP_PLATAFORMA_DISPOSITIVO_CONTATO", request.getPlataforma()))
				.token(request.getToken())
				.ddd(request.getDdd())
				.numero(request.getNumero())
				.versao(request.getVersao())
				.tipo(domainValueService.findDomainValueByValue(DM_TIPO_CONTATO_PESSOA, TP_CONTATO_CN))
				.pessoa(pessoaService.findById(documentoServico.getClienteByIdClienteDestinatario().getIdCliente()))
				.asList();
	}
	
	private List<Contato> populaContato(TypedFlatMap criteria, DoctoServico doctoServico, List<String> msgErro) {
		List<Contato> lista = new ArrayList<Contato>();
		String allEmail = criteria.getString(TrackingContantsUtil.WHITE_LIST_PRM_LISTA_EMAILS);
		String[] emails = allEmail.split(",");
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DomainValue tpContato = domainValueService.findDomainValueByValue(DM_TIPO_CONTATO_PESSOA, TP_CONTATO_CN);
		Pessoa pessoa = pessoaService.findById(doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
		// verifica se o contato está na lista de blacklist
		List<Object[]> listEmailNaBlack = blackListService.findBlackListByEmails(emails);
		if(!listEmailNaBlack.isEmpty()){
			msgErro.add(configuracoesFacade.getMensagem("radarContatosBlackList", new String[]{convertEmailsBlackListToString(listEmailNaBlack)}));
		}else{
			for (String email : emails) {
				Contato contato = findContatoByEmail(email);
				if(contato == null){
					contato = new Contato();
					contato.setNmContato("RADAR");
					contato.setDsEmail(email.toLowerCase());
					contato.setTpContato(tpContato);
					contato.setPessoa(pessoa);
					contato.setDtCadastro(dataAtual);
				} else {
					contato.setDtUltimaMovimentacao(dataAtual);
				}
				lista.add(contato);
			}
		}
		return lista;
	}
	
	private String convertEmailsBlackListToString(List<Object[]> listEmailNaBlack) {
		String emails = "";
		for(int i = 0; i < listEmailNaBlack.size(); i++){
			emails += String.valueOf(listEmailNaBlack.get(i)) + ", ";
		}
		return removeUltimaVirgulaTexto(emails);
	}
	
	private String removeUltimaVirgulaTexto(String emailsSucesso) {
		if(emailsSucesso.length() > 0){
			emailsSucesso = emailsSucesso.trim();
			emailsSucesso = emailsSucesso.substring(0, emailsSucesso.length() - 1);
		}
		return emailsSucesso;
	}

	public Contato findContatoByEmail(String email) {
		return getContatoDAO().findContatoByEmail(email);
	}
	
	public Object[] findContatoTelefoneFilialByIdFilialTpContato(Long idFilial, String tpContato) {
		return getContatoDAO().findContatoTelefoneFilialByIdFilialTpContato(idFilial, tpContato);
	}

	public void setDispositivoContatoService(DispositivoContatoService dispositivoContatoService) {
		this.dispositivoContatoService = dispositivoContatoService;
	}

	public void setBlackListService(BlackListService blackListService) {
		this.blackListService = blackListService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
