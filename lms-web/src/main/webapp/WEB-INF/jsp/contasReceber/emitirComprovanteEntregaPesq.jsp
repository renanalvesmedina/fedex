<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirComprovanteEntregaAction" onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirComprovanteEntrega" >

		<adsm:hidden property="siglaFilial" serializable="true"/>
		
		<adsm:hidden property="nrFatura" serializable="true"/> 
		
		<adsm:hidden property="filial.pessoa.nmPessoa" serializable="false"/>
		<adsm:hidden property="filial.pessoa.nmFantasia" serializable="false"/>
		
		<adsm:lookup label="fatura"
					 popupLabel="pesquisarFilial"	
					 action="/municipios/manterFiliais" 
					 service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFilial" 
					 dataType="text" 
					 property="filialByIdFilial" 
					 idProperty="idFilial"
					 serializable="true"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 					 
					 maxLength="3" 
					 labelWidth="20%"
					 width="5%"
					 exactMatch="true" onchange="return myFilialOnChange()">

							 
			<adsm:propertyMapping formProperty="siglaFilial" modelProperty="sgFilial" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:hidden property="tpSituacaoFaturaValido" value="2"/>
			
			<adsm:lookup popupLabel="pesquisarFatura"
						 serializable="true"
   					 	 service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFatura" 
   					 	 dataType="integer" 
	   					 property="fatura" 
   	 					 idProperty="idFatura"
   	 					 criteriaProperty="nrFatura" 
   	 					 onPopupSetValue="fatura_cb"
   	 					 onDataLoadCallBack="faturacb"
    					 size="20"
   						 maxLength="10"
   						 mask="0000000000"
   					 	 width="75%"
   					 	 required="true"
   					  	 action="/contasReceber/manterFaturas"
   					  	 onclickPicker="myFaturasOnClickPicker()"
   					  	 >
	            
	            <adsm:propertyMapping formProperty="nrFatura" modelProperty="nrFatura"/>
   				<adsm:propertyMapping criteriaProperty="tpSituacaoFaturaValido" modelProperty="tpSituacaoFaturaValido" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filialByIdFilial.idFilial"/> 
   				
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filialByIdFilial.sgFilial" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/> 
	            
	         </adsm:lookup>
    
        </adsm:lookup>
		
		
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			labelWidth="20%" width="80%"/>				
	
		<adsm:buttonBar>
			<adsm:button caption="visualizar" onclick="visualizar_onclick();reportButtonScript('lms.contasreceber.emitirComprovanteEntregaAction', 'openPdf', this.form);"/>
			<adsm:button caption="limpar" onclick="limpar();" buttonType="resetButton" disabled="false"/>
		</adsm:buttonBar>
					
	</adsm:form>
	
</adsm:window>

<script><!-- 
	
	getElement("filialByIdFilial.sgFilial").required = "true";
	
	function findFilialUsuarioLogado(){
		_serviceDataObjects = new Array();
		var dados = new Array();	
        addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirComprovanteEntregaAction.findFilialUsuarioLogado",
			"retornoFindFilialUsuarioLogado", 
			dados));

        xmit(false);
	}
	
	function retornoFindFilialUsuarioLogado_cb(data,error){
		
		if (error != undefined){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		onDataLoad_cb(data,error);
		trataFaturaByFilial();
				
	}

function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setElementValue("tpFormatoRelatorio", "pdf");
	findFilialUsuarioLogado();
}

function limpar(){
	cleanButtonScript(document);
	setElementValue("tpFormatoRelatorio", "pdf");
	findFilialUsuarioLogado();
}

function fatura_cb(data, errors){

	setElementValue("filialByIdFilial.sgFilial", data.filialByIdFilial.sgFilial);
	setElementValue("filialByIdFilial.idFilial", data.filialByIdFilial.idFilial);
	setElementValue("filial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);

}

/** Tratado o retorno da lookup
	Chamdo: da lookup fatura
 */
function faturacb_cb(data, errors){

	var retorno = fatura_nrFatura_exactMatch_cb(data);
	
	if ( data != null && data[0] != undefined) {
		
		setElementValue("filialByIdFilial.sgFilial", data[0].filialByIdFilial.sgFilial);
		setElementValue("filialByIdFilial.idFilial", data[0].filialByIdFilial.idFilial);
		setElementValue("filial.pessoa.nmFantasia", data[0].filialByIdFilial.pessoa.nmFantasia);
	}
	return retorno;
}

function myFilialOnChange(){
	var retorno = filialByIdFilial_sgFilialOnChangeHandler()
	setDisabled("fatura.nrFatura",retorno);
	return retorno;
}

function trataFaturaByFilial(){
	var txtFilial = document.getElementById("filialByIdFilial.sgFilial");
	var txtFatura = document.getElementById("fatura.nrFatura");
	if (txtFilial.value.length != 0){
		txtFatura.disabled = false;
		setFocus(txtFatura);
	}else{
		txtFatura.disabled = true;
		setFocus(txtFilial);
	}
}
function myFaturasOnClickPicker(){
	lookupClickPicker({e:document.getElementById('fatura.idFatura')});
	trataFaturaByFilial();
	var drop = document.getElementById("tpFormatoRelatorio");
	var txtFatura = document.getElementById("fatura.nrFatura");
	if (txtFatura.value.length != 0){
		setFocus(drop);
	}
}
function visualizar_onclick(){
    var sgFatura = document.getElementById("filialByIdFilial.sgFilial").value;
    document.getElementById("siglaFilial").value = sgFatura;
	//trataFaturaByFilial()
}
--></script>