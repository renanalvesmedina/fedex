<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnPageLoad"
	service="lms.expedicao.manterCRTDespachantesAction">
	<adsm:form
		id="despachante.form"
		action="/expedicao/manterCRTDespachantes">

		<adsm:section
			caption="despachantes"
			width="75"/>

		<adsm:grid
			property="despachante"
			idProperty="idDespachante"
			service="lms.expedicao.manterCRTDespachantesAction.findDespachantesCtoIntGrid"
			gridHeight="60"
			width="488"
			onRowClick="rowClick"
			onPopulateRow="setChecked"
			onDataLoadCallBack="gridDataLoad"
			unique="true"
			mode="main"
			rows="7"
			showPagging="false"
			showGotoBox="false"
			scrollBars="vertical">

			<adsm:gridColumn
				title="nome"
				property="pessoa.nmPessoa"
				width="250"/>

			<adsm:gridColumn
				title="telefone"
				property="pessoa.enderecoPessoa.nrTelefone"
				align="right"
				width="148"/>

			<adsm:gridColumn
				title="local"
				isDomain="true"
				property="tpLocal"
				width="90"/>
		</adsm:grid>

		<adsm:buttonBar>
			<adsm:button
				onclick	="salvar()"
				id="btnSalvar"
				caption="salvar"/>
			<adsm:button
				id="btnFechar"
				onclick="self.close()"
				caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function salvar(){
		var ids = despachanteGridDef.getSelectedIds();

		var service = 'lms.expedicao.manterCRTDespachantesAction.storeInSession';
		var sdo = createServiceDataObject(service, 'storeSession', ids);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}

		self.close();
	}
	/************************************************************\
	*
	\************************************************************/
	function setChecked(objTr, data){
		var objChk = objTr.firstChild.firstChild;
		objChk.checked = (data.isChecked == 'S');
	}
	/************************************************************\
	*
	\************************************************************/
	function gridDataLoad_cb(data){
		var numRows = despachanteGridDef.getRowCount()
		var numRowsCheckeds = despachanteGridDef.getSelectedIds().ids;
		if(numRows > 0 && numRows == numRowsCheckeds.length){
			getElement('despachante.chkSelectAll').click();
		}
		findIdCtoInternacional();
	}
	/************************************************************\
	*
	\************************************************************/
	function populaGrid() {
		var url = new URL(parent.location.href);
		var idCliente = url.parameters['idCliente'];
		var criteria = {};

		if(idCliente){
			criteria.idCliente = idCliente;
		}
		despachanteGridDef.executeSearch(criteria, true);
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad(){
		onPageLoad();
		populaGrid();
    }
    /************************************************************\
	*
	\************************************************************/
    function findIdCtoInternacional(){
    	var service = 'lms.expedicao.manterCRTDespachantesAction.getIdCtoInternacionalInSession';
		var sdo = createServiceDataObject(service, 'populateWindow', {});
		xmit({serviceDataObjects:[sdo]});
    }
    /************************************************************\
	*
	\************************************************************/
	function populateWindow_cb(data, error){
		var modo = dialogArguments.MODO_TELA;

		var isDisable = (modo != 'INCLUSAO');

		setDisabled(document, isDisable);
		setDisabled('btnSalvar', isDisable);
		setDisabled('btnFechar', false);
		setFocus('btnFechar', false);
	}
    /************************************************************\
	*
	\************************************************************/
	function rowClick(valor) {
		return false;
	}
</script>