<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.parametroFilialService">
	<adsm:form action="/configuracoes/manterParametrosFiliais">
		<adsm:textbox dataType="text" property="nmParametroFilial" label="nome" maxLength="20" size="40"/>
		<adsm:textbox dataType="text" property="dsParametroFilial" label="descricao" maxLength="50" size="40"/>
		<adsm:combobox property="tpFormato" label="formato" domain="DM_FORMATO_PARAMETRO"/>
		<adsm:textbox dataType="integer" property="nrTamanho" label="tamanho" maxLength="4" size="5"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametroFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idParametroFilial" property="parametroFilial" defaultOrder="nmParametroFilial" rows="13">	
		<adsm:gridColumn width="50%" title="nome" property="nmParametroFilial"/>
		<adsm:gridColumn width="25%" title="formato" property="tpFormato" isDomain="true"/>
		<adsm:gridColumn width="25%" title="tamanho" property="nrTamanho" align="right"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>