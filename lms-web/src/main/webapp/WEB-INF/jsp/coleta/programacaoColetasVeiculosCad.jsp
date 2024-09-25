<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" >
	<adsm:form action="/coleta/programacaoColetasVeiculos" idProperty="idControleCarga" onDataLoadCallBack="carregaDados_retorno"
			   service="lms.coleta.programacaoColetasVeiculosAction.findByIdControleCarga" >

		<adsm:hidden property="nrControleCarga" serializable="false" />
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false" />
		<adsm:hidden property="filialByIdFilialOrigem.sgFilial" serializable="false" />
		
		<adsm:hidden property="vlAColetar" serializable="false" />
		<adsm:hidden property="vlTotalFrota" serializable="false" />
		
		<adsm:textbox label="numeroColeta" property="manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" dataType="text" 
			  		  size="3" width="35%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.pedidoColeta.nrColeta" dataType="text" size="10" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte"/>
		<adsm:textbox label="meioTransporte" property="meioTransporteByIdTransportado.nrFrota" dataType="text" width="35%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" size="19" serializable="false" disabled="true" />
		</adsm:textbox>
	</adsm:form>

	<adsm:tabGroup  selectedTab="0" >
		<adsm:tab title="coletasRealizadas" id="realizadas" src="/coleta/programacaoColetasVeiculosRealizadas" cmd="realizadas" boxWidth="180" height="370" />
		<adsm:tab title="entregasRealizar" id="realizar" src="/coleta/programacaoColetasVeiculosEntregasRealizar" cmd="realizar" boxWidth="180" height="370" />
		<adsm:tab title="entregasParciaisRealizar" id="parciais" src="/coleta/programacaoColetasVeiculosEntregasParciaisRealizar" cmd="parciais" boxWidth="180" height="370" />
	</adsm:tabGroup >
</adsm:window>

<script>
function povoaDadosMaster() {
    var tabDet = getTabGroup(this.document).getTab("veiculo");
    var filialColeta = tabDet.getFormProperty("filialByIdFilialResponsavel.sgFilial");
    var numeroColeta = tabDet.getFormProperty("nrColeta");
    setElementValue("manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" , filialColeta);
    setElementValue("manifestoColeta.pedidoColeta.nrColeta" , numeroColeta);
}

function carregaDados_retorno_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	onDataLoad_cb(data);
	setElementValue('idControleCarga', data.idControleCarga);
	setElementValue('nrControleCarga', data.nrControleCarga);
	setElementValue('filialByIdFilialOrigem.idFilial', data.filialByIdFilialOrigem.idFilial);
	setElementValue('filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);
	
	povoaDadosMaster();

	var tabGroupVeiculo = getTabGroup(this.document);
 	var tabDetVeiculo = tabGroupVeiculo.getTab("veiculo");
    var blRedirecionar = tabDetVeiculo.getFormProperty("blRedirecionar");
    // Após transmitir a coleta, se existir procedimentos ger. de risco, deve redirecionar para a aba de ger. riscos
    if (blRedirecionar != undefined && blRedirecionar != "") {
		tabGroupVeiculo.selectTab('riscos', {name:'tab_click'}); 
	}
	else {
		tabGroup.selectTab('realizadas');
		tabGroup.getTab("realizadas").tabOwnerFrame.window.povoaGrid();
	}
}
</script>