<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%-- Parametro utilizado por rotaPreco.jsp --%>
<% Boolean blActiveValues = false; %>
<adsm:window service="lms.vendas.manterParametrosClienteAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01024"/>
		<adsm:include key="LMS-30017"/>
	</adsm:i18nLabels>

	<adsm:form 
		action="/vendas/manterParametrosCliente" 
		height="140"
	>

		<adsm:hidden property="idParametroClienteRef"/>
		<adsm:hidden property="tpAcesso" value="A"/>
		<adsm:hidden property="origem"/>
		<adsm:hidden property="idDivisaoCliente"/>
		<adsm:hidden property="dsDivisaoCliente"/>

		<adsm:hidden property="idTabelaDivisaoCliente"/>
		<adsm:hidden property="tipoTabelaPreco"/>
		<adsm:hidden property="nrVersao"/>
		<adsm:hidden property="subtipoTabelaPreco"/>
		<adsm:hidden property="dsDescricao"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimboloMoeda"/>
		<adsm:hidden property="dsServico"/>
		
		<%-------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
			label="cliente"
			property="tabelaDivisaoCliente.divisaoCliente.cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterParametrosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="16%"
			maxLength="20"
			size="20"
			width="45%"
			required="true"
			afterPopupSetValue="afterPopupCliente"
		>
			<adsm:propertyMapping 
				relatedProperty="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30" 
			/>
		</adsm:lookup>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:hidden
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			serializable="false"
		/>
		<adsm:combobox
			property="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			service="lms.vendas.manterParametrosClienteAction.findDivisaoCombo"
			labelWidth="10%"
			width="29%"
			label="divisao"
			boxWidth="140"
		>
			<adsm:propertyMapping
				relatedProperty="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
				modelProperty="dsDivisaoCliente"
			/>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.divisaoCliente.cliente.idCliente"
				modelProperty="cliente.idCliente"
			/>
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA COMBO --%>
		<%-------------------%>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao" serializable="false"/>

		<adsm:combobox
			label="tabela"
			labelWidth="16%"
			optionLabelProperty="tabelaPreco.tabelaPrecoStringDescricao"
			optionProperty="idTabelaDivisaoCliente"
			property="tabelaDivisaoCliente.idTabelaDivisaoCliente"
			service="lms.vendas.manterParametrosClienteAction.findTabelaDivisaoClienteCombo"
			width="45%"
			boxWidth="330"
			onchange="tabelaDivisaoClienteOnChange();"
			onDataLoadCallBack="tabelaDivisaoClienteDataLoad"
		>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente"
				modelProperty="divisaoCliente.idDivisaoCliente"
			/>
		</adsm:combobox>
		
		<adsm:hidden
			property="idSubtipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden 
			property="idTipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" 
			serializable="false"
		/>
		<adsm:hidden
			property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" 
			serializable="false"
		/>

		<%-- Include do JSP que contem os campos das rotas de origem e destino --%>
		<%@ include file="../tabelaPrecos/rotaPreco.jsp" %>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
				callbackProperty="parametrosCliente"
			/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		idProperty="idParametroCliente" 
		gridHeight="125" 
		property="parametrosCliente" 
		unique="true" 
		scrollBars="horizontal" 
		rows="6"
		showPagging="true"
		onRowClick="myOnRowClick"
		showTotalPageCount="true"
		rowCountService="lms.vendas.manterParametrosClienteAction.getRowCount"
		service="lms.vendas.manterParametrosClienteAction.findPaginated"
	>

		<adsm:gridColumn title="zonaOrigem" property="zonaByIdZonaOrigem.dsZona" width="150" />
		<adsm:gridColumn title="paisOrigem" property="paisByIdPaisOrigem.nmPais" width="150" />
		<adsm:gridColumn title="ufOrigem2" property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialOrigem" property="filialByIdFilialOrigem.sgFilial" width="50" />
		<adsm:gridColumn title="municipioOrigem" property="municipioByIdMunicipioOrigem.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoOrigem" property="aeroportoByIdAeroportoOrigem.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoOrigem" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" width="100" />

		<adsm:gridColumn title="zonaDestino" property="zonaByIdZonaDestino.dsZona" width="150" />
		<adsm:gridColumn title="paisDestino" property="paisByIdPaisDestino.nmPais" width="150" />
		<adsm:gridColumn title="ufDestino2" property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="50" />
		<adsm:gridColumn title="municipioDestino" property="municipioByIdMunicipioDestino.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoDestino" property="aeroportoByIdAeroportoDestino.sgAeroporto" width="80" />
		<adsm:gridColumn title="localizacaoDestino" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" width="100" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript" src="../lib/rotaPreco.js"></script>
<script language="javascript" type="text/javascript">

document.getElementById("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao").serializable="false";


function tabelaDivisaoClienteOnChange() {
	setElementValue("idTabelaDivisaoCliente", getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
}

function tabelaDivisaoClienteDataLoad_cb(data, error) {
	tabelaDivisaoCliente_idTabelaDivisaoCliente_cb(data);
	if (data.length == 1) {
		setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", data[0].idTabelaDivisaoCliente);
	}
}

function afterPopupCliente(data) {
	setElementValue("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
}

function myOnRowClick(pk){
	if (pk != undefined){
		document.getElementById("idParametroClienteRef").value = pk;
		disableTabs(false);
		getTabGroup(document).selectTab("cad", {name:"gridRow_click2"}, false);
	}
	//Importante retornar false para não chamar o onDataLoad
	return false;
}

function disableTabs(disabled){
	getTabGroup(document).setDisabledTab("cad",disabled);
	getTabGroup(document).setDisabledTab("generalidades",disabled);
	getTabGroup(document).setDisabledTab("taxas",disabled);
}

function initWindow(event){
	if (event.name == "tab_click"){
		document.getElementById("idParametroClienteRef").value = "";
		disableTabs(true);
	}
}

</script>