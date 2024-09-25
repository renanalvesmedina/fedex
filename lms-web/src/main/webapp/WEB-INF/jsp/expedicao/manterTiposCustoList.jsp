<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.tipoCustoService" >
	<adsm:form action="/expedicao/manterTiposCusto" idProperty="idTipoCusto">
		<adsm:textbox 
			maxLength	="60" 
			dataType	="text" 
			property	="dsTipoCusto" 
			label		="tipoCusto" 
			size		="40"
		/>
		
		<adsm:combobox 
			property	="tpSituacao" 
			label		="situacao" 
			domain		="DM_STATUS" 
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoCusto"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="check" idProperty="idTipoCusto" property="tipoCusto" gridHeight="200" unique="true" defaultOrder="dsTipoCusto:asc" rows="14">
		<adsm:gridColumn title="tipoCusto" property="dsTipoCusto" width="80%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
