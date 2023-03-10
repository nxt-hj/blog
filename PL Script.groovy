node {
    def IP
    stage('同步代码') {
        // Get some code from a GitHub repository
        checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/$branchName']], extensions: [[$class: 'PruneStaleBranch']], userRemoteConfigs: [[credentialsId: '0a03e51e-969b-41a2-9393-63005529bfc', url: 'http://xx.com/xx/xx.git']]])

        echo params.branchName
    }
    stage('准备环境') {
        // Run the maven build
        if (isUnix()) {
            sh 'npm i'
        }
        if(params.env=='test'){
            IP = '192.168.1.37'
        }
        if(params.env=='test1'){
            IP = '192.168.1.57'
        }
    }
    stage('获取接口配置') {
        if (isUnix()) {
            sh 'npm run gs $env $platform'
        }
    }
    stage('同步静态资源'){
        echo IP
        withEnv(["IP=$IP"]) {
            sh 'rsync -avrz src/platform/$platform/static/xxx/ root@${IP}:/var/www/xxx/web_$platform/xx/'
        }
    }
    stage('构建打包') {
        echo params.platform
        sh 'npm run build $platform'
    }
    stage('上传生产文件'){
        sshPublisher(
            publishers: [
                sshPublisherDesc(
                    configName: IP, 
                    transfers: [
                        sshTransfer(cleanRemote: false, excludes: '', execCommand: 
                        '''#! /bin/bash
                            shopt -s extglob 
                            cd /var/www/xxx/web_$platform 
                            rm -rf !(_@server_resource)''', 
                            execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'dist/'),
                        sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/var/www/oms_huangjian/web_$platform', remoteDirectorySDF: false, removePrefix: 'dist/0.1.1', sourceFiles: 'dist/0.1.1/**')
                    ], 
                    usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false
                )
            ]   
        )
    }
    stage('反馈结果'){
        echo params.remoteUrl
        httpRequest responseHandle: 'NONE', url: "http://${params.remoteUrl}:8087?result=${currentBuild.currentResult}", wrapAsMultipart: false
    }
}
