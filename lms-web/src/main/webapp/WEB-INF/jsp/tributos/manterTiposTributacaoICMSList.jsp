<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterTiposTributacaoICMSAction">
	<adsm:form action="/tributos/manterTiposTributacaoICMS">
		<adsm:textbox dataType="text" property="dsTipoTributacaoIcms" label="descricao" size="70" maxLength="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoTributacaoIcms"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoTributacaoIcms" property="tipoTributacaoIcms" defaultOrder="dsTipoTributacaoIcms" rows="13" unique="true">
		<adsm:gridColumn width="70%" title="descricao" property="dsTipoTributacaoIcms" dataType="text" />
		<adsm:gridColumn width="30%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
