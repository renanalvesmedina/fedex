<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterObservacoesICMSAction">
	<adsm:form action="/tributos/manterObservacoesICMS">
		<adsm:combobox 
			service="lms.tributos.manterObservacoesICMSAction.findUf" 
			property="unidadeFederativa.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" style="width:222px"
			label="ufOrigem">
		</adsm:combobox>
        <adsm:combobox property="tipoTributacaoIcms.idTipoTributacaoIcms" 
        optionLabelProperty="dsTipoTributacaoIcms" optionProperty="idTipoTributacaoIcms" 
        service="lms.tributos.manterObservacoesICMSAction.findTipoTributacao" label="tipoTributacao" boxWidth="220"/>  
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="descricaoTributacaoIcms"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDescricaoTributacaoIcms" property="descricaoTributacaoIcms" 
	defaultOrder="unidadeFederativa_.sgUnidadeFederativa, tipoTributacaoIcms_.dsTipoTributacaoIcms" unique="true" rows="14">
        <adsm:gridColumn title="ufOrigem" property="unidadeFederativa.siglaDescricao" />
		<adsm:gridColumn title="tipoTributacao" property="tipoTributacaoIcms.dsTipoTributacaoIcms" width="500" />
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>