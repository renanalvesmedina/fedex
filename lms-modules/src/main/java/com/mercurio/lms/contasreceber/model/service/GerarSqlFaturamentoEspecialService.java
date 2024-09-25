package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlGroupByFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;


public class GerarSqlFaturamentoEspecialService extends GerarSqlFaturamentoService {
	/*
	Caso dom.tp_campo
		Seja “CD” //Cliente destinatário
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo doc.id_cliente_destinatario
		Seja “CR” //Cliente remetente
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo doc.id_cliente_remetente
		Seja “DE” //Data de emissão do documento de serviço
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo trunc(doc.dh_emissao)
		Seja “FO” //Filial de origem
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo doc.id_filial_origem
		Seja “FD” //Filial de destino
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo doc.id_filial_destino
		Seja “NM” //Natureza da mercadoria
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo ctrc.id_natureza_produto
		Seja “AI” //Alíquota ICMS
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo doc.pc_aliquota_icms
		Seja “TO” //Tipo de operação
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo ctrc.tp_conhecimento
		Seja “UO” //UF de origem
			Incluir um relacionamento com o endereço da filial de origem (doc.id_filial_origem) com endereço e municipio (munori)
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo munori.id_unidade_federativa
		Seja “UD” //UF de destino
			Incluir um relacionamento com o endereço da filial de origem (doc.id_filial_destino) com endereço e municipio (mundes)
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo mundes.id_unidade_federativa			
		Seja “CS” //Complemento documento serviço				
			Incluir um relacionamento com a tabela dados_complemento (dados) relacionamento com a tabela conhecimento (ctrc) conforme o 			dom.id_informacao_doc_servico ou dom.id_informacao_docto_cliente
			Incluir na “projection” e no “group by” do SQL principal do faturamento o campo dados.ds_valor_campo
	Fim caso
	 
	 */
	protected void mountSqlSpecific(SqlTemplate sql, SqlFaturamentoParam param) {
		sql.addCustomCriteria(" NOT EXISTS( SELECT 1 FROM LIBERACAO_DOC_SERV WHERE LIBERACAO_DOC_SERV.TP_BLOQUEIO_LIBERADO = '"+ ConstantesExpedicao.LIBERACAO_CALCULO_CORTESIA +"' AND LIBERACAO_DOC_SERV.ID_DOCTO_SERVICO = doc.id_docto_servico ) ");
		
		sql.addCustomCriteria("(doc.TP_CALCULO_PRECO != '"+  ConstantesExpedicao.CALCULO_CORTESIA + "' or doc.TP_CALCULO_PRECO is null)");
		
		//Por cada agrupadoa, modificar
		for (int i = 0; i < param.getLstAgrupador().size(); i++) {

			LinhaSqlGroupByFaturamentoParam linha = (LinhaSqlGroupByFaturamentoParam)param.getLstAgrupador().get(i);

			getClienteDestinatario(sql, linha);
			getClienteRemetente(sql, linha);
			getDataEmissao(sql, linha);
			getFilialOrigem(sql, linha);
			getFilialDestino(sql, linha);
			getNaturezaMercadoria(sql, linha);
			getAliquotaICMS(sql, linha);
			getTipoOperacao(sql, linha);
			getBlIdentificadorEDI(sql, linha);
			getUfOrigem(sql, linha);
			getUfDestino(sql, linha);
			getComplemento(sql, linha, i);		
		}

			sql.addOrderBy("trunc(doc.dh_emissao)");
		}


	/**
	 * Busca da tabela dados complementos
	 */
	private void getComplemento(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha, int i) {
		//Complemento documento serviço
		if (linha.getTpCampo().equals("CS")){
			sql.addProjection("dados"+i+".ds_valor_campo");
			
			sql.addFrom("dados_complemento", "dados"+i);
			sql.addJoin("ctrc.id_conhecimento", "dados"+i+".id_conhecimento(+)");

			if (linha.getIdInformacaoDocServico() != 0){
				sql.addCustomCriteria("nvl(dados"+i+".ID_INFORMACAO_DOC_SERVICO, ?) = ? ", linha.getIdInformacaoDocServico());
				sql.addCriteriaValue(linha.getIdInformacaoDocServico());
			}
			
			if (linha.getIdInformacaoDoctoCliente() != 0){
				sql.addCustomCriteria("nvl(dados"+i+".ID_INFORMACAO_DOCTO_CLIENTE, ?) = ? ", linha.getIdInformacaoDoctoCliente());
				sql.addCriteriaValue(linha.getIdInformacaoDoctoCliente());
			}
			
			sql.addOrderBy("dados"+i+".ds_valor_campo");
		}
		
		
	}

	private void getUfDestino(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//UF destino
		if (linha.getTpCampo().equals("UD")){
			sql.addProjection("mundes.id_unidade_federativa");
			
			sql.addFrom("filial", "fildes");
			sql.addFrom("pessoa", "despes");
			sql.addFrom("endereco_pessoa", "enddes");
			sql.addFrom("municipio", "mundes");
			
			sql.addJoin("doc.id_filial_destino", "fildes.id_filial(+)");
			sql.addJoin("fildes.id_filial", "despes.id_pessoa(+)");
			sql.addJoin("despes.id_endereco_pessoa", "enddes.id_endereco_pessoa(+)");
			sql.addJoin("enddes.id_municipio", "mundes.id_municipio(+)");			
			
			sql.addOrderBy("mundes.id_unidade_federativa");
		}
	}

	private void getUfOrigem(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//UF Origem
		if (linha.getTpCampo().equals("UO")){
			sql.addProjection("munori.id_unidade_federativa");
			
			sql.addFrom("filial", "filori");
			sql.addFrom("pessoa", "oripes");
			sql.addFrom("endereco_pessoa", "endori");
			sql.addFrom("municipio", "munori");
			
			sql.addJoin("doc.id_filial_origem", "filori.id_filial");
			sql.addJoin("filori.id_filial", "oripes.id_pessoa(+)");
			sql.addJoin("oripes.id_endereco_pessoa", "endori.id_endereco_pessoa");
			sql.addJoin("endori.id_municipio", "munori.id_municipio");

			sql.addOrderBy("munori.id_unidade_federativa");
		}
	}

	private void getTipoOperacao(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Tipo operação
		if (linha.getTpCampo().equals("TO")){
			sql.addProjection("ctrc.tp_conhecimento");
			// A linha abaixo comentada é utilizada para testar a ordem de prioridade da forma de agrupamento
			sql.addOrderBy("ctrc.tp_conhecimento");
		}
	}

	private void getBlIdentificadorEDI(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Conhecimentos de EDI
		if (linha.getTpCampo().equals("ED")){
			sql.addProjection("ctrc.bl_indicador_edi");
			sql.addOrderBy("ctrc.bl_indicador_edi");
		}		
	}		

	private void getAliquotaICMS(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Alíquota ICMS
		if (linha.getTpCampo().equals("AI")){
			sql.addProjection("doc.pc_aliquota_icms");
			sql.addOrderBy("doc.pc_aliquota_icms");
		}
	}

	private void getNaturezaMercadoria(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Natureza mercadoria
		if (linha.getTpCampo().equals("NM")){
			sql.addProjection("ctrc.id_natureza_produto");
			// A linha abaixo comentada é utilizada para testar a ordem de prioridade da forma de agrupamento
			sql.addOrderBy("ctrc.id_natureza_produto");
		}
	}

	private void getFilialDestino(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Filial destino
		if (linha.getTpCampo().equals("FD")){
			sql.addProjection("doc.id_filial_destino");
			sql.addOrderBy("doc.id_filial_destino");
		}
	}
	
	private void getFilialOrigem(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Filial origem
		if (linha.getTpCampo().equals("FO")){
			sql.addProjection("doc.id_filial_origem");
			sql.addOrderBy("doc.id_filial_origem");
		}
	}

	private void getDataEmissao(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Data emissão do documento serviço
		if (linha.getTpCampo().equals("DE")){
			sql.addProjection("trunc(doc.dh_emissao)");
		}
	}

	private void getClienteRemetente(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Cliente remetente
		if (linha.getTpCampo().equals("CR")){
			sql.addProjection("doc.id_cliente_remetente");
			sql.addOrderBy("doc.id_cliente_remetente");
		}
	}

	private void getClienteDestinatario(SqlTemplate sql, LinhaSqlGroupByFaturamentoParam linha) {
		//Cliente destinatário
		if (linha.getTpCampo().equals("CD")){
			sql.addProjection("doc.id_cliente_destinatario");
			sql.addOrderBy("doc.id_cliente_destinatario");
		}
	}
}