<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="lacres" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >
			 
	<adsm:form action="/carregamento/manterControleCargasLacres" idProperty="idLacreControleCarga" onDataLoadCallBack="retornoCarregaDados"
			   service="lms.carregamento.manterControleCargasJanelasAction.findByIdLacre" >

		<adsm:section caption="lacres" />

		<adsm:hidden property="controleCarga.idControleCarga" serializable="true" />
		<adsm:hidden property="tpStatusControleCarga" serializable="false" />
		<adsm:hidden property="blPermiteAlterar" serializable="false" />
		
		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="numeroLacre" property="nrLacre" dataType="integer" size="15" maxLength="6" width="42%" required="true" />

		<adsm:textbox label="status" property="tpStatusLacre" dataType="text" labelWidth="14%" width="29%" disabled="true"/>


		<adsm:textbox label="filialInclusao" property="filialByIdFilialInclusao.sgFilial" dataType="text" size="3" 
					  width="42%" disabled="true" cellStyle="vertical-align: middle">
			<adsm:textbox dataType="text" property="filialByIdFilialInclusao.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:textbox>

		<adsm:textarea label="observacaoInclusao" property="obInclusaoLacre" 
					   maxLength="200" rows="2" columns="40" labelWidth="14%" width="29%" />

		<adsm:textbox label="localInclusao" property="dsLocalInclusao" dataType="text" size="60" maxLength="80" width="85%" />


		<adsm:textbox label="filialConferencia" property="filialByIdFilialAlteraStatus.sgFilial" dataType="text" size="3" 
					  width="42%" disabled="true" cellStyle="vertical-align: middle">
			<adsm:textbox dataType="text" property="filialByIdFilialAlteraStatus.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:textbox>

		<adsm:textarea label="observacaoConferencia" property="obConferenciaLacre" 
					   maxLength="200" rows="2" columns="40" labelWidth="14%" width="29%" disabled="true" />

		<adsm:textbox label="localConferencia" property="dsLocalConferencia" dataType="text" 
					  size="60" maxLength="80" width="85%" disabled="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvar" id="botaoSalvar" onclick="botaoSalvar_onClick(this.form)" disabled="false" />
			<adsm:button caption="novo" id="botaoNovo" onclick="botaoNovo_onClick()" disabled="false" />
		</adsm:buttonBar>

		<script>
			var lms_05067 = '<adsm:label key="LMS-05067"/>';
		</script>
	</adsm:form>

	<adsm:grid idProperty="idLacreControleCarga" property="lacresControleCarga" 
			   selectionMode="none" scrollBars="horizontal" gridHeight="141" title="lacres" unique="true" rows="7"
			   onRowClick="populaForm"
			   service="lms.carregamento.manterControleCargasJanelasAction.findPaginatedLacre"
			   rowCountService="lms.carregamento.manterControleCargasJanelasAction.getRowCountLacre"
			   >
		<adsm:gridColumn title="lacre" 					property="nrLacre" width="100" align="right" />
		<adsm:gridColumn title="dataInclusao"			property="dhInclusao" width="140" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="status" 				property="tpStatusLacre" isDomain="true" width="125" />
		<adsm:gridColumn title="filialInclusao" 		property="filialByIdFilialInclusao.sgFilial" width="110" />
		<adsm:gridColumn title="observacaoInclusao" 	property="obInclusaoLacre" width="200" />
		<adsm:gridColumn title="dataAlteracao"			property="dhAlteracao" width="140" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="filialConferencia" 		property="filialByIdFilialAlteraStatus.sgFilial" width="120" />
		<adsm:gridColumn title="conferencia" 			property="conferencia" image="/images/popup.gif" openPopup="true" link="javascript:exibirConferencia" width="90" align="center" />
		<adsm:gridColumn title="observacaoConferencia" 	property="obConferenciaLacre" width="200" />
		<adsm:gridColumn title="usuarioInclusao"	 	property="usuarioByIdFuncInclusao.nmUsuario" width="200" />
		<adsm:gridColumn title="usuarioAlteracao"	 	property="usuarioByIdFuncAlteraStatus.nmUsuario" width="200" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>
function populaForm(id) {
	desabilitaCampos(true);
	setDisabled("botaoSalvar", true);
	onDataLoad(id);
	return false;
}

function retornoCarregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	povoaDadosMaster();
	desabilitaCampos(true)
	setDisabled("botaoSalvar", true);
	setDisabled("botaoFechar", false);
	setFocusOnFirstFocusableField();
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		inicializaTela();
		povoaGrid();
	}
}

function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
	setElementValue("blPermiteAlterar", dialogArguments.window.document.getElementById('blPermiteAlterar').value);
	setElementValue("tpStatusControleCarga", dialogArguments.window.document.getElementById('tpStatusControleCarga.value').value);
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
    lacresControleCargaGridDef.executeSearch(filtro, true);
    return false;
}

function inicializaTela() {
	resetValue(this.document);
	povoaDadosMaster();
	desabilitaCampos(false);
	desabilitaBotaoSalvar(false);
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.getDadosIniciaisByLacre", 
				"retornoDadosIniciais", {idControleCarga:getElementValue("controleCarga.idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoDadosIniciais_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
   	setElementValue("filialByIdFilialInclusao.sgFilial", getNestedBeanPropertyValue(data,"filialUsuario.sgFilial"));
   	setElementValue("filialByIdFilialInclusao.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filialUsuario.pessoa.nmFantasia"));
   	setElementValue("tpStatusLacre", getNestedBeanPropertyValue(data,"tpStatusLacre"));
   	setFocusOnFirstFocusableField();
}

function desabilitaCampos(valor) {
	setDisabled("nrLacre", valor);
	document.getElementById("obInclusaoLacre").readOnly=valor;
	document.getElementById("obConferenciaLacre").readOnly=false;
	setDisabled("dsLocalInclusao", valor);
}

function botaoNovo_onClick() {
	inicializaTela();
}

function botaoSalvar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
    var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.storeLacres", "store", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}

function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	desabilitaCampos(true);
   	showSuccessMessage();
   	setDisabled("botaoSalvar", true);
   	setFocus(document.getElementById("botaoNovo"));
   	povoaGrid();
}

function exibirConferencia(id){
	var tpStatusLacre = lacresControleCargaGridDef.findById(id).tpStatusLacre.value;
	if (getElementValue("tpStatusControleCarga") != "CA" && getElementValue("tpStatusControleCarga") != "AD" && tpStatusLacre != "FE") {
		alert(lms_05067);
	}
	else {
		if (getElementValue("blPermiteAlterar") == "true") {
			showModalDialog('carregamento/manterControleCargasConferenciaLacres.do?cmd=main&idLacreControleCarga=' + id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:220px;');
			povoaGrid();
		}
	}
}


function desabilitaBotaoSalvar(valor) {
	if (valor == true) {
		setDisabled("botaoSalvar", true);
	}
	else {
		if (getElementValue("blPermiteAlterar") == "true")
			setDisabled("botaoSalvar", false);
		else
			setDisabled("botaoSalvar", true);
	}
}
</script>