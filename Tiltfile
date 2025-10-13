# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the image - using env parameter for cross-platform compatibility
    command='gradlew jibDockerBuild --image %EXPECTED_REF%',
    # files to watch for changes
    deps=['build.gradle.kts', 'src']
)

# Deploy
k8s_yaml(kustomize('k8s'))

# Manage
k8s_resource(
    'catalog-service',
    port_forwards=['9001']
)