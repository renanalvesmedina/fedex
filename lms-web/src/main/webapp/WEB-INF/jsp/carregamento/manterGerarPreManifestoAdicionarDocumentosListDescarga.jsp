<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.manterGerarPreManifestoAction">

	<adsm:grid idProperty="idDoctoServico" property="descarga" gridHeight="162"
			   selectionMode="check" scrollBars="both" unique="false" autoSearch="false"
			   showGotoBox="false" showPagging="false" showTotalPageCount="false"
			   service="lms.carregamento.manterGerarPreManifestoAction.findListDescarga"
			   onRowClick="myOnRowClick" onPopulateRow="myOnPopulateRow" onDataLoadCallBack="calculaTotais"
			   onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow">
			   
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" isDomain="true" width="35"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="destino" property="filialByIdFilialDestino.sgFilial" width="60" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" property="psReal" dataType="decimal" mask="#,###,##0.000" align="right" unit="kg" width="70"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorMercadoria" property="moeda.sgMoeda" width="30" />		
			<adsm:gridColumn title="" property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right"/>		
		<adsm:gridColumn title="dpe" property="dtPrevEntrega" dataType="JTDate" width="100" align="center" />
		<adsm:gridColumn title="servico" property="servico.sgServico" width="50" />		
		<adsm:gridColumn title="dataEmissao" property="dhEmissao" dataType="JTDateTimeZone" width="150" align="center"/>
		<adsm:gridColumn title="remetente" property="clienteByIdClienteRemetente.pessoa.nmPessoa" width="200" />
		<adsm:gridColumn title="destinatario" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" width="200" />				   
	</adsm:grid>

	<adsm:form action="/carregamento/manterGerarPreManifestoAdicionarDocumentos" >
		<adsm:textbox label="totalDocumentos" property="totalDocumentos" dataType="integer" 
					  disabled="true" labelWidth="14%" width="11%" size="10"/>
		<adsm:textbox label="totalDeVolumes" property="totalVolumes" dataType="currency" 
					  disabled="true" labelWidth="12%" width="11%" size="10"/>
		<adsm:textbox label="totalDoPeso" property="totalPeso" dataType="currency" unit="kg"
					  disabled="true" labelWidth="10%" width="13%" size="10"/>
		<adsm:textbox label="totalMercadoria" property="totalMercadoria" dataType="currency" 
					  disabled="true" labelWidth="14%" width="14%" size="20"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="adicionar" id="adicionar" onclick="return adicionarDocumento();" buttonType="removeButton" />
  		    <adsm:button caption="fechar" id="fechar" onclick="javascript:window.close();"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "gridRow_click") {
			povoaGrid();
			setDisabled("adicionar", false);
			setDisabled("fechar", false);
			
			// Seta o nome da Tab selecionada na Tab pai.
			window.parent.setTabSelecionada("descarga");			
		}
	}	
	
	/**
	 * Função que popula a grid com registros de DoctoServico
	 */
    //Pode ser que não esteja mais sendo utilizada pela tela pai.
	function povoaGrid() {
		resetTotais();		
	   	var formPrincipal = buildFormBeanFromForm(window.parent.document.forms[0]);
	   	descargaGridDef.executeSearch(formPrincipal, true);
		return false;
	}
	
	/**
	 * Função que reseta a grid
	 */
	function resetGrid() {		
	   	descargaGridDef.resetGrid();
	}	
	
	/**
	 * Função que apaga os totais; Utilizada ao disparar uma nova consulta.
	 */
	function resetTotais(){
		resetValue("totalDocumentos");
		resetValue("totalVolumes");
		resetValue("totalPeso");
		resetValue("totalMercadoria");
	}
	
	/**
	 * Função que adiciona um documento da grid na tela pai.
	 */
	function adicionarDocumento() {
		if (descargaGridDef.getSelectedIds().ids[0] == undefined) {
			return;
		}
		
		var data = new Array();	
		merge(data, buildFormBeanFromForm(window.parent.document.forms[0]));
		merge(data, descargaGridDef.getSelectedIds());
		
		_serviceDataObjects = new Array();
		
		var actionAddress = "lms.carregamento.manterGerarPreManifestoAction.savePreManifestoDocumento";
		
		//Verifica se a tela esta sendo chamada da tela de carregarVeiculoDocumentos...
		var from = window.parent.window.document.getElementById("from").value;
		if (from=="carregamento") {
			actionAddress = "lms.carregamento.manterGerarPreManifestoAction.savePreManifestoDocumentoToCarregamento";
		} 
		
		addServiceDataObject(createServiceDataObject(actionAddress, "adicionarDocumento", data)); 
		xmit(true); // deseja que o alert seja exibido, a função store_cb irá mostrar caso ocorra erro.
	}
	
	/**
	 * Resultado da adição de documento na grid pai.
	 */
	function adicionarDocumento_cb(data, error, errorMsg, eventObj) {
		onDataLoad_cb(data, error, errorMsg, eventObj);
		
		if (error == undefined) {
			//Caso a tela nao esteja vindo do carregamento...
			var from = window.parent.window.document.getElementById("from").value;
			if (from!="carregamento") {
				dialogArguments.window.preManifestoDocumentoGridDef.executeSearch();
			}
			
			window.close();
		}
	}
	
	/**
	 * Função que retorna 'false' caso uma linha da grid tenha sido clicada.
	 */
	function myOnRowClick(id) {
		return false;
	}
	
	/**
	 * Verifica cada linha da grid (no momento em que ela esta sendo carregada) 
	 * possui DPE menos que a data atual, tpModal = 'A' e tipoServico.blPriorizar = 'S'
	 */
	function myOnPopulateRow(tr, data) {
		if(data.emDestaque == "true") {
			tr.style.backgroundColor = "#8FBFD6";
		}
	}
	
	/**
	 * Função q calcula os totais de volumes, pesos, mercadoria e documentos
	 */
	function calculaTotais_cb(data, error) {
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getCalculaTotais", "resultadoCalculaTotais", {list:data});
    	xmit({serviceDataObjects:[sdo]});
	}
	 
	/**
	 * Resultado do cálculo dos totais dos registros da grid
	 */
	function resultadoCalculaTotais_cb(data, error) {
		setElementValue("totalVolumes", data.totalVolumes);
	 	setElementValue("totalPeso", data.totalPeso);
	 	setElementValue("totalMercadoria", data.totalMercadoria);
	 	setElementValue("totalDocumentos", data.totalDocumentos);
	} 
	 
	/**
	 * Função que retorna 'false' caso tenha sido selecionado a opção de checkAll
	 */
	function myOnSelectAll(checked) {		
		var mapGrid = new Object();
		if( checked == true) {
			var formPrincipal = buildFormBeanFromForm(window.parent.document.forms[0]);						
			var rowList = new Array();			
			for(var i = 0; i < descargaGridDef.getRowCount(); i++) {
				var rowData = { checkedAll:true, 
								rowIndex:i, 
								doctoServico:descargaGridDef.gridState.data[i],
								manifesto:{ tpManifesto:formPrincipal.manifesto.tpManifesto,
											tpPreManifesto:formPrincipal.manifesto.tpPreManifesto,
											tpModal:formPrincipal.manifesto.tpModal,
											controleCarga:{ rotaColetaEntrega:{ 
												idRotaColetaEntrega:formPrincipal.controleCarga.rotaColetaEntrega.idRotaColetaEntrega } },
											cliente:{ idCliente:formPrincipal.doctoServico.clienteByIdClienteConsignatario.idCliente } },
								filialByIdFilialDestino:{ idFilial:formPrincipal.filialByIdFilialDestino.idFilial } };
							
			    rowList[i] = rowData;
			}
			mapGrid = {list:rowList};			
	
			var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getValidacaoDoctoServicoSelectAll", "resultadoValidacaoDoctoServicoSelectAll", mapGrid);
		   	xmit({serviceDataObjects:[sdo]});			
		}
	}
	
	/**
	 * Resultado da validação dos registros para todos os DoctoServico da grid 
	 */
	function resultadoValidacaoDoctoServicoSelectAll_cb(data, error) {
		if (data.length > 0) {
			for(var i = 0; i < data.length; i++) {
				document.getElementById("descarga:" + data[i].rowIndex + ".idDoctoServico").checked = false;				
			}
		}		
		return;
	} 
	
	/**
	 * Função chamada ao selecionar um registro na grid de DoctoServico, faz validações para 
	 * Agendamento, PCE, RRE, Rota, Bloqueio, Modal, Substituta.
	 */
	function myOnSelectRow(rowRef) {
		if(rowRef.firstChild.firstChild.checked) {
		    var mapGrid = new Array();
		    setNestedBeanPropertyValue(mapGrid, "checkedAll", "false");
			setNestedBeanPropertyValue(mapGrid, "rowIndex", rowRef.rowIndex);
		    setNestedBeanPropertyValue(mapGrid, "doctoServico", descargaGridDef.gridState.data[rowRef.rowIndex]);
		    var formPrincipal = buildFormBeanFromForm(window.parent.document.forms[0]);
		    setNestedBeanPropertyValue(mapGrid, "manifesto.tpManifesto", formPrincipal.manifesto.tpManifesto);
		    setNestedBeanPropertyValue(mapGrid, "manifesto.tpPreManifesto", formPrincipal.manifesto.tpPreManifesto);
		    setNestedBeanPropertyValue(mapGrid, "manifesto.tpModal", formPrincipal.manifesto.tpModal);
		    setNestedBeanPropertyValue(mapGrid, "manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", formPrincipal.controleCarga.rotaColetaEntrega.idRotaColetaEntrega);
		    setNestedBeanPropertyValue(mapGrid, "filialByIdFilialDestino.idFilial", formPrincipal.filialByIdFilialDestino.idFilial);
		    setNestedBeanPropertyValue(mapGrid, "manifesto.cliente.idCliente", formPrincipal.doctoServico.clienteByIdClienteConsignatario.idCliente);
		
			var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getValidacaoDoctoServico", "resultadoValidacaoDoctoServico", mapGrid);
	    	xmit({serviceDataObjects:[sdo]});		
		}				
	}
	
	/**
	 * Resultado da validação dos registros de DoctoServico da grid
	 */
	function resultadoValidacaoDoctoServico_cb(data, error) {
		if(data.mensagem) {
			if(data.confirmar) {
				var option = window.confirm(data.mensagem);
				if(option == false) {
					document.getElementById("descarga:" + data.rowIndex + ".idDoctoServico").checked = false;
					return;
				}
			} else {
				document.getElementById("descarga:" + data.rowIndex + ".idDoctoServico").checked = false;
				alert(data.mensagem);
				return;
			}
		}
		
		return;
	} 
	 	 
</script>