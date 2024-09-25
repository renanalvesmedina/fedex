<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/contasReceber/emitirRelacaoContaFrete">
		
		<adsm:hidden property="siglaFilial"/>
		<adsm:hidden property="nmFantasia"/>
		 
		<adsm:lookup
			label="fatura" 
			width="5%"
			labelWidth="20%"
			dataType="text"
			property="filial" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial"
			onchange="return changeFilial()"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findFilial" 
			action="/municipios/manterFiliais"
			size="3" 
			popupLabel="pesquisarFilial"
			maxLength="3" 
			picker="false"
			serializable="true">

         	<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial" />
         	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="nmFantasia" />

     		
     		<adsm:lookup service="lms.contasreceber.emitirRelacaoContaFreteAction.findFaturaByFilial" 
					 idProperty="idFatura"
					 dataType="integer"
					 mask="0000000000" 
					 property="fatura" 					 
					 criteriaProperty="nrFatura"		
					 criteriaSerializable="true"			  
					 size="10"
					 popupLabel="pesquisarFatura"
					 width="25%"
					 maxLength="10" 
					 required="true"
					 action="/contasReceber/manterFaturas"
					 exactMatch="false" minLengthForAutoPopUpSearch="3">

				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialByIdFilial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filialByIdFilial.sgFilial"/>
				<adsm:propertyMapping criteriaProperty="nmFantasia" 	 modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/>
				<adsm:propertyMapping modelProperty="filialByIdFilial.sgFilial" relatedProperty="siglaFilial" />
				<adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filialByIdFilial.idFilial" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filialByIdFilial.sgFilial" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="nmFantasia" modelProperty="filialByIdFilial.pessoa.nmFantasia" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>		
		
		<adsm:hidden property="moeda.siglaSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
			labelWidth="20%" width="30%" boxWidth="85"  required="true" serializable="true">
			
				<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="siglaSimbolo"/>			
		</adsm:combobox>
		
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   labelWidth="20%"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacaoContaFreteAction"/>
			<adsm:button caption="limpar" onclick="btnLimparClick();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	document.getElementById("filial.sgFilial").required = "true";

	setDisabled("fatura.nrFatura", true);
	
	
	function initWindow(eventObj){
		findMoedaUsuarioLogado();
	}
	
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		findMoedaUsuarioLogado();
	}
	
	function findMoedaUsuarioLogado(){
		var dados = new Array();                 
         
        var sdo = createServiceDataObject("lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaUsuarioLogado",
                                          "retornoFindMoedaUsuarioLogado",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}
	
	function retornoFindMoedaUsuarioLogado_cb(data,error){
		onDataLoad_cb(data,error); 
	}
	
	function changeFilial(){
		var retorno = filial_sgFilialOnChangeHandler();
		
		if( retorno ){
			setDisabled("fatura.nrFatura", true);
		}else{
			setDisabled("fatura.nrFatura", false); 	
		}
	
		return retorno;
	}
	
	function btnLimparClick(){
		cleanButtonScript(this.document);
		setDisabled("fatura.nrFatura", true);
	}
</script>