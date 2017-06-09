# gradle-vgfr-plugin

## Build status

[![Build Status](https://travis-ci.org/Tanemahuta/gradle-vgfr-plugin.svg?branch=development)](https://travis-ci.org/Tanemahuta/gradle-vgfr-plugin)
[![Code Coverage](https://img.shields.io/codecov/c/github/Tanemahuta/gradle-vgfr-plugin/development.svg)](https://codecov.io/github/Tanemahuta/gradle-vgfr-plugin?branch=development)

## Description
A gradle plugin collection for using 
- the semantic version scheme (in combination with feature branches)
- git flow and a version scheme for releasing 

## Plugins

### General
The plugin code is based on git and the semantic version scheme. 

Though this should be sufficient, you are able to extend the functionality by implementing services or using various extension points.
 
Most of the extension points use SPI via `java.util.ServiceLoader`, which means you have to define 
`META-INF/services/<fully qualified service interface>` using a line containing the fully qualified class name of the 
implementations of the services you want to provide.


### VCS Plugin

#### Introduction
This plugin creates an extension `project.vcs` with type `tane.mahuta.gradle.plugin.vcs.VcsExtension`.
You are able to access your VCS via the extension and 

#### Usage
Apply the plugin and configure it. The following code shows the defaults: 
```groovy
project.apply plugin: 'tane.mahuta.gradle.vcs-plugin'

project.vcs {
    
    // Read only properties
    branch      // current checked out branch
    revisionId  // revision id
    
    flowConfig {
    
        // branch names for productive and
        productionBranch = 'master'
        developmentBranch = 'develop'
        
        // branch prefixes for feature, support, hotfix and release branches
        featureBranchPrefix = 'feature/'
        supportBranchPrefix = 'support/'
        hotfixBranchPrefix = 'hotfix/'
        releaseBranchPrefix = 'release/'
        
        // tag prefix for release tags
        versionTagPrefix = 'version/'
    }

}
```
#### Extension points
At the moment only git is supported through the jgit-core library by atlassian (which uses eclipse jgit).

If you need to add an implementation for another VCS, you may write your own class implementing 
`tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory` which returns an implemenation of `tane.mahuta.buildtools.vcs.VcsAccessor` for a `project`.

To provide further implementation, create a file `META-INF/services/tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory` which exposes
your custom implementation.

The plugin's file is to be found [here](plugins/gradle-vcs-plugin/src/main/resources/META-INF/services/tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory).

### Version Plugin

#### Introduction
This plugin enhances gradle's capabilities of `project.version` setting it to a `tane.mahuta.gradle.plugin.version.VersionExtension`.

It override's gradle's `project.version` which and tampers with `project.metaClass.setVersion` in the way that setting is version is delegated to the extension.

#### Usage
Usage is simple, just apply the plugin:
```groovy
apply plugin: 'tane.mahuta.gradle.version-plugin'
```

#### Extension points

##### Version storage
A version storage provides the capabilities to `VersionStorage.load()` and `VersionStorage.store(<newversion>)` versions
enduring the end of the build process (e.g. for releases).

These storages are created using `tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory` which creates a 
`tane.mahuta.buildtools.version.VersionStorage` for a `project`.

By default the following storage factories are available:
- `tane.mahuta.gradle.plugin.version.storage.GradlePropertiesProjectVersionStorageFactory`: uses the `version` property from `gradle.properties` to load and store the version. 

To provide an implementation, create a file `META-INF/services/tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory` which exposes
your custom implementation.

The plugin's file is to be found [here](plugins/gradle-vcs-plugin/src/main/resources/META-INF/services/tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory). 


##### Version parser
Version parsers transform stored version (`java.lang.String`, preferably) to a domain object to facilitate usage (e.g. a `SemanticVersion`).

You can write your own version parsers implementing the interface `tane.mahuta.buildtools.version.VersionParser` and set it via
`version.parser = new MyOwnVersionParser()`.
 
The parsed `tane.mahuta.buildtools.version.Version` defines a method `toStorable()` which converts the domain object into 
an object (`java.lang.String`, preferably) which can be stored using the version storage (you could call that unparsing).

##### Transformers
It is often necessary to define transformations for the versions, e.g. for transforming a version to a release version.
You may define any transformation on `project.version` by simply adding a property which is a closure 
(as known by meta class extension in groovy).
 
Examples:
```groovy
// Precondition
version = "1.2.3-SNAPSHOT"
// Define a transformation
version.toRelease = { v -> v.replaceAll(/-SNAPSHOT$/, '') }
// Use the transformation
assert version.toRelease() == "1.2.3"
```

The release plugin uses some of the transformations create new versions in the
release process. The names of the transformations are listed in the plugin's chapter.

### Semantic version plugin

#### Introduction
The semantic version plugin applies the version plugin and
- adds a `VersionParser` which parses the `project`'s version to a `tane.mahuta.buildtools.version.SemanticVersion`
- defines the transformations for the release plugin which apply to the parsed `SemanticVersion`s

#### Usage
Make sure your `project`'s version fits to the [SemVer Scheme](http://www.semver.org).
If this precondition is satisfied you can use the plugin as follows:

```groovy
// Precondition
version = '1.2.3-SNAPSHOT' 

apply plugin: 'tane.mahuta.gradle.semver-plugin'

// Access the version parts (readonly) using
assert version.major == 1 
assert version.minor == 2 
assert version.micro == 3
assert version.qualifier == 'SNAPSHOT'
```

### Semantic branch version plugin

#### Introduction
In continuous integration it is often necessary to use qualifiers for versions from different branches.

**Example**:

You are working on *feature/xy* while the development of the version `1.2.3` (`1.2.3-SNAPSHOT` continues in *development*.
There are projects which integrate the currently developed code, because they depend on it.
Whenever your CI/CD deploys/installs the artifacts now, you are never sure if `1.2.3-SNAPSHOT` contains the 
feature *xy* code or not. 

One solution is to manually add a qualifier `xy` to `1.2.3-SNAPSHOT`, so the
version for the feature `xy` now is `1.2.3-xy-SNAPSHOT`.

And of course, you could do that manually after creating the branch.
But this will result in a merge conflict when merging to *development*.

Semantic branch version plugin to the rescue!

This plugin applies the semantic version plugin and the vcs plugin and
- decorates the parser creating a `tane.mahuta.buildtools.version.SemanticBranchVersion`
- decorates the transformers creating a `SemanticBranchVersion` from the `SemanticVersion`

#### Usage
```groovy
apply plugin: 'tane.mahuta.gradle.semver-branch-plugin'

assert version.branchQualifier == null // on most branches
```

#### Branch qualifiers
The branch qualifiers use the VCS plugin's flow configuration to create a branch qualifier as follows:
- the following branches do not create a branch qualifier
  - branch equals `vcs.flowConfig.productionBranch` or `vcs.flowConfig.developmentBranch`
  - branch starts with `vcs.flowConfig.releaseBranchPrefix` or `vcs.flowConfig.hotfixBranchPrefix`
- branches starting with any of the following prefixes create a branch qualifier for which the prefix is removed:
  - `vcs.flowConfig.featureBranchPrefix`
  - `vcs.flowConfig.supportBranchPrefix`
- branch qualifiers are normalized replacing all non letter/number characters with a `_`

**Examples (using default gitflow config):**
- on *development* with version `1.2.3-SNAPSHOT` results in `1.2.3-SNAPSHOT`
- on *feature/xy* with version `1.2.3-SNAPSHOT` results in `1.2.3-xy-SNAPSHOT` 
- on *support/ab* with version `1.2.3-SNAPSHOT` results in `1.2.3-ab-SNAPSHOT`
- on *master* with version `1.2.3` results in `1.2.3`
- on *release/1.3.0* with version `1.3.0` results in `1.3.0`
- on *hotfix/1.2.4* with version `1.2.4` results in `1.2.4`
