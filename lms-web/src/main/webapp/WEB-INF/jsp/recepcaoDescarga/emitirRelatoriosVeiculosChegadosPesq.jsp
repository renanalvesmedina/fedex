<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.recepcaodescarga.emitirRelatoriosVeiculosChegadosAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/recepcaoDescarga/emitirRelatoriosVeiculosChegados" idProperty="id" >

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>

		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" width="80%" labelWidth="20%" required="true"
					 property="filial" exactMatch="true" 
					 service="lms.recepcaodescarga.emitirRelatoriosVeiculosChegadosAction.findLookupFilial" 
					 onPopupSetValue="validaAcessoFilial"
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial"
                     serializable="true"
                     >
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		
		<adsm:range label="data" required="true" labelWidth="20%" width="80%">
		<adsm:textbox property="dtConsultaInicial" dataType="JTDateTimeZone" labelWidth="20%" width="80%" required="true"/>
		<adsm:textbox property="dtConsultaFinal"   dataType="JTDateTimeZone" labelWidth="20%" width="80%" required="true"/>
		</adsm:range>
		
		<%--adsm:textbox label="data" property="dtConsulta" dataType="JTDate" labelWidth="20%" width="80%" required="true"/--%>
		

		<adsm:section caption="situacao" /> 

		<%--adsm:checkbox property="aguardandoDescarga" label="aguardandoDescarga" labelWidth="20%" width="5%" onclick="onAguardandoDescargaClick(this);"/>
		<adsm:range label="horarioChegada" labelWidth="20%" width="55%">
			<adsm:textbox dataType="JTTime" property="horarioChegadaInicio" disabled="true"/>
			<adsm:textbox dataType="JTTime" property="horarioChegadaFinal"  disabled="true"/>
		</adsm:range>
		<adsm:checkbox property="emDescarga" label="emDescarga" labelWidth="20%" width="5%" onclick="onEmDescargaClick(this);"/>
		<adsm:range label="horarioInicioDescarga" labelWidth="20%" width="55%">
			<adsm:textbox dataType="JTTime" property="horarioInicioDescargaInicial" disabled="true"/>
			<adsm:textbox dataType="JTTime" property="horarioInicioDescargaFinal"   disabled="true"/>
		</adsm:range>
		<adsm:checkbox property="descarregados" label="descarregados" labelWidth="20%" width="5%" onclick="onDescarregadosClick(this);"/>
		<adsm:range label="horarioFimDescarga" labelWidth="20%" width="55%">
			<adsm:textbox dataType="JTTime" property="horarioFimDescargaInicial"  disabled="true"/>
			<adsm:textbox dataType="JTTime" property="horarioFimDescargaFinal"    disabled="true"/>
		</adsm:range--%>
		
		<adsm:checkbox property="aguardandoDescarga" label="aguardandoDescarga" labelWidth="20%" width="80%" />
		<adsm:checkbox property="emDescarga"         label="emDescarga"         labelWidth="20%" width="80%" />
		<adsm:checkbox property="descarregados"      label="descarregados"      labelWidth="20%" width="80%" />

		<adsm:combobox label="formatoRelatorio" labelWidth="20%" width="80%"
		               domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true" />

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.recepcaodescarga.emitirRelatoriosVeiculosChegadosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function pageLoad_cb() {
		getDataOnLoadPage();
	}

	function getDataOnLoadPage() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.recepcaodescarga.emitirRelatoriosVeiculosChegadosAction.getOnLoadData", "getOnLoadData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function getOnLoadData_cb(data, error) {
		setElementValue("dtConsultaInicial", setFormat("dtConsultaInicial", getNestedBeanPropertyValue(data, "hojeHoraZero")));
		setElementValue("dtConsultaFinal", setFormat("dtConsultaFinal", getNestedBeanPropertyValue(data, "hoje")));
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFilial"));
	}

	function initWindow(event) {
		if (event.name == "cleanButton_click") {
			getDataOnLoadPage();
		}
	}
	
	/**
	 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
	 * Função necessária pois quando é selecionado um item na popup não é chamado
	 * o serviço definido na lookup.
	 */
	 function validaAcessoFilial(data) {
		var criteria = new Array();
	    // setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
	    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
	    var sdo = createServiceDataObject("lms.recepcaodescarga.emitirRelatoriosVeiculosChegadosAction.findLookupFilial", "resultadoLookup", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função que trata o retorno da função validaAcessoFilial.
	 */
	 function resultadoLookup_cb(data, error) {
		if (error != undefined) {
			alert(error);
		    setFocus(document.getElementById("filial.sgFilial"));
		    resetValue("filial.idFilial");
			return false;
		} else {
			filial_sgFilial_exactMatch_cb(data, error);
		}
	}
	

</script>