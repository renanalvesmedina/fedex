<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tipoDificuldadeAcessoService">
	<adsm:form action="/municipios/manterTiposDificuldadeAcesso" idProperty="idTipoDificuldadeAcesso">
		<adsm:textbox dataType="text" size="50"  property="dsTipoDificuldadeAcesso" label="descricao" required="true" maxLength="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true"/>
		<adsm:buttonBar>	
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   