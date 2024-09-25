<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	/**
	 * Carrega página.
	 */	
	function carregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
	    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		dadosSessao = data;
		setaDadosSessao();
	}
	
</script>
<adsm:window service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosControleCarga">
		
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		<adsm:hidden property="blMatriz"/>
		
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return controleCargaFilial_OnChange();"
					 onDataLoadCallBack="retornoControleCargaFilial"
					 label="controleCargas" size="3" 
					 maxLength="3" picker="false" serializable="false" popupLabel="pesquisarFilial">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />					 
			<adsm:lookup dataType="integer" property="controleCarga" 
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.findControleCargaByNrControleByFilial" 
						 action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga" 
						 onDataLoadCallBack="loadDataByNrControleCarga" 
						 onchange="return checkValueControleCarga(this.value)"
						 maxLength="8" size="8" mask="00000000" required="true" popupLabel="pesquisarControleCarga">
				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial"/>
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
				
			</adsm:lookup>
		</adsm:lookup>	
	
		<adsm:hidden property="controleCarga.tpControleCarga.value"/>
		<adsm:textbox property="controleCarga.tpControleCarga.description" label="tipo" dataType="text" size="15" disabled="true"/>

		<adsm:textbox property="nrFrotaTransporte" label="meioTransporte" dataType="text" 
					  size="6" disabled="true">
			<adsm:textbox property="nrIdentificadorTransporte" dataType="text" 
						  size="25" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="nrFrotaSemiReboque" label="semiReboque" dataType="text" 
					  size="6" disabled="true">
			<adsm:textbox property="nrIdentificadorSemiReboque" dataType="text" 
						  size="25" disabled="true"/>					  
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="limpar" id="limpar" onclick="limpaTela()"/>
		</adsm:buttonBar>
		
		<script>
			// Mensagem: Somente a filial pode executar esta operação
			var lms00071 = '<adsm:label key="LMS-00071"/>';
			// Mensagem: Nenhum registro selecionado.
			var lms00053 = '<adsm:label key="LMS-00053"/>';
		</script>
		
	</adsm:form>

	<adsm:grid idProperty="idCustom" property="manifesto" selectionMode="check" showPagging="true" rows="13" 
			   service="lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.findPaginatedManifesto"
			   rowCountService="lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.getRowCountManifesto"
			   onRowClick="onRowClick" onSelectAll="onSelectAll" onSelectRow="onSelectRow" onPopulateRow="onPopulateRow">
		<adsm:gridColumnGroup separatorType="MANIFESTO">
    		<adsm:gridColumn title="manifesto" property="sgFilialOrigem" width="70"/>
    		<adsm:gridColumn property="nrManifesto" title="" dataType="integer" width="80" mask="00000000"/>
        </adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="150" dataType="text"/>
		<adsm:gridColumn title="tipoManifesto" property="tpManifesto" width="150" isDomain="true"/>
		<adsm:gridColumn title="bloqueado" property="blBloqueado" width="150" renderMode="image-check"/>
		<adsm:gridColumn title="documentos" property="documentos" align="center" image="/images/popup.gif" />
		<adsm:editColumn field="hidden" property="idManifesto" dataType="text" title="hidden" width="0"/>		
		<adsm:buttonBar>
			<adsm:button caption="registrarOcorrencia" id="registrarOcorrencia" 
						 onclick="openPopup()"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada ao iniciar a página.
	 */
	function initWindow(eventObj) {
		disableNrControleCarga(true);
		setDisabled("limpar", false);
	}

	/**
	 * Seta os Dados referente ao usuário da sessão.
	 */
	function setaDadosSessao() {
		setElementValue("blMatriz", dadosSessao.blMatriz);
	}

	/**
	 * Função que limpa o Form e a Grid.
	 */
	function limpaTela() {
		cleanButtonScript();
		manifestoGridDef.resetGrid();
		setaDadosSessao();
	}
	

	/**
	 * Controla o objeto de controle carga
	 */	
	function controleCargaFilial_OnChange() {
		var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		if (r==true && getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView();
		}
		return r;
	}
	
	/**
	 * Desabilita o campo numero do controle de carga
	 */
	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.nrControleCarga", disable);
	}
	
	function retornoControleCargaFilial_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
		if (r == true) {
			setDisabled('controleCarga.idControleCarga', false);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
	}
	
	/**
	 * Carrega os dados da tela de descarregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findControleCargaByNrControleByFilial'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		var r = controleCarga_nrControleCarga_exactMatch_cb(data);
	
		//Verifica se este objeto e nulo
		if (r==true && data[0]!=undefined) {
			setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", data[0].filialByIdFilialOrigem.pessoa.nmFantasia);			
			setElementValue("controleCarga.tpControleCarga.value", data[0].tpControleCarga.value);
			setElementValue("controleCarga.tpControleCarga.description", data[0].tpControleCarga.description);
			if(data[0].meioTransporteByIdTransportado) {
				setElementValue("nrFrotaTransporte", data[0].meioTransporteByIdTransportado.nrFrota);
				setElementValue("nrIdentificadorTransporte", data[0].meioTransporteByIdTransportado.nrIdentificador);
			}
			if(data[0].meioTransporteByIdSemiRebocado) {
				setElementValue("nrFrotaSemiReboque", data[0].meioTransporteByIdSemiRebocado.nrFrota);
				setElementValue("nrIdentificadorSemiReboque", data[0].meioTransporteByIdSemiRebocado.nrIdentificador);
			}
			
			// Formata o campo de nrControlecarga
			format(document.getElementById("controleCarga.nrControleCarga"));
			validateControleCarga(getElementValue("controleCarga.idControleCarga"));
		}
		return r;
	}

	function validateControleCarga(idControleCarga) {
	    var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.validateControleCarga", 
	    	"retornoControleCarga", {idControleCarga:idControleCarga});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	function retornoControleCarga_cb(data, error) {
		if (error != undefined) {
			alert(error);
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView();
			setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
			return false;
		}
		findButtonScript('manifesto', document.forms[0]);
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}


	/**
	 * Chama a consulta de 'findCarregamentoDescargaByNrControleCarga' a partir de um dos dados retornados 
	 * da lookup
	 */
	function loadDataByNrControleCarga(data) {
		setDisabled("controleCarga.nrControleCarga", false);
		//setNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial", getElementValue("controleCarga.filialByIdFilialOrigem.idFilial"));		
		var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.findControleCargaByNrControleByFilial", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});
	}	
		
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {		
		var r = controleCarga_nrControleCargaOnChangeHandler();
		if (r==true && valor=="") {			
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
            var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
            resetView();
            setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		}
		return r;
	}
	
	/**
	 * Reseta a tela
	 */
	function resetView(){
		resetValue(this.document);
		manifestoGridDef.resetGrid();
		setaDadosSessao();
	}
	
	/**
	 *	Abre a pop-up de documentos a partir da grid
	 */
	function carregaDocumentos(idCustom) {
		var idManifesto = null;
		var nrManifesto = null;
		var sgFilial = null;
		
		for (var i=0; i < manifestoGridDef.gridState.data.length; i++) {
			if (manifestoGridDef.gridState.data[i].idCustom == idCustom){
				idManifesto = manifestoGridDef.gridState.data[i].idManifesto;
				nrManifesto = manifestoGridDef.gridState.data[i].nrManifesto;
				sgFilialOrigem = manifestoGridDef.gridState.data[i].sgFilialOrigem;
				break;
			}
		}			
		var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;		
		showModalDialog("/pendencia/registrarOcorrenciasDocumentosControleCarga.do?cmd=documentos" + dadosURL, window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}
	
	/**
	 *	Abre a pop-up de Pedidos Coletas a partir da grid
	 */
	function carregaDocumentosColetas(idCustom) {
		var idManifesto = null;
		var nrManifesto = null;
		var sgFilial = null;
		
		for (var i=0; i < manifestoGridDef.gridState.data.length; i++) {
			if (manifestoGridDef.gridState.data[i].idCustom == idCustom){
				idManifesto = manifestoGridDef.gridState.data[i].idManifesto;
				nrManifesto = manifestoGridDef.gridState.data[i].nrManifesto;
				sgFilialOrigem = manifestoGridDef.gridState.data[i].sgFilialOrigem;
				break;
			}
		}			
		var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;		
		showModalDialog("/pendencia/registrarOcorrenciasDocumentosControleCarga.do?cmd=documentosColetas" + dadosURL, window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}	
	
	
	/**
	 * Função que retorna 'false' caso uma linha da grid tenha sido clicada.
	 */
	function onRowClick(id) {
		return false;
	}	
		
	/**
	 * Função que retorna 'false' caso tenha sido selecionado a opção de checkAll
	 */
	function onSelectAll() {
		return false;		
	}
	
	/**
	 * Função chamada ao selecionar um registro na grid para validação de situações
	 */
	function onSelectRow(rowRef) {
		if(getElementValue("blMatriz") == "true"){		
			if(rowRef.firstChild.firstChild.checked) {
			    var mapGrid = new Array();
			    dataGrid = manifestoGridDef.gridState.data[rowRef.rowIndex];
			    setNestedBeanPropertyValue(mapGrid, "rowIndex", rowRef.rowIndex);
			    setNestedBeanPropertyValue(mapGrid, "manifesto", dataGrid);
				
				var listManifestos = new Array();
				var count = 0;
				var ids = manifestoGridDef.getSelectedIds().ids;
				if(ids.length > 1) {
					for(var i=0; i<ids.length; i++) {
						if(ids[i] != dataGrid.idManifesto) {
							var manifesto = new Array();
							setNestedBeanPropertyValue(manifesto, "manifesto", manifestoGridDef.getDataRowById(ids[i]));
							listManifestos[count] = manifesto;
							count++;
						}
					}
					setNestedBeanPropertyValue(mapGrid, "listManifestos", listManifestos);
				}
				var sdo = createServiceDataObject("lms.pendencia.registrarOcorrenciasDocumentosControleCargaAction.validacaoManifesto", "validacaoManifesto", mapGrid);
		    	xmit({serviceDataObjects:[sdo]});
			}
		} else {
			//Somente a matriz pode executar esta operação.
			alert(lms00071);
			document.getElementById("manifesto:" + rowRef.rowIndex + ".idCustom").checked = false;
			if (document.getElementById("manifesto.chkSelectAll").checked == true){
				document.getElementById("manifesto.chkSelectAll").checked = false;
			}
			//setDisabled("registrarOcorrencia", true);
		}
	}
	
	/**
	 * Função do resultado da validação do Manifesto.
	 */
	function validacaoManifesto_cb(data, error) {
		if(data.error) {
			alert(data.error);
			document.getElementById("manifesto:" + data.rowIndex + ".idCustom").checked = false;
			if (document.getElementById("manifesto.chkSelectAll").checked == true){
				document.getElementById("manifesto.chkSelectAll").checked = false;
			}
		} else if(data.idDoctoServico) {
			setElementValue("doctoServico.idDoctoServico", data.idDoctoServico);
		}
	}	
	
	function openPopup(){
		if (validaBotaoRegistrar()){
			var modal = showModalDialog('pendencia/registrarOcorrenciasDocumentosControleCarga.do?cmd=registrar',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
			findButtonScript('manifesto', document.forms[0]);
		}
	}
	
	/**
	 * Função que valida o botão registrar.
	 */
	function validaBotaoRegistrar(data, error) {		
		var ids = manifestoGridDef.getSelectedIds().ids;
		if(ids.length == 0) {
			alert(lms00053);
			return false;
		} else {
			if(getElementValue("blMatriz") != "true") {
				alert(lms00071);
				return false;
			}	
		}
		return true;

	}
	
	/**
	 * Função que povoa a grid de manifestos corretamente para chamar a popup de coletas 
	 * ou de documentos de serviço, dependendo do manifesto em questão.	 
	 */
	function onPopulateRow(tr, data) {
		var tdblob = tr.children[5].innerHTML;
		var blob = tdblob.substring(tdblob.indexOf("</IMG>") + 6, tdblob.indexOf("</A>")).replace(" ","");
		fakeDiv = document.createElement("<DIV></DIV>");
		
		if(data.tpManifesto.value == "C") {
			fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:carregaDocumentosColetas('" + data.idCustom + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/popup.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";			
		} else {
			fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:carregaDocumentos('" + data.idCustom + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/popup.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		}
		tr.children[5].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
	}
	
	/**
 * Função para pegar os verdadeiros ids dos registros selecionados.
 */
function selectedIds() 
{
	var gridFormElems = document.getElementById("manifesto.form").elements;
	// array que armazena os ids das linhas que dever?o ser removidas da camada de dados.
	var selectedIds = new Array();
	for (var j = 0; j < gridFormElems.length; j++) 
	{
		if ((gridFormElems[j].type == "checkbox") || (gridFormElems[j].type == "radio")) {
			if ((gridFormElems[j].name.indexOf(".idManifesto")>0) || (gridFormElems[j].type == "radio")) {
				if (gridFormElems[j].checked) {
					if (gridFormElems[j].value != "undefined" && gridFormElems[j].value != "null") {
						selectedIds.push(gridFormElems[j].value);
					}
				}
			}
		}
	}
	var selMap = new Object();
	selMap["ids"] = selectedIds;
	return selMap;
}	
</script>