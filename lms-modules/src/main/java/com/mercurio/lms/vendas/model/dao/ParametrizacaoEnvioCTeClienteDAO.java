package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.vendas.model.ParametrizacaoEnvioCTeCliente;
import com.mercurio.lms.vendas.util.ConstantesVendas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class ParametrizacaoEnvioCTeClienteDAO extends BaseCrudDao<ParametrizacaoEnvioCTeCliente, Long> {

    @Override
    protected Class getPersistentClass() {
        return ParametrizacaoEnvioCTeCliente.class;
    }

    public ParametrizacaoEnvioCTeCliente find(String nrIdentificacao, String dsDiretorioArmazenagem, String tpPesquisa) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> parameters = new HashMap<String, Object>();
        sb.append("from ").append(getPersistentClass().getName()).append( " as p ");
        sb.append("WHERE p.nrIdentificacao LIKE :nrIdentificacao ");
        sb.append("and p.dsDiretorioArmazenagem LIKE :dsDiretorioArmazenagem ");
        sb.append("and p.tpPesquisa LIKE :tpPesquisa ");

        parameters.put("nrIdentificacao", nrIdentificacao);
        parameters.put("dsDiretorioArmazenagem", dsDiretorioArmazenagem);
        parameters.put("tpPesquisa", tpPesquisa);

        List<ParametrizacaoEnvioCTeCliente> result = getAdsmHibernateTemplate().findByNamedParam(sb.toString(), parameters);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<ParametrizacaoEnvioCTeCliente> findBy
            (
                String tpCnpj, String nrIdentificacao,
                String dsDiretorioArmazenagem, String tpPesquisa,
                Boolean blAtivo
            ) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> parameters = new HashMap<String, Object>();
        sb.append("from ").append(getPersistentClass().getName()).append( " as p ");
        sb.append("WHERE p.blAtivo = :blAtivo ");

        parameters.put("blAtivo", blAtivo);

        if(nrIdentificacao != null && !nrIdentificacao.equals("")) {
            if(tpCnpj.equals("Parcial")){
                nrIdentificacao = nrIdentificacao + "%";
                sb.append("and p.nrIdentificacao LIKE :nrIdentificacao ");
            } else {
                sb.append("and p.nrIdentificacao LIKE :nrIdentificacao and LENGTH(NR_IDENTIFICACAO) = 14");
            }

            parameters.put("nrIdentificacao", nrIdentificacao);
        }

        if(dsDiretorioArmazenagem != null && !dsDiretorioArmazenagem.equals("")) {
            sb.append("and upper(p.dsDiretorioArmazenagem) LIKE :dsDiretorioArmazenagem ");
            parameters.put("dsDiretorioArmazenagem", "%" + dsDiretorioArmazenagem.toUpperCase() + "%");
        }

        if(tpPesquisa != null && !tpPesquisa.equals("")) {
            sb.append("and p.tpPesquisa = :tpPesquisa ");
            parameters.put("tpPesquisa", tpPesquisa);
        }

        List<ParametrizacaoEnvioCTeCliente> result = getAdsmHibernateTemplate().findByNamedParam(sb.toString(), parameters);
        return !result.isEmpty() ? result : null;
    }

    public String findNomeByCNPJParcial(String nrCNPJParcial){
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(Pessoa.class.getName()).append( " as p ");
        nrCNPJParcial = nrCNPJParcial + "%";
        sb.append("WHERE p.nrIdentificacao LIKE :nrIdentificacao ");
        sb.append("and p.tpIdentificacao = 'CNPJ' ");
        sb.append("and ROWNUM < 2");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("nrIdentificacao", nrCNPJParcial);

        List<Pessoa> result = getAdsmHibernateTemplate().findByNamedParam(sb.toString(), parameters);

        return !result.isEmpty() ? result.get(0).getNmFantasia() : null;
    }

    public String findNomeByCNPJCompleto(String nrIdentificacao) {
        StringBuilder sb = new StringBuilder();
        sb.append("from ").append(Pessoa.class.getName()).append( " as p ");
        sb.append("WHERE p.nrIdentificacao = :nrIdentificacao ");
        sb.append("and p.tpIdentificacao = 'CNPJ' ");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("nrIdentificacao", nrIdentificacao);

        List<Pessoa> result = getAdsmHibernateTemplate().findByNamedParam(sb.toString(), parameters);

        return !result.isEmpty() ? result.get(0).getNmFantasia() : null;
    }

    public List<Map<String, Object>> findDsDiretorioArmazenagem(String nrIdentificaoDestinatario,
            String nrIdentificaoRemetente, String nrIdentificaoTomador) {

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT new Map(");
        hql.append(" pecc.dsDiretorioArmazenagem AS dsDiretorioArmazenagem) ");
        hql.append("FROM ").append(getPersistentClass().getName()).append(" AS pecc ");
        hql.append("WHERE pecc.blAtivo = 'S' ");
        hql.append("AND ((substr(pecc.nrIdentificacao, 1, 8) = ? and pecc.tpPesquisa = ? ) ");
        hql.append("OR (substr(pecc.nrIdentificacao, 1, 8) = ?  and pecc.tpPesquisa = ? )  ");
        hql.append("OR (substr(pecc.nrIdentificacao, 1, 8) = ?  and pecc.tpPesquisa = ? )) ");
        
        Object[] param = new Object[]
                                {
                                    nrIdentificaoDestinatario, ConstantesVendas.REMDEST,
                                    nrIdentificaoRemetente, ConstantesVendas.REMDEST,
                                    nrIdentificaoTomador, ConstantesVendas.TOM
                                };

        return getAdsmHibernateTemplate().find(hql.toString(), param);
    }
}