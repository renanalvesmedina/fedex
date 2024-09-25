<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.carregamento.carregarVeiculoAction">
	<adsm:form action="/carregamento/carregarVeiculo" idProperty="idControleCarga">
	
		<adsm:hidden property="tpControleCarga"/>
	
		<adsm:textbox property="controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text" 
			label="controleCargas" size="3" maxLength="3" labelWidth="25%" width="75%" disabled="true" serializable="false" >
			<adsm:textbox property="controleCarga.nrControleCarga" dataType="text" 
						  maxValue="8" size="8" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox property="sgFilialPostoAvancado" dataType="text" 
					  label="postoAvancado" maxValue="3" size="3" labelWidth="25%" width="75%" disabled="true" serializable="false" >
			<adsm:textbox property="nmPessoaPostoAvancado" dataType="text" maxLength="50" size="50" disabled="true" serializable="false" />
		</adsm:textbox>
				
		<adsm:textbox property="sgFilial"  dataType="text" 
					  label="filial" maxValue="3" size="3" labelWidth="25%" width="75%" disabled="true" serializable="false" >
			<adsm:textbox property="nmPessoa" dataType="text" maxLength="50" size="50" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox property="moeda" dataType="text" 
					  label="totalCarregado" disabled="true" maxLength="8" size="8" labelWidth="25%" width="75%" serializable="false" >
			<adsm:textbox property="totalCarregado" dataType="currency" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox property="moedaSeguroMercurio" dataType="text" 
					  label="totalCarregadoSeguroMercurio" disabled="true" maxLength="8" size="8" labelWidth="25%" width="75%" serializable="false" >
			<adsm:textbox property="totalCarregadoSeguroMercurio" dataType="currency" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="moedaSeguroCliente" dataType="text" 
					  label="totalCarregadoSeguroCliente" disabled="true" maxLength="8" size="8" labelWidth="25%" width="75%" serializable="false" >
			<adsm:textbox property="totalCarregadoSeguroCliente" dataType="currency" disabled="true" serializable="false" />
		</adsm:textbox>
		
	</adsm:form>
	<adsm:grid property="exigenciasConteudoAtual" idProperty="idExigenciaGerRisco" title="exigenciasConteudoAtual" 
			   service="lms.carregamento.carregarVeiculoAction.findPaginatedExigenciasGerRisco" 
               selectionMode="none" showPagging="false" autoSearch="false" onRowClick="exigenciasConteudoAtual_OnClick">
        <adsm:gridColumn title="exigencia" property="dsResumida" width="90%"/>
        <adsm:gridColumn title="detalhe"   property="detalhe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosRiscoDetalhe.do?cmd=main" popupDimension="550,220" width="10%" align="center" linkIdProperty="idExigenciaGerRisco"/>
    	<adsm:buttonBar/>  
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name=="tab_click")  {
			newButtonScript();
			loadDadosCarregamento();
			fillValoresManifesto();
		}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Carrega os dados da aba de carregamento.
	 */ 
	function loadDadosCarregamento() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("carregamento");
		
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial"));
		setElementValue("controleCarga.nrControleCarga", tabDet.getFormProperty("controleCarga.nrControleCarga"));
		setElementValue("idControleCarga", tabDet.getFormProperty("controleCarga.idControleCarga"));
		setElementValue("sgFilial", tabDet.getFormProperty("sgFilial"));
		setElementValue("nmPessoa", tabDet.getFormProperty("nmPessoa"));
		setElementValue("sgFilialPostoAvancado", tabDet.getFormProperty("sgFilialPostoAvancado"));
		setElementValue("nmPessoaPostoAvancado", tabDet.getFormProperty("nmPessoaPostoAvancado"));
		setElementValue("tpControleCarga", tabDet.getElementById("tpControleCargaValue").value);
		
	}
	
	/**
	 * Carrega os campos com os valores dos manifestos na pagina.
	 */
	function fillValoresManifesto() {
		var idControleCargaValue = getElementValue("idControleCarga");
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.getValoresManifestos", "fillValoresManifesto", {idControleCarga:idControleCargaValue});
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Preenche os campos da tela com os valores retornados da pesquisa de valoresManifesto
	 */
	function fillValoresManifesto_cb(data, error) {
		if (data._exception==undefined) {
			setElementValue("moeda", data.moeda.dsMoeda);
			setElementValue("moedaSeguroMercurio", data.moeda.dsMoeda);
			setElementValue("moedaSeguroCliente", data.moeda.dsMoeda);
			
			setElementValue("totalCarregado", setFormat(document.getElementById("totalCarregado"), data.moeda.totalCarregado));
			setElementValue("totalCarregadoSeguroMercurio", setFormat(document.getElementById("totalCarregado"), data.moeda.totalCarregadoSeguroMercurio));
			setElementValue("totalCarregadoSeguroCliente", setFormat(document.getElementById("totalCarregado"), data.moeda.totalCarregadoSeguroCliente));
			
		} else {
			alert(data._exception._message);
		}
		
		carregaDadosGrid();
	}
	
	function carregaDadosGrid() {
		var filtro = new Array();
		setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
	    setNestedBeanPropertyValue(filtro, "tpControleCarga", getElementValue("tpControleCarga"));
	    exigenciasConteudoAtualGridDef.executeSearch(filtro, true);
	}
	
	function exigenciasConteudoAtual_OnClick(id) {
    	return false;
    }
</script>