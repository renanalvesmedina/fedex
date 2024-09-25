<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.listarClientesStatusAction" onPageLoadCallBack="opl" onPageLoad="pl" >
	<adsm:form action="/vendas/listarClientesStatus">
		<adsm:i18nLabels>
			<adsm:include key="LMS-30052"/>
		</adsm:i18nLabels>

		<adsm:combobox label="regional" property="regional.idRegional" required="true"
			optionLabelProperty="siglaDescricao" optionProperty="idRegional"
			service="lms.vendas.listarClientesStatusAction.findRegional"
			boxWidth="240"
			onchange="return resetFilial(this);">
			<adsm:propertyMapping 
				relatedProperty="regional.siglaDescricao"
				modelProperty="siglaDescricao" />
		</adsm:combobox>
		<adsm:hidden property="regional.siglaDescricao" />
		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			criteriaSerializable="true"
			service="lms.vendas.listarClientesStatusAction.findRegionalFilial"
			action="/municipios/manterFiliais" 
			onDataLoadCallBack="lookup_filial"
			onPopupSetValue="ajustaRegional"
			dataType="text"
			labelWidth="15%"
			maxLength="3"
			size="3"
			width="8%">
			<adsm:propertyMapping criteriaProperty="regional.idRegional"
				modelProperty="regionalFiliais.regional.idRegional" />
			<adsm:propertyMapping criteriaProperty="filial.sgFilial"
				modelProperty="sgFilial" />

			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="nmFantasia" />

			<adsm:textbox
				dataType="text"
				width="27%"
				property="filial.pessoa.nmFantasia"
				size="30"
				disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="tpCliente.valor" />
		<adsm:hidden property="tpCliente.descricao" />
		<adsm:combobox label="tipoCliente" property="tipoCliente"
			domain="DM_TIPO_CLIENTE">
			<adsm:propertyMapping relatedProperty="tpCliente.valor"
				modelProperty="value" />
			<adsm:propertyMapping relatedProperty="tpCliente.descricao"
				modelProperty="description" />
		</adsm:combobox>
		<adsm:hidden property="situacao.valor" />
		<adsm:hidden property="situacao.descricao" />
		<adsm:combobox label="situacao" property="sit" serializable="false"
			domain="DM_STATUS_PESSOA">
			<adsm:propertyMapping relatedProperty="situacao.valor"
				modelProperty="value" />
			<adsm:propertyMapping relatedProperty="situacao.descricao"
				modelProperty="description" />
		</adsm:combobox>
		<adsm:buttonBar>
			<adsm:button
				id="btnEmitir"
				caption="visualizar"
				service="lms.vendas.listarClientesStatusAction.scheduleReport"
				callbackProperty="retornoReport"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

//document.getElementById("filial.sgFilial").serializable="true";

function resetFilial(e) {
	resetValue("filial.idFilial");
	resetValue("filial.sgFilial");
	resetValue("filial.pessoa.nmFantasia");
	return comboboxChange({e:e});;
}

function lookup_filial_cb(dados, erros) {
	var r = lookupExactMatch({e:document.getElementById("filial.idFilial"), callBack:"lookup_filial_like", data:dados});
	ajustaRegional(dados[0]);
	return r;
}

function lookup_filial_like_cb(dados, erros) {
	var r = lookupLikeEndMatch({e:document.getElementById("filial.idFilial"), data:dados});
	ajustaRegional(dados[0]);
	return r;
}

function ajustaRegional(dados){
	if(dados) {
		var idRegional = getNestedBeanPropertyValue(dados, "idRegional");
		if(idRegional != undefined){
			setElementValue("regional.idRegional", idRegional);
		}
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(dados, "sgFilial"));
	}
	return true;
}

function pl() {
   onPageLoad();
   var pms = document.getElementById("filial.idFilial").propertyMappings;
   var pmsn = new Array();
   for (var i = 2; i < pms.length; i++)
      pmsn[i - 2] = pms[i];
   document.getElementById("filial.idFilial").propertyMappings = pmsn;
}

function opl_cb(dados,erros){
	onPageLoad_cb(dados,erros);
	var sdo = createServiceDataObject("lms.vendas.listarClientesStatusAction.findDadosDefault", "findDadosDefault", {});
	xmit({serviceDataObjects:[sdo]});
}

var idFilialUsuarioLogado;
var sgFilialUsuarioLogado;
var nmFilialUsuarioLogado;
var idRegionalFilialUsuarioLogado;
var siglaDescricaoRegionalFilialUsuarioLogado;
function findDadosDefault_cb(dados,erro){
	if(erro!=undefined){
		alert(erro);
		return;
	}
	idFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idFilialUsuarioLogado");
	sgFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "sgFilialUsuarioLogado");
	nmFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "nmFilialUsuarioLogado");
	idRegionalFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idRegionalFilialUsuarioLogado");
	siglaDescricaoRegionalFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "siglaDescricaoRegionalFilialUsuarioLogado");
	ajustaValorDefault();
}

function ajustaValorDefault(){
	if(idRegionalFilialUsuarioLogado != undefined && idFilialUsuarioLogado != undefined) {
		setElementValue("filial.pessoa.nmFantasia",nmFilialUsuarioLogado);
		setElementValue("filial.sgFilial",sgFilialUsuarioLogado);
		setElementValue("filial.idFilial",idFilialUsuarioLogado);
		setElementValue("regional.idRegional",idRegionalFilialUsuarioLogado);
		setElementValue("regional.siglaDescricao",siglaDescricaoRegionalFilialUsuarioLogado);
	}
}

function retornoReport_cb(data, error){
	if(error != undefined) {
		alert(error);
		return;
	}
	alertI18nMessage("LMS-30052");
}

function initWindow(eventObj) {
	setDisabled('btnEmitir',false);
	if (eventObj.name == "cleanButton_click"){
		ajustaValorDefault();
	}
}
</script>