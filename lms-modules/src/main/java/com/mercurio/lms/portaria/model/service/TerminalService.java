package com.mercurio.lms.portaria.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.dao.TerminalDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.terminalService"
 */
public class TerminalService extends CrudService<Terminal, Long> {
	private PessoaService pessoaService;
	private DocaService docaService;
	private BoxService boxService;
	private FilialService filialService;
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>Terminal</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Terminal findById(java.lang.Long id) {
		return (Terminal)super.findById(id);
	}

	public List findByIdView(Long idTerminal) {
		return getTerminalDAO().findByIdView(idTerminal);
	}

	public Filial findFilialUsuarioLogado() {
		return filialService.getFilialUsuarioLogado();
	}

	public List findTerminalVigenteByFilial(Long idFilial){
		return getTerminalDAO().findTerminalVigenteByFilial(idFilial);
	}
	
	public List findTerminalVigenteByFilial(Long idFilial, Long idTerminal){
		return getTerminalDAO().findTerminalVigenteByFilial(idFilial, idTerminal);
	}

	public List findTerminalVigenteOrVigenciaFuturaByFilial(Long idFilial) {
		return getTerminalDAO().findTerminalVigenteOrVigenciaFuturaByFilial(idFilial);
	}

	/**
	 * Verifica se a vigencia do terminal esta dentro do periodo informado
	 * @param idTerminal
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean findTerminalValidaVigencia(Long idTerminal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getTerminalDAO().findTerminalValidaVigencia(idTerminal, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		Terminal t = null;
		for(int x = 0; x < ids.size(); x++) {
			t = findById((Long)ids.get(x));
			JTVigenciaUtils.validaVigenciaRemocao(t);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		Terminal t = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(t);
		super.beforeRemoveById(id);
	}

	protected Terminal beforeStore(Terminal bean) {
		Terminal t = (Terminal)bean;
		filialService.verificaExistenciaHistoricoFilial(t.getFilial().getIdFilial(),t.getDtVigenciaInicial(),t.getDtVigenciaFinal());
		if (getTerminalDAO().findTerminalByFilialAndNameVigente(t.getIdTerminal(),t.getFilial().getIdFilial(),
				t.getPessoa().getNmPessoa(),t.getDtVigenciaInicial(),t.getDtVigenciaFinal()))
			throw new BusinessException("LMS-00003");

		if (vigenciaService.validateIntegridadeVigencia(Doca.class,"terminal",t))
			throw new BusinessException("LMS-06025");

		return super.beforeStore(bean);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
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
	public Terminal store(Terminal bean) {
		vigenciaService.validaVigenciaBeforeStore(bean);
		pessoaService.store(bean);
		super.store(bean);
		return bean;
	}

	public List findCombo(TypedFlatMap criteria) {
		List rs = getTerminalDAO().findCombo(criteria);
		return rs;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getTerminalDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List rs = rsp.getList();
		for (int x = 0; x < rs.size(); x++) {
			Map result = (Map)rs.get(x);
			Map filial_pessoa = new HashMap();
			filial_pessoa.put("nmFantasia",result.get("filial_pessoa_nmFantasia"));
			Map filial = new HashMap();
			filial.put("pessoa",filial_pessoa);
			filial.put("sgFilial",result.get("filial_sgFilial"));
			Map pessoa = new HashMap();
			pessoa.put("nmPessoa",result.get("pessoa_nmPessoa"));

			result.put("pessoa",pessoa);
			result.put("filial",filial);

			result.remove("filial_pessoa_nmPessoa");
			result.remove("filial_sgFilial");
			result.remove("pessoa_nmPessoa");

			result.put("nrDocas",docaService.getRowCountDocasVigenteByTerminal((Long)result.get("idTerminal")));
			result.put("nrBoxes",boxService.getRowCountBoxesVigenteByTerminal((Long)result.get("idTerminal")));

		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getTerminalDAO().getRowCount(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTerminalDAO(TerminalDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TerminalDAO getTerminalDAO() {
		return (TerminalDAO) getDao();
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	public void setDocaService(DocaService docaService) {
		this.docaService = docaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}