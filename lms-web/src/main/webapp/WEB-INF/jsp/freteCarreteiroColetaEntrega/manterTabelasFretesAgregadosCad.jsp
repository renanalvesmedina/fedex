<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1);
			getTabGroup(this.document).setDisabledTab("pesq",true);
		}else{
		    var data = new Array();
		    var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findInformacoesUsuarioLogado", "filialSession",data);
		    xmit({serviceDataObjects:[sdo]});
		}

	}

	function changeTipoCalculo(){
		ParcelaTabelaCeGridDef.executeSearch(
				{	idTabelaColetaEntrega:getElementValue("idTabelaColetaEntrega"),
					tpCalculo:getElementValue("tpCalculo")
				}
		);
		resetValue("cliente.idCliente");
		resetValue("rotaColetaEntrega.idRotaColetaEntrega");

		var tabGroup = getTabGroup(this.document);
		
		if(getElementValue("tpCalculo") == 'C1'){
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").required = true;
			document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").required = false;
			setDisabled("rotaColetaEntrega.idRotaColetaEntrega",true);
			setDisabled("cliente.idCliente",true);
			tabGroup.setDisabledTab("faixaPeso", true);
			
			ParcelaTabelaCeGridDef.addColumn({name:"pcSobreValor",visible:true,isDomain:false,formatObj:{dataType:"currency",mask:"##0.00"},renderMode:3, editGrid:{}}); 
			ParcelaTabelaCeGridDef.resetGrid();
			
		} else if(getElementValue("tpCalculo") == 'C2'){
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").required = false;
			document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").required = true;
			setDisabled("rotaColetaEntrega.idRotaColetaEntrega",false);
			setDisabled("cliente.idCliente",false);
			tabGroup.setDisabledTab("faixaPeso", false);
			
			ParcelaTabelaCeGridDef.addColumn({name:"pcSobreValor",visible:true,isDomain:false,formatObj:{dataType:"currency",mask:"##0.00"},renderMode:3, editGrid:{}}); 
			ParcelaTabelaCeGridDef.resetGrid();
		}	
	
		removeItensFromSession();
	}
	

</script>


<adsm:window title="manterTabelasFretesAgregados" 
		service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction" 
		onPageLoadCallBack="carregaDados" >
	
	<adsm:form idProperty="idTabelaColetaEntrega" 
			action="/freteCarreteiroColetaEntrega/manterTabelasFretesAgregados"  
			onDataLoadCallBack="pageLoad" 
			service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findByIdDetalhamento" >
		
		<adsm:hidden property="idFilialSessao" />
		<adsm:hidden property="sgFilialSessao" />
		<adsm:hidden property="nmFilialSessao" />
		<adsm:hidden property="disableTabListagem" />
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="acaoVigenciaAtual" serializable="false"/>
		
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="18%"  width="32%" minLengthForAutoPopUpSearch="3"
				exactMatch="false"  disabled="false" required="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"  disabled="true" size="25"  />			
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tipoTabela" 
					   service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findComboTipoTabelaColetaEntrega" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" onlyActiveValues="true" required="true"
					   labelWidth="18%" width="26%" boxWidth="180"/>
					   
		<adsm:combobox property="tpCalculo" 
			label="tpCalculo"
			domain="DM_TP_CALCULO_TABELA_COLETA_ENTREGA"  
			onchange="comboboxChange({e:this});changeTipoCalculo();"
			labelWidth="18%" width="32%" boxWidth="150" required="true" onlyActiveValues="true"/>
		
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" 
			optionLabelProperty="dsTipoMeioTransporte" 
			optionProperty="idTipoMeioTransporte"  onlyActiveValues="true"
			service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findComboTpMeioTransporte" 
			label="tipoMeioTransporte" labelWidth="18%" width="32%" boxWidth="180" />
		
		<adsm:range label="vigencia" labelWidth="18%" width="32%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false" />
		
		<adsm:lookup label="rota" size="3" maxLength="3" labelWidth="18%" width="82%" 
					 exactMatch="true"
					 dataType="integer" 
					 property="rotaColetaEntrega" 
					 idProperty="idRotaColetaEntrega" 
					 criteriaProperty="nrRota"
					 service="lms.coleta.consultaClientesColetaAction.findLookupRotaColetaEntrega"
					 action="/municipios/manterRotaColetaEntrega" >

			 <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
 		    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
 		    
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" inlineQuery="false"/>
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
        
        <adsm:lookup dataType="text" property="cliente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" 
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupCliente" 
				label="cliente" size="17" maxLength="20" labelWidth="18%" width="80%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
        
	</adsm:form>
		<adsm:grid property="ParcelaTabelaCe"  idProperty="id" autoSearch="false" 
				selectionMode="none" showRowIndex="false" autoAddNew="false" gridHeight="120" 
 	    		showGotoBox="false" showPagging="false" showTotalPageCount="false"
 	    		service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findGridParcelas"
 	    		maxRows="7" rows="7"  onRowClick="returnFalse();" onDataLoadCallBack="dataLoadGrid" > 

			<adsm:gridColumn property="dsParcela" title="tipoParcela"/>
			<adsm:editColumn property="tpParcela" dataType="text"  title="valorDefinido"  field="hidden"  />
			<adsm:editColumn property="vlDefinido" dataType="currency" title="valorDefinido" field="textbox" align="right" mask="#,###,###,###,###,##0.00"/>
			
			<adsm:editColumn property="pcSobreValor" dataType="text"  title="percentualParcela"  field="hidden"  />
			<adsm:gridColumn property="pcSobreValor" title="percentualParcela"/>
			

		<adsm:buttonBar >
			<adsm:button id="storeButton"  caption="salvar"  onclick="store()"            disabled="false"/>
			<adsm:button id="botaoLimpar"  caption="limpar"  onclick="limpar_OnClick();"  disabled="false"/>
			<adsm:button id="botaoExcluir" caption="excluir" onclick="excluir_OnClick();" buttonType="removeButton"/>
		</adsm:buttonBar>
					
		</adsm:grid>
	
</adsm:window>
<script>
	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;
	
	function returnFalse(){
		return false;
	}
	function limpar_OnClick(){
		newButtonScript(this.document, true, {name:'newButton_click'});
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("faixaPeso", true);
	}
	
	function excluir_OnClick(){
	 	// Chama a exclusao padrao da tela
		removeButtonScript('lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.removeById', 'removeById', 'idTabelaColetaEntrega', document);
	}
	
	function dataLoadGrid_cb(data) {
		desabilitaCampos();
	}
	
	function initWindow(evento){
		if ( (evento.name == "tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id == "pesq") || evento.name == "newButton_click" || evento.name == "removeButton") {
			
			if(evento.name == "newButton_click"){
				var data = new Array();
	    		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findInformacoesUsuarioLogado", "filialSession",data);
	    		xmit({serviceDataObjects:[sdo]});
			}

			setDisabled(document,false);	
			
			//LMS-2637 - Campos de descricao do cliente e da rota devem estar desabilitados.
			desabilitaCamposRotaCliente(true);
			
			populaFilial();
			
			
			ParcelaTabelaCeGridDef.resetGrid();
			removeItensFromSession();
			
			loadGrid();
			
			resetValue('acaoVigenciaAtual');
			setFocusOnFirstFocusableField();
		} else {
			if(getElementValue("idTabelaColetaEntrega") == ""){
			 	setDisabled("botaoLimpar",false);
				setDisabled("storeButton",false);
				setDisabled("botaoExcluir",true);
			} else {
				desabilitaCampos();
			}
			
		}
		
	}

	function removeItensFromSession(){
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.cleanItensSession", null,null);
		xmit({serviceDataObjects:[sdo]});
	}

	// Faz a verificacao e salva os dois forms 
	function store() {
		getTabGroup(this.document).selectTab("cad");
		validaPercentual();
		storeEditGridScript('lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.storeCustom', 'afterStore', document.forms[0], document.forms[1]);
	}
	
	function validaPercentual(){
		if(getElementValue("tpCalculo") == 'C2'){
			setaObrigatorio(3);
			setaObrigatorio(4);
		}
	}
	
	function setaObrigatorio(indice){
		var peso = getElementValue("ParcelaTabelaCe:"+indice+".pcSobreValor");
		var valor = getElementValue("ParcelaTabelaCe:"+indice+".vlDefinido");
				
		
		if( (peso != "" && valor == "") ||(peso == "" && valor != "") ){
			document.getElementById("ParcelaTabelaCe:"+indice+".pcSobreValor").required='true';
			document.getElementById("ParcelaTabelaCe:"+indice+".vlDefinido").required='true';
		}else{
			document.getElementById("ParcelaTabelaCe:"+indice+".pcSobreValor").required='false';
			document.getElementById("ParcelaTabelaCe:"+indice+".vlDefinido").required='false';
		}
	}
	
	
	function populaFilial() {
		setElementValue("filial.idFilial",getElementValue("idFilialSessao"));	
		setElementValue("filial.sgFilial",getElementValue("sgFilialSessao"));	
		setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
		
	 	setDisabled("filial.pessoa.nmFantasia",true);		
	}
	
	function filialSession_cb(data) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
		populaFilial();
	}

	// Metodo e chamdoM�todo � chamado depois que � gravado os dados no banco
	function afterStore_cb(data,error){
		store_cb(data,error);
		if (error == undefined) {
			desabilitaCampos(data,error);
		
			var parcelas = getNestedBeanPropertyValue(data,"ParcelaTabelaCe");
			if (parcelas != undefined) {
				
				for (var x = 0; x < parcelas.length; x++) {			
					setElementValue("ParcelaTabelaCe:" + (x) + ".id",getNestedBeanPropertyValue(parcelas[x],"id"));
				}
				
			}
			//setFocus("botaoLimpar",false);
			setDisabled(document,true);	
			setDisabled("botaoLimpar",false);
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("faixaPeso", true);
		}
	}
	
	function loadGrid() {
		ParcelaTabelaCeGridDef.executeSearch({idTabelaColetaEntrega:getElementValue("idTabelaColetaEntrega")});
	}

	function desabilitaCampos() {
		var acaoVigenciaAtual = getElementValue("acaoVigenciaAtual");
		if(acaoVigenciaAtual != ""){
			if (acaoVigenciaAtual == 0) {
				setDisabled(document,false);
				
				//LMS-2637 - Campos de descriÃ§Ã£o do cliente e da rota devem estar desabilitados.
				desabilitaCamposRotaCliente(true);
				
				setDisabled("filial.pessoa.nmFantasia",true);
				setFocus("filial.sgFilial");
			} else if (acaoVigenciaAtual == 1) {
				setDisabled(document,true);
			 	setDisabled("botaoLimpar",false);
				setDisabled("storeButton",false);
				setDisabled("botaoExcluir",true);
				setDisabled("dtVigenciaFinal",false);		
				setFocus("dtVigenciaFinal");
			} else if (acaoVigenciaAtual == 2) {
				setDisabled(document,true);
				setDisabled("botaoLimpar",false);
				setFocus("botaoLimpar",false);
			}	
		}
		
		setDisabled("filial.pessoa.nmFantasia",true);	
		if(getElementValue("tpCalculo") == 'C2'){
			try{
				var griddef = document.getElementById("ParcelaTabelaCe.dataTable");
				var i = 0;
				for (i = 0; i < griddef.rows.length; i++) {
					if(i == 3 || i == 4){
						continue;
					}
					desabilitar_pc(i);
				}
			}catch(E){
				//alert(E);
			}
		}
		
		if(getElementValue("tpCalculo") == 'C1'){
			try{
				var griddef = document.getElementById("ParcelaTabelaCe.dataTable");
				var x = 0;
				for (x = 0; x < griddef.rows.length; x++) {
					desabilitar_pc(x);
				}
			}catch(E){
				//alert(E);
			}
		}
	}
	
	//LMS-2637 - Campos de descriÃ§Ã£o do cliente e da rota devem estar desabilitados
	function desabilitaCamposRotaCliente(desabilita){
		setDisabled("rotaColetaEntrega.dsRota", desabilita);
		setDisabled("cliente.pessoa.nmPessoa", desabilita);
		
		if(getElementValue("tpCalculo") == 'C1'){
			setDisabled("rotaColetaEntrega.idRotaColetaEntrega", true);
			setDisabled("cliente.idCliente", true);
		}		
	}
	
	function desabilitar_pc(i){
		setDisabled("ParcelaTabelaCe:"+i+".pcSobreValor",true);
		
		var linha_parcela = document.getElementById("ParcelaTabelaCe:"+i+".pcSobreValor");
		linha_parcela.style.border = "none";
		linha_parcela.style.background="none";
	}
	
	function pageLoad_cb(data, error) {
		onDataLoad_cb(data,error);
		var tabGroup = getTabGroup(this.document);

		if (getElementValue("disableTabListagem") == "true") {
			tabGroup.setDisabledTab("pesq", true);
		}

		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));

		populaFilial();
		loadGrid();
		desabilitaCampos();
		
		setComboBoxElementValue(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"),
			 getNestedBeanPropertyValue(data,"tipoMeioTransporte.idTipoMeioTransporte"), 
			 getNestedBeanPropertyValue(data,"tipoMeioTransporte.idTipoMeioTransporte"),
			 getNestedBeanPropertyValue(data,"tipoMeioTransporte.dsTipoMeioTransporte"));

		setComboBoxElementValue(document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"),
			getNestedBeanPropertyValue(data,"tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"),
			getNestedBeanPropertyValue(data,"tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega"), 
			getNestedBeanPropertyValue(data,"tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega"));

		if (getElementValue("idProcessoWorkflow") != "")
			setDisabled(document,true);

		if(getElementValue("tpCalculo") == 'C1'){
			ParcelaTabelaCeGridDef.addColumn({name:"pcSobreValor",visible:true,isDomain:false,formatObj:{dataType:"currency",mask:"##0.00"},renderMode:3, editGrid:{}});
			tabGroup.setDisabledTab("faixaPeso", true);
		} else if(getElementValue("tpCalculo") == 'C2'){
			ParcelaTabelaCeGridDef.addColumn({name:"pcSobreValor",visible:true,isDomain:false,formatObj:{dataType:"currency",mask:"##0.00"},renderMode:3, editGrid:{}});
			tabGroup.setDisabledTab("faixaPeso", false);
		}
	} 
	

</script>