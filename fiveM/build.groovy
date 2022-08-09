node {
    def app
    stage('Clone repository') {
        sh 'mkdir /tmp/${BUILD_TAG}'
        sh 'git clone -b ${branch} https://github.com/Canderson8495/fivem-1.git /tmp/${BUILD_TAG}'
    }

    stage('Build image') {
        /* Build your image */
        app = docker.build("fivem", "/tmp/${BUILD_TAG}")
    }

    stage('Push image') {
        /* Push image using withRegistry. */
        docker.withRegistry('http://localhost:5000') {
            app.push("${branch}")
        }
    }
}

