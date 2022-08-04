# Release HowTo
1. Merge your changes into the master branch
1. Tag the resulting commit with a new SemVer conform tag
    - which tag exactly depends on your changes and the previous tag
1. Create a new commit that updates the SNAPSHOT version in the pom.xml
    - the commit message should be something like `Update version in pom for next development iteration`
1. Push everything with `git push --tags origin master`
1. Wait for Travis CI to do the rest & for the artifact to be available in Maven Central
    - you can usually directly access the artifact after 10 to 20 minutes, way before it shows up when searching on [Maven Central](https://search.maven.org/)
    - i.e. `https://search.maven.org/artifact/com.mercateo.sqs/sqs-utils/X.Y.Z/jar` where `X.Y.Z` is the version that you just released
