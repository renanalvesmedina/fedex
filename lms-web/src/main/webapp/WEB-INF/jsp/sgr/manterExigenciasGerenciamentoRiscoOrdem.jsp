<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.exigenciaGerRiscoService">
	<adsm:form action="/sgr/manterExigenciasGerenciamentoRisco" idProperty="idExigenciaGerRisco" height="380">
		<adsm:section caption="exigenciasOrdemCrescenteRelevancia"/>

		<adsm:combobox label="tipoExigencia" labelWidth="17%" width="83%"
					   property="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" 
					   optionLabelProperty="dsTipoExigenciaGerRisco" 
					   optionProperty="idTipoExigenciaGerRisco" 
					   service="lms.sgr.tipoExigenciaGerRiscoService.findOrdenadoPorDescricao" 
					   onlyActiveValues="true"
					   onchange="return populaExigencias();"
					   serializable="true"
		/>		
		
		<adsm:listbox label="exigencias" labelWidth="17%" width="83%" size="5" boxWidth="371"
					  property="exigenciasGerRisco"
					  optionProperty="idExigenciaGerRisco"
					  optionLabelProperty="dsResumida"
					  showOrderControls="true"
					  showIndex="true"
					  orderProperty="nrNivel"	
					  serializable="true"
					  onchange="populaDescricaoCompleta(this)"
		/>
		
		<adsm:textarea label="descricaoCompleta" labelWidth="17%" width="83%" 
					   property="dsCompleta" serializable="false"
					   columns="70" rows="5"
					   style="overflow: visible;"
					   maxLength="1000" disabled="true" />

		<adsm:buttonBar>
			<adsm:storeButton service="lms.sgr.exigenciaGerRiscoService.storeListOrder"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>



// Foi colocado como global para poder ser usado
// para popular a descricao completa
var dataExigencias = new Array();

/**
 * Popula o campo descricao completa baseado na exigencia selecionada
 */
 function populaDescricaoCompleta(obj) {
 
 	var idAtual = obj.getAttribute("value")+"";
	// verifica se tem dados no mapa que vem do cb de populaExigencias 	
 	if (dataExigencias!=undefined && dataExigencias.length > -1) {
 		// Pega o array de exigencias que está dentro do mapa
 		var array = getNestedBeanPropertyValue(dataExigencias, "exigenciasGerRisco");
 		//Percorre o array para achar dsCompleta da exigencia selecionada.
 		if(array.length > 0) {
	 		for (var i = 0; i< array.length; i++) {
	 			if (array[i].idExigenciaGerRisco == idAtual) {
	 				setElementValue("dsCompleta","");
	 				setElementValue("dsCompleta",array[i].dsCompleta);
	 				return true;
	 			}
	 		}
 		} 
 	}
 	return false;
}

function setDsCompletaFirstExigencia(arrayExigencias) {
	setElementValue("dsCompleta","");
	setElementValue("dsCompleta",arrayExigencias[0].dsCompleta);
}

/**
 * Função para popular a listbox de exigências
 */ 
function populaExigencias() {
	// busca o valor do tipo de exigencia
    var idTipoExigencia = getElementValue("tipoExigenciaGerRisco.idTipoExigenciaGerRisco");
	//limpa a listbox
    resetValue(document.getElementById("exigenciasGerRisco"));
    setElementValue("dsCompleta","");

	// verifica se tinha valor selecionado na combo.
    if (idTipoExigencia == "")
       return true;

    var data = new Array();
    
    // Monta um map
    setNestedBeanPropertyValue(data, "tipoExigenciaGerRisco.idTipoExigenciaGerRisco", idTipoExigencia);
	 
    var sdo = createServiceDataObject("lms.sgr.manterExigenciasGerenciamentoRiscoAction.findExigenciasByTipoExigencia", "exigenciasGerRiscoRetorno",data);
    xmit({serviceDataObjects:[sdo]});
}

function exigenciasGerRiscoRetorno_cb(data, error) {
	dataExigencias = data;
	exigenciasGerRisco_cb(data, error);
	
	var array = getNestedBeanPropertyValue(dataExigencias, "exigenciasGerRisco");
	if(array!=undefined && array.length == 1) {
	 		setDsCompletaFirstExigencia(array);
	}

}

/**
 * Função que irá carregar o valor da combo tipo de exigencia, conforme 
 * a tela de cad e posteriormente carregar a listbox
 */ 
function initWindow(eventObj) {

	// Não é necessário chamar quando o initWindow é chamado
	//pelo storeButton, pois os dados devem permanecer
	if (eventObj.name!='storeButton') {
	    var tabGroup = getTabGroup(this.document);
	    var tabDet = tabGroup.getTab("cad");
	    var idTipoExigenciaGerRisco = tabDet.getFormProperty("tipoExigenciaGerRisco.idTipoExigenciaGerRisco");
	
		setElementValue("tipoExigenciaGerRisco.idTipoExigenciaGerRisco" , idTipoExigenciaGerRisco);
		populaExigencias();
	}
}

</script>