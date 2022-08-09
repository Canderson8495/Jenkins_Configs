node {
    def app
    stage('Clone repository') {
        sh 'mkdir -p ${BUILD_TAG}'
        sh 'git clone -b ${branch} https://github.com/Canderson8495/fivem-1.git /tmp/${BUILD_TAG}'
    }

    stage('Build image') {
        /* Build your image */
        app = docker.build("fivem", "/tmp/build-${BUILD_NUMBER}")
    }

    stage('Push image') {
        /* Push image using withRegistry. */
        docker.withRegistry('http://localhost:5000') {
            app.push("${branch}")
        }
    }

    stage("Prepare"){
        try {
            sh 'docker-compose -p Cluster_1 stop'
            sh 'docker-compose -p Cluster_1 rm -f'
            echo "Removed old containers"
        } catch (Exception e) {
            echo 'Returned: ' + e.toString()
        }
    }

    stage('Pull Image') {
        sh 'docker pull localhost:5000/fivem:${branch}'
    }
    
    stage('deploy') {
        //sh 'docker run -d --name C1 localhost:9999/fivem:${branch}'
        sh 'git clone https://github.com/Canderson8495/fivem-1.git /tmp/deploy-${BUILD_NUMBER}'
        sh 'ls /tmp/deploy-${BUILD_NUMBER}'
        sh 'cd /tmp/deploy-${BUILD_NUMBER} && sudo docker-compose -p Cluster_1 up -d'
        sh "docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' fivem"
    }
    
}

