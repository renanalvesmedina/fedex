<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="findDadosSessao">
	<adsm:form action="/vendas/emitirRelacaoClientesEspeciaisTabelas">
	
		<adsm:hidden property="tabelaPreco.tabelaPrecoStringHidden" />
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" />
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.nrVersao" />
		<adsm:hidden property="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco" />
		<adsm:hidden property="tpAbrangencia.valor" />
		<adsm:hidden property="tpAbrangencia.descricao" />
		<adsm:hidden property="tpModal.valor" />
		<adsm:hidden property="tpModal.descricao" />
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />
		<adsm:hidden property="tpAcesso" value="F"/>
		
		<!-- Lookup de tabelas -->
		<adsm:lookup
			label="tabela" labelWidth="15%" width="34%"
			property="tabelaPreco" 
			service="lms.vendas.emitirRelacaoClientesEspeciaisTabelasAction.findLookupTabelaPreco" 
			action="/tabelaPrecos/manterTabelasPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString" 
			onclickPicker="onclickPickerLookupTabelaPreco()"
			dataType="text"
			size="10"
			maxLength="9"
		>
			<adsm:propertyMapping relatedProperty="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" modelProperty="tipoTabelaPreco.tpTipoTabelaPreco.value"/>
			<adsm:propertyMapping relatedProperty="tabelaPreco.tipoTabelaPreco.nrVersao" modelProperty="tipoTabelaPreco.nrVersao"/>
			<adsm:propertyMapping relatedProperty="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco" modelProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco"/>
			<adsm:propertyMapping relatedProperty="tabelaPreco.dsDescricao" modelProperty="dsDescricao"/>
			<adsm:propertyMapping relatedProperty="tabelaPreco.tabelaPrecoStringHidden" modelProperty="tabelaPrecoString"/>

			<adsm:textbox
				dataType="text"
				property="tabelaPreco.dsDescricao" 
				size="30"
				maxLength="30"
				disabled="true"
			/>
		</adsm:lookup>

		<adsm:textbox dataType="JTDate" property="dataReferencia" required="true" label="dataReferencia" labelWidth="13%" width="38%"/>

		
		<!-- Lookup de filiais -->
		<adsm:lookup
			label="filial" labelWidth="15%" width="34%"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			criteriaSerializable="true"
			action="/municipios/manterFiliais"
			service="lms.vendas.emitirRelacaoClientesEspeciaisTabelasAction.findLookupFilial"
			dataType="text"
			size="5" required="true"
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>

			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				serializable="true"
				size="30"
				maxLength="50"
				disabled="true"
			/>
		</adsm:lookup>
		
		<!-- Lookup de clientes -->
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			action="/vendas/manterDadosIdentificacao" 
			service="lms.vendas.emitirRelacaoClientesEspeciaisTabelasAction.findLookupClientes" 
			dataType="text"
			size="18"
			maxLength="20"
			width="38%"
			labelWidth="13%"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"
				size="26" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:combobox labelWidth="15%" width="34%" label="modal" property="tpModal" domain="DM_MODAL" serializable="false">
			<adsm:propertyMapping relatedProperty="tpModal.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpModal.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:combobox labelWidth="13%" width="38%" label="abrangencia" property="tpAbrangencia" domain="DM_ABRANGENCIA" serializable="false">
			<adsm:propertyMapping relatedProperty="tpAbrangencia.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpAbrangencia.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:combobox width="35%" label="formatoRelatorio" 
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<!-- vendas/emitirRelacaoClientesEspeciaisTabelas.jasper -->
			<adsm:reportViewerButton service="lms.vendas.emitirRelacaoClientesEspeciaisTabelasAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString","");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

	if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
	}
}

function setFormatoDefault_cb(data, error) {
	if(data) {
		tpFormatoRelatorio_cb(data);
	}
	setElementValue("tpFormatoRelatorio", "pdf");
	ajustaFormatoRelatorio();
}

function ajustaFormatoRelatorio() {
	var combo = document.getElementById("tpFormatoRelatorio");
	setElementValue("tpFormatoRelatorio.descricao", combo.options[combo.selectedIndex].text);
	setElementValue("tpFormatoRelatorio.valor", combo.value);
}

function findDadosSessao_cb() {
	onPageLoad_cb();
	var sdo = createServiceDataObject("lms.vendas.emitirRelacaoClientesEspeciaisTabelasAction.findDadosSessao", "ajustaDadosSessao");
	xmit({serviceDataObjects:[sdo]});
}

function ajustaDadosSessao_cb(data, errorMsg, errorKey) {
	if(errorMsg) {
		alert(errorMsg);
		return;
	}
	var dataAtual = "";
	var idFilialSessao = "";
	var nmFantasiaSessao = "";
	var sgFilialSessao = "";

	if(data) {
		dataAtual = data.dataAtual;
		idFilialSessao = data.idFilialSessao;
		nmFantasiaSessao = data.nmFantasiaSessao;
		sgFilialSessao = data.sgFilialSessao;
	}
	setElementValue("dataReferencia", dataAtual);
	setElementValue("filial.idFilial", idFilialSessao);
	setElementValue("filial.pessoa.nmFantasia", nmFantasiaSessao);
	setElementValue("filial.sgFilial", sgFilialSessao);

	ajustaDadosRecebidos();
}

function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click"){
		setFormatoDefault_cb(null, null);
		findDadosSessao_cb(null, null);
	}
}

function ajustaDadosRecebidos() {
	var url = new URL(parent.location.href);
	var tabelaPrecoString = url.parameters["tabelaPreco.tabelaPrecoString"];
	var idTabelaPreco = url.parameters["tabelaPreco.idTabelaPreco"];
	var dsDescricao = url.parameters["tabelaPreco.dsDescricao"];
	var tpModal = url.parameters["tpModal"];
	var tpAbrangencia = url.parameters["tpAbrangencia"];
	var nrIdentificacao = url.parameters["cliente.pessoa.nrIdentificacao"];
	var idCliente = url.parameters["cliente.idCliente"];
	var nmPessoa = url.parameters["cliente.pessoa.nmPessoa"];

	if (tabelaPrecoString != undefined) {
		setElementValue("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
		setElementValue("tabelaPreco.idTabelaPreco", idTabelaPreco);
		setElementValue("tabelaPreco.dsDescricao", dsDescricao);
		setElementValue("tpModal", tpModal);
		setElementValue("tpAbrangencia", tpAbrangencia);
		setElementValue("cliente.pessoa.nrIdentificacao", nrIdentificacao);
		setElementValue("cliente.idCliente", idCliente);
		setElementValue("cliente.pessoa.nmPessoa", nmPessoa);

		setDisabled("tabelaPreco.idTabelaPreco", true);
		setDisabled("cliente.idCliente", true);
		setDisabled("tpModal", true);
		setDisabled("tpAbrangencia", true);
	}
}
</script>