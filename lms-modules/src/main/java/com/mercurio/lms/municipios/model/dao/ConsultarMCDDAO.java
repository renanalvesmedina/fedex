package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.param.MCDParam;

/**
 * DAO pattern.
 * 
 * Aqui encontra-se o SQL utilizado na emissão do relatório de MCD e na consulta
 * de MCD. ATENÇÃO! Muito cuidado ao realizar uma alteração. Analisar o impacto
 * em ambas as implementações. Caso seja alterado o calculo de dias de atendimento que se encontra na getSqlBasic
 *  alterar também o método ConsultarMCDService.verificaDiasAtendimento
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ConsultarMCDDAO extends AdsmDao {

	public static SqlTemplate getSqlBasic(MCDParam mcdParam) {
		List<Object> values = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT tb.id_filial_destino as ID_FILIAL_DESTINO \n");
		sb.append(",      tb.id_filial_origem as ID_FILIAL_ORIGEM \n");
		sb.append(",      tb.id_filial_reembarcadora as ID_FILIAL_REEMBARCADORA \n");
		sb.append(",      tb.sg_filial_reembarcadora as SG_FILIAL_REEMBARCADORA \n");
		sb.append(",      tb.nm_filial_reembarcadora as NM_FILIAL_REEMBARCADORA \n");
		sb.append(",      tb.nr_prazo_total \n");
		sb.append(",      REGEXP_REPLACE( tb.ds_servico, '^.*pt_BR»(([[:alnum:]]|[[:space:]])+).*$', '\\1' ) as DS_SERVICO \n");
		sb.append(",      tb.id_servico as ID_SERVICO \n");
		sb.append(",      tb.nr_dias_atendidos_origem as NR_DIAS_ATENDIDOS_ORIGEM \n");
		sb.append(",      tb.nr_dias_atendidos_destino as NR_DIAS_ATENDIDOS_DESTINO \n");
		sb.append(" \n");
		sb.append("--dados da origem \n");
		sb.append(",      tb.bl_distrito_origem as BL_DISTRITO_ORIGEM \n");
		sb.append(",      tb.bl_domingo_origem as BL_DOMINGO_ORIGEM \n");
		sb.append(",      tb.bl_segunda_origem as BL_SEGUNDA_ORIGEM \n");
		sb.append(",      tb.bl_terca_origem as BL_TERCA_ORIGEM \n");
		sb.append(",      tb.bl_quarta_origem as BL_QUARTA_ORIGEM \n");
		sb.append(",      tb.bl_quinta_origem as BL_QUINTA_ORIGEM \n");
		sb.append(",      tb.bl_sexta_origem as BL_SEXTA_ORIGEM \n");
		sb.append(",      tb.bl_sabado_origem as BL_SABADO_ORIGEM \n");
		sb.append(",      tb.cd_ibge_origem as CD_IBGE_ORIGEM \n");
		sb.append(",      tb.id_municipio_filial_origem as ID_MUNICIPIO_FILIAL_ORIGEM \n");
		sb.append(",      tb.id_municipio_origem as ID_MUNICIPIO_ORIGEM \n");
		sb.append(",      tb.id_pais_origem as ID_PAIS_ORIGEM \n");
		sb.append(",      tb.id_tipo_localizacao_origem as ID_TIPO_LOCALIZACAO_ORIGEM \n");
		sb.append(",      tb.id_unidade_federativa_origem as ID_UNIDADE_FEDERATIVA_ORIGEM \n");
		sb.append(",      tb.id_zona_origem as ID_ZONA_ORIGEM \n");
		sb.append(",      tb.nm_filial_origem as NM_FILIAL_ORIGEM \n");
		sb.append(",      tb.nm_municipio_origem as NM_MUNICIPIO_ORIGEM \n");
		sb.append(",      REGEXP_REPLACE( tb.nm_pais_origem, '^.*pt_BR»(([[:alnum:]]|[[:space:]])+).*$', '\\1' ) as NM_PAIS_ORIGEM \n");
		sb.append(" \n");
		sb.append(",      tb.nm_unidade_federativa_origem as NM_UNIDADE_FEDERATIVA_ORIGEM \n");
		sb.append(",      tb.sg_filial_origem as SG_FILIAL_ORIGEM \n");
		sb.append(",      tb.nr_tempo_entrega_origem as NR_TEMPO_ENTREGA_ORIGEM \n");
		sb.append(",      tb.dt_vigencia_final as DT_VIGENCIA_FINAL \n");
		sb.append(",      tb.dt_vigencia_inicial as DT_VIGENCIA_INICIAL \n");
		sb.append(",      tb.nr_tempo_coleta as NR_TEMPO_COLETA \n");
		sb.append("--dados do destino \n");
		sb.append(",      tb.bl_distrito_destino as BL_DISTRITO_DESTINO \n");
		sb.append(",      tb.bl_domingo_destino as BL_DOMINGO_DESTINO \n");
		sb.append(",      tb.bl_segunda_destino as BL_SEGUNDA_DESTINO \n");
		sb.append(",      tb.bl_terca_destino as BL_TERCA_DESTINO \n");
		sb.append(",      tb.bl_quarta_destino as BL_QUARTA_DESTINO \n");
		sb.append(",      tb.bl_quinta_destino as BL_QUINTA_DESTINO \n");
		sb.append(",      tb.bl_sexta_destino as BL_SEXTA_DESTINO \n");
		sb.append(",      tb.bl_sabado_destino as BL_SABADO_DESTINO \n");
		sb.append(",      tb.cd_ibge_destino as CD_IBGE_DESTINO \n");
		sb.append(",      tb.id_municipio_destino as ID_MUNICIPIO_DESTINO \n");
		sb.append(",      tb.id_municipio_filial_destino as ID_MUNICIPIO_FILIAL_DESTINO \n");
		sb.append(",      tb.id_pais_destino as ID_PAIS_DESTINO \n");
		sb.append(",      tb.id_tipo_localizacao_destino as ID_TIPO_LOCALIZACAO_DESTINO \n");
		sb.append(",      tb.id_unidade_federativa_destino as ID_UNIDADE_FEDERATIVA_DESTINO \n");
		sb.append(",      tb.id_zona_destino as ID_ZONA_DESTINO \n");
		sb.append(",      tb.nm_filial_destino as NM_FILIAL_DESTINO \n");
		sb.append(",      tb.nm_municipio_destino as NM_MUNICIPIO_DESTINO \n");
		sb.append(",      REGEXP_REPLACE( tb.nm_pais_destino, '^.*pt_BR»(([[:alnum:]]|[[:space:]])+).*$', '\\1' ) as NM_PAIS_DESTINO \n");
		sb.append(",      tb.nm_unidade_federativa_destino as NM_UNIDADE_FEDERATIVA_DESTINO \n");
		sb.append(",      tb.sg_filial_destino as SG_FILIAL_DESTINO \n");
		sb.append(",      tb.nr_tempo_entrega_destino as NR_TEMPO_ENTREGA_DESTINO \n");
		sb.append(" \n");
		sb.append("--dados do calculo de tarifa \n");
		sb.append(",      tb.distancia_total as DISTANCIA_TOTAL \n");
		sb.append(" \n");
		sb.append("--somar todos os postos de passagem do municipio filial de origem \n");
		sb.append(",      tb.qt_pedagio as QT_PEDAGIO \n");
		sb.append(" \n");
		sb.append("--Ver esquema da empresa \n");
		sb.append(",      ( SELECT cd_tarifa_preco \n");
		sb.append("         FROM   tarifa_preco \n");
		sb.append("         WHERE  tb.distancia_total BETWEEN nr_km_inicial \n");
		sb.append("                                   AND nr_km_final ) AS cd_tarifa_preco \n");
		sb.append(",      ( SELECT cd_tarifa_preco \n");
		sb.append("         FROM   tarifa_preco \n");
		sb.append("         WHERE  tb.distancia_total BETWEEN nr_km_inicial_atual \n");
		sb.append("                                   AND nr_km_final_atual ) AS CD_TARIFA_PRECO_ATUAL \n");
		sb.append(" \n");
		sb.append("--Dados do calculo de PPE, em dias \n");
		sb.append(",      CEIL( NVL( nr_prazo_cliente \n");
		sb.append("                , DECODE( tb.nr_dias_atendidos_destino \n");// ATENÇÃO Caso seja alterado o calculo de dias de atendimento alterar também o método ConsultarMCDService.verificaDiasAtendimento
		sb.append("                        , 1, 5 \n");
		sb.append("                        , 2, 2 \n");
		sb.append("                        , 3, 1 \n");
		sb.append("                        , 0 ) * 24 \n");
		sb.append("                + ( tb.nr_tempo_entrega_destino \n");
		sb.append("                  + tb.nr_tempo_coleta \n");
		sb.append("                  + nr_prazo_total ) / 60 ) \n");
		sb.append("           / DECODE( tb.tp_emissao, 'D', 24, 'H', 1 ) ) as NR_PPE \n");
		sb.append(" \n");
		sb.append(",      tb.sg_regional_origem as SG_REGIONAL_ORIGEM \n");
		sb.append(",      tb.sg_regional_destino as SG_REGIONAL_DESTINO \n");
		sb.append(",      tb.sg_unidade_federativa_origem \n");
		sb.append(",      tb.sg_unidade_federativa_destino as SG_UNIDADE_FEDERATIVA_DESTINO \n");
		sb.append(" \n");
		sb.append("FROM ( \n");
		sb.append("      SELECT \n");
		sb.append("      --Dados do Fluxo Filial \n");
		sb.append("             ff.id_filial_destino                       AS ID_FILIAL_DESTINO, \n");
		sb.append("             ff.id_filial_origem                        AS ID_FILIAL_ORIGEM, \n");
		sb.append("             ff.id_filial_reembarcadora                 AS ID_FILIAL_REEMBARCADORA, \n");
		sb.append("             fr.sg_filial                               AS sg_filial_reembarcadora, \n");
		sb.append("             fr.sg_filial \n");
		sb.append("                || NVL2( ff.id_filial_reembarcadora, ' - ', '' ) \n");
		sb.append("                || p_fr.nm_fantasia                     AS NM_FILIAL_REEMBARCADORA,    --Dados da filial reembarcadora \n");
		sb.append(" \n");
		sb.append("             ( SELECT SUM( ff1.nr_prazo ) \n");
		sb.append("               FROM   fluxo_filial ff1 \n");
		sb.append("               WHERE  p.dt BETWEEN ff1.dt_vigencia_inicial AND ff1.dt_vigencia_final \n");
		sb.append("               AND    ff1.id_filial_destino = ff.id_filial_destino \n");
		sb.append("               AND    NVL(ff.id_servico, -1) = NVL(ff1.id_servico, -1) \n");
		sb.append("               AND    EXISTS ( SELECT 1 \n");
		sb.append("                               FROM   ordem_filial_fluxo off1 \n");
		sb.append("                               WHERE  off1.id_fluxo_filial = ff.id_fluxo_filial \n");
		sb.append("                               AND    ff1.id_filial_origem = off1.id_filial \n");
		sb.append("                               AND    NOT ( off1.nr_ordem > 1 \n");
		sb.append("                                        AND off1.id_filial = ff.id_filial_destino ) \n");
		sb.append("                               AND    ROWNUM = 1 ) )    as NR_PRAZO_TOTAL, \n");
		sb.append(" \n");
		sb.append("             (  SELECT NVL( ( SELECT  pec.nr_prazo --Municipio \n");
		sb.append("                               FROM   prazo_entrega_cliente pec \n");
		sb.append("                               WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                               AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                               AND    pec.id_municipio_destino = m_fd.id_municipio \n");
		sb.append("                               AND    pec.id_uf_destino = uf_fd.id_unidade_federativa \n");
		sb.append("                               AND    ROWNUM = 1 ) \n");
		sb.append("                 ,      NVL( ( SELECT pec.nr_prazo --Filial \n");
		sb.append("                               FROM   prazo_entrega_cliente pec \n");
		sb.append("                               WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                               AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                               AND    pec.id_municipio_destino IS NULL \n");
		sb.append("                               AND    pec.id_filial_destino = fd.id_filial \n");
		sb.append("                               AND    pec.id_uf_destino = uf_fd.id_unidade_federativa \n");
		sb.append("                               AND    ROWNUM = 1  ) \n");
		sb.append("                ,      NVL( ( SELECT  pec.nr_prazo --Tipo Localização Municipio \n");
		sb.append("                               FROM   prazo_entrega_cliente pec \n");
		sb.append("                               WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                               AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                               AND    pec.id_municipio_destino IS NULL \n");
		sb.append("                               AND    pec.id_tipo_loc_munic_destino = osl_fd.id_tipo_localizacao_municipio \n");
		sb.append("                               AND    pec.id_uf_destino = uf_fd.id_unidade_federativa \n");
		sb.append("                               AND    ROWNUM = 1  ) \n");
		sb.append("                ,      NVL( ( SELECT pec.nr_prazo -- UF \n");
		sb.append("                               FROM   prazo_entrega_cliente pec \n");
		sb.append("                               WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                               AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                               AND    pec.id_municipio_destino      IS NULL \n");
		sb.append("                               AND    pec.id_tipo_loc_munic_destino IS NULL \n");
		sb.append("                               AND    pec.id_filial_destino         IS NULL \n");
		sb.append("                               AND    pec.id_uf_destino = uf_fd.id_unidade_federativa \n");
		sb.append("                               AND    ROWNUM = 1  ) \n");
		sb.append("                ,      NVL( ( SELECT pec.nr_prazo \n");
		sb.append("                               FROM   prazo_entrega_cliente pec \n");
		sb.append("                               WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                               AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                               AND    pec.id_municipio_destino      IS NULL \n");
		sb.append("                               AND    pec.id_tipo_loc_munic_destino IS NULL \n");
		sb.append("                               AND    pec.id_filial_destino         IS NULL \n");
		sb.append("                               AND    pec.id_uf_destino             IS NULL \n");
		sb.append("                               AND    pec.id_pais_destino = ps_fd.id_pais \n");
		sb.append("                               AND    ROWNUM = 1  ) \n");
		sb.append("                ,        ( SELECT pec.nr_prazo \n");
		sb.append("                      FROM   prazo_entrega_cliente pec \n");
		sb.append("                      WHERE  NVL( ff.id_servico, NVL( pec.id_servico, -1 ) ) = NVL( pec.id_servico, -1 ) \n");
		sb.append("                      AND    pec.id_cliente = p.id_cliente \n");
		sb.append("                      AND    pec.id_municipio_destino      IS NULL \n");
		sb.append("                      AND    pec.id_tipo_loc_munic_destino IS NULL \n");
		sb.append("                      AND    pec.id_filial_destino         IS NULL \n");
		sb.append("                      AND    pec.id_uf_destino             IS NULL \n");
		sb.append("                      AND    pec.id_pais_destino           IS NULL \n");
		sb.append("                      AND    pec.id_zona_destino = ps_fd.id_zona \n");
		sb.append("                      AND    ROWNUM = 1  ) ) ) ) ) ) \n");
		sb.append(" \n");
		sb.append("             FROM   dual \n");
		sb.append("             WHERE  p.id_cliente IS NOT NULL ) AS nr_prazo_cliente, \n");
		sb.append(" \n");
		sb.append("             s.ds_servico_i                             AS DS_SERVICO, --Descrição do servico, null SE O FLUXO NÃO TEMS ERVIÇO \n");
		sb.append("             ff.id_servico                              AS ID_SERVICO, \n");
		sb.append("             p.tp_emissao                               AS tp_emissao, \n");
		sb.append(" \n");
		sb.append("             ( DECODE( osl_fo.bl_domingo, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_segunda, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_terca, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_quarta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_quinta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_sexta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fo.bl_sabado, 'S', 1, 0 )  ) AS NR_DIAS_ATENDIDOS_ORIGEM, \n");
		sb.append("             ( DECODE( osl_fd.bl_domingo, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_segunda, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_terca, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_quarta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_quinta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_sexta, 'S', 1, 0 ) \n");
		sb.append("             + DECODE( osl_fd.bl_sabado, 'S', 1, 0 ) ) AS NR_DIAS_ATENDIDOS_DESTINO, \n");
		sb.append("  --DADOS DA ORIGEM \n");
		sb.append(" \n");
		sb.append("             osl_fo.bl_domingo                    AS BL_DOMINGO_ORIGEM, \n");
		sb.append("             osl_fo.bl_segunda                    AS BL_SEGUNDA_ORIGEM, \n");
		sb.append("             osl_fo.bl_terca                      AS BL_TERCA_ORIGEM, \n");
		sb.append("             osl_fo.bl_quarta                     AS BL_QUARTA_ORIGEM, \n");
		sb.append("             osl_fo.bl_quinta                     AS BL_QUINTA_ORIGEM, \n");
		sb.append("             osl_fo.bl_sexta                      AS BL_SEXTA_ORIGEM, \n");
		sb.append("             osl_fo.bl_sabado                     AS BL_SABADO_ORIGEM, \n");
		sb.append("             NVL( osl_fo.nr_tempo_entrega, 0 )    AS NR_TEMPO_ENTREGA_ORIGEM, \n");
		sb.append("             NVL( osl_fo.nr_tempo_coleta , 0 )    AS nr_tempo_coleta, \n");
		sb.append("             osl_fo.id_tipo_localizacao_municipio AS ID_TIPO_LOCALIZACAO_ORIGEM, \n");
		sb.append(" \n");
		sb.append("             m_fo.cd_ibge                         AS CD_IBGE_ORIGEM, \n");
		sb.append("             m_fo.bl_distrito                     AS BL_DISTRITO_ORIGEM, \n");
		sb.append("             m_fo.id_municipio                    AS ID_MUNICIPIO_ORIGEM, \n");
		sb.append("             m_fo.nm_municipio                    AS NM_MUNICIPIO_ORIGEM, \n");
		sb.append(" \n");
		sb.append("             mf_fo.id_municipio_filial            AS ID_MUNICIPIO_FILIAL_ORIGEM, \n");
		sb.append("             mf_fo.dt_vigencia_inicial            AS DT_VIGENCIA_INICIAL, \n");
		sb.append("             mf_fo.dt_vigencia_final              AS DT_VIGENCIA_FINAL, \n");
		sb.append(" \n");
		sb.append("             ps_fo.id_pais                        AS ID_PAIS_ORIGEM, \n");
		sb.append("             ps_fo.id_zona                        AS ID_ZONA_ORIGEM, \n");
		sb.append("             ps_fo.nm_pais_i                      AS NM_PAIS_ORIGEM, \n");
		sb.append(" \n");
		sb.append("             uf_fo.id_unidade_federativa          AS ID_UNIDADE_FEDERATIVA_ORIGEM, \n");
		sb.append("             uf_fo.nm_unidade_federativa          AS NM_UNIDADE_FEDERATIVA_ORIGEM, \n");
		sb.append("             uf_fo.sg_unidade_federativa          AS sg_unidade_federativa_origem, \n");
		sb.append(" \n");
		sb.append("             p_fo.nm_fantasia                     AS NM_FILIAL_ORIGEM, \n");
		sb.append(" \n");
		sb.append("             fo.sg_filial                         AS SG_FILIAL_ORIGEM, \n");
		sb.append(" \n");
		sb.append("             r_fo.sg_regional                     AS sg_regional_origem, \n");
		sb.append("  --DADOS DO DESTINO \n");
		sb.append(" \n");
		sb.append("             osl_fd.bl_domingo                    AS BL_DOMINGO_DESTINO, \n");
		sb.append("             osl_fd.bl_segunda                    AS BL_SEGUNDA_DESTINO, \n");
		sb.append("             osl_fd.bl_terca                      AS BL_TERCA_DESTINO, \n");
		sb.append("             osl_fd.bl_quarta                     AS BL_QUARTA_DESTINO, \n");
		sb.append("             osl_fd.bl_quinta                     AS BL_QUINTA_DESTINO, \n");
		sb.append("             osl_fd.bl_sexta                      AS BL_SEXTA_DESTINO, \n");
		sb.append("             osl_fd.bl_sabado                     AS BL_SABADO_DESTINO, \n");
		sb.append(" \n");
		sb.append("             osl_fd.id_tipo_localizacao_municipio AS ID_TIPO_LOCALIZACAO_DESTINO, \n");
		sb.append("             NVL( osl_fd.nr_tempo_entrega, 0 )    AS nr_tempo_entrega_destino, \n");
		sb.append(" \n");
		sb.append("             m_fd.bl_distrito                     AS BL_DISTRITO_DESTINO, \n");
		sb.append("             m_fd.cd_ibge                         AS CD_IBGE_DESTINO, \n");
		sb.append("             m_fd.id_municipio                    AS ID_MUNICIPIO_DESTINO, \n");
		sb.append("             m_fd.nm_municipio                    AS NM_MUNICIPIO_DESTINO, \n");
		sb.append(" \n");
		sb.append("             mf_fd.id_municipio_filial            AS ID_MUNICIPIO_FILIAL_DESTINO, \n");
		sb.append("             mf_fd.bl_padrao_mcd                  AS mf_fd_bl_mcd, \n");
		sb.append(" \n");
		sb.append("             ps_fd.id_pais                        AS ID_PAIS_DESTINO, \n");
		sb.append("             ps_fd.id_zona                        AS ID_ZONA_DESTINO, \n");
		sb.append("             ps_fd.nm_pais_i                      AS NM_PAIS_DESTINO, \n");
		sb.append(" \n");
		sb.append("             uf_fd.id_unidade_federativa          AS ID_UNIDADE_FEDERATIVA_DESTINO, \n");
		sb.append("             uf_fd.nm_unidade_federativa          AS NM_UNIDADE_FEDERATIVA_DESTINO, \n");
		sb.append("             uf_fd.sg_unidade_federativa          AS sg_unidade_federativa_destino, \n");
		sb.append(" \n");
		sb.append("             p_fd.nm_fantasia                     AS NM_FILIAL_DESTINO, \n");
		sb.append(" \n");
		sb.append("             fd.sg_filial                         AS SG_FILIAL_DESTINO, \n");
		sb.append(" \n");
		sb.append("             r_fd.sg_regional                     AS sg_regional_destino, \n");
		sb.append(" \n");
		sb.append("  --Dados do calculo de tarifa \n");
		sb.append("             NVL(ff.nr_grau_dificuldade, 0) \n");
		sb.append("      +      NVL(ff.nr_distancia, 0) \n");
		sb.append("      +      NVL(mf_fo.nr_distancia_asfalto, 0) \n");
		sb.append("      +      NVL(mf_fo.nr_grau_dificuldade, 0) \n");
		sb.append("      +      NVL(mf_fo.nr_distancia_chao, 0) \n");
		sb.append("      +      NVL(mf_fd.nr_distancia_asfalto, 0) \n");
		sb.append("      +      NVL(mf_fd.nr_grau_dificuldade, 0) \n");
		sb.append("      +      NVL(mf_fd.nr_distancia_chao, 0)      AS DISTANCIA_TOTAL, \n");
		sb.append(" \n");
		sb.append("  --Dados da quantidade de pedagio \n");
		sb.append(" \n");
		sb.append("  --SOMAR TODOS OS POSTOS DE PASSAGEM DO MUNICIPIO FILIAL DE ORIGEM \n");
		sb.append("  ( \n");
		sb.append("            ( SELECT COUNT ( 1 ) \n");
		sb.append("              FROM   posto_passagem_municipio ppm_fo \n");
		sb.append("              JOIN   posto_passagem           pp_fo  ON ppm_fo.id_posto_passagem = pp_fo.id_posto_passagem \n");
		sb.append("              ,      posto_passagem_municipio ppm_fd \n");
		sb.append("              JOIN   posto_passagem           pp_fd  ON ppm_fd.id_posto_passagem = pp_fd.id_posto_passagem \n");
		sb.append(" \n");
		              sb.append("WHERE ppm_fo.id_municipio_filial = mf_fo.id_municipio_filial \n");
		sb.append("              AND   ppm_fd.id_municipio_filial = mf_fd.id_municipio_filial \n");
		sb.append("              AND   p.dt BETWEEN ppm_fd.dt_vigencia_inicial AND ppm_fd.dt_vigencia_final \n");
		sb.append("              AND   p.dt BETWEEN ppm_fo.dt_vigencia_inicial AND ppm_fo.dt_vigencia_final \n");
		sb.append("              AND   p.dt BETWEEN pp_fd.dt_vigencia_inicial AND pp_fd.dt_vigencia_final \n");
		sb.append("              AND   p.dt BETWEEN pp_fo.dt_vigencia_inicial AND pp_fo.dt_vigencia_final \n");
		sb.append("              AND   (   mf_fo.id_municipio != mf_fd.id_municipio \n");
		sb.append("                    OR  mf_fo.id_municipio = mf_fd.id_municipio \n");
		sb.append("                    AND pp_fd.tp_sentido_cobranca = 'BD'  ) ) \n");
		sb.append("              + ( \n");
		sb.append("              SELECT COUNT ( 1 ) \n");
		sb.append("              FROM   posto_passagem_trecho ppt \n");
		sb.append("              JOIN   ordem_filial_fluxo    offo ON ppt.id_filial_origem = offo.id_filial \n");
		sb.append("              JOIN   ordem_filial_fluxo    offd ON ppt.id_filial_destino = offd.id_filial \n");
		sb.append("              WHERE  offo.id_fluxo_filial = ff.id_fluxo_filial \n");
		sb.append("              AND    offd.id_fluxo_filial = ff.id_fluxo_filial \n");
		sb.append("              AND    offo.nr_ordem + 1 = offd.nr_ordem \n");
		sb.append("              AND    p.dt BETWEEN ppt.dt_vigencia_inicial AND ppt.dt_vigencia_final ) ) AS QT_PEDAGIO \n");
		sb.append(" \n");
		sb.append(expressao("      FROM      ( SELECT ?           AS dt \n",mcdParam.getDtVigencia(),values));
		sb.append(expressao("                  ,      ?           AS id_serv \n",mcdParam.getIdServico(),values));
		sb.append(expressao("                  ,      ?           AS tp_emissao \n",mcdParam.getTpEmissao(),values));
		sb.append(expressao("                  ,      ?           AS id_mun_o \n",mcdParam.getIdMunicipioOrigem(),values));
		sb.append(expressao("                  ,      ?           AS id_fo \n",mcdParam.getIdFilialOrigem(),values));
		sb.append(expressao("                  ,      ?           AS id_uf_o \n",mcdParam.getIdUnidadeFederativaOrigem(),values));
		sb.append(expressao("                  ,      ?           AS id_reg_o \n",mcdParam.getIdRegionalOrigem(),values));
		sb.append(expressao("                  ,      ?           AS id_mun_d \n",mcdParam.getIdMunicipioDestino(),values));
		sb.append(expressao("                  ,      ?           AS id_fd \n",mcdParam.getIdFilialDestino(),values));
		sb.append(expressao("                  ,      ?           AS id_uf_d \n",mcdParam.getIdUnidadeFederativaDestino(),values));
		sb.append(expressao("                  ,      ?           AS id_reg_d \n",mcdParam.getIdRegionalDestino(),values));
		sb.append(expressao("                  ,      ?           AS id_cliente \n",mcdParam.getIdCliente(),values));
		sb.append("                  FROM   dual ) p \n");
		sb.append("      ,         fluxo_filial              ff \n");
		sb.append("      LEFT JOIN servico                   s      ON s.id_servico = ff.id_servico \n");
		sb.append("      --JOIN com a filial reeembarcadora \n");
		sb.append("      LEFT JOIN filial                    fr     ON fr.id_filial = ff.id_filial_reembarcadora \n");
		sb.append("      LEFT JOIN pessoa                    p_fr   ON p_fr.id_pessoa = fr.id_filial \n");
		sb.append(" \n");
		sb.append("      --JOIN com a filial de origem \n");
		sb.append("      JOIN      filial                    fo     ON  fo.id_filial = ff.id_filial_origem \n");
		sb.append("      JOIN      pessoa                    p_fo   ON  fo.id_filial = p_fo.id_pessoa \n");
		sb.append("      JOIN      endereco_pessoa           ep_fo  ON  ep_fo.id_endereco_pessoa = p_fo.id_endereco_pessoa \n");
		sb.append(" \n");
		sb.append("      JOIN      municipio_filial          mf_fo  ON  mf_fo.id_municipio = ep_fo.id_municipio \n");
		sb.append(expressao("                                                 AND ? IS NULL \n",mcdParam.getIdMunicipioOrigem(),values));
		sb.append("                                                 OR  mf_fo.id_filial = fo.id_filial \n");
		sb.append(expressao("                                                 AND ? IS NOT NULL \n",mcdParam.getIdMunicipioOrigem(),values));
		sb.append(" \n");
		sb.append("      JOIN      municipio                 m_fo   ON  mf_fo.id_municipio = m_fo.id_municipio \n");
		sb.append("      JOIN      unidade_federativa        uf_fo  ON  m_fo.id_unidade_federativa = uf_fo.id_unidade_federativa \n");
		sb.append("      JOIN      pais                      ps_fo  ON  uf_fo.id_pais = ps_fo.id_pais \n");
		sb.append("      JOIN      operacao_servico_localiza osl_fo ON  osl_fo.id_municipio_filial = mf_fo.id_municipio_filial \n");
		sb.append("      JOIN      regional_filial           rf_fo  ON  fo.id_filial = rf_fo.id_filial \n");
		sb.append("      JOIN      regional                  r_fo   ON  rf_fo.id_regional = r_fo.id_regional \n");
		sb.append("      JOIN      historico_filial          h_fo   ON  fo.id_filial = h_fo.id_filial \n");
		sb.append(" \n");
		sb.append("      --JOIN com a filial de destino \n");
		sb.append("      JOIN      filial                    fd     ON  fd.id_filial = ff.id_filial_destino \n");
		sb.append("      JOIN      pessoa                    p_fd   ON  fd.id_filial = p_fd.id_pessoa \n");
		sb.append("      JOIN      endereco_pessoa           ep_fd  ON  p_fd.id_endereco_pessoa = ep_fd.id_endereco_pessoa \n");
		sb.append(" \n");
		sb.append("      JOIN      municipio_filial          mf_fd  ON  mf_fd.id_municipio = ep_fd.id_municipio \n");
		sb.append(expressao("                                                 AND ? IS NULL \n",mcdParam.getIdMunicipioDestino(),values));
		sb.append("                                                 OR  mf_fd.id_filial = fd.id_filial \n");
		sb.append(expressao("                                                 AND ? IS NOT NULL \n",mcdParam.getIdMunicipioDestino(),values));
		sb.append(" \n");
		sb.append("      JOIN      municipio                 m_fd   ON  m_fd.id_municipio = mf_fd.id_municipio \n");
		sb.append("      JOIN      unidade_federativa        uf_fd  ON  m_fd.id_unidade_federativa = uf_fd.id_unidade_federativa \n");
		sb.append("      JOIN      pais                      ps_fd  ON  uf_fd.id_pais = ps_fd.id_pais \n");
		sb.append("      JOIN      operacao_servico_localiza osl_fd ON  osl_fd.id_municipio_filial = mf_fd.id_municipio_filial \n");
		sb.append("      JOIN      regional_filial           rf_fd  ON  fd.id_filial = rf_fd.id_filial \n");
		sb.append("      JOIN      regional                  r_fd   ON  rf_fd.id_regional = r_fd.id_regional \n");
		sb.append("      JOIN      historico_filial          h_fd   ON  fd.id_filial = h_fd.id_filial \n");
		sb.append("      WHERE NVL( s.bl_gera_mcd, 'S' ) = 'S' \n");
		sb.append("      AND   NVL( fr.tp_sistema, 'LMS' ) = 'LMS' \n");
		sb.append(" \n");
		sb.append("      AND   mf_fo.bl_padrao_mcd = 'S' \n");
		sb.append("      AND   osl_fo.tp_operacao IN ( 'A', 'C' ) \n");
		sb.append("      AND   fo.tp_sistema = 'LMS' \n");
		sb.append(" \n");
		sb.append("      AND   ( mf_fd.bl_padrao_mcd = 'S' OR h_fd.tp_filial IN ( 'LO' ) ) \n");
		sb.append("      AND   osl_fd.tp_operacao IN ( 'A', 'E' ) \n");
		sb.append("      AND   fd.tp_sistema = 'LMS' \n");
		sb.append(" \n");
		sb.append("      AND   p.dt BETWEEN ff.dt_vigencia_inicial AND ff.dt_vigencia_final \n");
		sb.append(" \n");
		sb.append("      AND   p.dt BETWEEN h_fo.dt_real_operacao_inicial AND h_fo.dt_real_operacao_final \n");
		sb.append("      AND   p.dt BETWEEN mf_fo.dt_vigencia_inicial AND mf_fo.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN osl_fo.dt_vigencia_inicial AND osl_fo.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN ep_fo.dt_vigencia_inicial AND ep_fo.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN r_fo.dt_vigencia_inicial AND r_fo.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN rf_fo.dt_vigencia_inicial AND rf_fo.dt_vigencia_final \n");
		sb.append(" \n");
		sb.append("      AND   p.dt BETWEEN h_fd.dt_real_operacao_inicial AND h_fd.dt_real_operacao_final \n");
		sb.append("      AND   p.dt BETWEEN mf_fd.dt_vigencia_inicial AND mf_fd.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN osl_fd.dt_vigencia_inicial AND osl_fd.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN ep_fd.dt_vigencia_inicial AND ep_fd.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN r_fd.dt_vigencia_inicial AND r_fd.dt_vigencia_final \n");
		sb.append("      AND   p.dt BETWEEN rf_fd.dt_vigencia_inicial AND rf_fd.dt_vigencia_final \n");
		sb.append(" \n");
		sb.append("      --Filtros \n");
		sb.append("      AND   NVL( p.id_serv, -1 ) IN ( -1, ff.id_servico ) \n");
		sb.append(" \n");
		sb.append("      AND   NVL( p.id_reg_o, -1 ) IN ( -1, r_fo.id_regional ) \n");
		sb.append("      AND   NVL( p.id_mun_o, -1 ) IN ( -1, m_fo.id_municipio ) \n");
		sb.append("      AND   NVL( p.id_uf_o, -1 )  IN ( -1, uf_fo.id_unidade_federativa ) \n");
		sb.append("      AND   NVL( p.id_fo, -1 )    IN ( -1, fo.id_filial ) \n");
		sb.append(" \n");
		sb.append("      AND   NVL( p.id_reg_d, -1 ) IN ( -1, r_fd.id_regional ) \n");
		sb.append("      AND   NVL( p.id_mun_d, -1 ) IN ( -1, m_fd.id_municipio ) \n");
		sb.append("      AND   NVL( p.id_uf_d, -1 )  IN ( -1, uf_fd.id_unidade_federativa ) \n");
		sb.append("      AND   NVL( p.id_fd, -1 )    IN ( -1, fd.id_filial ) \n");
		sb.append(") tb \n");
		sb.append("ORDER  BY id_servico        ASC \n");
		sb.append(",         nm_filial_origem  ASC \n");
		sb.append(",         nm_filial_destino ASC \n");

		SqlTemplate sqlTemplate = new SqlTemplate(sb.toString());
		sqlTemplate.addCriteriaValue(values.toArray());
		return sqlTemplate;
	}

	private static String expressao(String string, Object e, List<Object> values) {
		if(e!=null){
			values.add(e);
			return string;
		}else{
			return string.replaceAll("[?]", "null");
		}
	}

	/**
	 * Monta o SQL para consulta de MCD.
	 * 
	 * @param dados
	 * @return
	 */
	private SqlTemplate getSqlTemplate(MCDParam dados) {
		return getSqlTemplate(new SqlTemplate(), dados);
	}

	/**
	 * Monta o SQL para consulta de MCD.
	 * 
	 * @param sql
	 *            instância de SQLTemplate. Este parâmetro é esperado pois em
	 *            reports, um SQLTemplate é obtido através de
	 *            createSqlTemplate().
	 * @param mcdParam
	 * @return
	 */
	public static final SqlTemplate getSqlTemplate(SqlTemplate sql, MCDParam mcdParam) {
		if (sql == null) {
			throw new IllegalArgumentException("Argumento de indice 0 é obrigatório.");
		}

		SqlTemplate sqlt = getSqlBasic(mcdParam);
		sql.addCriteriaValue(sqlt.getCriteria());
		sql.add(sqlt.toString());

		return sql;
	}

	/**
	 * Find Paginated da tela de consulta de MCD.
	 * 
	 * @param dados
	 *            MCDParam classe contendo os parâmtros aceitáveis.
	 * @param pageNumber
	 *            número da página atual na grid.
	 * @param pageSize
	 *            número de registros a aparecer em cada página.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginatedCustom(MCDParam dados, Integer pageNumber, Integer pageSize) {
		SqlTemplate sql = getSqlTemplate(dados);

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {

			/**
			 * Quando alterado algum dos indices abaixo deve ser verificado as seguintes
			 * classes, as duas devem refletir as mesmas posições.
			 * 
			 * com.mercurio.lms.municipios.model.param.MCDParam
			 * com.mercurio.lms.municipios.model.dao.ConsultarMCDDAO
			 */
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("BL_DISTRITO_DESTINO", Hibernate.STRING); // 0
				sqlQuery.addScalar("BL_DISTRITO_ORIGEM", Hibernate.STRING); // 1
				sqlQuery.addScalar("BL_DOMINGO_DESTINO", Hibernate.STRING); // 2
				sqlQuery.addScalar("BL_DOMINGO_ORIGEM", Hibernate.STRING); // 3
				sqlQuery.addScalar("BL_QUARTA_DESTINO", Hibernate.STRING); // 4
				sqlQuery.addScalar("BL_QUARTA_ORIGEM", Hibernate.STRING); // 5
				sqlQuery.addScalar("BL_QUINTA_DESTINO", Hibernate.STRING); // 6
				sqlQuery.addScalar("BL_QUINTA_ORIGEM", Hibernate.STRING); // 7
				sqlQuery.addScalar("BL_SABADO_DESTINO", Hibernate.STRING); // 8
				sqlQuery.addScalar("BL_SABADO_ORIGEM", Hibernate.STRING); // 9
				sqlQuery.addScalar("BL_SEGUNDA_DESTINO", Hibernate.STRING); // 10
				sqlQuery.addScalar("BL_SEGUNDA_ORIGEM", Hibernate.STRING); // 11
				sqlQuery.addScalar("BL_SEXTA_DESTINO", Hibernate.STRING); // 12
				sqlQuery.addScalar("BL_SEXTA_ORIGEM", Hibernate.STRING); // 13
				sqlQuery.addScalar("BL_TERCA_DESTINO", Hibernate.STRING); // 14
				sqlQuery.addScalar("BL_TERCA_ORIGEM", Hibernate.STRING); // 15
				sqlQuery.addScalar("CD_IBGE_DESTINO", Hibernate.LONG); // 16
				sqlQuery.addScalar("CD_IBGE_ORIGEM", Hibernate.LONG); // 17
				sqlQuery.addScalar("CD_TARIFA_PRECO", Hibernate.STRING); // 18--
				sqlQuery.addScalar("CD_TARIFA_PRECO_ATUAL", Hibernate.STRING); // 19
				sqlQuery.addScalar("DISTANCIA_TOTAL", Hibernate.INTEGER); // 20
				sqlQuery.addScalar("DS_SERVICO", Hibernate.STRING); // 21
				sqlQuery.addScalar("DT_VIGENCIA_FINAL", Hibernate.DATE); // 22
				sqlQuery.addScalar("DT_VIGENCIA_INICIAL", Hibernate.DATE); // 23
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG); // 24
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG); // 25
				sqlQuery.addScalar("ID_FILIAL_REEMBARCADORA", Hibernate.LONG); // 26
				sqlQuery.addScalar("ID_MUNICIPIO_DESTINO", Hibernate.LONG); // 27
				sqlQuery.addScalar("ID_MUNICIPIO_FILIAL_DESTINO", Hibernate.LONG); // 28
				sqlQuery.addScalar("ID_MUNICIPIO_FILIAL_ORIGEM", Hibernate.LONG); // 29
				sqlQuery.addScalar("ID_MUNICIPIO_ORIGEM", Hibernate.LONG); // 30
				sqlQuery.addScalar("ID_PAIS_DESTINO", Hibernate.LONG); // 31
				sqlQuery.addScalar("ID_PAIS_ORIGEM", Hibernate.LONG); // 32
				sqlQuery.addScalar("ID_SERVICO", Hibernate.INTEGER); // 33
				//sqlQuery.addScalar("ID_SERVICO_DESTINO", Hibernate.LONG); // 34--
				//sqlQuery.addScalar("ID_SERVICO_ORIGEM", Hibernate.LONG); // 35--
				sqlQuery.addScalar("ID_TIPO_LOCALIZACAO_DESTINO", Hibernate.LONG); // 36
				sqlQuery.addScalar("ID_TIPO_LOCALIZACAO_ORIGEM", Hibernate.LONG); // 37
				sqlQuery.addScalar("ID_UNIDADE_FEDERATIVA_DESTINO", Hibernate.LONG); // 38
				sqlQuery.addScalar("ID_UNIDADE_FEDERATIVA_ORIGEM", Hibernate.LONG); // 39
				sqlQuery.addScalar("ID_ZONA_DESTINO", Hibernate.LONG); // 40
				sqlQuery.addScalar("ID_ZONA_ORIGEM", Hibernate.LONG); // 41
				sqlQuery.addScalar("NM_FILIAL_DESTINO", Hibernate.STRING); // 42
				sqlQuery.addScalar("NM_FILIAL_ORIGEM", Hibernate.STRING); // 43
				sqlQuery.addScalar("NM_FILIAL_REEMBARCADORA", Hibernate.STRING); // 44
				sqlQuery.addScalar("NM_MUNICIPIO_DESTINO", Hibernate.STRING); // 45
				sqlQuery.addScalar("NM_MUNICIPIO_ORIGEM", Hibernate.STRING); // 46
				sqlQuery.addScalar("NM_PAIS_DESTINO", Hibernate.STRING); // 47
				sqlQuery.addScalar("NM_PAIS_ORIGEM", Hibernate.STRING); // 48
				sqlQuery.addScalar("NM_UNIDADE_FEDERATIVA_DESTINO", Hibernate.STRING); // 49
				sqlQuery.addScalar("NM_UNIDADE_FEDERATIVA_ORIGEM", Hibernate.STRING); // 50
				sqlQuery.addScalar("NR_DIAS_ATENDIDOS_DESTINO", Hibernate.INTEGER); // 51
				sqlQuery.addScalar("NR_DIAS_ATENDIDOS_ORIGEM", Hibernate.INTEGER); // 52
				sqlQuery.addScalar("NR_PPE", Hibernate.INTEGER); // 53
				sqlQuery.addScalar("NR_PRAZO_TOTAL", Hibernate.INTEGER); // 54
				sqlQuery.addScalar("NR_TEMPO_COLETA", Hibernate.INTEGER); // 55
				sqlQuery.addScalar("NR_TEMPO_ENTREGA_DESTINO", Hibernate.INTEGER); // 56
				sqlQuery.addScalar("NR_TEMPO_ENTREGA_ORIGEM", Hibernate.INTEGER); // 57
				sqlQuery.addScalar("QT_PEDAGIO", Hibernate.INTEGER); // 58
				sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING); // 59
				sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING); // 60
				sqlQuery.addScalar("SG_REGIONAL_DESTINO", Hibernate.STRING); // 61
				sqlQuery.addScalar("SG_REGIONAL_ORIGEM", Hibernate.STRING); // 62
				sqlQuery.addScalar("SG_FILIAL_REEMBARCADORA", Hibernate.STRING); // 63
				sqlQuery.addScalar("SG_UNIDADE_FEDERATIVA_DESTINO", Hibernate.STRING); // 64
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), pageNumber, pageSize, sql.getCriteria(), csq);
    }

	/**
	 * Retorna número de registros da consulta de MCD.
	 * 
	 * @param dados
	 * @return
	 */
    public Integer getRowCountCustom(MCDParam dados) {
    	SqlTemplate sql = getSqlTemplate(dados);

		/*
		 * Como o FRW não é preparado para SQL com sub-select, foi necessário
		 * adicionar este select para que o substring seja feito no local
		 * correto (from).
		 */
    	Integer value = getAdsmHibernateTemplate().getRowCountBySql("Select * from ("+sql.toString()+")",sql.getCriteria());

		return  value;
	}

	public static void main(String[] args) {
		MCDParam mcdParam = new MCDParam();
		mcdParam.setDtVigencia(new YearMonthDay(2011, 8, 31));
		mcdParam.setIdFilialOrigem(370L);
		mcdParam.setIdFilialDestino(356L);
		mcdParam.setIdMunicipioOrigem(2114L);
		mcdParam.setIdMunicipioDestino(6457L);
		mcdParam.setTpEmissao("D");

		SqlTemplate sqlt = getSqlBasic(mcdParam);

		System.out.println(sqlt.getSql());
		Object[] criteria = sqlt.getCriteria();
		for (Object object : criteria) {
			System.out.println(String.valueOf(object));
		}
	}

}