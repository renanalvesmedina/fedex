<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.monitoramentoConhecimentosSemAWBAction" onPageLoadCallBack="pageLoad">
	
	<adsm:form id="filter.form" action="/expedicao/monitoramentoConhecimentosSemAWB" >
	
		<adsm:hidden property="filialSessaoMatriz"/>
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-04159"/>
		</adsm:i18nLabels>
		
	 	<adsm:lookup 
	 		service="lms.municipios.filialService.findLookup" 
	 		dataType="text" 
	 		property="filial" 
	 		criteriaProperty="sgFilial" 
	 		label="filial" 
	 		size="3" 
	 		maxLength="3" 
	 		action="/municipios/manterFiliais" 
	 		idProperty="idFilial" 
	 		style="width:45px"
	 		labelWidth="3%"
	 		width="10%"
	 		required="true"
	 		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" width="25%"/>
		</adsm:lookup>
		
		<%-----------------------------%>
		<%-- PERIODO DE EMISSAO TEXT --%>
		<%-----------------------------%>
		<adsm:range label="periodoEmissao" labelWidth="13%" width="43%">
			<adsm:textbox 
				dataType="JTDate" 
				property="dataInicial"/>

			<adsm:textbox 
				dataType="JTDate" 
				property="dataFinal"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="listaConhecimentos"/>
			<adsm:button id="reset" caption="limpar" onclick="limpar()" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:form id="filter.form2" action="/expedicao/monitoramentoConhecimentosSemAWB" >
		<adsm:section caption="dadosCarga"/>
		
		<adsm:textbox label="qtdDocumentos" 
					  dataType="integer" 
					  property="qtdDocumentosTotal" 
					  labelWidth="18%" 
					  width="11%"
					  maxLength="5" 
					  size="6"
					  disabled="true" />
		
		<adsm:textbox label="valorTotalDaMercadoriaRs" 
					  dataType="currency"
					  property="vlMercadoriaTotal" 
					  labelWidth="21%" 
					  width="21%" 
					  maxLength="9"
					  size="9"
					  disabled="true" />
				 
		<adsm:textbox dataType="decimal" 
					  label="pesoRealTotal" 
					  property="psRealTotal" 
					  unit="kg" 
					  labelWidth="16%"
					  width="13%" 
					  maxLength="18"
					  size="8"
					  disabled="true" />
        
        <adsm:label key="branco" 
					style="border:none;" 
					width="29%" />
        
        <adsm:textbox label="qtdeTotalDeVolumes" 
					  dataType="integer" 
					  property="qtVolumesTotal" 
					  disabled="true" 
					  size="9" 
					  labelWidth="21%" 
					  maxLength="5"
					  width="21%" />
        
        <adsm:textbox dataType="decimal" 
					  label="pesoCubadoTotal" 
					  property="psCubadoTotal" 
					  disabled="true" 
					  unit="kg" 
					  labelWidth="16%" 
					  width="13%" 
					  maxLength="18"
					  size="8"/> 	
	</adsm:form>
	
		<adsm:grid idProperty="idConhecimento"
				   property="listaConhecimentos"
				   service="lms.expedicao.monitoramentoConhecimentosSemAWBAction.findConhecimentosSemVinculoAWB"
				   showPagging="false"
				   gridHeight="200"
				   autoSearch="false"
				   unique="false"
				   scrollBars="vertical"
				   onDataLoadCallBack="gridCallBack"
				   onSelectRow="selecionarRegistro"
				   onSelectAll="selecionarTodosRegistros"
				   onRowClick="clicarRegistro">
	
			<adsm:gridColumn title="documento" 
							 property="nrDocumento" />
			
			<adsm:gridColumn title="aeroportoDestino" 
							 property="aeroportoDestino.sgAeroporto"/>
			
			<adsm:gridColumn title="pesoKG"		 
							 property="doctoServico.psReal"
							 align="right"					     
						     dataType="weight"/>
			
			<adsm:gridColumn title="pesoCubado" 		 
							 property="doctoServico.psAforado"
							 align="right"
							 unit="kg"
							 dataType="weight"/>
						
			<adsm:gridColumn title="qtdeVolumes" 			 
							 property="qtVolumesFormatado"
							 align="right"
							 dataType="integer" />
			
			<adsm:gridColumn title="valorMerc" 			 
							 property="doctoServico.vlMercadoria"
							 dataType="currency"
							 align="right"
							 unit="reais"/>
							 
			<adsm:gridColumn title="dimensoes" 
	        				 property="dimensoes" 
	        				 image="/images/popup.gif" 
	        				 openPopup="true" 
	        				 width="170px"
	        				 link="javascript:openPopup"        				
	        				 align="center" 
	        				 linkIdProperty="idConhecimento"/>
	        				 
			<adsm:buttonBar>  
				<adsm:button id="consolidarCarga" 
					caption="consolidarCarga" 
					disabled="false"
					onclick="consolidarCargas();" />
			</adsm:buttonBar>
		</adsm:grid>				 
		
</adsm:window>

<script>
	
	function pageLoad_cb() {
		onPageLoad_cb();
		loadFilialUsuarioLogado();
		desabilitarBtnConsolidarCarga(true);
	}
	
	function loadFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.expedicao.monitoramentoConhecimentosSemAWBAction.loadFilialUsuarioLogado",
				"loadFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function loadFilialUsuarioLogado_cb(data,error) {
		var idFilial = getNestedBeanPropertyValue(data, "idFilial");
		var sgFilial = getNestedBeanPropertyValue(data, "sgFilial");
		var nmFilial = getNestedBeanPropertyValue(data, "pessoa.nmFantasia");
		var dataInicial = getNestedBeanPropertyValue(data, "dataInicial");
		var dataFinal = getNestedBeanPropertyValue(data, "dataFinal");
		var filialSessaoMatriz = getNestedBeanPropertyValue(data, "filialSessaoMatriz");
		
		setElementValue("dataInicial",setFormat("dataInicial", dataInicial));
		setElementValue("dataFinal", setFormat("dataInicial", dataFinal));
		setElementValue("filialSessaoMatriz", filialSessaoMatriz);
		
		if(idFilial !== null && idFilial !== "" && idFilial !== undefined) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			setDisabled("filial.idFilial", true);			
			setDisabled("filial.sgFilial", true);			
		}
		
		setDisabled("reset", false);
	}
	
	function loadGridConhecimento(){		
		var data = new Object();
		
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
		setNestedBeanPropertyValue(data, "dataInicial", getElementValue("dataInicial"));
		setNestedBeanPropertyValue(data, "dataFinal", getElementValue("dataFinal"));
		listaConhecimentosGridDef.executeSearch(data, true);		
	}
	
	function desabilitarBtnConsolidarCarga(valor){
		setDisabled('consolidarCarga', valor);
	}
	
	function atualizarDados(){
		limparTela();
		loadGridConhecimento();
	}
	
	function atualizarDadosTelaCallBack_cb(data){
		
	}
	
	function gridCallBack_cb(data, error){
		var param = new Object();
		
		setNestedBeanPropertyValue(param, "filial.idFilial", getElementValue("filial.idFilial"));
		setNestedBeanPropertyValue(param, "dataInicial", getElementValue("dataInicial"));
		setNestedBeanPropertyValue(param, "dataFinal", getElementValue("dataFinal"));
	
		var service = "lms.expedicao.monitoramentoConhecimentosSemAWBAction.findTotalizadores";
		var sdo = createServiceDataObject(service, "retornoGridCallBack", param);	
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function retornoGridCallBack_cb(data){
		setElementValue("qtdDocumentosTotal", data.qtdDocumentosTotal);
		setElementValue("vlMercadoriaTotal", data.vlMercadoriaTotal);	
		setElementValue("psRealTotal", data.psRealTotal);	
		setElementValue("qtVolumesTotal", data.qtVolumesTotal);	
		setElementValue("psCubadoTotal", data.psCubadoTotal);	
	}
	
	function consolidarCargas(){
		var gridData = listaConhecimentosGridDef.getSelectedIds();
		var dados = new Array();
		
		for(i=0; i < gridData.ids.length; i++) {
			var lineData = listaConhecimentosGridDef.findById(gridData.ids[i]);
			dados.push(lineData.idConhecimento);
		}
		var params = '&tela=acao&dados=' + dados;			 		
		
		showModalDialog('expedicao/digitarPreAWB.do?cmd=main'+ params, window,
			'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');		
	}
	
	function consolidarCarga_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		
		listaConhecimentosGridDef.executeSearch(undefined, true);		
	}
	
	function selecionarRegistro(rowRef) {
		var ids = listaConhecimentosGridDef.getSelectedIds();
		var status = (ids.ids.length > 0);
		desabilitarBtnConsolidarCarga(!status);			
	}
	
	function selecionarTodosRegistros(estado) {
		desabilitarBtnConsolidarCarga(!estado);
	}
	
	function openPopup(id){
		var param = '&idConhecimento=' + id;	
		
		showModalDialog('expedicao/monitoramentoConhecimentosSemAWB.do?cmd=popup' + param, window,
				'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:490px;dialogHeight:270px;');		
	}
	
	function clicarRegistro(id) {
		return false;
	}
	
	function limparTela(){
		listaConhecimentosGridDef.resetGrid();
		desabilitarBtnConsolidarCarga(true);
		if(getElementValue("filialSessaoMatriz") == "true") {
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");
		}
	}
	
	function limpar(){
		limparTela();
		limpaCampos();
		loadFilialUsuarioLogado();
	}
	
	function limpaCampos(){
		setElementValue("qtdDocumentosTotal", "");
		setElementValue("vlMercadoriaTotal", "");
		setElementValue("psRealTotal", "");
		setElementValue("qtVolumesTotal", "");
		setElementValue("psCubadoTotal", "");
	}
	
</script>