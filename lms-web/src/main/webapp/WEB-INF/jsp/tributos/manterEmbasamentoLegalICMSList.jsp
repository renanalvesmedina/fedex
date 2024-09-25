<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tributos.manterEmbasamentoLegalICMSAction">

	<adsm:form
		action="/tributos/manterEmbasamentoLegalICMS"
		idProperty="idEmbasamento">

        <adsm:combobox
	        service="lms.tributos.manterEmbasamentoLegalICMSAction.findUnidadeFederativa"
	        property="unidadeFederativaOrigem.sgUnidadeFederativa"
	        optionProperty="sgUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
	        labelWidth="26%" 
	        width="27%" 
	        boxWidth="170"
	        label="ufOrigem"/>
	       		
        <adsm:combobox 
	        service="lms.tributos.manterAliquotasICMSAction.findTipoTributacaoIcms" 
	        property="tipoTributacaoIcms.idTipoTributacaoIcms" 
	        optionLabelProperty="dsTipoTributacaoIcms" 
	        optionProperty="idTipoTributacaoIcms" 
	        label="tipoTributacao"
	        labelWidth="27%" 
	        width="20%"
	        boxWidth="130" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="embasamentoLegaIcms"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idEmbasamento" defaultOrder="" selectionMode="check" property="embasamentoLegaIcms" gridHeight="300" unique="true" rows="13">

        <adsm:gridColumn title="ufOrigem" property="unidadeFederativaOrigem.sgUnidadeFederativa" width="100" />
		<adsm:gridColumn title="tpTributacao" property="tipoTributacaoIcms.dsTipoTributacaoIcms" width="100" />
        <adsm:gridColumn title="embLegalCompleto" property="dsEmbLegalCompleto" width="100" />
		<adsm:gridColumn title="embLegalResumido" property="dsEmbLegalResumido" width="150" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>	
	</adsm:grid>
</adsm:window>
