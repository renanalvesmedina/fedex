
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction" >
	
	<adsm:form action="/contasReceber/consultarReciboDemonstrativoDesconto" >


			
		<adsm:combobox label="tipoDesconto" property="tpDesconto" labelWidth="18%" width="32%" domain="DM_RECIBO_DEMONSTRATIVO"/>
		
		<adsm:lookup label="filialOrigem" 
				     dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 					 
					 size="3" 
					 maxLength="3" 
					 width="32%"
					 exactMatch="true">
				<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
				<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="31" maxLength="30" disabled="true"/>
			</adsm:lookup>
		<adsm:hidden property="siglaFilial" serializable="true"/>
		
   	    <adsm:textbox label="numero" property="nrDesconto" dataType="integer" size = "10" maxLength="10" labelWidth="18%" width="32%"/>

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS_RECIBO_DESCONTO"/>

		<adsm:range label="dataEmissao" labelWidth="18%" width="32%" >
			<adsm:textbox property="dtEmissaoInicial" dataType="JTDate"/>
			<adsm:textbox property="dtEmissaoFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:range label="valorDesconto" >
			<adsm:textbox property="vlDescontoInicial" dataType="currency" size="10"/>
			<adsm:textbox property="vlDescontoFinal" dataType="currency" size="10"/>
		</adsm:range>
		
        <adsm:combobox label="situacaoAprovacao" labelWidth="18%" property="tpSituacaoAprovacao" domain="DM_STATUS_WORKFLOW"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="desconto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idGeral" property="desconto" disableMarkAll="true" onRowClick="myOnRowClick" selectionMode="none" 
	           service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findPaginatedByReciboDemonstrativo"
	           rowCountService="lms.contasreceber.consultarReciboDemonstrativoDescontoAction.getRowCountByReciboDemonstrativo">
		<adsm:gridColumn width="120" title="tipoDesconto" 		property="tpDesconto" dataType="text"/>
		<adsm:gridColumn width="90" title="filialOrigem" 		property="sgFilialOrigem" dataType="text"/>
		<adsm:gridColumn width="70" title="numero" 			property="nrDesconto" dataType="integer"/>
		<adsm:gridColumn width="100" title="situacao" 			property="tpSituacao" dataType="text"/>
        <adsm:gridColumn width="100" title="situacaoAprovacao" 	property="tpSituacaoAprovacao" dataType="text"/>
		
		<adsm:gridColumn width="" title="dataEmissao" 		property="dtEmissao" dataType="JTDate"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="valorDesconto" property="sgMoeda" dataType="text"/>	
			<adsm:gridColumn width="50" title="" property="dsSimbolo" dataType="text"/>
		</adsm:gridColumnGroup>							
		<adsm:gridColumn title="" property="vlDesconto" width="60" dataType="currency" align="right"/>	
		
		<adsm:buttonBar />

	</adsm:grid>
	
	
</adsm:window>
<script>
	function initWindow(eventObj){
	
	//mantem a última consulta realizada
	if( eventObj.name != 'tab_click' ){
		buscaDadosPadroes();
	}
		
		var tabGroup = getTabGroup(document);
		
		// controle das abas 
		if( eventObj.name == 'tab_click' ){
			tabGroup.setDisabledTab("cad", true);
			tabGroup.setDisabledTab("item", true);
		}
		
	}


	/**
	*	Busca dados da filial padrão do usuário logado
	*/
	function buscaDadosPadroes(){
	
		var dados = new Array();
         
        _serviceDataObjects = new Array();
            
        addServiceDataObject(createServiceDataObject("lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findFilialUsuario",
                                                     "retornoBuscaFilialUsuario",
                                                     dados));
        xmit(false);
	}
	
	/**
	*	Seta a filial do usuário como padrão.
	* 	Se filial usuario for 'MTZ' pesquisa somente filiais ativas
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}else{
		setElementValue('filial.idFilial', data.filial.idFilial);
		setElementValue('filial.sgFilial', data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		setElementValue('siglaFilial', data.filial.sgFilial);

		}
	}	

	
	/**
	*	Método chamado ao clicar numa linha da grid.
	*	Chama um findPaginated passando o id do item selecionado e o tipo de desconto
	*/
	function myOnRowClick(rowRef){
	
		getTabGroup(document).setDisabledTab("cad", false);
		getTabGroup(document).setDisabledTab("item", false);
	
		var dataGrid = null;
		
		for( var i = 0; i < descontoGridDef.gridState.data.length; i++ ){
			if( descontoGridDef.gridState.data[i].idGeral == rowRef ){
				dataGrid = descontoGridDef.gridState.data[i];
			}
		}
		
		if( dataGrid == null ){
			return false;
		}
		
		var dados = new Array();
         
        setNestedBeanPropertyValue(dados, "idReciboDemonstrativo", dataGrid.idReciboDemonstrativo);
        setNestedBeanPropertyValue(dados, "tpDescontoSelecionado", dataGrid.tipoDesconto);        
         
        var sdo = createServiceDataObject("lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findByIdFromReciboDemonstrativo",
                                          "retornoFindById",
                                          dados);
        xmit({serviceDataObjects:[sdo]}); 		
        
        return false;
		
	}
	
	/**
	*	Método de retorno do findPaginated
	*	Lança uma exception se essa ocorrer ou seta os dados retornados da pesquisa
	*/
	function retornoFindById_cb(data, erro){
	
		if( erro != undefined ){
			getTabGroup(document).setDisabledTab("cad", true);
			getTabGroup(document).setDisabledTab("item", true);
			alert(erro);
			setFocus(document.getElementById('tpDesconto'));
			return false;
		}	
		
		var documentCad = getTabGroup(this.document).getTab("cad").tabOwnerFrame.document;
			
		descontoGridDef.detailGridRow("setaDados", data);
		
	}
</script>