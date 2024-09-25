package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.PagtoProprietarioCc;
import com.mercurio.lms.configuracoes.model.Pessoa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PagtoProprietarioCcDAO extends BaseCrudDao<PagtoProprietarioCc, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PagtoProprietarioCc.class;
    }

   

    /**
     * Verifica se já existe algum registro em PagtoProprietarioCc para o idProprietario ou, caso tpIdenficacao = 'CNPJ'
     * verifca se existe algum registro com os 8 primeiros dígitos iguais para o campo nrIdentificacao do proprietário.
     * 
     * @param idControleCarga
     * @param idProprietario
     * @return 
     */
    public Long validateExistePagtoProprietarioCcByControleCargaByProprietario(Long idControleCarga, Long idProprietario) {
    	StringBuffer sql = new StringBuffer()
    		.append("select new map(ppcc.id as idPpcc) ")
			.append("from ").append(PagtoProprietarioCc.class.getName()).append(" ppcc ")
			.append("inner join ppcc.proprietario prop ")
			.append("inner join prop.pessoa pes ")
			.append("where ppcc.controleCarga.id = ? ")
			.append("and (ppcc.proprietario.id = ? ")
	    	.append("or exists (")
	    	.append("select 1 from ").append(Pessoa.class.getName()).append(" p ")
	    	.append("where ")
	    	.append("p.id = ? ")
	    	.append("and p.tpIdentificacao = 'CNPJ' ")
	    	.append("and substr(p.nrIdentificacao, 0, 8) = substr(pes.nrIdentificacao, 0, 8) ")
	    	.append(")) ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idProprietario);
    	param.add(idProprietario);

    	List lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    	if (lista.isEmpty())
    		return null;
    	
    	return (Long)((Map)lista.get(0)).get("idPpcc");
    }



    /**
     * 
     * @param map
     * @param param
     * @param isRowCount
     * @return
     */
    private String addSqlByPagtoProprietarioCc(Long idControleCarga, List param, boolean isRowCount) {
    	StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
	    	sql.append("select new map(")
	    	.append("ppcc.idPagtoProprietarioCc as idPagtoProprietarioCc, ")
			.append("ppcc.vlPagamento as vlPagamento, ")
			.append("pessoaProprietario.tpIdentificacao as proprietario_pessoa_tpIdentificacao, ")
			.append("pessoaProprietario.nrIdentificacao as proprietario_pessoa_nrIdentificacao, ")
			.append("pessoaProprietario.nmPessoa as proprietario_pessoa_nmPessoa, ")
			.append("sc.nrSolicitacaoContratacao as veiculoControleCarga_solicitacaoContratacao_nrSolicitacaoContratacao, ")
			.append("filial.sgFilial as veiculoControleCarga_solicitacaoContratacao_filial_sgFilial, ")
			.append("moeda.sgMoeda as moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moeda_dsSimbolo) ");
		}
		sql.append("from ")
		.append(PagtoProprietarioCc.class.getName()).append(" as ppcc ")
		.append("inner join ppcc.controleCarga as cc ")
		.append("inner join ppcc.proprietario as proprietario ")
		.append("inner join proprietario.pessoa as pessoaProprietario ")
		.append("inner join ppcc.veiculoControleCarga as vcc ")
		.append("left join vcc.solicitacaoContratacao as sc ")
		.append("left join sc.filial as filial ")
		.append("left join ppcc.moeda as moeda ")
		.append("where ")
		.append("cc.id = ? ")
		.append("order by pessoaProprietario.nmPessoa");
		
		param.add(idControleCarga);
		return sql.toString();
    }
    
    
    /**
     * 
     * @param map
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedPagtoProprietarioCc(Long idControleCarga, FindDefinition findDefinition) {
		List param = new ArrayList();
		String sql = addSqlByPagtoProprietarioCc(idControleCarga, param, false);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
	    return rsp;
    }

    
    /**
     * 
     * @param map
     * @return
     */
    public Integer getRowCountPagtoProprietarioCc(Long idControleCarga) {
		List param = new ArrayList();
		String sql = addSqlByPagtoProprietarioCc(idControleCarga, param, true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }
    
    
    
    /**
     * 
     * @param idPagtoProprietarioCc
     * @return
     */
    public Map findByIdByControleCarga(Long idPagtoProprietarioCc) {
    	StringBuffer sql = new StringBuffer()
	    	.append("select new map(")
	    	.append("ppcc.idPagtoProprietarioCc as idPagtoProprietarioCc, ")
			.append("ppcc.vlPagamento as vlPagamento, ")
			.append("cc.vlFreteCarreteiro as controleCarga_vlFreteCarreteiro, ")
			.append("moedaCc.sgMoeda as controleCarga_moeda_sgMoeda, ")
			.append("moedaCc.dsSimbolo as controleCarga_moeda_dsSimbolo, ")
			.append("proprietario.idProprietario as proprietario_idProprietario, ")
			.append("pessoaProprietario.tpIdentificacao as proprietario_pessoa_tpIdentificacao, ")
			.append("pessoaProprietario.nrIdentificacao as proprietario_pessoa_nrIdentificacao, ")
			.append("pessoaProprietario.nmPessoa as proprietario_pessoa_nmPessoa, ")
			.append("moeda.idMoeda as moeda_idMoeda, ")
			.append("moeda.sgMoeda as moeda_sgMoeda, ")
			.append("moeda.dsSimbolo as moeda_dsSimbolo, ")
			.append("meioTransporte.tpVinculo as veiculoControleCarga_meioTransporte_tpVinculo, ")
			.append("filial.sgFilial as veiculoControleCarga_solicitacaoContratacao_filial_sgFilial, ")
			.append("sc.nrSolicitacaoContratacao as veiculoControleCarga_solicitacaoContratacao_nrSolicitacaoContratacao) ")
			.append("from ")
			.append(PagtoProprietarioCc.class.getName()).append(" as ppcc ")
			.append("inner join ppcc.controleCarga as cc ")
			.append("inner join ppcc.proprietario as proprietario ")
			.append("inner join proprietario.pessoa as pessoaProprietario ")
			.append("left join ppcc.veiculoControleCarga as vcc ")
			.append("left join vcc.solicitacaoContratacao as sc ")
			.append("left join sc.filial as filial ")
			.append("left join vcc.meioTransporte as meioTransporte ")
			.append("left join ppcc.moeda as moeda ")
			.append("left join cc.moeda as moedaCc ")
			.append("where ")
			.append("ppcc.id = ? ");
		
		List param = new ArrayList();
		param.add(idPagtoProprietarioCc);

		List lista = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	    return (Map)lista.get(0);
    }
}