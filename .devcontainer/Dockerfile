FROM mcr.microsoft.com/devcontainers/java:8-bookworm

# Install Maven, Node.js 22 (for Claude Code CLI), Tomcat 9, and GitHub CLI
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - \
    && mkdir -p -m 755 /etc/apt/keyrings \
    && wget -qO- https://cli.github.com/packages/githubcli-archive-keyring.gpg \
       | tee /etc/apt/keyrings/githubcli-archive-keyring.gpg > /dev/null \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" \
       | tee /etc/apt/sources.list.d/github-cli.list > /dev/null \
    && apt-get update \
    && apt-get install -y --no-install-recommends nodejs podman gh maven \
    && npm install -g @anthropic-ai/claude-code \
    # Install Tomcat 9.0 for WAR deployment
    && wget -qO- https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.98/bin/apache-tomcat-9.0.98.tar.gz | tar xz -C /opt \
    && mv /opt/apache-tomcat-9.0.98 /opt/tomcat \
    && chmod +x /opt/tomcat/bin/*.sh \
    && rm -rf /var/lib/apt/lists/*

# Ensure SDKMAN Java 8 is on PATH for root user
ENV CATALINA_HOME=/opt/tomcat
ENV JAVA_HOME=/usr/local/sdkman/candidates/java/current
ENV PATH="${CATALINA_HOME}/bin:${JAVA_HOME}/bin:${PATH}"
