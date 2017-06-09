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

### VCS Plugin

#### Introduction
This plugin creates an extension `project.vcs` with type `tane.mahuta.gradle.plugin.vcs.VcsExtension`.
You are able to access your VCS via the extension and 

#### Usage
Apply the plugin and configure it. The following code shows the defaults: 
```
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
`tane.mahuta.buildtools.vcs.VcsAccessorFactory` and `tane.mahuta.buildtools.vcs.VcsAccessor`.

To expose the class through SPI (via `java.util.ServiceLoader`), a text file 
`META-INF/services/tane.mahuta.buildtools.vcs.VcsAccessorFactory` must be on the classpath which contains one line with the fully qualified classname per implementation of the factory.

The plugin's file is to be found [here](plugins/gradle-vcs-plugin/src/main/resources/META-INF/services/tane.mahuta.gradle.plugin.vcs.VcsAccessorFactory).

 