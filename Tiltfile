# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # command to build the image
    command='./gradlew bootBuildImage --imageName $EXPECTED_REF',
    # files to watch for changes
    deps=['build.gradle.kts', 'src']
)

# Deploy
k8s_yaml(['k8s/deployment.yaml', 'k8s/service.yaml'])

# Manage
k8s_resource(
    'catalog-service',
    port_forwards=['9001']
)