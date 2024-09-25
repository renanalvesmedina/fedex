package com.mercurio.lms.tabelaprecos.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tabelaprecos.model.LimiteDesconto;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.LimiteDescontoDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.limiteDescontoService"
 */
public class LimiteDescontoService extends CrudService<LimiteDesconto, Long> {


	/**
	 * Recupera uma instância de <code>LimiteDesconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TypedFlatMap findByIdMap(java.lang.Long id) {
		LimiteDesconto ld = (LimiteDesconto)super.findById(id);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idLimiteDesconto", ld.getIdLimiteDesconto());
		retorno.put("tpTipoTabelaPreco.value", ld.getTpTipoTabelaPreco().getValue());
		retorno.put("tpTipoTabelaPreco.description", ld.getTpTipoTabelaPreco().getDescription());
		retorno.put("pcLimiteDesconto", ld.getPcLimiteDesconto());
		DomainValue tpIndicadorDesconto = ld.getTpIndicadorDesconto();
		if(tpIndicadorDesconto != null) {
			retorno.put("tpIndicadorDesconto.value", tpIndicadorDesconto.getValue());
			retorno.put("tpIndicadorDesconto.description", tpIndicadorDesconto.getDescription());
		}
		retorno.put("tpSituacao.value", ld.getTpSituacao().getValue());
		retorno.put("tpSituacao.description", ld.getTpSituacao().getDescription());
		DivisaoGrupoClassificacao dgc = ld.getDivisaoGrupoClassificacao();
		if(dgc != null)
			retorno.put("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao", dgc.getIdDivisaoGrupoClassificacao());
		Filial f = ld.getFilial();
		if(f != null) {
			retorno.put("filial.idFilial", f.getIdFilial());
			retorno.put("filial.sgFilial", f.getSgFilial());
			retorno.put("filial.pessoa.nmFantasia", f.getPessoa().getNmFantasia());
		}
		Usuario us = ld.getUsuario();
		if(us != null) {
			retorno.put("usuario.idUsuario", us.getIdUsuario());
			retorno.put("usuario.nmUsuario", us.getNmUsuario());
			Funcionario func = us.getVfuncionario();
			retorno.put("funcionario.nrMatricula", func.getNrMatricula());
			retorno.put("funcionario.codPessoa.nome", func.getNmFuncionario());
		}
		ParcelaPreco pp = ld.getParcelaPreco();
		retorno.put("parcelaPreco.idParcelaPreco", pp.getIdParcelaPreco());
		retorno.put("parcelaPreco.nmParcelaPreco", pp.getNmParcelaPreco());
		SubtipoTabelaPreco stp = ld.getSubtipoTabelaPreco();
		DomainValue tpTipoTabelaPreco = stp.getTpTipoTabelaPreco();
		if(tpTipoTabelaPreco != null) {
			retorno.put("subtipoTabelaPreco.tpTipoTabelaPreco.value", tpTipoTabelaPreco.getValue());
			retorno.put("subtipoTabelaPreco.tpTipoTabelaPreco.description", tpTipoTabelaPreco.getDescription());
		}
		retorno.put("subtipoTabelaPreco.idSubtipoTabelaPreco", stp.getIdSubtipoTabelaPreco());
		retorno.put("subtipoTabelaPreco.tpSubtipoTabelaPreco", stp.getTpSubtipoTabelaPreco());
		
		if(ld.getPerfil() != null){
			retorno.put("perfil.dsPerfil", ld.getPerfil().getDsPerfil());
			retorno.put("perfil.idPerfil", ld.getPerfil().getIdPerfil());			
	}

		return retorno;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	@Override
	protected LimiteDesconto beforeStore(LimiteDesconto bean) {
		DivisaoGrupoClassificacao divisaoGrupoClassificacao = bean.getDivisaoGrupoClassificacao();
		Filial filial = bean.getFilial();
		Usuario usuario = bean.getUsuario();
		if( (usuario != null) && (usuario.getIdUsuario() == null) ) {
			usuario = null;
			bean.setUsuario(usuario);
		}

		if(divisaoGrupoClassificacao != null) {
			if( (filial != null) || (usuario != null) ) {
				throw new BusinessException("LMS-00042");
			}
		}

		if(filial != null) {
			if( (divisaoGrupoClassificacao != null) || (usuario != null) ) {
				throw new BusinessException("LMS-00042");
			}
		}

		if(usuario != null) {
			if( (filial != null) || (divisaoGrupoClassificacao != null) ) {
				throw new BusinessException("LMS-00042");
			}
		}

		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(LimiteDesconto bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setLimiteDescontoDAO(LimiteDescontoDAO dao) {
		setDao( dao );
	}
	
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private LimiteDescontoDAO getLimiteDescontoDAO() {
		return (LimiteDescontoDAO) getDao();
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getLimiteDescontoDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> result = rsp.getList();
		for(Map<String, Object> aux : result) {
			VarcharI18n dsGrupoClassificacao = (VarcharI18n) aux.remove("dsGrupoClassificacao");

			VarcharI18n dsDivisaoGrupoClassificacao = (VarcharI18n) aux.remove("dsDivisaoGrupoClassificacao");
			if (dsGrupoClassificacao!=null && dsDivisaoGrupoClassificacao!=null)
			aux.put("dsGrupoDivisao",dsGrupoClassificacao.getValue() + " - " + dsDivisaoGrupoClassificacao.getValue());
		}
		return rsp;
	}

	public BigDecimal findPcLimiteDesconto(Long idParcelaPreco, Long idSubtipoTabelaPreco, 
			String tpTipoTabelaPreco, String tpIndicadorDesconto, String tpSituacao, Long idUsuario, Long idFilial, Long idDivisaoGrupoClassificacao) {
		return getLimiteDescontoDAO().findPcLimiteDesconto(null, idParcelaPreco, idSubtipoTabelaPreco, tpTipoTabelaPreco, tpIndicadorDesconto, tpSituacao, idUsuario, idFilial, idDivisaoGrupoClassificacao);
	}

	public BigDecimal findPcLimiteDesconto(String cdParcelaPreco, Long idParcelaPreco, Long idSubtipoTabelaPreco, String tpTipoTabelaPreco, 
			String tpIndicadorDesconto, String tpSituacao, Long idUsuario, Long idFilial, Long idDivisaoGrupoClassificacao) {
		return getLimiteDescontoDAO().findPcLimiteDesconto(cdParcelaPreco, idParcelaPreco, idSubtipoTabelaPreco, tpTipoTabelaPreco, tpIndicadorDesconto, tpSituacao, idUsuario, idFilial, idDivisaoGrupoClassificacao);
	}

	public BigDecimal findPcLimiteDesconto(String cdParcelaPreco, Long idParcelaPreco, Long idSubtipoTabelaPreco, String tpTipoTabelaPreco, 
			String tpIndicadorDesconto, String tpSituacao, Long idUsuario, Long idFilial, Long idDivisaoGrupoClassificacao, Long... idsPerfil) {
		return getLimiteDescontoDAO().findPcLimiteDesconto(cdParcelaPreco, idParcelaPreco, idSubtipoTabelaPreco, tpTipoTabelaPreco, tpIndicadorDesconto, tpSituacao, idUsuario, idFilial, idDivisaoGrupoClassificacao, idsPerfil);
	}	

}