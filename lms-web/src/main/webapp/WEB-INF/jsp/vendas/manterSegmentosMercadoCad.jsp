<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.segmentoMercadoService">
	<adsm:form action="/vendas/manterSegmentosMercado" idProperty="idSegmentoMercado">
		<adsm:textbox maxLength="60" dataType="text" property="dsSegmentoMercado" label="segmentoMercado" size="60" width="80%" labelWidth="20%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="80%" labelWidth="20%" required="true"/>
		<adsm:textbox 
			dataType="decimal" 
			property="nrFatorCubagemReal" 
			label="fatorCubagemReal" 
			labelWidth="20%"
			size="10"
			maxValue="999999.99"
			width="28%"
			required="true"
			mask="###,##0.00"
		/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>