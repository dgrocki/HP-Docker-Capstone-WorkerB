node('docker') {
	def app
	environment {
		DOCKER_CREDS = credentials('docker-hub')
	}
	stage('Clone repository on slave') {
		/* Let's make sure we have the repository cloned to our workspace */
	checkout scm
	}

	stage('Gradle Build') {
	sh './gradlew build'
	}

	stage('Gradle Tests') {
	sh './gradlew test'
	/* Publish test results */
	publishHTML([
			 allowMissing: false, 
			 alwaysLinkToLastBuild: false, 
			 keepAll: false, 
			 reportDir: './build/reports/tests/test/', 
			 reportFiles: 'index.html', 
			 reportName: 'HTML Test Report', 
			 reportTitles: ''
	])
	}	

	stage('Build image') {
		/* This builds the actual image; synonymous to
		* docker build on the command line */
	//Check if file exists
	sh './gradlew build'
	def exists = fileExists './build/libs/workerB-all.jar'
	if (exists) {
	    echo 'The Jar Exists'
	} else {
		echo 'Can not find the jar'
		output=sh (
			script: 'ls -LR',
			returnStdout: true
		).trim()
		echo output
	}
	app = docker.build("iceberg00/hp-docker-capstone-workerb")
	}

	stage('Test image') {

	}
	stage('Push image'){
		docker.withRegistry('https://registry.hub.docker.com', 'docker-hub') {
				app.push("${env.BUILD_NUMBER}")
				app.push("latest")
		}
	}
}
