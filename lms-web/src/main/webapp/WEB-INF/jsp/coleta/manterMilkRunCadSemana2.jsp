<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	var data;
	var errorMsg;

	function populaExigenciasSemana2() {    
		if(data.semana2 != undefined) {
			for(var i=0; i<data.semana2.length; i++) {
				segundaFeira_cb(data.semana2[i], errorMsg);
				tercaFeira_cb(data.semana2[i], errorMsg);
				quartaFeira_cb(data.semana2[i], errorMsg);
				quintaFeira_cb(data.semana2[i], errorMsg);
				sextaFeira_cb(data.semana2[i], errorMsg);
				sabado_cb(data.semana2[i], errorMsg);
				domingo_cb(data.semana2[i], errorMsg);			
			}		
		}
	}
	
</script>
<adsm:window>
	<adsm:form action="/coleta/manterMilkRun" id="idSemana2" height="280">
	
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
					  	labelWidth="10%" width="36%"/>

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