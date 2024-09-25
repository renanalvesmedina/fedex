<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.tpCombustTpMeioTranspService">
	<adsm:form action="/contratacaoVeiculos/manterCombustiveisTiposMeiosTransporte" idProperty="idTpCombustTpMeioTransp">
		
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte"/> 
		
		<adsm:textbox dataType="text" property="dsMeioTransporte" label="modalidade" maxLength="50" labelWidth="20%" size="25" width="30%" disabled="true" serializable="false" />
		
		<adsm:textbox dataType="text" property="tipoMeioTransporte.dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="50" labelWidth="20%" size="25" width="30%" disabled="true" />
		
		<adsm:combobox property="tipoCombustivel.idTipoCombustivel" required="true" label="tipoCombustivel" service="lms.fretecarreteiroviagem.tipoCombustivelService.find" optionLabelProperty="dsTipoCombustivel" optionProperty="idTipoCombustivel" labelWidth="20%" width="30%" onlyActiveValues="true" boxWidth="200"/>
		
		<adsm:textbox dataType="decimal" mask="#,##0.00" property="qtConsumo" label="consumo" maxLength="6" labelWidth="20%" size="25" width="30%" required="true"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao" labelWidth="20%" width="30%"/>
		<adsm:buttonBar>
		    <adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   