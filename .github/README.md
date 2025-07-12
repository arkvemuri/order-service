# GitHub Actions Workflows

This repository uses GitHub Actions for CI/CD automation. Below are the available workflows:

## 🔄 CI/CD Pipeline (`ci-cd.yml`)

**Triggers:** Push to `master`/`main` branch

**Jobs:**
1. **Test and Code Quality** - Runs tests, generates coverage reports
2. **SonarQube Analysis** - Code quality and security analysis
3. **Build and Push** - Builds and pushes Docker image to Docker Hub
4. **Update GitOps** - Updates deployment manifests in GitOps repository
5. **Security Scan** - Vulnerability scanning with Trivy
6. **Deploy Staging** - Deploys to staging environment
7. **Deploy Production** - Deploys to production environment (manual approval)
8. **Notify** - Sends notifications on success/failure

## 🔍 Pull Request Checks (`pr-check.yml`)

**Triggers:** Pull requests to `master`/`main` branch

**Features:**
- Validates Maven project structure
- Runs unit tests and coverage analysis
- Builds Docker image (without pushing)
- Posts validation results as PR comments

## 🔒 Security and Dependency Checks (`security.yml`)

**Triggers:** 
- Weekly schedule (Mondays at 2 AM)
- Push to main branches
- Pull requests

**Scans:**
- OWASP dependency vulnerability check
- CodeQL security analysis
- Secret scanning with TruffleHog
- License compliance checking

## 🚀 Release Workflow (`release.yml`)

**Triggers:** Push of version tags (e.g., `v1.0.0`)

**Process:**
1. Creates GitHub release with changelog
2. Builds and pushes versioned Docker images
3. Updates GitOps repository for production deployment
4. Creates PR for production deployment approval

## 📋 Required Secrets

Configure these secrets in your GitHub repository settings:

### Docker Hub
- `DOCKERHUB_USERNAME` - Docker Hub username
- `DOCKERHUB_TOKEN` - Docker Hub access token

### SonarQube
- `SONAR_TOKEN` - SonarQube authentication token
- `SONAR_HOST_URL` - SonarQube server URL

### GitOps Repository
- `GITOPS_TOKEN` - GitHub token with access to deployment repository

### Optional Notifications
- `SLACK_WEBHOOK` - Slack webhook URL for notifications
- `TEAMS_WEBHOOK` - Microsoft Teams webhook URL

## 🏗️ Environments

The workflows use GitHub Environments for deployment approvals:

- **staging** - Automatic deployment after successful build
- **production** - Manual approval required

Configure these in your repository settings under "Environments".

## 📊 Workflow Status Badges

Add these badges to your main README.md:

```markdown
![CI/CD](https://github.com/arkvemuri/order-service/workflows/CI/CD%20Pipeline/badge.svg)
![Security](https://github.com/arkvemuri/order-service/workflows/Security%20and%20Dependency%20Checks/badge.svg)
```

## 🔧 Customization

### Modifying Java Version
Update the `JAVA_VERSION` environment variable in each workflow file.

### Changing Docker Registry
Update the `DOCKER_IMAGE` environment variable to use a different registry.

### Adding Deployment Steps
Modify the deployment jobs in `ci-cd.yml` to include your specific deployment commands (kubectl, helm, etc.).

### Notification Integration
Add notification steps in the `notify` jobs to integrate with your preferred communication tools.

## 🐛 Troubleshooting

### Common Issues

1. **Docker Hub Authentication Fails**
   - Verify `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` secrets
   - Ensure the token has push permissions

2. **SonarQube Analysis Fails**
   - Check `SONAR_TOKEN` and `SONAR_HOST_URL` configuration
   - Verify SonarQube server accessibility

3. **GitOps Update Fails**
   - Ensure `GITOPS_TOKEN` has write access to deployment repository
   - Verify repository and file paths in the workflow

4. **Tests Fail**
   - Check test dependencies and configuration
   - Verify Java version compatibility

### Debugging Workflows

1. Enable debug logging by setting repository secret `ACTIONS_STEP_DEBUG` to `true`
2. Check workflow logs in the Actions tab
3. Use `echo` statements to debug variable values
4. Test workflows on feature branches before merging

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Hub Integration](https://docs.docker.com/ci-cd/github-actions/)
- [SonarQube GitHub Integration](https://docs.sonarqube.org/latest/analysis/github-integration/)
- [GitOps Best Practices](https://www.gitops.tech/)