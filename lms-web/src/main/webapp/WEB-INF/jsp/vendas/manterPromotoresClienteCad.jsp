<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPromotoresClienteAction" onPageLoadCallBack="myPageLoad">

	<adsm:form action="/vendas/manterPromotoresCliente" idProperty="idPromotorCliente">

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" labelWidth="20%" width="80%" serializable="false" >
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:lookup label="promotor" property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" dataType="text"
			service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor"
			action="/configuracoes/consultarFuncionarios" cmd="promotor"
			size="16"  maxLength="16" required="true"
			labelWidth="20%" width="80%">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" inlineQuery="true"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:combobox label="modal" property="tpModal" domain="DM_MODAL" 		 
			labelWidth="20%" width="40%"/>
		<adsm:combobox label="abrangencia" property="tpAbrangencia" domain="DM_ABRANGENCIA" 
			labelWidth="20%" width="20%"/>

		<adsm:textbox label="dataInicioPromotor" property="dtInicioPromotor" dataType="JTDate" required="true"	
			labelWidth="20%" width="40%" onchange="onChangeDataInicio()"/>
		<adsm:textbox label="dataFimPromotor" property="dtFimPromotor"  dataType="JTDate" required="false" 
			labelWidth="20%" width="20%"/>

		<adsm:textbox label="dataPrimeiroPromotor" property="dtPrimeiroPromotor" dataType="JTDate" disabled="true" picker="false"
			labelWidth="20%" width="40%"/>
		<adsm:textbox label="dataReconquista" property="dtReconquista" dataType="JTDate" 
			labelWidth="20%" width="20%"/>
		
		<adsm:textbox label="dataInclusao" property="dtInclusao" dataType="JTDate" required="true" disabled="true" 
			labelWidth="20%" width="40%"/>
		<adsm:textbox label="percentualComissaoReconquista" property="pcReconquista" dataType="decimal" required="false" 
			mask="##0.00" size="10" maxLength="5" minValue="0.01" maxValue="100.00"
			labelWidth="20%" width="20%"/>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>		
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	var defaultData;
	function initWindow(eventObj) {
		setDefaultData();
	}

	function myPageLoad_cb() {
		onPageLoad_cb();
		findDefaultData();
	}

	function findDefaultData() {
		var sdo = createServiceDataObject("lms.vendas.manterPromotoresClienteAction.findDefaultData", "findDefaultData");
		xmit({serviceDataObjects:[sdo]});
	}

	function findDefaultData_cb(data, error) {
		if(error) {
			alert(error);
			return;
		}
		if(data) {
			defaultData = data;
			setDefaultData();
		}
	}

	function setDefaultData() {
		setElementValue("dtInclusao", setFormat("dtInclusao", defaultData.dtInclusao));
	}

	function onChangeDataInicio(){
		setElementValue("dtFimPromotor","");
	}

</script>