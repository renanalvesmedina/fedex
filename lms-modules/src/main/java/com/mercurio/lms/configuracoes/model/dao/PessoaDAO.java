package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ContatoCorrespondencia;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PessoaDAO extends BaseCrudDao<Pessoa, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Pessoa.class;
	}

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("inscricaoEstaduais",FetchMode.JOIN);
	}

	/**
	 * Retorna o número de endereço com tipo de endereço que a pessoa tem
	 * 
	 * @return Interer número de registro.
	 */ 
	public Integer verificaExistenciaEnderecoTipoEndereco(Long idPessoa){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(tep)");
		sql.addFrom(TipoEnderecoPessoa.class.getName()+" tep join tep.enderecoPessoa as ep join ep.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);
		   
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
    }

	/**
	 * Retorna o número de endereços do tipo fornecido que a pessoa tem
	 * 
	 * @return Interer número de registro. 
	 */
	public Integer verificaExistenciaEnderecoTipoEndereco(Long idPessoa, String tpEndereco){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(tep)");
		sql.addFrom(TipoEnderecoPessoa.class.getName()+" tep join tep.enderecoPessoa as ep join ep.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);
		sql.addCriteria("tep.tpEndereco", "=", tpEndereco);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Busca pessoas onde o endereço padrão não está mais vigente.
	 * Rotina usada pela atualização automatica de endereço padrão
	 * @return List de ids de Pessoa
	 */
	public List findPessoasComEnderecoForaVigencia() {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("distinct p "); 
		hql.addFrom(getPersistentClass().getName()+" p join p.enderecoPessoa ep ");
		hql.addCriteria("ep.dtVigenciaFinal", "<=", JTDateTimeUtils.getDataAtual());
		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna o número de endereço com tipo de endereço que a pessoa tem
	 * 
	 * @return Interer número de registro.
	 */
	public Integer verificaExistenciaEndereco(Long idPessoa){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(ep)");
		sql.addFrom(EnderecoPessoa.class.getName()+" ep join ep.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Retorna o número de endereço com tipo de endereço que a pessoa tem
	 * 
	 * @return Interer número de registro.
	 */
	public Integer verifyInscricaoEstadualPadraoAtiva(Long idPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(TipoTributacaoIE.class, "ttie");
		dc.setProjection(Projections.countDistinct("ie.id"));
		dc.createAlias("ttie.inscricaoEstadual", "ie");
		dc.add(Restrictions.eq("ie.pessoa.id", idPessoa));
		dc.add(Restrictions.eq("ie.blIndicadorPadrao", Boolean.TRUE));
		dc.add(Restrictions.eq("ie.tpSituacao", "A"));
		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return result.intValue();
	} 

	/**
	 * Retorna o número de telefone que a pessoa tem
	 * 
	 * @return Interer número de registro.
	 */
	public Integer verificaExistenciaTelefoneEndereco(Long idPessoa){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(te)");
		sql.addFrom(TelefoneEndereco.class.getName()+" te join te.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	} 

	/**
	 * Retorna o número de contato que a pessoa tem
	 * 
	 * @return Interer número de registro.
	 */
	public Integer verificaExistenciaContato(Long idPessoa){
        SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(co)");
		sql.addFrom(Contato.class.getName()+" co join co.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	} 
	
	public Integer verificaExistenciaContatoFaturamento(Long idPessoa){
        SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(co)");
		sql.addFrom(Contato.class.getName()+" co join co.pessoa as pe");  	   	   
		sql.addCriteria("pe.idPessoa","=",idPessoa);
		sql.addCriteria("co.tpContato", "=", "FA");
		sql.addCriteria("co.dsEmail", "!=","null");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	} 

	/**
	 * Finder de número de identificacao.<BR>
	 * @return nrIdentificacao
	 */
	public String findNrIdentificacao(Long id){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("id", id));
		dc.setProjection(Projections.property("nrIdentificacao"));
		List<String> result = findByDetachedCriteria(dc);
		if (!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}

	/**
	 * Retorna a pessoa caso que não existe como especializado
	 * 
	 * @return Interer número de registro.
	 */
	public Integer findValidPessoa(Class clazz, Map map){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(es)");
		sql.addFrom(clazz.getName()+" es join es.pessoa as pe");
		sql.addCriteria("pe.idPessoa","=", (String)map.get("idPessoa"));			
		sql.addCriteria("pe.tpPessoa","=", (String)map.get("tpPessoa"));			
		sql.addCriteria("pe.tpIdentificacao","=", (String)map.get("tpIdentificacao"));			
		sql.addCriteria("pe.nrIdentificacao","=", (String)map.get("nrIdentificacao"));

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true),sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Consulta a pessoa pelo Id e Tipo de Pessoa informado.<BR>
	 * Retorna verdadeiro se ela foi encontrada.
	 * @author Robson Edemar Gehl
	 * @param idPessoa
	 * @param tpPessoa
	 * @return true para pessoa com tipo válido; false, inválido
	 */
	public boolean validateTipoPessoa(Long idPessoa, String tpPessoa){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("id", idPessoa));
		dc.add(Restrictions.eq("tpPessoa", tpPessoa));
		dc.setProjection(Projections.count("id"));
		Integer count = (Integer) getAdsmHibernateTemplate().findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0);
	}

	/**
	 * Busca uma Pessoa ou uma especializacao de Pessoa pela identificação dela.
	 *  
	 * @param tpIdentificacao
	 * @param nrIdentificacao
	 * @param classEspecializacao se for <code>null</code> usa <code>Pessoa.class</code>.
	 * @return
	 */
	public Object findByIdentificacao(String tpIdentificacao, String nrIdentificacao, Class classEspecializacao, boolean fetchPessoa) {
		boolean isPessoa = false;
		if (classEspecializacao == null || classEspecializacao == Pessoa.class) {
			classEspecializacao = Pessoa.class;
			isPessoa = true;
		}
		DetachedCriteria dc = DetachedCriteria.forClass(classEspecializacao);
		if (isPessoa) {
			dc.add(Restrictions.eq("tpIdentificacao", tpIdentificacao));
			dc.add(Restrictions.eq("nrIdentificacao", nrIdentificacao));
		} else {
			dc.createAlias("pessoa", "pessoa_");
			dc.add(Restrictions.eq("pessoa_.tpIdentificacao", tpIdentificacao));
			dc.add(Restrictions.eq("pessoa_.nrIdentificacao", nrIdentificacao));
			if (fetchPessoa) {
				dc.setFetchMode("pessoa_", FetchMode.JOIN);
			}
		}

		Iterator i = getAdsmHibernateTemplate().findByDetachedCriteria(dc).iterator();
		if (i.hasNext()) {
			return i.next();
		}
		return null;
	}

	public Object findById(Long id, Class classEspecializacao, boolean fetchPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(classEspecializacao);
		dc.add(Restrictions.eq("pessoa.id", id));
		if (fetchPessoa) {
			dc.setFetchMode("pessoa", FetchMode.JOIN);
		}

		Iterator i = findByDetachedCriteria(dc).iterator();
		if (i.hasNext()) {
			return i.next();
		}
		return null;
	}

	/**
	 * Retorna a pessoa por id informado com o endereco e municipio 'fetchado'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param Long idPessoa
	 * @return Pessoa
	 * */
	public Pessoa findById(Long idPessoa) {
		SqlTemplate hql = mountHql(idPessoa);
		hql.addProjection("pe");

		List<Pessoa> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if(!result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	private SqlTemplate mountHql(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(Pessoa.class.getName(), "pe");
		hql.addLeftOuterJoin("fetch pe.enderecoPessoa", "ep");
		hql.addLeftOuterJoin("fetch ep.municipio", "mu");
		hql.addLeftOuterJoin("fetch mu.unidadeFederativa", "uf");
		
		hql.addCriteria("pe.id", "=", idPessoa);
		
		return hql;
	}
	
	/**
	 * Método que busca uma List de Pessoa de acordo com o ID do TelefoneEndereco.
	 * 
	 * @param idTelefoneEndereco
	 * @return
	 */
	public List findClientesByTelefoneEndereco(Long idTelefoneEndereco) {
		SqlTemplate sqlTemp = new SqlTemplate();

		sqlTemp.addFrom(Pessoa.class.getName() + " p join fetch p.telefoneEnderecos te");

		sqlTemp.addCriteria("te.id", "=", idTelefoneEndereco);

		return getAdsmHibernateTemplate().find(sqlTemp.getSql(), sqlTemp.getCriteria());
	}

	public String findDsEmailByIdPessoa(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("p.dsEmail"); 
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCriteria("idPessoa", "=", idPessoa);
		return (String) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	@SuppressWarnings("unchecked")
	public Boolean validatePessoaByNrIdentificacao(String nrIdentificacao) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("p.nrIdentificacao"); 
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCriteria("p.nrIdentificacao", "=", nrIdentificacao);
		
		List<String> retorno = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		return !retorno.isEmpty();
	}
	
	
	/**
	 * Método responsável por buscar nrCNPJParcialDev igual as primeiras 8 posições do nrIdentificacao da Pessoa 
	 * 
	 * @author HectorJ
	 * @since 31/05/2006
	 * 
	 * @param nrCNPJParcial
	 * @return List <Pessoa>
	 */
	public List findNrCNPJParcialEqualNrIdentificacaoPessoa(String nrCNPJParcial){

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" p "); 
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCustomCriteria("p.nrIdentificacao like '"+nrCNPJParcial+"%'");
		hql.addCustomCriteria("p.tpIdentificacao = 'CNPJ'");

		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	

	
	
	/**
	 * Busca a primeira pessoa na lista por cnpj parcial 
	 * 
	 * @author Vagner Huzalo
	 * @since 06/12/2007
	 * 
	 * @param nrCNPJParcial
	 * @return <code>Pessoa</code>
	 */
	public Pessoa findByCNPJParcial(String nrCNPJParcial){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" p "); 
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCustomCriteria("p.nrIdentificacao like '"+nrCNPJParcial+"%'");
		hql.addCustomCriteria("p.tpIdentificacao = 'CNPJ'");
		hql.addCustomCriteria("rownum < 2");
		
		List list = getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		Pessoa pessoa = null;
		if (list != null && list.size() !=0)pessoa = (Pessoa)list.get(0);
		
		return pessoa; 
	}
	
	
	public List findIdPessoaByNrCNPJParcial(String nrCNPJParcial){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("p.id");
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCriteria("p.nrIdentificacao", "like", nrCNPJParcial + "%");

		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List findIdPessoaByNrIdentificacaoParcial(String nrCNPJParcial){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("p.id");
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCriteria("p.nrIdentificacao", "like", nrCNPJParcial + "%");

		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	public List finPessoasByNrIdentificacaoParcial(String nrCNPJParcial){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("p");
		hql.addFrom(Pessoa.class.getName(), "p");
		hql.addCriteria("p.nrIdentificacao", "like", nrCNPJParcial + "%");

		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List finPessoasByNrIdentificacaoParcialWithClient(String nrCNPJParcial){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c.pessoa");
		hql.addFrom(Cliente.class.getName(), "c");
		hql.addCriteria("c.pessoa.nrIdentificacao", "like", nrCNPJParcial + "%");
		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Busca uma Pessoa pelo seu número
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param nrIdentificacao Número de identificação da pessoa
	 * @return Pessoa
	 */
	public Pessoa findByNrIdentificacao(String nrIdentificacao) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("p");

		hql.addFrom(getPersistentClass().getName(),"p");

		hql.addCriteria("p.nrIdentificacao","=",nrIdentificacao);

		List <Pessoa> result = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		if(!result.isEmpty() ){
			return result.get(0); 
		}
		return null;
	}

	/**
	 * Retorna o id da pessoa do tipo de endereco informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idTipoEnderecoPessoa
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdTipoEnderecoPessoa(Long idTipoEnderecoPessoa){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct te.enderecoPessoa.pessoa.id");

		hql.addInnerJoin(TipoEnderecoPessoa.class.getName(), "te");

		hql.addCriteria("te.id", "=", idTipoEnderecoPessoa);

		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna o id da pessoa da inscrição estadual informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idInscricaoEstadual
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdInscricaoEstadual(Long idInscricaoEstadual){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct ie.pessoa.id");

		hql.addInnerJoin(InscricaoEstadual.class.getName(), "ie");

		hql.addCriteria("ie.id", "=", idInscricaoEstadual);

		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna o id da pessoa do contato informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idContato
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdContato(Long idContato){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct cc.pessoa.id");

		hql.addInnerJoin(Contato.class.getName(), "cc");

		hql.addCriteria("cc.id", "=", idContato);

		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	

	public Pessoa findPessoaByIdMonitoramentoDocEletronic(Long idMonitoramentoDocEletronic) {
		SqlTemplate sql = new SqlTemplate();

        sql.addProjection("pes");

        sql.addFrom(Pessoa.class.getName() + " pes ");
        sql.addFrom(DevedorDocServ.class.getName() + " dds ");
        sql.addFrom(MonitoramentoDocEletronico.class.getName() + " mde ");
        
        sql.addJoin("pes.id","dds.cliente.id");
        sql.addJoin("dds.doctoServico.id","mde.doctoServico.id");
        
        sql.addCriteria("mde.id","=",idMonitoramentoDocEletronic);

		Pessoa pessoa = (Pessoa) this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
		return pessoa;
	}

	/**
	 * Retorna o id da pessoa do contato de correspondencia informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idContatoCorrespondencia
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdContatoCorrespondencia(Long idContatoCorrespondencia){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct cc.contato.pessoa.id");

		hql.addInnerJoin(ContatoCorrespondencia.class.getName(), "cc");

		hql.addCriteria("cc.id", "=", idContatoCorrespondencia);

		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Retorna o id da pessoa da observaçao icms pessoa informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/01/2007
	 * 
	 * @param idObservacaoICMSPessoa
	 * @return idPessoa
	 */
	public Long findIdPessoaByIdObservacaoICMSPessoa(Long idObservacaoICMSPessoa){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct oip.inscricaoEstadual.pessoa.id");

		hql.addInnerJoin(ObservacaoICMSPessoa.class.getName(), "oip");

		hql.addCriteria("oip.id", "=", idObservacaoICMSPessoa);

		return (Long)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}	
	
	public Map findPessoaSOMById(Long idPessoa) {
		StringBuilder sql = new StringBuilder()
		.append(" select new Map(")
        .append("		 	p.nrIdentificacao as nrIdentificacao,")
        .append("		 	p.nmPessoa as nmPessoa,")
        .append("		 	p.id as idPessoa,")
        .append("		 	p.tpPessoa as tpPessoa,")
        .append("		 	ep.nrCep as nrCep,")
        .append("		 	ep.dsEndereco as dsEndereco,")
        .append("		 	ep.nrEndereco as nrEndereco,")
        .append("		 	ep.dsComplemento as dsComplemento,")
        .append("		 	ep.dsBairro as dsBairro,")
        .append("		 	ep.municipio.id as idMunicipio,")
        .append("		 	"+PropertyVarcharI18nProjection.createProjection("ep.tipoLogradouro.dsTipoLogradouro")+" as dsTipoLogradouro,")
        .append("		 	flAtendeComercial.id as idFlAtendeComercial,")
        .append("		 	flCobranca.id as idFlCobranca")
        .append("		 )")
        .append("   from Cliente c")
        .append("   	 join c.pessoa p")
		.append("		 join p.enderecoPessoa ep")
		.append("		 join c.filialByIdFilialAtendeComercial flAtendeComercial")
		.append("		 join c.filialByIdFilialCobranca flCobranca")
	    .append("  where p.id = :idPessoa");

		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idPessoa", idPessoa);
		
		Map map = new HashMap(); 
		if(result != null && result.size() > 0)
			map = (Map) result.get(0);
		
		return map;
}
	
	public List findPessoaByCnpjCpf(List<String> listaCnpjCpf) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" PES.Tp_Identificacao, ");
		sql.append(" PES.Nr_Identificacao, ");
		sql.append(" PES.NM_PESSOA, ");
		sql.append(" PEM.Dt_Bloqueio, ");
		sql.append(" vi18n(MPE.DS_MOTIVO_PROIBIDO_EMBARQUE_I), ");
		sql.append(" MPE.ID_MOTIVO_PROIBIDO_EMBARQUE, ");
		sql.append(" USB.nm_usuario, ");
		sql.append(" PEM.DT_DESBLOQUEIO, ");
		sql.append(" PES.ID_PESSOA, ");
		sql.append(" PEM.ID_PROIBIDO_EMBARQUE ");
		
		sql.append(" FROM PESSOA PES ");
		sql.append(" JOIN CLIENTE CLI on CLI.ID_CLIENTE = PES.ID_PESSOA ");
		sql.append(" LEFT JOIN PROIBIDO_EMBARQUE PEM on PEM.ID_CLIENTE = CLI.ID_CLIENTE and PEM.DT_DESBLOQUEIO is null ");
		sql.append(" LEFT JOIN MOTIVO_PROIBIDO_EMBARQUE MPE ON MPE.ID_MOTIVO_PROIBIDO_EMBARQUE = PEM.ID_MOTIVO_PROIBIDO_EMBARQUE ");
		sql.append(" LEFT JOIN USUARIO USB ON USB.ID_USUARIO = PEM.ID_USUARIO_BLOQUEIO ");
		sql.append(" WHERE (PES.TP_IDENTIFICACAO,PES.NR_IDENTIFICACAO) in ("+listaCnpjCpf.toString().replace("[", "").replace("]", "")+") ");
		sql.append(" ORDER BY PEM.Dt_Bloqueio,PES.NM_PESSOA ");

		List result = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		
		return result;
}
	
	@SuppressWarnings("rawtypes")
	public List findNomePessoaByCompetenciaIdFranquia(YearMonthDay dtCompetencia, Long idFranquia) {
		
		StringBuilder query = new StringBuilder();
		query.append(" select p.nm_pessoa ");
		query.append(" from   pessoa p, franqueado_franquia ff ");
		query.append(" where  ff.id_franqueado = p.id_pessoa ");
		query.append(" and    ff.id_franquia = :franquia ");
		query.append(" and    TO_DATE(':competencia','dd/MM/yyyy') between ff.DT_VIGENCIA_inicial and ff.DT_VIGENCIA_FINAL ");
		
		String sql = query.toString();

		sql = sql.replaceAll(":franquia", idFranquia.toString());

		String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
		sql = sql.replaceAll(":competencia", competencia);
		
		List result = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		
		return result;
}
	
}
