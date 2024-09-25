<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.RHFuncaoService">
	<adsm:form action="/configuracoes/consultarCargos" idProperty="codigo">
	
		<adsm:textbox dataType="text" property="idCodigo" label="codigo" maxLength="60" size="60" width="85%"/>
		<adsm:textbox dataType="text" property="nome" label="nome" maxLength="60" size="60" width="85%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rhFuncao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="codigo" property="rhFuncao" gridHeight="200" unique="true" defaultOrder="nome">
		<adsm:gridColumn title="codigo" property="idCodigo" width="20%" />
		<adsm:gridColumn title="nome" property="nome" width="80%"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>