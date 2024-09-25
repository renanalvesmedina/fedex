<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.gm.detalheRotaService">
	<adsm:form action="/expedicao/manterRotaVolumeGM" idProperty="idDetalheRota">
		<adsm:textbox  labelWidth="15%" dataType="text" property="siglaRota" label="sigla" maxLength="5" size="15"/>
		<adsm:combobox  property="idRotaEmbarque" optionLabelProperty="siglaRota" optionProperty="idRotaEmbarque"  
						service="lms.gm.rotaEmbarqueService.find" label="rotaMaster" boxWidth="240" labelWidth="15%" width="45%"
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="detalheRota"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDetalheRota" property="detalheRota" gridHeight="200" defaultOrder="siglaRota" unique="true" rows="13">
		
		<adsm:gridColumn title="sigla" property="siglaRota" width="25%" />
		<adsm:gridColumn title="nomeRota" property="nomeRota"  width="75%" />
		
		<adsm:buttonBar>
			<adsm:button caption="excluir" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:grid>
	
<script>

function onRemoveButtonClick() {
	detalheRotaGridDef.removeByIds('lms.gm.detalheRotaService.removeByIds', 'remove');
}

//Callback da exclusao... apos excluir recarrega a lista de rotaEmbarque
function remove_cb(data,error){
	if (error != undefined){
		alert(error);
	}

	var data = buildFormBeanFromForm(document.forms[0]);
	detalheRotaGridDef.executeSearch(data);

	reloadCombo();
}

function reloadCombo(){
	//Busca a lista de rota embarque
	var sdo = createServiceDataObject("lms.gm.rotaEmbarqueService.find", "reloadCombo");
	xmit({serviceDataObjects:[sdo]});

	//recarrega a combo da aba listagem
	var tabGroup = getTabGroup(this.document);
	var tabList = tabGroup.getTab("cad");

	tabList.tabOwnerFrame.location.reload();
}


function reloadCombo_cb(data,error){
	if (error != undefined){
		alert(error);
	}else{
		comboboxLoadOptions({e:getElement("idRotaEmbarque"), data:data});
	}
}	

</script>

</adsm:window>