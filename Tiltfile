# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the image - using env parameter for cross-platform compatibility
    command='gradlew bootBuildImage --imageName %EXPECTED_REF%',
    # files to watch for changes
    deps=['build.gradle.kts', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml', 'k8s/configmap.yml'])

# Manage
k8s_resource(
    'catalog-service',
    port_forwards=['9001']
)