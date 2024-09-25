<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterTiposTributacaoICMSAction">
	<adsm:form action="/tributos/manterTiposTributacaoICMS" idProperty="idTipoTributacaoIcms">
		<adsm:textbox dataType="text" property="dsTipoTributacaoIcms" label="descricao" size="70" maxLength="60" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="observacao" action="/tributos/manterObservacoesICMS" cmd="main">
				<adsm:linkProperty src="idTipoTributacaoIcms" target="idTipoTributacaoIcms"/>
				<adsm:linkProperty src="idTipoTributacaoIcms" target="tipoTributacaoIcms.idTipoTributacaoIcms"/>				
			</adsm:button>
			<adsm:storeButton/> 
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>