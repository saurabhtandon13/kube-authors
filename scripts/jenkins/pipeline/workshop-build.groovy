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
  - name: kubectl
    image: gcr.io/cloud-builders/kubectl
    command:
    - cat
    tty: true
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
            git 'https://github.com/saurabhtandon13/kube-authors.git'
        }
    }
    stage('Build and Push') {
      steps {
        container("kaniko"){
            sh '/kaniko/executor -f `pwd`/frontend/Dockerfile -c `pwd` --insecure --skip-tls-verify --destination=connect.kubelab.com:8433/kubeworkshop:$BUILD_NUMBER --verbosity=debug'
        }
       }
    }

    stage('Deploy Smoke Green'){
        steps{
            // patching green zone
            // sh """kubectl patch svc  "${PROD_BLUE_SERVICE}" -p '{\"spec\":{\"selector\":{\"app\":\"taxicab\",\"version\":\"${BUILD_NUMBER}\"}}}'"""
           container('kubectl'){
            sh """
                kubectl patch deployment authors-green -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"kubeworkshop\",\"image\":\"connect.kubelab.com:8433/kubeworkshop:${BUILD_NUMBER}\"}]}}}}'
                kubectl scale --replicas=1 deployment authors-green
                kubectl wait --for=condition=ready pod -l app=authors-green --timeout=60s
            """
           }
        }
        
    }


    stage('Smoke Test'){
        steps{
            input 'Proceed to Production ?'
        }
    }

    stage('Deploy to Prod') {
      
      steps {
          container('kubectl'){
        sh """
                kubectl patch deployment authors-blue -p '{\"spec\":{\"template\":{\"spec\":{\"containers\":[{\"name\":\"kubeworkshop\",\"image\":\"connect.kubelab.com:8433/kubeworkshop:${BUILD_NUMBER}\"}]}}}}'
                kubectl scale --replicas=1 deployment authors-blue
                kubectl wait --for=condition=ready pod -l app=authors-blue --timeout=60s
                kubectl scale --replicas=0 deployment authors-green
            """
          }
      }
    }


  }
}
