<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.parametroFilialService">
	<adsm:form action="/configuracoes/manterParametrosFiliais" idProperty="idParametroFilial">
		<adsm:textbox dataType="text" property="nmParametroFilial" label="nome" maxLength="20" size="40" required="true"/>
		<adsm:textbox dataType="text" property="dsParametroFilial" label="descricao" maxLength="50" size="40" required="true"/>
		<adsm:combobox property="tpFormato" label="formato" domain="DM_FORMATO_PARAMETRO" required="true"/>
		<adsm:textbox dataType="integer" property="nrTamanho" minValue="1" label="tamanho" maxLength="4" size="5" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="conteudosFiliais" action="/configuracoes/manterConteudosFiliais" cmd="main">
				<adsm:linkProperty src="idParametroFilial" target="parametroFilial.idParametroFilial"/>
				<adsm:linkProperty src="nmParametroFilial" target="parametroFilial.nmParametroFilial"/>
				<adsm:linkProperty src="dsParametroFilial" target="parametroFilial.dsParametroFilial"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
