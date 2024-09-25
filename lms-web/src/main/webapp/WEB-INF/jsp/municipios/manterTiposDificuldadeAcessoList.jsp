<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoDificuldadeAcessoService">
	<adsm:form action="/municipios/manterTiposDificuldadeAcesso" idProperty="idTipoDificuldadeAcesso">
	<adsm:textbox dataType="text" size="50"  property="dsTipoDificuldadeAcesso" label="descricao" maxLength="60" width="85%"/>
	<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoDificuldadeAcesso"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoDificuldadeAcesso" property="tipoDificuldadeAcesso" selectionMode="check" gridHeight="200" unique="true" defaultOrder="dsTipoDificuldadeAcesso" rows="12">
	<adsm:gridColumn title="descricao" property="dsTipoDificuldadeAcesso"/>
	<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
