<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterPrealertasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/manterPrealertas">
		<adsm:textbox labelWidth="22%" width="28%" maxLength="8"
			dataType="integer" 
			property="nrPreAlerta" 
			label="numeroPrealerta"/>
        
        <adsm:combobox label="numeroAWB" property="tpStatusAwb"
					   domain="DM_LOOKUP_AWB" 
					   labelWidth="18%" width="32%"
					   defaultValue="E" renderOptions="true" disabled="true">
        
			<adsm:lookup property="ciaFilialMercurio.empresa" 
						 idProperty="idEmpresa"
						 dataType="text"
						 criteriaProperty="sgEmpresa"
				 		 criteriaSerializable="true"
						 service="lms.expedicao.manterPrealertasAction.findLookupSgCiaAerea"
						 action="" 	
						 size="3" maxLength="3"						 
					 	picker="false">
			</adsm:lookup>

	        <adsm:lookup dataType="integer" size="13" maxLength="11" 
	        	property="awb"
	        	idProperty="idAwb"
	        	criteriaProperty="nrAwb"
	        	criteriaSerializable="true"
	 			service="lms.expedicao.manterPrealertasAction.findLookupAwb"
				action="expedicao/consultarAWBs"
				onDataLoadCallBack="awbOnDataLoadCallBack"
				onPopupSetValue="findAwb_cb">
				
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
				
	        </adsm:lookup>
	    </adsm:combobox>        

		<adsm:textbox dataType="text" labelWidth="22%" width="28%" maxLength="20"
			property="dsVoo" 
			label="numeroVoo" />
		<adsm:combobox labelWidth="18%" width="32%"
			property="blVooConfirmado" 
			label="embarqueConfirmado" 
			domain="DM_SIM_NAO" />

		<adsm:range label="dataDeEmbarque" labelWidth="22%" width="78%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhSaidaInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhSaidaFinal"/>
		</adsm:range>
		<adsm:range label="dataPrevistaChegada" labelWidth="22%" width="78%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhChegadaInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhChegadaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				id="consultar"
				caption="consultar" 
				onclick="validateFields();"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		idProperty="idPreAlerta" 
		property="preAlerta" 
		service="lms.expedicao.manterPrealertasAction.findPaginatedPreAlerta"
		rowCountService="lms.expedicao.manterPrealertasAction.getRowCountPreAlerta"
		selectionMode="none"
		onRowClick="rowClick"
		gridHeight="200" 
		unique="true">
		<adsm:gridColumn title="numeroPrealerta" property="nrPreAlerta" width="140" align="right"/>
		<adsm:gridColumn title="awb" property="awbFormatado" width="140" align="left" />
		<adsm:gridColumn title="numeroVoo" property="dsVoo" width="70"/>
		<adsm:gridColumn title="confirmado" property="blVooConfirmado" renderMode="image-check" width="80"/>
		<adsm:gridColumn title="saida" property="dhSaida" dataType="JTDateTimeZone" width="130" align="center"/>
		<adsm:gridColumn title="chegada" property="dhChegada" dataType="JTDateTimeZone" width="130" align="center"/>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	function initWindow(event) {
		changeTabStatus(true);
		setDisabled("consultar",false);
	}

	function rowClick() {
		//*** habilita as abas
		changeTabStatus(false);
	}

	function changeTabStatus(status) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", status);
	}

	/**
	* Valida se algum campo foi informado.
	*/
	function validateFields() {
		/* Caso nenhum campo tenha sido informado, mostra mensagem */
		if (getElementValue("nrPreAlerta") == ''
			&& getElementValue("awb.idAwb") == ''
			&& getElementValue("dsVoo") == ''
			&& getElementValue("blVooConfirmado") == ''
			&& getElementValue("dhSaidaInicial") == ''
			&& getElementValue("dhSaidaFinal") == ''
			&& getElementValue("dhChegadaInicial") == ''
			&& getElementValue("dhChegadaFinal") == '') {
			alert(i18NLabel.getLabel("LMS-00055"));
			return false;
		}
		findButtonScript("preAlerta", this.document.forms[0]);
	}
	
	
	function findAwb_cb(data, error){
		if(data != null){
			data = data[0] != null ? data[0] : data;
			setElementValue("awb.idAwb", data.idAwb);
			setElementValue("ciaFilialMercurio.empresa.idEmpresa", data.ciaFilialMercurio.empresa.idEmpresa);
			setElementValue("ciaFilialMercurio.empresa.sgEmpresa", data.ciaFilialMercurio.empresa.sgEmpresa);
			
		}
	}
	
	function awbOnDataLoadCallBack_cb(data) {
		awb_nrAwb_exactMatch_cb(data);
		if(data != null && data.length > 0){
			data = data[0] != null ? data[0] : data;			
		}
	}
	
</script>