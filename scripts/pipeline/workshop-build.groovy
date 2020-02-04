pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes'
      //defaultContainer 'kaniko'
      //defaultContainer 'jnlp'
      yaml """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug-v0.16.0
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: docker-config
        mountPath: /kaniko/.docker
  restartPolicy: Never
  volumes:
    - name: docker-config
      configMap:
        name: docker-config
"""
    }
  }
  stages {
    stage('Clone Authors Repo') {
        steps{
            git 'https://github.com/robinfoe/kube-authors.git'
        }
    }
    stage('Build and Push') {
      steps {
        container("kaniko"){
            sh '/kaniko/executor -f `pwd`/frontend/Dockerfile -c `pwd` --insecure --skip-tls-verify --destination=robinfoe/kubeworkshop:$BUILD_NUMBER --verbosity=debug'
        }
       }
    }
  }
}
