<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterCarteiraVendasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/manterCarteiraVendas">
		<adsm:textbox property="idCarteiraVendas" label="numeroLote"  dataType="integer" 
				size="16" maxLength="10" labelWidth="17%" width="33%" disabled="false"/>
				
		<adsm:lookup
			label="vendedor"
			property="usuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor"
			action="/configuracoes/consultarFuncionarios"
			cmd="promotor"
			dataType="text"
			size="16"
			maxLength="16"
			labelWidth="17%"
			width="80%">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" inlineQuery="true"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:range label="dataInicio" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtInicioLoteInicial"/>
			<adsm:textbox dataType="JTDate" property="dtInicioLoteFinal"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="btnConsultar" onclick="onClickConsultar()" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="carteiraVendas" idProperty="idCarteiraVendas"
		service="lms.vendas.manterCarteiraVendasAction.findPaginated"
		rowCountService="lms.vendas.manterCarteiraVendasAction.getRowCount"
		rows="11">
		<adsm:gridColumn title="vendedor" property="nmUsuario" width="35%" dataType="text"/>
		<adsm:gridColumn title="numeroLote" property="numeroLote" dataType="text"/>
		<adsm:gridColumn title="dataInicio" property="dtInicioLote" dataType="JTDate" align="center" />
		<adsm:gridColumn title="situacao" property="tpSituacaoAprovacao" isDomain="true" />
		<adsm:gridColumn title="efetivadoNivel1" property="blEfetivadoNivel1" renderMode="image-check" />
		<adsm:gridColumn title="efetivadoNivel2" property="blEfetivadoNivel2" renderMode="image-check"/>
		
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
			
</adsm:window>
<script>
function initWindow(event){
	setDisabled("btnConsultar", false);	
}
function onClickConsultar(){
	var nrLoteOk = getElementValue('idCarteiraVendas').length >0;
	var vendedorOk = getElementValue('usuario.idUsuario').length >0;
	var dtInicioLoteInicialOk = getElementValue('dtInicioLoteInicial').length >0;
	var dtInicioLoteFinalOk = getElementValue('dtInicioLoteFinal').length >0;

	if (nrLoteOk || vendedorOk || dtInicioLoteInicialOk || dtInicioLoteFinalOk){
		findButtonScript('carteiraVendas', document.forms[0]);
	}else{
		alertI18nMessage("LMS-00055");
	}	
}

</script>