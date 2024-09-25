<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction">
	<adsm:form action="/contratacaoVeiculos/consultarEventosMeiosTransporte" idProperty="idEventoMeioTransporte">

		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="filial"/>
			<adsm:include key="meioTransporte"/>
			<adsm:include key="evento"/>
		</adsm:i18nLabels>

		<adsm:hidden property="tpEmpresaMercurioValue" serializable="false" value="M" />
		<adsm:lookup service="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction.findLookupFilial"
				dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				action="/municipios/manterFiliais" labelWidth="18%" width="82%" exactMatch="true" >
         	<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa"/>
         	
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"
         			size="25" maxLength="50" disabled="true" serializable="false" />
	    </adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
                  service="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction.findLookupMeioTransporte"
                  action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrFrota"
                  label="meioTransporte" labelWidth="18%" width="32%" size="8" picker="false" >
            <adsm:propertyMapping criteriaProperty="meioTransporte2.nrIdentificador"
                       modelProperty="nrIdentificador" /> 
            <adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
                       modelProperty="idMeioTransporte" /> 
            <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador"
                       modelProperty="nrIdentificador" />
                             
            <adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
                       service="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction.findLookupMeioTransporte" picker="true"
                       action="/contratacaoVeiculos/manterMeiosTransporte" criteriaProperty="nrIdentificador"
                       size="20" serializable="false" >
                 <adsm:propertyMapping criteriaProperty="meioTransporte.nrFrota"
                            modelProperty="nrFrota" />
                 <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
                            modelProperty="idMeioTransporte" />      
                 <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota"
                            modelProperty="nrFrota" />
	         </adsm:lookup>
	         
         </adsm:lookup>         

		<adsm:combobox property="tpSituacaoMeioTransporte" label="evento" domain="DM_EVENTO_MEIO_TRANSPORTE" labelWidth="12%" width="32%"/>

		<adsm:range label="periodoEvento" width="70%" labelWidth="18%" maxInterval="30" required="true" >
			<adsm:textbox dataType="JTDate" property="dtInicioEvento" />
			<adsm:textbox dataType="JTDate" property="dtFimEvento"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="eventoMeioTransporte" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="eventoMeioTransporte" idProperty="idEventoMeioTransporte" selectionMode="none" onRowClick="rowClick"
			service="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction.findEventos"
			rowCountService="lms.contratacaoveiculos.consultarEventosMeiosTransporteAction.getRowCountCustom"
			gridHeight="220" unique="true" rows="12" >
		<adsm:gridColumn title="meioTransporte"  property="meioTransporte_nrFrota"  width="60" />
		<adsm:gridColumn title="" property="meioTransporte_nrIdentificador" width="80" align="left" />
		<adsm:gridColumn title="evento" property="descEvento" />
		<adsm:gridColumn title="dataInicio" property="dhInicioEvento" width="120" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="dataFim" property="dhFimEvento" width="120" dataType="JTDateTimeZone" align="center" />
		
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window> 
<script type="text/javascript">
	document.getElementById("tpEmpresaMercurioValue").masterLink = true;

	function rowClick(){
		return false;
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("filial.idFilial") != "" ||
					getElementValue("meioTransporte.idMeioTransporte") != "" ||
					getElementValue("tpSituacaoMeioTransporte") != "") {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013") + i18NLabel.getLabel("filial") + ', ' 
						+ i18NLabel.getLabel("meioTransporte") + ', ' 
						+ i18NLabel.getLabel("evento")+ ".");
				setFocusOnFirstFocusableField(document);		
			}
		}
		return false;
	}
	
</script>
