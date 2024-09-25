<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
var idMilkRun;
var iFrameSemana2;
var iFrameSemana3;
var iFrameSemana4;

/**
 * Função para popular a listbox de exigências
 */ 
function populaExigencias() {
	// busca o valor do tipo de exigencia

    var idTipoExigencia = idMilkRun;

    var data = new Array();
    setNestedBeanPropertyValue(data, "milkRun.idMilkRun", idTipoExigencia);
    
    var sdo = createServiceDataObject("lms.coleta.manterMilkRunAction.findRemetenteByIdMilkRun", "servico", data);    
    xmit({serviceDataObjects:[sdo]});
}
	
function servico_cb(data, errorMsg) {	
	var tabGroup = getTabGroup(document);
		
	// Limpa as Listbox das 4 semanas antes de incluir os dados.
	for(var i=1; i <= 4; i++) {
		var tabSemana = tabGroup.getTab("semana" + i);
		resetValue(tabSemana.getElementById("segundaFeira"));
		resetValue(tabSemana.getElementById("tercaFeira"));
		resetValue(tabSemana.getElementById("quartaFeira"));
		resetValue(tabSemana.getElementById("quintaFeira"));
		resetValue(tabSemana.getElementById("sextaFeira"));
		resetValue(tabSemana.getElementById("sabado"));
		resetValue(tabSemana.getElementById("domingo"));
	}
	
	iFrameSemana2.contentWindow.data = data;
	iFrameSemana2.contentWindow.errorMsg = errorMsg;
	var populaExigenciasSemana2 = iFrameSemana2.contentWindow.populaExigenciasSemana2;
	populaExigenciasSemana2();

	iFrameSemana3.contentWindow.data = data;
	iFrameSemana3.contentWindow.errorMsg = errorMsg;
	var populaExigenciasSemana3 = iFrameSemana3.contentWindow.populaExigenciasSemana3;
	populaExigenciasSemana3();

	iFrameSemana4.contentWindow.data = data;
	iFrameSemana4.contentWindow.errorMsg = errorMsg;
	var populaExigenciasSemana4 = iFrameSemana4.contentWindow.populaExigenciasSemana4;
	populaExigenciasSemana4();

	if(data.semana1 != undefined) {
		for(var i=0; i<data.semana1.length; i++) {
			segundaFeira_cb(data.semana1[i], errorMsg);
			tercaFeira_cb(data.semana1[i], errorMsg);
			quartaFeira_cb(data.semana1[i], errorMsg);
			quintaFeira_cb(data.semana1[i], errorMsg);
			sextaFeira_cb(data.semana1[i], errorMsg);
			sabado_cb(data.semana1[i], errorMsg);
			domingo_cb(data.semana1[i], errorMsg);			
		}		
	}
	
}	
	
</script>
<adsm:window service="lms.coleta.milkRemetenteService">
	<adsm:form action="/coleta/manterMilkRun" id="idSemana1" height="280">

		<adsm:listbox label="domingo" size="4" 
	         			property="domingo" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>

		<adsm:listbox label="segundaFeira" size="4" 
	         			property="segundaFeira" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%" />

		<adsm:listbox label="tercaFeira" size="4" 
	         			property="tercaFeira" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>					  	

		<adsm:listbox label="quartaFeira" size="4" 
	         			property="quartaFeira" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>					  	

		<adsm:listbox label="quintaFeira" size="4" 
	         			property="quintaFeira"
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>					  	

		<adsm:listbox label="sextaFeira" size="4" 
	         			property="sextaFeira" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>					  	

		<adsm:listbox label="sabado" size="4" 
	         			property="sabado" 
	     				optionProperty="id"
					  	optionLabelProperty="descricao"
					  	showOrderControls="true"
					  	showIndex="false"
					  	orderProperty="ordem"	
					  	serializable="false"
					  	boxWidth="280"					  	
					  	labelWidth="10%" width="36%"/>					  	
					  	
	</adsm:form>
</adsm:window>