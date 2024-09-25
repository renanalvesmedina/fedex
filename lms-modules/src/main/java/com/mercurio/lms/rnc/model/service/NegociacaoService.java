
package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.Negociacao;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.NegociacaoDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.negociacaoService"
 */
public class NegociacaoService extends CrudService<Negociacao, Long> {

	private UsuarioService usuarioService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private FilialService filialService;

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService){
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setNegociacaoDAO(NegociacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private NegociacaoDAO getNegociacaoDAO() {
        return (NegociacaoDAO) getDao();
    }

	
	/**
	 * Recupera uma instância de <code>Negociacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Negociacao findById(java.lang.Long id) {
        return (Negociacao)super.findById(id);
    }
    
    public Integer getRowCountCustom(Long idNaoConformidade, Long idOcorrenciaNaoConformidade) {
    	return this.getNegociacaoDAO().getRowCountCustom(idNaoConformidade, idOcorrenciaNaoConformidade);
    }
    
    public ResultSetPage findPaginatedCustom(Long idNaoConformidade, Long idOcorrenciaNaoConformidade, FindDefinition fd) {
    	ResultSetPage rsp =this.getNegociacaoDAO().findPaginatedCustom(idNaoConformidade, idOcorrenciaNaoConformidade, fd);
    	List result = new AliasToTypedFlatMapResultTransformer().transformListResult(rsp.getList());
    	rsp.setList(result);
    	return rsp;
    }
    
	/**
	 * Apaga uma entidade através do Id caso lhe seja permitido, ou seja
	 * o usuário logado seja o usuário que gerou a negocição.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	Usuario usuario = SessionUtils.getUsuarioLogado();
        
    	Map criteria = new HashMap();
    	criteria.put("idUsuario", usuario.getIdUsuario());
    	
        List idsNegociacao = new ArrayList();
        idsNegociacao.add(id);
    	criteria.put("idsNegociacao", idsNegociacao);
    	
        List removeList = getNegociacaoDAO(). findByUsuario(criteria);
        
        if (removeList.size()>0)
        	super.removeByIds(removeList);
        else
        	throw new BusinessException("LMS-00030");
    }
    
	/**
	 * Apaga uma entidade através do Id, caso ela pertença ao usuário que está  
	 * removendo-a.
	 *
	 * @param List ids indica as entidades que deveram ser removidas.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	Usuario usuario = SessionUtils.getUsuarioLogado();
         
    	Map criteria = new HashMap();
    	criteria.put("idUsuario", usuario.getIdUsuario());
    	criteria.put("idsNegociacao", ids);
    	
        List removeList = getNegociacaoDAO(). findByUsuario(criteria);
        
        if (removeList.size()!=ids.size()) {
    		throw new BusinessException("LMS-12001");
        } else if (removeList.size()>0){
        	super.removeByIds(removeList);
        }
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * Caso o usuario que esta editando o registro seja o mesmo que o adicionou, ele podera
	 * edita-lo caso o contrario nao sera possivel.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Negociacao storeByRegistrarNegociacoes(Long idNegociacao, String dsNegociacao, List idsOcorrenciaNaoConformidade) {
    	Negociacao negociacao = null;
    	if (idNegociacao == null) {
    		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
    		for (Iterator iter = idsOcorrenciaNaoConformidade.iterator(); iter.hasNext();) {
				TypedFlatMap mapIdOnc = (TypedFlatMap) iter.next();
				OcorrenciaNaoConformidade onc = ocorrenciaNaoConformidadeService.findById(mapIdOnc.getLong("idOcorrenciaNaoConformidade"));
				negociacao = new Negociacao();
				negociacao.setDhNegociacao(dhAtual);
				negociacao.setDsNegociacao(dsNegociacao);
				negociacao.setOcorrenciaNaoConformidade(onc);
				negociacao.setUsuario(SessionUtils.getUsuarioLogado());
				negociacao.setFilial(SessionUtils.getFilialSessao());
				store(negociacao);
			}
    	} else {
    		negociacao = findById(idNegociacao);
    		if (negociacao.getUsuario().getIdUsuario().compareTo( SessionUtils.getUsuarioLogado().getIdUsuario() ) !=0 ) { 
    			throw new BusinessException("LMS-00030");
    		}
    		negociacao.setDsNegociacao(dsNegociacao);
    		store(negociacao);
    	}
    	Negociacao retorno = new Negociacao();
    	retorno.setIdNegociacao(negociacao.getIdNegociacao());
    	retorno.setDhNegociacao(negociacao.getDhNegociacao());
    	return retorno;
    }


	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Negociacao bean) {
        return super.store(bean);
    }

    
	/**
	 * Obtém a quantidade de negociações por documento de servico
	 * @param idNaoConformidade
	 * @return
	 */
	public Integer getRowCountNegociacoesByIdNaoConformidade(Long idNaoConformidade) {
		return getNegociacaoDAO().getRowCountNegociacoesByIdNaoConformidade(idNaoConformidade);
	}

}