<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.visualizarHistoricoNegociacoesClientesAction">
	<adsm:form action="/vendas/visualizarHistoricoNegociacoesClientes" 
		idProperty="idGeneralidadeCliente" 
		service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findGeneralidadesById">
		
		<adsm:hidden property="parametroCliente.idParametroCliente" />
		
		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:complement 
			label="cliente" 
			labelWidth="18%" 
			width="45%"
			separator="branco"
		>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false" 
				size="18"
				maxLength="18"
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" 
				size="36"
			/>
		</adsm:complement>
		
		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="divisao" 
			labelWidth="130" 
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			serializable="false"
			width="18%" 
		/>
		
		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="tabela" 
			labelWidth="18%" 
			property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"
			serializable="false" 
			size="60" 
			width="78%"
		/>
		
		<%----------------%>
		<%-- ROTAS TEXT --%>
		<%----------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="origem" 
			labelWidth="18%"
			property="rotaPreco.origemString" 
			serializable="false"
			size="85" 
			width="78%" 
		/>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="destino" 
			labelWidth="18%"
			property="rotaPreco.destinoString" 
			serializable="false"
			size="85" 
			width="78%" 
		/>
		
		<%------------------------%>
		<%-- GENERALIDADE COMBO --%>
		<%------------------------%>
		<adsm:textbox 
			dataType="text"
			disabled="true"
			label="generalidade"
			labelWidth="18%"
			width="45%"
			size="30"
			property="parcelaPreco.nmParcelaPreco"
		/>
		
		<%---------------------%>
		<%-- INDICADOR COMBO --%>
		<%---------------------%>
		<adsm:combobox 
			disabled="true" 
			label="indicador" 
			labelWidth="130" 
			property="tpIndicador"
			serializable="false"
			width="18%" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			boxWidth="120"
		/>
		
		<%-----------%>
		<%-- MOEDA --%>
		<%-----------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo" 
			serializable="false"
			label="moeda" 
			labelWidth="18%" 
			width="72%" 
			size="15"
		/>
		
		<%-----------%>
		<%-- VALOR --%>
		<%-----------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlGeneralidade" 
			label="valor" 
			disabled="true" 
			mask="##,###,###,###,##0.00" 
			size="15" 
			labelWidth="18%" 
			width="45%" />
			
		<%--------------------------------%>
		<%-- REAJUSTE GENERALIDADE TEXT --%>
		<%--------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajGeneralidade" 
			label="percentualReajuste" 
			labelWidth="13%" 
			width="13%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>

		<adsm:combobox 
			disabled="true" 
			label="indicadorDoMinimo" 
			labelWidth="18%" 
			property="tpIndicadorMinimo"
			serializable="false"
			width="45%" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			boxWidth="120"
		/>

		<adsm:textbox 
			dataType="decimal" 
			property="vlMinimo" 
			label="valor" 
			disabled="true" 
			mask="##,###,###,###,##0.00" 
			size="15" 
			labelWidth="13%" 
			width="13%" />

		<adsm:textbox 
			dataType="decimal" 
			property="pcReajMinimo" 
			label="percentualReajuste" 
			labelWidth="18%" 
			width="45%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		

		<adsm:buttonBar freeLayout="true" /> 
	</adsm:form>
		
	<adsm:grid 
		autoSearch="false"
		detailFrameName="gen"
		idProperty="idGeneralidadeCliente" 
		gridHeight="170" 
		property="generalidadeCliente" 
		unique="true" 
		rows="5"
		selectionMode="none"
		service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findPaginatedGeneralidades"
		rowCountService="lms.vendas.visualizarHistoricoNegociacoesClientesAction.getRowCountGeneralidades">
		<adsm:gridColumn 
			title="generalidade" 
			property="parcelaPreco.nmParcelaPreco" 
			width="30%" 
		/>
		<adsm:gridColumn 
			title="indicador" 
			property="tpIndicador" 
			width="25%" 
			isDomain="true"
		/>
		<adsm:gridColumn 
			align="right"
			title="valorIndicador" 
			property="valorIndicador" 
			width="25%"
		/>
		<adsm:gridColumn 
			title="reajuste" 
			unit="percent"
			property="pcReajGeneralidade" 
			mask="##0.00" 
			dataType="decimal" 
			align="right" 
			width="20%" 
		/>
		<adsm:buttonBar />
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
function copyValuesFromParam() {
	var frame = parent.document.frames["cad_iframe"];
	var dados = frame.getValuesFromParam();
	copyValues(dados);
}

function copyValues(dados) {
	setElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao", getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"));
	document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao").masterLink = true;
	setElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa", getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"));
	document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa").masterLink = true;
 	setElementValue("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente", getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"));
 	document.getElementById("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente").masterLink = true;
	setElementValue("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao", getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"));
	document.getElementById("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao").masterLink = true;
	setElementValue("rotaPreco.destinoString", getNestedBeanPropertyValue(dados, "rotaPreco.destinoString"));
	document.getElementById("rotaPreco.destinoString").masterLink = true;
	setElementValue("rotaPreco.origemString", getNestedBeanPropertyValue(dados, "rotaPreco.origemString"));
	document.getElementById("rotaPreco.origemString").masterLink = true;
	setElementValue("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo", getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"));
	document.getElementById("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo").masterLink = true;
	setElementValue("parametroCliente.idParametroCliente", getNestedBeanPropertyValue(dados, "parametroCliente.idParametroCliente"));
	document.getElementById("parametroCliente.idParametroCliente").masterLink = true;
}

function initWindow(eventObj) {
	var event = eventObj.name;
	if(event == "tab_click"){
		copyValuesFromParam();
		populaGrid();
	}
}

function populaGrid() {
	generalidadeClienteGridDef.executeSearch({parametroCliente:{idParametroCliente:getElementValue("parametroCliente.idParametroCliente")}}, true); 
}

/**
 * onHide da tab
 * - limpa os campos do detalhe da generalidade
 */
function hide() {
	newButtonScript();
	tab_onHide();
	var frame = parent.document.frames["pesq_iframe"];
	frame.setOrigem("detail");
	return true;
}
</script>
