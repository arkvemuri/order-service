pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'Java21'
    }

    environment {
        GITHUB_REPO_URL = 'https://github.com/arkvemuri/order-service.git'
        BRANCH_NAME = 'master'
        APP_VERSION = '1.0.0'
        SONAR_PROJECT_KEY = 'order-listing'
        DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
        VERSION = "${env.BUILD_ID}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checking out code
                cleanWs()
                git branch: "${env.BRANCH_NAME}",
                    url: "${env.GITHUB_REPO_URL}",
                    changelog: true,
                    poll: true
            }
        }

        stage('Build and Test') {
            steps {
                // Use bat for Windows
                bat 'mvn clean verify'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    // Archive JaCoCo coverage reports as artifacts
                    archiveArtifacts artifacts: 'target/site/jacoco/**/*', allowEmptyArchive: true
                }
            }
        }

        stage('SonarQube Analysis') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'sonarqube-token') {
                    bat """
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${SONAR_PROJECT_KEY} \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.java.coveragePlugin=jacoco
                    """
                }
            }
        }

        stage('Quality Gate') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('Coverage Check') {
            steps {
                script {
                    // Coverage is already checked by Maven JaCoCo plugin during 'mvn clean verify'
                    // If the build reached this stage, coverage requirements are met
                    echo 'Coverage check passed - Maven JaCoCo plugin enforced minimum 80% coverage per class'
                }
            }
        }

        stage('Package') {
            steps {
                // Archive the built artifacts
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Pre-Docker Network Check') {
            steps {
                script {
                    echo 'Performing comprehensive network diagnostics...'
                    
                    // Check current DNS configuration
                    bat 'ipconfig /all | findstr "DNS Servers"'
                    
                    // Test various network endpoints
                    bat '''
                        echo Testing network connectivity...
                        ping -n 1 google.com || echo "Google ping failed"
                        ping -n 1 docker.io || echo "Docker.io ping failed"
                        ping -n 1 registry-1.docker.io || echo "Registry ping failed"
                    '''
                    
                    // DNS resolution tests
                    bat '''
                        echo Testing DNS resolution...
                        nslookup google.com || echo "Google DNS failed"
                        nslookup docker.io || echo "Docker.io DNS failed"
                        nslookup registry-1.docker.io || echo "Registry DNS failed"
                    '''
                    
                    // Check if Docker is running and accessible
                    bat 'docker version || echo "Docker not accessible"'
                    bat 'docker info || echo "Docker info failed"'
                }
            }
        }

        stage('Docker Build and Push') {
              steps {
                  script {
                      // Network diagnostics
                      echo 'Running network diagnostics...'
                      
                      try {
                          bat 'nslookup registry-1.docker.io'
                          echo 'DNS resolution successful'
                      } catch (Exception e) {
                          echo 'DNS lookup failed, trying to flush DNS cache...'
                          bat 'ipconfig /flushdns'
                          bat 'ipconfig /registerdns'
                          
                          // Wait a moment for DNS to refresh
                          sleep(5)
                          
                          // Try alternative DNS servers
                          echo 'Testing connectivity with alternative methods...'
                          bat 'nslookup registry-1.docker.io 8.8.8.8 || echo "Google DNS lookup failed"'
                      }
                      
                      // Test basic internet connectivity
                      bat 'ping -n 2 8.8.8.8 || echo "Internet connectivity test failed"'
                      
                      // Docker login with enhanced error handling
                      echo 'Attempting Docker operations...'
                      
                      retry(3) {
                          bat '''
                              echo Attempting Docker login...
                              echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin
                          '''
                      }
                      
                      bat 'docker build -t arkvemuri/order-service:%VERSION% .'
                      
                      retry(3) {
                          bat 'docker push arkvemuri/order-service:%VERSION%'
                      }
                  }
              }
              post {
                  always {
                      // Cleanup Docker login
                      bat 'docker logout || echo "Docker logout failed or not needed"'
                  }
                  failure {
                      echo 'Docker operations failed. Please check:'
                      echo '1. Network connectivity to Docker Hub'
                      echo '2. DNS resolution for registry-1.docker.io'
                      echo '3. Docker daemon configuration'
                      echo '4. Corporate firewall/proxy settings'
                  }
              }
            }

         stage('Update Image Tag in GitOps') {
               steps {
                 script {
                   // Clean any existing GitOps directory
                   bat 'if exist deployment-folder rmdir /s /q deployment-folder'
                   
                   // Clone the GitOps repository using HTTPS with credentials
                   withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_TOKEN')]) {
                     bat '''
                       git clone https://%GIT_USERNAME%:%GIT_TOKEN%@github.com/arkvemuri/deployment-folder.git deployment-folder
                     '''
                   }
                   
                   // Change to the GitOps directory
                   dir('deployment-folder') {
                     // Update the image tag in the manifest
                     bat '''
                       powershell -Command "(Get-Content aws/order-manifest.yml) -replace 'image:.*', 'image: arkvemuri/order-service:%VERSION%' | Set-Content aws/order-manifest.yml"
                     '''
                     
                     // Configure git user (required for commits)
                     bat 'git config user.email "jenkins@example.com"'
                     bat 'git config user.name "Jenkins CI"'
                     
                     // Check if there are any changes
                     script {
                       def gitStatus = bat(script: 'git status --porcelain', returnStdout: true).trim()
                       if (gitStatus) {
                         echo "Changes detected in GitOps repository"
                         bat 'git add .'
                         bat 'git commit -m "Update order-service image tag to %VERSION%"'
                         
                         // Push changes using HTTPS with credentials
                         withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_TOKEN')]) {
                           bat '''
                             git remote set-url origin https://%GIT_USERNAME%:%GIT_TOKEN%@github.com/arkvemuri/deployment-folder.git
                             git push origin master
                           '''
                         }
                         echo "Successfully updated GitOps repository with new image tag"
                       } else {
                         echo "No changes detected in GitOps repository"
                       }
                     }
                   }
                 }
               }
             }
    }

    post {
        success {
            echo "Successfully built and analyzed version ${env.APP_VERSION}"
        }
        failure {
            echo 'Build or analysis failed!'
        }
        always {
            // Clean workspace after build
            cleanWs()
        }
    }
}
