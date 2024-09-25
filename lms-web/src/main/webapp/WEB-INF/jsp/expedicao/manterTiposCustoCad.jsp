<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.tipoCustoService">
	<adsm:form action="/expedicao/manterTiposCusto" idProperty="idTipoCusto">
		<adsm:textbox 
			maxLength="60" 
			dataType="text" 
			property="dsTipoCusto" 
			label="tipoCusto" 
			size="40" 
			required="true"
		/>

		<adsm:combobox 
			property	="tpSituacao" 
			label		="situacao" 
			domain		="DM_STATUS" 
			required	="true"
		/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
