# 🔐 GitHub Secrets Configuration Guide

This document explains how to configure the required and optional secrets for the CI/CD pipelines.

## 🚀 Required Secrets

These secrets are **mandatory** for the basic CI/CD pipeline to work:

### Docker Hub (Required for Docker builds)
- `DOCKERHUB_USERNAME` - Your Docker Hub username
- `DOCKERHUB_TOKEN` - Docker Hub access token (not password!)

**How to get Docker Hub token:**
1. Go to [Docker Hub](https://hub.docker.com/)
2. Login → Account Settings → Security → New Access Token
3. Copy the generated token

## 🔧 Optional Secrets

These secrets are **optional** - the pipeline will skip related steps if not configured:

### SonarQube/SonarCloud (Code Quality Analysis)
- `SONAR_TOKEN` - SonarQube authentication token
- `SONAR_HOST_URL` - SonarQube server URL (e.g., `https://sonarcloud.io`)

**How to get SonarCloud token:**
1. Go to [SonarCloud](https://sonarcloud.io/)
2. Login → My Account → Security → Generate Token
3. Set `SONAR_HOST_URL` to `https://sonarcloud.io`

### Codecov (Code Coverage Reports)
- `CODECOV_TOKEN` - Codecov.io authentication token

**How to get Codecov token:**
1. Go to [Codecov](https://codecov.io/)
2. Login with GitHub → Add repository
3. Copy the repository token

### GitOps Repository (Automated Deployments)
- `GITOPS_TOKEN` - GitHub Personal Access Token with access to deployment repository

**How to create GitHub PAT:**
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token with `repo` scope
3. Copy the token

## 📝 How to Add Secrets to GitHub Repository

1. Go to your GitHub repository
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add the secret name and value
5. Click **Add secret**

## ⚠️ What Happens Without Optional Secrets?

- **No SONAR_TOKEN/SONAR_HOST_URL**: SonarQube analysis step will be skipped with a warning message
- **No CODECOV_TOKEN**: Code coverage upload to Codecov will be skipped
- **No GITOPS_TOKEN**: GitOps repository updates will be skipped with a warning message

## ✅ Pipeline Behavior

The CI/CD pipeline is designed to be **graceful** - it will:
- ✅ Continue running even if optional secrets are missing
- ⚠️ Show helpful warning messages for missing optional configurations
- ❌ Fail only if required secrets (Docker Hub) are missing

## 🔍 Verification

After adding secrets, you can verify they work by:
1. Pushing a commit to trigger the pipeline
2. Checking the GitHub Actions logs for any warnings about missing secrets
3. Confirming that optional steps run when their secrets are configured

## 📚 Additional Resources

- [GitHub Secrets Documentation](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Docker Hub Access Tokens](https://docs.docker.com/docker-hub/access-tokens/)
- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [Codecov Documentation](https://docs.codecov.com/)