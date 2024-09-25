package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.*;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.expedicao.dto.QtdVolumesConhecimentoDto;
import com.mercurio.lms.expedicao.dto.UpdateVolumeDadosSorterDto;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.VolumeNotaFiscalHelper;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class VolumeNotaFiscalDAO extends BaseCrudDao<VolumeNotaFiscal, Long> {

	public static final char INDICADOR_SIM = 'S';
	public static final int ID_LOCALIZACAO_MERCADORIA_CANCELADO = 124;
	public static final int ID_LOCALIZACAO_MERCADORIA_ENTREGA_REALIZADA = 126;
	public static final int ID_LOCALIZACAO_MERCADORIA_DEVOLVIDA_REMETENTE = 182;
	public static final int ID_LOCALIZACAO_MERCADORIA_REFATURADA = 183;
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return VolumeNotaFiscal.class;
	}


    @Override
    protected void initFindByIdLazyProperties(Map lazyFindById) {
        lazyFindById.put("localizacaoMercadoria", FetchMode.JOIN);
    }


    public List findDadosVolumes(PaginatedQuery paginatedQuery, boolean isVolumeGMDireto, boolean isImprimeEtiquetaReembarque) {

    	Boolean blExibeAtendimento = Boolean.TRUE;
    	
        List<Object> parametros = new ArrayList<>();

        Long idFilial = (Long) paginatedQuery.getCriteria().get("idFilialUsuario");

        StringBuilder query = getSelectFindDadosVolume(false,blExibeAtendimento,parametros);
        query.append(getFromFindDadosVolume(blExibeAtendimento)).append(" where ");

        if (isVolumeGMDireto || isImprimeEtiquetaReembarque) {
            query.append(" conh.tpSituacaoConhecimento = 'E' ");
        } else {
            query.append(" conh.tpSituacaoConhecimento = 'P' ");
            query.append(" and fior.idFilial = ? ");
            parametros.add(idFilial);
        }
        
        query.append(" and vonf.tpVolume in ('M', 'U') ");

        if (!isImprimeEtiquetaReembarque) {
            query.append("  and mode.tpSituacaoDescarga in ('D', 'G', 'P') ");
        }

        parametros.add(paginatedQuery.getCriteria().get("nrVolumeColeta").toString());
        query.append(" and mode.idMonitoramentoDescarga in (select mode2.idMonitoramentoDescarga ")
            .append(" from VolumeNotaFiscal as vonf2, ")
            .append(" MonitoramentoDescarga as mode2, ")
            .append(" NotaFiscalConhecimento as nofc2, ")
            .append(" Conhecimento as conh2 ")
            .append(" where vonf2.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc2.idNotaFiscalConhecimento ")
            .append(" and vonf2.nrVolumeColeta = ? ")
            .append(" and mode2.idMonitoramentoDescarga = vonf2.monitoramentoDescarga.idMonitoramentoDescarga ");
        if (!isImprimeEtiquetaReembarque) {
            query.append(" and mode2.tpSituacaoDescarga in ('D', 'G', 'P') ");
        }
        if (!isVolumeGMDireto && !isImprimeEtiquetaReembarque) {
            query.append(" and conh2.filialOrigem.idFilial = ? ").append(" and conh2.tpSituacaoConhecimento = 'P' ");
            parametros.add(idFilial);
        } else {
            query.append(" and conh2.tpSituacaoConhecimento = 'E' ");
        }
        query.append(" and nofc2.conhecimento.idDoctoServico = conh2.idDoctoServico ")
                .append(" and vonf2.tpVolume in ('M', 'U')) ")
                .append(" order by mode.idMonitoramentoDescarga, vonf.idVolumeNotaFiscal ");

        return getAdsmHibernateTemplate().find(query.toString(), parametros.toArray());
    }

    public List findDadosVolume(PaginatedQuery paginatedQuery, boolean isVolumeGMDireto, boolean isDanfeSimplificada) {

    	Boolean blExibeAtendimento = Boolean.TRUE;
    	List<Object> parametros = new ArrayList<Object>();
        StringBuilder query = getSelectFindDadosVolume(false,blExibeAtendimento,parametros);

        query.append(getFromFindDadosVolume(blExibeAtendimento))

                .append(" where ")
                .append(" vonf.nrVolumeColeta = ? ")
                .append(" and conh.tpSituacaoConhecimento = ? ")
                .append(" and vonf.tpVolume in ('M', 'U') ");

        if(isDanfeSimplificada){
            query.append(" and vonf.nrVolumeEmbarque is null ");
        }

        String nr = String.valueOf(paginatedQuery.getCriteria().get("nrVolumeColeta"));

        Object fil = paginatedQuery.getCriteria().get("idFilialUsuario");
        parametros.add(nr);
        if (isVolumeGMDireto) {
            parametros.add("E");
        } else {
            parametros.add("P");
            query.append(" and fior.idFilial = ? ");
            parametros.add(fil);
        }

        return getAdsmHibernateTemplate().find(query.toString(), parametros.toArray());
    }

    public List findDadosVolumeReimpressao(PaginatedQuery paginatedQuery) {

    	Boolean blExibeAtendimento = Boolean.TRUE;
    	
    	List<Object> params = new ArrayList<Object>();
        StringBuilder query = getSelectFindDadosVolume(true,blExibeAtendimento,params);

        query.append(getFromFindDadosVolume(blExibeAtendimento));

        query.append(" where");

        StringBuilder filter = new StringBuilder();

        //LMS-3515
        filter.append(" vonf.nrVolumeEmbarque is not null ");

        if (paginatedQuery.getCriteria().get("nrVolumeColeta") != null) {

            if (((String) paginatedQuery.getCriteria().get("nrVolumeColeta")).length() != 33) {
                filter.append(" and vonf.nrVolumeColeta = ?");
            } else {
                filter.append(" and vonf.nrVolumeEmbarque = ?");
            }


            params.add(paginatedQuery.getCriteria().get("nrVolumeColeta"));
        } else if (paginatedQuery.getCriteria().get("idDoctoServico") != null) {
            filter.append(" and conh.id = ?");
            params.add(paginatedQuery.getCriteria().get("idDoctoServico"));
        } else if (paginatedQuery.getCriteria().get("idCliente") != null) {
            filter.append(" and nofc.cliente.id = ?");
            filter.append(" and nofc.nrNotaFiscal = ?");
            params.add(paginatedQuery.getCriteria().get("idCliente"));
            params.add(Integer.parseInt(paginatedQuery.getCriteria().get("nrNotaFiscal").toString()));
        } else {
            throw new BusinessException("LMS-04440");
        }


        if (paginatedQuery.getCriteria().get("nrSequencia") != null && Integer.parseInt(paginatedQuery.getCriteria().get("nrSequencia").toString()) != 0) {
            filter.append(" and ((vonf.nrSequencia = ? and vonf.tpVolume in ('U','M'))");
            filter.append(" or (vonf.nrSequenciaPalete = ? and vonf.tpVolume = 'D'))");
            params.add(Integer.parseInt(paginatedQuery.getCriteria().get("nrSequencia").toString()));
            params.add(Integer.parseInt(paginatedQuery.getCriteria().get("nrSequencia").toString()));
        }

        
		/* Remove o primeiro 'and' e adiciona o filtro na query */
        query.append(filter.toString());


        
        return getAdsmHibernateTemplate().find(query.toString(), params.toArray());
    }

    /**
     * Constrói o MAPA dos métodos: findDadosVolume() e findDadosVolumes()
     * @param blExibeAtendimento 
     * @param params 
     *
     * @return
     */
    private StringBuilder getSelectFindDadosVolume(boolean reemissao, Boolean blExibeAtendimento, List<Object> params) {

        String subQtVolumesVolumeMestre = reemissao ? "(select vm.qtVolumes from VolumeNotaFiscal vm where vm.tpVolume = 'M' and vm.nrVolumeColeta = vonf.nrVolumeColeta and vm.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento) as qtVolumesVolumeMestre," : "";

        StringBuilder query = new StringBuilder()

                .append(" select new Map (")

                .append("vonf.nrVolumeEmbarque as nrVolumeEmbarque, ")
                .append("mode.idMonitoramentoDescarga as idMonitoramentoDescarga, ")
                .append("mode.tpSituacaoDescarga as tpSituacaoDescarga, ")
                .append("metc.idMeioTransporte as idMeioTransporte, ")
                .append("metc.nrFrota as nrFrota, ")
                .append("serv.tpModal as tpModal, ")
                .append("conh.tpCargaDocumento as tpCargaDocumento, ")
                .append("conh as conhecimento, ")
                .append("conh.id as idConhecimento, ")
                .append("clieConsignatario as clieConsignatario, ")
                .append("conh.idDoctoServico as idDoctoServico, ")
                .append("conh.tpCtrcParceria as tpCtrcParceria, ")
                .append("conh.tpCalculoPreco as tpCalculoPreco, ")
                .append("conh.tpDoctoServico as tpDoctoServico, ")
                
                .append("conh.blIndicadorEdi as blIndicadorEdi, ")
                .append("conh.dsLocalEntrega as dsLocalEntrega, ")
                .append("conh.dsEnderecoEntrega as dsEnderecoEntrega, ")
                .append("conh.dhEmissao as dhEmissao, ")
                .append("conh.qtVolumes as qtVolumes, ")
                .append("conh.qtPaletes as qtPaletes, ")
                .append("conh.blPaletizacao as blPaletizacao, ")
                .append("conh.blProdutoPerigoso as blProdutoPerigoso, ")
                .append("vonf.idVolumeNotaFiscal as idVolumeNotaFiscal, ")
                .append("conh.nrConhecimento as nrPreConhecimento, ")
                .append("vonf.nrConhecimento as nrConhecimento, ")
                .append("vonf.qtVolumes as qtVolumesVolume, ")
                .append("vonf.tpVolume as tpVolume, ")
                .append("vonf.nrVolumeColeta as nrVolumeColeta, ")
                .append("vonf.psAferido as psAferido, ")
                .append("nofc.nrNotaFiscal as nrNotaFiscal, ")
                .append("vonf.nrSequencia as nrSequencia, ")
                .append("vonf.nrSequenciaPalete as nrSequenciaPalete, ")
                .append("vonf.nrDimensao1Cm as nrDimensao1Cm, ")
                .append("vonf.nrDimensao2Cm as nrDimensao2Cm, ")
                .append("vonf.nrDimensao3Cm as nrDimensao3Cm, ")
                .append("vonf.nrCubagemM3 as nrCubagemM3, ")
                .append("vonf.nrSeqNoPalete as nrSeqNoPalete, ")
                .append(subQtVolumesVolumeMestre)

                .append("roce.nrRota as routeFinalDepotNumber, ")
                .append("pessRemet.nmPessoa as nmRemetente, ")
                .append("clieRemet.blUtilizaPesoEDI as blUtilizaPesoEDIRemet, ")
                .append("clieRemet.blLiberaEtiquetaEdi as blLiberaEtiquetaEdi, ")
                .append("clieResp.blPesagemOpcional as blPesagemOpcional, ")
                .append("clieRemet.blPaleteFechado as blPaleteFechado, ")

                .append("pessConsignatario.nmPessoa as nmDestinatarioConsignatario, ")
                .append("tiloConsignatario.dsTipoLogradouro as tipoLogradouroDestinatarioConsignatario, ")
                .append("enpeConsignatario.dsEndereco as enderecoDestinatarioConsignatario, ")
                .append("enpeConsignatario.nrEndereco as nrEnderecoDestinatarioConsignatario, ")
                .append("enpeConsignatario.dsComplemento as complementoEnderecoDestinatarioConsignatario, ")
                .append("muniConsignatario.nmMunicipio as municipioDestinatarioConsignatario, ")
                .append("unfeConsignatario.sgUnidadeFederativa as unidFederativaDestinatarioConsignatario, ")
                .append("paisConsignatario.nmPais as paisDestinatarioConsignatario, ")
                .append("enpeConsignatario.nrCep as cepDestinatarioConsignatario, ")
                .append("enpeConsignatario.dsBairro as bairroDestinatarioConsignatario, ")

                .append("pessDestinatario.nmPessoa as nmDestinatarioLocalEntrega, ")
                .append("tiloLocalEntrega.dsTipoLogradouro as tipoLogradouroDestinatarioLocalEntrega, ")
                .append("conh.dsEnderecoEntrega as enderecoDestinatarioLocalEntrega, ")
                .append("conh.nrEntrega as nrEnderecoDestinatarioLocalEntrega, ")
                .append("conh.dsComplementoEntrega as complementoEnderecoDestinatarioLocalEntrega, ")
                .append("muniLocalEntrega.nmMunicipio as municipioDestinatarioLocalEntrega, ")
                .append("muniLocalEntrega.idMunicipio as idMunicipioDestinatarioLocalEntrega, ")
                .append("unfeLocalEntrega.sgUnidadeFederativa as unidFederativaDestinatarioLocalEntrega, ")
                .append("paisLocalEntrega.nmPais as paisDestinatarioLocalEntrega, ")
                .append("conh.nrCepEntrega as cepDestinatarioLocalEntrega, ")
                .append("conh.dsBairroEntrega as bairroDestinatarioLocalEntrega, ")
                .append("clieResp.blEtiquetaPorVolume as blEtiquetaPorVolume,")

                .append("pessDestinatario.nmPessoa as nmDestinatarioDestinatario, ")
                .append("tiloDestinatario.dsTipoLogradouro as tipoLogradouroDestinatarioDestinatario, ")
                .append("enpeDestinatario.dsEndereco as enderecoDestinatarioDestinatario, ")
                .append("enpeDestinatario.nrEndereco as nrEnderecoDestinatarioDestinatario, ")
                .append("enpeDestinatario.dsComplemento as complementoEnderecoDestinatarioDestinatario, ")
                .append("muniDestinatario.nmMunicipio as municipioDestinatarioDestinatario, ")
                .append("unfeDestinatario.sgUnidadeFederativa as unidFederativaDestinatarioDestinatario, ")
                .append("paisDestinatario.nmPais as paisDestinatarioDestinatario, ")
                .append("enpeDestinatario.nrCep as cepDestinatarioDestinatario, ")
                .append("enpeDestinatario.dsBairro as bairroDestinatarioDestinatario, ")

                .append("fior.idFilial as idFilialOrigem, ")
                .append("fior.sgFilial as sgFilialOrigem, ")
                .append("fior.codFilial as codFilialFilialOrigem, ")
                .append("pessFilialOrigem.nrIdentificacao as nrIdentificacaoFilialOrigem, ")
                .append("fido.idFilial as idFilialDestino, ")
                .append("fido.codFilial as codFilialDestino, ")
                .append("pessFilialDestinoOperacional.nrIdentificacao as nrIdentificacaoFilialDestino, ")
                .append("serv.idServico as idServico ");
                

                query.append(" ) ");

        return query;
    }

    
    public Map<String,Object> findAtendimentoByMunicipioFilialServicoVigente(Long idMunicipio, Long idFilial, Long idServico){
    	String query = montarConsultaFindAtendimentoByMunicipioFilialServicoVigente(idMunicipio, idFilial, idServico, Boolean.FALSE);

    	Map<String,Object> criteria = new HashMap<String, Object>();
    	criteria.put("idMunicipio", idMunicipio);
    	criteria.put("idFilial", idFilial);
    	criteria.put("idServico", idServico);
    	
    	List<Object[]> result = configureQueryFindAtendimentoByMunicipioFilialServicoVigente(query, criteria);

        if (result == null || !CollectionUtils.isNotEmpty(result)){
            query = montarConsultaFindAtendimentoByMunicipioFilialServicoVigente(idMunicipio, idFilial, idServico, Boolean.TRUE);
            result = configureQueryFindAtendimentoByMunicipioFilialServicoVigente(query, criteria);
        }

    	if (result != null && CollectionUtils.isNotEmpty(result)){
    		Object[] data = result.get(0);
    		Map<String,Object> mapAtendimento = new HashMap<String,Object>();
    		mapAtendimento.put("blDomingo",data[0]);
    		mapAtendimento.put("blSegunda",data[1]);
    		mapAtendimento.put("blTerca",data[2]);
    		mapAtendimento.put("blQuarta",data[3]);
    		mapAtendimento.put("blQuinta",data[4]);
    		mapAtendimento.put("blSexta",data[5]);
    		mapAtendimento.put("blSabado",data[6]);
    		return mapAtendimento;
    	}
    	return null;
    }

    private String montarConsultaFindAtendimentoByMunicipioFilialServicoVigente(Long idMunicipio, Long idFilial, Long idServico, Boolean blPadraoMcd){
        StringBuilder query = new StringBuilder();
        query.append("select osl.BL_DOMINGO, ")
        .append("osl.BL_SEGUNDA, ")
        .append("osl.BL_TERCA, ")
        .append("osl.BL_QUARTA, ")
        .append("osl.BL_QUINTA, ")
        .append("osl.BL_SEXTA, ")
        .append("osl.BL_SABADO, ")
        .append("osl.ID_SERVICO ")
        .append("from operacao_servico_localiza osl,  municipio_filial mf ")
        .append("where mf.ID_MUNICIPIO = :idMunicipio ");
        if(blPadraoMcd) {
            query.append("and mf.bl_padrao_mcd = 'S' ");
        }else{
            query.append("and mf.id_filial = :idFilial ");
        }
        query.append("and osl.id_municipio_filial = mf.id_municipio_filial ")
        .append("and (osl.id_servico = :idServico or osl.id_servico is null) ")
        .append("and osl.dt_vigencia_inicial <= sysdate ")
        .append("and osl.dt_vigencia_final >= sysdate ")
        .append("and mf.dt_vigencia_inicial <= sysdate ")
        .append("and mf.dt_vigencia_final >= sysdate ")
        .append("order by id_servico ");

        return query.toString();
    }

    private List<Object[]> configureQueryFindAtendimentoByMunicipioFilialServicoVigente(String query, Map<String,Object> criteria){
        return getAdsmHibernateTemplate().findBySql(query, criteria, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("BL_DOMINGO", Hibernate.STRING);
                query.addScalar("BL_SEGUNDA", Hibernate.STRING);
                query.addScalar("BL_TERCA", Hibernate.STRING);
                query.addScalar("BL_QUARTA", Hibernate.STRING);
                query.addScalar("BL_QUINTA", Hibernate.STRING);
                query.addScalar("BL_SEXTA", Hibernate.STRING);
                query.addScalar("BL_SABADO", Hibernate.STRING);


            }
        });
    }
    
    
    /**
     * Constrói o FROM dos métodos: findDadosVolume() e findDadosVolumes()
     * @param blExibeAtendimento 
     *
     * @return
     */
    private StringBuilder getFromFindDadosVolume(Object blExibeAtendimento) {

        StringBuilder query = new StringBuilder()

                .append(" from VolumeNotaFiscal vonf ")
                .append(" join vonf.notaFiscalConhecimento nofc ")
                .append(" join nofc.conhecimento conh ")

                .append(" left outer join conh.devedorDocServs dds ")
                .append(" left outer join dds.cliente clieResp ")

                .append(" left outer join vonf.monitoramentoDescarga mode ")
                .append(" left outer join mode.meioTransporte metc ")

                .append(" left outer join conh.servico serv ") //conhecimento.getServico()
                .append(" left outer join conh.rotaColetaEntregaByIdRotaColetaEntregaSugerid roce ") //conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid()
                .append(" left outer join conh.clienteByIdClienteRemetente clieRemet ") //conhecimento.getClienteByIdClienteRemetente();
                .append(" left outer join clieRemet.pessoa pessRemet ") //conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
                .append(" left outer join conh.clienteByIdClienteConsignatario clieConsignatario ") //conhecimento.getClienteByIdClienteConsignatario()
                .append(" left outer join clieConsignatario.pessoa pessConsignatario ") //clienteConsignatario.getPessoa()
                .append(" left outer join pessConsignatario.enderecoPessoa enpeConsignatario ") //clienteConsignatario.getPessoa().getEnderecoPessoa()
                .append(" left outer join enpeConsignatario.tipoLogradouro tiloConsignatario ") //clienteConsignatario.getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro()
                .append(" left outer join enpeConsignatario.municipio muniConsignatario ") //clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio()
                .append(" left outer join muniConsignatario.unidadeFederativa unfeConsignatario ") //clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa()
                .append(" left outer join unfeConsignatario.pais paisConsignatario ") //clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais()
                .append(" left outer join conh.tipoLogradouroEntrega tiloLocalEntrega ") //conhecimento.getTipoLogradouroEntrega()
                .append(" join conh.municipioByIdMunicipioEntrega muniLocalEntrega ") //conhecimento.getMunicipioByIdMunicipioEntrega()
                .append(" join muniLocalEntrega.unidadeFederativa unfeLocalEntrega ") //conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa()
                .append(" join unfeLocalEntrega.pais paisLocalEntrega ") //conhecimento.getMunicipioByIdMunicipioEntrega().getUnidadeFederativa().getPais()
                .append(" left outer join conh.clienteByIdClienteDestinatario clieDestinatario ") //conhecimento.getClienteByIdClienteDestinatario()
                .append(" left outer join clieDestinatario.pessoa pessDestinatario ") //clienteByIdClienteDestinatario.getPessoa()
                .append(" left outer join pessDestinatario.enderecoPessoa enpeDestinatario ") //clienteByIdClienteDestinatario.getPessoa().getEnderecoPessoa()
                .append(" left outer join enpeDestinatario.tipoLogradouro tiloDestinatario ") //clienteByIdClienteDestinatario.getPessoa().getEnderecoPessoa().getTipoLogradouro()
                .append(" left outer join enpeDestinatario.municipio muniDestinatario ") //clienteByIdClienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio()
                .append(" left outer join muniDestinatario.unidadeFederativa unfeDestinatario ") //clienteByIdClienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa()
                .append(" left outer join unfeDestinatario.pais paisDestinatario ") //clienteByIdClienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais()
                .append(" join conh.filialOrigem fior ") //conhecimento.getFilialOrigem()
                .append(" join conh.filialByIdFilialDestino fide ") //doctoServico.filialByIdFilialDestino()
                .append(" join fior.pessoa pessFilialOrigem ") //conhecimento.getFilialOrigem().getPessoa()
                .append(" left outer join conh.filialDestinoOperacional fido ") //conhecimento.getFilialDestinoOperacional()
                .append(" left outer join fido.pessoa pessFilialDestinoOperacional "); //conhecimento.getFilialDestinoOperacional().getPessoa()
        	
        
        
        	
        

        return query;
    }

    public Map findAllConhecimentosByNrVolumeColetaByIdFilial(String nrVolumecoleta, Long idFilialUsuario) {
        Map<Long, List> mapResult = new HashMap<Long, List>();

        StringBuilder query = new StringBuilder()
                .append(" select distinct mode2.idMonitoramentoDescarga as idMonitoramentoDescarga ")
                .append(" from VolumeNotaFiscal as vonf2, ")
                .append(" 	NotaFiscalConhecimento as nofc2, ")
                .append(" 	Conhecimento as conh2, ")
                .append(" 	MonitoramentoDescarga as mode2 ")
                .append(" where vonf2.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc2.idNotaFiscalConhecimento ")
                .append(" and vonf2.nrVolumeColeta = ? ")
                .append(" and mode2.idMonitoramentoDescarga = vonf2.monitoramentoDescarga.idMonitoramentoDescarga ")
                .append(" and conh2.filialOrigem.idFilial = ? ")
                .append(" and nofc2.conhecimento.idDoctoServico = conh2.idDoctoServico ")
                .append(" and vonf2.tpVolume in ('M', 'U') ")
                .append(" and conh2.tpSituacaoConhecimento = ? ");

        List<Long> list = getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrVolumecoleta, idFilialUsuario, "P"});
        if (list != null && list.size() > 0) {
            for (Long idMonitoramentoDescarga : list) {
                query = new StringBuilder()
                        .append(" select distinct conh as conhecimento ")
                        .append(" from VolumeNotaFiscal vonf ")
                        .append(" 	join vonf.notaFiscalConhecimento nofc ")
                        .append(" 	join nofc.conhecimento conh ")
                        .append(" 	join vonf.monitoramentoDescarga mode ")
                        .append(" 	join conh.filialOrigem fior ")
                        .append(" where conh.tpSituacaoConhecimento = 'P' ")
                        .append(" and fior.idFilial = ? ")
                        .append(" and mode.idMonitoramentoDescarga = ? ")
                        .append(" and vonf.tpVolume in ('M', 'U') ")
                        .append(" and mode.tpSituacaoDescarga in ('D', 'G', 'P') ");

                mapResult.put(idMonitoramentoDescarga, getAdsmHibernateTemplate().find(query.toString(), new Object[]{idFilialUsuario, idMonitoramentoDescarga}));
            }
            return mapResult;
        }

        return null;
    }

    public List findVolumesPaleteTipoDPeloNrVolumeColeta(String nrVolumeColeta) {
        StringBuilder query = new StringBuilder()
                .append(" select new map( vnf.nrSequencia as nrSequencia, ")
                .append(" 				  vnf.nrVolumeEmbarque as nrVolumeEmbarque,  ")
                .append(" 				  vnf.nrSequenciaPalete as nrSequenciaPalete,  ")
                .append(" 				  vnf.qtVolumes as qtVolumes,  ")
                .append(" 				  vnf.tpVolume as tpVolume ")
                .append(" 				) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" where vnf.nrVolumeColeta = ? ")
                .append(" and vnf.tpVolume in ('D') ");
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrVolumeColeta});
    }

    /**
     * Busca o somatório de Peso Aferido dos Volumes
     *
     * @param idNotaFiscalConhecimento
     * @return
     * @author André Valadas
     */
    public BigDecimal findTotalPsAferidoByIdNotaFiscalConhecimento(final Long idNotaFiscalConhecimento) {
        StringBuilder query = new StringBuilder()
                .append(" select sum(vnf.psAferido) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" where vnf.notaFiscalConhecimento.id = ? ")
                .append("  and vnf.tpVolume in ('M', 'U') ")
                .append("   and vnf.psAferido is not null ");
        return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idNotaFiscalConhecimento});
    }

    public BigDecimal findMaxPsAferidoByIdDoctoServico(final Long idDoctoServico) {
        StringBuilder query = new StringBuilder()
                .append(" select max(vnf.psAferido) ")
                .append(" from VolumeNotaFiscal as vnf, ")
                .append(" NotaFiscalConhecimento as nfc, ")
                .append(" Conhecimento as conh ")
                .append(" where vnf.notaFiscalConhecimento.idNotaFiscalConhecimento = nfc.idNotaFiscalConhecimento ")
                .append(" and nfc.conhecimento.idDoctoServico = conh.idDoctoServico ")
                .append(" and conh.idDoctoServico = ? ")
                .append(" and vnf.psAferido is not null ");
        return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idDoctoServico});
    }
    
    public List findVolumeNotaFiscalByIdMonitoramentoDescarga(Long id) {
        StringBuilder query = new StringBuilder()
                .append(" select new map(mode.qtVolumesTotal as qtVolumesTotal, count(vnf.id) as countIds) ")
                .append("   from VolumeNotaFiscal as vnf ")
                .append("   join vnf.monitoramentoDescarga mode ")
                .append("  where mode.id = ? ")
                .append("    and vnf.tpVolume in ('D', 'U') ")
                .append("    and vnf.psAferido is not null ")
                .append(" group by mode.qtVolumesTotal ");
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{id});
    }

    public List findQtVolumesNotaFiscalByIdMonitoramentoDescarga(Long id) {
        StringBuilder query = new StringBuilder()
                .append(" select new map(mode.qtVolumesTotal as qtVolumesTotal, nvl(sum(nvl(vnf.qtVolumes,0)),0) as qtVolumes) ")
                .append("   from VolumeNotaFiscal as vnf ")
                .append("   join vnf.monitoramentoDescarga mode ")
                .append("  where mode.id = ? ")
                .append("    and vnf.psAferido is not null ")
                .append(" group by mode.qtVolumesTotal ");
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{id});
    }

    public List<VolumeNotaFiscal> findVolumeNotaFiscalByMonitoramentoDescarga(Long id, boolean isVolumeGMDireto) {
        StringBuilder query = new StringBuilder()
                .append(" select vnf ")
                .append("   from VolumeNotaFiscal as vnf ")
                .append("   join vnf.monitoramentoDescarga mode ")
                .append("  where mode.id = ? ")
                .append("    and vnf.tpVolume in ('D', 'U') ");
        if (isVolumeGMDireto) {
            query.append(" and vnf.nrVolumeEmbarque is null ");
        } else {
            query.append(" and vnf.psAferido is null ");
        }
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{id});
    }

    public List<Long> findVolumeByIdConhecimentoAndNrSequencia(final Long idConhecimento, final Integer nrSequencia) {
        final StringBuilder query = new StringBuilder();

        query.append(" select vnf.nrVolumeColeta as nrVolumeColeta ")
                .append(" from VolumeNotaFiscal as vnf, ")
                .append(" 	NotaFiscalConhecimento as nfc ")
                .append(" where vnf.notaFiscalConhecimento.idNotaFiscalConhecimento = nfc.idNotaFiscalConhecimento ")
                .append(" and vnf.psAferido is not null ")
                .append(" and nfc.conhecimento.id = ? ");

        if (nrSequencia != 0) {
            query.append(" and ((vnf.nrSequencia = ? and vnf.tpVolume = 'U')");
            query.append(" or (vnf.nrSequenciaPalete = ? and vnf.tpVolume = 'D'))");
            return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idConhecimento, nrSequencia, nrSequencia});

        } else {
            return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idConhecimento});
        }

    }


    public List<VolumeNotaFiscal> findByIdConhecimentoAndIdLocalizacaoFilial(Long idConhecimento, Long idFilial) {
        final StringBuilder query = new StringBuilder();
        query.append("select vnf ")
                .append("  from VolumeNotaFiscal as vnf ")
                .append(" 	inner join fetch vnf.notaFiscalConhecimento as nfc ")
                .append(" 	inner join fetch vnf.localizacaoFilial as filial ")
                .append("  inner join fetch vnf.localizacaoMercadoria localizacao ")
                .append(" where  filial.idFilial = ? ")
                .append("        and nfc.conhecimento.id = ? ");

        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idFilial, idConhecimento});

    }


    public List<VolumeNotaFiscal> findByIdConhecimento(Long idConhecimento) {
        final StringBuilder query = new StringBuilder();
        query.append("select vnf ")
                .append("  from VolumeNotaFiscal as vnf ")
                .append(" 	inner join fetch vnf.notaFiscalConhecimento as nfc ")
                .append(" where  nfc.conhecimento.id = ? ");

        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idConhecimento});

    }
    
    public List<VolumeNotaFiscal> findByIdNotaFiscal(Long idNotaFiscal) {
        final StringBuilder query = new StringBuilder();
        query.append("select vnf ")
                .append("  from VolumeNotaFiscal as vnf ")
                .append(" 	inner join fetch vnf.notaFiscalConhecimento as nfc ")
                .append(" where  nfc.id = ? ");

        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idNotaFiscal});

    }

    public VolumeNotaFiscal findVolumeByIdConhecimento(Long idConhecimento) {
        final StringBuilder query = new StringBuilder();
        query.append("select vnf ")
                .append("  from VolumeNotaFiscal as vnf ")
                .append(" 	inner join fetch vnf.notaFiscalConhecimento as nfc ")
                .append(" where  nfc.conhecimento.id = ? ")
                .append(" and vnf.nrSequencia = 1 ")
                .append(" and vnf.tpVolume in ('U', 'M') ");

        return (VolumeNotaFiscal) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idConhecimento});
    }

    public List<Long> findVolumeByClienteNotaFiscalSequenciaVol(final Long idCliente, final Integer nrNotaFiscal, final Integer nrSequencia) {
        final StringBuilder query = new StringBuilder();

        query.append(" select vnf.nrVolumeColeta as nrVolumeColeta ")
                .append(" from VolumeNotaFiscal as vnf, ")
                .append(" 	NotaFiscalConhecimento as nfc ")
                .append(" where vnf.notaFiscalConhecimento.idNotaFiscalConhecimento = nfc.idNotaFiscalConhecimento ")
                .append(" and nfc.nrNotaFiscal = ? ")
                .append(" and vnf.psAferido is not null ")
                .append(" and nfc.cliente.id = ? ");

        if (nrSequencia != 0) {
            query.append(" and ((vnf.nrSequencia = ? and vnf.tpVolume = 'U')");
            query.append(" or (vnf.nrSequenciaPalete = ? and vnf.tpVolume = 'D'))");
            return getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrNotaFiscal, idCliente, nrSequencia, nrSequencia});

        } else {
            return getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrNotaFiscal, idCliente});
        }
    }

    /**
     * Consulta Otimizada para busca de Volumes pelo Código de Barras
     *
     * @param codigoBarras
     * @param alias
     * @return
     * @author André Valadas
     */
    public List<VolumeNotaFiscal> findByCodigoBarras(final String codigoBarras, final Map<String, String> alias) {
		Criteria criteria = obterCriteriaFindVolumeNotaFiscalByCodigoBarras(codigoBarras, alias);
		return (List<VolumeNotaFiscal>) criteria.list();
	}

	public List<VolumeNotaFiscal> findByCodigoBarrasMWW(final String codigoBarras, final Map<String, String> alias, Date dataMinimaMWW) {
		Criteria criteria = obterCriteriaFindVolumeNotaFiscalByCodigoBarras(codigoBarras, alias);
		criteria.add(Restrictions.ge("con.dhEmissao.value", dataMinimaMWW));
		return (List<VolumeNotaFiscal>) criteria.list();
	}

	private Criteria obterCriteriaFindVolumeNotaFiscalByCodigoBarras(String codigoBarras, Map<String, String> alias) {
		Criteria criteria = createCriteria();
        /** Joins */
		configAlias(criteria, alias, JoinFragment.LEFT_OUTER_JOIN);
        /** Restrictions */
		criteria.add(
				Restrictions.or(
						Restrictions.eq("nrVolumeEmbarque", codigoBarras),
						Restrictions.and
								(Restrictions.eq("nrVolumeColeta", codigoBarras),
                        Restrictions.in("tpVolume", new Object[]{"M", "U"}))));
		return criteria;
	}

	public List<VolumeNotaFiscal> findByCodigoBarras(final String codigoBarras){
		DetachedCriteria dc = obterDetachedCriteriaFindByCodigoBarras(codigoBarras);
		return (List<VolumeNotaFiscal>) getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public List<VolumeNotaFiscal> findByCodigoBarrasMWW(String barcode, Date dataMinimaMWW) {
		DetachedCriteria dc = obterDetachedCriteriaFindByCodigoBarras(barcode);
		dc.add(Restrictions.ge("CO.dhEmissao.value", dataMinimaMWW));
        return (List<VolumeNotaFiscal>) getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	private DetachedCriteria obterDetachedCriteriaFindByCodigoBarras(String codigoBarras) {
		return DetachedCriteria.forClass(VolumeNotaFiscal.class)
                .setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
                .setFetchMode("localizacaoFilial", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .setFetchMode("monitormanetoDescarga", FetchMode.JOIN)
			.createAlias("notaFiscalConhecimento.conhecimento", "CO")
                .setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteRemetente", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteDestinatario", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteRemetente.pessoa", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.servico", FetchMode.JOIN)
		    .setFetchMode("notaFiscalConhecimento.conhecimento.doctoServico", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao.tipoDispositivoUnitizacao", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao.dispositivoUnitizacaoPai", FetchMode.JOIN)
                .add(Restrictions.or(Restrictions.eq("nrVolumeEmbarque", codigoBarras),
                        Restrictions.and(Restrictions.eq("nrVolumeColeta", codigoBarras),
                                Restrictions.in("tpVolume", new Object[]{"M", "U"}))));
    }

    public List<VolumeNotaFiscal> findByDispositivoUnitizacao(Long idDispositivoUnitizacao) {
        DetachedCriteria dc = DetachedCriteria.forClass(VolumeNotaFiscal.class)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao", FetchMode.JOIN)
                .add(Restrictions.eq("dispositivoUnitizacao.id", idDispositivoUnitizacao));

        return super.findByDetachedCriteria(dc);
    }

    public List findByNrVolumeColeta(String nrVolumeColeta) {
        StringBuilder query = new StringBuilder()
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" where vnf.nrVolumeColeta = ? ")
                .append(" and vnf.tpVolume in ('M', 'U') ");
        return getAdsmHibernateTemplate().find(query.toString(), new Object[]{nrVolumeColeta});
    }

    public Long countVolumesByIntervaloEtiquetas(String nrVolumeColetaInicial, String nrVolumeColetaFinal) {
        List<String> etiquetasIntervalo = new ArrayList<String>();
        Long count = 0l;
        long inicio = Long.valueOf(nrVolumeColetaInicial) + 1;
        long fim = Long.valueOf(nrVolumeColetaFinal) - inicio;
        long fimReal = Long.valueOf(nrVolumeColetaFinal);
        int qt = Long.valueOf(fim / 1000).intValue();
        long[] fimAr = new long[qt];
        for (long l : fimAr) {
            for (long i = 0; i < 1000; i++, inicio++) {
                etiquetasIntervalo.add(FormatUtils.fillNumberWithZero(String.valueOf(inicio), 12));
            }
            count += countVolumesByIntervaloEtiquetas(etiquetasIntervalo);
            etiquetasIntervalo = new ArrayList<String>();
        }
        for (long i = inicio; i < fimReal; i++) {
            etiquetasIntervalo.add(FormatUtils.fillNumberWithZero(String.valueOf(i), 12));
        }
        if (etiquetasIntervalo.size() > 0) count += countVolumesByIntervaloEtiquetas(etiquetasIntervalo);
        return count;
    }

	public Long countVolumesByIntervaloEtiquetas(List<String> etiquetasIntervalo) {
		StringBuffer hql = new StringBuffer();

        hql.append(" select count(vnf.id) \n");
        hql.append("   from " + VolumeNotaFiscal.class.getName() + " as vnf \n");
        hql.append("   join vnf.notaFiscalConhecimento as nfc \n");
        hql.append("   join nfc.conhecimento conh \n");
        hql.append("  where vnf.tpVolume in ('M', 'U') \n");
        hql.append("    and (conh.blIndicadorEdi is null OR conh.blIndicadorEdi = 'N') \n");
        hql.append("    and vnf.nrVolumeColeta in (:nrVolumeColeta) \n");

        Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
        query.setParameterList("nrVolumeColeta", etiquetasIntervalo);

        return (Long) query.uniqueResult();
    }

    public List findAll() {
        return super.findAllEntities();
    }

    public VolumeNotaFiscal updateDataById(final String nrVolumeEmbarque, final BigDecimal psAferido, final Long idVolumeNotaFiscal, final Integer qtVolumes, final String tpVolume, final Integer nrSequenciaPalete, String dsMac, Long idUsuario, Long idFilial, Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, Double nrCubagemM3, DomainValue tpOrigemDimensoes, DomainValue tpOrigemPeso, Integer nrSeqNoPalete) {
        VolumeNotaFiscal vol = findById(idVolumeNotaFiscal);
        vol.setPsAferido(psAferido);
        VolumeNotaFiscalHelper.setDimensoes(vol, nrDimensao1Cm, nrDimensao2Cm, nrDimensao3Cm, nrCubagemM3);
        if (qtVolumes != null) {
            vol.setQtVolumes(qtVolumes);
        }
        if (tpVolume != null) {
            vol.setTpVolume(tpVolume);
        }
        if (nrSequenciaPalete != null) {
            vol.setNrSequenciaPalete(nrSequenciaPalete);
        }
        vol.setNrVolumeEmbarque(nrVolumeEmbarque);
        vol.setDsMac(dsMac);
        vol.setDhEmissao(new DateTime());
        UsuarioLMS usuario = new UsuarioLMS();
        usuario.setIdUsuario(idUsuario);
        Filial filial = new Filial();
        filial.setIdFilial(idFilial);
        vol.setUsuario(usuario);
        vol.setFilialEmissao(filial);
        vol.setTpOrigemDimensoes(tpOrigemDimensoes);
        vol.setTpOrigemPeso(tpOrigemPeso);
        vol.setNrSeqNoPalete(nrSeqNoPalete);

        store(vol, true);
        return vol;
    }

    public void updateVolumeDadosSorterById(final UpdateVolumeDadosSorterDto dados) {

        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                String sql = "UPDATE VOLUME_NOTA_FISCAL SET ";

                String sqlSet = "";

                if (dados.getPsAferido() != null) {
                    sqlSet += ", PS_AFERIDO = :psAferido \n";
                }

                if (dados.getNrDimensao1Cm() != null) {
                    sqlSet += ", NR_DIMENSAO_1_CM = :nrDimensao1Cm \n";
                }
                if (dados.getNrDimensao2Cm() != null) {
                    sqlSet += ", NR_DIMENSAO_2_CM = :nrDimensao2Cm \n";
                }
                if (dados.getNrDimensao3Cm() != null) {
                    sqlSet += ", NR_DIMENSAO_3_CM = :nrDimensao3Cm \n";
                }
                if (dados.getDhPesagem() != null) {
                    sqlSet += ", DH_PESAGEM = to_timestamp_tz(:dhPesagem,'yyyy-mm-dd hh24:mi:ss tzh:tzm') \n";
                    sqlSet += ", DH_PESAGEM_TZR = 'America/Sao_Paulo' \n";
                }

                if (dados.getNrDimensao1Sorter() != null && dados.getNrDimensao1Sorter() > 0) {
                    sqlSet += ", NR_DIMENSAO_1_SORTER = :nrDimensao1Sorter \n";
                }
                if (dados.getNrDimensao2Sorter() != null && dados.getNrDimensao2Sorter() > 0) {
                    sqlSet += ", NR_DIMENSAO_2_SORTER = :nrDimensao2Sorter \n";
                }
                if (dados.getNrDimensao3Sorter() != null && dados.getNrDimensao3Sorter() > 0) {
                    sqlSet += ", NR_DIMENSAO_3_SORTER = :nrDimensao3Sorter \n";
                }
                if (dados.getPsAferidoSorter() != null && (dados.getPsAferidoSorter().compareTo(new BigDecimal(0)) > 0)) {
                    sqlSet += ", PS_AFERIDO_SORTER = :psAferidoSorter \n";
                }

                if (sqlSet.startsWith(",")) {
                    sqlSet = sqlSet.substring(1);
                }
                sql += sqlSet;

                sql += " WHERE ID_VOLUME_NOTA_FISCAL = :idVolumeNotaFiscal";

                Query query = session.createSQLQuery(sql);

                if (dados.getPsAferido() != null) {
                    query.setBigDecimal("psAferido", dados.getPsAferido());
                }

                if (dados.getNrDimensao1Cm() != null) {
                    query.setInteger("nrDimensao1Cm", dados.getNrDimensao1Cm());
                }
                if (dados.getNrDimensao2Cm() != null) {
                    query.setInteger("nrDimensao2Cm", dados.getNrDimensao2Cm());
                }
                if (dados.getNrDimensao3Cm() != null) {
                    query.setInteger("nrDimensao3Cm", dados.getNrDimensao3Cm());
                }
                if (dados.getDhPesagem() != null) {
                    String dhPesaggem = dados.getDhPesagem().toString("yyyy-MM-dd HH:mm:ss ZZ");
                    query.setString("dhPesagem", dhPesaggem);
                }
                if (dados.getNrDimensao1Sorter() != null && dados.getNrDimensao1Sorter() > 0) {
                    query.setInteger("nrDimensao1Sorter", dados.getNrDimensao1Sorter());
                }
                if (dados.getNrDimensao2Sorter() != null && dados.getNrDimensao2Sorter() > 0) {
                    query.setInteger("nrDimensao2Sorter", dados.getNrDimensao2Sorter());
                }
                if (dados.getNrDimensao3Sorter() != null && dados.getNrDimensao3Sorter() > 0) {
                    query.setInteger("nrDimensao3Sorter", dados.getNrDimensao3Sorter());
                }
                if (dados.getPsAferidoSorter() != null && (dados.getPsAferidoSorter().compareTo(new BigDecimal(0)) > 0)) {
                    query.setBigDecimal("psAferidoSorter", dados.getPsAferidoSorter());
                }

                query.setLong("idVolumeNotaFiscal", dados.getIdVolumeNotaFiscal());

                query.executeUpdate();
                return null;
            }
        });

    }

    public void updateNrVolumeEmbarqueById(final Long idVolumeNotaFiscal, final String nrVolumeEmbarque) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String hql = "UPDATE " + getPersistentClass().getName() + " vnf SET " +
                        " vnf.nrVolumeEmbarque = :nrVolumeEmbarque " +
                        " WHERE vnf.idVolumeNotaFiscal = :idVolumeNotaFiscal";

                query = session.createQuery(hql);
                query.setString("nrVolumeEmbarque", nrVolumeEmbarque);
                query.setLong("idVolumeNotaFiscal", idVolumeNotaFiscal);
                query.executeUpdate();

                return null;
            }
        });
    }

    public Map findVolumeNotaFiscalNaoAferido(final String nrVolumeEmbarque) {
        StringBuilder query = new StringBuilder()
                .append("select new map(vnf.id as idVolumeNotaFiscal ")
                .append("              ,vnf.nrConhecimento as nrConhecimento ")
                .append("              ,conh.id as idConhecimento ")
                .append("              ,conh.blIndicadorEdi as blIndicadorEdi ")
                .append("              ,fo.id as idFilialOrigem ")
                .append("              ,clieRemet.blUtilizaPesoEDI as blUtilizaPesoEDIRemet ")
                .append("               ) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" join vnf.notaFiscalConhecimento nfc ")
                .append(" join nfc.conhecimento conh ")
                .append(" join conh.filialOrigem fo ")
                .append(" left outer join conh.clienteByIdClienteRemetente clieRemet ")
                .append(" where vnf.nrVolumeEmbarque = ? ")
                .append(" and vnf.tpVolume in ('M', 'U') ")
                .append(" and conh.tpSituacaoConhecimento = 'P' ")
                .append(" and vnf.psAferido is null ");
        return (Map) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{nrVolumeEmbarque});
    }

    //LMS-2354
    public Map findVolumeNotaFiscalNaoAferidoSorter(final String nrVolumeEmbarque) {
        StringBuilder query = new StringBuilder()
                .append("select new map(vnf.id as idVolumeNotaFiscal ")
                .append("              ,vnf.nrConhecimento as nrConhecimento ")
                .append("              ,conh.id as idConhecimento ")
                .append("              ,conh.blIndicadorEdi as blIndicadorEdi ")
                .append("              ,fo.id as idFilialOrigem ")
                .append("              ,clieRemet.blUtilizaPesoEDI as blUtilizaPesoEDIRemet ")
                .append("              ,conh.tpSituacaoConhecimento as tpSituacaoConhecimento ")
                .append("              ,vnf.psAferidoSorter as psAferidoSorter ")
                .append("              ,conh.tpConhecimento as tpConhecimento ")
                .append("              ,conh.vlTotalSorter as vlTotalSorter ")
                .append("              ,conh.tpCalculoPreco as tpCalculoPreco ")
                .append("               ) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" join vnf.notaFiscalConhecimento nfc ")
                .append(" join nfc.conhecimento conh ")
                .append(" join conh.filialOrigem fo ")
                .append(" left outer join conh.clienteByIdClienteRemetente clieRemet ")
                .append(" where vnf.nrVolumeEmbarque = ? ")
                .append(" and vnf.tpVolume in ('M', 'U') ")
                .append(" and vnf.psAferidoSorter is null ");
        return (Map) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{nrVolumeEmbarque});
    }

    public Map findVolumeBynrVolumeEmbarque(String nrVolumeEmbarque) {
        StringBuilder query = new StringBuilder()
                .append("select new map(vnf.id as idVolumeNotaFiscal, vnf.psAferido as psAferido, c.tpSituacaoConhecimento as tpSituacaoConhecimento) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" join vnf.notaFiscalConhecimento nfc ")
                .append(" join nfc.conhecimento c ")
                .append(" where vnf.nrVolumeEmbarque = ? ")
                .append(" and vnf.tpVolume in ('M', 'U') ");
        return (Map) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{nrVolumeEmbarque});
    }


    public Map<String, Object> findVolumeNotaFiscalSorter(final String nrVolumeEmbarque) {
        StringBuilder query = new StringBuilder()
                .append("select new map(vnf.id as idVolumeNotaFiscal ")
                .append("              ,vnf.nrConhecimento as nrConhecimento ")
                .append("              ,conh.id as idConhecimento ")
                .append("              ,conh.blIndicadorEdi as blIndicadorEdi ")
                .append("              ,vnf.tpVolume as tpVolume ")
                .append("              ,fo.id as idFilialOrigem ")
                .append("              ,clieRemet.blUtilizaPesoEDI as blUtilizaPesoEDIRemet ")
                .append("              ,conh.tpSituacaoConhecimento as tpSituacaoConhecimento ")
                .append("              ,vnf.psAferido as psAferido ")
                .append("              ,vnf.psAferidoSorter as psAferidoSorter ")
                .append("              ,conh.tpConhecimento as tpConhecimento ")
                .append("              ,conh.vlTotalSorter as vlTotalSorter ")
                .append("              ,conh.tpCalculoPreco as tpCalculoPreco ")
                .append("               ) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append(" join vnf.notaFiscalConhecimento nfc ")
                .append(" join nfc.conhecimento conh ")
                .append(" join conh.filialOrigem fo ")
                .append(" left outer join conh.clienteByIdClienteRemetente clieRemet ")
                .append(" where vnf.nrVolumeEmbarque = ? ")
                .append(" and vnf.tpVolume in ('M', 'U') ");

        return (Map) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{nrVolumeEmbarque});
    }

    public void updateNrConhecimentoByPreCT(final Long nrConhecimento, final Long idDoctoServico) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String hql = "UPDATE " + getPersistentClass().getName() + " vnf \n" +
                        "SET vnf.nrConhecimento = :nrConhecimento \n" +
                        "WHERE vnf.notaFiscalConhecimento IN (SELECT nfc " +
                        " FROM NotaFiscalConhecimento nfc " +
                        " join nfc.conhecimento conh " +
                        " WHERE conh.idDoctoServico = :idDoctoServico" +
                        " and conh.tpSituacaoConhecimento = 'P')" +
                        " and vnf.tpVolume in ('M', 'U') ";
                query = session.createQuery(hql);
                query.setLong("nrConhecimento", nrConhecimento);
                query.setParameter("idDoctoServico", idDoctoServico);
                query.executeUpdate();

                return null;
            }
        });

        this.getHibernateTemplate().flush();
    }


    public List findVolumesNaoChecadosPorNrPreCTRC(Long nrPreConhecimento, Long idFilialOrigem, boolean isVolumeGMDireto) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select vnf.id ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append("	join vnf.notaFiscalConhecimento as nfc ")
                .append("	join nfc.conhecimento as conh ")
                .append(" where ")
                .append(" conh.tpSituacaoConhecimento = :tpSituacaoConhecimento ")
                .append(" and vnf.tpVolume in ('M', 'U') ")
                .append(" and conh.nrConhecimento = :nrPreConhecimento ");

        Map<String, Object> criteria = new HashMap<String, Object>();
        if (isVolumeGMDireto) {
            criteria.put("tpSituacaoConhecimento", "E");
        } else {
            criteria.put("tpSituacaoConhecimento", "P");
            sql.append(" and vnf.psAferido is null ");
            sql.append(" and conh.filialOrigem.idFilial = :idFilialOrigem ");
            criteria.put("idFilialOrigem", idFilialOrigem);
        }
        criteria.put("nrPreConhecimento", nrPreConhecimento);

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }

    /**
     * @param nrPreConhecimento
     * @param idFilialOrigem
     * @return
     * @see {@link #verifyVolumesPendentesWithLock}
     */
    public Boolean verifyVolumesPendentes(final Long nrPreConhecimento, final Long idFilialOrigem) {
        final StringBuffer sql = new StringBuffer("");
        sql.append("select count(vnf.id) ");
        sql.append(" from VolumeNotaFiscal as vnf ");
        sql.append("	join vnf.notaFiscalConhecimento as nfc ");
        sql.append("	join nfc.conhecimento as conh ");
        sql.append(" where ");
        sql.append(" conh.tpSituacaoConhecimento = :tpSituacaoConhecimento ");
        sql.append(" and vnf.tpVolume in ('M', 'U') ");
        sql.append(" and conh.nrConhecimento = :nrPreConhecimento ");
        sql.append(" and vnf.psAferido is null ");
        sql.append(" and conh.filialOrigem.idFilial = :idFilialOrigem ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("tpSituacaoConhecimento", "P");
        criteria.put("idFilialOrigem", idFilialOrigem);
        criteria.put("nrPreConhecimento", nrPreConhecimento);

        final Long rowCount = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
        return LongUtils.hasValue(rowCount);
    }

    public Boolean verifyVolumesPendentesSorter(final Long nrPreConhecimento, final Long idFilialOrigem) {
        final StringBuffer sql = new StringBuffer("");
        sql.append("select count(vnf.id) ");
        sql.append(" from VolumeNotaFiscal as vnf ");
        sql.append("	join vnf.notaFiscalConhecimento as nfc ");
        sql.append("	join nfc.conhecimento as conh ");
        sql.append(" where ");
        sql.append(" vnf.tpVolume in ('M', 'U') ");
        sql.append(" and conh.nrConhecimento = :nrPreConhecimento ");
        sql.append(" and vnf.psAferidoSorter is null ");
        sql.append(" and conh.filialOrigem.idFilial = :idFilialOrigem ");

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("idFilialOrigem", idFilialOrigem);
        criteria.put("nrPreConhecimento", nrPreConhecimento);

        final Long rowCount = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
        return LongUtils.hasValue(rowCount);
    }

    /**
     * Retorna a quantidade de volumes que ja foram pesados referentes a pré-conhecimentos que já reservaram nro de conhecimento porem o calculo do frete ainda nao foi finalizado.
     * A busca é feita para todos os conhecimentos da frota especificada.
     *
     * @param idMonitoramentoDescarga
     * @return
     */
    public Long countVolumesDeCTRCsEmCalculoFretePorMonitoramentoDescarga(Long idMonitoramentoDescarga) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select count(distinct conh.id) ")
                .append(" from VolumeNotaFiscal as vnf ")
                .append("	join vnf.notaFiscalConhecimento as nfc ")
                .append("	join nfc.conhecimento as conh ")
                .append("	join vnf.monitoramentoDescarga as md ")
                .append(" where ")
                .append(" conh.tpSituacaoConhecimento = 'P' ")
                .append(" and (conh.blPesoAferido = 'N' OR conh.blPesoAferido is null) ")
                .append(" and conh.nrConhecimento <= 0 ")
                .append(" and vnf.nrConhecimento is not null ")
                .append(" and vnf.tpVolume in ('M', 'U') ")
                .append(" and not exists ( ")
                .append("              select 1 ")
                .append("                from VolumeNotaFiscal as vnf1 ")
                .append("               where vnf1.nrConhecimento = vnf.nrConhecimento ")
                .append("                 and vnf1.psAferido is null ")
                .append(" 				  and vnf1.tpVolume in ('M', 'U') ")
                .append(" ) ")
                .append(" and md.id = ? ");

        return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idMonitoramentoDescarga});
    }

    public Object[] findVolumesChecadosPorIdCTRC(Long idConhecimento, Long idFilial, boolean isVolumeGMDireto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(qtVolumesTotal) as qtVolumesTotal, ")
                .append("	   nvl(sum(nvl(qtVolumes,0)),0) as qtVolumes ")
                .append("  from ( ")
                .append("        select nfc.qt_Volumes as qtVolumesTotal")
                .append("              ,(select sum(vnf.qt_volumes) from volume_nota_fiscal vnf where ")
                .append("              vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ")
                .append("              and vnf.tp_Volume in ('D', 'U')")
                .append("              and vnf.ps_Aferido is not null) as qtVolumes ")
                .append("        FROM ")
                .append("           conhecimento conh ")
                .append("           inner join docto_servico ds on (ds.id_docto_servico = conh.id_conhecimento) ")
                .append("           inner join nota_fiscal_conhecimento nfc on (nfc.id_conhecimento = conh.id_conhecimento) ")
                .append("           inner join filial flo on (flo.id_filial = conh.id_filial_origem) ")
                .append("        where ")
                .append("              conh.tp_Situacao_Conhecimento = :tpSituacaoConhecimento ")
                .append("              and conh.id_Conhecimento = :idConhecimento ");
        if (!isVolumeGMDireto) {
            sql.append("              and flo.id_Filial = :idFilial ");
        }
        sql.append(')');

        SQLQuery query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addScalar("qtVolumesTotal", Hibernate.LONG);
        query.addScalar("qtVolumes", Hibernate.LONG);
        query.setLong("idConhecimento", idConhecimento);
        if (isVolumeGMDireto) {
            query.setString("tpSituacaoConhecimento", "E");
        } else {
            query.setLong("idFilial", idFilial);
            query.setString("tpSituacaoConhecimento", "P");
        }
        return (Object[]) query.uniqueResult();
    }

    /**
     * Busca Total de Volumes Pesados do CTRC
     *
     * @param idConhecimento
     * @param idFilial
     * @return
     */
    public BigDecimal findTotalVolumesCTRC(final Long idConhecimento, final Long idFilial) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT SUM(vnf.ps_Aferido) AS psVolumesTotal ");
        sql.append(" FROM ");
        sql.append(" 	conhecimento c ");
        sql.append("	INNER JOIN docto_servico ds ");
        sql.append("		ON (ds.id_docto_servico = c.id_conhecimento) ");
        sql.append("    INNER JOIN nota_fiscal_conhecimento nfc ");
        sql.append("		ON (nfc.id_conhecimento = c.id_conhecimento) ");
        sql.append("    INNER JOIN volume_nota_fiscal vnf ");
        sql.append("		ON (vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento) ");
        sql.append("    INNER JOIN filial flo ");
        sql.append("		ON (flo.id_filial = c.id_filial_origem) ");
        sql.append(" WHERE ");
        sql.append("    c.tp_Situacao_Conhecimento = 'P' ");
        sql.append("    AND vnf.tp_Volume in ('D', 'U') ");
        sql.append("    AND vnf.ps_Aferido IS NOT NULL ");
        sql.append("    AND c.id_Conhecimento = :idConhecimento ");
        sql.append("    AND flo.id_Filial = :idFilial");

        SQLQuery query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addScalar("psVolumesTotal", Hibernate.BIG_DECIMAL);
        query.setLong("idConhecimento", idConhecimento);
        query.setLong("idFilial", idFilial);
        return (BigDecimal) query.uniqueResult();
    }

    public void updateVolumesByMonitDescargasComCTRCEmitido(final Long idFilial, final DateTime dhTmpEmi) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String hqlDelete = " UPDATE " + getPersistentClass().getName() + " vnf " +
                        " SET vnf.monitoramentoDescarga = NULL \n" +
                        " where vnf.monitoramentoDescarga.id in (Select md.id from MonitoramentoDescarga md " +
                        "              where md.filial.id = :idFilial " +
                        "                and md.tpSituacaoDescarga = 'S' " +
                        "                and md.dhEmissaoCTRC.value <= :dhTmpEmi)";
                query = session.createQuery(hqlDelete);
                query.setLong("idFilial", idFilial);
                query.setParameter("dhTmpEmi", new Timestamp(dhTmpEmi.getMillis()));

                query.executeUpdate();

                return null;
            }
        });
    }

    public void updateVolumesByMonitDescargasAntigas(final Long idFilial, final DateTime dhTmpExc) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String hqlDelete = " UPDATE " + getPersistentClass().getName() + " vnf " +
                        " SET vnf.monitoramentoDescarga = NULL \n" +
                        " where vnf.monitoramentoDescarga.id in (Select md.id from MonitoramentoDescarga md " +
                        "              where md.filial.id = :idFilial" +
                        "                and md.dhChegadaVeiculo.value <= :dhTmpExc)";
                query = session.createQuery(hqlDelete);
                query.setLong("idFilial", idFilial);
                query.setParameter("dhTmpExc", new Timestamp(dhTmpExc.getMillis()));

                query.executeUpdate();

                return null;
            }
        });
    }

    public Long findTotalVolumesNaoAferidos(Long idMonitoramentoDescarga, boolean isVolumeGMDireto) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select count(*) ");
        sql.append("from VolumeNotaFiscal vonf, ");
        sql.append(" MonitoramentoDescarga modes ");
        sql.append("where vonf.monitoramentoDescarga.idMonitoramentoDescarga = modes.idMonitoramentoDescarga ");
        sql.append(" and modes.idMonitoramentoDescarga = :idMonitoramentoDescarga ");
        if (isVolumeGMDireto) {
            sql.append(" and vonf.nrVolumeEmbarque is null ");
        } else {
            sql.append(" and vonf.psAferido is null ");
        }
        sql.append(" and vonf.tpVolume in ('M', 'U') ");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idMonitoramentoDescarga", idMonitoramentoDescarga);

        return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), param);
    }

    public List<VolumeNotaFiscal> findVolumesNotaFiscalByIdMonitoramento(Long idMonitoramentoDescarga) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select vonf ");
        sql.append("from VolumeNotaFiscal vonf, ");
        sql.append(" MonitoramentoDescarga mdes ");
        sql.append("where vonf.monitoramentoDescarga.idMonitoramentoDescarga = mdes.idMonitoramentoDescarga ");
        //sql.append(" and mdes.idMonitoramentoDescarga = :idMonitoramentoDescarga ");

        sql.append(" and mdes.idMonitoramentoDescarga = ? ");

        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idMonitoramentoDescarga});
    }

    public BigDecimal findTotalDimensaoVolumes(Long idConhecimento) {

        StringBuilder sql = new StringBuilder()

                .append(" select ")
                .append(" coalesce(sum (   				  ")
                .append(" coalesce(vol.nrDimensao1Cm,0) * ")
                .append(" coalesce(vol.nrDimensao2Cm,0) * ")
                .append(" coalesce(vol.nrDimensao3Cm,0)   ")
                .append(" ) ,0) as valor			 	  ")
                .append(" from  ")
                .append(" Conhecimento c ")
                .append(" join c.notaFiscalConhecimentos nf ")
                .append(" join nf.volumeNotaFiscais vol ")
                .append(" where c.id = ? ")
                .append(" and vol.nrDimensao1Cm is not null ");

        return BigDecimalUtils.getBigDecimal(getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idConhecimento}));
    }

    public BigDecimal findTotalCubagemVolumes(Long idConhecimento) {
        StringBuilder sql = new StringBuilder()
                .append(" select coalesce(sum(vol.nrCubagemM3), 0) as valor ")
                .append(" from Conhecimento c ")
                .append(" join c.notaFiscalConhecimentos nf ")
                .append(" join nf.volumeNotaFiscais vol ")
                .append(" where c.id = ? ")
                .append(" and vol.nrCubagemM3 is not null ");
        return BigDecimalUtils.getBigDecimal(getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idConhecimento}));
    }


    public List<VolumeNotaFiscal> findByIdControleCargaTpManifestoAndCdLocalizacao(Long idControleCarga, String tpManifesto, List<Short> cdsLocalizacaoMercadoria) {

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN);
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN);
        dc.createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.createAlias("localizacaoMercadoria", "localizacao");

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("manifesto.tpManifesto", tpManifesto));
        dc.add(Restrictions.in("localizacao.cdLocalizacaoMercadoria", cdsLocalizacaoMercadoria));

        return super.findByDetachedCriteria(dc);
    }

    /**
     * Retorna uma lista de mapas com as informações dos Volumes.
     *
     * @param idControleCarga
     * @return
     */
    public List<Map<String, Object>> findVolumeByIdControleCarga(Long idControleCarga) {
        StringBuffer sql = new StringBuffer()
                .append("select new map(vnf.idVolumeNotaFiscal as idVolumeNotaFiscal, ")
                .append("m.tpManifesto as tpManifesto, ")
                .append("m.tpStatusManifesto as tpStatusManifesto ")
                .append(")")
                .append(" from ")
                .append(VolumeNotaFiscal.class.getName()).append(" as vnf ")
                .append("inner join vnf.preManifestoVolumes pmv ")
                .append("inner join pmv.manifesto m ")
                .append("where ")
                .append("m.controleCarga.id = ? ");

        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    }

    public Integer countVolumesCarregadosNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.preManifestoVolumes pmv ");
        hql.append(" join pmv.manifesto m ");
        hql.append(" join pmv.doctoServico d ");
        hql.append(" join m.controleCarga c ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");
        hql.append(" and vnf.dispositivoUnitizacao is null ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(), new Object[]{idControleCarga, idDoctoServico});
    }

    public Integer countVolumesCarregadosTotal(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.preManifestoVolumes pmv ");
        hql.append(" join pmv.manifesto m ");
        hql.append(" join pmv.doctoServico d ");
        hql.append(" join m.controleCarga c ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(), new Object[]{idControleCarga, idDoctoServico});
    }

    public Integer countVolumesDescarregadosEntregaNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.localizacaoMercadoria l ");
        hql.append(" join vnf.manifestoEntregaVolumes mev ");
        hql.append(" join mev.manifestoEntrega me ");
        hql.append(" join me.manifesto m ");
        hql.append(" join m.controleCarga c ");
        hql.append(" join vnf.notaFiscalConhecimento nfc ");
        hql.append(" join nfc.conhecimento d ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");
        hql.append(" and l.cdLocalizacaoMercadoria = ? ");
        hql.append(" and vnf.dispositivoUnitizacao is null ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(),
                new Object[]{idControleCarga, idDoctoServico, Short.parseShort("35")});
    }

    public Integer countVolumesDescarregadosViagemNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.localizacaoMercadoria l ");
        hql.append(" join vnf.manifestoNacionalVolumes mnv ");
        hql.append(" join mnv.manifestoViagemNacional mvn ");
        hql.append(" join mvn.manifesto m ");
        hql.append(" join m.controleCarga c ");
        hql.append(" join vnf.notaFiscalConhecimento nfc ");
        hql.append(" join nfc.conhecimento d ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");
        hql.append(" and l.cdLocalizacaoMercadoria = ? ");
        hql.append(" and vnf.dispositivoUnitizacao is null ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(),
                new Object[]{idControleCarga, idDoctoServico, Short.valueOf("24")});
    }

    public Integer countVolumesDescarregadosEntregaTotal(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.localizacaoMercadoria l ");
        hql.append(" join vnf.manifestoEntregaVolumes mev ");
        hql.append(" join mev.manifestoEntrega me ");
        hql.append(" join me.manifesto m ");
        hql.append(" join m.controleCarga c ");
        hql.append(" join vnf.notaFiscalConhecimento nfc ");
        hql.append(" join nfc.conhecimento d ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");
        hql.append(" and l.cdLocalizacaoMercadoria = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(),
                new Object[]{idControleCarga, idDoctoServico, Short.parseShort("35")});
    }

    public Integer countVolumesDescarregadosViagemTotal(Long idControleCarga, Long idDoctoServico) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" vnf ");
        hql.append(" join vnf.localizacaoMercadoria l ");
        hql.append(" join vnf.manifestoNacionalVolumes mnv ");
        hql.append(" join mnv.manifestoViagemNacional mvn ");
        hql.append(" join mvn.manifesto m ");
        hql.append(" join m.controleCarga c ");
        hql.append(" join vnf.notaFiscalConhecimento nfc ");
        hql.append(" join nfc.conhecimento d ");
        hql.append(" where c.idControleCarga = ? ");
        hql.append(" and d.idDoctoServico = ? ");
        hql.append(" and l.cdLocalizacaoMercadoria = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(
                hql.toString(),
                new Object[]{idControleCarga, idDoctoServico, Short.parseShort("24")});
    }

    public Integer countVolumesCarregamentoDescarga(Long idControleCarga, Long idDoctoServico, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao) {
        return countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, tpOperacao, dispositivoUnitizacao, Boolean.TRUE);
    }

    public Integer countVolumesCarregamentoDescargaDispositivo(Long idControleCarga, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao) {
        return countVolumesCarregamentoDescarga(idControleCarga, null, tpOperacao, dispositivoUnitizacao, Boolean.TRUE);
    }

    public Integer countVolumesCarregamentoDescargaConhecimento(Long idControleCarga, Long idDoctoServico, String tpOperacao) {
        return countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, tpOperacao, null, Boolean.FALSE);
    }

    public Integer countVolumesCarregamentoDescarga(Long idControleCarga, Long idDoctoServico, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao, Boolean isConhecimento) {

        List<Object> lista = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select sum(cdv.qtVolumes) from ");

        hql.append(CarregamentoDescargaVolume.class.getName()).append(" cdv ");
        hql.append(" where ");
        hql.append(" cdv.carregamentoDescarga.tpOperacao = ? ");
        lista.add(tpOperacao);
        hql.append(" AND cdv.carregamentoDescarga.controleCarga.idControleCarga = ? ");
        lista.add(idControleCarga);
        if (idDoctoServico != null) {
            hql.append(" AND cdv.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico = ? ");
            lista.add(idDoctoServico);
        }
        if ("C".equals(tpOperacao)) {
            hql.append(" AND cdv.carregamentoDescarga.tpStatusOperacao <> 'C'");
        } else {
            hql.append(" AND cdv.carregamentoDescarga.tpStatusOperacao in ('I', 'E') ");
            hql.append(" AND cdv.carregamentoDescarga.filial.idFilial = ? ");
            lista.add(SessionUtils.getFilialSessao().getIdFilial());
        }

        if (isConhecimento) {
            if (dispositivoUnitizacao != null) {
                hql.append(" AND cdv.dispositivoUnitizacao.idDispositivoUnitizacao = ? ");
                lista.add(dispositivoUnitizacao.getIdDispositivoUnitizacao());
            } else {
                hql.append(" AND cdv.dispositivoUnitizacao is null ");
            }
        }

        Long total = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), lista.toArray());
        if (total != null) {
            return Integer.valueOf(total.toString());
        }
        return 0;
    }
    public Set<QtdVolumesConhecimentoDto> findQtdVolumesPorIdsConhecimento(Long idControleCarga, Set<Long> idsDoctoServico, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao, boolean isConhecimento) {

        List<Object> lista = new ArrayList<>();

        StringBuffer hql = new StringBuffer();
        hql.append("select new com.mercurio.lms.expedicao.dto.QtdVolumesConhecimentoDto( ");
        hql.append(" cdv.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico,  ");
        hql.append(" sum(cdv.qtVolumes) ) from ");
        hql.append(CarregamentoDescargaVolume.class.getName()).append(" cdv ");
        hql.append(" where ");
        hql.append(" cdv.carregamentoDescarga.tpOperacao = ? ");
        lista.add(tpOperacao);
        hql.append(" AND cdv.carregamentoDescarga.controleCarga.idControleCarga = ? ");
        lista.add(idControleCarga);
        if (idsDoctoServico != null && !idsDoctoServico.isEmpty()) {
            String params = idsDoctoServico.stream().map(d -> "?").collect(Collectors.joining(","));
            hql.append(" AND cdv.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico IN ("+params+") ");
            lista.addAll(idsDoctoServico);
        }
        if ("C".equals(tpOperacao)) {
            hql.append(" AND cdv.carregamentoDescarga.tpStatusOperacao <> 'C'");
        } else {
            hql.append(" AND cdv.carregamentoDescarga.tpStatusOperacao in ('I', 'E') ");
            hql.append(" AND cdv.carregamentoDescarga.filial.idFilial = ? ");
            lista.add(SessionUtils.getFilialSessao().getIdFilial());
        }

        if (isConhecimento) {
            if (dispositivoUnitizacao != null) {
                hql.append(" AND cdv.dispositivoUnitizacao.idDispositivoUnitizacao = ? ");
                lista.add(dispositivoUnitizacao.getIdDispositivoUnitizacao());
            } else {
                hql.append(" AND cdv.dispositivoUnitizacao is null ");
            }
        }

        hql.append(" GROUP BY cdv.volumeNotaFiscal.notaFiscalConhecimento.conhecimento.idDoctoServico ");
        Object[] obs = lista.toArray();
        List<QtdVolumesConhecimentoDto> result = getAdsmHibernateTemplate().find(hql.toString(), obs);


        return new HashSet<>(result);
    }

    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacaoPai
     * @return lista de volumes
     */
    @SuppressWarnings("unchecked")
    public List<VolumeNotaFiscal> findVolumesNaoUnitizadosControleCarga(Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento.doctoServicoOriginal", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN)
                .createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN)
                .createAlias("manifesto.controleCarga", "controleCarga");
        dc.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));
        dc.add(Restrictions.isNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacaoPai
     * @return lista de volumes
     */
    @SuppressWarnings("unchecked")
    public List<VolumeNotaFiscal> findVolumesDispositivoUnitizacao(Long idControleCarga, Long idDispositivoUnitizacao) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento.doctoServicoOriginal", FetchMode.JOIN);
        dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN).createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN).createAlias("manifesto.controleCarga", "controleCarga");
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));

        return super.findByDetachedCriteria(dc);
    }

    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacaoPai
     * @return lista de volumes
     */
    @SuppressWarnings("unchecked")
    public List<VolumeNotaFiscal> findVolumesUnitizadosControleCarga(Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN)
                .createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN).createAlias("manifesto.controleCarga", "controleCarga");
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.isNotNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescargaControleCarga(Long idControleCarga, Boolean isViagem) {
        return findVolumesDescargaControleCarga(idControleCarga, null, isViagem, true);
    }

    public List<VolumeNotaFiscal> findVolumesDescargaControleCarga(Long idControleCarga, Long idDispositivoUnitizacao, Boolean isViagem) {
        return findVolumesDescargaControleCarga(idControleCarga, idDispositivoUnitizacao, isViagem, false);
    }

    private List<VolumeNotaFiscal> findVolumesDescargaControleCarga(Long idControleCarga, Long idDispositivoUnitizacao, Boolean isViagem, Boolean findDispositovo) {

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento.conhecimento.doctoServicoOriginal", FetchMode.JOIN);
        dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN).createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN).createAlias("manifesto.controleCarga", "controleCarga");

        if (isViagem) {
            dc.add(Restrictions.eq("manifesto.filialByIdFilialDestino.idFilial", SessionUtils.getFilialSessao().getIdFilial()));
        } else {
            dc.setFetchMode("manifestoEntregaVolumes", FetchMode.JOIN).createAlias("manifestoEntregaVolumes", "manifestoEntregaVolume");
            dc.createAlias("manifestoEntregaVolume.manifestoEntrega", "manifestoEntrega");
            dc.setFetchMode("manifestoEntregaVolume.ocorrenciaEntrega", FetchMode.JOIN).createAlias("manifestoEntregaVolume.ocorrenciaEntrega", "ocorrenciaEntrega");

            dc.add(Restrictions.and(Restrictions.ne("ocorrenciaEntrega.tpOcorrencia", "E"),
                    Restrictions.ne("ocorrenciaEntrega.tpOcorrencia", "A")));
            dc.add(Restrictions.eq("ocorrenciaEntrega.tpSituacao", "A"));
            dc.add(Restrictions.eqProperty("manifesto.id", "manifestoEntrega.id"));
        }

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));

        if (findDispositovo) {
            dc.add(Restrictions.isNotNull("dispositivoUnitizacao"));
        } else {
            if (idDispositivoUnitizacao != null) {
                dc.add(Restrictions.eq("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
            } else {
                dc.add(Restrictions.isNull("dispositivoUnitizacao"));
            }
        }

        return super.findByDetachedCriteria(dc);
    }

    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacaoPai
     * @return lista de volumes
     */
    @SuppressWarnings("unchecked")
    public List<VolumeNotaFiscal> findVolumesNaoUnitizadosControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.doctoServico", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto.controleCarga", FetchMode.JOIN);
        dc.createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.createAlias("preManifestoVolumes.doctoServico", "doctoServico");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("doctoServico.id", idDoctoServico));
        dc.add(Restrictions.isNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    @SuppressWarnings("unchecked")
    public List<Map> findVolumeNotaFiscalSemDoctoServicoNaFilial(Long idDoctoServico, Long idFilialLogada, List<Long> idVolumeNotaFiscalList,
                                                                 List<String> cdLocalizacaoMercadoriaList, String bloqFluxoSubcontratacao) {

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");
        sql.append("   volumenota0_.ID_VOLUME_NOTA_FISCAL       AS idVolumeNotaFiscal, ");
        sql.append("   volumenota0_.NR_SEQUENCIA                AS nrSequencia, ");
        sql.append("   conhecimen3_1_.QT_VOLUMES                AS qtVolumes, ");
        sql.append("   conhecimen3_.ID_CONHECIMENTO             AS idDoctoServico, ");
        sql.append("   filial4_.SG_FILIAL                       AS sgFilialOrigem, ");
        sql.append("   conhecimen3_1_.TP_DOCUMENTO_SERVICO      AS tpDocumentoServico, ");
        sql.append("   conhecimen3_.NR_CONHECIMENTO             AS nrDoctoServico, ");
        sql.append("   conhecimen3_.DV_CONHECIMENTO             AS dvConhecimento, ");
        sql.append("   localizaca2_.ID_LOCALIZACAO_MERCADORIA   AS idLocalizacaoMercadoria, ");
        sql.append("   VI18N(localizaca2_.DS_LOCALIZACAO_MERCADORIA_I) as dsLocalizacaoMercadoria ");
        sql.append(" FROM VOLUME_NOTA_FISCAL volumenota0_, ");
        sql.append("   NOTA_FISCAL_CONHECIMENTO notafiscal1_, ");
        sql.append("   CONHECIMENTO conhecimen3_, ");
        sql.append("   DOCTO_SERVICO conhecimen3_1_, ");
        sql.append("   FILIAL filial4_, ");
        sql.append("   LOCALIZACAO_MERCADORIA localizaca2_ ");
        sql.append(" WHERE volumenota0_.ID_NOTA_FISCAL_CONHECIMENTO     =notafiscal1_.ID_NOTA_FISCAL_CONHECIMENTO ");
        sql.append(" AND notafiscal1_.ID_CONHECIMENTO                   =conhecimen3_.ID_CONHECIMENTO ");
        sql.append(" AND conhecimen3_.ID_CONHECIMENTO                   =conhecimen3_1_.ID_DOCTO_SERVICO ");
        sql.append(" AND conhecimen3_.ID_FILIAL_ORIGEM                  =filial4_.ID_FILIAL ");
        sql.append(" AND volumenota0_.ID_LOCALIZACAO_MERCADORIA         =localizaca2_.ID_LOCALIZACAO_MERCADORIA ");
        sql.append(" AND conhecimen3_1_.BL_BLOQUEADO                    = 'N' ");

        if (idDoctoServico != null) {
            sql.append(" AND conhecimen3_1_.ID_DOCTO_SERVICO 				= " + idDoctoServico + " ");
        }

        if (idFilialLogada != null) {
            sql.append(" AND volumenota0_.ID_LOCALIZACAO_FILIAL             = " + idFilialLogada + " ");
            sql.append(" AND conhecimen3_1_.ID_FILIAL_LOCALIZACAO          <> " + idFilialLogada + " ");
        }

        if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)){
            sql.append(" AND (conhecimen3_1_.BL_FLUXO_SUBCONTRATACAO = 'N' or conhecimen3_1_.BL_FLUXO_SUBCONTRATACAO is null) ");
        }
      
        if (!cdLocalizacaoMercadoriaList.isEmpty()) {
            sql.append(" AND (localizaca2_.CD_LOCALIZACAO_MERCADORIA       IN " + SQLUtils.mountNumberForInExpression(cdLocalizacaoMercadoriaList) + ") ");
        }

        if (!idVolumeNotaFiscalList.isEmpty()) {
            sql.append(" AND (volumenota0_.ID_VOLUME_NOTA_FISCAL    NOT IN " + SQLUtils.mountNumberForInExpression(idVolumeNotaFiscalList) + ") ");
        }

        //ultimos 30 dias
        sql.append(" AND TRUNC(CAST(conhecimen3_1_.DH_EMISSAO AS DATE))>= trunc(sysdate - 30) ");
        sql.append(" AND TRUNC(CAST(conhecimen3_1_.DH_EMISSAO AS DATE))<= trunc(sysdate + 1) ");

        RowMapper rowMapper = new RowMapper() {
            @Override
            public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("idVolumeNotaFiscal", resultSet.getLong(1));
                map.put("nrSequencia", resultSet.getInt(2));
                map.put("qtVolumes", resultSet.getInt(3));
                map.put("idDoctoServico", resultSet.getLong(4));
                map.put("sgFilialOrigem", resultSet.getString(5));
                map.put("tpDocumentoServico", new DomainValue(resultSet.getString(6)));
                map.put("nrDoctoServico", resultSet.getLong(7));
                map.put("dvConhecimento", resultSet.getInt(8));
                map.put("idLocalizacaoMercadoria", resultSet.getLong(9));
                map.put("dsLocalizacaoMercadoria", resultSet.getString(10));
                return map;
            }
        };

        return jdbcTemplate.query(sql.toString(), rowMapper);
    }


    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacaoPai
     * @return lista de volumes
     */
    public List<VolumeNotaFiscal> findVolumesByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
        DetachedCriteria dc = DetachedCriteria.forClass(VolumeNotaFiscal.class)
                .setFetchMode("dispositivoUnitizacao", FetchMode.JOIN)
                .setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
                .setFetchMode("localizacaoFilial", FetchMode.JOIN)
                .add(Restrictions.eq("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao));
        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public VolumeNotaFiscal findVolumeById(Long idVolume) {
        DetachedCriteria dc = DetachedCriteria.forClass(VolumeNotaFiscal.class)
                .setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
                .setFetchMode("localizacaoFilial", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteRemetente", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteDestinatario", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.clienteByIdClienteRemetente.pessoa", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento.conhecimento.filialOrigem", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao", FetchMode.JOIN)
                .setFetchMode("dispositivoUnitizacao.tipoDispositivoUnitizacao", FetchMode.JOIN)
                .add(Restrictions.eq("idVolumeNotaFiscal", idVolume));

        return (VolumeNotaFiscal) getAdsmHibernateTemplate().findUniqueResult(dc);
    }


    public List<VolumeNotaFiscal> findByControleCargaTpManifestoCdLocalizacaoDoctoServico(Long idControleCarga, Long idDoctoServico, String tpManifesto, Short cdLocalizacaoMercadoria) {

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.manifesto", FetchMode.JOIN);
        dc.setFetchMode("preManifestoVolumes.doctoServico", FetchMode.JOIN);
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN);
        dc.createAlias("preManifestoVolumes.manifesto", "manifesto");
        dc.createAlias("preManifestoVolumes.doctoServico", "doctoServico");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.createAlias("localizacaoMercadoria", "localizacao");

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("manifesto.tpManifesto", tpManifesto));
        dc.add(Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", cdLocalizacaoMercadoria));
        dc.add(Restrictions.isNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntregaVolumes", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
        dc.setFetchMode("manifesto", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("docto", FetchMode.JOIN);
        dc.setFetchMode("controleCarga", FetchMode.JOIN);
        dc.createAlias("manifestoEntregaVolumes.manifestoEntrega", "manifestoEntrega");
        dc.createAlias("manifestoEntrega.manifesto", "manifesto");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.createAlias("notaFiscalConhecimento.conhecimento", "docto");
        dc.createAlias("localizacaoMercadoria", "localizacao");

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("docto.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("35")));
        dc.add(Restrictions.isNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescarregadosEntrega(Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntregaVolumes", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
        dc.setFetchMode("manifesto", FetchMode.JOIN);
        dc.setFetchMode("controleCarga", FetchMode.JOIN);
        dc.createAlias("manifestoEntregaVolumes.manifestoEntrega", "manifestoEntrega");
        dc.createAlias("manifestoEntrega.manifesto", "manifesto");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.createAlias("localizacaoMercadoria", "localizacao");
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("35")));
        dc.add(Restrictions.isNotNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesTotalDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntregaVolumes", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
        dc.setFetchMode("manifesto", FetchMode.JOIN);
        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("docto", FetchMode.JOIN);
        dc.setFetchMode("controleCarga", FetchMode.JOIN);
        dc.createAlias("manifestoEntregaVolumes.manifestoEntrega", "manifestoEntrega");
        dc.createAlias("manifestoEntrega.manifesto", "manifesto");
        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.createAlias("notaFiscalConhecimento.conhecimento", "docto");
        dc.createAlias("localizacaoMercadoria", "localizacao");

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("docto.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("35")));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());

        dc.createAlias("localizacaoMercadoria", "localizacao");
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);

        dc.setFetchMode("manifestoNacionalVolumes", FetchMode.JOIN);

        dc.createAlias("manifestoNacionalVolumes.manifestoViagemNacional", "manifestoViagemNacional");
        dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);

        dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
        dc.setFetchMode("manifesto", FetchMode.JOIN);

        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);

        dc.createAlias("notaFiscalConhecimento.conhecimento", "docto");
        dc.setFetchMode("docto", FetchMode.JOIN);

        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.setFetchMode("controleCarga", FetchMode.JOIN);

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("docto.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("24")));
        dc.add(Restrictions.isNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescarregadosViagem(Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
        dc.setFetchMode("manifestoNacionalVolumes", FetchMode.JOIN);
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
                .createAlias("localizacaoMercadoria", "localizacao");
        dc.setFetchMode("manifestoNacionalVolumes.manifestoViagemNacional", FetchMode.JOIN)
                .createAlias("manifestoNacionalVolumes.manifestoViagemNacional", "manifestoViagemNacional");
        dc.setFetchMode("manifestoViagemNacional.manifesto", FetchMode.JOIN)
                .createAlias("manifestoViagemNacional.manifesto", "manifesto");
        dc.setFetchMode("manifesto.controleCarga", FetchMode.JOIN)
                .createAlias("manifesto.controleCarga", "controleCarga");
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("24")));
        dc.add(Restrictions.isNotNull("dispositivoUnitizacao"));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesTotalDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());

        dc.createAlias("localizacaoMercadoria", "localizacao");
        dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);

        dc.setFetchMode("manifestoNacionalVolumes", FetchMode.JOIN);

        dc.createAlias("manifestoNacionalVolumes.manifestoViagemNacional", "manifestoViagemNacional");
        dc.setFetchMode("manifestoViagemNacional", FetchMode.JOIN);

        dc.createAlias("manifestoViagemNacional.manifesto", "manifesto");
        dc.setFetchMode("manifesto", FetchMode.JOIN);

        dc.setFetchMode("notaFiscalConhecimento", FetchMode.JOIN);

        dc.createAlias("notaFiscalConhecimento.conhecimento", "docto");
        dc.setFetchMode("docto", FetchMode.JOIN);

        dc.createAlias("manifesto.controleCarga", "controleCarga");
        dc.setFetchMode("controleCarga", FetchMode.JOIN);

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq("docto.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("localizacao.cdLocalizacaoMercadoria", Short.parseShort("24")));

        return super.findByDetachedCriteria(dc);
    }

    @SuppressWarnings("unchecked")
    public List<VolumeNotaFiscal> findVolumesEmDescargaByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial, DomainValue tpControleCarga) {
        ArrayList<Short> cdsLocalizacaoMercadoria = new ArrayList<Short>();

        if (tpControleCarga.getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM)) {
            cdsLocalizacaoMercadoria.add(Short.valueOf("34"));
        } else if (tpControleCarga.getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA)) {
            cdsLocalizacaoMercadoria.add(Short.valueOf("24"));
            cdsLocalizacaoMercadoria.add(Short.valueOf("28"));
            cdsLocalizacaoMercadoria.add(Short.valueOf("35"));
            cdsLocalizacaoMercadoria.add(Short.valueOf("36"));
            cdsLocalizacaoMercadoria.add(Short.valueOf("43"));
        }

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.createAlias("notaFiscalConhecimento", "nfc");
        dc.createAlias("nfc.conhecimento", "c");
        dc.createAlias("localizacaoFilial", "localizacaoFilial");
        dc.createAlias("localizacaoMercadoria", "localizacaoMercadoria");

        dc.add(Restrictions.eq("c.id", idDoctoServico));
        dc.add(Restrictions.eq("localizacaoFilial.id", idFilial));
        dc.add(Restrictions.in("localizacaoMercadoria.cdLocalizacaoMercadoria", cdsLocalizacaoMercadoria));

        return super.findByDetachedCriteria(dc);
    }

    public List<VolumeNotaFiscal> findVolumesByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial) {

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.createAlias("notaFiscalConhecimento", "nfc");
        dc.createAlias("nfc.conhecimento", "c");
        dc.createAlias("localizacaoFilial", "localizacaoFilial");
        dc.createAlias("localizacaoMercadoria", "localizacaoMercadoria");

        dc.add(Restrictions.eq("c.id", idDoctoServico));
        dc.add(Restrictions.eq("localizacaoFilial.id", idFilial));

        return super.findByDetachedCriteria(dc);
    }

    /**
     * @param idDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedVolumesByDoctoServico(Long idDoctoServico, FindDefinition findDefinition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select new map(c.tpDoctoServico as tpDoctoServico, ");
        sql.append("fo.sgFilial as sgFilialOrigem, ");
        sql.append("c.nrConhecimento as nrConhecimento, ");
        sql.append("c.dvConhecimento as dvConhecimento, ");
        sql.append("fd.sgFilial as sgFilialDestino, ");
        sql.append("nfc.nrNotaFiscal as nrNotaFiscal, ");
        sql.append("vnf.nrSequencia as nrSequencia, ");
        sql.append("du.nrIdentificacao as nrIdentificacaoDispositivoUnitizacao, ");
        sql.append("tdu.dsTipoDispositivoUnitizacao as dsTipoDispositivoUnitizacao, ");
        sql.append("lm.dsLocalizacaoMercadoria as dsLocalizacaoMercadoria, ");
        sql.append("lf.sgFilial as sgLocalizacaoFilial, ");
        sql.append("mz.dsMacroZona as dsMacroZona) ");
        sql.append("from " + VolumeNotaFiscal.class.getName() + " as vnf ");
        sql.append("inner join vnf.notaFiscalConhecimento as nfc ");
        sql.append("inner join nfc.conhecimento as c ");
        sql.append("inner join c.filialByIdFilialOrigem fo ");
        sql.append("inner join c.filialByIdFilialDestino fd ");
        sql.append("left join vnf.macroZona as mz ");
        sql.append("left join vnf.dispositivoUnitizacao as du ");
        sql.append("left join du.tipoDispositivoUnitizacao tdu ");
        sql.append("left join vnf.localizacaoMercadoria as lm ");
        sql.append("left join vnf.localizacaoFilial as lf ");
        sql.append("where c.id = ? ");

        List param = new ArrayList();
        param.add(idDoctoServico);

        return getAdsmHibernateTemplate().findPaginated(sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    public ResultSetPage<Map<String, Object>> findPaginatedVolumesByDoctoServicoAndManifestoEntrega(Long idDoctoServico, Long idManifestoEntrega, FindDefinition findDefinition) {
        StringBuilder sql = new StringBuilder();
        sql.append("select new map(c.tpDoctoServico as tpDoctoServico, ");
        sql.append("fo.sgFilial as sgFilialOrigem, ");
        sql.append("vnf.id as idVolumeNotaFiscal, ");
        sql.append("vnf.nrVolumeColeta as nrVolumeColeta, ");
        sql.append("vnf.nrVolumeEmbarque as nrVolumeEmbarque, ");
        sql.append("mev.id as idManifestoEntregaVolume, ");
        sql.append("c.nrConhecimento as nrConhecimento, ");
        sql.append("c.dvConhecimento as dvConhecimento, ");
        sql.append("fd.sgFilial as sgFilialDestino, ");
        sql.append("nfc.nrNotaFiscal as nrNotaFiscal, ");
        sql.append("vnf.nrSequencia as nrSequencia) ");

        sql.append(getFromByDoctoServicoAndManifestoEntrega());

        List param = new ArrayList();
        param.add(idDoctoServico);
        param.add(idManifestoEntrega);

        return getAdsmHibernateTemplate().findPaginated(sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    public Integer getRowCountVolumesByDoctoServicoAndManifestoEntrega(Long idDoctoServico, Long idManifestoEntrega) {
        StringBuffer sql = new StringBuffer();

        sql.append(getFromByDoctoServicoAndManifestoEntrega());

        List param = new ArrayList();
        param.add(idDoctoServico);
        param.add(idManifestoEntrega);

        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
    }

    private String getFromByDoctoServicoAndManifestoEntrega() {
        StringBuilder sql = new StringBuilder();
        sql.append("from " + VolumeNotaFiscal.class.getName() + " as vnf ");
        sql.append("inner join vnf.notaFiscalConhecimento as nfc ");
        sql.append("inner join nfc.conhecimento as c ");
        sql.append("inner join c.filialByIdFilialOrigem fo ");
        sql.append("inner join c.filialByIdFilialDestino fd ");
        sql.append("left join vnf.manifestoEntregaVolumes mev ");
        sql.append("left join mev.ocorrenciaEntrega oco ");
        sql.append("where c.id = ? ");
        sql.append("and oco = null ");
        sql.append("and mev.manifestoEntrega.id = ? ");
        sql.append("order by vnf.nrSequencia ");
        return sql.toString();
    }

    /**
     * @param idDoctoServico return
     */
    public Integer getRowCountVolumesByDoctoServico(Long idDoctoServico) {
        StringBuffer sql = new StringBuffer();
        sql.append("from " + VolumeNotaFiscal.class.getName() + " as vnf ");
        sql.append("inner join vnf.notaFiscalConhecimento as nfc ");
        sql.append("inner join nfc.conhecimento as c ");
        sql.append("inner join c.filialByIdFilialOrigem fo ");
        sql.append("inner join c.filialByIdFilialDestino fd ");
        sql.append("left join vnf.macroZona as mz ");
        sql.append("left join vnf.dispositivoUnitizacao as du ");
        sql.append("left join du.tipoDispositivoUnitizacao tdu ");
        sql.append("left join vnf.localizacaoMercadoria as lm ");
        sql.append("left join vnf.localizacaoFilial as lf ");
        sql.append("where c.id = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idDoctoServico});
    }


    /**
     * Retorna uma lista de ManifestoEntregaVolumes a
     *
     * @param idControleCarga
     * @return
     */
    public List<ManifestoEntregaVolume> findByControleCarga(Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        dc.setFetchMode("manifestoEntrega", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntrega.manifesto", FetchMode.JOIN);
        dc.setFetchMode("manifestoEntrega.manifesto.controleCarga", FetchMode.JOIN);
        dc.setFetchMode("volumeNotaFiscal", FetchMode.JOIN);
        dc.setFetchMode("volumeNotaFiscal.localizacao", FetchMode.JOIN);
        dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("volumeNotaFiscal.notaFiscalConhecimento.conhecimento", FetchMode.JOIN);
        dc.createAlias("manifestoEntrega.manifesto.controleCarga", "controleCarga");

        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));

        return super.findByDetachedCriteria(dc);
    }

    public ResultSetPage<VolumeNotaFiscal> findPaginated(PaginatedQuery paginatedQuery) {
        Map<String, Object> criteria = paginatedQuery.getCriteria();
        return getAdsmHibernateTemplate().findPaginated(paginatedQuery, this.getHqlPaginated(criteria));
    }

    @Override
    public Integer getRowCount(Map criteria) {
        return getAdsmHibernateTemplate().getRowCountForQuery(this.getHqlPaginated(criteria), criteria);
    }

    public String getHqlPaginated(Map<String, Object> criteria) {
        StringBuilder hql = new StringBuilder();
        hql.append("from " + VolumeNotaFiscal.class.getName() + " as volume ");
        hql.append("inner join fetch volume.notaFiscalConhecimento as nota ");
        hql.append("inner join fetch nota.conhecimento as conhecimento ");
        hql.append("inner join fetch conhecimento.filialOrigem as filialOrigemConhecimento ");
		/* Fetchs para obtenção de informações de Macro Zona */
        hql.append("left join fetch volume.macroZona as macroZona ");
        hql.append("left join fetch macroZona.terminal terminal ");
        hql.append("left join fetch terminal.filial filial ");
        hql.append("left join fetch terminal.pessoa pessoaTerminal ");
		/* Fetch para obtenção de informações de Localização de Mercadorias */
        hql.append("left join fetch volume.localizacaoMercadoria as localizacao ");
        hql.append("left join fetch volume.localizacaoFilial as filialLocalizacao ");
		/* Fetch para obtenção de informações de Dispositivo de Unitizacao */
        hql.append("left join fetch volume.dispositivoUnitizacao as dispositivoUnitizacao ");
        hql.append("left join fetch dispositivoUnitizacao.tipoDispositivoUnitizacao as tipoDispositivoUnitizacao ");
        hql.append("left join fetch dispositivoUnitizacao.localizacaoMercadoria as localizacaoDispositivo ");
        hql.append("left join fetch dispositivoUnitizacao.localizacaoFilial as filialLocalizacaoDispositivo ");
        hql.append("where 1=1 ");
        hql.append("and volume.tpVolume <> '" + ConstantesExpedicao.TP_VOLUME_MESTRE + "' ");


        List param = new ArrayList();
        if (criteria.get("idMacroZona") != null) {
            hql.append("and macroZona.id = :idMacroZona ");
            hql.append("and volume.dispositivoUnitizacao.id is null ");
        }
        if (criteria.get("idDispositivoUnitizacao") != null) {
            hql.append("and volume.dispositivoUnitizacao.id = :idDispositivoUnitizacao ");

        }
        if (criteria.get("idDoctoServico") != null) {
            hql.append("and conhecimento.id = :idDoctoServico ");
        }
        return hql.toString();
    }

    /**
     * Busca Volumes do Manifesto
     *
     * @param idManifesto
     * @param projection
     * @param alias
     * @param criterions
     * @return
     * @author André Valadas
     */
    public List<VolumeNotaFiscal> findByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions) {
        Set<VolumeNotaFiscal> result = new HashSet<VolumeNotaFiscal>();

		/* Busca em Pré Manifesto Volume */
        Criteria dc = getSession(false).createCriteria(getPersistentClass(), "vnf");
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        dc.setFetchMode("vnf.localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("vnf.notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("vnf.preManifestoVolumes", FetchMode.JOIN);
        dc.setFetchMode("vnf.preManifestoVolumes.manifesto", FetchMode.JOIN);
        dc.createAlias("vnf.preManifestoVolumes", "volumes");
        dc.createAlias("volumes.manifesto", "manifesto");
        dc.add(Restrictions.eq("manifesto.id", idManifesto));
        if (projection != null) {
            dc.setResultTransformer(new AliasToNestedBeanResultTransformer(VolumeNotaFiscal.class));
        }
        result.addAll(dc.list());

		/* Busca em Manifesto Entrega Volumes */
        dc = getSession(false).createCriteria(getPersistentClass(), "vnf");
        ;
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        dc.setFetchMode("vnf.localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("vnf.notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("vnf.manifestoEntregaVolumes", FetchMode.JOIN);
        dc.setFetchMode("vnf.manifestoEntregaVolumes.manifestoEntrega", FetchMode.JOIN);
        dc.createAlias("vnf.manifestoEntregaVolumes", "volumes");
        dc.createAlias("volumes.manifestoEntrega", "manifesto");
        dc.add(Restrictions.eq("manifesto.id", idManifesto));
        if (projection != null) {
            dc.setResultTransformer(new AliasToNestedBeanResultTransformer(VolumeNotaFiscal.class));
        }
        result.addAll(dc.list());

		/* Busca em Manifesto Nacional Volume */
        dc = getSession(false).createCriteria(getPersistentClass(), "vnf");

        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        dc.setFetchMode("vnf.localizacaoMercadoria", FetchMode.JOIN);
        dc.setFetchMode("vnf.notaFiscalConhecimento", FetchMode.JOIN);
        dc.setFetchMode("vnf.manifestoNacionalVolumes", FetchMode.JOIN);
        dc.setFetchMode("vnf.manifestoNacionalVolumes.manifestoViagemNacional", FetchMode.JOIN);
        dc.createAlias("vnf.manifestoNacionalVolumes", "volumes");
        dc.createAlias("volumes.manifestoViagemNacional", "manifesto");
        dc.add(Restrictions.eq("manifesto.id", idManifesto));
        if (projection != null) {
            dc.setResultTransformer(new AliasToNestedBeanResultTransformer(VolumeNotaFiscal.class));
        }
        result.addAll(dc.list());

        return new ArrayList<VolumeNotaFiscal>(result);
    }


    /**
     * Seta volumeNotaFiscal.psAferido para null em volumeNotaFiscal.tpVolume
     * igual a 'D' ou 'N' e com volumeNotaFiscal.idMonitoramentoDescarga igual
     * ao parametro
     *
     * @param idMonitoramentoDescarga
     * @param isVolumeGMDireto
     * @author lucianos
     */
    public void updatePsAferidoToNull(final Long idMonitoramentoDescarga, final boolean isVolumeGMDireto) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                StringBuffer hql = new StringBuffer();
                hql.append("UPDATE " + getPersistentClass().getName() + " vnf ")
                        .append("SET vnf.psAferido = null ")
                        .append("WHERE vnf.monitoramentoDescarga.id = :idMonitoramentoDescarga  ")
                        .append("AND vnf.tpVolume in ( 'D', 'U' ) ");

                if (isVolumeGMDireto) {
                    hql.append(" and vnf.nrVolumeEmbarque is null ");
                } else {
                    hql.append(" and vnf.psAferido is null ");
                }

                query = session.createQuery(hql.toString());
                query.setParameter("idMonitoramentoDescarga", idMonitoramentoDescarga);
                query.executeUpdate();

                return null;
            }
        });
    }

    /**
     * Retorna o somatório do volumeNotaFiscal.qtVolumes e a quantidade de
     * volumeNotaFiscal que estão relacionados com o monitoramentoDescarga
     *
     * @param idMonitoramentoDescarga
     * @param isVolumeGMDireto
     * @return
     * @author lucianos
     */
    public Object[] countQtdVolumesByIdMonitoramentoDescargaAndTpVolume(Long idMonitoramentoDescarga,
                                                                        boolean isVolumeGMDireto) {

        StringBuffer sql = new StringBuffer()
                .append("select sum(vnf.qtVolumes), count(*) ")
                .append("from " + VolumeNotaFiscal.class.getName() + " as vnf ")
                .append("join vnf.monitoramentoDescarga md ")
                .append("where md.idMonitoramentoDescarga = ? ")
                .append(" and  vnf.tpVolume in ( 'D', 'U' ) ");

        if (isVolumeGMDireto) {
            sql.append(" and vnf.nrVolumeEmbarque is null ");
        } else {
            sql.append(" and vnf.psAferido is null ");
        }

        sql.append("group by md.idMonitoramentoDescarga ");

        return (Object[]) getAdsmHibernateTemplate().findUniqueResult(sql.toString(),
                new Object[]{idMonitoramentoDescarga});

    }


    private void configProjection(final Criteria dc, final Projection projection) {
        if (projection != null) {
            dc.setProjection(projection);
        }
    }

    private void configAlias(final Criteria dc, final Map<String, String> alias) {
        configAlias(dc, alias, -1);
    }

    private void configAlias(final Criteria dc, final Map<String, String> alias, final int joinType) {
        if (alias == null) return;

        final Set<String> keySet = alias.keySet();
        for (final String key : keySet) {
            if (joinType > -1) {
                dc.createAlias(key, alias.get(key), joinType);
            } else dc.createAlias(key, alias.get(key));
        }
    }

    private void configCriterions(final Criteria dc, final List<Criterion> criterions) {
        if (criterions == null) return;

        for (final Criterion criterion : criterions) {
            dc.add(criterion);
        }
    }

    /**
     * LMS-2505
     *
     * @param idPreManifesto
     * @param idDoctoServico
     * @return Boolean
     */

    public Boolean hasVolumesCarregadosByIdManifesto(Long idManifesto, Long idDoctoServico) {
        if (idManifesto == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("from " + VolumeNotaFiscal.class.getName() + " as vnf ");
        sql.append("inner join vnf.notaFiscalConhecimento as nfc ");
        sql.append("inner join vnf.preManifestoVolumes as pmv ");
        sql.append("where ");
        sql.append("pmv.manifesto.id = ? ");
        sql.append("and nfc.conhecimento.id = ? ");

        Integer num = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idManifesto, idDoctoServico});

        return (num > 0);
    }


    public boolean validateVolumeManifestado(VolumeNotaFiscal volume, Filial filial) {
        String hql = "select vnf from " + ManifestoNacionalVolume.class.getName() + " manifesto_volume " +
                " join manifesto_volume.manifestoViagemNacional mvn" +
                " join manifesto_volume.volumeNotaFiscal vnf" +
                " join mvn.manifesto m " +
                " where m.tpStatusManifesto <> 'CA'" +
                " 	and m.tpManifesto = 'V'" +
                "	and manifesto_volume.volumeNotaFiscal = ?" +
                "	and m.filialByIdFilialDestino = ?";

        Integer num = getAdsmHibernateTemplate().getRowCountForQuery(hql, new Object[]{volume, filial});

        return num > 0;
    }

    public Object[] findVolumesUMchecadosPorIdCTRC(Long idConhecimento, Long idFilial, boolean isVolumeGMDireto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(qtVolumesTotal) as qtVolumesTotal, ")
                .append("	   nvl(sum(nvl(qtVolumes,0)),0) as qtVolumes ")
                .append("  from ( ")
                .append("        select nfc.qt_Volumes as qtVolumesTotal")
                .append("              ,(select sum(vnf.qt_volumes) from volume_nota_fiscal vnf where ")
                .append("              vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ")
                .append("              and vnf.tp_Volume in ('M', 'U')")
                .append("              and vnf.ps_Aferido is not null) as qtVolumes ")
                .append("        FROM ")
                .append("           conhecimento conh ")
                .append("           inner join docto_servico ds on (ds.id_docto_servico = conh.id_conhecimento) ")
                .append("           inner join nota_fiscal_conhecimento nfc on (nfc.id_conhecimento = conh.id_conhecimento) ")
                .append("           inner join filial flo on (flo.id_filial = conh.id_filial_origem) ")
                .append("        where ")
                .append("              conh.tp_Situacao_Conhecimento = :tpSituacaoConhecimento ")
                .append("              and conh.id_Conhecimento = :idConhecimento ");
        if (!isVolumeGMDireto) {
            sql.append("              and flo.id_Filial = :idFilial ");
        }
        sql.append(')');

        SQLQuery query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addScalar("qtVolumesTotal", Hibernate.LONG);
        query.addScalar("qtVolumes", Hibernate.LONG);
        query.setLong("idConhecimento", idConhecimento);
        if (isVolumeGMDireto) {
            query.setString("tpSituacaoConhecimento", "E");
        } else {
            query.setLong("idFilial", idFilial);
            query.setString("tpSituacaoConhecimento", "P");
        }
        return (Object[]) query.uniqueResult();
    }

    public Boolean existeVolumeSemDimensaoCubagem(Long idConhecimento) {
        StringBuilder sql = new StringBuilder()

                .append(" select c")
                .append(" from  ")
                .append(" Conhecimento c ")
                .append(" join c.notaFiscalConhecimentos nf ")
                .append(" join nf.volumeNotaFiscais vol ")
                .append(" where c.id = ? ")
                .append(" and( vol.tpVolume = '" + ConstantesExpedicao.TP_VOLUME_MESTRE + "' or vol.tpVolume = '" + ConstantesExpedicao.TP_VOLUME_UNITARIO + "')")
                .append(" and vol.nrDimensao1Cm is null ")
                .append(" and vol.nrCubagemM3 is null ");

        return !getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idConhecimento}).isEmpty();
    }

    /**
     * Retorna quantidade de volumes do pre-CTRC tipo "M" ou "U" cujo peso
     * aferido nao originou-se de uma pesagem em balanca com modulo ou de forma
     * automatizada.
     *
     * @param idDoctoServico Identificador do documento de servico
     * @return Quantidade de volumes tipo M ou U tem peso aferido em balanca sem
     * modulo ou de forma nao automatizada
     */
    public Integer countVolumeSemModulo(Long idDoctoServico) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT c ")
                .append("FROM Conhecimento c ")
                .append("JOIN c.notaFiscalConhecimentos nf ")
                .append("JOIN nf.volumeNotaFiscais vol ")
                .append("WHERE c.id = ? ")
                .append("AND vol.tpVolume IN ('"
                        + ConstantesExpedicao.TP_VOLUME_MESTRE + "', '"
                        + ConstantesExpedicao.TP_VOLUME_UNITARIO + "') ")
                .append("AND NOT vol.tpOrigemPeso IN ('"
                        + ConstantesExpedicao.TP_ORIGEM_PESO_AUTOMACAO + "', '"
                        + ConstantesExpedicao.TP_ORIGEM_PESO_AUTOMATICA_MANUAL
                        + "')");

        return getAdsmHibernateTemplate().find(sql.toString(),
                new Object[]{idDoctoServico}).size();
    }

    public BigDecimal findTotalDimensaoVolumesSorter(Long idConhecimento) {

        StringBuilder sql = new StringBuilder()

                .append(" select ")
                .append(" coalesce(sum (   				  ")
                .append(" coalesce(vol.nrDimensao1Sorter,0) * ")
                .append(" coalesce(vol.nrDimensao2Sorter,0) * ")
                .append(" coalesce(vol.nrDimensao3Sorter,0)   ")
                .append(" ) ,0) as valor			 	  ")
                .append(" from  ")
                .append(" Conhecimento c ")
                .append(" join c.notaFiscalConhecimentos nf ")
                .append(" join nf.volumeNotaFiscais vol ")
                .append(" where c.id = ? ")
                .append(" and vol.nrDimensao1Sorter is not null ");

        return BigDecimalUtils.getBigDecimal(getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idConhecimento}));
    }

    public BigDecimal findTotalPsAferidoSorter(Long idConhecimento) {

        StringBuilder sql = new StringBuilder()

                .append(" select ")
                .append(" sum ( nvl(vol.psAferidoSorter,0) ) as valor	")
                .append(" from  ")
                .append(" Conhecimento c ")
                .append(" join c.notaFiscalConhecimentos nf ")
                .append(" join nf.volumeNotaFiscais vol ")
                .append(" where c.id = ? ")
                .append(" and vol.psAferidoSorter is not null ");

        return BigDecimalUtils.getBigDecimal(getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idConhecimento}));
    }

    public boolean validateImprimeEtiquetaReembarque(Long idFilial, String nrVolumeColeta) {
        StringBuffer sql = new StringBuffer("")
                .append(" select count(*) ")
                .append(" 	from VolumeNotaFiscal as vnf, ")
                .append(" 	MonitoramentoDescarga as mds, ")
                .append(" 	NotaFiscalConhecimento as nfc, ")
                .append(" 	DoctoServico as ds ")
                .append("		where vnf.notaFiscalConhecimento.idNotaFiscalConhecimento = nfc.idNotaFiscalConhecimento ")
                .append("		and nfc.conhecimento.id = ds.idDoctoServico ")
                .append(" 		and mds.idMonitoramentoDescarga = vnf.monitoramentoDescarga.idMonitoramentoDescarga ")
                .append(" 		and ds.filialByIdFilialOrigem.idFilial <> ? ")
                .append(" 		and vnf.nrVolumeColeta = vnf.nrVolumeEmbarque ")
                .append(" 		and vnf.tpVolume = 'U' ")
                .append(" 		and vnf.psAferido is not null ")
                .append(" 		and mds.filial.idFilial <> ? ")
                .append(" 		and vnf.nrVolumeColeta = ? ");


        Integer num = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idFilial, idFilial, nrVolumeColeta});
        return num > 0;
    }


    public Integer findCodigoLocalizacaoMercadoria(final Long idVolumeNotaFiscal) {

        HibernateCallback hcb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" select lm.cd_localizacao_mercadoria ");
                sql.append(" from localizacao_mercadoria lm ");
                sql.append("      inner join volume_nota_fiscal vnf on vnf.id_localizacao_mercadoria = lm.id_localizacao_mercadoria ");
                sql.append(" where vnf.id_volume_nota_fiscal = ").append(idVolumeNotaFiscal);

                SQLQuery query = session.createSQLQuery(sql.toString());

                query.addScalar("cd_localizacao_mercadoria", Hibernate.INTEGER);

                return (Integer) query.uniqueResult();
            }

        };

        Integer cdLocalizacaoMercadoria = (Integer) getAdsmHibernateTemplate().execute(hcb);

        return cdLocalizacaoMercadoria;

    }

    /**
     * Atualiza a filial da localizacao do volume pela filial do usuario logado
     */
    public void atualizarFilialLocalizacaoVolume(VolumeNotaFiscal volumeNotaFiscal) {
        getAdsmHibernateTemplate().update(volumeNotaFiscal);
        getAdsmHibernateTemplate().flush();
    }

    /**
     * Retorna a lista de volume nota fiscal onde o cliente da nota fiscal conhecimento possua o nrIdentificao
     * igual ao passado como parametro e o numero da nota fiscal seja igual ao passado como parametro.
     * Utilizado pela rotina generateEventoVolumeGM
     *
     * @param nrIdentificacao
     * @param nrNotaFiscal
     * @return
     */
    public List<VolumeNotaFiscal> findVolumesNotaFiscalByNrIdentificacaoClienteENrNota(String nrIdentificacao, Integer nrNotaFiscal) {
        StringBuffer hql = new StringBuffer();
        hql.append("select vnf ")
                .append("  from " + VolumeNotaFiscal.class.getName() + " vnf ")
                .append("where vnf.monitoramentoDescarga.idMonitoramentoDescarga = ( ")
                .append("  select m.idMonitoramentoDescarga from " + Conhecimento.class.getName() + " c ")
                .append(" inner join c.notaFiscalConhecimentos nfc ")
                .append(" inner join nfc.volumeNotaFiscais vnf ")
                .append(" inner join vnf.monitoramentoDescarga m ")

                .append("inner join nfc.cliente cli ")
                .append("inner join cli.pessoa p ")

                .append("where p.nrIdentificacao = ? ")
                .append(" and nfc.nrNotaFiscal = ? ")
                .append(" and rownum = 1 ) ")
                .append(" and exists (select hv.idHistoricoVolume ")
                .append("  from " + HistoricoVolume.class.getName() + " hv ")
                .append(" where hv.codigoVolume = vnf.nrVolumeColeta) ");

        return (List<VolumeNotaFiscal>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{nrIdentificacao, nrNotaFiscal});
    }

    public void updateLocalizacaoELocalizacaoFilial(final Long idVolume, final Long localizacaoMercadoria, final Long idFilial) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String hql = "UPDATE " + getPersistentClass().getName() + " vnf SET " +
                        " vnf.localizacaoMercadoria.idLocalizacaoMercadoria = :idLocalizacaoMercadoria, " +
                        " vnf.localizacaoFilial.idFilial = :idlocalizacaoFilial " +
                        " WHERE vnf.idVolumeNotaFiscal = :idVolumeNotaFiscal";

                query = session.createQuery(hql);
                query.setLong("idLocalizacaoMercadoria", localizacaoMercadoria);
                query.setLong("idlocalizacaoFilial", idFilial);
                query.setLong("idVolumeNotaFiscal", idVolume);
                query.executeUpdate();

                return null;
            }
        });
    }

    /**
     * LMS-6531 - Verifica se para todos os volumes do pre-CTRC que sejam do
     * tipo "M" ou "U" (<tt>VNF.TP_VOLUME</tt>) o peso aferido originou-se de
     * uma pesagem com modulo ou de forma automatizada
     * (<tt>VNF.TP_ORIGEM_PESO</tt> igual a "A" ou "B"). Retorna <tt>false</tt>
     * se existir algum volume do tipo "M" ou "U" que nao atenda a regra acima.
     *
     * @param idDoctoServico id do <tt>DoctoServico</tt>
     * @return <tt>false</tt> se existir algum volume tipo "M" ou "U" com peso
     * aferido de modo nao automatico, caso contrario retorna
     * <tt>true</tt>
     */
    public boolean pesoOriginadoBalancaModulo(Long idDoctoServico) {
        StringBuilder hql = new StringBuilder()
                .append("FROM Conhecimento c ")
                .append("JOIN c.notaFiscalConhecimentos nfc ")
                .append("JOIN nfc.volumeNotaFiscais vnf ")
                .append("WHERE vnf.tpVolume IN ('").append(ConstantesExpedicao.TP_VOLUME_MESTRE).append("', '").append(ConstantesExpedicao.TP_VOLUME_UNITARIO).append("') ")
                .append("AND vnf.tpOrigemPeso NOT IN ('").append(ConstantesExpedicao.TP_ORIGEM_PESO_AUTOMACAO).append("', '").append(ConstantesExpedicao.TP_ORIGEM_PESO_AUTOMATICA_MANUAL).append("') ")
                .append("AND c.idDoctoServico = ? ");
        Integer count = getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{idDoctoServico});
        return count != null && count.equals(0);
    }

	/**
	 * LMS-5719 - Busca as informacoes que deveram ser exibidas na tela de consulta de volume aberta pela tela de divergencia.
	 * Busca as seguinte informacoes:
	 * 		VOLUME_NOTA_FISCAL.NR_SEQUENCIA
	 * 		VOLUME_NOTA_FISCAL.ID_LOCALIZACAO_FILIAL = FILIAL.ID_FILIAL -> FILIAL.SG_FILIAL
	 * 		LOCALIZACAO_MERCADORIA.DS_LOCALIZACAO_MERCADORIA_I
	 * 		Indicador de leitura no carregamento 
	 * 
	 * @param idDoctoServico
	 * @return
	 */
	public List<Map<String,Object>> findVolumesConsultaDivergencia(Long idDoctoServico, Long idControleCarga) {
		StringBuilder query = new StringBuilder();
		
		// Busca o id da nota fiscal conhecimento para poder trazer alem dos volumes ja bipados,
		// tambem os volumes ainda naobipados do documento de servico informado como parametro.
		query.append("select distinct vnf_sec.notaFiscalConhecimento.idNotaFiscalConhecimento ");
		query.append(" from ").append(VolumeNotaFiscal.class.getName()).append(" as vnf_sec ");
		query.append(" left outer join vnf_sec.preManifestoVolumes as pmv_sec ");
		query.append(" where pmv_sec.doctoServico.idDoctoServico = :idDoctoServico");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idDoctoServico", idDoctoServico);
		
		List<Long> idNotaFiscalConhecimento = (List<Long>) getAdsmHibernateTemplate().findByNamedParam(query.toString(), parameters);
		
		StringBuilder subQuery = new StringBuilder();
		subQuery.append("select 1 ");
		subQuery.append("from ").append(PreManifestoVolume.class.getName()).append(" as pmv_2 ");
		subQuery.append("where pmv_2.manifesto.controleCarga.idControleCarga = :idControleCarga ");
		subQuery.append("and pmv_2.volumeNotaFiscal = vnf");
		
		// Busca as informacoes que devem ser apresentadas na tela de consulta de volumes
		query = new StringBuilder();
		query.append("select distinct new map(vnf.nrSequencia as nrSequencia, ");
		query.append("fil.sgFilial as sgFilialLocalizacao, ");
		query.append("lm.dsLocalizacaoMercadoria as statusVolume, ");
		query.append("(" + subQuery.toString()+ ") as volumeLido) ");
		query.append(" from ").append(VolumeNotaFiscal.class.getName()).append(" as vnf "); 
		query.append(" inner join vnf.localizacaoFilial as fil ");
		query.append(" left outer join vnf.localizacaoMercadoria as lm ");
		query.append(" left outer join vnf.preManifestoVolumes as pmv ");
		query.append(" left outer join vnf.notaFiscalConhecimento as nfc ");
		query.append(" left outer join pmv.preManifestoDocumento as pmd ");
		query.append(" left outer join pmd.manifesto as man ");
		query.append(" left outer join man.controleCarga as cc ");
		query.append("where nfc.idNotaFiscalConhecimento IN (:idNotaFiscalConhecimento) ");
		query.append("order by 1 asc");
		
		parameters.put("idControleCarga", idControleCarga);
		parameters.put("idNotaFiscalConhecimento", idNotaFiscalConhecimento);
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parameters);
	}
	
	public boolean validateVolumeSobra(VolumeNotaFiscal volume, ControleCarga controleCarga, Filial filialDestino) {
		String hql = 
				"from "+ManifestoNacionalVolume.class.getName() +" manifesto_volume " +
				" join manifesto_volume.manifestoViagemNacional mvn" +
				" join mvn.manifesto m " +
				" where m.tpStatusManifesto <> 'CA'" +
				" 	and m.tpManifesto = 'V'" +
				"	and manifesto_volume.volumeNotaFiscal = ? " +
				"   and m.controleCarga = ? " +
				"	and m.filialByIdFilialDestino = ?";
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql, new Object[] { volume, controleCarga, filialDestino }) == 0;
	}

	public boolean validateIsEtiquetaMWW(String nrCodigoBarras, Date dataMinimaMWW, boolean isFilialSorter) {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM VOLUME_NOTA_FISCAL VNF ")
		.append("LEFT JOIN NOTA_FISCAL_CONHECIMENTO NFC ON VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
		.append("LEFT JOIN DOCTO_SERVICO DS ON NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
		.append("LEFT JOIN CLIENTE CL ON NFC.ID_CLIENTE = CL.ID_CLIENTE ")
		.append("WHERE 1 = 1 ")
		.append("AND (NR_VOLUME_EMBARQUE = :nrCodigoBarras OR NR_VOLUME_COLETA = :nrCodigoBarras) ")
		.append("AND CL.BL_LIBERA_MWW = :blLiberaMWW ")
		.append("AND CL.BL_NUMERO_VOLUME_EDI = :blNumeroVolumeEDI ")
		.append("AND CL.BL_LIBERA_ETIQUETA_EDI = :blLiberaEtiqueEDI ");

		if(isFilialSorter){
				sql.append("AND (DS.ID_LOCALIZACAO_MERCADORIA NOT IN (:localizacaoCancelado, :localizacaoEntregaRealizada, :localizacaoDevolvidaRemetente, :localizacaoRefaturada) OR DS.ID_LOCALIZACAO_MERCADORIA IS NULL) ");
		} else {
				sql.append("AND DS.ID_LOCALIZACAO_MERCADORIA NOT IN (:localizacaoCancelado, :localizacaoEntregaRealizada, :localizacaoDevolvidaRemetente, :localizacaoRefaturada) ");
		}

		sql.append("AND DS.DH_EMISSAO >= TO_DATE(:dataMinimaMWW, 'DD/MM/YYYY')");

		Map parameters = new HashMap<String, Object>();
		parameters.put("nrCodigoBarras", nrCodigoBarras);
		parameters.put("blLiberaMWW", INDICADOR_SIM);
		parameters.put("blNumeroVolumeEDI", INDICADOR_SIM);
		parameters.put("blLiberaEtiqueEDI", INDICADOR_SIM);
		parameters.put("localizacaoCancelado", ID_LOCALIZACAO_MERCADORIA_CANCELADO);
		parameters.put("localizacaoEntregaRealizada", ID_LOCALIZACAO_MERCADORIA_ENTREGA_REALIZADA);
		parameters.put("localizacaoDevolvidaRemetente", ID_LOCALIZACAO_MERCADORIA_DEVOLVIDA_REMETENTE);
		parameters.put("localizacaoRefaturada", ID_LOCALIZACAO_MERCADORIA_REFATURADA);
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		parameters.put("dataMinimaMWW", sf.format(dataMinimaMWW));

		Integer rowCount = getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters);
		return rowCount > 0;
	}
	
	public List<VolumeNotaFiscal> findVolumesNotaFiscalByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		StringBuilder hql = new StringBuilder();
		hql.append("select vnf ")
		.append(" from " + VolumeNotaFiscal.class.getName() + " vnf ")
		.append(" where vnf.notaFiscalConhecimento.id = ? ");

		return (List<VolumeNotaFiscal>) getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idNotaFiscalConhecimento });
	}

	public List<VolumeNotaFiscal> findVolumeNotaFiscalNotaCreditoPadrao(Long idConhecimento, Long idControleCarga) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();

        StringBuilder query = new StringBuilder();
        query.append(" select vnf");
        query.append(" from VolumeNotaFiscal as vnf, ");
        query.append(" 		NotaFiscalConhecimento as nfc, ");
        query.append("		EntregaNotaFiscal as enf, ");
        query.append("		OcorrenciaEntrega as oe, ");
        query.append("      Manifesto as man "); 
        query.append(" where vnf.notaFiscalConhecimento.id = nfc.id");
        query.append("	 and nfc.conhecimento.id = :idConhecimento");
        query.append("	 and enf.notaFiscalConhecimento.id = nfc.id");
        query.append("	 and enf.ocorrenciaEntrega.id = oe.id");
        query.append("   and enf.manifesto.id = man.id ");
        query.append("   and man.controleCarga.id = :idControleCarga "); 
        query.append("	 and oe.tpOcorrencia in ('E','A')");
        
        parametersValues.put("idConhecimento", idConhecimento);
        parametersValues.put("idControleCarga", idControleCarga);

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }

	public void updateLocalizacaoMercadoriaByNotaFiscalConhecimento(
			NotaFiscalConhecimento notaFiscalConhecimento,
			LocalizacaoMercadoria localizacaoMercadoria) {
		String sql = "update VOLUME_NOTA_FISCAL set ID_LOCALIZACAO_MERCADORIA = :idLocalizacaoMercadoria where ID_NOTA_FISCAL_CONHECIMENTO = :idNotaFiscalConhecimento";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idLocalizacaoMercadoria", localizacaoMercadoria.getIdLocalizacaoMercadoria());
		param.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());
		getAdsmHibernateTemplate().executeUpdateBySql(sql, param);
	}
	 
}
