package com.mercurio.lms.sgr.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.sgr.model.GerenciadoraRisco;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProcedimentoGerRiscoDAO extends BaseCrudDao<GerenciadoraRisco, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return GerenciadoraRisco.class;
    }
    
    
    /**
     * 
     * @param idControleCarga
     * @return
     */
	public List findDocumentosParaViagem(Long idControleCarga) {
		ConfigureSqlQuery configSql = getConfigSqlByFindGerRiscoToViagem();
		Set result = new HashSet();

		List param = new ArrayList();
		param.add(JTDateTimeUtils.getDataAtual());
		param.add(idControleCarga);

    	StringBuffer sqlProjecao = new StringBuffer()
    	.append("SELECT SUM (vl_mercadoria) AS vl_soma, ")
		.append("id_moeda, ")
		.append("id_cliente_remetente, ")
		.append("id_cliente_destinatario, ")
		.append("id_reg_seguro, ")
        .append("id_filial_origem, ")
        .append("id_filial_destino, ")
        .append("id_municipio_origem, ")
        .append("id_municipio_destino, ")
        .append("id_uf_origem, ")
        .append("id_uf_destino, ")
        .append("id_pais_origem, ")
        .append("id_pais_destino, ")
        .append("tp_modal, ")
        .append("tp_abrangencia, ")
        .append("id_natureza_produto, ")                                                                               
        .append("tp_vinculo ");
    	
    	StringBuffer sqlGroupBy = new StringBuffer()
		.append("GROUP BY ")
		.append("id_moeda, ")
		.append("id_cliente_remetente, ")
		.append("id_cliente_destinatario, ")
        .append("id_reg_seguro, ")
        .append("id_filial_origem, ")
        .append("id_filial_destino, ")
        .append("id_municipio_origem, ")
        .append("id_municipio_destino, ")
        .append("id_uf_origem, ")
        .append("id_uf_destino, ")
        .append("id_pais_origem, ")
        .append("id_pais_destino, ")
        .append("tp_modal, ")
        .append("tp_abrangencia, ")
		.append("id_natureza_produto, ")
		.append("tp_vinculo ");

    	
    	StringBuffer sql = new StringBuffer()
    	.append(sqlProjecao.toString())
    	.append("FROM ( ")
    	.append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
		.append("doctoservico.id_moeda AS id_moeda, ")
		.append("doctoservico.id_cliente_remetente AS id_cliente_remetente, ")
		.append("doctoservico.id_cliente_destinatario AS id_cliente_destinatario, ")
			.append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds, ")
			.append("cliente c1, ")
			.append("seguro_cliente sc1 ")
			.append("where ")
			.append("c1.id_cliente = dds.id_cliente ")
			.append("and sc1.id_cliente = c1.id_cliente ")
			.append("and dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS id_reg_seguro, ")
        .append("doctoservico.id_filial_origem AS id_filial_origem, ")
        .append("doctoservico.id_filial_destino AS id_filial_destino, ")
        .append("municipiocoleta.id_municipio AS id_municipio_origem, ")
        .append("municipioentrega.id_municipio AS id_municipio_destino, ")
        .append("municipiocoleta.id_unidade_federativa AS id_uf_origem, ")
        .append("municipioentrega.id_unidade_federativa AS id_uf_destino, ")
        .append("unidadefederativacoleta.id_pais AS id_pais_origem, ")
        .append("unidadefederativaentrega.id_pais AS id_pais_destino, ")
        .append("servico.tp_modal AS tp_modal, ")
        .append("servico.tp_abrangencia AS tp_abrangencia, ")
        .append("conhecimento.id_natureza_produto AS id_natureza_produto, ")
        .append("mt.tp_vinculo AS tp_vinculo ")
        .append("FROM ")
        .append("controle_carga cc, ") 
        .append("manifesto manifesto, ")
        .append("manifesto_viagem_nacional manifestoviagemnacional, ")
        .append("manifesto_nacional_cto manifestonacionalcto, ")
        .append("conhecimento conhecimento, ")
        .append("docto_servico doctoservico, ")
        .append("municipio municipiocoleta, ")
        .append("unidade_federativa unidadefederativacoleta, ")
        .append("municipio municipioentrega, ")
        .append("unidade_federativa unidadefederativaentrega, ")
        .append("servico, ")            
        .append("meio_transporte mt ")
        .append("WHERE ")
        .append("manifesto.id_controle_carga = cc.id_controle_carga ")
        .append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
        .append("AND manifestoviagemnacional.id_manifesto_viagem_nacional = manifesto.id_manifesto ")
    	.append("AND manifestonacionalcto.id_manifesto_viagem_nacional = manifestoviagemnacional.id_manifesto_viagem_nacional ")
    	.append("AND conhecimento.id_conhecimento = manifestonacionalcto.id_conhecimento ")
    	.append("AND doctoservico.id_docto_servico = conhecimento.id_conhecimento ")
        .append("AND municipiocoleta.id_municipio(+) = conhecimento.id_municipio_coleta ")
        .append("AND unidadefederativacoleta.id_unidade_federativa(+) = municipiocoleta.id_unidade_federativa ")
        .append("AND municipioentrega.id_municipio(+) = conhecimento.id_municipio_entrega ")
        .append("AND unidadefederativaentrega.id_unidade_federativa(+) = municipioentrega.id_unidade_federativa ")
        .append("AND servico.id_servico(+) = doctoservico.id_servico ")
        .append("AND mt.id_meio_transporte(+) = cc.id_transportado ")
    	.append("AND cc.id_controle_carga = ? ")
        .append("AND conhecimento.tp_documento_servico IN ('CTR', 'CTE', 'NFT', 'NTE') ")
        .append(") ")
        .append(sqlGroupBy.toString());
    	result.addAll(getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(100000), param.toArray(), configSql).getList());


    	sql = new StringBuffer()
    	.append(sqlProjecao.toString())
    	.append("FROM ( ")
        .append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
        .append("doctoservico.id_moeda AS id_moeda, ")
        .append("doctoservico.id_cliente_remetente AS id_cliente_remetente, ")
        .append("doctoservico.id_cliente_destinatario AS id_cliente_destinatario, ")
	   		.append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds, ")
			.append("cliente c1, ")
			.append("seguro_cliente sc1 ")
			.append("where ")
			.append("c1.id_cliente = dds.id_cliente ")
			.append("AND sc1.id_cliente = c1.id_cliente ")
			.append("AND dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS id_reg_seguro, ")
        .append("doctoservico.id_filial_origem AS id_filial_origem, ")
        .append("doctoservico.id_filial_destino AS id_filial_destino, ")
        .append("municipioorigem.id_municipio AS id_municipio_origem, ")
        .append("municipiodestino.id_municipio AS id_municipio_destino, ")
        .append("municipioorigem.id_unidade_federativa AS id_uf_origem, ")
        .append("municipiodestino.id_unidade_federativa AS id_uf_destino, ")
        .append("unidadefederativaorigem.id_pais AS id_pais_origem, ")
        .append("unidadefederativadestino.id_pais AS id_pais_destino, ")
        .append("servico.tp_modal AS tp_modal, ")
        .append("servico.tp_abrangencia AS tp_abrangencia, ")
        .append("NULL AS id_natureza_produto, ")
        .append("mt.tp_vinculo AS tp_vinculo ")
        .append("FROM ")
        .append("controle_carga cc, ") 
        .append("manifesto manifesto, ")
        .append("manifesto_internacional manifestointernacional, ")
        .append("manifesto_internac_cto manifestointernaccto, ")
        .append("cto_internacional ctointernacional, ")
        .append("docto_servico doctoservico, ")
        .append("filial filialorigem, ")
        .append("pessoa pessoaorigem, ")
        .append("endereco_pessoa enderecoorigem, ")
        .append("municipio municipioorigem, ")
        .append("unidade_federativa unidadefederativaorigem, ")
        .append("filial filialdestino, ")
        .append("pessoa pessoadestino, ")
        .append("endereco_pessoa enderecodestino, ")
        .append("municipio municipiodestino, ")
        .append("unidade_federativa unidadefederativadestino, ")
        .append("servico, ")
        .append("meio_transporte mt ")
    	.append("WHERE ")
        .append("manifesto.id_controle_carga = cc.id_controle_carga ") 
        .append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
       	.append("AND manifestointernacional.id_manifesto_internacional = manifesto.id_manifesto ")
       	.append("AND manifestointernaccto.id_manifesto_internacional = manifestointernacional.id_manifesto_internacional ")
       	.append("AND ctointernacional.id_cto_internacional = manifestointernaccto.id_cto_internacional ")
        .append("AND doctoservico.id_docto_servico = ctointernacional.id_cto_internacional ")
        .append("AND filialorigem.id_filial(+) = doctoservico.id_filial_origem ")
        .append("AND pessoaorigem.id_pessoa(+) = filialorigem.id_filial ")
        .append("AND enderecoorigem.id_pessoa(+) = pessoaorigem.id_pessoa ")
        .append("AND municipioorigem.id_municipio(+) = enderecoorigem.id_municipio ")
        .append("AND unidadefederativaorigem.id_unidade_federativa(+) = municipioorigem.id_unidade_federativa ")
        .append("AND filialdestino.id_filial(+) = doctoservico.id_filial_destino ")
        .append("AND pessoadestino.id_pessoa(+) = filialdestino.id_filial ")
        .append("AND enderecodestino.id_pessoa(+) = pessoadestino.id_pessoa ")
        .append("AND municipiodestino.id_municipio(+) = enderecodestino.id_municipio ")
        .append("AND unidadefederativadestino.id_unidade_federativa(+) = municipiodestino.id_unidade_federativa ")
        .append("AND servico.id_servico(+) = doctoservico.id_servico ")
        .append("AND mt.id_meio_transporte(+) = cc.id_transportado ")
    	.append("AND cc.id_controle_carga = ? ")
        .append("AND doctoservico.tp_documento_servico = 'CRT' ")
        .append(") ")
        .append(sqlGroupBy.toString());
    	result.addAll(getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(100000), param.toArray(), configSql).getList());


    	sql = new StringBuffer()
    	.append(sqlProjecao.toString())
    	.append("FROM ( ")
        .append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
        .append("doctoservico.id_moeda AS id_moeda, ")
        .append("doctoservico.id_cliente_remetente AS id_cliente_remetente, ")
        .append("doctoservico.id_cliente_destinatario AS id_cliente_destinatario, ")
	        .append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds, ")
			.append("cliente c1, ")
			.append("seguro_cliente sc1 ")
			.append("where ")
			.append("c1.id_cliente = dds.id_cliente ")
			.append("AND sc1.id_cliente = c1.id_cliente ")
			.append("AND dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS id_reg_seguro, ")
        .append("doctoservico.id_filial_origem AS id_filial_origem, ")
        .append("doctoservico.id_filial_destino AS id_filial_destino, ")
        .append("municipiocoleta.id_municipio AS id_municipio_origem, ")
        .append("municipioentrega.id_municipio AS id_municipio_destino, ")
        .append("municipiocoleta.id_unidade_federativa AS id_uf_origem, ")
        .append("municipioentrega.id_unidade_federativa AS id_uf_destino, ")
        .append("unidadefederativacoleta.id_pais AS id_pais_origem, ")
        .append("unidadefederativaentrega.id_pais AS id_pais_destino, ")
        .append("servico.tp_modal AS tp_modal, ")
        .append("servico.tp_abrangencia AS tp_abrangencia, ")
        .append("conhecimento.id_natureza_produto AS id_natureza_produto, ")
        .append("mt.tp_vinculo AS tp_vinculo ")
        .append("FROM ")
        .append("controle_carga cc, ")
        .append("manifesto manifesto, ") 
    	.append("pre_manifesto_documento premanifestodocumento, ")
        .append("docto_servico doctoservico, ")
        .append("conhecimento conhecimento, ")
        .append("municipio municipiocoleta, ")
        .append(" unidade_federativa unidadefederativacoleta, ")
        .append("municipio municipioentrega, ")
        .append("unidade_federativa unidadefederativaentrega, ")
        .append("servico, ")
        .append("meio_transporte mt ")
    	.append("WHERE  ")
        .append("manifesto.id_controle_carga = cc.id_controle_carga ") 
		.append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') AND manifesto.dh_emissao_manifesto IS NULL ")
    	.append("AND manifesto.id_manifesto = premanifestodocumento.id_manifesto ")
        .append("AND doctoservico.id_docto_servico = premanifestodocumento.id_docto_servico ")
        .append("AND conhecimento.id_conhecimento = doctoservico.id_docto_servico ")
        .append("AND municipiocoleta.id_municipio(+) = conhecimento.id_municipio_coleta ")
        .append("AND unidadefederativacoleta.id_unidade_federativa(+) = municipiocoleta.id_unidade_federativa ")
        .append("AND municipioentrega.id_municipio(+) = conhecimento.id_municipio_entrega ")
        .append("AND unidadefederativaentrega.id_unidade_federativa(+) = municipioentrega.id_unidade_federativa ")
        .append("AND servico.id_servico(+) = doctoservico.id_servico ")
        .append("AND mt.id_meio_transporte(+) = cc.id_transportado ")
    	.append("AND cc.id_controle_carga = ? ")
        .append("AND conhecimento.tp_documento_servico IN ('CTR', 'CTE', 'NFT', 'NTE') ")
        .append(") ")
        .append(sqlGroupBy.toString());
    	result.addAll(getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(100000), param.toArray(), configSql).getList());


    	sql = new StringBuffer()
    	.append(sqlProjecao.toString())
    	.append("FROM ( ")
        .append("SELECT doctoservico.vl_mercadoria AS vl_mercadoria, ")
        .append("doctoservico.id_moeda AS id_moeda, ")
        .append("doctoservico.id_cliente_remetente AS id_cliente_remetente, ")
        .append("doctoservico.id_cliente_destinatario AS id_cliente_destinatario, ")
	   		.append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds, ")
			.append("cliente c1, ")
			.append("seguro_cliente sc1 ")
			.append("where ")
			.append("c1.id_cliente = dds.id_cliente ")
			.append("AND sc1.id_cliente = c1.id_cliente ")
			.append("AND dds.id_docto_servico = doctoservico.id_docto_servico ")
			.append("and ? between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS id_reg_seguro, ")
        .append("doctoservico.id_filial_origem AS id_filial_origem, ")
        .append("doctoservico.id_filial_destino AS id_filial_destino, ")
        .append("municipioorigem.id_municipio AS id_municipio_origem, ")
        .append("municipiodestino.id_municipio AS id_municipio_destino, ")
        .append("municipioorigem.id_unidade_federativa AS id_uf_origem, ")
        .append("municipiodestino.id_unidade_federativa AS id_uf_destino, ")
        .append("unidadefederativaorigem.id_pais AS id_pais_origem, ")
        .append("unidadefederativadestino.id_pais AS id_pais_destino, ")
        .append("servico.tp_modal AS tp_modal, ")
        .append("servico.tp_abrangencia AS tp_abrangencia, ")
        .append("NULL AS id_natureza_produto, ")
        .append("mt.tp_vinculo AS tp_vinculo ")
        .append("FROM ")
        .append("controle_carga cc, ") 
        .append("manifesto manifesto, ") 
        .append("pre_manifesto_documento premanifestodocumento, ")
        .append("docto_servico doctoservico, ")
        .append("cto_internacional ctointernacional, ")
        .append("filial filialorigem, ")
        .append("pessoa pessoaorigem, ")
        .append("endereco_pessoa enderecoorigem, ")
        .append("municipio municipioorigem, ")
        .append("unidade_federativa unidadefederativaorigem, ") 
        .append("filial filialdestino, ")
        .append("pessoa pessoadestino, ")
        .append("endereco_pessoa enderecodestino, ")
        .append("municipio municipiodestino, ")
        .append("unidade_federativa unidadefederativadestino, ") 
        .append("servico, ")
        .append("meio_transporte mt ")
    	.append("WHERE ")
    	.append("manifesto.id_controle_carga = cc.id_controle_carga ") 
        .append("AND manifesto.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') AND manifesto.dh_emissao_manifesto IS NULL ")
        .append("AND manifesto.id_manifesto = premanifestodocumento.id_manifesto ")
        .append("AND doctoservico.id_docto_servico = premanifestodocumento.id_docto_servico ")
        .append("AND ctointernacional.id_cto_internacional = doctoservico.id_docto_servico ")
        .append("AND filialorigem.id_filial(+) = doctoservico.id_filial_origem ")
        .append("AND pessoaorigem.id_pessoa(+) = filialorigem.id_filial ")
        .append("AND enderecoorigem.id_pessoa(+) = pessoaorigem.id_pessoa ")
        .append("AND municipioorigem.id_municipio(+) = enderecoorigem.id_municipio ")
        .append("AND unidadefederativaorigem.id_unidade_federativa(+) = municipioorigem.id_unidade_federativa ")
        .append("AND filialdestino.id_filial(+) = doctoservico.id_filial_destino ")
        .append("AND pessoadestino.id_pessoa(+) = filialdestino.id_filial ")
        .append("AND enderecodestino.id_pessoa(+) = pessoadestino.id_pessoa ")
        .append("AND municipiodestino.id_municipio(+) = enderecodestino.id_municipio ")
        .append("AND unidadefederativadestino.id_unidade_federativa(+) = municipiodestino.id_unidade_federativa ")
        .append("AND servico.id_servico(+) = doctoservico.id_servico ")
        .append("AND mt.id_meio_transporte(+) = cc.id_transportado ")
    	.append("AND cc.id_controle_carga = ? ")
        .append("AND doctoservico.tp_documento_servico = 'CRT' ")
        .append(") ")
        .append(sqlGroupBy.toString());
    	result.addAll(getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(100000), param.toArray(), configSql).getList());

    	return new ArrayList(result);
    }

	/**
	 * 
	 * @return
	 */
	private ConfigureSqlQuery getConfigSqlByFindGerRiscoToViagem() {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("VL_SOMA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ID_MOEDA", Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE_REMETENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE_DESTINATARIO", Hibernate.LONG);
				sqlQuery.addScalar("ID_REG_SEGURO", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("ID_UF_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("ID_UF_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("ID_PAIS_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("ID_PAIS_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("TP_MODAL", Hibernate.STRING);
				sqlQuery.addScalar("TP_ABRANGENCIA", Hibernate.STRING);
				sqlQuery.addScalar("ID_NATUREZA_PRODUTO", Hibernate.LONG);
				sqlQuery.addScalar("TP_VINCULO", Hibernate.STRING);
			}
		};
		return configSql;
	}


    /**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	private Object[] createSqlGerRiscoToColetaEntrega(Long idControleCarga, String tpConteudo) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT SUM (vlmercadoria) AS vlsoma, ")
		.append("tpgraurisco, ")
		.append("idmoeda, ")
		.append("idpais, ")
        .append("idclientedestinatario, ")
        .append("idpessoareguladora, ") 
        .append("idnaturezaproduto, ")
        .append("idfilial, ")
        .append("tpmodal, ")
        .append("tpvinculo, ")
        .append("idclienteremetente, ")

		// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
		.append("tpmanifesto, ")
		.append("tppedidocoleta, ")
		.append("idawb ")

        .append("FROM ( ")

        .append("SELECT docto_servico.id_docto_servico, ")
        .append("docto_servico.vl_mercadoria AS vlmercadoria, ")
        .append("ric.tp_grau_risco AS tpgraurisco, ")
        .append("docto_servico.id_moeda AS idmoeda, ")
        .append("docto_servico.id_pais AS idpais, ")
        .append("docto_servico.id_cliente_destinatario AS idclientedestinatario, ")
       	.append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = docto_servico.id_docto_servico ")
			.append("and :param1 between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS idpessoareguladora, ")
        .append("conhecimento.id_natureza_produto AS idnaturezaproduto, ")
        .append("docto_servico.id_filial_destino AS idfilial, ")
        .append("servico.tp_modal AS tpmodal, ")
        .append("mt.tp_vinculo AS tpvinculo, ")
        .append("docto_servico.id_cliente_remetente AS idclienteremetente, ")

		// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
		.append("manifesto.tp_manifesto AS tpmanifesto, ")
		.append("NULL AS tppedidocoleta, ")
		.append("med.id_awb AS idawb ")

        .append("FROM ")
        .append("controle_carga cc ")
        .append("INNER JOIN manifesto ON (cc.id_controle_carga = manifesto.id_controle_carga) ")
        .append("INNER JOIN manifesto_entrega me ON (manifesto.id_manifesto = me.id_manifesto_entrega) ")
        .append("INNER JOIN manifesto_entrega_documento med ON (me.id_manifesto_entrega = med.id_manifesto_entrega) ")
        .append("INNER JOIN docto_servico ON (med.id_docto_servico = docto_servico.id_docto_servico) ")
        .append("LEFT JOIN servico ON (docto_servico.id_servico = servico.id_servico) ")
        .append("LEFT JOIN rota_intervalo_cep ric ON (docto_servico.id_rota_intervalo_cep = ric.id_rota_intervalo_cep) ")
        .append("LEFT JOIN conhecimento ON (docto_servico.id_docto_servico = conhecimento.id_conhecimento) ")
        .append("LEFT JOIN cto_internacional ON (docto_servico.id_docto_servico = cto_internacional.id_cto_internacional) ")
        .append("LEFT JOIN meio_transporte mt ON (mt.id_meio_transporte = cc.id_transportado) ")
        .append("WHERE ")
        .append("docto_servico.tp_documento_servico IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
        .append("AND cc.id_controle_carga = :param2 ")
        .append("AND manifesto.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")
        .append("AND NOT EXISTS ( ")
        		.append("SELECT 1 ")
        		.append("FROM ")
        		.append("evento_documento_servico ")
        		.append("INNER JOIN evento ON (evento_documento_servico.id_evento = evento.id_evento) ")
        		.append("WHERE evento.cd_evento = 21 ")
        		.append("AND evento_documento_servico.bl_evento_cancelado = 'N' ")
        		.append("AND evento_documento_servico.id_docto_servico = docto_servico.id_docto_servico) ")

        .append("UNION ")
        
        .append("SELECT ")
        .append("docto_servico.id_docto_servico, ")
        .append("docto_servico.vl_mercadoria AS vlmercadoria, ")
        .append("ric.tp_grau_risco AS tpgraurisco, ")
        .append("docto_servico.id_moeda AS idmoeda, ")
        .append("docto_servico.id_pais AS idpais, ")
        .append("docto_servico.id_cliente_destinatario AS idclientedestinatario, ")
        .append("(select max(sc1.id_pessoa_reguladora) ")
			.append("from devedor_doc_serv dds ")
			.append("inner join cliente c1 on (c1.id_cliente = dds.id_cliente) ")
			.append("inner join seguro_cliente sc1 on (sc1.id_cliente = c1.id_cliente) ")
			.append("where ")
			.append("dds.id_docto_servico = docto_servico.id_docto_servico ")
			.append("and :param1 between sc1.dt_vigencia_inicial and sc1.dt_vigencia_final ")
			.append("and sc1.tp_modal = servico.tp_modal ")
			.append("and sc1.tp_abrangencia = servico.tp_abrangencia) ")
		.append("AS idpessoareguladora, ")
        .append("conhecimento.id_natureza_produto AS idnaturezaproduto, ")
        .append("docto_servico.id_filial_destino AS idfilial, ")
        .append("servico.tp_modal AS tpmodal, ")
        .append("mt.tp_vinculo AS tpvinculo, ")
        .append("docto_servico.id_cliente_remetente AS idclienteremetente, ")

		// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
		.append("manifesto.tp_manifesto AS tpmanifesto, ")
		.append("NULL AS tppedidocoleta, ")
		.append("NULL AS idawb ")

        .append("FROM ")
        .append("controle_carga cc ")
        .append("INNER JOIN manifesto ON (cc.id_controle_carga = manifesto.id_controle_carga) ")
        .append("INNER JOIN pre_manifesto_documento pmd ON (pmd.id_manifesto = manifesto.id_manifesto) ")
        .append("INNER JOIN docto_servico ON (pmd.id_docto_servico = docto_servico.id_docto_servico) ")
        .append("LEFT JOIN servico ON (docto_servico.id_servico = servico.id_servico) ")
        .append("LEFT JOIN rota_intervalo_cep ric ON (docto_servico.id_rota_intervalo_cep = ric.id_rota_intervalo_cep) ")
        .append("LEFT JOIN conhecimento ON (docto_servico.id_docto_servico = conhecimento.id_conhecimento) ")
        .append("LEFT JOIN cto_internacional ON (docto_servico.id_docto_servico = cto_internacional.id_cto_internacional) ")
        .append("LEFT JOIN meio_transporte mt ON (mt.id_meio_transporte = cc.id_transportado) ")
        .append("WHERE ")
        .append("docto_servico.tp_documento_servico IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
        .append("AND cc.id_controle_carga = :param2 ")
        .append("AND manifesto.tp_status_manifesto not in ('CA', 'DC', 'ED', 'FE') ")
        .append("AND NOT EXISTS ( ")
        		.append("SELECT 1 ")
        		.append("FROM ")
        		.append("evento_documento_servico ")
        		.append("INNER JOIN evento ON (evento_documento_servico.id_evento = evento.id_evento) ")
        		.append("WHERE evento.cd_evento = 21 ")
        		.append("AND evento_documento_servico.bl_evento_cancelado = 'N' ")
        		.append("AND evento_documento_servico.id_docto_servico = docto_servico.id_docto_servico) ")
        
        .append("UNION ")

        .append("SELECT ")
        .append("dc.id_detalhe_coleta, ")
        .append("dc.vl_mercadoria AS vlmercadoria, ")
        .append("ric.tp_grau_risco AS tpgraurisco, ")
        .append("pc.id_moeda AS idmoeda, ")
        .append("pais.id_pais as idpais, ")
        .append("dc.id_cliente AS idclientedestinatario, ")
        .append("sc.id_pessoa_reguladora AS idpessoareguladora, ")
        .append("dc.id_natureza_produto AS idnaturezaproduto, ")
        .append("pc.id_filial_responsavel AS idfilial, ")
        .append("servico.tp_modal AS tpmodal, ")
        .append("mt.tp_vinculo AS tpvinculo, ")
        .append("pc.id_cliente AS idclienteremetente, ")

		// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
		.append("'C' AS tpmanifesto, ")
		.append("pc.tp_pedido_coleta AS tppedidocoleta, ")
		.append("NULL AS idawb ")

        .append("FROM ")
        .append("controle_carga cc ")
        .append("INNER JOIN manifesto_coleta mc ON (cc.id_controle_carga = mc.id_controle_carga) ")
        .append("INNER JOIN pedido_coleta pc ON (pc.id_manifesto_coleta = mc.id_manifesto_coleta) ")
        .append("INNER JOIN detalhe_coleta dc ON (pc.id_pedido_coleta = dc.id_pedido_coleta) ")
        .append("INNER JOIN cliente ON (pc.id_cliente = cliente.id_cliente) ")
        .append("INNER JOIN filial on (filial.id_filial = pc.id_filial_responsavel) ")
		.append("INNER JOIN pessoa on (pessoa.id_pessoa = filial.id_filial) ")
		.append("INNER JOIN endereco_pessoa ep on (ep.ID_ENDERECO_PESSOA = pessoa.ID_ENDERECO_PESSOA) ")
		.append("INNER JOIN municipio on (municipio.ID_MUNICIPIO = ep.ID_MUNICIPIO) ")
		.append("INNER JOIN unidade_federativa uf on (uf.ID_UNIDADE_FEDERATIVA = municipio.ID_UNIDADE_FEDERATIVA) ")
		.append("INNER JOIN pais on (pais.ID_PAIS = uf.ID_PAIS) ")
        .append("LEFT JOIN servico ON (dc.id_servico = servico.id_servico) ")
        .append("LEFT JOIN rota_intervalo_cep ric ON (pc.id_rota_intervalo_cep = ric.id_rota_intervalo_cep) ")
        .append("LEFT JOIN seguro_cliente sc ON (sc.id_cliente = cliente.id_cliente ")
        		.append("AND :param1 BETWEEN sc.dt_vigencia_inicial AND sc.dt_vigencia_final) ")
        .append("LEFT JOIN meio_transporte mt ON (mt.id_meio_transporte = cc.id_transportado) ")
        .append("WHERE ")
        .append("cc.id_controle_carga = :param2 ");

        if (tpConteudo.equals("A")) {	
            sql.append("AND exists ( ")
    		.append("SELECT 1 ")
    		.append("FROM evento_coleta ")
    		.append("WHERE tp_evento_coleta = 'EX' and evento_coleta.id_pedido_coleta = pc.id_pedido_coleta) ");
        }

        sql.append(") ")
		.append("GROUP BY ")
		.append("tpgraurisco, ")
        .append("idmoeda, ")
        .append("idpais, ")
        .append("idclientedestinatario, ")
        .append("idpessoareguladora, ")
        .append("idnaturezaproduto, ")
        .append("idfilial, ")
        .append("tpmodal, ")
        .append("tpvinculo, ")
        .append("idclienteremetente, ")

		// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
		.append("tpmanifesto, ")
		.append("tppedidocoleta, ")
		.append("idawb ");

		Map parameters = new HashMap();
		parameters.put("param1", JTDateTimeUtils.getDataAtual());
		parameters.put("param2", idControleCarga);
		return new Object[]{sql.toString(), parameters};
	}



    /**
     * @param idControleCarga
     * @param tpConteudo
     * @return
     */
    public List findDocumentosParaColetaEntrega(Long idControleCarga, String tpConteudo) {
    	Object[] obj = createSqlGerRiscoToColetaEntrega(idControleCarga, tpConteudo);
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("vlSoma", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("tpGrauRisco",Hibernate.STRING);
    			sqlQuery.addScalar("idMoeda",Hibernate.LONG);
    			sqlQuery.addScalar("idPais",Hibernate.LONG);
    			sqlQuery.addScalar("idClienteDestinatario",Hibernate.LONG);
    			sqlQuery.addScalar("idPessoaReguladora",Hibernate.LONG);
    			sqlQuery.addScalar("idNaturezaProduto",Hibernate.LONG);
    			sqlQuery.addScalar("idFilial",Hibernate.LONG);
    			sqlQuery.addScalar("tpModal",Hibernate.STRING);
    			sqlQuery.addScalar("tpVinculo", Hibernate.STRING);
    			sqlQuery.addScalar("idClienteRemetente",Hibernate.LONG);
				// LMS-6850 - colunas adicionais para verificação de coleta/entrega aeroporto
				sqlQuery.addScalar("tpmanifesto", Hibernate.STRING);
				sqlQuery.addScalar("tppedidocoleta", Hibernate.STRING);
				sqlQuery.addScalar("idawb", Hibernate.LONG);
    		}
    	};
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql((String)obj[0], Integer.valueOf(1), Integer.valueOf(10000), (Map)obj[1], csq);
    	return rsp.getList();
    }



	/**
	 * 
	 * @param idControleCarga
	 * @param param
	 * @return
	 */
    private String createSqlGerRiscoToViagem(Long idControleCarga, List param) {
   	return null;
    }
}