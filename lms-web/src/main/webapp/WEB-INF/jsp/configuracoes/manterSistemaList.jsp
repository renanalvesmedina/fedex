<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.manterSistemaAction">
	<adsm:form action="/seguranca/manterSistema">
		<adsm:textbox dataType="text" property="nmSistema" label="descricao" width="20%" size="20" maxLength="20"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="sistema"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idSistema" property="sistema" rows="13" defaultOrder="nmSistema" >
		<adsm:gridColumn title="descricao" property="nmSistema" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>