<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function loadDetalheRota_cb(data, exception){
		onDataLoad_cb(data, exception);
		setElementValue("idDetalheRota",data.idDetalheRota);
	} 


	function onRemoveButtonClick() {
		removeButtonScript('lms.gm.detalheRotaService.removeById', 'customRemoveById', 'idDetalheRota', this.document);
	}

	//Callback da exclusao... apos excluir recarrega a lista de rotaEmbarque
	function customRemoveById_cb(data,error){
		if (error != undefined){
			alert(error);
		}
		newButtonScript();

		//Busca a lista de rota embarque
		var sdo = createServiceDataObject("lms.gm.rotaEmbarqueService.find", "reloadCombo");
		xmit({serviceDataObjects:[sdo]});
	}

	
	function reloadCombo_cb(data,error){
		if (error != undefined){
			alert(error);
		}else{
			comboboxLoadOptions({e:getElement("idRotaEmbarque"), data:data});

			//recarrega a combo da aba listagem
			var tabGroup = getTabGroup(this.document);
			var tabList = tabGroup.getTab("pesq");

			tabList.tabOwnerFrame.location.reload();

		}
	}		
</script>
<adsm:window service="lms.gm.detalheRotaService">
	<adsm:form action="/expedicao/manterRotaVolumeGM" idProperty="idDetalheRota" onDataLoadCallBack="loadDetalheRota">
	<adsm:hidden property="idDetalheRota"/>
		<adsm:textbox 
			dataType="text" 
			property="siglaRota" 
			label="sigla" 
			labelWidth="15%" 
			maxLength="5" 
			size="15" 
			required="true"
		/>
		<adsm:combobox 
			property="idRotaEmbarque" 
			optionLabelProperty="siglaRota" 
			optionProperty="idRotaEmbarque"
			service="lms.gm.rotaEmbarqueService.find" 
			label="rotaMaster" 
			boxWidth="240" 
			width="45%"
			labelWidth="15%" 
			onlyActiveValues="true"
			required="true"
		/>
		<adsm:textbox 
			dataType="text" 
			labelWidth="15%" 
			property="nomeRota" 
			label="nomeRota" 
			maxLength="25" 
			size="40" 
			width="45%"
			required="true"
		/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:button caption="excluir" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
