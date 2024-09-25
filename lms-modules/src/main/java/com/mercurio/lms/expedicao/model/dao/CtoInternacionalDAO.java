package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CtoInternacionalDAO extends BaseCrudDao<CtoInternacional, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */ 
	protected final Class getPersistentClass() {
		return CtoInternacional.class;
	}
	
	protected void initFindLookupLazyProperties(Map map) {
		map.put("moeda", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
	}

	public CtoInternacional findByIdDocServico(final Long id){
		StringBuffer hql = new StringBuffer();
		hql.append("select new Map(ci.idDoctoServico as idDoctoServico,");
		hql.append("       ci.nrDoctoServico as nrDoctoServico,");
		hql.append("       ci.sgPais as sgPais,");
		hql.append(" 	   ci.tpSituacaoCrt as tpSituacaoCrt,  ");
		hql.append(" 	   ci.nrCrt as nrCrt,  ");
		hql.append(" 	   filial.idFilial  as filial_idFilial, ");
		hql.append(" 	   filial.sgFilial  as filial_sgFilial, ");
		hql.append(" 	   pr.idPendencia  as pendenciaReemissao_idPendencia, ");
		hql.append(" 	   pr.tpSituacaoPendencia  as pendenciaReemissao_tpSituacaoPendencia  ) ");

		hql.append(" from ").append(getPersistentClass().getName()).append(" ci");
		hql.append("      LEFT JOIN fetch ci.pendenciaReemissao pr");
		hql.append("      JOIN fetch ci.filial filial");
		hql.append("      JOIN fetch ci.pendenciaReemissao pr");
		hql.append(" 	  WHERE ci.idDoctoServico = ?");

		List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{id});
		if(result.size() == 1) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(getPersistentClass());
			result = transformer.transformListResult(result);
			return (CtoInternacional) result.get(0);
		}
		return null;
		
	}
	
	public TypedFlatMap findCrtById(final Long id){
		
		StringBuilder hql = new StringBuilder()
		.append("select	new Map(\n")
		.append("      	ci.idDoctoServico as idCtoInternacional\n")
		.append("      	,ci.nrCrt as nrCrt\n")
		.append("      	,ci.dhEmissao as dhEmissao\n")
		.append("      	,ci.dhInclusao as dhInclusao\n")
		.append("      	,ci.dhAlteracao as dhAlteracao\n")
		.append("      	,ci.dtCarregamento as dtCarregamento\n")
		.append("      	,ci.tpSituacaoCrt as tpSituacaoCrt\n")
		.append("      	,ci.dsNomeRemetente as dsNomeRemetente\n")
		.append("      	,ci.dsDadosRemetente as dsDadosRemetente\n")
		.append("      	,ci.dsDadosDestinatario as dsDadosDestinatario\n")
		.append("      	,ci.dsParceiroEntrega as dsParceiroEntrega\n")
		.append("      	,ci.tpEntregarEm as tpEntregarEm\n")
		.append("      	,ci.dsDadosConsignatario as dsDadosConsignatario\n")
		.append("      	,ci.dsLocalEmissao as dsLocalEmissao\n")//Campo 05
		.append("      	,ci.dsLocalCarregamento as dsLocalCarregamento\n")//Campo 07
		.append("      	,ci.dsLocalEntrega as dsLocalEntrega\n")//Campo 08
		.append("      	,ci.dsNotificar as dsNotificar\n")//Campo 09
		.append("      	,ci.dsTransportesSucessivos as dsTransportesSucessivos\n")//Campo 10
		.append("      	,ci.dsDadosMercadoria as dsDadosMercadoria\n")//Campo 11
		.append("      	,ci.psReal as psReal\n")//Campo 12 Bruto
		.append("      	,ci.psLiquido as psLiquido\n")//Campo 12 Liquido
		.append("      	,ci.vlVolume as vlVolume\n")//Campo 13
		.append("      	,ci.vlMercadoria as vlMercadoria\n")//Campo 14
		.append("      	,ci.dsValorMercadoria as dsValorMercadoria\n")//Campo 16 Antes
		.append("      	,ci.vlTotalMercadoria as vlTotalMercadoria\n")//Campo 16 Depois
		.append("      	,ci.dsAnexos as dsAnexos\n")//Campo 17
		.append("      	,ci.dsAduanas as dsAduanas\n")//Campo 18
		.append("      	,ci.qtVolumes as qtVolumes\n")
		.append("      	,ci.vlTotalParcelas as vlTotalParcelas\n")
		.append("      	,ci.vlTotalServicos as vlTotalServicos\n")
		.append("      	,ci.vlTotalDocServico as vlTotalDocServico\n")
		.append("      	,ci.vlDesconto as vlDesconto\n")
		.append("      	,ci.nrDiasPrevEntrega as nrDiasPrevEntrega\n")
		.append("      	,ci.pcAforo as pcAforo\n")

		//Filial Origem
		.append("      	,fo.idFilial as filialByIdFilialOrigem_idFilial\n")
		.append("      	,fo.sgFilial as filialByIdFilialOrigem_sgFilial\n")
		.append("      	,po.idPessoa as filialByIdFilialOrigem_pessoa_idPessoa\n")
		.append("      	,po.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia\n")

		//Filial Destino
		.append("      	,fd.idFilial as filialByIdFilialDestino_idFilial\n")
		.append("      	,fd.sgFilial as filialByIdFilialDestino_sgFilial\n")
		.append("      	,pd.idPessoa as filialByIdFilialDestino_pessoa_idPessoa\n")
		.append("      	,pd.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia\n")
		.append("      	,epdpmufp.idPais as filialByIdFilialDestino_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_idPais\n")
		

		//Remetente
		.append("      	,cr.idCliente as clienteByIdClienteRemetente_idCliente\n")
		.append("      	,pr.nmPessoa as clienteByIdClienteRemetente_pessoa_nmPessoa\n")
		.append("      	,pr.nrIdentificacao as clienteByIdClienteRemetente_pessoa_nrIdentificacao\n")
		.append("      	,pr.tpIdentificacao as clienteByIdClienteRemetente_pessoa_tpIdentificacao\n")
		.append("      	,epr.dsEndereco as clienteByIdClienteRemetente_pessoa_enderecoPessoa_dsEndereco\n")
		.append("      	,epr.nrEndereco as clienteByIdClienteRemetente_pessoa_enderecoPessoa_nrEndereco\n")
		.append("      	,epr.dsComplemento as clienteByIdClienteRemetente_pessoa_enderecoPessoa_dsComplemento\n")
		.append("      	,epr.dsBairro as clienteByIdClienteRemetente_pessoa_enderecoPessoa_dsBairro\n")
		.append("      	,epr.nrCep as clienteByIdClienteRemetente_pessoa_enderecoPessoa_nrCep\n")
		.append("      	,eprm.idMunicipio as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_idMunicipio\n")
		.append("      	,eprm.nmMunicipio as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_nmMunicipio\n")
		.append("      	,eprmuf.sgUnidadeFederativa as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa\n")
		.append("      	,eprmuf.idUnidadeFederativa as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_unidadeFederativa_idUnidadeFederativa\n")
		.append("      	,eprmufp.idPais as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_idPais\n")
		.append("      	,eprmufp.nmPais as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_nmPais\n")
		.append("      	,eprmufpz.idZona as clienteByIdClienteRemetente_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_zona_idZona\n")
		.append("      	,eprtl.dsTipoLogradouro as clienteByIdClienteRemetente_pessoa_enderecoPessoa_tipoLogradouro_dsTipoLogradouro\n")

		//Destinatario
		.append("      	,cd.idCliente as clienteByIdClienteDestinatario_idCliente\n")
		.append("      	,cpd.nmPessoa as clienteByIdClienteDestinatario_pessoa_nmPessoa\n")
		.append("      	,cpd.nrIdentificacao as clienteByIdClienteDestinatario_pessoa_nrIdentificacao\n")
		.append("      	,cpd.tpIdentificacao as clienteByIdClienteDestinatario_pessoa_tpIdentificacao\n")
		.append("      	,epd.dsEndereco as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_dsEndereco\n")
		.append("      	,epd.nrEndereco as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_nrEndereco\n")
		.append("      	,epd.dsComplemento as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_dsComplemento\n")
		.append("      	,epd.dsBairro as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_dsBairro\n")
		.append("      	,epd.nrCep as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_nrCep\n")
		.append("      	,epdm.idMunicipio as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_idMunicipio\n")
		.append("      	,epdm.nmMunicipio as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_nmMunicipio\n")
		.append("      	,epdmuf.sgUnidadeFederativa as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa\n")
		.append("      	,epdmuf.idUnidadeFederativa as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_idUnidadeFederativa\n")
		.append("      	,epdmufp.idPais as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_idPais\n")
		.append("      	,epdmufp.nmPais as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_nmPais\n")
		.append("      	,epdmufpz.idZona as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_zona_idZona\n")
		.append("      	,epdmufp.nmPais as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_nmPais\n")
		.append("      	,epdtl.dsTipoLogradouro as clienteByIdClienteDestinatario_pessoa_enderecoPessoa_tipoLogradouro_dsTipoLogradouro\n")

		//Consignatario
		.append("      	,ccp.nmPessoa as clienteByIdClienteConsignatario_pessoa_nmPessoa\n")
		.append("      	,ccp.nrIdentificacao as clienteByIdClienteConsignatario_pessoa_nrIdentificacao\n")
		.append("      	,ccp.tpIdentificacao as clienteByIdClienteConsignatario_pessoa_tpIdentificacao\n")
		.append("      	,ccpe.dsEndereco as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_dsEndereco\n")
		.append("      	,ccpe.nrEndereco as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_nrEndereco\n")
		.append("      	,ccpe.dsComplemento as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_dsComplemento\n")
		.append("      	,ccpe.dsBairro as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_dsBairro\n")
		.append("      	,ccpe.nrCep as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_nrCep\n")
		.append("      	,ccpem.nmMunicipio as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_municipio_nmMunicipio\n")
		.append("      	,ccpemuf.sgUnidadeFederativa as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa\n")
		.append("      	,ccpemufp.nmPais as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_nmPais\n")
		.append("      	,ccpeml.dsTipoLogradouro as clienteByIdClienteConsignatario_pessoa_enderecoPessoa_tipoLogradouro_dsTipoLogradouro\n")

		//Notificado
		.append("      	,credp.nmPessoa as clienteByIdClienteRedespacho_pessoa_nmPessoa\n")
		.append("      	,credp.nrIdentificacao as clienteByIdClienteRedespacho_pessoa_nrIdentificacao\n")
		.append("      	,credp.tpIdentificacao as clienteByIdClienteRedespacho_pessoa_tpIdentificacao\n")
		.append("      	,credpe.dsEndereco as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_dsEndereco\n")
		.append("      	,credpe.nrEndereco as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_nrEndereco\n")
		.append("      	,credpe.dsComplemento as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_dsComplemento\n")
		.append("      	,credpe.dsBairro as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_dsBairro\n")
		.append("      	,credpe.nrCep as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_nrCep\n")
		.append("      	,credpem.nmMunicipio as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_municipio_nmMunicipio\n")
		.append("      	,credpemuf.sgUnidadeFederativa as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa\n")
		.append("      	,credpemufp.nmPais as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_municipio_unidadeFederativa_pais_nmPais\n")
		.append("      	,credpeml.dsTipoLogradouro as clienteByIdClienteRedespacho_pessoa_enderecoPessoa_tipoLogradouro_dsTipoLogradouro\n")

		//Moeda
		.append("      	,m.idMoeda as moeda_idMoeda\n")
		.append("      	,m.sgMoeda as moeda_sgMoeda\n")
		.append("      	,m.dsSimbolo as moeda_dsSimbolo\n")
		.append("      	,mvm.idMoeda as moedaValorMercadoria_idMoeda\n")
		.append("      	,mvtm.idMoeda as moedaValorTotalMercadoria_idMoeda\n")
		
		//Pedido Coleta
		.append("		,cipp.idPedidoColeta as pedidoColeta_idPedidoColeta")
		.append("		,cipp.nrColeta as pedidoColeta_nrColeta")
		
		//Divisao Cliente
		.append("      	,cidc.idDivisaoCliente as divisaoCliente_idDivisaoCliente\n")
		.append("      	,cidc.dsDivisaoCliente as divisaoCliente_dsDivisaoCliente\n")
		
		//Servico
		.append("      	,s.idServico as servico_idServico\n")
		.append("      	,s.tpModal as servico_tpModal\n")
		.append("      	,s.tpAbrangencia as servico_tpAbrangencia\n")
		
		//Produto e Embalagem
		.append("      	,p.idProduto as produto_idProduto\n")
		.append("      	,e.idEmbalagem as embalagem_idEmbalagem\n")

		//Usuario
		.append("      	,ua.nmUsuario as usuarioByIdUsuarioAlteracao_nmUsuario\n")
		.append("      	,ui.nmUsuario as usuarioByIdUsuarioInclusao_nmUsuario\n")
		
		
		//Tabela Preco
		.append("      	,citp.dsDescricao as tabelaPreco_dsDescricao\n")
		.append(")\n")
		.append("from	").append(getPersistentClass().getName()).append(" as ci\n")
		
		.append("join	ci.moeda as m\n")
		.append("join	ci.moedaValorMercadoria as mvm\n")
		.append("join	ci.moedaValorTotalMercadoria as mvtm\n")
		
		.append("join	ci.divisaoCliente as cidc\n")
		
		.append("join	ci.servico as s\n")

		.append("join	ci.produto as p\n")//Tipo de mercadoria
		.append("join	ci.embalagem as e\n")//Tipo de volumes
		.append("left	outer join ci.usuarioByIdUsuarioAlteracao as ua\n")
		.append("join	ci.usuarioByIdUsuarioInclusao as ui\n")

		.append("left	outer join ci.pedidoColeta as cipp\n")
		

		.append("join	ci.filialByIdFilialOrigem as fo\n")
		.append("join	fo.pessoa as po\n")

		.append("join	ci.filialByIdFilialDestino as fd\n")
		.append("join	fd.pessoa as pd\n")
		.append("join	pd.enderecoPessoa as epdp\n")
		.append("join	epdp.municipio as epdpm\n")
		.append("join	epdpm.unidadeFederativa as epdpmuf\n")
		.append("join	epdpmuf.pais as epdpmufp\n")

		.append("join	ci.clienteByIdClienteRemetente as cr\n")
		.append("join	cr.pessoa as pr\n")
		.append("join	pr.enderecoPessoa as epr\n")
		.append("join	epr.municipio as eprm\n")
		.append("join	epr.tipoLogradouro as eprtl\n")
		.append("join	eprm.unidadeFederativa as eprmuf\n")
		.append("join	eprmuf.pais as eprmufp\n")
		.append("join	eprmufp.zona as eprmufpz\n")

		.append("join	ci.clienteByIdClienteDestinatario as cd\n")
		.append("join	cd.pessoa as cpd\n")
		.append("join	cpd.enderecoPessoa as epd\n")
		.append("join	epd.municipio as epdm\n")
		.append("join	epd.tipoLogradouro as epdtl\n")
		.append("join	epdm.unidadeFederativa as epdmuf\n")
		.append("join	epdmuf.pais as epdmufp\n")
		.append("join	epdmufp.zona as epdmufpz\n")

		.append("join	ci.clienteByIdClienteConsignatario as cc\n")
		.append("join	cc.pessoa as ccp\n")
		.append("join	ccp.enderecoPessoa as ccpe\n")
		.append("join	ccpe.municipio as ccpem\n")
		.append("join	ccpe.tipoLogradouro as ccpeml\n")
		.append("join	ccpem.unidadeFederativa as ccpemuf\n")
		.append("join	ccpemuf.pais as ccpemufp\n")
		
		.append("join	ci.clienteByIdClienteRedespacho as cred\n")
		.append("join	cred.pessoa as credp\n")
		.append("join	credp.enderecoPessoa as credpe\n")
		.append("join	credpe.municipio as credpem\n")
		.append("join	credpe.tipoLogradouro as credpeml\n")
		.append("join	credpem.unidadeFederativa as credpemuf\n")
		.append("join	credpemuf.pais as credpemufp\n")

		.append("join	ci.tabelaPreco as citp\n")
		
		.append("where	ci.id = ").append(id);
		

		List result = getAdsmHibernateTemplate().find(hql.toString());

		if(result.size() == 1) {
			result = new AliasToTypedFlatMapResultTransformer().transformListResult(result);
			return (TypedFlatMap) result.get(0);
		}
		
		return null;
	}
	
	public ResultSetPage findPaginatedDevedorDocServFat(Map map, FindDefinition findDef){
		SqlTemplate sql = getSqlCtoInternacionalByDevedorDocServFat(map);
		sql.addProjection( new StringBuffer()
			.append(" new Map(cto.nrCrt as nrDocumento, cto.filial.idFilial as filial_idFilial, ")
			.append(" cto.filial.sgFilial as filial_sgFilial, doc.cliente.pessoa.nmPessoa as cliente_nmCliente, ")
			.append(" doc.vlDevido as vlDevido, des.vlDesconto as vlDesconto, doc.tpSituacaoCobranca as tpSituacaoCobranca ")
			.append(" ) ")
			.toString()
		);

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	public ResultSetPage findPaginatedCrt(TypedFlatMap criteria) {
		SqlTemplate hql = getSqlPaginatedCrt(criteria);

		hql.addProjection("new Map(ci.idDoctoServico", "idCtoInternacional");
		hql.addProjection("ci.idDoctoServico", "idDoctoServico");
		hql.addProjection("ci.tpSituacaoCrt", "tpSituacaoCrt");
		hql.addProjection("ci.nrCrt", "nrCrt");
		hql.addProjection("ci.sgPais", "sgPais");
		hql.addProjection("ci.nrPermisso", "nrPermisso");
		hql.addProjection("ci.dhEmissao", "dhEmissao");
		hql.addProjection("cr.pessoa.nmPessoa", "clienteByIdClienteRemetente_pessoa_nmPessoa");
		hql.addProjection("cd.pessoa.nmPessoa", "clienteByIdClienteDestinatario_pessoa_nmPessoa");
		hql.addProjection("pcc.nrIdentificacao", "clienteByIdClienteConsignatario_pessoa_nrIdentificacao");
		hql.addProjection("pcc.nmPessoa", "clienteByIdClienteConsignatario_pessoa_nmPessoa");
		hql.addProjection("fo.idFilial", "filialByIdFilialOrigem_idFilial");
		hql.addProjection("fo.sgFilial", "filialByIdFilialOrigem_sgFilial");
		hql.addProjection("fo.pessoa.nmFantasia", "filialByIdFilialOrigem_pessoa_nmFantasia");
		hql.addProjection("fd.sgFilial", "filialByIdFilialDestino_sgFilial)");
		hql.addOrderBy("ci.dhEmissao.value desc");

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));

		return rsp;
	}
	
	private SqlTemplate getSqlPaginatedCrt(TypedFlatMap criteria){

		Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
		Long idFilialDestino = criteria.getLong("filialByIdFilialDestino.idFilial");
		Long idDestinatario = criteria.getLong("clienteByIdClienteDestinatario.idCliente");
		Long idRemetente = criteria.getLong("clienteByIdClienteRemetente.idCliente");
		Long idConsignatario = criteria.getLong("clienteByIdClienteConsignatario.idCliente");
		Long nrCrt = criteria.getLong("nrCrt");
		YearMonthDay dhEmissaoInicial = criteria.getYearMonthDay("periodoEmissaoInicial");
		YearMonthDay dhEmissaoFinal = criteria.getYearMonthDay("periodoEmissaoFinal");

		SqlTemplate hql = new SqlTemplate();

		StringBuilder from = new StringBuilder()
		.append("CtoInternacional as ci").append("\n")
		.append("left 	join ci.filialByIdFilialOrigem as fo").append("\n")
		.append("left 	join ci.filialByIdFilialDestino as fd").append("\n")
		.append("left 	join ci.clienteByIdClienteRemetente as cr").append("\n")
		.append("left 	join ci.clienteByIdClienteDestinatario as cd").append("\n")
		.append("left 	join ci.clienteByIdClienteConsignatario as cc").append("\n")
		.append("left 	join cc.pessoa as pcc").append("\n");

		hql.addFrom(from.toString());

		hql.addCriteria("fo.id", "=", idFilialOrigem);
		hql.addCriteria("fd.id", "=", idFilialDestino);
		hql.addCriteria("cr.id", "=", idRemetente);
		hql.addCriteria("cd.id", "=", idDestinatario);
		hql.addCriteria("cc.id", "=", idConsignatario);
		hql.addCriteria("ci.nrCrt", "=", nrCrt);

		if(dhEmissaoInicial != null) {
			hql.addCriteria("ci.dhEmissao.value", ">=", dhEmissaoInicial.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()));
		}
		if(dhEmissaoFinal != null) {
			hql.addCriteria("ci.dhEmissao.value", "<", dhEmissaoFinal.plusDays(1).toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()));		
		}
		return hql;
	}
	
	public Integer getRowCountCrt(TypedFlatMap criteria){
		SqlTemplate hql = getSqlPaginatedCrt(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

	public Integer getRowCountDevedorDocServFat(Map criteria) {
		SqlTemplate sql = getSqlCtoInternacionalByDevedorDocServFat(criteria);
		sql.addProjection(" count (cto.nrCrt) ");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Busca CtoInternacional que estão em DevedorDocServFat.<BR>
	 * @param map
	 * @return
	 */
	public List findCtoInternacionalDevedorDocServFat(Map map){
		SqlTemplate sql = getSqlCtoInternacionalByDevedorDocServFat(map);
		sql.addProjection("new map(cto.id as idDocumento, cto.nrCrt as nrDocumento, " +
		"cto.filial.sgFilial as filial_sgFilial, cto.filial.idFilial as filial_idFilial, cto.vlTotalDocServico as vlDocumento)");

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()){
			list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		}
		return list;
	}

	private SqlTemplate getSqlCtoInternacionalByDevedorDocServFat(Map criteria){
		TypedFlatMap map = (TypedFlatMap)criteria;
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom( CtoInternacional.class.getName() + " cto join cto.devedorDocServFats as doc join doc.descontos as des ");
		
		String tpDocumentoServico = map.getString("tpDocumentoServico");
		if (StringUtils.isNotEmpty(tpDocumentoServico))
			sql.addCriteria("cto.tpDocumentoServico", "=", tpDocumentoServico);

		Long idCliente = map.getLong("cliente.idCliente");
		if (idCliente != null)
			sql.addCriteria("","=",idCliente);

		Long idFilial = map.getLong("filial.idFilial");
		if (idFilial != null)
			sql.addCriteria("cto.filial.id","=",idFilial);	

		String tpSituacaoAprovacao = map.getString("desconto.tpSituacaoAprovacao");
		if (StringUtils.isNotEmpty(tpSituacaoAprovacao))
			sql.addCriteria("des.tpSituacaoAprovacao","=",tpSituacaoAprovacao);

		String nrCrt = map.getString("nrDocumento");
		if (StringUtils.isNotEmpty(nrCrt))
			sql.addCriteria("cto.nrCrt", "=", Integer.valueOf(Integer.parseInt(nrCrt)) );

		return sql;
	}

	public List findByNrCrtByFilial(Long nrCrt, Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrCrt", nrCrt));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));

		return findByDetachedCriteria(dc);
	}

	public List findBySgPaisNrCrtByFilial(String sgPais, Long nrCrt, Long idFilial){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.nrCrt"), "nrCrt")
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico");
		return findByDetachedCriteria(getDetachedCriteria(sgPais, nrCrt, idFilial, pl));
	}

	public List findLookup(String sgPais, Long nrCrt, Long idFilial){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.nrCrt"), "nrCrt")
			.add(Projections.property("c.tpSituacaoCrt"), "tpSituacaoCrt")
			.add(Projections.property("c.vlTotalDocServico"), "vlTotalDocServico")
			.add(Projections.property("c.dsDadosRemetente"), "dsDadosRemetente")
			.add(Projections.property("c.dsDadosDestinatario"), "dsDadosDestinatario")
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico");
		
		return findByDetachedCriteria(getDetachedCriteria(sgPais, nrCrt, idFilial, pl));
	}

	public List findBySgPaisNrCrtByFilialFull(String sgPais, Long nrCrt, Long idFilial){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.nrCrt"), "nrCrt")
			.add(Projections.property("c.nrPermisso"), "nrPermisso")
			.add(Projections.property("c.tpSituacaoCrt"), "tpSituacaoCrt")
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico")
			.add(Projections.property("c.dsDadosRemetente"), "dsDadosRemetente")
			.add(Projections.property("c.dsDadosDestinatario"), "dsDadosDestinatario")
			.add(Projections.property("c.dsLocalEmissao"), "dsLocalEmissao")
			.add(Projections.property("c.dsDadosConsignatario"), "dsDadosConsignatario")
			.add(Projections.property("c.dsLocalCarregamento"), "dsLocalCarregamento")
			.add(Projections.property("c.dsLocalEntrega"), "dsLocalEntrega")
			.add(Projections.property("c.dsNotificar"), "dsNotificar")
			.add(Projections.property("c.dsTransportesSucessivos"), "dsTransportesSucessivos")
			.add(Projections.property("c.dsDadosMercadoria"), "dsDadosMercadoria")
			.add(Projections.property("c.psReal"), "psReal")
			.add(Projections.property("c.psLiquido"), "psLiquido")
			.add(Projections.property("c.vlVolume"), "vlVolume")
			.add(Projections.property("c.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("m.dsSimbolo"), "moeda_dsSimbolo")
			.add(Projections.property("c.dsValorMercadoria"), "dsValorMercadoria")
			.add(Projections.property("c.vlTotalMercadoria"), "vlTotalMercadoria")
			.add(Projections.property("c.dsAnexos"), "dsAnexos")
			.add(Projections.property("c.dsAduanas"), "dsAduanas")
			.add(Projections.property("c.vlFreteExterno"), "vlFreteExterno")
			.add(Projections.property("c.dsNomeRemetente"), "dsNomeRemetente")
			.add(Projections.property("cdp.nmPessoa"), "clienteByIdClienteDestinatario_pessoa_nmPessoa")
			.add(Projections.property("cdp.nrIdentificacao"), "clienteByIdClienteDestinatario_pessoa_nrIdentificacao")
			.add(Projections.property("crp.nmPessoa"), "clienteByIdClienteRemetente_pessoa_nmPessoa")
			.add(Projections.property("crp.nrIdentificacao"), "clienteByIdClienteRemetente_pessoa_nrIdentificacao")
			.add(Projections.property("fo.sgFilial"), "filialByIdFilialOrigem_sgFilial")
			.add(Projections.property("fo.idFilial"), "filialByIdFilialOrigem_idFilial")
			.add(Projections.property("po.nmFantasia"), "filialByIdFilialOrigem_pessoa_nmFantasia");
		DetachedCriteria dc = getDetachedCriteria(sgPais, nrCrt, idFilial, pl)
			.createAlias("c.moeda", "m")
			.createAlias("fo.pessoa", "po")
			.createAlias("c.clienteByIdClienteDestinatario", "cd")
			.createAlias("cd.pessoa", "cdp")
			.createAlias("c.clienteByIdClienteRemetente", "cr")
			.createAlias("cr.pessoa", "crp");
		return findByDetachedCriteria(dc);
	}

	private DetachedCriteria getDetachedCriteria(String sgPais, Long nrCrt, Long idFilial, ProjectionList pl) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass() , "c")
			.setProjection(pl)
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.add(Restrictions.eq("c.nrCrt", nrCrt))
			.add(Restrictions.eq("c.tpDocumentoServico", "CRT"))
			.add(Restrictions.eq("c.sgPais", sgPais));
		if(idFilial != null) {
			dc = dc.add(Restrictions.eq("fo.id", idFilial));
		}
		dc = dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return dc;
	}

	public List findDocumentosServico(Long idCtoInternacional) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.property("idDoctoServico"));
		dc.add(Restrictions.eq("idDoctoServico", idCtoInternacional));    	

		return findByDetachedCriteria(dc);
	}
	
	public List findAnexosByIdCtoInternacional(Long idCtoInternacional) {
		
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("da.dsDocumento"), "documentosAnexos_dsDocumento")
			.add(Projections.property("ads.dsAnexoDoctoServico"), "anexoDoctoServico_dsAnexoDoctoServico");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cto")	
			.setProjection(pl)
			.createAlias("cto.documentosAnexos", "da")
			.createAlias("da.anexoDoctoServico", "ads")
			.add(Restrictions.eq("cto.id", idCtoInternacional))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	/**
	 * Busca os documentos de servico (Ctos Internacionais - CRT)
	 * a partir do id do Manifesto de viagem internacional.
	 */
	public List findCtosInternacionaisByIdManifestoViagemInternacional(Long idManifesto){
		String sql = "select ctoInternacional from CtoInternacional as ctoInternacional, "+
		"Manifesto as manifesto, "+
		"ManifestoInternacional as manifestoInternacional, "+
		"ManifestoInternacCto as manifestoInternacCto "+
		"where manifestoInternacional.id = manifesto.id "+
		"and manifestoInternacional.id = manifestoInternacCto.manifestoViagemInternacional.id "+
		"and manifestoInternacCto.ctoInternacional.id = ctoInternacional.id "+
		"and ctoInternacional.tpDocumentoServico = 'CRT' "+
		"and manifesto.id = :idManifesto";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idManifesto", idManifesto);
	}
	
	/**
	 * Busca os documentos de servico (Ctos Internacionais - CRT)
	 * a partir do id do Manifesto de entrega.
	 */
	public List findCtosInternacionaisByIdManifestoEntrega(Long idManifesto){
		String sql = "select ctoInternacional from CtoInternacional as ctoInternacional, "+
		"Manifesto as manifesto, "+
		"ManifestoEntrega as manifestoEntrega, "+
		"ManifestoEntregaDocumento as manifestoEntregaDocumento "+
		"where manifesto.id = manifestoEntrega.id "+
		"and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id "+
		"and manifestoEntregaDocumento.doctoServico.id = ctoInternacional.id "+
		"and ctoInternacional.tpDocumentoServico = 'CRT' "+
		"and manifesto.id = :idManifesto";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idManifesto", idManifesto);
	}

	/**
	 * Retorna o tpSituacao do cto informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * 
	 * @param Long idCto
	 * @return DomainValue
	 * */	
	public DomainValue findTpSituacaoByCto(Long idCto) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c.tpSituacaoCrt");
		hql.addInnerJoin(CtoInternacional.class.getName(), "c");
		hql.addCriteria("c.id", "=", idCto);

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (DomainValue)list.get(0);
		}
		return null;
	}	
}