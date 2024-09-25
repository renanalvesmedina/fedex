<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.contasreceber.pesquisarRelacoesCobrancaAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/contasReceber/pesquisarRelacoesCobranca"  onDataLoadCallBack="myOnDataLoad" >
					   

		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.municipios.filialService.findLookupBySgFilial" 
					 dataType="text" 
					 property="filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 label = "filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="85%"
					 exactMatch="false"
					 labelWidth="228">
					 
		<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>			
		</adsm:lookup>

		<adsm:textbox property="nrRelacaoCobrancaFilial"  label="relacaoCobranca" dataType="integer" size="10" labelWidth="22%" width="30%" />


		<adsm:lookup 
			label="redeco"
			popupLabel="pesquisarFilial"
			labelWidth="10%" 
			width="38%"
			dataType="text"
			property="redeco.filial" 
		 	idProperty="idFilial" 
			criteriaProperty="sgFilial"
			service="lms.contasreceber.pesquisarRelacoesCobrancaAction.findLookupByFilial" 
			action="/municipios/manterFiliais"
			size="3" 
			maxLength="3" 
			picker="false"
			minLengthForAutoPopUpSearch="3"
 			serializable="false">
	
			<adsm:propertyMapping formProperty="redeco.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>		 
			<adsm:propertyMapping formProperty="siglaFilial" modelProperty="sgFilial"/>
			<adsm:lookup property="redeco"
			 			 dataType="integer"
						 size="10"
						 maxLength="10"
						 mask="0000000000"
						 idProperty="idRedeco" 
						 criteriaProperty="nrRedeco"
						 onPopupSetValue="setaFilial"
						 popupLabel="pesquisarRedeco"
						 onDataLoadCallBack="retornoLookupRedeco"
						 action="/contasReceber/manterRedeco"  
						 service="lms.contasreceber.pesquisarRelacoesCobrancaAction.findRedecos"
						 >
				 <adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="redeco.filial.idFilial"/>
				 <adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="redeco.filial.sgFilial"/>
				 <adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="redeco.filial.pessoa.nmFantasia" inlineQuery="true"/>
				 <adsm:propertyMapping formProperty="nrRedeco" modelProperty="nrRedeco"/>
			</adsm:lookup>
		</adsm:lookup>	
		

		<adsm:hidden property="redeco.filial.pessoa.nmFantasia"/>			 
		<adsm:hidden property="siglaFilial"/>			 		
		<adsm:hidden property="nrRedeco"/>	
		


		<adsm:buttonBar freeLayout="true">
			<adsm:findButton caption="consultar" callbackProperty="RelacaoCobranca"/>
			<adsm:button caption="limpar" onclick="limpar();" buttonType="resetButton" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>
	


	<adsm:grid idProperty="idRelacaoCobranca" property="RelacaoCobranca"
				   service="lms.contasreceber.pesquisarRelacoesCobrancaAction.findPaginated"
				   rowCountService="lms.contasreceber.pesquisarRelacoesCobrancaAction.getRowCount"
				   rows="13" selectionMode="none" onRowClick="liberaCad">
	
        <adsm:gridColumnGroup separatorType="FILIAL">
                <adsm:gridColumn width="0"  dataType="text" title="filialCobranca" property="sgFilial"/>
                <adsm:gridColumn width="150" dataType="text" title="" property="nmFantasiaFilial"/>      
        </adsm:gridColumnGroup>
	
		<adsm:gridColumn width="151" dataType="integer" title="relacaoCobranca" property="nrRelacaoCobranca"/>
		<adsm:gridColumn width="189" dataType="integer" title="quantidadeDocumentos" property="qtDocumentos"/>
	
		<adsm:gridColumn width="50" dataType="text" title="valorTotalRecebido" property="siglaMoeda"/>
		<adsm:gridColumn width="66" dataType="currency" title="" property="valorTotalPago"/>
	
	
		<adsm:gridColumn width="115" dataType="text" title="situacao" property="dsSituacaoCobranca"/>

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>

		
	
</adsm:window>

<script>

	/**
	  * Evento chamando toda a vez que se acessa a pagina
	  */
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click"){
		  var tabGroup = getTabGroup(this.document);
	  	  tabGroup.setDisabledTab("documentosServico", true);	
	  	  tabGroup.setDisabledTab("cad", true);	
		}
		
//		setDisabled("redeco.nrRedeco",true);
		
	 }
 
	/**
	  * Função chamada no rowClick da grid,
	  * responsável por habilitar a aba de detalhamento, caso a List não esteja sendo chamada de uma lookup
	  */
	function liberaCad(){
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["mode"] != "lookup")) {
		  var tabGroup = getTabGroup(this.document);
	 	  tabGroup.setDisabledTab("cad", false);	
	 	}
	 	return true;
	} 
	    
	/**
	  * Torna o botão fechar visível, caso a List esteja sendo chamada de uma lookup
	  */
	function myOnPageLoad(){
		onPageLoad();
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["mode"] == "lookup")) {
			document.getElementById('btnFechar').property = ".closeButton";
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}




	/**
	*	Ao retornar da da tela da lookup de Redeco seta a sigla da filial associada ao redeco
	*/
	function setaFilial(data, error) {
		if ( data != null){
			setElementValue("redeco.filial.sgFilial", data.sgFilial);
			setElementValue("redeco.filial.idFilial", data.idFilial);
			setElementValue("redeco.filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		}
	}
	
	/**
	*	Ao retornar a lookup de Redeco seta a sigla da filial associada ao redeco
	*/

		
	/*
		Trata o retorno da lookup de redeco, seta a filial
	*/
	function retornoLookupRedeco_cb(data,error){
	
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		var retorno = redeco_nrRedeco_exactMatch_cb(data); 		
		
		if( retorno == true && data[0] != null ){		

			setElementValue("redeco.filial.idFilial", data[0].filial.idFilial);
			setElementValue("redeco.filial.sgFilial", data[0].filial.sgFilial);
			setElementValue("redeco.filial.pessoa.nmFantasia", data[0].filial.pessoa.nmFantasia);
	
		}
		
		return retorno;
	
	}		
	
	function myPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data,erro);	
		findFilialUsuarioLogado();
	}
	
	function limpar(){
		cleanButtonScript(document);
		findFilialUsuarioLogado();
	}
	
	function findFilialUsuarioLogado(){
		_serviceDataObjects = new Array();
		var dados = new Array();	
        addServiceDataObject(createServiceDataObject("lms.contasreceber.pesquisarRelacoesCobrancaAction.findFilialUsuarioLogado",
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
		
	}		
	
</script>


