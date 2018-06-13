# gradle-vgfr-plugin

## Build status

[![Build Status](https://travis-ci.org/Tanemahuta/gradle-vgfr-plugin.svg?branch=development)](https://travis-ci.org/Tanemahuta/gradle-vgfr-plugin)
[![Code Coverage](https://img.shields.io/codecov/c/github/Tanemahuta/gradle-vgfr-plugin/development.svg)](https://codecov.io/github/Tanemahuta/gradle-vgfr-plugin?branch=development)

## Introduction
Whenever you are developing and releasing software, you have to deal certain questions. 
E.g.
 - issueing checks on the artifacts to be released
 - tagging the release in your Version Control System
 - using the correct release version
For versioning the [semantic versioning schem](https://www.semver.org) has become the quasi-standard.
The branch model [git-flow](https://danielkummer.github.io/git-flow-cheatsheet/) has become the model of choice in many projects when it comes to maintaining source code streaming.
I decided to write a release plugin which behaves pretty much like the maven release plugin using git-flow and semantic versioning for automatic releasing.

## Release plugin

### Usage
In your root project have a `gradle.properties` ready which provides the current version and will be maintained by the plugin:
```properties
version=1.0.0-SNAPSHOT
```
#### Gradle >=2.1
In your root `build.gradle` apply the release plugin and the semantic branch version plugin, the repository is necessary, because some modules of the project are referenced.
```groovy
buildscript {
    repositories {
        maven { url "http://dl.bintray.com/tanemahuta/gradle-plugins" }    
    }
}
plugins {    
    id 'tane.mahuta.gradle.release-plugin' version 'latest.release'
    id 'tane.mahuta.gradle.semver-branch-plugin' version 'latest.release'
}
```
#### Gradle <2.1
In your root `build.gradle` configure the repository and add the plugin jar to the class path, then apply the release plugin and the semantic branch version plugin
```groovy
buildscript {
    repositories { 
        maven { url "http://dl.bintray.com/tanemahuta/gradle-plugins" } 
    }
    dependencies {
        classpath "tane.mahuta.build:gradle-plugin-release:latest.release"
    }
}
apply plugin: 'tane.mahuta.gradle.release-plugin'
apply plugin: 'tane.mahuta.gradle.semver-branch-plugin'
```
#### Necessary additional configuration
For analysing the release and comparing the release versions, the buildscripts need to be configured the way that they also have access to the target repository for dependency resolution. Thus your configuration should match the following:
```groovy
allprojects {    
    apply plugin: 'maven'
    repositories {
        maven { url = "<your maven repository url>" }    
    }
    uploadArchives {        
        repositories {
            mavenDeployer {
                repository(url: "<your maven repository url>")            
            }        
        }    
    }
}
```
### Checking the release
If you want to check your release prior to running it, you may issue a `./gradlew releaseCheck`.
This will check the following:
 - issue the `check` tasks  on each project
 - there are uncommitted changes
 - you are on a releasable branch (develop, release/..., hotfix/...)
 - any project from the root references SNAPSHOT dependencies on their compile configuration
 - any of the projects' artifacts has already been released
 - the API changes are matching the current version increment (to be release) regarding the last released version
A report with problems will be provided for each project and its artifacts.

### Releasing
Releasing is as simple as running `./gradlew release`. This will run the release check as well.
If you want to skip a check or all checks, you may use the `-x <task>` switch in gradle.
The release steps being performed are as follows:
 - issue all checks
 - if not on hotfix/... or release/..., start a release branch with git flow
 - set and store the release version, commit and push it
 - run the `check` and `uploadArchives` task
 - finish the release via git flow and push the tags
 - set and store the next development iteration on the development branch


### Configuration
If you need to override the defaults while releasing, you can configure the `release` extension in the root (or sub projects):
```groovy
release {
    releaseTasks = ['myReleaseTask1', 'myTask2']
}
```
For other configuration parts (e.g. the VCS flow), please refer to the plugin's documentation.

### Note on the API compatibility check
The API is being checked using [CLIRR](http://clirr.sourceforge.net/) which provides a report of incompatible class changes (e.g. public method signatures which have been changed). From this report, the tooling suggests the next semantic version for the release (referring to the latest release version). If the version is greater than the version to be released, the check will fail, providing the minimum version to be used for the release.
In future versions it will be possible to provide an own adapter for a report builder.

### Example project 
An example project being used for integration testing can be found [here](https://github.com/Tanemahuta/gradle-vgfr-plugin/tree/development/gradle-plugin/gradle-plugin-release/src/integrationTest/resources/baseProject).

## Plugins

### General
There's certain extension points available for gradle in each plugin. Though, some of the code is was written to
 provide an abstraction layer for other build tools (e.g. maven). 
 
Thus, most of the extension points use SPI via `java.util.ServiceLoader`, which means you have to define 
`META-INF/services/<fully qualified service interface>` using a line containing the fully qualified class name of the 
implementations of the services you want to provide.

### VCS Plugin

#### Introduction
This plugin creates an extension `project.vcs` with type `tane.mahuta.gradle.plugin.vcs.VcsExtension`.
You are able to access your VCS via the extension and change the flow configuration, as well as query the current branch and revision id.

#### Usage
Apply the plugin and configure it. The following code shows the defaults: 
```groovy
project.apply plugin: 'tane.mahuta.gradle.vcs-plugin'

project.vcs { // (git defaults are displayed)
    
    storage
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
### Extension points
At the moment only git is supported through the jgit-core library by atlassian (which uses eclipse jgit).

If you need to add an implementation for another VCS, you may write your own class implementing 
`tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory` which returns an implementation of `tane.mahuta.buildtools.vcs.VcsAccessor` for a `project`.

To provide further implementation, create a file `META-INF/services/tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory` which exposes
your custom implementation.

The plugin's file is to be found [here](plugins/gradle-vcs-plugin/src/main/resources/META-INF/services/tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory).

### Version Plugin

#### Introduction
This plugin enhances gradle's capabilities of `project.version` setting it to a `tane.mahuta.gradle.plugin.version.VersioningExtension`.

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
`VersionStorage` for a `project`.

By default the following storage factories are available:
- `GradlePropertiesProjectVersionStorageFactory`: uses the `version` property from `gradle.properties` to load and store the version. 

To provide an implementation, create a file `META-INF/services/tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory` which exposes
your custom implementation.

The plugin's file is to be found [here](plugins/gradle-vcs-plugin/src/main/resources/META-INF/services/tane.mahuta.gradle.plugin.version.ProjectVersionStorageFactory). 


##### Version parser
Version parsers transform stored version (`java.lang.String`, preferably) to a domain object to facilitate usage (e.g. a `SemanticVersion`).

You can write your own version parsers implementing the interface `VersionParser` and set it via
`version.parser = new MyOwnVersionParser()`.
 
The parsed `tane.mahuta.buildtools.version.Version` defines a method `toStorable()` which converts the domain object into 
an object (`java.lang.String`, preferably) which can be stored using the version storage (you could call that unparsing).

### Semantic version plugin

#### Introduction
The semantic version plugin applies the version plugin and
- adds a `VersionParser` which parses the `project`'s version to a `SemanticVersion`
- the `SemanticVersion` defines transformations for the release plugin which apply to the parsed `SemanticVersion`s

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
- decorates the parser creating a `SemanticBranchVersion`
- defines transformers for `SemanticBranchVersion` from the `SemanticVersion`

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
