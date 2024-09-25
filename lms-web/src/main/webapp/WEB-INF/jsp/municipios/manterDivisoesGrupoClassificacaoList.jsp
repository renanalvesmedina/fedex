<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.divisaoGrupoClassificacaoService">
	<adsm:form action="/municipios/manterDivisoesGrupoClassificacao" idProperty="idDivisaoGrupoClassificacao">

		<adsm:combobox property="grupoClassificacao.idGrupoClassificacao" label="grupoClassificacao" service="lms.municipios.grupoClassificacaoService.find" optionLabelProperty="dsGrupoClassificacao" optionProperty="idGrupoClassificacao" labelWidth="18%" width="32%" boxWidth="220">
		  <adsm:propertyMapping relatedProperty="dsTpSituacaoGrupo" modelProperty="tpSituacao.description"/>
		</adsm:combobox>
		<adsm:textbox label="situacao" dataType="text" property="dsTpSituacaoGrupo" serializable="false" labelWidth="18%" width="32%" disabled="true"/>
 
		<adsm:textbox dataType="text" label="descricao" property="dsDivisaoGrupoClassificacao" size="40" maxLength="60" disabled="false" width="32%" labelWidth="18%"/>		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="82%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="divisaoGrupoClassificacao"/>
			<adsm:resetButton/>    	</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDivisaoGrupoClassificacao" property="divisaoGrupoClassificacao" selectionMode="check" unique="true" defaultOrder="grupoClassificacao_.dsGrupoClassificacao, dsDivisaoGrupoClassificacao">
		<adsm:gridColumn width="40%" title="grupoClassificacao" property="grupoClassificacao.dsGrupoClassificacao" />
		<adsm:gridColumn width="10%" title="situacao" property="grupoClassificacao.tpSituacao" isDomain="true"  />	
		<adsm:gridColumn width="40%" title="descricao" property="dsDivisaoGrupoClassificacao" />
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true" />		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>	
</adsm:window>