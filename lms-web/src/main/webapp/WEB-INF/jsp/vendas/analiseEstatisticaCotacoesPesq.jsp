<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoad="myPageLoad" onPageLoadCallBack="findUsuarioSessao">
	<adsm:form action="/vendas/analiseEstatisticaCotacoes">
		<adsm:hidden property="regional.siglaDescricao"/>
		<adsm:hidden property="funcionario.codPessoa.codigo" />
		<adsm:hidden property="filial.sgFilialHidden" />
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />
		<adsm:hidden property="tpAcesso" value="F" />

		<!-- Combo de regionais -->
		<adsm:combobox label="regional" property="regional.idRegional"
			optionLabelProperty="siglaDescricao" optionProperty="idRegional"
			service="lms.vendas.analiseEstatisticaCotacoesAction.findRegional"
			boxWidth="270" required="true" width="40%">
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao" blankFill="false"/>
		</adsm:combobox>

		<!-- Lookup de filial -->
		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais" 
			service="lms.vendas.analiseEstatisticaCotacoesAction.findLookupFilial"
			dataType="text"
			labelWidth="10%"
			width="30%"
			size="3" 
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>

			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>

			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			property="funcionario"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.vendas.analiseEstatisticaCotacoesAction.findLookupUsuarioFuncionario" 
			dataType="text" label="funcionarioSolicitante" size="16" maxLength="16" 
			width="40%" action="/configuracoes/consultarFuncionariosView">

			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />

			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.codigo" modelProperty="idUsuario"/>

			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="25" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox width="25%" label="tipoCliente" labelWidth="10%" property="tipoCliente" domain="DM_TIPO_CLIENTE" serializable="false" boxWidth="120">
			<adsm:hidden property="tpCliente.valor"/>
			<adsm:hidden property="tpCliente.descricao"/>
			<adsm:propertyMapping relatedProperty="tpCliente.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpCliente.descricao" modelProperty="description"/>
		</adsm:combobox>
		
		<adsm:hidden property="tpModal.valor"/>
		<adsm:hidden property="tpModal.descricao"/>
		<adsm:combobox  property="tpModal" label="modal" width="40%"
			domain="DM_MODAL" serializable="false" required="true">
			
			<adsm:propertyMapping relatedProperty="tpModal.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpModal.descricao" modelProperty="description"/>
			
		</adsm:combobox>
			
		<adsm:hidden property="tpAbrangencia.valor"/>
		<adsm:hidden property="tpAbrangencia.descricao"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" required="true"
			labelWidth="10%" width="30%" boxWidth="120" domain="DM_ABRANGENCIA" serializable="false">
			
			<adsm:propertyMapping relatedProperty="tpAbrangencia.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpAbrangencia.descricao" modelProperty="description"/>
			
		</adsm:combobox>

		<adsm:range label="periodo" maxInterval="31" width="70%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial"/>
			<adsm:textbox dataType="JTDate" property="dataFinal"/>
		</adsm:range>
		
		<adsm:combobox width="35%" label="formatoRelatorio"
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<!-- vendas/analiseEstatisticaCotacoes.jasper -->
			<adsm:reportViewerButton service="lms.vendas.analiseEstatisticaCotacoesAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">

function ajustaRegionalSiglaDescricao() {
	var comboRegional = getElement("regional.idRegional");
	if((comboRegional.options.length > 0) && (comboRegional.selectedIndex > 0) ) {
		setElementValue("regional.siglaDescricao", comboRegional.options[comboRegional.selectedIndex].text);
	} else {
		setElementValue("regional.siglaDescricao", "");
		setElementValue("regional.idRegional", 0);
	}
}

function myPageLoad() {
	onPageLoad();

	var pms = document.getElementById("filial.idFilial").propertyMappings;
	var pmsn = new Array();
	for (var i = 2; i < pms.length; i++)
		pmsn[i - 2] = pms[i];
	document.getElementById("filial.idFilial").propertyMappings = pmsn;
}

function findUsuarioSessao_cb(dados, erro) {
	if(erro != undefined) {
		alert(erro);
		return;
	} else {
		var sdo = createServiceDataObject("lms.vendas.analiseEstatisticaCotacoesAction.findDadosSessao", "findDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
}

function findDadosSessao_cb(data, errorMsg, errorKey) {
	if (errorMsg) {
		alert(errorMsg);
		return false;
	}
	var idFilialSessao = "";
	var nmFantasiaSessao = "";
	var sgFilialSessao = "";
	var idRegionalSessao = "";
	if(data) {
		idFilialSessao = data.idFilialSessao;
		nmFantasiaSessao = data.nmFantasiaSessao;
		sgFilialSessao = data.sgFilialSessao;
		idRegionalSessao = data.idRegionalSessao;
	}
	setElementValue("regional.idRegional", idRegionalSessao);
	setElementValue("filial.idFilial", idFilialSessao);
	setElementValue("filial.pessoa.nmFantasia", nmFantasiaSessao);
	setElementValue("filial.sgFilial", sgFilialSessao);
	setElementValue("filial.sgFilialHidden", sgFilialSessao);
	ajustaRegionalSiglaDescricao();
}

function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click"){
		findUsuarioSessao_cb(null, null);
		setFormatoDefault_cb(null, null);
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
</script>