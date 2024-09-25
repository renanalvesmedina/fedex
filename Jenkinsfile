timestamps {
    node ("${NODE_NAME}") {
        /* Parâmetros */
        /*parameters {
            booleanParam defaultValue: false, description: 'Select Artifact', name: 'AdsmFramework'
            booleanParam defaultValue: false, description: 'Select Artifact', name: 'IntegrationDomain'
            booleanParam defaultValue: true, description: 'Select Artifact', name: 'DeployLMSapp'
            booleanParam defaultValue: true, description: 'Select Artifact', name: 'DeployLMSswt'
            //text defaultValue: 'hf', description: '', name: 'LMS_ENVIRONMENT'
        }*/

        def jdk8 = 'jdk1.8.0_60'
        def maven3 = 'apache-maven-3.X.X'
        def ant = 'apache-ant-1.8.2'
        
        if (LMS_ENVIRONMENT != 'pd') {
            if (params.AdsmFramework == true) {
                stage ('Build ADSM Framework') {
                    if (LMS_ENVIRONMENT == 'rg'){
                        vbranch = BranchADSMFramework
                        //println (BranchADSMFramework)
                    } else {
                        vbranch = BRANCH
                        //println (BRANCH)
                    }   
                    checkout([$class: 'GitSCM', 
                        branches: [[name: "${vbranch}"]], 
                        doGenerateSubmoduleConfigurations: false, 
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'adsm-framework/']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:adsm/adsm-framework.git']]
                    ])  
                    withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                        bat '''
                            mvn -f adsm-framework/pom.xml clean deploy source:jar -U && exit %%ERRORLEVEL%%
                        '''
                    }
                }
            }
            if (params.IntegrationDomain == true) {
                stage ('Build Integration Domain') {
                    if (LMS_ENVIRONMENT == 'rg'){
                        vbranch = BranchIntegrationDomain
                        //println (BranchIntegrationDomain)
                    } else {
                        vbranch = BRANCH
                        //println (BRANCH)
                    }   
                    checkout([$class: 'GitSCM', 
                        branches: [[name: "${vbranch}"]], 
                        doGenerateSubmoduleConfigurations: false, 
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'integration-domain/']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:integration/domains/integration-domain.git']]
                    ])  
                    withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                        bat '''
                            mvn -f integration-domain/pom.xml clean deploy source:jar -U && exit %%ERRORLEVEL%%
                        '''
                    }
                }
            }
        }

        stage ("Checkout LMS-APP") {
                //Checkout LMS-APP
                checkout([$class: 'GitSCM', 
                    branches: [[name: '$BRANCH']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'lms-app/']], 
                    submoduleCfg: [], 
                    userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:systems/lms/lms-app.git']]
                ])
        }
        if (LMS_ENVIRONMENT != 'pd') {
            lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
            
            if (params.BuildLMSapp == true) {
                wrap([$class: 'BuildUser']) {
                    stage ("Build LMS-APP - ${lmsEnvironment}") {
                        //Build LMS-APP
                        if (LMS_ENVIRONMENT != "rg" && BUILD_USER_ID != "diogost" && BUILD_USER_ID != "valdemirms"){
                            //echo "entrou na condição 1"
                            withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                                if (params.skipCompileReports == true) {
                                    bat '''
                                        mvn -f lms-app/pom.xml clean install source:jar -U -T 2 -DskipCompileReports=true && exit %%ERRORLEVEL%%
                                    '''
                                } else if (params.skipCompileReports == false) {
                                    bat '''
                                        mvn -f lms-app/pom.xml clean install source:jar -U -T 2 && exit %%ERRORLEVEL%%
                                    '''
                                }
                            }
                        }else if (LMS_ENVIRONMENT == 'rg'){
                            withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                                bat '''
                                    mvn -f lms-app/pom.xml clean deploy source:jar -U -T 2 && exit %%ERRORLEVEL%%
                                '''
                            }
                        }else if ((params.BRANCH.contains("release") && BUILD_USER_ID.equals("valdemirms")) || (params.BRANCH.contains("release") && BUILD_USER_ID.equals("diogost"))) {
                            withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                                bat '''
                                    mvn -f lms-app/pom.xml clean deploy source:jar -U -T 2 && exit %%ERRORLEVEL%%
                                '''
                            }
                        }else if ((params.BRANCH.contains("feature") && BUILD_USER_ID.equals("valdemirms")) || (params.BRANCH.contains("feature") && BUILD_USER_ID.equals("diogost"))) {
                            withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                                if (params.skipCompileReports == true) {
                                    bat '''
                                        mvn -f lms-app/pom.xml clean install source:jar -U -T 2 -DskipCompileReports=true && exit %%ERRORLEVEL%%
                                    '''
                                } else if (params.skipCompileReports == false) {
                                    bat '''
                                        mvn -f lms-app/pom.xml clean install source:jar -U -T 2 && exit %%ERRORLEVEL%%
                                    '''
                                }
                            }
                        }
                    }
                }
            }

            if (params.DeployLMSapp == true || params.DeployLMSswt == true){
                stage ("Restart Weblogic - Initial - ${lmsEnvironment}") {
                    withCredentials([usernamePassword(credentialsId: '401b9a88-b87d-4862-b449-39c1acdd5690', passwordVariable: 'password', usernameVariable: 'userName')]) {
                        def remote = [:]
                        remote.name = "lx-wldev02"
                        remote.host = "172.16.3.122"
                        remote.allowAnyHosts = true
                        remote.user = "$userName"
                        remote.password = "$password"
                        sshCommand remote: remote, command: "bash -x /home/adm.gc/ManageServers.sh lms-${LMS_ENVIRONMENT} restart"
                    }
                }
                if (params.DeployLMSapp == true) {
                    stage("Deploy LMS-APP' - ${lmsEnvironment}"){
                        wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                            //echo "Password: ${Weblogic_Password}"
                            withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                                bat '''
                                    mvn -f lms-app/lms-ear/pom.xml clean integration-test -Pweblogic-%LMS_ENVIRONMENT% -Ddeploy.admin.user=%WEBLOGIC_USER% -Ddeploy.admin.password=%WEBLOGIC_PASSWORD% -Ddeploy=true
                                '''                        
                            }
                        }
                    }
                }
                if (params.DeployLMSswt == true) {
                    stage ("Deploy LMS-SWT-CLIENT - ${lmsEnvironment}") {
                        //Checkout LMS-SWT-CLIENT
                        checkout([$class: 'GitSCM', 
                            branches: [[name: '$BRANCH']], 
                            doGenerateSubmoduleConfigurations: false, 
                            extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'lms-swt-client/']], 
                            submoduleCfg: [], 
                            userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:systems/lms/lms-swt-client.git']]
                        ])
                        //Deploy LMS-SWT-CLIENT
                        wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                            withAnt(installation: "${ant}", jdk: "${jdk8}") {
                                bat '''
                                    ant -f lms-swt-client/build.xml -Dwl.deploy.admin.password=%WEBLOGIC_PASSWORD% -Djks.alias=lms.tntbrasil.com.br -Dbuild-file=%LMS_ENVIRONMENT% -Djks.storepass=E7n@v5L$  build-war wl-server-deploy
                                '''
                            }
                        }
                    }
                    stage ("Deploy LMS-SWT-CLIENT-FDX - ${lmsEnvironment}") {
                        //Checkout LMS-SWT-CLIENT
                        checkout([$class: 'GitSCM', 
                            branches: [[name: '$BRANCH']], 
                            doGenerateSubmoduleConfigurations: false, 
                            extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'lms-swt-client/']], 
                            submoduleCfg: [], 
                            userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:systems/lms/lms-swt-client.git']]
                        ])
                        //Deploy LMS-SWT-CLIENT
                        wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                            withAnt(installation: "${ant}", jdk: "${jdk8}") {
                                bat '''
                                    ant -f lms-swt-client/build.xml -Dwl.deploy.admin.password=%WEBLOGIC_PASSWORD% -Djks.alias=lms.tntbrasil.com.br -Dbuild-file=%LMS_ENVIRONMENT%fdx -Djks.storepass=E7n@v5L$  build-war wl-server-deploy
                                '''
                            }
                        }
                    }
                }
                stage ("Restart Weblogic - Last - ${lmsEnvironment}") {
                    withCredentials([usernamePassword(credentialsId: '401b9a88-b87d-4862-b449-39c1acdd5690', passwordVariable: 'password', usernameVariable: 'userName')]) {
                        def remote = [:]
                        remote.name = "lx-wldev02"
                        remote.host = "172.16.3.122"
                        remote.allowAnyHosts = true
                        remote.user = "$userName"
                        remote.password = "$password"
                        sshCommand remote: remote, command: "bash -x /home/adm.gc/ManageServers.sh lms-${LMS_ENVIRONMENT} restart"
                    }
                }
            }
        }

                                ////////////////////////////////////////////////////////////////////////////////////
                                ///////////////////////           stages de Produção        ///////////////////////
                                ///////////////////////////////////////////////////////////////////////////////////

        else {
            if (params.DeployLMSapp == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                if (cluster == 'Odd') {
                    profile = 'weblogic-pd-01'
                    vcluster = 'Cluster Impar'
                }
                if (cluster == 'Even') {
                    profile = 'weblogic-pd-02'
                    vcluster = 'Cluster Par'
                }
                stage("Deploy LMS-APP - ${lmsEnvironment} - ${vcluster}"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat """
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -P${profile} -Ddeploy.admin.user=${WEBLOGIC_USER} -Ddeploy.admin.password=${WEBLOGIC_PASSWORD} -Ddeploy=true
                            """
                        }
                    }
                }
            }
            if  (params.DeployLMSswt == true) {   
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                stage ("Build LMS-SWT-CLIENT TNT - ${lmsEnvironment}") {
                    //Checkout LMS-SWT-CLIENT
                    checkout([$class: 'GitSCM', 
                        branches: [[name: '$BRANCH']], 
                        doGenerateSubmoduleConfigurations: false, 
                         extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'lms-swt-client/']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:systems/lms/lms-swt-client.git']]
                    ])
                    //Build LMS-SWT-CLIENT
                    withCredentials([usernamePassword(credentialsId: "${DB_PASSWORD}", passwordVariable: 'password', usernameVariable: 'userName')]) {
                        withAnt(installation: "${ant}", jdk: "${jdk8}") {
                            bat '''
                                @echo off
                                if exist "target" (
                                    RMDIR /S /Q target
                                ) else (
                                    echo não exite o diretório target
                                )                            
                            '''
                            bat '''
                                ant -f lms-swt-client/build.xml -Djks.alias=lms.tntbrasil.com.br -Dbuild-file=externo -Djks.storepass=E7n@v5L$ -Ddb.password=%password% build-war
                            '''
                        }
                    }

                }
                stage ('Deploy LMS-SWT-CLIENT TNT - lx-swlb07') {
                    sshPublisher(publishers: 
                        [sshPublisherDesc
                            (configName: 'lx-swlb07.mercurio.local', 
                                transfers: 
                                [sshTransfer
                                    (cleanRemote: false, 
                                        excludes: '', 
                                        execCommand: '', 
                                        execTimeout: 120000, 
                                        flatten: false, 
                                        makeEmptyDirs: false, 
                                        noDefaultExcludes: false, 
                                        patternSeparator: '[, ]+', 
                                        remoteDirectory: 'lms-swt/', 
                                        remoteDirectorySDF: false, 
                                        removePrefix: 'lms-swt-client\\target\\war', 
                                        sourceFiles: 'lms-swt-client\\target\\war\\**'
                                    )
                                ],
                                usePromotionTimestamp: false, 
                                useWorkspaceInPromotion: false,
                                verbose: false
                            )
                        ]
                    )
                }
                stage ('Deploy LMS-SWT-CLIENT TNT - lx-swlb08') {
                    sshPublisher(publishers: 
                        [sshPublisherDesc
                            (configName: 'lx-swlb08.mercurio.local', 
                                transfers: 
                                [sshTransfer
                                    (cleanRemote: false, 
                                        excludes: '', 
                                        execCommand: '', 
                                        execTimeout: 120000, 
                                        flatten: false, 
                                        makeEmptyDirs: false, 
                                        noDefaultExcludes: false, 
                                        patternSeparator: '[, ]+', 
                                        remoteDirectory: 'lms-swt/', 
                                        remoteDirectorySDF: false, 
                                        removePrefix: 'lms-swt-client\\target\\war', 
                                        sourceFiles: 'lms-swt-client\\target\\war\\**'
                                    )
                                ],
                                usePromotionTimestamp: false, 
                                useWorkspaceInPromotion: false,
                                verbose: false
                            )
                        ]
                    )
                }
                stage ("Build LMS-SWT-CLIENT FDX - ${lmsEnvironment}") {
                    //Checkout LMS-SWT-CLIENT
                    checkout([$class: 'GitSCM', 
                        branches: [[name: '$BRANCH']], 
                        doGenerateSubmoduleConfigurations: false, 
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'lms-swt-client/']], 
                        submoduleCfg: [], 
                        userRemoteConfigs: [[credentialsId: "${GIT_CREDENTIAL}", url: 'git@gitlab.tntbrasil.com.br:systems/lms/lms-swt-client.git']]
                    ])
                    //Build LMS-SWT-CLIENT
                    withCredentials([usernamePassword(credentialsId: "${DB_PASSWORD}", passwordVariable: 'password', usernameVariable: 'userName')]) {
                        withAnt(installation: "${ant}", jdk: "${jdk8}") {
                            bat '''
                                @echo off
                                if exist "target" (
                                    RMDIR /S /Q target
                                ) else (
                                    echo não exite o diretório target
                                )                            
                            '''
                            bat '''
                                ant -f lms-swt-client/build.xml -Djks.alias=lms.tntbrasil.com.br -Dbuild-file=fdx -Djks.storepass=E7n@v5L$ -Ddb.password=%password% build-war
                            '''
                        }
                    }
                }
                stage ('Deploy LMS-SWT-CLIENT FDX - lx-swlb07') {
                    sshPublisher(publishers: 
                        [sshPublisherDesc
                            (configName: 'lx-swlb07.mercurio.local', 
                                transfers: 
                                [sshTransfer
                                    (cleanRemote: false, 
                                        excludes: '', 
                                        execCommand: '', 
                                        execTimeout: 120000, 
                                        flatten: false, 
                                        makeEmptyDirs: false, 
                                        noDefaultExcludes: false, 
                                        patternSeparator: '[, ]+', 
                                        remoteDirectory: 'lms-swt-fdx/', 
                                        remoteDirectorySDF: false, 
                                        removePrefix: 'lms-swt-client\\target\\war', 
                                        sourceFiles: 'lms-swt-client\\target\\war\\**'
                                    )
                                ],
                                usePromotionTimestamp: false, 
                                useWorkspaceInPromotion: false,
                                verbose: false
                            )
                        ]
                    )
                }
                stage ('Deploy LMS-SWT-CLIENT FDX - lx-swlb08') {
                    sshPublisher(publishers: 
                        [sshPublisherDesc
                            (configName: 'lx-swlb08.mercurio.local', 
                                transfers: 
                                [sshTransfer
                                    (cleanRemote: false, 
                                        excludes: '', 
                                        execCommand: '', 
                                        execTimeout: 120000, 
                                        flatten: false, 
                                        makeEmptyDirs: false, 
                                        noDefaultExcludes: false, 
                                        patternSeparator: '[, ]+', 
                                        remoteDirectory: 'lms-swt-fdx/', 
                                        remoteDirectorySDF: false, 
                                        removePrefix: 'lms-swt-client\\target\\war', 
                                        sourceFiles: 'lms-swt-client\\target\\war\\**'
                                    )
                                ],
                                usePromotionTimestamp: false, 
                                useWorkspaceInPromotion: false,
                                verbose: false
                            )
                        ]
                    )
                }
            }
            if (params.DeployLMSappRadar == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                stage("Deploy LMS-APP(RADAR) - lms15/lms16"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat '''
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -Pweblogic-pd-lms-radar -Ddeploy.admin.user=%WEBLOGIC_USER% -Ddeploy.admin.password=%WEBLOGIC_PASSWORD% -Ddeploy=true
                            '''
                        }
                    }
                }
            }
            if (params.DeployLMSappReajuste == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                stage("Deploy LMS-APP(Reajuste) - lms21/lms22"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat '''
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -Pweblogic-pd-lms-reajuste -Ddeploy.admin.user=%WEBLOGIC_USER% -Ddeploy.admin.password=%WEBLOGIC_PASSWORD% -Ddeploy=true
                            '''
                        }
                    }
                }
            }
            if (params.DeployLMSappBatch == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                stage("Deploy LMS-APP(Batch) - lms23/lms24"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat '''
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -Pweblogic-pd-lms-batch -Ddeploy.admin.user=%WEBLOGIC_USER% -Ddeploy.admin.password=%WEBLOGIC_PASSWORD% -Ddeploy=true
                            '''
                        }
                    }
                }
            }
            if (params.DeployLMSappMobilescan == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                if (cluster == 'Odd') {
                    profile = 'weblogic-pd-lms-mobilescan-01'
                    vcluster = 'Cluster Impar - lms05/lms07'
                }
                if (cluster == 'Even') {
                    profile = 'weblogic-pd-lms-mobilescan-02'
                    vcluster = 'Cluster Par - lms06/lms08'
                }
                stage("Deploy LMS-APP(Mobilescan) - ${vcluster}"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat """
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -P${profile} -Ddeploy.admin.user=${WEBLOGIC_USER} -Ddeploy.admin.password=${WEBLOGIC_PASSWORD} -Ddeploy=true
                            """
                        }
                    }
                }
            }
            if (params.DeployLMSappDebug == true) {
                lmsEnvironment = LMS_ENVIRONMENT.toUpperCase()
                stage("Deploy LMS-APP(Debug) - lms30"){
                    wrap([$class: "MaskPasswordsBuildWrapper", varPasswordPairs: [[password: WEBLOGIC_PASSWORD]]]) {
                        //echo "Password: ${WEBLOGIC_PASSWORD}"
                        withMaven(jdk: "${jdk8}", maven: "${maven3}") {
                            bat '''
                                mvn -f lms-app/lms-ear/pom.xml clean integration-test -Pweblogic-pd-lms-debug -Ddeploy.admin.user=%WEBLOGIC_USER% -Ddeploy.admin.password=%WEBLOGIC_PASSWORD% -Ddeploy=true
                            '''
                        }
                    }
                }
            }
        }
    }
}
