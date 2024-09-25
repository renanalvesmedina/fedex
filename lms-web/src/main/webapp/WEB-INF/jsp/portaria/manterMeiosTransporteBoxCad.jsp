<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterMeiosTransporteBoxAction">
	<adsm:form action="/portaria/manterMeiosTransporteBox" idProperty="idMeioTransporteRodoBox" onDataLoadCallBack="meioTransporteDataLoad" service="lms.portaria.manterMeiosTransporteBoxAction.findById">
	
		<adsm:textbox dataType="text" property="box.doca.terminal.filial.sgFilial" size="3" label="filial" labelWidth="20%" width="30%" disabled="true">
			<adsm:textbox dataType="text"property="box.doca.terminal.filial.pessoa.nmFantasia" size="27" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="box.doca.terminal.pessoa.nmPessoa" label="terminal" labelWidth="20%" width="30%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.doca.numeroDescricaoDoca" serializable="false" label="doca" labelWidth="20%" width="30%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.nrBox" label="box" labelWidth="20%" width="30%" disabled="true" />
		<adsm:hidden property="box.idBox"/>
		
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" maxLength="6"
				service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="20%" width="8%" size="7" serializable="false">
				
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />				
		
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" maxLength="25"
					service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					width="22%" size="20" required="true">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			</adsm:lookup>
			
	    </adsm:lookup>
		
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		
		<adsm:range label="vigencia" labelWidth="20%" width="68%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
	<adsm:buttonBar>
			<adsm:storeButton id="store" service="lms.portaria.manterMeiosTransporteBoxAction.storeMap" callbackProperty="beforeStore"/>
			<adsm:newButton id="novo"/>
			<adsm:removeButton id="remover"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	function initWindow(evt){
		if(evt.name != "gridRow_click" && evt.name != 'storeButton'){
			novo();
			setFocusOnFirstFocusableField();
		}
	}
	
	function novo(){
		setDisabled("meioTransporteRodoviario2.idMeioTransporte", false);
		setDisabled("meioTransporteRodoviario.idMeioTransporte", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);

	}
	
	function beforeStore_cb(data, error, key){
		store_cb(data, error, key);

		if (error == undefined && data != undefined){
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}

	function comportamentoDetalhe(data) {
		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");

		if (acaoVigencia == 0){
			novo();			
			setFocusOnFirstFocusableField();
		} else if (acaoVigencia == 1){
			setDisabled(document, true);
			setDisabled("dtVigenciaFinal",false);
			setDisabled("novo",false);
			setDisabled("store",false);
			setFocusOnFirstFocusableField();		
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("novo", false);		
			setFocusOnNewButton();
		}
	}

	function meioTransporteDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		
		if (data != undefined) {
			var idFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.idMeioTransporte");
			var nrFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.nrFrota");
						
			if (idFrota != undefined) {
			    setElementValue("meioTransporteRodoviario2.idMeioTransporte", idFrota);
				setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota", nrFrota);
			}			
		}
			
		comportamentoDetalhe(data);		
	}

</script>