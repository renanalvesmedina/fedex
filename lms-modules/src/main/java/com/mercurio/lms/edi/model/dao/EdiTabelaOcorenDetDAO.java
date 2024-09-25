package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.edi.model.EdiTabelaOcorenDet;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdiTabelaOcorenDetDAO extends BaseCrudDao<EdiTabelaOcorenDet, Long> {
    @Override
    protected Class getPersistentClass() {
        return EdiTabelaOcorenDet.class;
    }

    public List<EdiTabelaOcorenDet> findByIdEdiTabelaOcorenDetAndIdEdiTabelaOcoren
        (Long idEdiTabelaOcorenDet, Long idEdiTabelaOcoren){

        StringBuilder sql = new StringBuilder();
        sql.append(" from " + getPersistentClass().getName() + " as etod ");
        sql.append(" where etod.idEdiTabelaOcorenDet = :idEdiTabelaOcorenDet");
        sql.append(" and etod.ediTabelaOcoren.idEdiTabelaOcoren = :idEdiTabelaOcoren ");

        Map criteria = new HashMap();
        criteria.put("idEdiTabelaOcorenDet", idEdiTabelaOcorenDet);
        criteria.put("idEdiTabelaOcoren", idEdiTabelaOcoren);

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }

    public List<EdiTabelaOcorenDet> findByIdEdiTabelaOcoren(Long idEdiTabelaOcoren){

        StringBuilder sql = new StringBuilder();
        sql.append(" from " + getPersistentClass().getName() + " as etod ");
        sql.append(" where etod.ediTabelaOcoren.idEdiTabelaOcoren = :idEdiTabelaOcoren ");

        Map criteria = new HashMap();
        criteria.put("idEdiTabelaOcoren", idEdiTabelaOcoren);

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }

    public List<Object[]> findCodigoOcorrenciaByTpOcorrencia(String tipoOcorrencia){
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT  ID_LMS AS idlms, CD_OCORRENCIA AS cdOcorrencia, ");
        sql.append("DS_OCORRENCIA AS dsOcorrencia FROM ( ");
        sql.append("    SELECT ID_OCORRENCIA_ENTREGA ID_LMS,");
        sql.append("    CD_OCORRENCIA_ENTREGA CD_OCORRENCIA,");
        sql.append("REPLACE(SUBSTR(DS_OCORRENCIA_ENTREGA_I,1,INSTR(DS_OCORRENCIA_ENTREGA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA");
        sql.append("    FROM OCORRENCIA_ENTREGA");
        sql.append("    WHERE TP_SITUACAO = 'A'");
        sql.append("    AND '1' = :tipoOcorrencia");
        sql.append("  UNION ");
        sql.append("  SELECT ID_OCORRENCIA_PENDENCIA ID_LMS, CD_OCORRENCIA AS CD_OCORRENCIA, ");
        sql.append("    REPLACE(SUBSTR(DS_OCORRENCIA_I,1,INSTR(DS_OCORRENCIA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("    FROM OCORRENCIA_PENDENCIA ");
        sql.append("    WHERE TP_SITUACAO = 'A' ");
        sql.append("    AND '2' = :tipoOcorrencia ");
        sql.append("  UNION  ");
        sql.append("  SELECT EV.ID_EVENTO ID_LMS, ");
        sql.append("  EV.CD_EVENTO CD_OCORRENCIA,  ");
        sql.append("  REPLACE(SUBSTR(DE.DS_DESCRICAO_EVENTO_I,1,INSTR(DE.DS_DESCRICAO_EVENTO_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("  FROM EVENTO EV, DESCRICAO_EVENTO DE ");
        sql.append("  WHERE EV.ID_DESCRICAO_EVENTO = DE.ID_DESCRICAO_EVENTO ");
        sql.append("  AND EV.TP_SITUACAO = 'A' ");
        sql.append("  AND '3' = :tipoOcorrencia ");
        sql.append("  UNION  ");
        sql.append("  SELECT ID_LOCALIZACAO_MERCADORIA ID_LMS, ");
        sql.append("  CD_LOCALIZACAO_MERCADORIA CD_OCORRENCIA,  ");
        sql.append("  REPLACE(SUBSTR(DS_LOCALIZACAO_MERCADORIA_I,1,INSTR(DS_LOCALIZACAO_MERCADORIA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("  FROM LOCALIZACAO_MERCADORIA ");
        sql.append("  WHERE TP_SITUACAO = 'A' ");
        sql.append("  AND '4' = :tipoOcorrencia) ");
        sql.append("  ORDER BY CD_OCORRENCIA ");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tipoOcorrencia", tipoOcorrencia);

        return getAdsmHibernateTemplate().findBySql(sql.toString(), param, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery sql) {
                sql.addScalar("idlms", Hibernate.LONG);
                sql.addScalar("cdOcorrencia", Hibernate.STRING);
                sql.addScalar("dsOcorrencia", Hibernate.STRING);

            }
        });
    }

    public List<Object[]> findCodigoOcorrenciaByTpOcorrenciaAndCdOcorrencia
        (String tipoOcorrencia, Short cdOcorrencia) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT CD_OCORRENCIA AS cdOcorrencia, ");
        sql.append("DS_OCORRENCIA AS dsOcorrencia FROM ( ");
        sql.append("    SELECT CD_OCORRENCIA_ENTREGA CD_OCORRENCIA, ");
        sql.append("REPLACE(SUBSTR(DS_OCORRENCIA_ENTREGA_I,1,INSTR(DS_OCORRENCIA_ENTREGA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA");
        sql.append("    FROM OCORRENCIA_ENTREGA");
        sql.append("    WHERE TP_SITUACAO = 'A'");
        sql.append("    AND '1' = :tipoOcorrencia");
        sql.append("  UNION ");
        sql.append("  SELECT CD_OCORRENCIA AS CD_OCORRENCIA, ");
        sql.append("    REPLACE(SUBSTR(DS_OCORRENCIA_I,1,INSTR(DS_OCORRENCIA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("    FROM OCORRENCIA_PENDENCIA ");
        sql.append("    WHERE TP_SITUACAO = 'A' ");
        sql.append("    AND '2' = :tipoOcorrencia ");
        sql.append("  UNION  ");
        sql.append("  SELECT EV.CD_EVENTO CD_OCORRENCIA,  ");
        sql.append("  REPLACE(SUBSTR(DE.DS_DESCRICAO_EVENTO_I,1,INSTR(DE.DS_DESCRICAO_EVENTO_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("  FROM EVENTO EV, DESCRICAO_EVENTO DE ");
        sql.append("  WHERE EV.ID_DESCRICAO_EVENTO = DE.ID_DESCRICAO_EVENTO ");
        sql.append("  AND EV.TP_SITUACAO = 'A' ");
        sql.append("  AND '3' = :tipoOcorrencia ");
        sql.append("  UNION  ");
        sql.append("  SELECT CD_LOCALIZACAO_MERCADORIA CD_OCORRENCIA,  ");
        sql.append("  REPLACE(SUBSTR(DS_LOCALIZACAO_MERCADORIA_I,1,INSTR(DS_LOCALIZACAO_MERCADORIA_I,'¦')-1),'pt_BR»','') AS DS_OCORRENCIA ");
        sql.append("  FROM LOCALIZACAO_MERCADORIA ");
        sql.append("  WHERE TP_SITUACAO = 'A' ");
        sql.append("  AND '4' = :tipoOcorrencia) ");
        sql.append("  WHERE CD_OCORRENCIA = :cdOcorrencia ");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tipoOcorrencia", tipoOcorrencia);
        param.put("cdOcorrencia", cdOcorrencia);

        return getAdsmHibernateTemplate().findBySql(sql.toString(), param, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery sql) {
                sql.addScalar("cdOcorrencia", Hibernate.STRING);
                sql.addScalar("dsOcorrencia", Hibernate.STRING);

            }
        });
    }
}
