<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.moedaCotacaoService">
	<adsm:form action="/configuracoes/manterCotacoesMoedas" idProperty="idCotacaoMoeda">

		<adsm:hidden property="situacao" serializable="false" value="A"/>

   		<adsm:lookup service="lms.municipios.paisService.findPaisesByMoeda" dataType="text" property="moedaPais.pais" 
			criteriaProperty="nmPais" idProperty="idPais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			label="pais" maxLength="60" action="/municipios/manterPaises" required="true">
				<adsm:propertyMapping criteriaProperty="moedaPais.moeda.idMoeda" modelProperty="moedaPais.moeda.idMoeda" 
					addChangeListener="false"/>
				<adsm:propertyMapping criteriaProperty="situacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		
		
	   	<adsm:lookup service="lms.configuracoes.moedaService.findMoedasByPais" dataType="text" exactMatch="false"
			property="moedaPais.moeda" criteriaProperty="dsMoeda" idProperty="idMoeda" minLengthForAutoPopUpSearch="3"
			label="moeda" maxLength="60" action="/configuracoes/manterMoedas" required="true">
				<adsm:propertyMapping criteriaProperty="moedaPais.pais.idPais" modelProperty="moedaPais.pais.idPais" 
					addChangeListener="false"/>
				<adsm:propertyMapping criteriaProperty="situacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:textbox dataType="JTDate" property="dtCotacaoMoeda" label="data" required="true"/>
		<adsm:textbox dataType="currency" mask="##,###,###,###,##0.0000" property="vlCotacaoMoeda" label="valor" size="25" maxLength="21" required="true" minValue="0.0001"/>
	
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript(null, null, {name:"newButton_click"});
		desabilitaTodosCampos(false);
		
	}
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}


		setDisabled("moedaPais.pais.idPais",val);
		setDisabled("moedaPais.pais.nmPais",val);
		setDisabled("moedaPais.moeda.idMoeda",val);
		setDisabled("moedaPais.moeda.dsMoeda",val);
		setDisabled("dtCotacaoMoeda",val);
		setDisabled("vlCotacaoMoeda",val);

		setFocusOnFirstFocusableField();				
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
		
		setDisabled('btnLimpar', false);
				
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
			//todos os campos ficam desabilitados entao o foco vai para limpar
			setFocus(document.getElementById('btnLimpar'),true,true);
			
		}

	}
</script>
