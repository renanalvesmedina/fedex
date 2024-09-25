<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.visualizarHistoricoNegociacoesClientesAction">
	<adsm:form action="/vendas/visualizarHistoricoNegociacoesClientes" 
		idProperty="idTaxaCliente" service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findTaxasById">
		
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
		
		<%----------------%>
		<%-- TAXA COMBO --%>
		<%----------------%>
		<adsm:textbox 
			dataType="text"
			disabled="true"
			label="taxa"
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
			property="tpTaxaIndicador"
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
			width="18%" 
			size="15"
		/>
		
		<%-----------%>
		<%-- VALOR --%>
		<%-----------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlTaxa" 
			label="valor" 
			disabled="true" 
			mask="##,###,###,###,##0.00" 
			size="18" 
			labelWidth="13%" 
			width="23%" />
			
		<%------------------------%>
		<%-- REAJUSTE TAXA TEXT --%>
		<%------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajTaxa" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="12%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<%-----------------------%>
		<%-- PRESO MINIMO TEXT --%>
		<%-----------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="psMinimo" 
			label="pesoMinimo" 
			mask="#,###,###,###,##0.000"
			size="15" 
			labelWidth="18%" 
			width="18%" 
			unit="kg" 
			disabled="true"
		/>
		
		<%--------------------------%>
		<%-- VALOR EXCEDENTE TEXT --%>
		<%--------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlExcedente" 
			label="valorExcedente" 
			mask="##,###,###,###,##0.00" 
			size="18" 
			labelWidth="13%" 
			width="23%" 
			disabled="true"
		/>
		
		<%-----------------------------------%>
		<%-- REAJUSTE VALOR EXCEDENTE TEXT --%>
		<%-----------------------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="pcReajVlExcedente" 
			label="percentualReajuste" 
			labelWidth="14%" 
			width="12%" 
			mask="##0.00"
			size="5" 
			disabled="true" 
		/>
		
		<adsm:buttonBar freeLayout="true"/>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idTaxaCliente" 
		property="taxaCliente" 
		service="lms.vendas.visualizarHistoricoNegociacoesClientesAction.findPaginatedTaxas"
		rowCountService="lms.vendas.visualizarHistoricoNegociacoesClientesAction.getRowCountTaxas"
		autoSearch="false"
		selectionMode="none"
		gridHeight="170" 
		unique="true" 
		rows="7"
		detailFrameName="tax">
		
		<adsm:gridColumn title="taxa" property="parcelaPreco.nmParcelaPreco" />
		<adsm:gridColumn title="indicador" property="tpTaxaIndicador" width="10%" isDomain="true" />
		<adsm:gridColumn title="valorIndicador" property="valor" align="right" width="15%" />
		<adsm:gridColumn title="reajuste" property="pcReajTaxa" mask="##0.00" dataType="decimal" align="right" width="11%" unit="percent"/>
		<adsm:gridColumn title="pesoMinimoKg" property="psMinimo" align="right" width="15%" mask="#,###,###,###,##0.000" dataType="decimal"/>
		<adsm:gridColumn title="valorExcedenteReal" property="vlExcedente" mask="##,###,###,###,##0.00" dataType="decimal" align="right" width="19%"/>
		<adsm:gridColumn title="reajuste" property="pcReajVlExcedente" mask="##0.00" dataType="decimal" align="right" width="11%" unit="percent"/>
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
	taxaClienteGridDef.executeSearch({parametroCliente:{idParametroCliente:getElementValue("parametroCliente.idParametroCliente")}}, true); 
}

// funcao para limpar os registros ao esconder a aba
function hide() {
	newButtonScript();
	tab_onHide();
	var frame = parent.document.frames["pesq_iframe"];
	frame.setOrigem("detail");
	return true;
}

</script>
