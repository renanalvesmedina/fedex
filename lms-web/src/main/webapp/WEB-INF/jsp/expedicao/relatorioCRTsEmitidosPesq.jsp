<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.relatorioCRTsEmitidosAction" 
	onPageLoadCallBack="opl">
	<adsm:form action="/expedicao/relatorioCRTsEmitidos">

		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.relatorioCRTsEmitidosAction.findFilial"
			dataType="text"
			property="filialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			size="5"
			maxLength="3"
			label="filialOrigem"
			labelWidth="24%"
			width="76%"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="filialOrigem.pessoa.nmFantasia"
				size="30"
			/>
		</adsm:lookup>
		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.relatorioCRTsEmitidosAction.findFilial"
			dataType="text"
			property="filialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			size="5"
			maxLength="3"
			label="filialDestino"
			labelWidth="24%"
			width="76%"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="filialDestino.pessoa.nmFantasia"
				size="30"
			/>
		</adsm:lookup>
		<adsm:range
			label="periodoEmissaoCRT"
			labelWidth="24%"
			width="76%"
			required="true"
		>
			<adsm:textbox
				dataType="JTDate"
				property="dtInicial"
				smallerThan="dtFinal"
			/>
			<adsm:textbox
				biggerThan="dtInicial"
				dataType="JTDate"
				property="dtFinal"
			/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.relatorioCRTsEmitidosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

document.getElementById("filialOrigem.sgFilial").serializable="true";
document.getElementById("filialDestino.sgFilial").serializable="true";

function opl_cb(dados,erros){
	onPageLoad_cb(dados,erros);
	var sdo = createServiceDataObject("lms.expedicao.relatorioCRTsEmitidosAction.findFilialUsuarioLogado", "buscaFilialUsuarioLogado", {});
	xmit({serviceDataObjects:[sdo]});
}

var idFilialUsuarioLogado;
var sgFilialUsuarioLogado;
var nmFilialUsuarioLogado;
function buscaFilialUsuarioLogado_cb(dados,erro){
	if(erro!=undefined){
		alert(erro);
		return;
	}
	idFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idFilialUsuarioLogado");
	sgFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "sgFilialUsuarioLogado");
	nmFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "nmFilialUsuarioLogado");
	ajustaValorDefaultFilialOrigem();
}

function ajustaValorDefaultFilialOrigem(){
	setElementValue("filialOrigem.pessoa.nmFantasia",nmFilialUsuarioLogado);
	setElementValue("filialOrigem.sgFilial",sgFilialUsuarioLogado);
	setElementValue("filialOrigem.idFilial",idFilialUsuarioLogado);
}


function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click"){
		ajustaValorDefaultFilialOrigem();
	}
}
</script>