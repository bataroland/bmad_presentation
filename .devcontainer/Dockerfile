FROM mcr.microsoft.com/devcontainers/java:25-bookworm

# Install Maven, Node.js 22 (for Claude Code CLI), and GitHub CLI
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - \
    && mkdir -p -m 755 /etc/apt/keyrings \
    && wget -qO- https://cli.github.com/packages/githubcli-archive-keyring.gpg \
       | tee /etc/apt/keyrings/githubcli-archive-keyring.gpg > /dev/null \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" \
       | tee /etc/apt/sources.list.d/github-cli.list > /dev/null \
    && apt-get update \
    && apt-get install -y --no-install-recommends nodejs podman fuse-overlayfs gh maven socat \
    && npm install -g @anthropic-ai/claude-code \
    && rm -rf /var/lib/apt/lists/*

# Install Docker 27 for Docker-in-Docker (Paketo builds require real Docker daemon)
RUN curl -fsSL https://download.docker.com/linux/static/stable/x86_64/docker-27.5.1.tgz \
    | tar xz --strip-components=1 -C /usr/local/bin

# Configure Podman to use fuse-overlayfs (kernel overlay not available in devcontainers)
RUN mkdir -p /etc/containers /run/containers/storage /var/lib/containers/storage \
    && printf '[storage]\ndriver = "overlay"\nrunroot = "/run/containers/storage"\ngraphroot = "/var/lib/containers/storage"\n\n[storage.options.overlay]\nmount_program = "/usr/bin/fuse-overlayfs"\n' > /etc/containers/storage.conf

# Install Java 8 via SDKMAN for running the original (pre-migration) version side by side
# Install Java 8 via SDKMAN (for running original pre-migration version side by side)
# Java 25 is already installed at /usr/lib/jvm/msopenjdk-current (MS OpenJDK, not via SDKMAN)
RUN bash -c "source /usr/local/sdkman/bin/sdkman-init.sh && sdk install java 8.0.482-tem && sdk install java 25.0.2-tem"

ENV JAVA_HOME=/usr/lib/jvm/msopenjdk-current
ENV PATH="${JAVA_HOME}/bin:${PATH}"
